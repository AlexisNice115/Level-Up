package com.gamerecommender.data;

import com.gamerecommender.model.Game;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import org.bson.Document;

import java.util.*;

/**
 * MongoDB implementation of the game database.
 * 
 * Prerequisites:
 * 1. Install MongoDB: https://www.mongodb.com/try/download/community
 * 2. Start MongoDB service
 * 3. Default connection: mongodb://localhost:27017
 */
public class MongoGameDatabase {
    
    private MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection<Document> gamesCollection;
    
    // Vocabulary for feature encoding (built from all games)
    private List<String> allGenres;
    private List<String> allTags;
    private List<String> allDifficulties;
    private List<String> allPlatforms;
    
    /**
     * Connect to MongoDB with default settings (localhost:27017)
     */
    public MongoGameDatabase() {
        this("mongodb://localhost:27017", "gameRecommender");
    }
    
    /**
     * Connect to MongoDB with custom connection string
     * 
     * @param connectionString MongoDB connection string
     *                         Examples:
     *                         - "mongodb://localhost:27017"
     *                         - "mongodb://username:password@localhost:27017"
     *                         - "mongodb+srv://user:pass@cluster.mongodb.net"
     * @param databaseName     Name of the database to use
     */
    public MongoGameDatabase(String connectionString, String databaseName) {
        try {
            System.out.println("Connecting to MongoDB...");
            mongoClient = MongoClients.create(connectionString);
            database = mongoClient.getDatabase(databaseName);
            gamesCollection = database.getCollection("games");
            
            // Create indexes for faster queries
            createIndexes();
            
            System.out.println("Connected to MongoDB: " + databaseName);
            System.out.println("Games in database: " + gamesCollection.countDocuments());
            
            // Build vocabulary from existing games
            buildVocabulary();
            
        } catch (Exception e) {
            System.err.println("Failed to connect to MongoDB: " + e.getMessage());
            System.err.println("Make sure MongoDB is running!");
            throw e;
        }
    }
    
    /**
     * Create indexes for common queries
     */
    private void createIndexes() {
        gamesCollection.createIndex(Indexes.ascending("gameId"), 
            new IndexOptions().unique(true));
        gamesCollection.createIndex(Indexes.ascending("genres"));
        gamesCollection.createIndex(Indexes.ascending("tags"));
        gamesCollection.createIndex(Indexes.ascending("rating"));
    }
    
    /**
     * Build vocabulary from all games in database
     */
    private void buildVocabulary() {
        Set<String> genreSet = new HashSet<>();
        Set<String> tagSet = new HashSet<>();
        Set<String> diffSet = new HashSet<>();
        Set<String> platSet = new HashSet<>();
        
        for (Game game : getAllGames()) {
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
    }
    
    // ==================== CREATE ====================
    
    /**
     * Insert a single game into the database
     */
    public void insertGame(Game game) {
        Document doc = gameToDocument(game);
        gamesCollection.insertOne(doc);
        System.out.println("Inserted: " + game.getTitle());
    }
    
    /**
     * Insert multiple games at once
     */
    public void insertGames(List<Game> games) {
        List<Document> docs = new ArrayList<>();
        for (Game game : games) {
            docs.add(gameToDocument(game));
        }
        gamesCollection.insertMany(docs);
        System.out.println("Inserted " + games.size() + " games");
    }
    
    // ==================== READ ====================
    
    /**
     * Get all games from the database
     */
    public List<Game> getAllGames() {
        List<Game> games = new ArrayList<>();
        for (Document doc : gamesCollection.find()) {
            games.add(documentToGame(doc));
        }
        return games;
    }
    
    /**
     * Find a game by its ID
     */
    public Game getGameById(String gameId) {
        Document doc = gamesCollection.find(Filters.eq("gameId", gameId)).first();
        if (doc != null) {
            return documentToGame(doc);
        }
        return null;
    }
    
    /**
     * Find games by genre
     */
    public List<Game> findByGenre(String genre) {
        List<Game> games = new ArrayList<>();
        for (Document doc : gamesCollection.find(Filters.in("genres", genre))) {
            games.add(documentToGame(doc));
        }
        return games;
    }
    
    /**
     * Find games by tag
     */
    public List<Game> findByTag(String tag) {
        List<Game> games = new ArrayList<>();
        for (Document doc : gamesCollection.find(Filters.in("tags", tag))) {
            games.add(documentToGame(doc));
        }
        return games;
    }
    
    /**
     * Find games with rating >= minRating
     */
    public List<Game> findByMinRating(double minRating) {
        List<Game> games = new ArrayList<>();
        for (Document doc : gamesCollection.find(Filters.gte("rating", minRating))) {
            games.add(documentToGame(doc));
        }
        return games;
    }
    
    /**
     * Find games under a certain price
     */
    public List<Game> findByMaxPrice(double maxPrice) {
        List<Game> games = new ArrayList<>();
        for (Document doc : gamesCollection.find(Filters.lte("price", maxPrice))) {
            games.add(documentToGame(doc));
        }
        return games;
    }
    
    /**
     * Find games with playtime under certain hours
     */
    public List<Game> findByMaxPlaytime(int maxHours) {
        List<Game> games = new ArrayList<>();
        for (Document doc : gamesCollection.find(Filters.lte("playtimeHours", maxHours))) {
            games.add(documentToGame(doc));
        }
        return games;
    }
    
    /**
     * Search games by title (partial match)
     */
    public List<Game> searchByTitle(String searchTerm) {
        List<Game> games = new ArrayList<>();
        // Case-insensitive regex search
        for (Document doc : gamesCollection.find(
                Filters.regex("title", searchTerm, "i"))) {
            games.add(documentToGame(doc));
        }
        return games;
    }
    
    /**
     * Complex query: Find games matching multiple criteria
     */
    public List<Game> findGames(String genre, Double minRating, Double maxPrice, 
                                 Boolean multiplayer, String difficulty) {
        List<org.bson.conversions.Bson> filters = new ArrayList<>();
        
        if (genre != null && !genre.isEmpty()) {
            filters.add(Filters.in("genres", genre));
        }
        if (minRating != null) {
            filters.add(Filters.gte("rating", minRating));
        }
        if (maxPrice != null) {
            filters.add(Filters.lte("price", maxPrice));
        }
        if (multiplayer != null) {
            filters.add(Filters.eq("multiplayer", multiplayer));
        }
        if (difficulty != null && !difficulty.isEmpty()) {
            filters.add(Filters.eq("difficulty", difficulty));
        }
        
        List<Game> games = new ArrayList<>();
        FindIterable<Document> results;
        
        if (filters.isEmpty()) {
            results = gamesCollection.find();
        } else {
            results = gamesCollection.find(Filters.and(filters));
        }
        
        for (Document doc : results) {
            games.add(documentToGame(doc));
        }
        return games;
    }
    
    // ==================== UPDATE ====================
    
    /**
     * Update a game's rating
     */
    public void updateRating(String gameId, double newRating) {
        gamesCollection.updateOne(
            Filters.eq("gameId", gameId),
            new Document("$set", new Document("rating", newRating))
        );
    }
    
    /**
     * Update a game's price
     */
    public void updatePrice(String gameId, double newPrice) {
        gamesCollection.updateOne(
            Filters.eq("gameId", gameId),
            new Document("$set", new Document("price", newPrice))
        );
    }
    
    /**
     * Add a tag to a game
     */
    public void addTag(String gameId, String tag) {
        gamesCollection.updateOne(
            Filters.eq("gameId", gameId),
            new Document("$addToSet", new Document("tags", tag))
        );
    }
    
    // ==================== DELETE ====================
    
    /**
     * Delete a game by ID
     */
    public void deleteGame(String gameId) {
        gamesCollection.deleteOne(Filters.eq("gameId", gameId));
        System.out.println("Deleted game: " + gameId);
    }
    
    /**
     * Delete all games (be careful!)
     */
    public void deleteAllGames() {
        long count = gamesCollection.countDocuments();
        gamesCollection.drop();
        gamesCollection = database.getCollection("games");
        createIndexes();
        System.out.println("Deleted " + count + " games");
    }
    
    // ==================== UTILITIES ====================
    
    /**
     * Get total number of games
     */
    public long size() {
        return gamesCollection.countDocuments();
    }
    
    /**
     * Convert Game object to MongoDB Document
     */
    private Document gameToDocument(Game game) {
        return new Document()
            .append("gameId", game.getId())
            .append("title", game.getTitle())
            .append("genres", new ArrayList<>(game.getGenres()))
            .append("tags", new ArrayList<>(game.getTags()))
            .append("rating", game.getRating())
            .append("releaseYear", game.getReleaseYear())
            .append("platform", game.getPlatform())
            .append("playtimeHours", game.getPlaytimeHours())
            .append("price", game.getPrice())
            .append("multiplayer", game.isMultiplayer())
            .append("difficulty", game.getDifficulty());
    }
    
    /**
     * Convert MongoDB Document to Game object
     */
    @SuppressWarnings("unchecked")
    private Game documentToGame(Document doc) {
        Game game = new Game(
            doc.getString("gameId"),
            doc.getString("title")
        );
        
        List<String> genres = (List<String>) doc.get("genres");
        if (genres != null) {
            game.withGenres(genres.toArray(new String[0]));
        }
        
        List<String> tags = (List<String>) doc.get("tags");
        if (tags != null) {
            game.withTags(tags.toArray(new String[0]));
        }
        
        if (doc.getDouble("rating") != null) {
            game.withRating(doc.getDouble("rating"));
        }
        if (doc.getInteger("releaseYear") != null) {
            game.withReleaseYear(doc.getInteger("releaseYear"));
        }
        if (doc.getString("platform") != null) {
            game.withPlatform(doc.getString("platform"));
        }
        if (doc.getInteger("playtimeHours") != null) {
            game.withPlaytime(doc.getInteger("playtimeHours"));
        }
        if (doc.getDouble("price") != null) {
            game.withPrice(doc.getDouble("price"));
        }
        if (doc.getBoolean("multiplayer") != null) {
            game.withMultiplayer(doc.getBoolean("multiplayer"));
        }
        if (doc.getString("difficulty") != null) {
            game.withDifficulty(doc.getString("difficulty"));
        }
        
        return game;
    }
    
    /**
     * Close the MongoDB connection
     */
    public void close() {
        if (mongoClient != null) {
            mongoClient.close();
            System.out.println("MongoDB connection closed");
        }
    }
    
    // Vocabulary getters for feature encoding
    public List<String> getAllGenres() { return allGenres; }
    public List<String> getAllTags() { return allTags; }
    public List<String> getAllDifficulties() { return allDifficulties; }
    public List<String> getAllPlatforms() { return allPlatforms; }
    
    public int getFeatureVectorSize() {
        return allGenres.size() + allTags.size() + allDifficulties.size() + 
               allPlatforms.size() + 5;
    }
}
