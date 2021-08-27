CREATE TABLE post (
  id SERIAL PRIMARY KEY,
  name TEXT
);

CREATE TABLE candidate (
  id SERIAL PRIMARY KEY,
  name TEXT
);

CREATE TABLE "user" (
   id SERIAL,
   name TEXT,
   email TEXT PRIMARY KEY,
   password TEXT
);