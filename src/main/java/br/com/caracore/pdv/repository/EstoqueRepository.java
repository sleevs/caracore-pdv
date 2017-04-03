package br.com.caracore.pdv.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.caracore.pdv.model.Estoque;
import br.com.caracore.pdv.model.Loja;
import br.com.caracore.pdv.model.Produto;

public interface EstoqueRepository extends JpaRepository<Estoque, Long> {

	List<Estoque> findByLojaOrderByProduto(Loja loja);

	List<Estoque> findByProduto(Produto produto);

	List<Estoque> findByLojaAndProduto(Loja loja, Produto produto);
	
	@Query("select e from Estoque e where e.loja.nome like ?1 and e.produto.descricao like ?2 order by e.produto")
	public List<Estoque> findByLojaAndProdutoByOrderByProduto(String loja, String produto);
	
	@Query("select e from Estoque e where e.loja.nome like ?1 order by e.produto")
	public List<Estoque> findByLojaByOrderByProduto(String loja);
	
	@Query("select e from Estoque e where e.produto.descricao like ?1 order by e.produto")
	public List<Estoque> findByProdutoByOrderByProduto(String produto);

}