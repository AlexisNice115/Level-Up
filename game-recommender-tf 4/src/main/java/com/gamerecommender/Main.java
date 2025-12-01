package com.gamerecommender;

import com.gamerecommender.model.Game;
import com.gamerecommender.model.UserProfile;
import com.gamerecommender.data.GameDatabase;
import com.gamerecommender.ai.TensorFlowRecommendationModel;
import com.gamerecommender.ai.TensorFlowRecommendationModel.ScoredGame;
import com.gamerecommender.ai.TensorFlowRecommendationModel.TrainingExample;

import java.util.*;

/**
 * Demo application for TensorFlow-based Game Recommender.
 */
public class Main {
    
    public static void main(String[] args) {
        System.out.println("╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║   GAME RECOMMENDER - TensorFlow Neural Network Edition    ║");
        System.out.println("╚═══════════════════════════════════════════════════════════╝\n");
        
        // Initialize database
        GameDatabase database = new GameDatabase();
        
        // Create the TensorFlow model
        TensorFlowRecommendationModel model = new TensorFlowRecommendationModel(database);
        
        // Create sample users
        UserProfile rpgFan = createRPGFan(database);
        UserProfile casualGamer = createCasualGamer(database);
        UserProfile competitiveGamer = createCompetitiveGamer(database);
        
        // ===== DEMO 1: Basic Recommendations =====
        System.out.println("═".repeat(60));
        System.out.println("DEMO 1: Neural Network Recommendations (Pre-Training)");
        System.out.println("═".repeat(60));
        
        System.out.println("\n--- Recommendations for RPG Fan ---");
        System.out.println("Profile: " + rpgFan);
        printRecommendations(model.recommend(rpgFan, 5));
        
        System.out.println("\n--- Recommendations for Casual Gamer ---");
        System.out.println("Profile: " + casualGamer);
        printRecommendations(model.recommend(casualGamer, 5));
        
        // ===== DEMO 2: Training the Model =====
        System.out.println("\n" + "═".repeat(60));
        System.out.println("DEMO 2: Training the Neural Network");
        System.out.println("═".repeat(60));
        
        // Generate training data from user interactions
        List<TrainingExample> trainingData = generateTrainingData(database);
        System.out.println("\nGenerated " + trainingData.size() + " training examples");
        
        // Train the model
        try {
            model.train(trainingData, 50, 0.01f);
        } catch (Exception e) {
            System.out.println("ERROR during training: " + e.getMessage());
            e.printStackTrace();
        }
        
        // ===== DEMO 3: Post-Training Recommendations =====
        try {
        System.out.println("═".repeat(60));
        System.out.println("DEMO 3: Neural Network Recommendations (Post-Training)");
        System.out.println("═".repeat(60));
        
        System.out.println("\n--- Updated Recommendations for RPG Fan ---");
        printRecommendations(model.recommend(rpgFan, 5));
        
        System.out.println("\n--- Updated Recommendations for Competitive Gamer ---");
        System.out.println("Profile: " + competitiveGamer);
        printRecommendations(model.recommend(competitiveGamer, 5));
        
        // ===== DEMO 4: Similar Games =====
        System.out.println("\n" + "═".repeat(60));
        System.out.println("DEMO 4: Find Similar Games (Neural Embeddings)");
        System.out.println("═".repeat(60));
        
        Game targetGame = database.getGameById("g002"); // Dragon's Legacy
        System.out.println("\nGames similar to: " + targetGame.getTitle());
        printRecommendations(model.findSimilar(targetGame, 5));
        
        // ===== DEMO 5: Embedding Analysis =====
        System.out.println("\n" + "═".repeat(60));
        System.out.println("DEMO 5: Neural Embedding Analysis");
        System.out.println("═".repeat(60));
        
        analyzeEmbeddings(model, database);
        
        // ===== DEMO 6: Real-time Learning =====
        System.out.println("\n" + "═".repeat(60));
        System.out.println("DEMO 6: Real-time User Learning");
        System.out.println("═".repeat(60));
        
        demonstrateRealTimeLearning(model, database);
        
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
            e.printStackTrace();
        }
        
        // Cleanup
        model.close();
        
        System.out.println("\n" + "═".repeat(60));
        System.out.println("DEMO COMPLETE");
        System.out.println("═".repeat(60));
    }
    
    // ===== Helper Methods =====
    
    private static UserProfile createRPGFan(GameDatabase db) {
        UserProfile user = new UserProfile("u001", "RPGMaster");
        user.setGenrePreference("rpg", 0.95);
        user.setGenrePreference("fantasy", 0.9);
        user.setGenrePreference("adventure", 0.7);
        user.setTagPreference("story-rich", 0.9);
        user.setTagPreference("open-world", 0.85);
        user.setTagPreference("magic", 0.8);
        
        user.learnFromGame(db.getGameById("g002"), 9.5); // Dragon's Legacy
        user.learnFromGame(db.getGameById("g006"), 8.5); // Tales of Avalon
        
        return user;
    }
    
    private static UserProfile createCasualGamer(GameDatabase db) {
        UserProfile user = new UserProfile("u002", "ChillPlayer");
        user.setGenrePreference("casual", 0.9);
        user.setGenrePreference("simulation", 0.85);
        user.setGenrePreference("puzzle", 0.8);
        user.setTagPreference("relaxing", 0.95);
        user.setTagPreference("wholesome", 0.9);
        
        user.learnFromGame(db.getGameById("g010"), 9.0); // Cozy Farm
        user.learnFromGame(db.getGameById("g011"), 8.0); // Mind Bender
        
        return user;
    }
    
    private static UserProfile createCompetitiveGamer(GameDatabase db) {
        UserProfile user = new UserProfile("u003", "ProGamer99");
        user.setGenrePreference("shooter", 0.9);
        user.setGenrePreference("fighting", 0.85);
        user.setGenrePreference("multiplayer", 0.95);
        user.setTagPreference("competitive", 0.95);
        user.setTagPreference("pvp", 0.9);
        user.setTagPreference("esports", 0.85);
        
        user.learnFromGame(db.getGameById("g012"), 8.5); // Warzone Elite
        user.learnFromGame(db.getGameById("g015"), 9.0); // Champion Fighters X
        
        return user;
    }
    
    private static List<TrainingExample> generateTrainingData(GameDatabase db) {
        List<TrainingExample> examples = new ArrayList<>();
        Random rand = new Random(42);
        
        // Simulate multiple users with different preferences
        for (int u = 0; u < 20; u++) {
            UserProfile user = new UserProfile("train_u" + u, "TrainUser" + u);
            
            // Random preference profile
            String[] genres = {"rpg", "action", "strategy", "casual", "shooter", "horror"};
            String[] tags = {"story-rich", "competitive", "relaxing", "open-world", "co-op"};
            
            // Set random preferences
            for (String genre : genres) {
                user.setGenrePreference(genre, rand.nextFloat());
            }
            for (String tag : tags) {
                user.setTagPreference(tag, rand.nextFloat());
            }
            
            // Generate ratings for games based on preference match
            for (Game game : db.getAllGames()) {
                // Calculate expected rating based on preference overlap
                float baseRating = 5.0f;
                
                for (String genre : game.getGenres()) {
                    baseRating += user.getGenrePreferences()
                        .getOrDefault(genre.toLowerCase(), 0.5).floatValue() * 2;
                }
                for (String tag : game.getTags()) {
                    baseRating += user.getTagPreferences()
                        .getOrDefault(tag.toLowerCase(), 0.5).floatValue();
                }
                
                // Add some noise
                float rating = Math.min(10, Math.max(1, 
                    baseRating + (rand.nextFloat() - 0.5f) * 2));
                
                user.learnFromGame(game, rating);
                examples.add(new TrainingExample(user, game, rating));
            }
        }
        
        return examples;
    }
    
    private static void analyzeEmbeddings(TensorFlowRecommendationModel model, 
                                          GameDatabase db) {
        System.out.println("\nGame Embeddings (first 5 dimensions):");
        System.out.println("-".repeat(50));
        
        for (Game game : db.getAllGames()) {
            float[] emb = model.getGameEmbedding(game.getId());
            System.out.printf("%-25s [%.2f, %.2f, %.2f, %.2f, %.2f, ...]%n",
                game.getTitle().substring(0, Math.min(25, game.getTitle().length())),
                emb[0], emb[1], emb[2], emb[3], emb[4]);
        }
        
        System.out.println("\nEmbedding size: " + model.getEmbeddingSize() + " dimensions");
        System.out.println("Note: Similar games have similar embedding vectors");
    }
    
    private static void demonstrateRealTimeLearning(TensorFlowRecommendationModel model,
                                                     GameDatabase db) {
        UserProfile newUser = new UserProfile("new001", "NewPlayer");
        System.out.println("\nCreated new user with no history");
        
        // Initial recommendations
        System.out.println("\nInitial recommendations (cold start):");
        printRecommendations(model.recommend(newUser, 3));
        
        // User plays some games
        System.out.println("\nUser plays 'Cozy Farm' and rates it 9/10...");
        newUser.learnFromGame(db.getGameById("g010"), 9.0);
        
        System.out.println("User plays 'Craft World' and rates it 8/10...");
        newUser.learnFromGame(db.getGameById("g014"), 8.0);
        
        System.out.println("User plays 'Warzone Elite' and rates it 3/10...");
        newUser.learnFromGame(db.getGameById("g012"), 3.0);
        
        // Updated recommendations
        System.out.println("\nUpdated recommendations after learning:");
        printRecommendations(model.recommend(newUser, 3));
        
        System.out.println("\n→ Notice: Recommendations shifted toward casual/creative games!");
    }
    
    private static void printRecommendations(List<ScoredGame> recs) {
        for (int i = 0; i < recs.size(); i++) {
            ScoredGame rec = recs.get(i);
            System.out.printf("  %d. %s (score: %.3f)%n", 
                i + 1, rec.game.getTitle(), rec.score);
        }
    }
}
