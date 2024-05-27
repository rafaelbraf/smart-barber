package com.optimiza.clickbarber.exception;

import com.optimiza.clickbarber.model.Resposta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolationException;
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
    public ResponseEntity<Resposta<Object>> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        Resposta<Object> resposta;
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

            resposta = montarResposta(HttpStatus.CONFLICT.value(), false, mensagem, e.getMessage());
            logger.error("DataIntegrityViolationException: {}", e.getMessage());

            return ResponseEntity.status(HttpStatus.CONFLICT).body(resposta);
        }

        resposta = montarResposta(HttpStatus.INTERNAL_SERVER_ERROR.value(), false, MENSAGEM_ERRO_INTERNO_SERVIDOR, e.getMessage());
        logger.error("Internal Server Error: {}", e.getMessage());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resposta);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Resposta<Object>> handleResourceNotFoundException(ResourceNotFoundException e) {
        var mensagem = String.format("%s não encontrado(a).", e.getResourceName());
        var resposta = montarResposta(HttpStatus.NOT_FOUND.value(), false, mensagem, e.getMessage());
        logger.error("ResourceNotFoundException: {}", e.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resposta);
    }

    private Resposta<Object> montarResposta(int statusCode, boolean success, String message, Object result) {
        return Resposta.<Object>builder()
                .statusCode(statusCode)
                .success(success)
                .message(message)
                .result(result)
                .build();
    }

}
