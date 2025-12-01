package com.gamerecommender.model;

import java.util.*;

/**
 * Represents a user's profile with their preferences and play history.
 */
public class UserProfile {
    private String userId;
    private String username;
    private Map<String, Double> genrePreferences;
    private Map<String, Double> tagPreferences;
    private Map<String, Double> playedGames; // gameId -> rating

    public UserProfile(String userId, String username) {
        this.userId = userId;
        this.username = username;
        this.genrePreferences = new HashMap<>();
        this.tagPreferences = new HashMap<>();
        this.playedGames = new HashMap<>();
    }

    public void addPlayedGame(String gameId, double rating) {
        playedGames.put(gameId, rating);
    }

    public void setGenrePreference(String genre, double weight) {
        genrePreferences.put(genre.toLowerCase(), Math.max(0, Math.min(1, weight)));
    }

    public void setTagPreference(String tag, double weight) {
        tagPreferences.put(tag.toLowerCase(), Math.max(0, Math.min(1, weight)));
    }

    /**
     * Learn preferences from a game the user rated.
     * Updates genre and tag weights based on rating.
     */
    public void learnFromGame(Game game, double userRating) {
        addPlayedGame(game.getId(), userRating);
        
        double normalizedRating = userRating / 10.0;
        double learningRate = 0.3;
        
        for (String genre : game.getGenres()) {
            String g = genre.toLowerCase();
            double current = genrePreferences.getOrDefault(g, 0.5);
            double updated = current + learningRate * (normalizedRating - current);
            genrePreferences.put(g, updated);
        }
        
        for (String tag : game.getTags()) {
            String t = tag.toLowerCase();
            double current = tagPreferences.getOrDefault(t, 0.5);
            double updated = current + learningRate * (normalizedRating - current);
            tagPreferences.put(t, updated);
        }
    }

    public boolean hasPlayed(String gameId) {
        return playedGames.containsKey(gameId);
    }

    // Getters
    public String getUserId() { return userId; }
    public String getUsername() { return username; }
    public Map<String, Double> getGenrePreferences() { return genrePreferences; }
    public Map<String, Double> getTagPreferences() { return tagPreferences; }
    public Map<String, Double> getPlayedGames() { return playedGames; }

    @Override
    public String toString() {
        return String.format("User: %s | Games Played: %d", username, playedGames.size());
    }
}
