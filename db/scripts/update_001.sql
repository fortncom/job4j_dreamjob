CREATE TABLE POST (
    ID SERIAL PRIMARY KEY,
    NAME TEXT,
    CREATED TIMESTAMP
);

CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    name TEXT,
    email TEXT,
    password TEXT
);

CREATE TABLE city (
    id SERIAL PRIMARY KEY,
    name TEXT
);

CREATE TABLE candidate (
    id SERIAL PRIMARY KEY,
    name TEXT,
    city_id int references city(id),
    created timestamp
);