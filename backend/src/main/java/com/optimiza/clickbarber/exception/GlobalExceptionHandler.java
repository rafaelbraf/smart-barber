package com.optimiza.clickbarber.exception;

import com.optimiza.clickbarber.model.Resposta;
import com.optimiza.clickbarber.model.RespostaUtils;
import com.optimiza.clickbarber.utils.Constants;
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

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Resposta<String>> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        String mensagem = Constants.Error.ERRO_AO_CADASTRAR_OBJETO;

        if (e.getRootCause() instanceof SQLException sqlException) {
            var sqlErroMessage = sqlException.getMessage();
            if (sqlErroMessage.contains("barbearias_cnpj_key")) {
                mensagem = Constants.Error.EXISTE_BARBEARIA_COM_ESSE_CNPJ;
            } else if (sqlErroMessage.contains("barbearias_email_key")) {
                mensagem = Constants.Error.EXISTE_BARBEARIA_COM_ESSE_EMAIL;
            } else if (sqlErroMessage.contains("fk_barbearia")) {
                mensagem = Constants.Error.BARBEARIA_NAO_ENCONTRADA_PRO_SERVICO;
            }

            logger.error("DataIntegrityViolationException: {}", e.getMessage());
            var resposta = RespostaUtils.conflict(mensagem, e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(resposta);
        }

        logger.error("Internal Server Error: {}", e.getMessage());
        mensagem = Constants.Error.ERRO_INTERNO_SERVIDOR;
        var resposta = RespostaUtils.error(mensagem, e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resposta);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Resposta<String>> handleResourceNotFoundException(ResourceNotFoundException e) {
        logger.error("ResourceNotFoundException: {}", e.getMessage());
        var mensagem = String.format(Constants.Error.RESOURCE_NOT_FOUND, e.getResourceName());
        var resposta = RespostaUtils.notFound(mensagem, e.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resposta);
    }

}
