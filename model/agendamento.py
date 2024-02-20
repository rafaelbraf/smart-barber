class Agendamento:
    def __init__(self, data, hora, valor_total, tempo_duracao_em_minutos, id_barbearia, id_usuario, id=None):
        self.id = id
        self.data = data
        self.hora = hora
        self.valor_total = valor_total
        self.tempo_duracao_em_minutos = tempo_duracao_em_minutos
        self.id_barbearia = id_barbearia
        self.id_usuario = id_usuario
