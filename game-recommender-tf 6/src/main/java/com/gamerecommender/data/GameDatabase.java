package com.gamerecommender.data;

import com.gamerecommender.model.Game;
import java.util.*;

/**
 * Placeholder game database with sample games.
 */
public class GameDatabase {
    
    private List<Game> games;
    private Map<String, Game> gameIndex;
    
    // Feature vocabulary (built from all games)
    private List<String> allGenres;
    private List<String> allTags;
    private List<String> allDifficulties;
    private List<String> allPlatforms;
    
    public GameDatabase() {
        games = new ArrayList<>();
        gameIndex = new HashMap<>();
        loadPlaceholderGames();
        buildVocabulary();
    }
    
    private void loadPlaceholderGames() {
        // Action/Adventure
        addGame(new Game("g001", "Stellar Odyssey")
            .withGenres("Action", "Adventure", "Sci-Fi")
            .withTags("open-world", "space", "exploration", "combat", "story-rich")
            .withRating(9.2).withReleaseYear(2024).withPlatform("PC")
            .withPlaytime(60).withPrice(59.99).withMultiplayer(false).withDifficulty("medium"));
        
        addGame(new Game("g002", "Dragon's Legacy")
            .withGenres("RPG", "Fantasy", "Action")
            .withTags("dragons", "magic", "open-world", "character-customization", "quests")
            .withRating(8.8).withReleaseYear(2023).withPlatform("PC")
            .withPlaytime(100).withPrice(49.99).withMultiplayer(false).withDifficulty("hard"));
        
        addGame(new Game("g003", "Neon Streets")
            .withGenres("Action", "Cyberpunk", "Shooter")
            .withTags("cyberpunk", "fps", "story-rich", "atmospheric", "noir")
            .withRating(8.5).withReleaseYear(2024).withPlatform("PC")
            .withPlaytime(35).withPrice(59.99).withMultiplayer(true).withDifficulty("medium"));
        
        // RPGs
        addGame(new Game("g004", "Kingdoms Reborn")
            .withGenres("RPG", "Strategy", "Fantasy")
            .withTags("turn-based", "tactics", "story-rich", "choices-matter", "medieval")
            .withRating(9.0).withReleaseYear(2023).withPlatform("PC")
            .withPlaytime(80).withPrice(44.99).withMultiplayer(false).withDifficulty("hard"));
        
        addGame(new Game("g005", "Pixel Heroes")
            .withGenres("RPG", "Indie", "Retro")
            .withTags("pixel-art", "roguelike", "dungeon-crawler", "procedural", "challenging")
            .withRating(8.2).withReleaseYear(2022).withPlatform("PC")
            .withPlaytime(40).withPrice(19.99).withMultiplayer(false).withDifficulty("very_hard"));
        
        addGame(new Game("g006", "Tales of Avalon")
            .withGenres("RPG", "JRPG", "Fantasy")
            .withTags("anime", "turn-based", "story-rich", "party-based", "emotional")
            .withRating(8.9).withReleaseYear(2024).withPlatform("Console")
            .withPlaytime(70).withPrice(59.99).withMultiplayer(false).withDifficulty("medium"));
        
        // Strategy
        addGame(new Game("g007", "Empire Builder")
            .withGenres("Strategy", "Simulation", "Historical")
            .withTags("city-builder", "management", "economy", "sandbox", "historical")
            .withRating(8.7).withReleaseYear(2023).withPlatform("PC")
            .withPlaytime(200).withPrice(39.99).withMultiplayer(true).withDifficulty("medium"));
        
        addGame(new Game("g008", "Galactic Command")
            .withGenres("Strategy", "Sci-Fi", "4X")
            .withTags("space", "grand-strategy", "exploration", "diplomacy", "warfare")
            .withRating(8.4).withReleaseYear(2022).withPlatform("PC")
            .withPlaytime(150).withPrice(49.99).withMultiplayer(true).withDifficulty("hard"));
        
        // Horror
        addGame(new Game("g009", "Whispers in the Dark")
            .withGenres("Horror", "Adventure", "Psychological")
            .withTags("scary", "atmospheric", "puzzle", "story-rich", "psychological")
            .withRating(8.6).withReleaseYear(2024).withPlatform("PC")
            .withPlaytime(15).withPrice(29.99).withMultiplayer(false).withDifficulty("medium"));
        
        // Casual/Relaxing
        addGame(new Game("g010", "Cozy Farm")
            .withGenres("Simulation", "Casual", "Farming")
            .withTags("farming", "relaxing", "life-sim", "crafting", "wholesome")
            .withRating(9.1).withReleaseYear(2024).withPlatform("PC")
            .withPlaytime(100).withPrice(24.99).withMultiplayer(true).withDifficulty("easy"));
        
        addGame(new Game("g011", "Mind Bender")
            .withGenres("Puzzle", "Indie", "Casual")
            .withTags("puzzle", "relaxing", "brain-teaser", "minimalist", "atmospheric")
            .withRating(8.0).withReleaseYear(2024).withPlatform("Mobile")
            .withPlaytime(20).withPrice(4.99).withMultiplayer(false).withDifficulty("medium"));
        
        // Shooters
        addGame(new Game("g012", "Warzone Elite")
            .withGenres("Shooter", "Action", "Multiplayer")
            .withTags("fps", "military", "pvp", "tactical", "competitive")
            .withRating(7.9).withReleaseYear(2024).withPlatform("PC")
            .withPlaytime(200).withPrice(0.00).withMultiplayer(true).withDifficulty("hard"));
        
        addGame(new Game("g013", "Alien Hunters")
            .withGenres("Shooter", "Co-op", "Sci-Fi")
            .withTags("co-op", "aliens", "survival", "fps", "team-based")
            .withRating(8.4).withReleaseYear(2023).withPlatform("PC")
            .withPlaytime(45).withPrice(39.99).withMultiplayer(true).withDifficulty("medium"));
        
        // Sandbox
        addGame(new Game("g014", "Craft World")
            .withGenres("Sandbox", "Survival", "Multiplayer")
            .withTags("crafting", "building", "sandbox", "exploration", "creative")
            .withRating(9.3).withReleaseYear(2020).withPlatform("PC")
            .withPlaytime(500).withPrice(29.99).withMultiplayer(true).withDifficulty("easy"));
        
        // Fighting
        addGame(new Game("g015", "Champion Fighters X")
            .withGenres("Fighting", "Arcade", "Competitive")
            .withTags("fighting", "combo", "esports", "competitive", "2d")
            .withRating(8.7).withReleaseYear(2024).withPlatform("Console")
            .withPlaytime(100).withPrice(59.99).withMultiplayer(true).withDifficulty("very_hard"));
        
        System.out.println("Loaded " + games.size() + " games into database");
    }
    
    private void buildVocabulary() {
        Set<String> genreSet = new HashSet<>();
        Set<String> tagSet = new HashSet<>();
        Set<String> diffSet = new HashSet<>();
        Set<String> platSet = new HashSet<>();
        
        for (Game game : games) {
            game.getGenres().forEach(g -> genreSet.add(g.toLowerCase()));
            game.getTags().forEach(t -> tagSet.add(t.toLowerCase()));
            if (game.getDifficulty() != null) diffSet.add(game.getDifficulty().toLowerCase());
            if (game.getPlatform() != null) platSet.add(game.getPlatform().toLowerCase());
        }
        
        allGenres = new ArrayList<>(genreSet);
        allTags = new ArrayList<>(tagSet);
        allDifficulties = new ArrayList<>(diffSet);
        allPlatforms = new ArrayList<>(platSet);
        
        Collections.sort(allGenres);
        Collections.sort(allTags);
        Collections.sort(allDifficulties);
        Collections.sort(allPlatforms);
        
        System.out.println("Vocabulary: " + allGenres.size() + " genres, " + 
                          allTags.size() + " tags, " + allDifficulties.size() + " difficulties");
    }
    
    private void addGame(Game game) {
        games.add(game);
        gameIndex.put(game.getId(), game);
    }
    
    public List<Game> getAllGames() { return new ArrayList<>(games); }
    public Game getGameById(String id) { return gameIndex.get(id); }
    public int size() { return games.size(); }
    
    // Vocabulary getters for feature encoding
    public List<String> getAllGenres() { return allGenres; }
    public List<String> getAllTags() { return allTags; }
    public List<String> getAllDifficulties() { return allDifficulties; }
    public List<String> getAllPlatforms() { return allPlatforms; }
    
    /**
     * Calculate total feature vector size for neural network input.
     */
    public int getFeatureVectorSize() {
        // genres + tags + difficulties + platforms + 5 numerical features
        return allGenres.size() + allTags.size() + allDifficulties.size() + 
               allPlatforms.size() + 5;
    }
}
