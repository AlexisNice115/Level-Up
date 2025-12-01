package com.gamerecommender.model;

import java.util.*;

/**
 * Represents a game in the database.
 */
public class Game {
    private String id;
    private String title;
    private Set<String> genres;
    private Set<String> tags;
    private double rating;
    private int releaseYear;
    private String platform;
    private int playtimeHours;
    private double price;
    private boolean multiplayer;
    private String difficulty;

    public Game(String id, String title) {
        this.id = id;
        this.title = title;
        this.genres = new HashSet<>();
        this.tags = new HashSet<>();
    }

    // Builder pattern
    public Game withGenres(String... genres) {
        this.genres.addAll(Arrays.asList(genres));
        return this;
    }

    public Game withTags(String... tags) {
        this.tags.addAll(Arrays.asList(tags));
        return this;
    }

    public Game withRating(double rating) {
        this.rating = rating;
        return this;
    }

    public Game withReleaseYear(int year) {
        this.releaseYear = year;
        return this;
    }

    public Game withPlatform(String platform) {
        this.platform = platform;
        return this;
    }

    public Game withPlaytime(int hours) {
        this.playtimeHours = hours;
        return this;
    }

    public Game withPrice(double price) {
        this.price = price;
        return this;
    }

    public Game withMultiplayer(boolean multiplayer) {
        this.multiplayer = multiplayer;
        return this;
    }

    public Game withDifficulty(String difficulty) {
        this.difficulty = difficulty;
        return this;
    }

    // Getters
    public String getId() { return id; }
    public String getTitle() { return title; }
    public Set<String> getGenres() { return genres; }
    public Set<String> getTags() { return tags; }
    public double getRating() { return rating; }
    public int getReleaseYear() { return releaseYear; }
    public String getPlatform() { return platform; }
    public int getPlaytimeHours() { return playtimeHours; }
    public double getPrice() { return price; }
    public boolean isMultiplayer() { return multiplayer; }
    public String getDifficulty() { return difficulty; }

    @Override
    public String toString() {
        return String.format("%s (%d) - Rating: %.1f", title, releaseYear, rating);
    }
}
