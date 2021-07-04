INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', '{noop}password'),
       ('Admin', 'admin@gmail.com', '{noop}admin');

INSERT INTO user_roles (role, user_id)
VALUES ('USER', 1),
       ('ADMIN', 2),
       ('USER', 2);

INSERT INTO restaurants (name)
VALUES ('Koreana'),
       ('Kriek'),
       ('Jager');

INSERT INTO dishes (name, create_date, price, description, rest_id)
VALUES ('Fresh Korea', now(), 500, 'Tomatoes, cheese, salad', 1),
       ('Asian soup', now(), 800, 'seafood, potatoes', 1),
       ('Bibimbap', '2021-03-08', 1000, 'rice, vegetables, beef', 1),
       ('Belgian waffles', '2021-03-08', 325, 'waffles, chocolate sauce, strawberry', 2),
       ('Belgian waffles', now(), 325, 'waffles, chocolate sauce, strawberry', 2),
       ('Marbled beef steak', now(), 1254, 'beef, BBQ sauce', 2),
       ('Bavarian sausage', '2021-03-08', 999, '', 3);

INSERT INTO votes(vote_date, user_id, rest_id)
VALUES (now(), 2, 2),
       ('2021-03-08', 1, 1),
       ('2021-03-08', 2, 3);