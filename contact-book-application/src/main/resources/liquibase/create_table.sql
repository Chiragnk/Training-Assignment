
CREATE TABLE users (
    id UUID PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255)
);


CREATE TABLE contact_book (
    id UUID PRIMARY KEY,
    firstname VARCHAR(255) NOT NULL,
    lastname VARCHAR(255) NOT NULL,
    email VARCHAR(255),
    phone VARCHAR(20),
    deleted BOOLEAN NOT NULL DEFAULT FALSE,
    user_id UUID,
    FOREIGN KEY (user_id) REFERENCES users(id)
);
