CREATE TABLE if not exists POST (
    ID SERIAL PRIMARY KEY,
    NAME TEXT,
    CREATED TIMESTAMP
);

CREATE TABLE if not exists users (
    id SERIAL PRIMARY KEY,
    name TEXT,
    email TEXT,
    password TEXT
);

CREATE TABLE if not exists city (
    id SERIAL PRIMARY KEY,
    name TEXT
);

CREATE TABLE if not exists candidate (
    id SERIAL PRIMARY KEY,
    name TEXT,
    city_id int references city(id),
    created timestamp
);