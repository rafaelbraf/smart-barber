CREATE TABLE IF NOT EXISTS agendamentos (
    id UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    data_hora TIMESTAMPTZ NOT NULL,
    valor_total NUMERIC(10, 2) NOT NULL,
    tempo_duracao_em_minutos INTEGER NOT NULL,
    barbearia_id BIGINT REFERENCES barbearias(id),
    CONSTRAINT fk_barbearia_agendamentos FOREIGN KEY (barbearia_id) REFERENCES barbearias(id),
    cliente_id BIGINT REFERENCES clientes(id),
    CONSTRAINT fk_cliente_agendamentos FOREIGN KEY (cliente_id) REFERENCES clientes(id)
);