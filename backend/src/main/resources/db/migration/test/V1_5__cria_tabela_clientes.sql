CREATE TABLE IF NOT EXISTS clientes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    cpf VARCHAR(11) UNIQUE NOT NULL,
    nome VARCHAR(100) NOT NULL,
    data_nascimento DATE NOT NULL,
    celular VARCHAR(20),
    usuario_id UUID,
    CONSTRAINT fk_usuario_clientes FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);