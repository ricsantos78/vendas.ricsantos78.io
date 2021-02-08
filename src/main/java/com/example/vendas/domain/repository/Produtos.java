package com.example.vendas.domain.repository;

import com.example.vendas.domain.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Produtos extends JpaRepository <Produto,Integer> {
}
