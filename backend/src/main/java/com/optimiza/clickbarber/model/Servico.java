package com.optimiza.clickbarber.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Table(name = "servicos")
@Entity
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Servico {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String nome;
    private BigDecimal preco;
    private Integer tempoDuracaoEmMinutos;

    @ManyToOne
    @JoinColumn(name = "barbearia_id")
    @JsonBackReference
    private Barbearia barbearia;

    private boolean ativo;

}
