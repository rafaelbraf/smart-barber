CREATE TABLE IF NOT EXISTS barbeiros (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    cpf VARCHAR(11) UNIQUE NOT NULL,
    nome VARCHAR(100) NOT NULL,
    celular VARCHAR(20),
    admin BOOLEAN NOT NULL,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    barbearia_id BIGINT,
    usuario_id UUID,
    CONSTRAINT fk_barbearia_barbeiros FOREIGN KEY (barbearia_id) REFERENCES barbearias(id),
    CONSTRAINT fk_usuario_barbeiros FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);