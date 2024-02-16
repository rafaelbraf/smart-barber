CREATE TABLE IF NOT EXISTS barbearias (
    id SERIAL PRIMARY KEY,
    cnpj VARCHAR(14) UNIQUE NOT NULL,
    nome VARCHAR(100) NOT NULL,
    endereco VARCHAR(255),
    email VARCHAR(100) UNIQUE NOT NULL,
    telefone VARCHAR(20),
    senha VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS barbeiros (
    id SERIAL PRIMARY KEY,
    cpf VARCHAR(11) UNIQUE NOT NULL,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    celular VARCHAR(20),
    admin BOOLEAN NOT NULL,
    id_barbearia INTEGER REFERENCES barbearias(id)
);

CREATE TABLE IF NOT EXISTS servicos (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    preco NUMERIC(10, 2) NOT NULL,
    tempo_duracao INTERVAL NOT NULL,
    id_barbearia INTEGER REFERENCES barbearias(id)
);

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS usuarios (
    id UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    cpf VARCHAR(11) UNIQUE NOT NULL,
    nome VARCHAR(100) NOT NULL,
    data_nascimento DATE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    celular VARCHAR(20),
    senha VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS agendamentos (
    id UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    data TIMESTAMP NOT NULL,
    valor_total NUMERIC(10, 2) NOT NULL,
    tempo_duracao INTERVAL NOT NULL,
    id_servico INTEGER REFERENCES servicos(id),
    id_barbearia INTEGER REFERENCES barbearias(id),
    id_barbeiro INTEGER REFERENCES barbeiros(id),
    id_usuario UUID REFERENCES usuarios(id)
);