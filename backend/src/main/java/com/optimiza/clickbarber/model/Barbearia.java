package com.optimiza.clickbarber.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Objects;

@Table(name = "barbearias")
@Entity
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Barbearia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String cnpj;
    private String nome;
    private String endereco;
    private String email;
    private String senha;
    private String telefone;

    @OneToMany(mappedBy = "barbearia", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Servico> servicos;

    @OneToMany(mappedBy = "barbearia", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Barbeiro> barbeiros;

}
