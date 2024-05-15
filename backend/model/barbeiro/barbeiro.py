class Barbeiro:
    def __init__(self, cpf, nome, email, celular, admin, id_barbearia, ativo, id=None):
        self.id = id
        self.cpf = cpf
        self.nome = nome
        self.email = email
        self.celular = celular
        self.admin = admin
        self.id_barbearia = id_barbearia
        self.ativo = ativo
