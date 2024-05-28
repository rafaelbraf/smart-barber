package com.optimiza.clickbarber.utils;

public class Constants {

    private Constants() {}

    public static final String FRONTEND_URL = "http://localhost:3000";
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String AUTHORIZATION_NAME = "Authorization";

    public static class Success {
        private Success() {}

        public static final String LOGIN_REALIZADO_COM_SUCESSO = "Login realizado com sucesso!";
        public static final String BARBEARIA_CADASTRADA_COM_SUCESSO = "Barbearia cadastrada com sucesso!";
        public static final String BARBEARIA_ENCONTRADA_COM_SUCESSO = "Barbearia encontrada com sucesso!";
        public static final String SERVICOS_ENCONTRADOS = "Serviços encontrados";
        public static final String SERVICO_ENCONTRADO_COM_SUCESSO = "Serviço encontrado com sucesso!";
        public static final String SERVICOS_ENCONTRADOS_DA_BARBEARIA = "Serviços encontrados para Barbearia com id ";
        public static final String SERVICO_CADASTRADO_COM_SUCESSO = "Serviço cadastrado com sucesso!";
        public static final String SERVICO_ATUALIZADO_COM_SUCESSO = "Serviço atualizado com sucesso!";
        public static final String BARBEIROS_ENCONTRADOS_DA_BARBEARIA = "Barbeiros encontrados para Barbearia com id ";
        public static final String BARBEIRO_ENCONTRADO_COM_SUCESSO = "Barbeiro encontrado com sucesso!";
        public static final String BARBEIRO_ATUALIZADO_COM_SUCESSO = "Barbeiro atualizado com sucesso!";
        public static final String BARBEIRO_CADASTRADO_COM_SUCESSO = "Barbeiro cadastrado com sucesso!";
        public static final String USUARIO_CADASTRADO_COM_SUCESSO = "Usuário cadastrado com sucesso!";
    }

    public static class Error {
        private Error() {}

        public static final String EMAIL_OU_SENHA_INCORRETA = "Email ou senha incorreta!";
        public static final String RESOURCE_NOT_FOUND_EXCEPTION = "Não foi encontrado(a) %s com %s = %s.";
        public static final String ERRO_AO_CADASTRAR_OBJETO = "Erro ao cadastrar objeto.";
        public static final String EXISTE_BARBEARIA_COM_ESSE_CNPJ = "Já existe barbearia cadastrada com esse CNPJ.";
        public static final String EXISTE_BARBEARIA_COM_ESSE_EMAIL = "Já existe barbearia cadastrada com esse Email.";
        public static final String ERRO_INTERNO_SERVIDOR = "Erro interno do servidor.";
        public static final String BARBEARIA_NAO_ENCONTRADA_PRO_SERVICO = "Barbearia não encontrada pro serviço.";
        public static final String RESOURCE_NOT_FOUND = "%s não encontrado(a).";
        public static final String TOKEN_INVALIDO = "Token inválido!";
        public static final String TOKEN_EXPIRADO = "Token expirado!";
        public static final String EXISTE_BARBEIRO_COM_ESSE_CPF = "Já existe Barbeiro cadastrado com esse CPF.";
        public static final String EXISTE_BARBEIRO_COM_ESSE_EMAIL = "Já existe Barbeiro cadastrado com esse email.";
        public static final String BARBEARIA_NAO_ENCONTRADA_PRO_BARBEIRO = "Barbearia não encontrada pro Barbeiro.";
        public static final String EXISTE_USUARIO_COM_ESSE_CPF = "Já existe Usuário cadastrado com esse CPF.";
        public static final String EXISTE_USUARIO_COM_ESSE_EMAIL = "Já existe Usuário cadastrado com esse email.";
    }

    public static class UrlPattern {
        private UrlPattern() {}

        public static final String BARBEARIAS_URL_PATTERN = "/barbearias/*";
        public static final String SERVICOS_URL_PATTERN= "/servicos/*";
        public static final String BARBEIROS_URL_PATTERN = "/barbeiros/*";
    }

    public static class Entity {
        private Entity() {}

        public static final String BARBEARIA = "Barbearia";
        public static final String SERVICO = "Servico";
        public static final String BARBEIRO = "Barbeiro";
        public static final String USUARIO = "Usuario";
    }

    public static class Attribute {
        private Attribute() {}

        public static final String ID = "id";
        public static final String EMAIL = "email";
    }

}
