class BarbeariaRequestDto:
    def __init__(self, cnpj, nome, endereco, email, telefone, senha):
        self.cnpj = cnpj
        self.nome = nome
        self.endereco = endereco
        self.email = email
        self.telefone = telefone
        self.senha = senha