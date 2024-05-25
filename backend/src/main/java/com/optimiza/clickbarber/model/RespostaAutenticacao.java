package com.optimiza.clickbarber.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class RespostaAutenticacao<T> extends Resposta<T> {

    private String accessToken;

}
