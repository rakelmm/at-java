package com.rakel.atjava.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Builder
public class Produto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String descricao;
    private BigDecimal preco;
    @ManyToMany
    @JoinTable(
            name = "produto_categoria",
            joinColumns = @JoinColumn(name = "produto_id"),
            inverseJoinColumns = @JoinColumn(name = "categoria_id")
    )
    private List<Categoria> categorias = new ArrayList<>();

    public Produto(Produto produto) {
        this.nome = produto.getNome();
        this.descricao = produto.getDescricao();
        this.preco = produto.getPreco();
        this.categorias = produto.getCategorias();
    }

    public Produto(ProdutoRequest produtoRequest) {
        this.nome = produtoRequest.nome();
        this.descricao = produtoRequest.descricao();
        this.preco = produtoRequest.preco();
        this.categorias = new ArrayList<>(produtoRequest.categorias());
    }

    public void updateData(Produto produto) {
        if (produto.nome != null) {
            this.nome = produto.getNome();
        }

        if (produto.descricao != null) {
            this.descricao = produto.getDescricao();
        }

        if (produto.preco != null) {
            this.preco = produto.getPreco();
        }

        if (!produto.categorias.isEmpty()) {
            this.categorias = produto.getCategorias();
        }
    }
}
