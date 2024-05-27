package com.optimiza.clickbarber.model;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RespostaLogin {

    private boolean success;
    private String message;
    private Object result;
    private String accessToken;

}
