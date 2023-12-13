package com.rakel.atjava.utils;

import com.rakel.atjava.model.Categoria;
import com.rakel.atjava.model.Produto;
import com.rakel.atjava.model.interfaces.CategoriaRepository;
import com.rakel.atjava.model.interfaces.ProdutoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class InicializadorDados implements CommandLineRunner {

    private final CategoriaRepository categoriaRepository;
    private final ProdutoRepository produtoRepository;

    public InicializadorDados(CategoriaRepository categoriaRepository, ProdutoRepository produtoRepository) {
        this.categoriaRepository = categoriaRepository;
        this.produtoRepository = produtoRepository;
    }

    @Override
    public void run(String... args) {
        if (categoriaRepository.count() == 0) {
            Categoria categoria1 = Categoria.builder().nome("Eletronicos").build();
            Categoria categoria2 = Categoria.builder().nome("Sala").build();
            Categoria categoria3 = Categoria.builder().nome("Cozinha").build();
            Categoria categoria4 = Categoria.builder().nome("Brinquedos").build();
            Categoria categoria5 = Categoria.builder().nome("Banheiros").build();
            categoriaRepository.saveAll(Arrays.asList(categoria1, categoria2, categoria3, categoria4, categoria5));
        }

        if (produtoRepository.count() == 0) {
            List<Categoria> categoriaList = categoriaRepository.findAll();
            List<String> produtosNomes = Arrays.asList("Smartphone", "TV", "Sofá", "Liquidificador", "Boneca", "Toalhas", "Notebook", "Cadeira", "Forno",
                    "Bicicleta");

            for (int i = 0; i < 10; i++) {
                Produto produto = Produto.builder()
                        .nome(produtosNomes.get(i % produtosNomes.size()))
                        .descricao("Descrição do produto " + produtosNomes.get(i % produtosNomes.size()))
                        .preco(BigDecimal.valueOf(Math.random() * 1000))
                        .categorias(Collections.singletonList(categoriaList.get(i % categoriaList.size())))
                        .build();
                produtoRepository.save(produto);
            }
        }
    }
}
