package com.gamerecommender;

import com.gamerecommender.model.Game;
import com.gamerecommender.data.MongoGameDatabase;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Interactive Game Recommender using MongoDB.
 * 
 * Run SetupMongoDB first to populate the database!
 */
public class MongoRecommender {
    
    private static Scanner scanner = new Scanner(System.in);
    private static MongoGameDatabase database;
    
    public static void main(String[] args) {
        System.out.println("╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║         GAME RECOMMENDER (MongoDB Edition)                ║");
        System.out.println("╚═══════════════════════════════════════════════════════════╝\n");
        
        try {
            // Connect to MongoDB
            database = new MongoGameDatabase();
            
            if (database.size() == 0) {
                System.out.println("\n⚠ Database is empty!");
                System.out.println("Run SetupMongoDB first to add sample games.");
                database.close();
                return;
            }
            
            // Main loop
            boolean running = true;
            while (running) {
                System.out.println("\n╔════════════════════════════════════╗");
                System.out.println("║             MAIN MENU              ║");
                System.out.println("╠════════════════════════════════════╣");
                System.out.println("║  1. Get Recommendations            ║");
                System.out.println("║  2. Search by Genre                ║");
                System.out.println("║  3. Search by Tag                  ║");
                System.out.println("║  4. Search by Title                ║");
                System.out.println("║  5. Advanced Search (Filters)      ║");
                System.out.println("║  6. Describe What I Want           ║");
                System.out.println("║  7. Browse All Games               ║");
                System.out.println("║  8. Add New Game                   ║");
                System.out.println("║  9. Exit                           ║");
                System.out.println("╚════════════════════════════════════╝");
                
                System.out.print("\nChoice: ");
                String choice = scanner.nextLine().trim();
                
                switch (choice) {
                    case "1" -> getRecommendations();
                    case "2" -> searchByGenre();
                    case "3" -> searchByTag();
                    case "4" -> searchByTitle();
                    case "5" -> advancedSearch();
                    case "6" -> describeAndFind();
                    case "7" -> browseAll();
                    case "8" -> addNewGame();
                    case "9" -> running = false;
                    default -> System.out.println("Invalid choice.");
                }
            }
            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            System.err.println("Make sure MongoDB is running!");
        } finally {
            if (database != null) {
                database.close();
            }
            scanner.close();
            System.out.println("\nGoodbye!");
        }
    }
    
    // ==================== RECOMMENDATION BY QUESTIONS ====================
    
    private static void getRecommendations() {
        System.out.println("\n=== Find Your Perfect Game ===\n");
        
        Map<String, Double> preferences = new HashMap<>();
        
        // Genre preference
        System.out.println("What genre interests you?");
        System.out.println("  1. RPG/Fantasy     4. Horror");
        System.out.println("  2. Action/Shooter  5. Casual/Relaxing");
        System.out.println("  3. Strategy        6. No preference");
        System.out.print("Choice: ");
        String genreChoice = scanner.nextLine().trim();
        
        switch (genreChoice) {
            case "1" -> { preferences.put("rpg", 1.0); preferences.put("fantasy", 1.0); }
            case "2" -> { preferences.put("action", 1.0); preferences.put("shooter", 1.0); }
            case "3" -> { preferences.put("strategy", 1.0); preferences.put("simulation", 1.0); }
            case "4" -> { preferences.put("horror", 1.0); }
            case "5" -> { preferences.put("casual", 1.0); preferences.put("relaxing", 1.0); }
        }
        
        // Multiplayer
        System.out.print("\nMultiplayer? (yes/no/either): ");
        String mpChoice = scanner.nextLine().trim().toLowerCase();
        Boolean wantsMultiplayer = mpChoice.startsWith("y") ? true : 
                                   mpChoice.startsWith("n") ? false : null;
        
        // Price
        System.out.print("Max price ($, or press Enter for any): ");
        String priceStr = scanner.nextLine().trim();
        Double maxPrice = priceStr.isEmpty() ? null : Double.parseDouble(priceStr);
        
        // Difficulty
        System.out.print("Difficulty (easy/medium/hard, or Enter for any): ");
        String difficulty = scanner.nextLine().trim().toLowerCase();
        if (difficulty.isEmpty()) difficulty = null;
        
        // Get all games and score them
        List<Game> allGames = database.getAllGames();
        
        final Boolean mp = wantsMultiplayer;
        final String diff = difficulty;
        final Double price = maxPrice;
        
        List<ScoredGame> results = allGames.stream()
            .map(game -> new ScoredGame(game, scoreGame(game, preferences, mp, diff, price)))
            .sorted((a, b) -> Double.compare(b.score, a.score))
            .limit(5)
            .collect(Collectors.toList());
        
        displayResults(results, "Your Personalized Recommendations");
    }
    
    private static double scoreGame(Game game, Map<String, Double> preferences,
                                     Boolean wantsMultiplayer, String difficulty, Double maxPrice) {
        double score = 0;
        
        // Genre match
        for (String genre : game.getGenres()) {
            if (preferences.containsKey(genre.toLowerCase())) {
                score += 20;
            }
        }
        
        // Tag match
        for (String tag : game.getTags()) {
            if (preferences.containsKey(tag.toLowerCase())) {
                score += 10;
            }
        }
        
        // Multiplayer match
        if (wantsMultiplayer != null && game.isMultiplayer() == wantsMultiplayer) {
            score += 15;
        }
        
        // Difficulty match
        if (difficulty != null && game.getDifficulty() != null 
            && game.getDifficulty().equalsIgnoreCase(difficulty)) {
            score += 10;
        }
        
        // Price match
        if (maxPrice != null && game.getPrice() <= maxPrice) {
            score += 10;
        }
        
        // Rating bonus
        score += game.getRating() * 2;
        
        return score;
    }
    
    // ==================== SEARCH BY GENRE ====================
    
    private static void searchByGenre() {
        System.out.println("\n=== Search by Genre ===");
        System.out.println("Examples: RPG, Action, Strategy, Horror, Casual, Shooter\n");
        System.out.print("Enter genre: ");
        String genre = scanner.nextLine().trim();
        
        List<Game> results = database.findByGenre(genre);
        
        if (results.isEmpty()) {
            // Try case-insensitive search on all games
            String lowerGenre = genre.toLowerCase();
            results = database.getAllGames().stream()
                .filter(g -> g.getGenres().stream()
                    .anyMatch(gg -> gg.toLowerCase().contains(lowerGenre)))
                .collect(Collectors.toList());
        }
        
        displayGameList(results, "Games in genre: " + genre);
    }
    
    // ==================== SEARCH BY TAG ====================
    
    private static void searchByTag() {
        System.out.println("\n=== Search by Tag ===");
        System.out.println("Examples: open-world, story-rich, multiplayer, co-op, relaxing\n");
        System.out.print("Enter tag: ");
        String tag = scanner.nextLine().trim();
        
        List<Game> results = database.findByTag(tag);
        
        if (results.isEmpty()) {
            String lowerTag = tag.toLowerCase();
            results = database.getAllGames().stream()
                .filter(g -> g.getTags().stream()
                    .anyMatch(t -> t.toLowerCase().contains(lowerTag)))
                .collect(Collectors.toList());
        }
        
        displayGameList(results, "Games with tag: " + tag);
    }
    
    // ==================== SEARCH BY TITLE ====================
    
    private static void searchByTitle() {
        System.out.println("\n=== Search by Title ===\n");
        System.out.print("Enter search term: ");
        String term = scanner.nextLine().trim();
        
        List<Game> results = database.searchByTitle(term);
        displayGameList(results, "Games matching: " + term);
    }
    
    // ==================== ADVANCED SEARCH ====================
    
    private static void advancedSearch() {
        System.out.println("\n=== Advanced Search ===");
        System.out.println("Press Enter to skip any filter.\n");
        
        System.out.print("Genre: ");
        String genre = scanner.nextLine().trim();
        if (genre.isEmpty()) genre = null;
        
        System.out.print("Min rating (1-10): ");
        String ratingStr = scanner.nextLine().trim();
        Double minRating = ratingStr.isEmpty() ? null : Double.parseDouble(ratingStr);
        
        System.out.print("Max price ($): ");
        String priceStr = scanner.nextLine().trim();
        Double maxPrice = priceStr.isEmpty() ? null : Double.parseDouble(priceStr);
        
        System.out.print("Multiplayer (yes/no): ");
        String mpStr = scanner.nextLine().trim().toLowerCase();
        Boolean multiplayer = mpStr.isEmpty() ? null : mpStr.startsWith("y");
        
        System.out.print("Difficulty (easy/medium/hard): ");
        String difficulty = scanner.nextLine().trim();
        if (difficulty.isEmpty()) difficulty = null;
        
        List<Game> results = database.findGames(genre, minRating, maxPrice, multiplayer, difficulty);
        displayGameList(results, "Advanced Search Results");
    }
    
    // ==================== DESCRIBE AND FIND ====================
    
    private static void describeAndFind() {
        System.out.println("\n=== Describe Your Ideal Game ===\n");
        System.out.println("Examples:");
        System.out.println("  \"relaxing game with crafting\"");
        System.out.println("  \"scary horror game\"");
        System.out.println("  \"competitive multiplayer shooter\"\n");
        
        System.out.print("Your description: ");
        String input = scanner.nextLine().trim().toLowerCase();
        
        Set<String> keywords = extractKeywords(input);
        System.out.println("Searching for: " + keywords);
        
        List<ScoredGame> results = database.getAllGames().stream()
            .map(game -> new ScoredGame(game, scoreByKeywords(game, keywords)))
            .filter(sg -> sg.score > 0)
            .sorted((a, b) -> Double.compare(b.score, a.score))
            .limit(5)
            .collect(Collectors.toList());
        
        displayResults(results, "Games matching your description");
    }
    
    private static Set<String> extractKeywords(String input) {
        Set<String> keywords = new HashSet<>();
        
        Map<String, String[]> mappings = Map.ofEntries(
            Map.entry("rpg", new String[]{"rpg", "role-playing", "leveling"}),
            Map.entry("action", new String[]{"action", "combat", "fighting"}),
            Map.entry("horror", new String[]{"horror", "scary", "creepy", "terrifying"}),
            Map.entry("strategy", new String[]{"strategy", "tactics", "planning"}),
            Map.entry("casual", new String[]{"casual", "relaxing", "chill", "cozy"}),
            Map.entry("shooter", new String[]{"shooter", "fps", "shooting", "gun"}),
            Map.entry("fantasy", new String[]{"fantasy", "magic", "dragon", "medieval"}),
            Map.entry("story-rich", new String[]{"story", "narrative", "plot"}),
            Map.entry("open-world", new String[]{"open-world", "open world", "sandbox"}),
            Map.entry("multiplayer", new String[]{"multiplayer", "online", "friends", "coop"}),
            Map.entry("competitive", new String[]{"competitive", "pvp", "esports"}),
            Map.entry("relaxing", new String[]{"relaxing", "peaceful", "calm", "cozy"}),
            Map.entry("crafting", new String[]{"crafting", "craft", "building"}),
            Map.entry("survival", new String[]{"survival", "survive"}),
            Map.entry("space", new String[]{"space", "sci-fi", "alien", "galaxy"})
        );
        
        for (Map.Entry<String, String[]> entry : mappings.entrySet()) {
            for (String keyword : entry.getValue()) {
                if (input.contains(keyword)) {
                    keywords.add(entry.getKey());
                    break;
                }
            }
        }
        
        return keywords;
    }
    
    private static double scoreByKeywords(Game game, Set<String> keywords) {
        double score = 0;
        
        for (String keyword : keywords) {
            for (String genre : game.getGenres()) {
                if (genre.toLowerCase().contains(keyword)) score += 20;
            }
            for (String tag : game.getTags()) {
                if (tag.toLowerCase().contains(keyword)) score += 15;
            }
        }
        
        score += game.getRating();
        return score;
    }
    
    // ==================== BROWSE ALL ====================
    
    private static void browseAll() {
        System.out.println("\n=== All Games ===\n");
        List<Game> games = database.getAllGames();
        
        for (int i = 0; i < games.size(); i++) {
            Game game = games.get(i);
            System.out.printf("%2d. %-25s | %-20s | %.1f/10 | $%.2f%n",
                i + 1,
                game.getTitle(),
                game.getGenres().toString(),
                game.getRating(),
                game.getPrice());
        }
        
        System.out.println("\nTotal: " + games.size() + " games");
    }
    
    // ==================== ADD NEW GAME ====================
    
    private static void addNewGame() {
        System.out.println("\n=== Add New Game ===\n");
        
        System.out.print("Game ID (unique): ");
        String id = scanner.nextLine().trim();
        
        System.out.print("Title: ");
        String title = scanner.nextLine().trim();
        
        System.out.print("Genres (comma-separated): ");
        String[] genres = scanner.nextLine().trim().split(",");
        
        System.out.print("Tags (comma-separated): ");
        String[] tags = scanner.nextLine().trim().split(",");
        
        System.out.print("Rating (1-10): ");
        double rating = Double.parseDouble(scanner.nextLine().trim());
        
        System.out.print("Release year: ");
        int year = Integer.parseInt(scanner.nextLine().trim());
        
        System.out.print("Platform (PC/Console/Mobile): ");
        String platform = scanner.nextLine().trim();
        
        System.out.print("Average playtime (hours): ");
        int playtime = Integer.parseInt(scanner.nextLine().trim());
        
        System.out.print("Price ($): ");
        double price = Double.parseDouble(scanner.nextLine().trim());
        
        System.out.print("Multiplayer (yes/no): ");
        boolean multiplayer = scanner.nextLine().trim().toLowerCase().startsWith("y");
        
        System.out.print("Difficulty (easy/medium/hard): ");
        String difficulty = scanner.nextLine().trim();
        
        // Create and insert game
        Game game = new Game(id, title)
            .withGenres(Arrays.stream(genres).map(String::trim).toArray(String[]::new))
            .withTags(Arrays.stream(tags).map(String::trim).toArray(String[]::new))
            .withRating(rating)
            .withReleaseYear(year)
            .withPlatform(platform)
            .withPlaytime(playtime)
            .withPrice(price)
            .withMultiplayer(multiplayer)
            .withDifficulty(difficulty);
        
        database.insertGame(game);
        System.out.println("\n✓ Game added successfully!");
    }
    
    // ==================== DISPLAY HELPERS ====================
    
    private static void displayResults(List<ScoredGame> results, String title) {
        System.out.println("\n════════════════════════════════════════");
        System.out.println(title);
        System.out.println("════════════════════════════════════════\n");
        
        if (results.isEmpty()) {
            System.out.println("No games found. Try different criteria.");
            return;
        }
        
        for (int i = 0; i < results.size(); i++) {
            Game game = results.get(i).game;
            System.out.printf("%d. %s%n", i + 1, game.getTitle());
            System.out.printf("   Genres:  %s%n", game.getGenres());
            System.out.printf("   Tags:    %s%n", game.getTags());
            System.out.printf("   Rating:  %.1f/10  |  Price: $%.2f%n", 
                game.getRating(), game.getPrice());
            System.out.printf("   Match:   %.0f points%n%n", results.get(i).score);
        }
    }
    
    private static void displayGameList(List<Game> games, String title) {
        System.out.println("\n════════════════════════════════════════");
        System.out.println(title);
        System.out.println("════════════════════════════════════════\n");
        
        if (games.isEmpty()) {
            System.out.println("No games found.");
            return;
        }
        
        for (int i = 0; i < games.size(); i++) {
            Game game = games.get(i);
            System.out.printf("%d. %s%n", i + 1, game.getTitle());
            System.out.printf("   Genres: %s  |  Rating: %.1f/10%n%n", 
                game.getGenres(), game.getRating());
        }
    }
    
    private static class ScoredGame {
        Game game;
        double score;
        ScoredGame(Game game, double score) {
            this.game = game;
            this.score = score;
        }
    }
}
