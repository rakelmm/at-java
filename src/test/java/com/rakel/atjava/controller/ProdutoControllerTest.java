package com.rakel.atjava.controller;

import com.rakel.atjava.model.Categoria;
import com.rakel.atjava.model.Produto;
import com.rakel.atjava.model.ProdutoRequest;
import com.rakel.atjava.model.interfaces.CategoriaRepository;
import com.rakel.atjava.model.interfaces.ProdutoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(MockitoExtension.class)
class ProdutoControllerTest {
    @Mock
    private ProdutoRepository produtoRepository;
    @Mock
    private CategoriaRepository categoriaRepository;
    @InjectMocks
    private ProdutoController produtoController;

    @Test
    @DisplayName("Test get produto pelo id")
    void testGetProdutoById() {
        Produto produto = new Produto();
        produto.setId(1L);
        produto.setNome("Produto Teste");

        Mockito.when(produtoRepository.findById(anyLong())).thenReturn(Optional.of(produto));

        ResponseEntity<Produto> produtoById = produtoController.getProdutoById(1L);

        assertEquals(HttpStatus.OK, produtoById.getStatusCode());
        assertNotNull(produtoById.getBody());
        assertEquals(produto.getId(), produtoById.getBody().getId());
    }

    @Test
    @DisplayName("Test post produto")
    void testPost() {
        Categoria categoria = new Categoria(1L, "Eletrônicos");

        Mockito.when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));
        Mockito.when(produtoRepository.save(Mockito.any())).thenAnswer(invocation -> {
            Produto produtoSalvo = invocation.getArgument(0);
            produtoSalvo.setId(1L);
            return produtoSalvo;
        });

        ProdutoRequest produtoRequest = new ProdutoRequest(null, "Smartphone", "Descrição do Smartphone", BigDecimal.valueOf(999.99), List.of(categoria));

        ResponseEntity<ProdutoRequest> responseEntity = produtoController.post(produtoRequest, UriComponentsBuilder.fromPath("/produto"));

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertNotNull(responseEntity.getBody().id());
        assertEquals(produtoRequest.nome(), responseEntity.getBody().nome());
    }

    @Test
    @DisplayName("Test put produto")
    void testPut() {
        Produto produtoExistente = new Produto();
        produtoExistente.setId(1L);
        produtoExistente.setNome("Produto Existente");
        produtoExistente.setDescricao("Descrição do Produto");
        Mockito.when(produtoRepository.getReferenceById(anyLong())).thenReturn(produtoExistente);
        Produto produtoAtualizado = new Produto();
        produtoAtualizado.setId(1L);
        produtoAtualizado.setNome("Produto Atualizado");
        ResponseEntity<ProdutoRequest> response = produtoController.put(produtoAtualizado);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(produtoAtualizado.getNome(), response.getBody().nome());
    }

    @Test
    @DisplayName("Test delete produto")
    void testDelete() {
        Long id = 1L;

        Mockito.doNothing().when(produtoRepository).deleteById(id);

        ResponseEntity<String> response = produtoController.delete(id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }
}