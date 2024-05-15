class Usuario:
    def __init__(self, cpf, nome, data_nascimento, email, celular, senha, id=None):
        self.id = id
        self.cpf = cpf
        self.nome = nome
        self.data_nascimento = data_nascimento
        self.email = email
        self.celular = celular
        self.senha = senha
