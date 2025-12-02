-- MySQL schema for Level-Up (compatible with MySQL 5.7+ / 8.0)
-- Use application-side UUID generation (UUID()) when inserting records.

-- Recommended charset / collation
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 1;

-- USERS: authentication + basic profile
CREATE TABLE IF NOT EXISTS users (
  user_id INT NOT NULL PRIMARY KEY,             
  username VARCHAR(255) UNIQUE,
  email VARCHAR(255) NOT NULL UNIQUE,
  password_hash VARCHAR(255),
  is_verified TINYINT(1) DEFAULT 0,
  preferences JSON,
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  last_login DATETIME,
  metadata JSON
);

CREATE INDEX idx_users_created_at ON users(created_at);

-- REFRESH TOKENS / SESSIONS
CREATE TABLE IF NOT EXISTS refresh_tokens (
  id INT NOT NULL PRIMARY KEY,
  user_id INT NOT NULL,
  token_hash VARCHAR(255) NOT NULL,
  ip VARCHAR(100),
  user_agent VARCHAR(1024),
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  expires_at DATETIME,
  revoked_at DATETIME,
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
CREATE INDEX idx_refresh_tokens_user ON refresh_tokens(user_id);

-- RECOMMENDATIONS / GAMES
CREATE TABLE IF NOT EXISTS recommendations (
  id INT NOT NULL PRIMARY KEY,
  title VARCHAR(1000) NOT NULL,
  slug VARCHAR(255) UNIQUE,
  description TEXT,
  link VARCHAR(2000),
  image_url VARCHAR(2000),
  platform VARCHAR(200),
  publisher VARCHAR(255),
  tags JSON,
  metadata JSON,
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  source VARCHAR(255)
);
CREATE INDEX idx_recommendations_title ON recommendations(title(255));

-- RECOMMENDATIONS FEEDBACK
CREATE TABLE IF NOT EXISTS recommendation_feedback (
    id INT NOT NULL PRIMARY KEY,
    user_id INT NOT NULL,
    recommendation_id INT,
    rating INT,  -- e.g., 1-5 stars
    comment TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (recommendation_id) REFERENCES recommendations(id) ON DELETE CASCADE
    );

-- FAVORITES: many-to-many
CREATE TABLE IF NOT EXISTS favorites (
  id INT NOT NULL PRIMARY KEY,
  user_id INT NOT NULL,
  recommendation_id CHAR(36) NOT NULL,
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,

  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
  FOREIGN KEY (recommendation_id) REFERENCES recommendations(id) ON DELETE CASCADE
);

-- NEWSLETTER SUBSCRIPTIONS
CREATE TABLE IF NOT EXISTS newsletter_subscriptions (
  id INT NOT NULL PRIMARY KEY,
  email VARCHAR(255) NOT NULL UNIQUE,
  name VARCHAR(255),
  source VARCHAR(255),
  subscribed_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  unsubscribed_at DATETIME,
  metadata JSON
);

-- CHAT CONVERSATIONS
CREATE TABLE IF NOT EXISTS chat_conversations (
  id INT NOT NULL PRIMARY KEY,
  user_id INT,
  session_id VARCHAR(512),
  started_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  metadata JSON,
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL
)

-- CHAT MESSAGES
CREATE TABLE IF NOT EXISTS chat_messages (
  id INT NOT NULL PRIMARY KEY,
  conversation_id CHAR(36) NOT NULL,
  user_id INT,
  direction ENUM('user','bot') NOT NULL,
  message_text TEXT NOT NULL,
  attachments JSON,
  metadata JSON,
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (conversation_id) REFERENCES chat_conversations(id) ON DELETE CASCADE,
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL
)
CREATE INDEX idx_chat_user ON chat_messages(user_id);
CREATE INDEX idx_chat_conv ON chat_messages(conversation_id);

-- OAUTH / EXTERNAL ACCOUNTS
CREATE TABLE IF NOT EXISTS oauth_accounts (
  id INT NOT NULL PRIMARY KEY,
  user_id INT NOT NULL,
  provider VARCHAR(255) NOT NULL,
  provider_user_id VARCHAR(512) NOT NULL,
  profile JSON,
  access_token_encrypted TEXT,
  refresh_token_hash VARCHAR(255),
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
  UNIQUE KEY uq_provider_user (provider, provider_user_id)
) 

-- VERIFICATION / PASSWORD RESET TOKENS
CREATE TABLE IF NOT EXISTS verification_tokens (
  id INT NOT NULL PRIMARY KEY,
  user_id INT NOT NULL,
  token_hash VARCHAR(255) NOT NULL,
  type ENUM('reset','verify') NOT NULL,
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  expires_at DATETIME,
  used TINYINT(1) DEFAULT 0,
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) 
CREATE INDEX idx_verif_user ON verification_tokens(user_id);

-- GAME / USAGE STATS
CREATE TABLE IF NOT EXISTS game_stats (
  id INT NOT NULL PRIMARY KEY,
  user_id INT,
  recommendation_id CHAR(36),
  playtime_hours DOUBLE DEFAULT 0,
  last_played DATETIME,
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  metadata JSON,
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL,
  FOREIGN KEY (recommendation_id) REFERENCES recommendations(id) ON DELETE SET NULL
) 

-- TRAINING DOCUMENTS (mirror/import from MongoDB)
CREATE TABLE IF NOT EXISTS training_documents (
  id CHAR(36) NOT NULL PRIMARY KEY,
  source_id VARCHAR(255),
  doc JSON NOT NULL,
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) 
CREATE INDEX idx_training_created_at ON training_documents(created_at);
