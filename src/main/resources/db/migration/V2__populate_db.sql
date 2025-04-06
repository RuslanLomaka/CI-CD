-- Insert planets
INSERT INTO planet (id, name)
VALUES ('MARS', 'Mars'),
       ('VEN', 'Venus'),
       ('EAR', 'Earth'),
       ('JUP', 'Jupiter'),
       ('SAT', 'Saturn');

-- Insert passengers
INSERT INTO passenger (name, passport)
VALUES ('Тарас Шевченко', 'UA001'),
       ('Симон Петлюра', 'UA002'),
       ('Степан Бандера', 'UA003'),
       ('Роман Шухевич', 'UA004'),
       ('Пес Патрон', 'UA005'),
       ('Володимир Зеленський', 'UA006'),
       ('Кирило Буданов', 'UA007'),
       ('Леся Українка', 'UA008'),
       ('Іван Франко', 'UA009'),
       ('Михайло Грушевський', 'UA010');

-- Insert tickets
-- Note: Use actual passenger IDs if they are generated; assuming 1–10 here
INSERT INTO ticket (createdAt, client_id, from_planet_id, to_planet_id)
VALUES (NOW(), 1, 'EAR', 'MARS'),
       (NOW(), 2, 'MARS', 'VEN'),
       (NOW(), 3, 'VEN', 'EAR'),
       (NOW(), 4, 'EAR', 'JUP'),
       (NOW(), 5, 'JUP', 'SAT'),
       (NOW(), 6, 'SAT', 'EAR'),
       (NOW(), 7, 'VEN', 'SAT'),
       (NOW(), 8, 'MARS', 'JUP'),
       (NOW(), 9, 'SAT', 'VEN'),
       (NOW(), 10, 'EAR', 'VEN');
