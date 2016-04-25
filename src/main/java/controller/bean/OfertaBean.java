package controller.bean;

import controller.dao.OfertaDao;

import java.util.List;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import model.Oferta;
import model.Ofertante;

@ManagedBean
@SessionScoped
public class OfertaBean {

	private Oferta oferta = new Oferta();
	private List<Oferta> ofertas = null;
	private Map<String, String> cidades = null;
	private Map<String, String> bairros = null;
	private Integer id;

	public String inserirOferta() {
		OfertaDao ofertaDao = new OfertaDao();
		this.oferta.setOfertante((Ofertante) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.get("usuarioLogado"));
		this.oferta.setId(ofertaDao.inserirOferta(this.oferta));
		if (this.oferta.getId() > 0) {
			FacesContext fc = FacesContext.getCurrentInstance();
			FacesMessage fm = new FacesMessage("Inserida com sucesso!");
			fm.setSeverity(FacesMessage.SEVERITY_INFO);
			fc.addMessage(null, fm);
		} else {
			FacesContext fc = FacesContext.getCurrentInstance();
			FacesMessage fm = new FacesMessage("Erro ao inserir!");
			fm.setSeverity(FacesMessage.SEVERITY_ERROR);
			fc.addMessage(null, fm);
			return null;
		}
		return null;
	}

	public String listaOfertasBairro() {
		if (!this.oferta.getOfertante().getPessoaJuridica().getEndereco().getBairro().equals("")) {
			OfertaDao ofertaDao = new OfertaDao();
			this.ofertas = ofertaDao
					.consultarOfertaPorBairro(this.oferta.getOfertante().getPessoaJuridica().getEndereco().getBairro());
			try {
				this.ofertas.get(0);
			} catch (IndexOutOfBoundsException e) {
				FacesContext fc = FacesContext.getCurrentInstance();
				FacesMessage fm = new FacesMessage("Nenhuma oferta encontrada!");
				fm.setSeverity(FacesMessage.SEVERITY_ERROR);
				fc.addMessage(null, fm);
				return null;
			}
			return "/restrito/cliente/lista_de_ofertas";
		} else {
			FacesContext fc = FacesContext.getCurrentInstance();
			FacesMessage fm = new FacesMessage("Bairro obrigatório!");
			fm.setSeverity(FacesMessage.SEVERITY_ERROR);
			fc.addMessage(null, fm);
			return null;
		}
	}

	public String listaOfertasPorCnpj() {
		Ofertante ofertante = (Ofertante) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.get("usuarioLogado");
		OfertaDao ofertaDao = new OfertaDao();
		this.ofertas = ofertaDao.consultarOfertaCnpj(ofertante.getPessoaJuridica().getCnpj());
		try {
			this.ofertas.get(0);
		} catch (IndexOutOfBoundsException e) {
			FacesContext fc = FacesContext.getCurrentInstance();
			FacesMessage fm = new FacesMessage("Nenhuma oferta encontrada!");
			fm.setSeverity(FacesMessage.SEVERITY_ERROR);
			fc.addMessage(null, fm);
			return "/restrito/ofertante/bv_ofe";
		}
		return "/restrito/ofertante/lista_de_ofertas";

	}

	public String excluirOfertaPorId() {
		OfertaDao ofertaDao = new OfertaDao();
		ofertaDao.excluirOferta(this.id);
		return this.listaOfertasPorCnpj();
		
	}
	
	public String consultarOfertaPorId() {
		OfertaDao ofertaDao = new OfertaDao();
		this.oferta = ofertaDao.consultarOfertaPorId(this.oferta.getId());
		return null;
		
	}
	
	public String atualizarOferta() {
		OfertaDao ofertaDao = new OfertaDao();
		boolean retorno = ofertaDao.atualizarOferta(this.oferta);
		if(retorno==true){
			FacesContext fc = FacesContext.getCurrentInstance();
			FacesMessage fm = new FacesMessage("Atualizado com sucesso!");
			fm.setSeverity(FacesMessage.SEVERITY_INFO);
			fc.addMessage(null, fm);
		}else{
			FacesContext fc = FacesContext.getCurrentInstance();
			FacesMessage fm = new FacesMessage("Erro ao atualizar!");
			fm.setSeverity(FacesMessage.SEVERITY_ERROR);
			fc.addMessage(null, fm);
		}
		return null;
		
	}

	public String cidadePorUf() {
		OfertaDao ofertaDao = new OfertaDao();
		this.cidades = ofertaDao
				.consultarCidadesPorUf(this.oferta.getOfertante().getPessoaJuridica().getEndereco().getUf());
		if (this.cidades == null) {
			FacesContext fc = FacesContext.getCurrentInstance();
			FacesMessage fm = new FacesMessage("Nenhuma cidade encontrada!");
			fm.setSeverity(FacesMessage.SEVERITY_ERROR);
			fc.addMessage(null, fm);
			return null;
		}
		return null;
	}

	public String bairroPorCidade() {
		OfertaDao ofertaDao = new OfertaDao();
		this.bairros = ofertaDao
				.consultarBairrosPorCidade(this.oferta.getOfertante().getPessoaJuridica().getEndereco().getCidade());
		if (this.bairros == null) {
			FacesContext fc = FacesContext.getCurrentInstance();
			FacesMessage fm = new FacesMessage("Nenhum bairro encontrado!");
			fm.setSeverity(FacesMessage.SEVERITY_ERROR);
			fc.addMessage(null, fm);
			return null;
		}
		return null;
	}

	public String limparCampos() {
		this.oferta = new Oferta();
		this.bairros = null;
		this.cidades = null;
		this.ofertas = null;
		this.id = null;
		return null;
	}

	public String paginaInicial() {
		this.oferta = new Oferta();
		this.bairros = null;
		this.cidades = null;
		this.ofertas = null;
		this.id = null;
		return "index";
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Oferta getOferta() {
		return oferta;
	}

	public void setOferta(Oferta oferta) {
		this.oferta = oferta;
	}

	public List<Oferta> getOfertas() {
		return ofertas;
	}

	public void setOfertas(List<Oferta> ofertas) {
		this.ofertas = ofertas;
	}

	public Map<String, String> getCidades() {
		return cidades;
	}

	public void setCidades(Map<String, String> cidades) {
		this.cidades = cidades;
	}

	public Map<String, String> getBairros() {
		return bairros;
	}

	public void setBairros(Map<String, String> bairros) {
		this.bairros = bairros;
	}

}
