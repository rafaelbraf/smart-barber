CREATE TABLE barbearias (
    id SERIAL PRIMARY KEY,
    cnpj VARCHAR(14) UNIQUE NOT NULL,
    nome VARCHAR(100) NOT NULL,
    endereco VARCHAR(255),
    telefone VARCHAR(20),
    usuario_id UUID REFERENCES usuarios(id),
    CONSTRAINT fk_usuario_barbearias FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);