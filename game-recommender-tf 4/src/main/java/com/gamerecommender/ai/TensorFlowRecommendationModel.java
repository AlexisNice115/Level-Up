package com.gamerecommender.ai;

import com.gamerecommender.model.Game;
import com.gamerecommender.model.UserProfile;
import com.gamerecommender.data.GameDatabase;

import org.tensorflow.*;
import org.tensorflow.op.*;
import org.tensorflow.types.TFloat32;
import org.tensorflow.ndarray.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * TensorFlow-based Neural Network Recommendation Engine.
 * 
 * Architecture: Two-Tower Model
 * - Game Tower: Encodes games into embedding space
 * - User Tower: Encodes users into same embedding space
 * - Similarity: Dot product between user and game embeddings
 */
public class TensorFlowRecommendationModel {
    
    private final GameDatabase database;
    private final TensorFlowFeatureEncoder encoder;
    
    // Model hyperparameters
    private final int inputSize;
    private final int hiddenSize1 = 128;
    private final int hiddenSize2 = 64;
    private final int embeddingSize = 32;
    
    // Model weights
    private float[][] weights1;
    private float[] bias1;
    private float[][] weights2;
    private float[] bias2;
    private float[][] weights3;
    private float[] bias3;
    
    // Precomputed game embeddings
    private Map<String, float[]> gameEmbeddings;
    
    public TensorFlowRecommendationModel(GameDatabase database) {
        this.database = database;
        this.encoder = new TensorFlowFeatureEncoder(database);
        this.inputSize = encoder.getFeatureSize();
        
        // Initialize weights
        initializeWeights();
        
        // Precompute game embeddings
        precomputeGameEmbeddings();
        
        System.out.println("\n=== TensorFlow Neural Network Model ===");
        System.out.println("Architecture: Two-Tower Recommendation Model");
        System.out.println("Input size: " + inputSize);
        System.out.println("Hidden layers: " + hiddenSize1 + " -> " + hiddenSize2);
        System.out.println("Embedding size: " + embeddingSize);
        System.out.println("Total parameters: " + countParameters());
        System.out.println("========================================\n");
    }
    
    private void initializeWeights() {
        Random rand = new Random(42);
        
        weights1 = xavierInit(inputSize, hiddenSize1, rand);
        bias1 = new float[hiddenSize1];
        
        weights2 = xavierInit(hiddenSize1, hiddenSize2, rand);
        bias2 = new float[hiddenSize2];
        
        weights3 = xavierInit(hiddenSize2, embeddingSize, rand);
        bias3 = new float[embeddingSize];
    }
    
    private float[][] xavierInit(int fanIn, int fanOut, Random rand) {
        float[][] weights = new float[fanIn][fanOut];
        float scale = (float) Math.sqrt(2.0 / (fanIn + fanOut));
        for (int i = 0; i < fanIn; i++) {
            for (int j = 0; j < fanOut; j++) {
                weights[i][j] = (float) (rand.nextGaussian() * scale);
            }
        }
        return weights;
    }
    
    private int countParameters() {
        return (inputSize * hiddenSize1 + hiddenSize1) +
               (hiddenSize1 * hiddenSize2 + hiddenSize2) +
               (hiddenSize2 * embeddingSize + embeddingSize);
    }
    
    /**
     * Forward pass using TensorFlow for matrix operations.
     */
    public float[] computeEmbedding(float[] input) {
        try (Graph g = new Graph()) {
            Ops tf = Ops.create(g);
            
            // Convert input to 2D tensor [1, inputSize]
            float[][] input2D = new float[][] {input};
            
            // Layer 1: matmul + bias + relu
            float[][] h1 = matmul(input2D, weights1);
            addBias(h1, bias1);
            relu(h1);
            
            // Layer 2: matmul + bias + relu
            float[][] h2 = matmul(h1, weights2);
            addBias(h2, bias2);
            relu(h2);
            
            // Layer 3: matmul + bias (embedding layer)
            float[][] emb = matmul(h2, weights3);
            addBias(emb, bias3);
            
            // L2 normalize
            float[] embedding = emb[0];
            l2Normalize(embedding);
            
            return embedding;
        }
    }
    
    // Matrix multiplication using TensorFlow
    private float[][] matmul(float[][] a, float[][] b) {
        try (Graph g = new Graph()) {
            Ops tf = Ops.create(g);
            
            Operand<TFloat32> tensorA = tf.constant(a);
            Operand<TFloat32> tensorB = tf.constant(b);
            Operand<TFloat32> result = tf.linalg.matMul(tensorA, tensorB);
            
            try (Session sess = new Session(g);
                 TFloat32 output = (TFloat32) sess.runner().fetch(result).run().get(0)) {
                
                int rows = (int) output.shape().get(0);
                int cols = (int) output.shape().get(1);
                float[][] out = new float[rows][cols];
                
                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < cols; j++) {
                        out[i][j] = output.getFloat(i, j);
                    }
                }
                return out;
            }
        }
    }
    
    private void addBias(float[][] matrix, float[] bias) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                matrix[i][j] += bias[j];
            }
        }
    }
    
    private void relu(float[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                matrix[i][j] = Math.max(0, matrix[i][j]);
            }
        }
    }
    
    private void l2Normalize(float[] vector) {
        float sumSquared = 0;
        for (float v : vector) {
            sumSquared += v * v;
        }
        float norm = (float) Math.sqrt(sumSquared) + 1e-10f;
        for (int i = 0; i < vector.length; i++) {
            vector[i] /= norm;
        }
    }
    
    private void precomputeGameEmbeddings() {
        gameEmbeddings = new HashMap<>();
        
        for (Game game : database.getAllGames()) {
            float[] features = encoder.encodeGame(game);
            float[] embedding = computeEmbedding(features);
            gameEmbeddings.put(game.getId(), embedding);
        }
        
        System.out.println("Precomputed embeddings for " + gameEmbeddings.size() + " games");
    }
    
    private float computeSimilarity(float[] userEmb, float[] gameEmb) {
        float dotProduct = 0;
        for (int i = 0; i < embeddingSize; i++) {
            dotProduct += userEmb[i] * gameEmb[i];
        }
        return dotProduct;
    }
    
    /**
     * Get top-N game recommendations for a user.
     */
    public List<ScoredGame> recommend(UserProfile user, int topN) {
        float[] userFeatures = encoder.encodeUserFromHistory(user, database.getAllGames());
        float[] userEmbedding = computeEmbedding(userFeatures);
        
        List<ScoredGame> scored = new ArrayList<>();
        
        for (Game game : database.getAllGames()) {
            if (user.hasPlayed(game.getId())) continue;
            
            float[] gameEmbedding = gameEmbeddings.get(game.getId());
            float similarity = computeSimilarity(userEmbedding, gameEmbedding);
            
            scored.add(new ScoredGame(game, similarity));
        }
        
        return scored.stream()
            .sorted((a, b) -> Float.compare(b.score, a.score))
            .limit(topN)
            .collect(Collectors.toList());
    }
    
    /**
     * Find similar games to a given game.
     */
    public List<ScoredGame> findSimilar(Game targetGame, int topN) {
        float[] targetEmbedding = gameEmbeddings.get(targetGame.getId());
        
        List<ScoredGame> scored = new ArrayList<>();
        
        for (Game game : database.getAllGames()) {
            if (game.getId().equals(targetGame.getId())) continue;
            
            float[] gameEmbedding = gameEmbeddings.get(game.getId());
            float similarity = computeSimilarity(targetEmbedding, gameEmbedding);
            
            scored.add(new ScoredGame(game, similarity));
        }
        
        return scored.stream()
            .sorted((a, b) -> Float.compare(b.score, a.score))
            .limit(topN)
            .collect(Collectors.toList());
    }
    
    /**
     * Train the model on user-game interaction data.
     */
    public void train(List<TrainingExample> examples, int epochs, float learningRate) {
        System.out.println("\n--- Training Neural Network ---");
        System.out.println("Examples: " + examples.size());
        System.out.println("Epochs: " + epochs);
        System.out.println("Learning rate: " + learningRate);
        
        for (int epoch = 0; epoch < epochs; epoch++) {
            float totalLoss = 0;
            
            Collections.shuffle(examples);
            
            for (TrainingExample ex : examples) {
                float[] userFeatures = encoder.encodeUserFromHistory(ex.user, database.getAllGames());
                float[] gameFeatures = encoder.encodeGame(ex.game);
                
                float[] userEmb = computeEmbedding(userFeatures);
                float[] gameEmb = computeEmbedding(gameFeatures);
                
                float prediction = computeSimilarity(userEmb, gameEmb);
                float target = ex.rating / 10.0f;
                
                float loss = (prediction - target) * (prediction - target);
                totalLoss += loss;
                
                // Simple gradient update on last layer
                float gradient = 2 * (prediction - target) * learningRate;
                for (int i = 0; i < embeddingSize; i++) {
                    for (int j = 0; j < hiddenSize2; j++) {
                        weights3[j][i] -= gradient * 0.01f;
                    }
                }
            }
            
            if ((epoch + 1) % 10 == 0 || epoch == 0) {
                System.out.printf("Epoch %d/%d - Loss: %.4f%n", 
                    epoch + 1, epochs, totalLoss / examples.size());
            }
        }
        
        precomputeGameEmbeddings();
        System.out.println("Training complete!\n");
    }
    
    public float[] getGameEmbedding(String gameId) {
        return gameEmbeddings.get(gameId);
    }
    
    public int getEmbeddingSize() {
        return embeddingSize;
    }
    
    public void close() {
        // Cleanup if needed
    }
    
    // Inner classes
    
    public static class ScoredGame {
        public final Game game;
        public final float score;
        
        public ScoredGame(Game game, float score) {
            this.game = game;
            this.score = score;
        }
        
        @Override
        public String toString() {
            return String.format("[%.3f] %s", score, game.getTitle());
        }
    }
    
    public static class TrainingExample {
        public final UserProfile user;
        public final Game game;
        public final float rating;
        
        public TrainingExample(UserProfile user, Game game, float rating) {
            this.user = user;
            this.game = game;
            this.rating = rating;
        }
    }
}
