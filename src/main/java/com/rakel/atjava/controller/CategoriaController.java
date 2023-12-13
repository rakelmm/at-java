package com.rakel.atjava.controller;

import com.rakel.atjava.model.Categoria;
import com.rakel.atjava.model.interfaces.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categoria")
public class CategoriaController {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @GetMapping
    public ResponseEntity<List<Categoria>> getAllCategorias() {
        List<Categoria> allCategorias = categoriaRepository.findAll();
        return ResponseEntity.ok(allCategorias);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Categoria> getCategoriaById(@PathVariable Long id) {
        Categoria categoria = categoriaRepository.findById(id).orElse(null);
        return ResponseEntity.ok(categoria);
    }

    @PostMapping
    @Transactional
    public Categoria post(@RequestBody Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    @PutMapping
    @Transactional
    public ResponseEntity<Categoria> put(@RequestBody Categoria categoriaNova) {
        Categoria categoria = categoriaRepository.getReferenceById(categoriaNova.getId());
        categoria.updateData(categoriaNova);

        return ResponseEntity.ok(new Categoria(categoria));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<String> delete(@PathVariable Long id) {
        categoriaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
