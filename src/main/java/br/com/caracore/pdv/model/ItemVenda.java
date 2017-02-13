package br.com.caracore.pdv.model;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.NumberFormat;

@Entity
public class ItemVenda {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long codigo;
	
	@NotNull(message = "Produto é obrigatório!")
	@OneToOne
	@JoinColumn(name = "PRODUTO_ID")
	private Produto produto;
	
	@NotNull(message = "Quantidade é obrigatória!")
	private Integer quantidade;
	
	@NotNull(message = "Preço é obrigatório!")
	private BigDecimal precoUnitario;
	
	@NumberFormat(pattern = "#0.000")
	@DecimalMin(value = "0.01", message = "Desconto não pode ser maior que 0,01")
	@DecimalMax(value = "1.00", message = "Desconto não pode ser maior que 1,00")
	private BigDecimal desconto;
	
	@NotNull(message = "Venda é obrigatória!")
	@OneToOne
	@JoinColumn(name = "VENDA_ID")
	private Venda venda;
	
	@Transient
	private BigDecimal subTotal;

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

	public BigDecimal getPrecoUnitario() {
		return precoUnitario;
	}

	public void setPrecoUnitario(BigDecimal precoUnitario) {
		this.precoUnitario = precoUnitario;
	}

	public BigDecimal getDesconto() {
		return desconto;
	}

	public void setDesconto(BigDecimal desconto) {
		this.desconto = desconto;
	}

	public Venda getVenda() {
		return venda;
	}

	public void setVenda(Venda venda) {
		this.venda = venda;
	}
	
	public BigDecimal getSubTotal() {
		long lngSubTotal = 0;
		if (precoUnitario != null && desconto != null && quantidade != null) {
			long lngPrecoTotal = precoUnitario.longValue();
			long lngDesconto = desconto.longValue();
			int intQuantidade = quantidade.intValue();
			if (lngDesconto > 0 && lngDesconto <= 1) {
				lngPrecoTotal = lngPrecoTotal * intQuantidade;
				lngSubTotal = lngPrecoTotal - (lngPrecoTotal * lngDesconto);
			}
		}
		return BigDecimal.valueOf(lngSubTotal);

	}	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		result = prime * result + ((desconto == null) ? 0 : desconto.hashCode());
		result = prime * result + ((precoUnitario == null) ? 0 : precoUnitario.hashCode());
		result = prime * result + ((produto == null) ? 0 : produto.hashCode());
		result = prime * result + ((quantidade == null) ? 0 : quantidade.hashCode());
		result = prime * result + ((venda == null) ? 0 : venda.hashCode());
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
		ItemVenda other = (ItemVenda) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		if (desconto == null) {
			if (other.desconto != null)
				return false;
		} else if (!desconto.equals(other.desconto))
			return false;
		if (precoUnitario == null) {
			if (other.precoUnitario != null)
				return false;
		} else if (!precoUnitario.equals(other.precoUnitario))
			return false;
		if (produto == null) {
			if (other.produto != null)
				return false;
		} else if (!produto.equals(other.produto))
			return false;
		if (quantidade == null) {
			if (other.quantidade != null)
				return false;
		} else if (!quantidade.equals(other.quantidade))
			return false;
		if (venda == null) {
			if (other.venda != null)
				return false;
		} else if (!venda.equals(other.venda))
			return false;
		return true;
	}

}