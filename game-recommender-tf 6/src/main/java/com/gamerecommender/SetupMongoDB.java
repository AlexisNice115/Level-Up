package com.gamerecommender;

import com.gamerecommender.model.Game;
import com.gamerecommender.data.MongoGameDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Run this once to populate MongoDB with sample games.
 * 
 * Prerequisites:
 * 1. Install MongoDB: https://www.mongodb.com/try/download/community
 * 2. Start MongoDB: mongod (or as a service)
 * 3. Run this class
 */
public class SetupMongoDB {
    
    public static void main(String[] args) {
        System.out.println("╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║           MONGODB SETUP - Game Recommender                ║");
        System.out.println("╚═══════════════════════════════════════════════════════════╝\n");
        
        MongoGameDatabase db = null;
        
        try {
            // Connect to MongoDB
            db = new MongoGameDatabase();
            
            // Check if data already exists
            if (db.size() > 0) {
                System.out.println("\nDatabase already has " + db.size() + " games.");
                System.out.print("Do you want to reset and reload? (yes/no): ");
                
                java.util.Scanner scanner = new java.util.Scanner(System.in);
                String answer = scanner.nextLine().trim().toLowerCase();
                
                if (answer.equals("yes") || answer.equals("y")) {
                    db.deleteAllGames();
                    insertSampleGames(db);
                } else {
                    System.out.println("Keeping existing data.");
                }
                scanner.close();
            } else {
                insertSampleGames(db);
            }
            
            // Show what's in the database
            System.out.println("\n=== Games in Database ===\n");
            for (Game game : db.getAllGames()) {
                System.out.printf("- %s (%s) - %.1f/10%n", 
                    game.getTitle(), game.getGenres(), game.getRating());
            }
            
            System.out.println("\n✓ Setup complete! Total games: " + db.size());
            
        } catch (Exception e) {
            System.err.println("\n✗ Error: " + e.getMessage());
            System.err.println("\nMake sure MongoDB is running:");
            System.err.println("  - macOS:   brew services start mongodb-community");
            System.err.println("  - Linux:   sudo systemctl start mongod");
            System.err.println("  - Windows: net start MongoDB");
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }
    
    private static void insertSampleGames(MongoGameDatabase db) {
        System.out.println("\nInserting sample games...\n");
        
        List<Game> games = new ArrayList<>();
        
        // Action/Adventure
        games.add(new Game("g001", "Stellar Odyssey")
            .withGenres("Action", "Adventure", "Sci-Fi")
            .withTags("open-world", "space", "exploration", "combat", "story-rich")
            .withRating(9.2).withReleaseYear(2024).withPlatform("PC")
            .withPlaytime(60).withPrice(59.99).withMultiplayer(false).withDifficulty("medium"));
        
        games.add(new Game("g002", "Dragon's Legacy")
            .withGenres("RPG", "Fantasy", "Action")
            .withTags("dragons", "magic", "open-world", "character-customization", "quests")
            .withRating(8.8).withReleaseYear(2023).withPlatform("PC")
            .withPlaytime(100).withPrice(49.99).withMultiplayer(false).withDifficulty("hard"));
        
        games.add(new Game("g003", "Neon Streets")
            .withGenres("Action", "Cyberpunk", "Shooter")
            .withTags("cyberpunk", "fps", "story-rich", "atmospheric", "noir")
            .withRating(8.5).withReleaseYear(2024).withPlatform("PC")
            .withPlaytime(35).withPrice(59.99).withMultiplayer(true).withDifficulty("medium"));
        
        // RPGs
        games.add(new Game("g004", "Kingdoms Reborn")
            .withGenres("RPG", "Strategy", "Fantasy")
            .withTags("turn-based", "tactics", "story-rich", "choices-matter", "medieval")
            .withRating(9.0).withReleaseYear(2023).withPlatform("PC")
            .withPlaytime(80).withPrice(44.99).withMultiplayer(false).withDifficulty("hard"));
        
        games.add(new Game("g005", "Pixel Heroes")
            .withGenres("RPG", "Indie", "Retro")
            .withTags("pixel-art", "roguelike", "dungeon-crawler", "procedural", "challenging")
            .withRating(8.2).withReleaseYear(2022).withPlatform("PC")
            .withPlaytime(40).withPrice(19.99).withMultiplayer(false).withDifficulty("very_hard"));
        
        games.add(new Game("g006", "Tales of Avalon")
            .withGenres("RPG", "JRPG", "Fantasy")
            .withTags("anime", "turn-based", "story-rich", "party-based", "emotional")
            .withRating(8.9).withReleaseYear(2024).withPlatform("Console")
            .withPlaytime(70).withPrice(59.99).withMultiplayer(false).withDifficulty("medium"));
        
        // Strategy
        games.add(new Game("g007", "Empire Builder")
            .withGenres("Strategy", "Simulation", "Historical")
            .withTags("city-builder", "management", "economy", "sandbox", "historical")
            .withRating(8.7).withReleaseYear(2023).withPlatform("PC")
            .withPlaytime(200).withPrice(39.99).withMultiplayer(true).withDifficulty("medium"));
        
        games.add(new Game("g008", "Galactic Command")
            .withGenres("Strategy", "Sci-Fi", "4X")
            .withTags("space", "grand-strategy", "exploration", "diplomacy", "warfare")
            .withRating(8.4).withReleaseYear(2022).withPlatform("PC")
            .withPlaytime(150).withPrice(49.99).withMultiplayer(true).withDifficulty("hard"));
        
        // Horror
        games.add(new Game("g009", "Whispers in the Dark")
            .withGenres("Horror", "Adventure", "Psychological")
            .withTags("scary", "atmospheric", "puzzle", "story-rich", "psychological")
            .withRating(8.6).withReleaseYear(2024).withPlatform("PC")
            .withPlaytime(15).withPrice(29.99).withMultiplayer(false).withDifficulty("medium"));
        
        // Casual/Relaxing
        games.add(new Game("g010", "Cozy Farm")
            .withGenres("Simulation", "Casual", "Farming")
            .withTags("farming", "relaxing", "life-sim", "crafting", "wholesome")
            .withRating(9.1).withReleaseYear(2024).withPlatform("PC")
            .withPlaytime(100).withPrice(24.99).withMultiplayer(true).withDifficulty("easy"));
        
        games.add(new Game("g011", "Mind Bender")
            .withGenres("Puzzle", "Indie", "Casual")
            .withTags("puzzle", "relaxing", "brain-teaser", "minimalist", "atmospheric")
            .withRating(8.0).withReleaseYear(2024).withPlatform("Mobile")
            .withPlaytime(20).withPrice(4.99).withMultiplayer(false).withDifficulty("medium"));
        
        // Shooters
        games.add(new Game("g012", "Warzone Elite")
            .withGenres("Shooter", "Action", "Multiplayer")
            .withTags("fps", "military", "pvp", "tactical", "competitive")
            .withRating(7.9).withReleaseYear(2024).withPlatform("PC")
            .withPlaytime(200).withPrice(0.00).withMultiplayer(true).withDifficulty("hard"));
        
        games.add(new Game("g013", "Alien Hunters")
            .withGenres("Shooter", "Co-op", "Sci-Fi")
            .withTags("co-op", "aliens", "survival", "fps", "team-based")
            .withRating(8.4).withReleaseYear(2023).withPlatform("PC")
            .withPlaytime(45).withPrice(39.99).withMultiplayer(true).withDifficulty("medium"));
        
        // Sandbox
        games.add(new Game("g014", "Craft World")
            .withGenres("Sandbox", "Survival", "Multiplayer")
            .withTags("crafting", "building", "sandbox", "exploration", "creative")
            .withRating(9.3).withReleaseYear(2020).withPlatform("PC")
            .withPlaytime(500).withPrice(29.99).withMultiplayer(true).withDifficulty("easy"));
        
        // Fighting
        games.add(new Game("g015", "Champion Fighters X")
            .withGenres("Fighting", "Arcade", "Competitive")
            .withTags("fighting", "combo", "esports", "competitive", "2d")
            .withRating(8.7).withReleaseYear(2024).withPlatform("Console")
            .withPlaytime(100).withPrice(59.99).withMultiplayer(true).withDifficulty("very_hard"));
        
        // Insert all games
        db.insertGames(games);
    }
}
