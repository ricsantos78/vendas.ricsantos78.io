package com.example.vendas.domain.repository;

import com.example.vendas.domain.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Clientes extends JpaRepository<Cliente, Integer> {



}
