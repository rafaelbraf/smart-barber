CREATE TABLE IF NOT EXISTS usuarios (
    id UUID DEFAULT random_uuid() PRIMARY KEY,
    email VARCHAR(100) UNIQUE NOT NULL,
    senha VARCHAR(255) NOT NULL,
    role VARCHAR(100) NOT NULL
);