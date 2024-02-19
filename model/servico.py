class Servico:
    def __init__(self, nome, preco, tempo_duracao_minutos, id_barbearia, id=None):
        self.id = id
        self.nome = nome
        self.preco = preco
        self.tempo_duracao_minutos = tempo_duracao_minutos
        self.id_barbearia = id_barbearia
