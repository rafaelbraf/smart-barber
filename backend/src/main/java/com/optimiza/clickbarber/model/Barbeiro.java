package com.optimiza.clickbarber.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Table(name = "barbeiros")
@Entity
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Barbeiro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String cpf;
    private String nome;
    private String email;
    private String celular;
    private boolean admin;
    private boolean ativo;

    @ManyToOne
    @JoinColumn(name = "barbearia_id")
    @JsonBackReference
    private Barbearia barbearia;

}
