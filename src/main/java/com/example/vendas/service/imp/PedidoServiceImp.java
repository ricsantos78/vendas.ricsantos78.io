package com.example.vendas.service.imp;

import com.example.vendas.domain.entity.Cliente;
import com.example.vendas.domain.entity.ItemPedido;
import com.example.vendas.domain.entity.Pedido;
import com.example.vendas.domain.entity.Produto;
import com.example.vendas.domain.enums.StatusPedido;
import com.example.vendas.domain.repository.Clientes;
import com.example.vendas.domain.repository.ItemsPedido;
import com.example.vendas.domain.repository.Pedidos;
import com.example.vendas.domain.repository.Produtos;
import com.example.vendas.exception.PedidoNaoEncontradoException;
import com.example.vendas.exception.RegraDeNegocioException;
import com.example.vendas.rest.dto.ItemPedidoDTO;
import com.example.vendas.rest.dto.PedidoDTO;
import com.example.vendas.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PedidoServiceImp implements PedidoService {
    private final Pedidos repository;
    private final Clientes clientesRepository;
    private final Produtos produtosRepository;
    private final ItemsPedido itemsPedidoRepository;

    @Override
    @Transactional
    public Pedido salvar(PedidoDTO dto) {
        Integer idCliente = dto.getCliente();

        Cliente cliente = clientesRepository
                .findById(idCliente)
                .orElseThrow(() -> new RegraDeNegocioException("Codigo de cliente Inválido."));

        Pedido pedido = new Pedido();
        pedido.setTotal(dto.getTotal());
        pedido.setDataPedido(LocalDate.now());
        pedido.setCliente(cliente);
        pedido.setStatus(StatusPedido.REALIZADO);

        List<ItemPedido> itensPedidos = converterItem(pedido, dto.getItens());
        repository.save(pedido);
        itemsPedidoRepository.saveAll(itensPedidos);
        pedido.setItens(itensPedidos);
        return pedido;
    }

    @Override
    public Optional<Pedido> obterPedidoCompleto(Integer id) {
        return repository.findByIdFetchItens(id);
    }

    @Override
    @Transactional
    public void atualizaStatus(Integer id, StatusPedido statusPedido) {
        repository
                .findById(id)
                .map(pedido -> {
                    pedido.setStatus(statusPedido);
                    return repository.save(pedido);
                }).orElseThrow(() -> new PedidoNaoEncontradoException());
    }

    private List<ItemPedido> converterItem(Pedido pedido,List<ItemPedidoDTO> itens){
        if (itens.isEmpty()) {
            throw new RegraDeNegocioException("Não é possivel realizar um pedido sem itens");
        }

        return itens
                .stream()
                .map(dto -> {
                    Integer idProduto = dto.getProduto();
                    Produto produto = produtosRepository
                            .findById(idProduto)
                            .orElseThrow(
                                    () -> new RegraDeNegocioException(
                                            "Codigo de produto Inválido: "+ idProduto
                                    ));

                    ItemPedido itemPedido = new ItemPedido();
                    itemPedido.setQuantidade(dto.getQuantidade());
                    itemPedido.setPedido(pedido);
                    itemPedido.setProduto(produto);
                    return itemPedido;
                }).collect(Collectors.toList());
    }
}
