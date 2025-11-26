package com.gamerecommender.ai;

import com.gamerecommender.model.Game;
import com.gamerecommender.model.UserProfile;
import com.gamerecommender.data.GameDatabase;

import org.tensorflow.ndarray.Shape;
import org.tensorflow.ndarray.NdArrays;
import org.tensorflow.ndarray.FloatNdArray;
import org.tensorflow.types.TFloat32;
import org.tensorflow.Tensor;

import java.util.*;

/**
 * Encodes games and users into TensorFlow tensors for neural network processing.
 */
public class TensorFlowFeatureEncoder {
    
    private final GameDatabase database;
    private final int featureSize;
    
    // Index mappings
    private final Map<String, Integer> genreIndex;
    private final Map<String, Integer> tagIndex;
    private final Map<String, Integer> difficultyIndex;
    private final Map<String, Integer> platformIndex;
    
    public TensorFlowFeatureEncoder(GameDatabase database) {
        this.database = database;
        
        // Build index mappings
        genreIndex = buildIndexMap(database.getAllGenres());
        tagIndex = buildIndexMap(database.getAllTags());
        difficultyIndex = buildIndexMap(database.getAllDifficulties());
        platformIndex = buildIndexMap(database.getAllPlatforms());
        
        this.featureSize = database.getFeatureVectorSize();
        
        System.out.println("TensorFlow Feature Encoder initialized");
        System.out.println("  Feature vector size: " + featureSize);
    }
    
    private Map<String, Integer> buildIndexMap(List<String> items) {
        Map<String, Integer> map = new HashMap<>();
        for (int i = 0; i < items.size(); i++) {
            map.put(items.get(i), i);
        }
        return map;
    }
    
    /**
     * Encode a single game into a float array.
     */
    public float[] encodeGame(Game game) {
        float[] features = new float[featureSize];
        int idx = 0;
        
        // One-hot encode genres
        for (String genre : database.getAllGenres()) {
            features[idx++] = game.getGenres().stream()
                .anyMatch(g -> g.toLowerCase().equals(genre)) ? 1.0f : 0.0f;
        }
        
        // One-hot encode tags
        for (String tag : database.getAllTags()) {
            features[idx++] = game.getTags().stream()
                .anyMatch(t -> t.toLowerCase().equals(tag)) ? 1.0f : 0.0f;
        }
        
        // One-hot encode difficulty
        for (String diff : database.getAllDifficulties()) {
            features[idx++] = (game.getDifficulty() != null && 
                game.getDifficulty().toLowerCase().equals(diff)) ? 1.0f : 0.0f;
        }
        
        // One-hot encode platform
        for (String plat : database.getAllPlatforms()) {
            features[idx++] = (game.getPlatform() != null && 
                game.getPlatform().toLowerCase().equals(plat)) ? 1.0f : 0.0f;
        }
        
        // Numerical features (normalized to 0-1)
        features[idx++] = (float) (game.getRating() / 10.0);
        features[idx++] = (float) ((game.getReleaseYear() - 1990) / 35.0);
        features[idx++] = (float) Math.min(1.0, game.getPlaytimeHours() / 100.0);
        features[idx++] = (float) Math.min(1.0, game.getPrice() / 70.0);
        features[idx++] = game.isMultiplayer() ? 1.0f : 0.0f;
        
        return features;
    }
    
    /**
     * Encode user preferences into a float array.
     */
    public float[] encodeUser(UserProfile user) {
        float[] features = new float[featureSize];
        int idx = 0;
        
        // Encode genre preferences
        for (String genre : database.getAllGenres()) {
            features[idx++] = user.getGenrePreferences()
                .getOrDefault(genre, 0.5).floatValue();
        }
        
        // Encode tag preferences
        for (String tag : database.getAllTags()) {
            features[idx++] = user.getTagPreferences()
                .getOrDefault(tag, 0.5).floatValue();
        }
        
        // For difficulty/platform/numerical, use neutral values
        // (could be enhanced with explicit user preferences)
        for (int i = 0; i < database.getAllDifficulties().size(); i++) {
            features[idx++] = 0.5f;
        }
        for (int i = 0; i < database.getAllPlatforms().size(); i++) {
            features[idx++] = 0.5f;
        }
        
        // Neutral numerical preferences
        features[idx++] = 0.7f;  // Prefers higher rated
        features[idx++] = 0.8f;  // Prefers newer
        features[idx++] = 0.5f;  // Neutral on playtime
        features[idx++] = 0.5f;  // Neutral on price
        features[idx++] = 0.5f;  // Neutral on multiplayer
        
        return features;
    }
    
    /**
     * Create user vector from their play history (weighted average of played games).
     */
    public float[] encodeUserFromHistory(UserProfile user, List<Game> allGames) {
        if (user.getPlayedGames().isEmpty()) {
            return encodeUser(user);
        }
        
        float[] userVector = new float[featureSize];
        float totalWeight = 0;
        
        for (Map.Entry<String, Double> entry : user.getPlayedGames().entrySet()) {
            Game game = database.getGameById(entry.getKey());
            if (game == null) continue;
            
            float weight = entry.getValue().floatValue() / 10.0f;
            float[] gameVector = encodeGame(game);
            
            for (int i = 0; i < featureSize; i++) {
                userVector[i] += gameVector[i] * weight;
            }
            totalWeight += weight;
        }
        
        // Normalize
        if (totalWeight > 0) {
            for (int i = 0; i < featureSize; i++) {
                userVector[i] /= totalWeight;
            }
        }
        
        // Blend with explicit preferences
        float[] explicitPrefs = encodeUser(user);
        for (int i = 0; i < featureSize; i++) {
            userVector[i] = (userVector[i] + explicitPrefs[i]) / 2.0f;
        }
        
        return userVector;
    }
    
    /**
     * Convert a float array to a TensorFlow Tensor.
     */
    public TFloat32 toTensor(float[] data) {
        FloatNdArray ndArray = NdArrays.ofFloats(Shape.of(1, data.length));
        for (int i = 0; i < data.length; i++) {
            ndArray.setFloat(data[i], 0, i);
        }
        return TFloat32.tensorOf(ndArray);
    }
    
    /**
     * Create a batch tensor from multiple games.
     */
    public TFloat32 encodeGameBatch(List<Game> games) {
        FloatNdArray ndArray = NdArrays.ofFloats(Shape.of(games.size(), featureSize));
        
        for (int g = 0; g < games.size(); g++) {
            float[] features = encodeGame(games.get(g));
            for (int f = 0; f < featureSize; f++) {
                ndArray.setFloat(features[f], g, f);
            }
        }
        
        return TFloat32.tensorOf(ndArray);
    }
    
    public int getFeatureSize() {
        return featureSize;
    }
}
