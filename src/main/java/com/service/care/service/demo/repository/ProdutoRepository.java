package com.service.care.service.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.service.care.service.demo.model.Produto;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    // Não precisa digitar nenhum método aqui dentro, o Spring Boot faz tudo sozinho.
}