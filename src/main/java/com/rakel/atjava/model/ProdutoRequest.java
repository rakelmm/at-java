package com.rakel.atjava.model;

import java.math.BigDecimal;
import java.util.List;

public record ProdutoRequest(
        Long id,
        String nome,
        String descricao,
        BigDecimal preco,
        List<Categoria> categorias
) {
    public ProdutoRequest(Produto produto) {
        this(produto.getId(), produto.getNome(), produto.getDescricao(), produto.getPreco(), produto.getCategorias());
    }
}
