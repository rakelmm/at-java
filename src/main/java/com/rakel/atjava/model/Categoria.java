package com.rakel.atjava.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Builder
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String nome;

    public Categoria(Categoria categoria) {
        this.id = categoria.getId();
        this.nome = categoria.getNome();
    }

    public void updateData(Categoria categoria) {
        if (categoria.nome != null) {
            this.nome = categoria.getNome();
        }
    }
}
