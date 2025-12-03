SET TIME ZONE 'UTC';

CREATE SEQUENCE users_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE users (
    id bigint NOT NULL PRIMARY KEY,
    created_at timestamp NOT NULL,
    updated_at timestamp NOT NULL,
    login_name varchar(255) NOT NULL UNIQUE CHECK (login_name <> ''),
    email_address varchar(254) NOT NULL UNIQUE CHECK (length(email_address) > 2), -- https://stackoverflow.com/a/574698/1974143, https://stackoverflow.com/a/1423203/1974143
    first_name varchar(255) NOT NULL CHECK (first_name <> ''),
    last_name varchar(255) NOT NULL CHECK (last_name <> '')
);
