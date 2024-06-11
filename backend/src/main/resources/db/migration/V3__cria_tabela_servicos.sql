CREATE TABLE servicos (
    id UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    preco NUMERIC(10, 2) NOT NULL,
    tempo_duracao_em_minutos INTEGER NOT NULL,
    barbearia_id INTEGER NOT NULL,
    ativo BOOLEAN NOT NULL DEFAULT true,
    CONSTRAINT fk_barbearia_servicos FOREIGN KEY (barbearia_id) REFERENCES barbearias(id)
);