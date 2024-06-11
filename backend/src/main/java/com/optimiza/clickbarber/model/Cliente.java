package com.optimiza.clickbarber.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Table(name = "clientes")
@Entity
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cpf;
    private String nome;
    private LocalDate dataNascimento;
    private String celular;

    @OneToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

}
