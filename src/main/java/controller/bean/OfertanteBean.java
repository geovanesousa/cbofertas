package controller.bean;

import controller.dao.OfertanteDao;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import model.Ofertante;

@ManagedBean
@ViewScoped
public class OfertanteBean {

	private Ofertante ofertante = new Ofertante();
	private String senha2 = "";

	public OfertanteBean() {
		this.ofertante.setSenha("");
	}

	public String inserirOfertante() {
		if (!this.ofertante.getSenha().equals("") && !this.senha2.equals("")) {
			if (this.ofertante.getSenha().equals(this.senha2)) {
				OfertanteDao ofertanteDao = new OfertanteDao();
				this.ofertante.setId(ofertanteDao.inserirOfertante(this.ofertante));
				if (this.ofertante.getId() > 0) {
					FacesContext fc = FacesContext.getCurrentInstance();
					FacesMessage fm = new FacesMessage("Inserido com sucesso! " + "Seu login é o seu CNPJ.");
					fm.setSeverity(FacesMessage.SEVERITY_INFO);
					fc.addMessage(null, fm);
				} else {
					FacesContext fc = FacesContext.getCurrentInstance();
					FacesMessage fm = new FacesMessage("Erro ao inserir!");
					fm.setSeverity(FacesMessage.SEVERITY_ERROR);
					fc.addMessage(null, fm);
					return null;
				}
			} else {
				FacesContext fc = FacesContext.getCurrentInstance();
				FacesMessage fm = new FacesMessage("As senhas não correspondem!");
				fm.setSeverity(FacesMessage.SEVERITY_ERROR);
				fc.addMessage(null, fm);
			}
		} else {
			FacesContext fc = FacesContext.getCurrentInstance();
			FacesMessage fm = new FacesMessage("Senhas obrigatórias!");
			fm.setSeverity(FacesMessage.SEVERITY_ERROR);
			fc.addMessage(null, fm);
		}
		return null;
	}

	public String ofertantePorCnpj() {
		if (this.ofertante.getPessoaJuridica().getCnpj() != null) {
			OfertanteDao ofertanteDao = new OfertanteDao();
			this.ofertante = ofertanteDao.consultarOfertante(this.ofertante.getPessoaJuridica().getCnpj());
			this.senha2 = this.ofertante.getSenha();
		}
		return null;
	}

	public String nmOfertantePorCnpj() {
		if (this.ofertante.getPessoaJuridica().getCnpj() != null) {
			OfertanteDao ofertanteDao = new OfertanteDao();
			this.ofertante = ofertanteDao.nomeFantasiaDoOfertante(this.ofertante.getPessoaJuridica().getCnpj());
		}
		return null;
	}
	
	public String excluirOfertante() {
		if (this.ofertante.getPessoaJuridica().getCnpj() != null || 
				!this.ofertante.getPessoaJuridica().getCnpj().equals("")) {
			OfertanteDao ofertanteDao = new OfertanteDao();
			boolean retorno = ofertanteDao.excluirOfertante(this.ofertante);
			if(retorno==true){
				FacesContext fc = FacesContext.getCurrentInstance();
				FacesMessage fm = new FacesMessage("Excluído com sucesso!");
				fm.setSeverity(FacesMessage.SEVERITY_INFO);
				fc.addMessage(null, fm);
			}else{
				FacesContext fc = FacesContext.getCurrentInstance();
				FacesMessage fm = new FacesMessage("Erro ao excluir!");
				fm.setSeverity(FacesMessage.SEVERITY_ERROR);
				fc.addMessage(null, fm);
			}
		}else{
			FacesContext fc = FacesContext.getCurrentInstance();
			FacesMessage fm = new FacesMessage("CNPJ obrigatório!");
			fm.setSeverity(FacesMessage.SEVERITY_ERROR);
			fc.addMessage(null, fm);
		}
		return null;
	}

	public String atualizarOfertante() {
		if (this.senha2.equals(this.ofertante.getSenha())) {
			OfertanteDao ofDao = new OfertanteDao();
			boolean retorno = ofDao.atualizarOfertante(this.ofertante);
			if (retorno == true) {
				FacesContext fc = FacesContext.getCurrentInstance();
				FacesMessage fm = new FacesMessage("Atualizado com sucesso!");
				fm.setSeverity(FacesMessage.SEVERITY_INFO);
				fc.addMessage(null, fm);
			} else {
				FacesContext fc = FacesContext.getCurrentInstance();
				FacesMessage fm = new FacesMessage("Erro ao atualizar!");
				fm.setSeverity(FacesMessage.SEVERITY_ERROR);
				fc.addMessage(null, fm);
			}
		} else {
			FacesContext fc = FacesContext.getCurrentInstance();
			FacesMessage fm = new FacesMessage("As senhas não correspondem!");
			fm.setSeverity(FacesMessage.SEVERITY_ERROR);
			fc.addMessage(null, fm);
		}
		return null;
	}

	public String limparCampos() {
		this.ofertante = new Ofertante();
		this.senha2 = "";
		return null;
	}

	public Ofertante getOfertante() {
		return ofertante;
	}

	public void setOfertante(Ofertante ofertante) {
		this.ofertante = ofertante;
	}

	public String getSenha2() {
		return senha2;
	}

	public void setSenha2(String senha2) {
		this.senha2 = senha2;
	}

}
