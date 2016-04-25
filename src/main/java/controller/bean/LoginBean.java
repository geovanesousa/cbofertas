package controller.bean;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import controller.dao.AdministradorDao;
import controller.dao.OfertanteDao;
import model.Administrador;
import model.Ofertante;

@ManagedBean
@ViewScoped
public class LoginBean {

	private String login = "", senha = "";

	public String autenticar() {
		FacesContext fc = FacesContext.getCurrentInstance();
		AdministradorDao admDao = new AdministradorDao();
		

		Administrador administrador = admDao.administradorPorCpf(this.login);
		try {
			if (administrador.getSenha().equals(this.senha)) {
				ExternalContext ec = fc.getExternalContext();
				HttpSession session = (HttpSession) ec.getSession(false);
				session.setAttribute("usuarioLogado", administrador);
				FacesMessage fm = new FacesMessage("Login efetuado com sucesso!");
				fm.setSeverity(FacesMessage.SEVERITY_INFO);
				fc.addMessage(null, fm);
				return "/restrito/adm/bv_adm";
			}else{
				FacesMessage fm = new FacesMessage("Login ou senha inválida");
				fm.setSeverity(FacesMessage.SEVERITY_ERROR);
				fc.addMessage(null, fm);
				return null;
			}

		} catch (NullPointerException e) {
			try {
				Ofertante ofertante = new OfertanteDao().consultarOfertante(this.login);
				if (ofertante.getSenha().equals(this.senha)) {
					ExternalContext ec = fc.getExternalContext();
					HttpSession session = (HttpSession) ec.getSession(false);
					session.setAttribute("usuarioLogado", ofertante);
					FacesMessage fm = new FacesMessage("Login efetuado com sucesso!");
					fm.setSeverity(FacesMessage.SEVERITY_INFO);
					fc.addMessage(null, fm);
					return "/restrito/ofertante/bv_ofe";
				}else{
					FacesMessage fm = new FacesMessage("Login ou senha inválida");
					fm.setSeverity(FacesMessage.SEVERITY_ERROR);
					fc.addMessage(null, fm);
					return null;
				}
			} catch (NullPointerException erro) {
				FacesMessage fm = new FacesMessage("Login ou senha inválida");
				fm.setSeverity(FacesMessage.SEVERITY_ERROR);
				fc.addMessage(null, fm);
				return null;
			}
		}
	}

	public String registraSaida() {
		FacesContext fc = FacesContext.getCurrentInstance();
		ExternalContext ec = fc.getExternalContext();
		HttpSession session = (HttpSession) ec.getSession(false);
		session.removeAttribute("usuarioLogado");
		session.invalidate();  
		return "/login.xhtml";
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

}
