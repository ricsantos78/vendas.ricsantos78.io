package com.example.vendas.rest.controller;

import com.example.vendas.exception.PedidoNaoEncontradoException;
import com.example.vendas.exception.RegraDeNegocioException;
import com.example.vendas.rest.ApiErrors;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ApplicationControllerAdvice {

@ExceptionHandler(RegraDeNegocioException.class)
@ResponseStatus(HttpStatus.BAD_REQUEST)
public ApiErrors handleRegraDeNegocioException(RegraDeNegocioException ex) {
    String messagemErro = ex.getMessage();
    return new ApiErrors(messagemErro);
    }
@ExceptionHandler(PedidoNaoEncontradoException.class)
@ResponseStatus(HttpStatus.NOT_FOUND)
public ApiErrors handlePedidoNotFoundException(PedidoNaoEncontradoException ex){
    return new ApiErrors(ex.getMessage());
}

@ExceptionHandler(MethodArgumentNotValidException.class)
@ResponseStatus(HttpStatus.BAD_REQUEST)
public ApiErrors handleMethodNotValidExceotion(MethodArgumentNotValidException ex){
    List<String> errors = ex.getBindingResult()
            .getAllErrors()
            .stream()
            .map(e -> e.getDefaultMessage())
            .collect(Collectors.toList());

    return new ApiErrors(errors);
}
}
