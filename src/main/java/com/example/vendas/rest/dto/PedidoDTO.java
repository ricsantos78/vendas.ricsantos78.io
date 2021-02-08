package com.example.vendas.rest.dto;

import com.example.vendas.validation.NotEmptyList;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PedidoDTO {

    @NotNull(message = "{campo.codigo-cliente.obrigatorio}")
    private Integer cliente;

    @NotNull(message = "{campo.codigo-pedido.obrigatorio}")
    private BigDecimal total;

    @NotEmptyList(message = "{campo.itens-cliente.obrigatorio}.")
    private List<ItemPedidoDTO> itens;


}
