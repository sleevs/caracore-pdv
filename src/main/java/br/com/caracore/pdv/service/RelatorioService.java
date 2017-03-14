package br.com.caracore.pdv.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.caracore.pdv.model.ItemVenda;
import br.com.caracore.pdv.model.Pagamento;
import br.com.caracore.pdv.model.Venda;
import br.com.caracore.pdv.util.Util;

@Service
public class RelatorioService {

	@Autowired
	private ItemVendaService itemVendaService;
	
	@Autowired
	private PagamentoService pagamentoService;
	
	/**
	 * Pesquisar o pagamento pelo código
	 * 
	 * @param codigoPagamento
	 * @return
	 */
	public Pagamento buscarPorCodigoPagamento(Long codigoPagamento) {
		return pagamentoService.pesquisarPorCodigo(codigoPagamento);
	}
	
	/**
	 * Método externo para buscar itens da compra
	 * 
	 * @param codigoPagamento
	 * @return
	 */
	public List<ItemVenda> buscarPorVenda(Venda venda) {
		List<ItemVenda> lista = itemVendaService.buscarItens(venda);
		if (Util.validar(lista)) {
			List<ItemVenda> result = new ArrayList<>();
			for (ItemVenda itemVenda : lista) {
				if (Util.validar(itemVenda.getProduto())) {
					if (Util.validar(itemVenda.getProduto().getDescricao())) {
						String descricao = itemVenda.getProduto().getDescricao();
						itemVenda.setDescricaoProduto(descricao);
					}
				}
				result.add(itemVenda);
			}
			lista = result;
		}
		return lista;
	}

	/**
	 * Método externo para recuperar nome do cliente
	 * 
	 * @param venda
	 * @return
	 */
	public String cliente(Venda venda) {
		String nome = "";
		if (Util.validar(venda.getCliente())) {
			nome = venda.getCliente().getNome(); 
		}
		return nome;
	}

	/**
	 * Método externo para recuperar nome do vendedor
	 * 
	 * @param venda
	 * @return
	 */
	public String vendedor(Venda venda) {
		String nome = "";
		if (Util.validar(venda.getVendedor())) {
			nome = venda.getVendedor().getNome(); 
		}
		return nome;
	}

	/**
	 * Método externo para recuperar nome da loja
	 * 
	 * @param venda
	 * @return
	 */
	public String loja(Venda venda) {
		String nome = "";
		if (Util.validar(venda.getVendedor())) {
			if (Util.validar(venda.getVendedor().getLoja())) {
				nome = venda.getVendedor().getLoja().getNome(); 
			}
		}
		return nome;
	}

}
