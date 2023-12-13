package com.rakel.atjava.model.interfaces;

import com.rakel.atjava.model.Categoria;
import com.rakel.atjava.model.Produto;
import com.rakel.atjava.model.ProdutoRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    @Query("""
            select distinct p from Produto p
            where
            (:id is null or p.id = :id)
            and (:nome is null or lower(p.nome) = lower(:nome))
            and (:categoria is null or :categoria member of p.categorias)
            """)
    List<Produto> findProdutoByCriterio(Long id, String nome, Categoria categoria);
}
