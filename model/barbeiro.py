class Barbeiro:
    def __init__(self, id, cpf, nome, email, celular, admin, id_barberia):
        self.id = id
        self.cpf = cpf
        self.nome = nome
        self.email = email
        self.celular = celular
        self.admin = admin
        self.id_barbearia = id_barberia

    def __str__(self):
        return f"Barbeiro(id = {self.id}, cpf = {self.cpf}, nome = {self.nome}, email = {self.email}, celular = {self.celular}, admin = {self.admin}, idBarbearia = {self.id_barbearia})"
