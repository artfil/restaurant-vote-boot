DELETE
FROM votes;
DELETE
FROM dishes;
DELETE
FROM restaurants;
DELETE
FROM user_roles;
DELETE
FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', '{noop}password'),
       ('Admin', 'admin@gmail.com', '{noop}admin');

INSERT INTO user_roles (role, user_id)
VALUES ('USER', 100000),
       ('ADMIN', 100001),
       ('USER', 100001);

INSERT INTO restaurants (name)
VALUES ('Koreana'),
       ('Kriek'),
       ('Jager');

INSERT INTO dishes (name, date, price, description, rest_id)
VALUES ('Fresh Korea', now(), 500, 'Tomatoes, cheese, salad', 100002),
       ('Asian soup', now(), 800, 'seafood, potatoes', 100002),
       ('Bibimbap', '2021-03-08', 1000, 'rice, vegetables, beef', 100002),
       ('Belgian waffles', '2021-03-08', 325, 'waffles, chocolate sauce, strawberry', 100003),
       ('Belgian waffles', now(), 325, 'waffles, chocolate sauce, strawberry', 100003),
       ('Marbled beef steak', now(), 1254, 'beef, BBQ sauce', 100003);

INSERT INTO dishes (name, date, price, rest_id)
VALUES ('Bavarian sausage', '2021-03-08', 999, 100004);

INSERT INTO votes(vote_date, user_id, rest_id)
VALUES (now(), 100001, 100003),
       ('2021-03-08', 100000, 100002),
       ('2021-03-08', 100001, 100004);