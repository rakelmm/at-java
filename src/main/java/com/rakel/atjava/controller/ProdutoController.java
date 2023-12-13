package com.rakel.atjava.controller;

import com.rakel.atjava.model.Categoria;
import com.rakel.atjava.model.Produto;
import com.rakel.atjava.model.ProdutoRequest;
import com.rakel.atjava.model.interfaces.CategoriaRepository;
import com.rakel.atjava.model.interfaces.ProdutoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/produto")
public class ProdutoController {
    Logger logger = LoggerFactory.getLogger(ProdutoController.class);

    @Autowired
    private ProdutoRepository produtoRepository;
    @Autowired
    private CategoriaRepository categoriaRepository;
    private final RestTemplate restTemplate;

    @Value("${external.api.url}")
    private String externalApiUrl;

    @Value("${external.jsonplaceholder.api.url}")
    private String externalJsonPlaceHolderApi;

    public ProdutoController() {
        this.restTemplate = new RestTemplate();
    }

    @GetMapping("/api-externa/{id}")
    public ResponseEntity<String> getApiExterna(@PathVariable Long id) {
        String url = externalJsonPlaceHolderApi + "/posts/" + id;
        String post = restTemplate.getForObject(url, String.class);

        return ResponseEntity.ok().body(post);
    }

    @GetMapping
    public ResponseEntity<List<Produto>> getAll() {
        List<Produto> allProdutos = produtoRepository.findAll();
        return ResponseEntity.ok().body(allProdutos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Produto> getProdutoById(@PathVariable Long id) {
        Optional<Produto> optionalProduto = produtoRepository.findById(id);

        if (optionalProduto.isPresent()) {
            Produto produto = optionalProduto.get();
            HttpStatusCode statusCode = ResponseEntity.ok().body(produto).getStatusCode();
            logger.info("Status code para o método getProdutoById: " + statusCode);

            return ResponseEntity.ok().body(produto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/campos")
    public ResponseEntity<List<Produto>> getProdutoPorCampos(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) Long categoriaId) {

        Categoria categoria = (categoriaId != null) ? categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new EntityNotFoundException("categoria não encontrada: " + categoriaId)) : null;

        List<Produto> produtoByCriterio = produtoRepository.findProdutoByCriterio(id, nome, categoria);

        return ResponseEntity.ok().body(produtoByCriterio);
    }

    @PostMapping
    @Transactional
    public ResponseEntity<ProdutoRequest> post(@RequestBody ProdutoRequest produtoRequest, UriComponentsBuilder uriBuilder) {
        Produto novoProduto = new Produto(produtoRequest);

        if (produtoRequest.categorias() != null && !produtoRequest.categorias().isEmpty()) {
            List<Categoria> categorias = produtoRequest.categorias().stream()
                    .map(categoria -> categoriaRepository.findById(categoria.getId())
                            .orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada: " + categoria.getId())))
                    .collect(Collectors.toList());
            novoProduto.setCategorias(categorias);
        }

        produtoRepository.save(novoProduto);

        URI uri = uriBuilder.path("/produto/{id}").buildAndExpand(novoProduto.getId()).toUri();

        return ResponseEntity.created(uri).body(new ProdutoRequest(novoProduto));
    }

    @PutMapping
    @Transactional
    public ResponseEntity<ProdutoRequest> put(@RequestBody Produto produtoNovo) {
        Produto produto = produtoRepository.getReferenceById(produtoNovo.getId());
        produto.updateData(produtoNovo);

        return ResponseEntity.ok(new ProdutoRequest(produto));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<String> delete(@PathVariable Long id) {
        produtoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
