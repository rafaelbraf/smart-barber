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
    id_barbearia INTEGER REFERENCES barbearias(id),
    ativo BOOLEAN NOT NULL
);

CREATE TABLE IF NOT EXISTS servicos (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    preco NUMERIC(10, 2) NOT NULL,
    tempo_duracao_em_minutos INTEGER NOT NULL,
    id_barbearia INTEGER REFERENCES barbearias(id),
    ativo BOOLEAN NOT NULL DEFAULT true
);

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS usuarios (
    id UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    cpf VARCHAR(11) UNIQUE NOT NULL,
    nome VARCHAR(100) NOT NULL,
    data_nascimento VARCHAR(10) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    celular VARCHAR(20),
    senha VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS agendamentos (
    id UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    data VARCHAR(10) NOT NULL,
    hora VARCHAR(5) NOT NULL,
    valor_total NUMERIC(10, 2) NOT NULL,
    tempo_duracao_em_minutos INTEGER NOT NULL,
    id_barbearia INTEGER REFERENCES barbearias(id),
    id_usuario UUID REFERENCES usuarios(id)
);

CREATE TABLE IF NOT EXISTS agendamento_servicos (
    agendamento_id UUID REFERENCES agendamentos(id),
    servico_id INTEGER REFERENCES servicos(id)
);

CREATE TABLE IF NOT EXISTS agendamento_barbeiros (
    agendamento_id UUID REFERENCES agendamentos(id),
    barbeiro_id INTEGER REFERENCES barbeiros(id)
);