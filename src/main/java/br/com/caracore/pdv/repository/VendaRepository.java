package br.com.caracore.pdv.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.caracore.pdv.model.Venda;
import br.com.caracore.pdv.model.Vendedor;

public interface VendaRepository extends JpaRepository<Venda, Long> {

	public List<Venda> findByData(Date data);
	
	public List<Venda> findByVendedor(Vendedor vendedor);

}