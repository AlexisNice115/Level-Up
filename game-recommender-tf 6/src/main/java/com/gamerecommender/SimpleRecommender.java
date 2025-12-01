package com.gamerecommender;

import com.gamerecommender.model.Game;
import com.gamerecommender.data.GameDatabase;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Simple Interactive Game Recommender
 * Takes user input and finds the best matching games from the database.
 */
public class SimpleRecommender {
    
    private static Scanner scanner = new Scanner(System.in);
    private static GameDatabase database;
    
    public static void main(String[] args) {
        System.out.println("╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║            GAME RECOMMENDER                               ║");
        System.out.println("╚═══════════════════════════════════════════════════════════╝\n");
        
        // Initialize database
        database = new GameDatabase();
        
        // Main loop
        boolean running = true;
        while (running) {
            System.out.println("\nHow would you like to find games?");
            System.out.println("1. Answer questions about my preferences");
            System.out.println("2. Search by genre");
            System.out.println("3. Search by keyword/tag");
            System.out.println("4. Describe what I want (natural input)");
            System.out.println("5. Exit");
            
            System.out.print("\nChoice: ");
            String choice = scanner.nextLine().trim();
            
            switch (choice) {
                case "1" -> recommendByQuestions();
                case "2" -> searchByGenre();
                case "3" -> searchByTag();
                case "4" -> recommendByDescription();
                case "5" -> running = false;
                default -> System.out.println("Invalid choice.");
            }
        }
        
        System.out.println("\nGoodbye!");
        scanner.close();
    }
    
    /**
     * Option 1: Ask user questions and score games based on answers
     */
    private static void recommendByQuestions() {
        System.out.println("\n=== Let's Find Your Perfect Game ===\n");
        
        // Collect user preferences
        Map<String, Double> preferences = new HashMap<>();
        
        // Question 1: Genre
        System.out.println("What genre interests you most?");
        System.out.println("  1. RPG/Fantasy");
        System.out.println("  2. Action/Shooter");
        System.out.println("  3. Strategy/Simulation");
        System.out.println("  4. Horror/Thriller");
        System.out.println("  5. Casual/Relaxing");
        System.out.print("Choice (1-5): ");
        String genreChoice = scanner.nextLine().trim();
        
        switch (genreChoice) {
            case "1" -> { preferences.put("rpg", 1.0); preferences.put("fantasy", 1.0); }
            case "2" -> { preferences.put("action", 1.0); preferences.put("shooter", 1.0); }
            case "3" -> { preferences.put("strategy", 1.0); preferences.put("simulation", 1.0); }
            case "4" -> { preferences.put("horror", 1.0); preferences.put("psychological", 1.0); }
            case "5" -> { preferences.put("casual", 1.0); preferences.put("puzzle", 1.0); }
        }
        
        // Question 2: Multiplayer
        System.out.print("\nDo you want multiplayer? (yes/no): ");
        String mpChoice = scanner.nextLine().trim().toLowerCase();
        boolean wantsMultiplayer = mpChoice.startsWith("y");
        
        // Question 3: Story
        System.out.print("Do you want a story-rich experience? (yes/no): ");
        String storyChoice = scanner.nextLine().trim().toLowerCase();
        if (storyChoice.startsWith("y")) {
            preferences.put("story-rich", 1.0);
        }
        
        // Question 4: Difficulty
        System.out.println("\nPreferred difficulty?");
        System.out.println("  1. Easy/Relaxing");
        System.out.println("  2. Medium/Balanced");
        System.out.println("  3. Hard/Challenging");
        System.out.print("Choice (1-3): ");
        String diffChoice = scanner.nextLine().trim();
        String preferredDifficulty = switch (diffChoice) {
            case "1" -> "easy";
            case "3" -> "hard";
            default -> "medium";
        };
        
        // Question 5: Price
        System.out.print("\nMaximum price you'd pay? (e.g., 30): $");
        double maxPrice = 100;
        try {
            maxPrice = Double.parseDouble(scanner.nextLine().trim());
        } catch (Exception e) {
            maxPrice = 100;
        }
        
        // Score all games based on user input
        final boolean mp = wantsMultiplayer;
        final String diff = preferredDifficulty;
        final double price = maxPrice;
        final Map<String, Double> prefs = preferences;
        
        List<ScoredGame> results = database.getAllGames().stream()
            .map(game -> new ScoredGame(game, scoreGame(game, prefs, mp, diff, price)))
            .sorted((a, b) -> Double.compare(b.score, a.score))
            .limit(5)
            .collect(Collectors.toList());
        
        // Display results
        displayResults(results, "Based on Your Preferences");
    }
    
    /**
     * Score a game based on how well it matches user preferences
     */
    private static double scoreGame(Game game, Map<String, Double> preferences, 
                                     boolean wantsMultiplayer, String difficulty, double maxPrice) {
        double score = 0;
        
        // Genre matching (0-30 points)
        for (String genre : game.getGenres()) {
            if (preferences.containsKey(genre.toLowerCase())) {
                score += 15;
            }
        }
        
        // Tag matching (0-20 points)
        for (String tag : game.getTags()) {
            if (preferences.containsKey(tag.toLowerCase())) {
                score += 10;
            }
        }
        
        // Multiplayer match (0-15 points)
        if (game.isMultiplayer() == wantsMultiplayer) {
            score += 15;
        }
        
        // Difficulty match (0-15 points)
        if (game.getDifficulty() != null && game.getDifficulty().equalsIgnoreCase(difficulty)) {
            score += 15;
        }
        
        // Price match (0-10 points)
        if (game.getPrice() <= maxPrice) {
            score += 10;
        }
        
        // Rating bonus (0-20 points)
        score += game.getRating() * 2;
        
        return score;
    }
    
    /**
     * Option 2: Search by genre
     */
    private static void searchByGenre() {
        System.out.println("\n=== Search by Genre ===\n");
        System.out.println("Available genres: RPG, Action, Strategy, Horror, Casual,");
        System.out.println("                  Shooter, Puzzle, Simulation, Adventure,");
        System.out.println("                  Fantasy, Sci-Fi, Multiplayer, Fighting\n");
        
        System.out.print("Enter genre: ");
        String genre = scanner.nextLine().trim().toLowerCase();
        
        List<ScoredGame> results = database.getAllGames().stream()
            .filter(game -> game.getGenres().stream()
                .anyMatch(g -> g.toLowerCase().contains(genre)))
            .map(game -> new ScoredGame(game, game.getRating() * 10))
            .sorted((a, b) -> Double.compare(b.score, a.score))
            .collect(Collectors.toList());
        
        displayResults(results, "Games in genre: " + genre);
    }
    
    /**
     * Option 3: Search by tag/keyword
     */
    private static void searchByTag() {
        System.out.println("\n=== Search by Keyword/Tag ===\n");
        System.out.println("Example tags: open-world, story-rich, multiplayer, co-op,");
        System.out.println("              competitive, relaxing, challenging, pvp,");
        System.out.println("              crafting, exploration, survival, space\n");
        
        System.out.print("Enter keyword: ");
        String keyword = scanner.nextLine().trim().toLowerCase();
        
        List<ScoredGame> results = database.getAllGames().stream()
            .filter(game -> 
                game.getTags().stream().anyMatch(t -> t.toLowerCase().contains(keyword)) ||
                game.getGenres().stream().anyMatch(g -> g.toLowerCase().contains(keyword)) ||
                game.getTitle().toLowerCase().contains(keyword))
            .map(game -> new ScoredGame(game, game.getRating() * 10))
            .sorted((a, b) -> Double.compare(b.score, a.score))
            .collect(Collectors.toList());
        
        displayResults(results, "Games matching: " + keyword);
    }
    
    /**
     * Option 4: Natural language input - describe what you want
     */
    private static void recommendByDescription() {
        System.out.println("\n=== Describe Your Ideal Game ===\n");
        System.out.println("Tell me what kind of game you're looking for.");
        System.out.println("Example: \"I want a relaxing game with crafting\"");
        System.out.println("         \"Something like Skyrim with dragons and magic\"");
        System.out.println("         \"Fast-paced competitive shooter\"\n");
        
        System.out.print("Your description: ");
        String input = scanner.nextLine().trim().toLowerCase();
        
        // Extract keywords from input
        Set<String> keywords = extractKeywords(input);
        
        System.out.println("\nSearching for: " + keywords + "\n");
        
        // Score games based on keyword matches
        List<ScoredGame> results = database.getAllGames().stream()
            .map(game -> new ScoredGame(game, scoreByKeywords(game, keywords)))
            .filter(sg -> sg.score > 0)
            .sorted((a, b) -> Double.compare(b.score, a.score))
            .limit(5)
            .collect(Collectors.toList());
        
        displayResults(results, "Games matching your description");
    }
    
    /**
     * Extract relevant keywords from natural language input
     */
    private static Set<String> extractKeywords(String input) {
        Set<String> keywords = new HashSet<>();
        
        // Genre keywords
        Map<String, String[]> genreMap = Map.of(
            "rpg", new String[]{"rpg", "role-playing", "role playing", "leveling"},
            "action", new String[]{"action", "combat", "fighting", "fast-paced", "fast paced"},
            "horror", new String[]{"horror", "scary", "creepy", "terrifying", "spooky"},
            "strategy", new String[]{"strategy", "tactics", "tactical", "planning", "4x"},
            "casual", new String[]{"casual", "relaxing", "chill", "cozy", "peaceful"},
            "shooter", new String[]{"shooter", "fps", "shooting", "gun", "military"},
            "puzzle", new String[]{"puzzle", "brain", "thinking", "logic"},
            "simulation", new String[]{"simulation", "sim", "management", "building"},
            "adventure", new String[]{"adventure", "exploration", "explore", "journey"},
            "fantasy", new String[]{"fantasy", "magic", "dragon", "medieval", "wizard"}
        );
        
        // Tag keywords
        Map<String, String[]> tagMap = Map.ofEntries(
            Map.entry("story-rich", new String[]{"story", "narrative", "plot", "story-rich"}),
            Map.entry("open-world", new String[]{"open-world", "open world", "sandbox", "free roam"}),
            Map.entry("multiplayer", new String[]{"multiplayer", "online", "friends", "coop", "co-op"}),
            Map.entry("competitive", new String[]{"competitive", "pvp", "esports", "ranked"}),
            Map.entry("relaxing", new String[]{"relaxing", "peaceful", "calm", "chill", "cozy"}),
            Map.entry("challenging", new String[]{"challenging", "difficult", "hard", "hardcore"}),
            Map.entry("crafting", new String[]{"crafting", "craft", "building", "create"}),
            Map.entry("survival", new String[]{"survival", "survive", "resource"}),
            Map.entry("space", new String[]{"space", "sci-fi", "scifi", "alien", "galaxy"}),
            Map.entry("atmospheric", new String[]{"atmospheric", "immersive", "mood", "ambient"})
        );
        
        // Check for genre matches
        for (Map.Entry<String, String[]> entry : genreMap.entrySet()) {
            for (String keyword : entry.getValue()) {
                if (input.contains(keyword)) {
                    keywords.add(entry.getKey());
                    break;
                }
            }
        }
        
        // Check for tag matches
        for (Map.Entry<String, String[]> entry : tagMap.entrySet()) {
            for (String keyword : entry.getValue()) {
                if (input.contains(keyword)) {
                    keywords.add(entry.getKey());
                    break;
                }
            }
        }
        
        // If no keywords found, try individual words
        if (keywords.isEmpty()) {
            String[] words = input.split("\\s+");
            for (String word : words) {
                if (word.length() > 3) {
                    keywords.add(word);
                }
            }
        }
        
        return keywords;
    }
    
    /**
     * Score a game based on how many keywords match
     */
    private static double scoreByKeywords(Game game, Set<String> keywords) {
        double score = 0;
        
        for (String keyword : keywords) {
            // Check genres
            for (String genre : game.getGenres()) {
                if (genre.toLowerCase().contains(keyword)) {
                    score += 20;
                }
            }
            
            // Check tags
            for (String tag : game.getTags()) {
                if (tag.toLowerCase().contains(keyword)) {
                    score += 15;
                }
            }
            
            // Check title
            if (game.getTitle().toLowerCase().contains(keyword)) {
                score += 10;
            }
        }
        
        // Add rating bonus
        score += game.getRating();
        
        return score;
    }
    
    /**
     * Display the results nicely
     */
    private static void displayResults(List<ScoredGame> results, String title) {
        System.out.println("\n════════════════════════════════════════");
        System.out.println(title);
        System.out.println("════════════════════════════════════════\n");
        
        if (results.isEmpty()) {
            System.out.println("No games found matching your criteria.");
            System.out.println("Try different keywords or broaden your search.");
            return;
        }
        
        for (int i = 0; i < results.size(); i++) {
            Game game = results.get(i).game;
            double score = results.get(i).score;
            
            System.out.printf("%d. %s%n", i + 1, game.getTitle());
            System.out.printf("   Genres:     %s%n", game.getGenres());
            System.out.printf("   Tags:       %s%n", game.getTags());
            System.out.printf("   Rating:     %.1f/10%n", game.getRating());
            System.out.printf("   Price:      $%.2f%n", game.getPrice());
            System.out.printf("   Difficulty: %s%n", game.getDifficulty());
            System.out.printf("   Match:      %.0f points%n", score);
            System.out.println();
        }
    }
    
    /**
     * Helper class to hold game with score
     */
    private static class ScoredGame {
        Game game;
        double score;
        
        ScoredGame(Game game, double score) {
            this.game = game;
            this.score = score;
        }
    }
}
