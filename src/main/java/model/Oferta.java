package model;

public class Oferta {

	private Integer id;
	private String nomeOferta;
	private String descricao;
	private String categoria;
	private Double preco;
	private Ofertante ofertante;

	public Oferta() {
		this.ofertante = new Ofertante();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNomeOferta() {
		return nomeOferta;
	}

	public void setNomeOferta(String nomeOferta) {
		this.nomeOferta = nomeOferta;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public Double getPreco() {
		return preco;
	}

	public void setPreco(Double preco) {
		this.preco = preco;
	}

	public Ofertante getOfertante() {
		return ofertante;
	}

	public void setOfertante(Ofertante ofertante) {
		this.ofertante = ofertante;
	}

}
