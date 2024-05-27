package com.optimiza.clickbarber.exception;

import com.optimiza.clickbarber.model.Resposta;
import com.optimiza.clickbarber.model.RespostaUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLException;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private static final String MENSAGEM_ERRO_GENERICA = "Erro ao cadastrar objeto.";
    private static final String MENSAGEM_ERRO_CNPJ = "Já existe barbearia com esse CNPJ.";
    private static final String MENSAGEM_ERRO_EMAIL = "Já existe barbearia com esse Email.";
    private static final String MENSAGEM_ERRO_INTERNO_SERVIDOR = "Erro interno do servidor.";
    private static final String MENSAGEM_BARBEARIA_NAO_ENCONTRADA_PRO_SERVICO = "Barbearia não encontrada pro serviço.";

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Resposta<String>> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        String mensagem = MENSAGEM_ERRO_GENERICA;

        if (e.getRootCause() instanceof SQLException sqlException) {
            var sqlErroMessage = sqlException.getMessage();
            if (sqlErroMessage.contains("barbearias_cnpj_key")) {
                mensagem = MENSAGEM_ERRO_CNPJ;
            } else if (sqlErroMessage.contains("barbearias_email_key")) {
                mensagem = MENSAGEM_ERRO_EMAIL;
            } else if (sqlErroMessage.contains("fk_barbearia")) {
                mensagem = MENSAGEM_BARBEARIA_NAO_ENCONTRADA_PRO_SERVICO;
            }

            logger.error("DataIntegrityViolationException: {}", e.getMessage());
            var resposta = RespostaUtils.conflict(mensagem, e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(resposta);
        }

        logger.error("Internal Server Error: {}", e.getMessage());
        mensagem = MENSAGEM_ERRO_INTERNO_SERVIDOR;
        var resposta = RespostaUtils.error(mensagem, e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resposta);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Resposta<String>> handleResourceNotFoundException(ResourceNotFoundException e) {
        logger.error("ResourceNotFoundException: {}", e.getMessage());
        var mensagem = String.format("%s não encontrado(a).", e.getResourceName());
        var resposta = RespostaUtils.notFound(mensagem, e.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resposta);
    }

}
