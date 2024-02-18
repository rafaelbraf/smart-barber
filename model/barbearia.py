class Barbearia:
    def __init__(self, id, cnpj, nome, endereco, email, telefone, senha):
        self.id = id
        self.cnpj = cnpj
        self.nome = nome
        self.endereco = endereco
        self.email = email
        self.telefone = telefone
        self.senha = senha

    def __str__(self):
        return f"Barbearia(id = {self.id}, cnpj={self.cnpj}, nome={self.nome}, endereco={self.endereco}, email=${self.email}, telefone={self.telefone}, senha=${self.senha})"
