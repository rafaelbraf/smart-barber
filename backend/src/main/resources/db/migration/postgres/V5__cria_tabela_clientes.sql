CREATE TABLE IF NOT EXISTS clientes (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    id_externo UUID UNIQUE NOT NULL,
    cpf VARCHAR(11) UNIQUE NOT NULL,
    nome VARCHAR(100) NOT NULL,
    data_nascimento DATE NOT NULL,
    celular VARCHAR(20),
    usuario_id BIGINT REFERENCES usuarios(id),
    CONSTRAINT fk_usuario_clientes FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);