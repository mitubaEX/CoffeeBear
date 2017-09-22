
CREATE TABLE users(
    id int primary key AUTO_INCREMENT,
    user_id varchar(128) NOT NULL,
    user_name varchar(128) NOT NULL
);

CREATE TABLE coffee_history(
    id int primary key AUTO_INCREMENT,
    user_id varchar(128) NOT NULL,
    user_name varchar(128) NOT NULL,
    year int NOT NULL,
    month int NOT NULL
);
