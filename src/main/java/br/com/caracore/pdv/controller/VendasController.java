package br.com.caracore.pdv.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.caracore.pdv.model.ItemVenda;
import br.com.caracore.pdv.model.Operador;
import br.com.caracore.pdv.model.Venda;
import br.com.caracore.pdv.model.Vendedor;
import br.com.caracore.pdv.repository.filter.ProdutoFilter;
import br.com.caracore.pdv.service.VendaService;
import br.com.caracore.pdv.util.Util;

@Controller
@RequestMapping("/vendas")
public class VendasController {
	
	@Autowired
	private VendaService vendaService;
	
	@GetMapping("/produto")
	public ModelAndView pesquisarProduto(ProdutoFilter produtoFilter) {
		Long codigoProduto = null;
		Operador operador = recuperarOperador();
		if (Util.validar(produtoFilter)) {
			if (Util.validar(produtoFilter.getCodigo())) {
				codigoProduto = Long.valueOf(produtoFilter.getCodigo());
			}
		}
		Venda venda = vendaService.comprar(codigoProduto, operador);
		return novo(venda);
	}
	
	@GetMapping("/novo")
	public ModelAndView novo(Venda venda) {
		ModelAndView mv = new ModelAndView("venda/cadastro-venda");
		Operador operador = recuperarOperador();
		if (!vendaService.validarVendaEmAndamento(venda)) {
			Vendedor vendedor = vendaService.buscarVendedor(operador);
			venda = vendaService.recuperarVendaEmAberto(vendedor);
		}
		mv.addObject("vendedores", buscarVendedores(operador));
		mv.addObject(limparFiltro(venda));
		mv.addObject(venda);
		return mv;
	}
	
	@PostMapping("/novo")
	public ModelAndView salvar(@Valid Venda venda, BindingResult result, RedirectAttributes attributes) {
		if (result.hasErrors()) {
			return novo(venda);
		}
		vendaService.salvar(venda);
		attributes.addFlashAttribute("mensagem", "Venda salva com sucesso!");
		return new ModelAndView("redirect:/vendas/novo");
	}

	/**
	 * Método para recuperar operador logado
	 * 
	 * @return
	 */
	private Operador recuperarOperador() {
		Operador operador = null;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null && auth.getName() != null) {
			String login = auth.getName();
			operador = vendaService.recuperarOperador(login);
		}
		return operador;
	}
	
	/**
	 * Método para auxiliar no filtro de tela de pesquisa de vendas em aberto
	 * 
	 * @param venda
	 * @return
	 */
	private ProdutoFilter limparFiltro(Venda venda) {
		String strUltimoCodigo = "";
		String strUltimaDescricao = "";
		String strData = "";
		ItemVenda item = vendaService.recuperarUltimoItemVendaDaCesta(venda);
		if (Util.validar(item)) {
			strUltimoCodigo = item.getProduto().getCodigo().toString();
			strUltimaDescricao = item.getProduto().getDescricao();
		}
		if (Util.validar(venda.getData())) {
			strData = vendaService.formatarData(venda.getData());
		} else {
			strData = vendaService.formatarData(null);
		}
		
		ProdutoFilter filtro = new ProdutoFilter(strUltimoCodigo, strUltimaDescricao, strData);
		return filtro;
	}
	
	/**
	 * Médoto para recuperar lista de vendedores da loja
	 * 
	 * @param operador
	 * @return
	 */
	private List<Vendedor> buscarVendedores(Operador operador) {
		List<Vendedor> vendedores = vendaService.listarVendedoresPorOperador(operador);
		if (!Util.validar(vendedores)) {
			vendedores = Util.criarListaDeVendedores();
		}
		return vendedores;
	}
	
}

