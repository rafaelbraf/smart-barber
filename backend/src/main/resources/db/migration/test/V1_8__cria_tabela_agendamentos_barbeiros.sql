CREATE TABLE IF NOT EXISTS agendamentos_barbeiros (
    agendamento_id UUID NOT NULL,
    barbeiro_id BIGINT NOT NULL,
    PRIMARY KEY (agendamento_id, barbeiro_id),
    FOREIGN KEY (agendamento_id) REFERENCES agendamentos(id),
    FOREIGN KEY (barbeiro_id) REFERENCES barbeiros(id)
);