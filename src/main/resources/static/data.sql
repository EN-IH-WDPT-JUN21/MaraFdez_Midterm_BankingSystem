DROP TABLE IF EXISTS credit_card_account;
DROP TABLE IF EXISTS savings_account;
DROP TABLE IF EXISTS student_checking_account;
DROP TABLE IF EXISTS checking_account;
DROP TABLE IF EXISTS account;

DROP TABLE IF EXISTS admin;
DROP TABLE IF EXITS account_holder;
DROP TABLE IF EXISTS user;
DROP TABLE IF EXISTS third_party;

-- for admins password is: adm1n
-- for accountHolders password is: p4ssword
INSERT INTO user(id, name, username, password) VALUES
(default, 'administrator', 'admin', '$2a$10$91cj77UnF9nWLqQppEJHoe1oSKRvNse3rUc02F0TuNR2osT1GK.gm' ),
(default, 'Alyssa Thompson', 'alysth', '$2a$10$w42M/2mWuYXiEiYhEfB2kOz.vWOiQJckNbs85xoZ5QIYU324.9/EC'),
(default, 'Alice Fisher', 'alifish', '$2a$10$91cj77UnF9nWLqQppEJHoe1oSKRvNse3rUc02F0TuNR2osT1GK.gm'),
(default, 'Thomas Clark', 'theclark', '$2a$10$w42M/2mWuYXiEiYhEfB2kOz.vWOiQJckNbs85xoZ5QIYU324.9/EC'),
(default, 'Anna Mugler', 'anmug', '$2a$10$w42M/2mWuYXiEiYhEfB2kOz.vWOiQJckNbs85xoZ5QIYU324.9/EC'),
(default, 'Simon Folliard', 'simonf', '$2a$10$w42M/2mWuYXiEiYhEfB2kOz.vWOiQJckNbs85xoZ5QIYU324.9/EC'),
(default, 'Bastien Richard', 'bastric', '$2a$10$w42M/2mWuYXiEiYhEfB2kOz.vWOiQJckNbs85xoZ5QIYU324.9/EC'),
(default, 'Thierry Jobin ', 'thiejb', '$2a$10$w42M/2mWuYXiEiYhEfB2kOz.vWOiQJckNbs85xoZ5QIYU324.9/EC');

INSERT INTO role (name, user_id) VALUES
('ADMIN', 1),
('ACCOUNTHOLDER', 2),
('ADMIN', 3),
('ACCOUNTHOLDER', 4),
('ACCOUNTHOLDER', 5),
('ACCOUNTHOLDER', 6),
('ACCOUNTHOLDER', 7),
('ACCOUNTHOLDER', 8);

INSERT INTO account_holder(user_id, date_of_birth, current_street, current_number, current_city, current_country, current_zip,
mailing_street, mailing_number, mailing_city, mailing_country, mailing_zip)  VALUES
(2, '1994-12-20', 'Calle Tristeza', 1, 'Vigo', 'Spain', 00002, null, null, null, null, null),
(4, '2000-09-12', 'Calle Alegría', 8, 'Alicante', 'Spain', 03003, 'Calle Italia', 18, 'Madrid', 'Spain', 28943),
(5, '1956-11-30', 'Calle Amargura', 10, 'Málaga', 'Spain', 29012, 'Calle Esperanza', 2, 'Sevilla', 'Spain', 41003),
(6, '1999-03-21', 'Calle Primavera', 8, 'Granada', 'Spain', 18011, null, null, null, null, null),
(7, '1993-09-22', 'Calle Ancha', 22, 'Huelva', 'Spain', 21100, 'Calle Murcia', 15, 'Madrid', 'Spain', 28045),
(8, '1969-12-30', 'Avenida Independencia', 5, 'León', 'Spain', 24003, null, null, null, null, null);

INSERT INTO admin (user_id) VALUES
(1),
(3);

-- third party hashedKey is hash3dKey
INSERT INTO third_party(id, hashed_key, name) VALUES
(default, '$2a$10$vBfaT7.r4PNSUsEKRF8mNeNnG/2gkF9APIooojA254vLi/GB0sImm', 'John Mckenzie'),
(default, '$2a$10$vBfaT7.r4PNSUsEKRF8mNeNnG/2gkF9APIooojA254vLi/GB0sImm', 'Oliver Twist'),
(default, '$2a$10$vBfaT7.r4PNSUsEKRF8mNeNnG/2gkF9APIooojA254vLi/GB0sImm', 'Leonora Blum');

INSERT INTO account(id, balance_amount, balance_currency, account_holder_id, secondary_owner_id, status, creation_date) VALUES
(default, '77000', 'USD', 2, 4, 'ACTIVE', '2019-10-10'),
(default, '1000', 'USD', 5, null, 'ACTIVE', '2021-01-01'),
(default, '8000', 'USD', 4, 6, 'ACTIVE', '2020-05-20'),
(default, '100000', 'USD', 6, 4, 'ACTIVE', '2014-08-12'),
(default, '750', 'USD', 7, 8, 'ACTIVE', '2014-02-18'),
(default, '1600', 'USD', 8, null, 'ACTIVE', '2020-10-27');

-- checking account secretKey is secr3tKey
INSERT INTO checking_account(secret_key, id) VALUES
('$2a$10$MPOlLHG/2B6IEMrIA6x1a.p.bS4Hr.aGSfzvlDhFHIqgLzJ/U0QSG', 1),
('$2a$10$MPOlLHG/2B6IEMrIA6x1a.p.bS4Hr.aGSfzvlDhFHIqgLzJ/U0QSG', 2);

-- student checking account secretKey is secr3tKey
INSERT INTO student_checking_account(secret_key, id) VALUES
('$2a$10$MPOlLHG/2B6IEMrIA6x1a.p.bS4Hr.aGSfzvlDhFHIqgLzJ/U0QSG', 3);

-- savings account secretKey is secr3tKey
INSERT INTO savings_account(interest_rate, last_interest_application_date, minimum_balance_amount, minimum_balance_currency, secret_key, id) VALUES
('0.03', '2020-08-12', '1000', 'EUR', '$2a$10$MPOlLHG/2B6IEMrIA6x1a.p.bS4Hr.aGSfzvlDhFHIqgLzJ/U0QSG', 4),
('0.25', '2019-03-21', '500', 'USD', '$2a$10$MPOlLHG/2B6IEMrIA6x1a.p.bS4Hr.aGSfzvlDhFHIqgLzJ/U0QSG', 5);

INSERT INTO credit_card_account(credit_limit_amount, credit_limit_currency, interest_rate, last_interest_application_date, id) VALUES
('400', 'EUR', '0.13', '2021-09-12', 6);