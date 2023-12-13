package com.rakel.atjava.model.interfaces;

import com.rakel.atjava.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
}
