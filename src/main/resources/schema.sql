CREATE TABLE users
(
    id         SERIAL PRIMARY KEY,
    name       VARCHAR                 NOT NULL,
    email      VARCHAR                 NOT NULL,
    password   VARCHAR                 NOT NULL,
    registered TIMESTAMP DEFAULT now() NOT NULL,
    enabled    BOOLEAN   DEFAULT TRUE  NOT NULL
);
CREATE UNIQUE INDEX users_unique_email_idx ON users (email);

CREATE TABLE user_roles
(
    user_id INTEGER NOT NULL,
    role    VARCHAR,
    CONSTRAINT user_roles_unique_idx UNIQUE (user_id, role),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE restaurants
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR NOT NULL
);
CREATE UNIQUE INDEX restaurant_unique_name_idx ON restaurants (name);

CREATE TABLE dishes
(
    id          SERIAL PRIMARY KEY,
    name        VARCHAR                          NOT NULL,
    create_date DATE    DEFAULT now()            NOT NULL,
    price       INT                              NOT NULL,
    description VARCHAR DEFAULT 'No description' NOT NULL,
    rest_id     INTEGER                          NOT NULL,
    CONSTRAINT rest_unique_dish_date_name_idx UNIQUE (rest_id, create_date, name),
    FOREIGN KEY (rest_id) REFERENCES restaurants (id) ON DELETE CASCADE
);

CREATE TABLE votes
(
    id        SERIAL PRIMARY KEY,
    vote_date DATE DEFAULT now() NOT NULL,
    user_id   INTEGER            NOT NULL,
    rest_id   INTEGER            NOT NULL,
    CONSTRAINT vote_unique_date_user_idx UNIQUE (user_id, vote_date),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (rest_id) REFERENCES restaurants (id) ON DELETE CASCADE
);