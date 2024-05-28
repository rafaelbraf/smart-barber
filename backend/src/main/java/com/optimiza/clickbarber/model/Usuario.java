package com.optimiza.clickbarber.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Table(name = "usuarios")
@Entity
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String cpf;
    private String nome;
    private LocalDate dataNascimento;
    private String email;
    private String celular;
    private String senha;

}
