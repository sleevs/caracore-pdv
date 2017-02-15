package br.com.caracore.pdv.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

import br.com.caracore.pdv.model.types.StatusVenda;

@Entity
public class Venda {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long codigo;
	
	@NotNull(message = "Data é obrigatória!")
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@Temporal(TemporalType.DATE)
	private Date data;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name = "VENDEDOR_ID")
    private Vendedor vendedor;
	
	@ManyToOne
	@JoinColumn(name = "CLIENTE_ID")
	private Cliente cliente;
	
	@NotNull
	@OneToMany(mappedBy = "venda")
	private List<ItemVenda> itens;
	
	@NotNull
	@Enumerated(EnumType.STRING)
	private StatusVenda status;
	
	@DecimalMin(value = "0.01", message = "Desconto não pode ser menor que 0,01")
	@DecimalMax(value = "1.00", message = "Desconto não pode ser maior que 1,00")
	@NumberFormat(pattern = "#0.000")
	private BigDecimal descontoTotal;

	@Transient
	private BigDecimal subTotal;

	@Transient
	private BigDecimal total;

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Vendedor getVendedor() {
		return vendedor;
	}

	public void setVendedor(Vendedor vendedor) {
		this.vendedor = vendedor;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public List<ItemVenda> getItens() {
		return itens;
	}

	public void setItens(List<ItemVenda> itens) {
		this.itens = itens;
	}

	public BigDecimal getSubTotal() {
		long total = 0L;
		if (itens != null && itens.size() > 0) {
			for (ItemVenda itemVenda : itens) {
				if (itemVenda.getSubTotal() != null) {
					total = total + itemVenda.getSubTotal().longValue();
				}
			}
		}
		return BigDecimal.valueOf(total);
	}

	public BigDecimal getDescontoTotal() {
		return descontoTotal;
	}

	public void setDescontoTotal(BigDecimal descontoTotal) {
		this.descontoTotal = descontoTotal;
	}
	
	public StatusVenda getStatus() {
		return status;
	}

	public void setStatus(StatusVenda status) {
		this.status = status;
	}

	public BigDecimal getTotal() {
		long lngTotal = 0;
		if (subTotal != null && descontoTotal != null) {
			long lngSubTotal = subTotal.longValue();
			long lngDesconto = descontoTotal.longValue();
			if (lngDesconto > 0 && lngDesconto <= 1) {
				lngTotal = lngSubTotal - (lngSubTotal * lngDesconto);
			}
		}
		return BigDecimal.valueOf(lngTotal);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cliente == null) ? 0 : cliente.hashCode());
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		result = prime * result + ((descontoTotal == null) ? 0 : descontoTotal.hashCode());
		result = prime * result + ((itens == null) ? 0 : itens.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((vendedor == null) ? 0 : vendedor.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Venda other = (Venda) obj;
		if (cliente == null) {
			if (other.cliente != null)
				return false;
		} else if (!cliente.equals(other.cliente))
			return false;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
			return false;
		if (descontoTotal == null) {
			if (other.descontoTotal != null)
				return false;
		} else if (!descontoTotal.equals(other.descontoTotal))
			return false;
		if (itens == null) {
			if (other.itens != null)
				return false;
		} else if (!itens.equals(other.itens))
			return false;
		if (status != other.status)
			return false;
		if (vendedor == null) {
			if (other.vendedor != null)
				return false;
		} else if (!vendedor.equals(other.vendedor))
			return false;
		return true;
	}
}
