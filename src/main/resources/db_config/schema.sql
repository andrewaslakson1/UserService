CREATE TABLE USER (
    user_id INTEGER AUTO_INCREMENT,
    username VARCHAR(128) UNIQUE NOT NULL,

    PRIMARY KEY (user_id)
);