CREATE TABLE IF NOT EXISTS barbeiros (
    id SERIAL PRIMARY KEY,
    cpf VARCHAR(11) UNIQUE NOT NULL,
    nome VARCHAR(100) NOT NULL,
    celular VARCHAR(20),
    admin BOOLEAN NOT NULL,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    barbearia_id INTEGER REFERENCES barbearias(id),
    CONSTRAINT fk_barbearia FOREIGN KEY (barbearia_id) REFERENCES barbearias(id),
    usuario_id UUID REFERENCES usuarios(id),
    CONSTRAINT fk_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);