CREATE TABLE IF NOT EXISTS account
(
    username           VARCHAR(128) NOT NULL,
    password           VARCHAR(256) NOT NULL,
    two_factor_secret  VARCHAR(256) NOT NULL,
    two_factor_enabled boolean      NOT NULL DEFAULT false,
    PRIMARY KEY (username)
);