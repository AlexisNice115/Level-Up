package com.gamerecommender;

import com.gamerecommender.model.Game;
import com.gamerecommender.data.MongoGameDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Loads the full game database (119 games) into MongoDB.
 */
public class LoadFullDatabase {
    
    public static void main(String[] args) {
        System.out.println("╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║         LOADING FULL GAME DATABASE (119 Games)            ║");
        System.out.println("╚═══════════════════════════════════════════════════════════╝\n");
        
        MongoGameDatabase db = null;
        
        try {
            db = new MongoGameDatabase();
            
            // Clear existing games
            if (db.size() > 0) {
                System.out.println("Clearing existing " + db.size() + " games...");
                db.deleteAllGames();
            }
            
            // Insert all games
            List<Game> games = getAllGames();
            db.insertGames(games);
            
            System.out.println("\n✓ Successfully loaded " + db.size() + " games!");
            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (db != null) db.close();
        }
    }
    
    public static List<Game> getAllGames() {
        List<Game> games = new ArrayList<>();
        
        // Game 1
        games.add(new Game("g001", "The Last of Us")
            .withGenres("Action-Adventure", "Survival", "Post-Apocalyptic")
            .withTags("story-rich", "emotional", "singleplayer", "third-person")
            .withRating(9.5).withReleaseYear(2013).withPlatform("PlayStation Store / Steam")
            .withPlaytime(15).withPrice(59.99).withMultiplayer(false).withDifficulty("medium"));
        
        // Game 2
        games.add(new Game("g002", "Ghost of Tsushima")
            .withGenres("Action-Adventure", "Open-World/Sandbox", "Stealth")
            .withTags("samurai", "story-rich", "exploration", "combat")
            .withRating(8.7).withReleaseYear(2020).withPlatform("PlayStation Store / Steam")
            .withPlaytime(50).withPrice(59.99).withMultiplayer(false).withDifficulty("medium"));
        
        // Game 3
        games.add(new Game("g003", "Detroit: Become Human")
            .withGenres("Adventure", "Interactive Movie", "Plot with Many Endings")
            .withTags("story-rich", "choices-matter", "narrative", "cinematic")
            .withRating(7.8).withReleaseYear(2018).withPlatform("PlayStation Store / Steam")
            .withPlaytime(12).withPrice(39.99).withMultiplayer(false).withDifficulty("easy"));
        
        // Game 4
        games.add(new Game("g004", "Counter-Strike 2")
            .withGenres("Shooter", "Tactical", "Competitive")
            .withTags("fps", "esports", "pvp", "team-based")
            .withRating(8.0).withReleaseYear(2023).withPlatform("Steam")
            .withPlaytime(500).withPrice(0.00).withMultiplayer(true).withDifficulty("hard"));
        
        // Game 5
        games.add(new Game("g005", "Call of Duty: Modern Warfare II")
            .withGenres("Shooter", "First-Person", "Action")
            .withTags("fps", "military", "campaign", "multiplayer")
            .withRating(7.6).withReleaseYear(2022).withPlatform("Steam / Epic Games Store")
            .withPlaytime(100).withPrice(69.99).withMultiplayer(true).withDifficulty("medium"));
        
        // Game 6
        games.add(new Game("g006", "Overwatch 2")
            .withGenres("Shooter", "Hero Shooter", "Multiplayer")
            .withTags("team-based", "competitive", "colorful", "fast-paced")
            .withRating(7.9).withReleaseYear(2022).withPlatform("Battle.net")
            .withPlaytime(200).withPrice(0.00).withMultiplayer(true).withDifficulty("medium"));
        
        // Game 7
        games.add(new Game("g007", "Assassin's Creed Mirage")
            .withGenres("Action-Adventure", "Stealth", "Open-World/Sandbox")
            .withTags("parkour", "assassination", "historical", "story-rich")
            .withRating(7.7).withReleaseYear(2023).withPlatform("Ubisoft Connect / Epic Games Store")
            .withPlaytime(20).withPrice(49.99).withMultiplayer(false).withDifficulty("medium"));
        
        // Game 8
        games.add(new Game("g008", "Assassin's Creed Valhalla")
            .withGenres("Action-Adventure", "Open-World/Sandbox", "RPG")
            .withTags("vikings", "exploration", "combat", "historical")
            .withRating(8.0).withReleaseYear(2020).withPlatform("Ubisoft Connect / Epic Games Store")
            .withPlaytime(60).withPrice(59.99).withMultiplayer(false).withDifficulty("medium"));
        
        // Game 9
        games.add(new Game("g009", "Assassin's Creed Origins")
            .withGenres("Action-Adventure", "RPG", "Open-World/Sandbox")
            .withTags("egypt", "exploration", "combat", "historical")
            .withRating(8.4).withReleaseYear(2017).withPlatform("Steam / Ubisoft Connect")
            .withPlaytime(50).withPrice(59.99).withMultiplayer(false).withDifficulty("medium"));
        
        // Game 10
        games.add(new Game("g010", "Red Dead Redemption 2")
            .withGenres("Action-Adventure", "Open-World/Sandbox", "Western")
            .withTags("story-rich", "exploration", "realistic", "cowboy")
            .withRating(9.6).withReleaseYear(2018).withPlatform("Steam / Rockstar Games Launcher")
            .withPlaytime(60).withPrice(59.99).withMultiplayer(true).withDifficulty("medium"));
        
        // Game 11
        games.add(new Game("g011", "Grand Theft Auto V")
            .withGenres("Open-World/Sandbox", "Action-Adventure", "Crime")
            .withTags("open-world", "multiplayer", "heists", "driving")
            .withRating(9.7).withReleaseYear(2013).withPlatform("Steam / Rockstar Games Launcher")
            .withPlaytime(100).withPrice(29.99).withMultiplayer(true).withDifficulty("medium"));
        
        // Game 12
        games.add(new Game("g012", "Elden Ring")
            .withGenres("RPG", "Soulsborne", "Open-World/Sandbox")
            .withTags("challenging", "dark-fantasy", "exploration", "boss-battles")
            .withRating(9.6).withReleaseYear(2022).withPlatform("Steam")
            .withPlaytime(80).withPrice(59.99).withMultiplayer(true).withDifficulty("very_hard"));
        
        // Game 13
        games.add(new Game("g013", "Dark Souls III")
            .withGenres("RPG", "Soulsborne", "Action")
            .withTags("challenging", "dark-fantasy", "boss-battles", "atmospheric")
            .withRating(8.9).withReleaseYear(2016).withPlatform("Steam")
            .withPlaytime(50).withPrice(59.99).withMultiplayer(true).withDifficulty("very_hard"));
        
        // Game 14
        games.add(new Game("g014", "Sekiro: Shadows Die Twice")
            .withGenres("Action-Adventure", "Soulsborne", "Singleplayer")
            .withTags("challenging", "ninja", "japanese", "combat")
            .withRating(9.0).withReleaseYear(2019).withPlatform("Steam")
            .withPlaytime(35).withPrice(59.99).withMultiplayer(false).withDifficulty("very_hard"));
        
        // Game 15
        games.add(new Game("g015", "Hades")
            .withGenres("Roguelike", "Action", "Indie")
            .withTags("fast-paced", "mythology", "story-rich", "replayable")
            .withRating(9.3).withReleaseYear(2018).withPlatform("Steam / Epic Games Store")
            .withPlaytime(50).withPrice(24.99).withMultiplayer(false).withDifficulty("medium"));
        
        // Game 16
        games.add(new Game("g016", "Honkai: Star Rail")
            .withGenres("RPG", "Turn-Based", "Gacha")
            .withTags("anime", "story-rich", "free-to-play", "sci-fi")
            .withRating(8.2).withReleaseYear(2023).withPlatform("Epic Games Store")
            .withPlaytime(100).withPrice(0.00).withMultiplayer(false).withDifficulty("easy"));
        
        // Game 17
        games.add(new Game("g017", "Genshin Impact")
            .withGenres("RPG", "Open-World/Sandbox", "Gacha")
            .withTags("anime", "exploration", "free-to-play", "fantasy")
            .withRating(8.6).withReleaseYear(2020).withPlatform("Epic Games Store")
            .withPlaytime(200).withPrice(0.00).withMultiplayer(true).withDifficulty("easy"));
        
        // Game 18
        games.add(new Game("g018", "Valorant")
            .withGenres("Shooter", "Tactical", "Competitive")
            .withTags("fps", "esports", "team-based", "abilities")
            .withRating(8.0).withReleaseYear(2020).withPlatform("Riot Client")
            .withPlaytime(500).withPrice(0.00).withMultiplayer(true).withDifficulty("hard"));
        
        // Game 19
        games.add(new Game("g019", "League of Legends")
            .withGenres("MOBA", "Competitive", "Team-Based")
            .withTags("esports", "strategy", "free-to-play", "pvp")
            .withRating(7.8).withReleaseYear(2009).withPlatform("Riot Client")
            .withPlaytime(1000).withPrice(0.00).withMultiplayer(true).withDifficulty("hard"));
        
        // Game 20
        games.add(new Game("g020", "Dota 2")
            .withGenres("MOBA", "Competitive", "Team-Based")
            .withTags("esports", "strategy", "free-to-play", "complex")
            .withRating(9.0).withReleaseYear(2013).withPlatform("Steam")
            .withPlaytime(1000).withPrice(0.00).withMultiplayer(true).withDifficulty("very_hard"));
        
        // Game 21
        games.add(new Game("g021", "Apex Legends")
            .withGenres("Shooter", "Battle Royale", "Hero Shooter")
            .withTags("fast-paced", "team-based", "free-to-play", "abilities")
            .withRating(8.8).withReleaseYear(2019).withPlatform("Steam / EA App")
            .withPlaytime(200).withPrice(0.00).withMultiplayer(true).withDifficulty("medium"));
        
        // Game 22
        games.add(new Game("g022", "Fortnite")
            .withGenres("Shooter", "Battle Royale", "Building")
            .withTags("building", "free-to-play", "crossover", "colorful")
            .withRating(8.5).withReleaseYear(2017).withPlatform("Epic Games Store")
            .withPlaytime(300).withPrice(0.00).withMultiplayer(true).withDifficulty("medium"));
        
        // Game 23
        games.add(new Game("g023", "Minecraft")
            .withGenres("Sandbox", "Survival", "Creative")
            .withTags("building", "exploration", "crafting", "relaxing")
            .withRating(9.3).withReleaseYear(2011).withPlatform("Minecraft Launcher")
            .withPlaytime(500).withPrice(29.99).withMultiplayer(true).withDifficulty("easy"));
        
        // Game 24
        games.add(new Game("g024", "Stardew Valley")
            .withGenres("Simulation", "Farming", "Indie")
            .withTags("relaxing", "farming", "life-sim", "wholesome")
            .withRating(8.9).withReleaseYear(2016).withPlatform("Steam / GOG")
            .withPlaytime(100).withPrice(14.99).withMultiplayer(true).withDifficulty("easy"));
        
        // Game 25
        games.add(new Game("g025", "The Witcher 3: Wild Hunt")
            .withGenres("RPG", "Open-World/Sandbox", "Fantasy")
            .withTags("story-rich", "choices-matter", "monsters", "medieval")
            .withRating(9.3).withReleaseYear(2015).withPlatform("Steam / GOG")
            .withPlaytime(100).withPrice(39.99).withMultiplayer(false).withDifficulty("medium"));
        
        // Game 26
        games.add(new Game("g026", "Cyberpunk 2077")
            .withGenres("RPG", "Open-World/Sandbox", "Sci-Fi")
            .withTags("cyberpunk", "story-rich", "futuristic", "choices-matter")
            .withRating(8.6).withReleaseYear(2020).withPlatform("Steam / GOG / Epic Games Store")
            .withPlaytime(60).withPrice(59.99).withMultiplayer(false).withDifficulty("medium"));
        
        // Game 27
        games.add(new Game("g027", "Horizon Zero Dawn")
            .withGenres("Action-Adventure", "Open-World/Sandbox", "Sci-Fi")
            .withTags("robots", "post-apocalyptic", "exploration", "story-rich")
            .withRating(8.9).withReleaseYear(2017).withPlatform("Steam / Epic Games Store")
            .withPlaytime(40).withPrice(49.99).withMultiplayer(false).withDifficulty("medium"));
        
        // Game 28
        games.add(new Game("g028", "Horizon Forbidden West")
            .withGenres("Action-Adventure", "Open-World/Sandbox", "Sci-Fi")
            .withTags("robots", "exploration", "story-rich", "beautiful")
            .withRating(8.8).withReleaseYear(2022).withPlatform("PlayStation Store")
            .withPlaytime(50).withPrice(59.99).withMultiplayer(false).withDifficulty("medium"));
        
        // Game 29
        games.add(new Game("g029", "Forza Horizon 5")
            .withGenres("Racing", "Open-World/Sandbox", "Simulation")
            .withTags("cars", "mexico", "arcade", "multiplayer")
            .withRating(9.2).withReleaseYear(2021).withPlatform("Steam / Microsoft Store")
            .withPlaytime(100).withPrice(59.99).withMultiplayer(true).withDifficulty("easy"));
        
        // Game 30
        games.add(new Game("g030", "Gran Turismo 7")
            .withGenres("Racing", "Simulation", "Sports")
            .withTags("realistic", "cars", "career", "graphics")
            .withRating(8.7).withReleaseYear(2022).withPlatform("PlayStation Store")
            .withPlaytime(80).withPrice(69.99).withMultiplayer(true).withDifficulty("medium"));
        
        // Game 31
        games.add(new Game("g031", "FIFA 23")
            .withGenres("Sports", "Football/Soccer", "Multiplayer")
            .withTags("soccer", "football", "competitive", "annual")
            .withRating(7.9).withReleaseYear(2022).withPlatform("Steam / EA App")
            .withPlaytime(100).withPrice(59.99).withMultiplayer(true).withDifficulty("medium"));
        
        // Game 32
        games.add(new Game("g032", "NBA 2K24")
            .withGenres("Sports", "Basketball", "Multiplayer")
            .withTags("basketball", "career", "competitive", "simulation")
            .withRating(7.2).withReleaseYear(2023).withPlatform("Steam")
            .withPlaytime(100).withPrice(59.99).withMultiplayer(true).withDifficulty("medium"));
        
        // Game 33
        games.add(new Game("g033", "Madden NFL 24")
            .withGenres("Sports", "American Football", "Simulation")
            .withTags("football", "nfl", "career", "franchise")
            .withRating(6.8).withReleaseYear(2023).withPlatform("Steam / EA App")
            .withPlaytime(100).withPrice(59.99).withMultiplayer(true).withDifficulty("medium"));
        
        // Game 34
        games.add(new Game("g034", "The Sims 4")
            .withGenres("Simulation", "Life Simulation", "Creative")
            .withTags("life-sim", "building", "creative", "relaxing")
            .withRating(7.0).withReleaseYear(2014).withPlatform("Steam / EA App")
            .withPlaytime(200).withPrice(0.00).withMultiplayer(false).withDifficulty("easy"));
        
        // Game 35
        games.add(new Game("g035", "Cities: Skylines")
            .withGenres("Simulation", "City Builder", "Strategy")
            .withTags("city-builder", "management", "sandbox", "relaxing")
            .withRating(8.5).withReleaseYear(2015).withPlatform("Steam")
            .withPlaytime(100).withPrice(29.99).withMultiplayer(false).withDifficulty("medium"));
        
        // Game 36
        games.add(new Game("g036", "Civilization VI")
            .withGenres("Strategy", "Turn-Based", "4X")
            .withTags("historical", "empire-building", "addictive", "strategic")
            .withRating(8.8).withReleaseYear(2016).withPlatform("Steam / Epic Games Store")
            .withPlaytime(200).withPrice(59.99).withMultiplayer(true).withDifficulty("medium"));
        
        // Game 37
        games.add(new Game("g037", "Total War: Warhammer III")
            .withGenres("Strategy", "RTS", "Turn-Based")
            .withTags("fantasy", "warfare", "epic-battles", "warhammer")
            .withRating(8.6).withReleaseYear(2022).withPlatform("Steam / Epic Games Store")
            .withPlaytime(150).withPrice(59.99).withMultiplayer(true).withDifficulty("hard"));
        
        // Game 38
        games.add(new Game("g038", "Starcraft II")
            .withGenres("RTS", "Sci-Fi", "Competitive")
            .withTags("esports", "strategy", "micro-management", "classic")
            .withRating(9.3).withReleaseYear(2010).withPlatform("Battle.net")
            .withPlaytime(200).withPrice(0.00).withMultiplayer(true).withDifficulty("very_hard"));
        
        // Game 39
        games.add(new Game("g039", "World of Warcraft")
            .withGenres("MMORPG", "Fantasy", "Online")
            .withTags("mmo", "raids", "dungeons", "social")
            .withRating(8.9).withReleaseYear(2004).withPlatform("Battle.net")
            .withPlaytime(1000).withPrice(14.99).withMultiplayer(true).withDifficulty("medium"));
        
        // Game 40
        games.add(new Game("g040", "Final Fantasy XIV")
            .withGenres("MMORPG", "Fantasy", "Online")
            .withTags("story-rich", "mmo", "raids", "jrpg")
            .withRating(8.9).withReleaseYear(2013).withPlatform("Steam / Square Enix Launcher")
            .withPlaytime(500).withPrice(0.00).withMultiplayer(true).withDifficulty("medium"));
        
        // Game 41
        games.add(new Game("g041", "Path of Exile")
            .withGenres("RPG", "Action", "Online")
            .withTags("arpg", "loot", "complex", "free-to-play")
            .withRating(8.6).withReleaseYear(2013).withPlatform("Steam")
            .withPlaytime(500).withPrice(0.00).withMultiplayer(true).withDifficulty("hard"));
        
        // Game 42
        games.add(new Game("g042", "Diablo IV")
            .withGenres("RPG", "Action", "Online")
            .withTags("arpg", "loot", "dark-fantasy", "demons")
            .withRating(8.5).withReleaseYear(2023).withPlatform("Battle.net")
            .withPlaytime(100).withPrice(69.99).withMultiplayer(true).withDifficulty("medium"));
        
        // Game 43
        games.add(new Game("g043", "Lost Ark")
            .withGenres("MMORPG", "Action", "Online")
            .withTags("arpg", "mmo", "raids", "free-to-play")
            .withRating(8.1).withReleaseYear(2019).withPlatform("Steam")
            .withPlaytime(300).withPrice(0.00).withMultiplayer(true).withDifficulty("medium"));
        
        // Game 44
        games.add(new Game("g044", "Terraria")
            .withGenres("Sandbox", "Adventure", "Survival")
            .withTags("crafting", "exploration", "2d", "boss-battles")
            .withRating(9.1).withReleaseYear(2011).withPlatform("Steam")
            .withPlaytime(200).withPrice(9.99).withMultiplayer(true).withDifficulty("medium"));
        
        // Game 45
        games.add(new Game("g045", "Don't Starve Together")
            .withGenres("Survival", "Co-op", "Indie")
            .withTags("survival", "crafting", "dark", "challenging")
            .withRating(8.2).withReleaseYear(2016).withPlatform("Steam")
            .withPlaytime(100).withPrice(14.99).withMultiplayer(true).withDifficulty("hard"));
        
        // Game 46
        games.add(new Game("g046", "Rust")
            .withGenres("Survival", "Multiplayer", "Sandbox")
            .withTags("pvp", "crafting", "base-building", "hardcore")
            .withRating(7.6).withReleaseYear(2018).withPlatform("Steam")
            .withPlaytime(500).withPrice(39.99).withMultiplayer(true).withDifficulty("hard"));
        
        // Game 47
        games.add(new Game("g047", "ARK: Survival Evolved")
            .withGenres("Survival", "Open-World/Sandbox", "Dinosaur")
            .withTags("dinosaurs", "taming", "base-building", "multiplayer")
            .withRating(7.0).withReleaseYear(2017).withPlatform("Steam / Epic Games Store")
            .withPlaytime(300).withPrice(29.99).withMultiplayer(true).withDifficulty("hard"));
        
        // Game 48
        games.add(new Game("g048", "Subnautica")
            .withGenres("Survival", "Adventure", "Underwater")
            .withTags("underwater", "exploration", "crafting", "atmospheric")
            .withRating(8.7).withReleaseYear(2018).withPlatform("Steam / Epic Games Store")
            .withPlaytime(40).withPrice(29.99).withMultiplayer(false).withDifficulty("medium"));
        
        // Game 49
        games.add(new Game("g049", "No Man's Sky")
            .withGenres("Adventure", "Exploration", "Sci-Fi")
            .withTags("space", "procedural", "exploration", "survival")
            .withRating(7.8).withReleaseYear(2016).withPlatform("Steam")
            .withPlaytime(100).withPrice(59.99).withMultiplayer(true).withDifficulty("easy"));
        
        // Game 50
        games.add(new Game("g050", "Factorio")
            .withGenres("Simulation", "Automation", "Strategy")
            .withTags("factory-building", "automation", "addictive", "complex")
            .withRating(9.1).withReleaseYear(2020).withPlatform("Steam")
            .withPlaytime(200).withPrice(35.00).withMultiplayer(true).withDifficulty("hard"));
        
        // Game 51
        games.add(new Game("g051", "Satisfactory")
            .withGenres("Simulation", "Automation", "First-Person")
            .withTags("factory-building", "exploration", "3d", "co-op")
            .withRating(8.5).withReleaseYear(2020).withPlatform("Steam / Epic Games Store")
            .withPlaytime(150).withPrice(29.99).withMultiplayer(true).withDifficulty("medium"));
        
        // Game 52
        games.add(new Game("g052", "RimWorld")
            .withGenres("Simulation", "Colony Sim", "Strategy")
            .withTags("colony-management", "storytelling", "complex", "addictive")
            .withRating(8.7).withReleaseYear(2018).withPlatform("Steam")
            .withPlaytime(200).withPrice(34.99).withMultiplayer(false).withDifficulty("hard"));
        
        // Game 53
        games.add(new Game("g053", "Crusader Kings III")
            .withGenres("Strategy", "Grand Strategy", "Simulation")
            .withTags("medieval", "dynasty", "roleplay", "complex")
            .withRating(9.1).withReleaseYear(2020).withPlatform("Steam")
            .withPlaytime(300).withPrice(49.99).withMultiplayer(true).withDifficulty("hard"));
        
        // Game 54
        games.add(new Game("g054", "Europa Universalis IV")
            .withGenres("Strategy", "Grand Strategy", "Historical")
            .withTags("historical", "empire-building", "complex", "diplomacy")
            .withRating(8.7).withReleaseYear(2013).withPlatform("Steam")
            .withPlaytime(500).withPrice(39.99).withMultiplayer(true).withDifficulty("very_hard"));
        
        // Game 55
        games.add(new Game("g055", "Hearts of Iron IV")
            .withGenres("Strategy", "Grand Strategy", "War")
            .withTags("ww2", "warfare", "historical", "complex")
            .withRating(8.3).withReleaseYear(2016).withPlatform("Steam")
            .withPlaytime(300).withPrice(39.99).withMultiplayer(true).withDifficulty("hard"));
        
        // Game 56
        games.add(new Game("g056", "Planet Zoo")
            .withGenres("Simulation", "Management", "Building")
            .withTags("zoo", "animals", "creative", "management")
            .withRating(8.1).withReleaseYear(2019).withPlatform("Steam")
            .withPlaytime(100).withPrice(44.99).withMultiplayer(false).withDifficulty("medium"));
        
        // Game 57
        games.add(new Game("g057", "Planet Coaster")
            .withGenres("Simulation", "Management", "Building")
            .withTags("theme-park", "creative", "building", "management")
            .withRating(8.4).withReleaseYear(2016).withPlatform("Steam")
            .withPlaytime(100).withPrice(44.99).withMultiplayer(false).withDifficulty("medium"));
        
        // Game 58
        games.add(new Game("g058", "RollerCoaster Tycoon Classic")
            .withGenres("Simulation", "Management", "Building")
            .withTags("theme-park", "classic", "nostalgic", "management")
            .withRating(8.5).withReleaseYear(2016).withPlatform("Steam / Mobile")
            .withPlaytime(50).withPrice(5.99).withMultiplayer(false).withDifficulty("medium"));
        
        // Game 59
        games.add(new Game("g059", "Age of Empires IV")
            .withGenres("RTS", "Historical", "Strategy")
            .withTags("medieval", "warfare", "base-building", "campaigns")
            .withRating(8.1).withReleaseYear(2021).withPlatform("Steam / Microsoft Store")
            .withPlaytime(50).withPrice(59.99).withMultiplayer(true).withDifficulty("medium"));
        
        // Game 60
        games.add(new Game("g060", "Age of Empires II: Definitive Edition")
            .withGenres("RTS", "Historical", "Strategy")
            .withTags("classic", "medieval", "warfare", "remastered")
            .withRating(9.0).withReleaseYear(2019).withPlatform("Steam / Microsoft Store")
            .withPlaytime(100).withPrice(19.99).withMultiplayer(true).withDifficulty("medium"));
        
        // Game 61
        games.add(new Game("g061", "Halo Infinite")
            .withGenres("Shooter", "Sci-Fi", "Multiplayer")
            .withTags("fps", "sci-fi", "arena", "campaign")
            .withRating(8.0).withReleaseYear(2021).withPlatform("Steam / Microsoft Store")
            .withPlaytime(50).withPrice(0.00).withMultiplayer(true).withDifficulty("medium"));
        
        // Game 62
        games.add(new Game("g062", "DOOM Eternal")
            .withGenres("Shooter", "Action", "Singleplayer")
            .withTags("fps", "fast-paced", "demons", "brutal")
            .withRating(9.0).withReleaseYear(2020).withPlatform("Steam")
            .withPlaytime(20).withPrice(39.99).withMultiplayer(false).withDifficulty("hard"));
        
        // Game 63
        games.add(new Game("g063", "Far Cry 6")
            .withGenres("Shooter", "Open-World/Sandbox", "Action")
            .withTags("fps", "guerrilla", "exploration", "chaos")
            .withRating(7.5).withReleaseYear(2021).withPlatform("Ubisoft Connect / Epic Games Store")
            .withPlaytime(30).withPrice(59.99).withMultiplayer(true).withDifficulty("medium"));
        
        // Game 64
        games.add(new Game("g064", "Borderlands 3")
            .withGenres("Shooter", "Looter Shooter", "Co-op")
            .withTags("loot", "humor", "co-op", "cel-shaded")
            .withRating(8.1).withReleaseYear(2019).withPlatform("Steam / Epic Games Store")
            .withPlaytime(40).withPrice(59.99).withMultiplayer(true).withDifficulty("medium"));
        
        // Game 65
        games.add(new Game("g065", "Destiny 2")
            .withGenres("Shooter", "Looter Shooter", "Online")
            .withTags("loot", "mmo", "raids", "sci-fi")
            .withRating(8.3).withReleaseYear(2017).withPlatform("Steam")
            .withPlaytime(300).withPrice(0.00).withMultiplayer(true).withDifficulty("medium"));
        
        // Game 66
        games.add(new Game("g066", "Rainbow Six Siege")
            .withGenres("Shooter", "Tactical", "Competitive")
            .withTags("fps", "destruction", "esports", "team-based")
            .withRating(8.2).withReleaseYear(2015).withPlatform("Steam / Ubisoft Connect")
            .withPlaytime(300).withPrice(19.99).withMultiplayer(true).withDifficulty("hard"));
        
        // Game 67
        games.add(new Game("g067", "Warframe")
            .withGenres("Shooter", "Looter Shooter", "Online")
            .withTags("ninja", "free-to-play", "co-op", "grinding")
            .withRating(7.8).withReleaseYear(2013).withPlatform("Steam")
            .withPlaytime(500).withPrice(0.00).withMultiplayer(true).withDifficulty("medium"));
        
        // Game 68
        games.add(new Game("g068", "Monster Hunter: World")
            .withGenres("RPG", "Action", "Co-op")
            .withTags("hunting", "monsters", "crafting", "co-op")
            .withRating(9.0).withReleaseYear(2018).withPlatform("Steam")
            .withPlaytime(100).withPrice(29.99).withMultiplayer(true).withDifficulty("hard"));
        
        // Game 69
        games.add(new Game("g069", "Monster Hunter Rise")
            .withGenres("RPG", "Action", "Co-op")
            .withTags("hunting", "monsters", "fast-paced", "co-op")
            .withRating(8.8).withReleaseYear(2021).withPlatform("Steam")
            .withPlaytime(80).withPrice(39.99).withMultiplayer(true).withDifficulty("medium"));
        
        // Game 70
        games.add(new Game("g070", "Pokémon Legends: Arceus")
            .withGenres("RPG", "Adventure", "Creature Collection")
            .withTags("pokemon", "open-world", "catching", "exploration")
            .withRating(8.3).withReleaseYear(2022).withPlatform("Nintendo Switch")
            .withPlaytime(30).withPrice(59.99).withMultiplayer(false).withDifficulty("easy"));
        
        // Game 71
        games.add(new Game("g071", "Pokémon Scarlet/Violet")
            .withGenres("RPG", "Adventure", "Creature Collection")
            .withTags("pokemon", "open-world", "catching", "multiplayer")
            .withRating(7.2).withReleaseYear(2022).withPlatform("Nintendo Switch")
            .withPlaytime(40).withPrice(59.99).withMultiplayer(true).withDifficulty("easy"));
        
        // Game 72
        games.add(new Game("g072", "Super Mario Odyssey")
            .withGenres("Platformer", "Adventure", "3D Platformer")
            .withTags("mario", "colorful", "collectibles", "family-friendly")
            .withRating(9.7).withReleaseYear(2017).withPlatform("Nintendo Switch")
            .withPlaytime(20).withPrice(59.99).withMultiplayer(false).withDifficulty("easy"));
        
        // Game 73
        games.add(new Game("g073", "The Legend of Zelda: Breath of the Wild")
            .withGenres("Action-Adventure", "Open-World/Sandbox", "Fantasy")
            .withTags("exploration", "puzzles", "combat", "beautiful")
            .withRating(9.7).withReleaseYear(2017).withPlatform("Nintendo Switch")
            .withPlaytime(80).withPrice(59.99).withMultiplayer(false).withDifficulty("medium"));
        
        // Game 74
        games.add(new Game("g074", "The Legend of Zelda: Tears of the Kingdom")
            .withGenres("Action-Adventure", "Open-World/Sandbox", "Fantasy")
            .withTags("exploration", "creativity", "puzzles", "building")
            .withRating(9.6).withReleaseYear(2023).withPlatform("Nintendo Switch")
            .withPlaytime(100).withPrice(69.99).withMultiplayer(false).withDifficulty("medium"));
        
        // Game 75
        games.add(new Game("g075", "Celeste")
            .withGenres("Platformer", "Indie", "Precision Platformer")
            .withTags("challenging", "story-rich", "pixel-art", "speedrun")
            .withRating(9.4).withReleaseYear(2018).withPlatform("Steam / Nintendo Switch")
            .withPlaytime(10).withPrice(19.99).withMultiplayer(false).withDifficulty("very_hard"));
        
        // Game 76
        games.add(new Game("g076", "Hollow Knight")
            .withGenres("Metroidvania", "Indie", "Action")
            .withTags("atmospheric", "challenging", "exploration", "boss-battles")
            .withRating(9.0).withReleaseYear(2017).withPlatform("Steam / Nintendo Switch")
            .withPlaytime(40).withPrice(14.99).withMultiplayer(false).withDifficulty("hard"));
        
        // Game 77
        games.add(new Game("g077", "Ori and the Blind Forest")
            .withGenres("Platformer", "Metroidvania", "Indie")
            .withTags("beautiful", "emotional", "challenging", "artistic")
            .withRating(8.8).withReleaseYear(2015).withPlatform("Steam / Microsoft Store")
            .withPlaytime(10).withPrice(19.99).withMultiplayer(false).withDifficulty("medium"));
        
        // Game 78
        games.add(new Game("g078", "Ori and the Will of the Wisps")
            .withGenres("Platformer", "Metroidvania", "Indie")
            .withTags("beautiful", "emotional", "combat", "artistic")
            .withRating(9.0).withReleaseYear(2020).withPlatform("Steam / Microsoft Store")
            .withPlaytime(15).withPrice(29.99).withMultiplayer(false).withDifficulty("medium"));
        
        // Game 79
        games.add(new Game("g079", "Cuphead")
            .withGenres("Platformer", "Run and Gun", "Indie")
            .withTags("challenging", "retro", "boss-battles", "co-op")
            .withRating(8.8).withReleaseYear(2017).withPlatform("Steam / GOG")
            .withPlaytime(15).withPrice(19.99).withMultiplayer(true).withDifficulty("very_hard"));
        
        // Game 80
        games.add(new Game("g080", "Dead Cells")
            .withGenres("Roguelike", "Metroidvania", "Action")
            .withTags("fast-paced", "challenging", "replayable", "pixel-art")
            .withRating(8.9).withReleaseYear(2018).withPlatform("Steam / Nintendo Switch")
            .withPlaytime(50).withPrice(24.99).withMultiplayer(false).withDifficulty("hard"));
        
        // Game 81
        games.add(new Game("g081", "Slay the Spire")
            .withGenres("Roguelike", "Deckbuilder", "Indie")
            .withTags("cards", "strategy", "replayable", "addictive")
            .withRating(8.9).withReleaseYear(2019).withPlatform("Steam / Nintendo Switch")
            .withPlaytime(100).withPrice(24.99).withMultiplayer(false).withDifficulty("medium"));
        
        // Game 82
        games.add(new Game("g082", "Inscryption")
            .withGenres("Card Game", "Puzzle", "Horror")
            .withTags("cards", "mystery", "creepy", "meta")
            .withRating(8.5).withReleaseYear(2021).withPlatform("Steam")
            .withPlaytime(15).withPrice(19.99).withMultiplayer(false).withDifficulty("medium"));
        
        // Game 83
        games.add(new Game("g083", "Return of the Obra Dinn")
            .withGenres("Puzzle", "Mystery", "Indie")
            .withTags("detective", "unique-art", "deduction", "atmospheric")
            .withRating(8.9).withReleaseYear(2018).withPlatform("Steam")
            .withPlaytime(10).withPrice(19.99).withMultiplayer(false).withDifficulty("medium"));
        
        // Game 84
        games.add(new Game("g084", "Outer Wilds")
            .withGenres("Adventure", "Exploration", "Sci-Fi")
            .withTags("space", "mystery", "time-loop", "exploration")
            .withRating(9.2).withReleaseYear(2019).withPlatform("Steam / Epic Games Store")
            .withPlaytime(20).withPrice(24.99).withMultiplayer(false).withDifficulty("medium"));
        
        // Game 85
        games.add(new Game("g085", "Disco Elysium")
            .withGenres("RPG", "Narrative", "Isometric")
            .withTags("story-rich", "choices-matter", "detective", "unique")
            .withRating(9.5).withReleaseYear(2019).withPlatform("Steam / GOG")
            .withPlaytime(30).withPrice(39.99).withMultiplayer(false).withDifficulty("medium"));
        
        // Game 86
        games.add(new Game("g086", "Baldur's Gate 3")
            .withGenres("RPG", "Turn-Based", "Fantasy")
            .withTags("story-rich", "choices-matter", "co-op", "dnd")
            .withRating(9.6).withReleaseYear(2023).withPlatform("Steam / GOG")
            .withPlaytime(100).withPrice(59.99).withMultiplayer(true).withDifficulty("medium"));
        
        // Game 87
        games.add(new Game("g087", "Lies of P")
            .withGenres("RPG", "Soulsborne", "Action")
            .withTags("challenging", "gothic", "pinocchio", "dark")
            .withRating(8.3).withReleaseYear(2023).withPlatform("Steam")
            .withPlaytime(30).withPrice(59.99).withMultiplayer(false).withDifficulty("very_hard"));
        
        // Game 88
        games.add(new Game("g088", "Wo Long: Fallen Dynasty")
            .withGenres("RPG", "Soulsborne", "Action")
            .withTags("challenging", "chinese-mythology", "combat", "co-op")
            .withRating(7.8).withReleaseYear(2023).withPlatform("Steam")
            .withPlaytime(30).withPrice(59.99).withMultiplayer(true).withDifficulty("very_hard"));
        
        // Game 89
        games.add(new Game("g089", "Remnant II")
            .withGenres("Shooter", "Soulsborne", "Co-op")
            .withTags("challenging", "co-op", "procedural", "boss-battles")
            .withRating(8.0).withReleaseYear(2023).withPlatform("Steam")
            .withPlaytime(30).withPrice(49.99).withMultiplayer(true).withDifficulty("hard"));
        
        // Game 90
        games.add(new Game("g090", "The Division 2")
            .withGenres("Shooter", "Looter Shooter", "Co-op")
            .withTags("loot", "washington-dc", "co-op", "tactical")
            .withRating(8.2).withReleaseYear(2019).withPlatform("Steam / Ubisoft Connect")
            .withPlaytime(80).withPrice(9.99).withMultiplayer(true).withDifficulty("medium"));
        
        // Game 91
        games.add(new Game("g091", "Alan Wake 2")
            .withGenres("Horror", "Adventure", "Narrative")
            .withTags("psychological", "story-rich", "atmospheric", "mystery")
            .withRating(8.9).withReleaseYear(2023).withPlatform("Epic Games Store")
            .withPlaytime(20).withPrice(59.99).withMultiplayer(false).withDifficulty("medium"));
        
        // Game 92
        games.add(new Game("g092", "Resident Evil 4 Remake")
            .withGenres("Horror", "Action", "Survival")
            .withTags("scary", "third-person", "zombies", "classic")
            .withRating(9.3).withReleaseYear(2023).withPlatform("Steam")
            .withPlaytime(20).withPrice(59.99).withMultiplayer(false).withDifficulty("medium"));
        
        // Game 93
        games.add(new Game("g093", "Resident Evil Village")
            .withGenres("Horror", "Action", "Survival")
            .withTags("scary", "first-person", "vampires", "atmospheric")
            .withRating(8.4).withReleaseYear(2021).withPlatform("Steam")
            .withPlaytime(12).withPrice(39.99).withMultiplayer(false).withDifficulty("medium"));
        
        // Game 94
        games.add(new Game("g094", "Dead Space Remake")
            .withGenres("Horror", "Action", "Sci-Fi")
            .withTags("scary", "space", "atmospheric", "necromorphs")
            .withRating(8.9).withReleaseYear(2023).withPlatform("Steam / EA App")
            .withPlaytime(15).withPrice(59.99).withMultiplayer(false).withDifficulty("medium"));
        
        // Game 95
        games.add(new Game("g095", "Silent Hill 2 Remake")
            .withGenres("Horror", "Psychological", "Story-Driven")
            .withTags("psychological", "scary", "atmospheric", "classic")
            .withRating(8.2).withReleaseYear(2024).withPlatform("Steam / PlayStation Store")
            .withPlaytime(12).withPrice(59.99).withMultiplayer(false).withDifficulty("medium"));
        
        // Game 96
        games.add(new Game("g096", "HITMAN World of Assassination")
            .withGenres("Stealth", "Action", "Sandbox")
            .withTags("assassination", "sandbox", "replayable", "creative")
            .withRating(8.8).withReleaseYear(2022).withPlatform("Steam / Epic Games Store")
            .withPlaytime(60).withPrice(69.99).withMultiplayer(false).withDifficulty("medium"));
        
        // Game 97
        games.add(new Game("g097", "Metal Gear Solid V: The Phantom Pain")
            .withGenres("Stealth", "Action-Adventure", "Open-World/Sandbox")
            .withTags("stealth", "tactical", "base-building", "story-rich")
            .withRating(9.1).withReleaseYear(2015).withPlatform("Steam")
            .withPlaytime(50).withPrice(19.99).withMultiplayer(true).withDifficulty("medium"));
        
        // Game 98
        games.add(new Game("g098", "Dishonored 2")
            .withGenres("Stealth", "Action-Adventure", "Immersive Sim")
            .withTags("stealth", "powers", "choices-matter", "atmospheric")
            .withRating(8.8).withReleaseYear(2016).withPlatform("Steam")
            .withPlaytime(15).withPrice(29.99).withMultiplayer(false).withDifficulty("medium"));
        
        // Game 99
        games.add(new Game("g099", "Prey (2017)")
            .withGenres("Shooter", "Immersive Sim", "Sci-Fi")
            .withTags("space-station", "aliens", "exploration", "atmospheric")
            .withRating(8.4).withReleaseYear(2017).withPlatform("Steam")
            .withPlaytime(20).withPrice(29.99).withMultiplayer(false).withDifficulty("medium"));
        
        // Game 100
        games.add(new Game("g100", "Black Myth: Wukong")
            .withGenres("Action-Adventure", "Soulsborne", "Fantasy")
            .withTags("chinese-mythology", "challenging", "beautiful", "combat")
            .withRating(8.8).withReleaseYear(2024).withPlatform("Steam / Epic Games Store")
            .withPlaytime(30).withPrice(59.99).withMultiplayer(false).withDifficulty("hard"));
        
        // Game 101 - Mobile
        games.add(new Game("g101", "Clash Royale")
            .withGenres("Strategy", "Card Game", "Real-Time")
            .withTags("cards", "pvp", "competitive", "mobile")
            .withRating(8.5).withReleaseYear(2016).withPlatform("Google Play / App Store")
            .withPlaytime(100).withPrice(0.00).withMultiplayer(true).withDifficulty("medium"));
        
        // Game 102
        games.add(new Game("g102", "Clash of Clans")
            .withGenres("Strategy", "Base Builder", "Multiplayer")
            .withTags("base-building", "clan-wars", "mobile", "strategic")
            .withRating(8.3).withReleaseYear(2012).withPlatform("Google Play / App Store")
            .withPlaytime(200).withPrice(0.00).withMultiplayer(true).withDifficulty("easy"));
        
        // Game 103
        games.add(new Game("g103", "Brawl Stars")
            .withGenres("Shooter", "MOBA", "Arcade")
            .withTags("fast-paced", "team-based", "mobile", "colorful")
            .withRating(8.2).withReleaseYear(2018).withPlatform("Google Play / App Store")
            .withPlaytime(100).withPrice(0.00).withMultiplayer(true).withDifficulty("easy"));
        
        // Game 104
        games.add(new Game("g104", "PUBG Mobile")
            .withGenres("Shooter", "Battle Royale", "Mobile")
            .withTags("battle-royale", "tactical", "mobile", "survival")
            .withRating(7.8).withReleaseYear(2018).withPlatform("Google Play / App Store")
            .withPlaytime(200).withPrice(0.00).withMultiplayer(true).withDifficulty("medium"));
        
        // Game 105
        games.add(new Game("g105", "Call of Duty: Mobile")
            .withGenres("Shooter", "Action", "Mobile")
            .withTags("fps", "multiplayer", "mobile", "battle-royale")
            .withRating(8.1).withReleaseYear(2019).withPlatform("Google Play / App Store")
            .withPlaytime(150).withPrice(0.00).withMultiplayer(true).withDifficulty("medium"));
        
        // Game 106
        games.add(new Game("g106", "Mobile Legends: Bang Bang")
            .withGenres("MOBA", "Action", "Multiplayer")
            .withTags("moba", "team-based", "mobile", "competitive")
            .withRating(8.0).withReleaseYear(2016).withPlatform("Google Play / App Store")
            .withPlaytime(200).withPrice(0.00).withMultiplayer(true).withDifficulty("medium"));
        
        // Game 107
        games.add(new Game("g107", "League of Legends: Wild Rift")
            .withGenres("MOBA", "Action", "Mobile")
            .withTags("moba", "team-based", "mobile", "esports")
            .withRating(8.3).withReleaseYear(2020).withPlatform("Google Play / App Store")
            .withPlaytime(200).withPrice(0.00).withMultiplayer(true).withDifficulty("medium"));
        
        // Game 108
        games.add(new Game("g108", "Among Us")
            .withGenres("Party", "Social Deduction", "Multiplayer")
            .withTags("deception", "friends", "casual", "fun")
            .withRating(8.0).withReleaseYear(2018).withPlatform("Steam / Google Play / App Store")
            .withPlaytime(50).withPrice(4.99).withMultiplayer(true).withDifficulty("easy"));
        
        // Game 109
        games.add(new Game("g109", "Candy Crush Saga")
            .withGenres("Puzzle", "Match-3", "Casual")
            .withTags("casual", "colorful", "addictive", "mobile")
            .withRating(7.5).withReleaseYear(2012).withPlatform("Google Play / App Store")
            .withPlaytime(100).withPrice(0.00).withMultiplayer(false).withDifficulty("easy"));
        
        // Game 110
        games.add(new Game("g110", "Subway Surfers")
            .withGenres("Arcade", "Endless Runner", "Casual")
            .withTags("runner", "casual", "colorful", "mobile")
            .withRating(7.8).withReleaseYear(2012).withPlatform("Google Play / App Store")
            .withPlaytime(50).withPrice(0.00).withMultiplayer(false).withDifficulty("easy"));
        
        // Game 111
        games.add(new Game("g111", "Temple Run 2")
            .withGenres("Arcade", "Endless Runner", "Casual")
            .withTags("runner", "casual", "obstacles", "mobile")
            .withRating(7.6).withReleaseYear(2013).withPlatform("Google Play / App Store")
            .withPlaytime(30).withPrice(0.00).withMultiplayer(false).withDifficulty("easy"));
        
        // Game 112
        games.add(new Game("g112", "8 Ball Pool")
            .withGenres("Sports", "Billiards", "Casual")
            .withTags("pool", "pvp", "casual", "mobile")
            .withRating(8.0).withReleaseYear(2010).withPlatform("Google Play / App Store")
            .withPlaytime(100).withPrice(0.00).withMultiplayer(true).withDifficulty("easy"));
        
        // Game 113
        games.add(new Game("g113", "FIFA Mobile")
            .withGenres("Sports", "Football/Soccer", "Mobile")
            .withTags("soccer", "football", "mobile", "simulation")
            .withRating(7.3).withReleaseYear(2016).withPlatform("Google Play / App Store")
            .withPlaytime(100).withPrice(0.00).withMultiplayer(true).withDifficulty("easy"));
        
        // Game 114
        games.add(new Game("g114", "Asphalt 9: Legends")
            .withGenres("Racing", "Arcade", "Mobile")
            .withTags("cars", "racing", "graphics", "mobile")
            .withRating(8.1).withReleaseYear(2018).withPlatform("Google Play / App Store")
            .withPlaytime(100).withPrice(0.00).withMultiplayer(true).withDifficulty("easy"));
        
        // Game 115
        games.add(new Game("g115", "Genshin Impact (Mobile)")
            .withGenres("RPG", "Open-World/Sandbox", "Gacha")
            .withTags("anime", "exploration", "free-to-play", "mobile")
            .withRating(8.6).withReleaseYear(2020).withPlatform("Google Play / App Store")
            .withPlaytime(200).withPrice(0.00).withMultiplayer(true).withDifficulty("easy"));
        
        // Game 116
        games.add(new Game("g116", "Honkai: Star Rail (Mobile)")
            .withGenres("RPG", "Turn-Based", "Gacha")
            .withTags("anime", "story-rich", "free-to-play", "mobile")
            .withRating(8.2).withReleaseYear(2023).withPlatform("Google Play / App Store")
            .withPlaytime(100).withPrice(0.00).withMultiplayer(false).withDifficulty("easy"));
        
        // Game 117
        games.add(new Game("g117", "Royal Match")
            .withGenres("Puzzle", "Match-3", "Casual")
            .withTags("casual", "colorful", "relaxing", "mobile")
            .withRating(7.8).withReleaseYear(2021).withPlatform("Google Play / App Store")
            .withPlaytime(50).withPrice(0.00).withMultiplayer(false).withDifficulty("easy"));
        
        // Game 118
        games.add(new Game("g118", "Roblox")
            .withGenres("Sandbox", "User-Generated", "Online")
            .withTags("creative", "social", "multiplayer", "family-friendly")
            .withRating(8.0).withReleaseYear(2006).withPlatform("Google Play / App Store / PC")
            .withPlaytime(500).withPrice(0.00).withMultiplayer(true).withDifficulty("easy"));
        
        // Game 119
        games.add(new Game("g119", "Hay Day")
            .withGenres("Simulation", "Farming", "Casual Farming")
            .withTags("farming", "relaxing", "mobile", "casual")
            .withRating(8.0).withReleaseYear(2012).withPlatform("Google Play / App Store")
            .withPlaytime(200).withPrice(0.00).withMultiplayer(false).withDifficulty("easy"));
        
        return games;
    }
}
