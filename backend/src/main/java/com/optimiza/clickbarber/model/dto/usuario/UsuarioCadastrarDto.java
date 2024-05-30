package com.optimiza.clickbarber.model.dto.usuario;

import com.optimiza.clickbarber.model.Role;
import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsuarioCadastrarDto {

    private String email;
    private String senha;
    private Role role;
    private Object data;

}
