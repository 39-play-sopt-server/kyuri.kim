CREATE TABLE IF NOT EXISTS members (
  id BIGINT NOT NULL AUTO_INCREMENT,
  name VARCHAR(50) NOT NULL,
  deleted BIT NOT NULL DEFAULT 0,
  created_at DATETIME(6) NULL,
  updated_at DATETIME(6) NULL,
  PRIMARY KEY (id)
);
CREATE TABLE IF NOT EXISTS post (
  id BIGINT NOT NULL AUTO_INCREMENT,
  title VARCHAR(50) NOT NULL,
  content VARCHAR(500) NOT NULL,
  member_id BIGINT NOT NULL,
  category VARCHAR(255) NOT NULL,
  view_count BIGINT NOT NULL DEFAULT 0,
  created_at DATETIME(6) NULL,
  updated_at DATETIME(6) NULL,
  PRIMARY KEY (id),
  INDEX idx_post_title (title),
  CONSTRAINT fk_post_member FOREIGN KEY (member_id) REFERENCES members(id)
);
ALTER TABLE members ADD COLUMN IF NOT EXISTS email VARCHAR(254) NULL UNIQUE;
ALTER TABLE members ADD COLUMN IF NOT EXISTS password_hash VARCHAR(255) NULL;
ALTER TABLE members ADD COLUMN IF NOT EXISTS role VARCHAR(20) NOT NULL DEFAULT 'ROLE_USER';

CREATE TABLE IF NOT EXISTS auth_sessions (
  id BINARY(16) NOT NULL,
  member_id BIGINT NOT NULL,
  expires_at DATETIME(6) NOT NULL,
  revoked_at DATETIME(6) NULL,
  PRIMARY KEY (id),
  INDEX idx_auth_session_member (member_id),
  CONSTRAINT fk_auth_session_member FOREIGN KEY (member_id) REFERENCES members(id)
);

CREATE TABLE IF NOT EXISTS refresh_token_records (
  id BIGINT NOT NULL AUTO_INCREMENT,
  session_id BINARY(16) NOT NULL,
  jti VARCHAR(64) NOT NULL,
  token_hash VARCHAR(64) NOT NULL,
  expires_at DATETIME(6) NOT NULL,
  used_at DATETIME(6) NULL,
  PRIMARY KEY (id),
  UNIQUE KEY uk_refresh_jti (jti),
  CONSTRAINT fk_refresh_session FOREIGN KEY (session_id) REFERENCES auth_sessions(id)
);

CREATE TABLE IF NOT EXISTS oauth_identities (
  id BIGINT NOT NULL AUTO_INCREMENT,
  provider VARCHAR(50) NOT NULL,
  provider_subject VARCHAR(255) NOT NULL,
  member_id BIGINT NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY uk_oauth_provider_subject (provider, provider_subject),
  CONSTRAINT fk_oauth_member FOREIGN KEY (member_id) REFERENCES members(id)
);
