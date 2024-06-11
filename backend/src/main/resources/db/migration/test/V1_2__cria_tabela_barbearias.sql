CREATE TABLE barbearias (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    cnpj VARCHAR(14) UNIQUE NOT NULL,
    nome VARCHAR(100) NOT NULL,
    endereco VARCHAR(255),
    telefone VARCHAR(20),
    usuario_id UUID,
    CONSTRAINT fk_usuario_barbearias FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);