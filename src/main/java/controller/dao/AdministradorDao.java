package controller.dao;

import controller.jdbc.Conexao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import model.Administrador;

public class AdministradorDao {

	private Connection conexao;

	public Integer inserirAdministrador(Administrador adm) {
		String sql = "INSERT INTO administrador(pessoa_fis_id, senha) VALUES (?,?)";
		PreparedStatement stmt = null;
		this.conexao = Conexao.factoryConexao();

		PessoaFisicaDao pessoaFisDao = new PessoaFisicaDao();
		Integer pessoaFisId = pessoaFisDao.inserirPessoaFisica(adm.getPessoaFisica());

		if (pessoaFisId > 0) {

			try {
				stmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				stmt.setInt(1, pessoaFisId);
				stmt.setString(2, adm.getSenha());
				stmt.execute();
				ResultSet rs = stmt.getGeneratedKeys();
				Integer admId = 0;
				if (rs.next()) {
					admId = rs.getInt(1);
				}

				return admId;
			} catch (SQLException e1) {
				FacesContext fc = FacesContext.getCurrentInstance();
				FacesMessage fm = new FacesMessage("Falha na operação de inserção: " + e1.getMessage());
				fm.setSeverity(FacesMessage.SEVERITY_ERROR);
				fc.addMessage(null, fm);
				return 0;
			} catch (NullPointerException e2) {
				FacesContext fc = FacesContext.getCurrentInstance();
				FacesMessage fm = new FacesMessage("Falha na operação de inserção: " + e2.getMessage());
				fm.setSeverity(FacesMessage.SEVERITY_ERROR);
				fc.addMessage(null, fm);
				return 0;
			} finally {
				try {
					if (stmt != null) {
						stmt.close();
					}
					if (this.conexao != null) {
						this.conexao.close();
					}
				} catch (SQLException e3) {
					FacesContext fc = FacesContext.getCurrentInstance();
					FacesMessage fm = new FacesMessage(
							"Falha ao fechar " + "PreparedStatement/Conexão: " + e3.getMessage());
					fm.setSeverity(FacesMessage.SEVERITY_ERROR);
					fc.addMessage(null, fm);
				}
			}
		}
		return 0;
	}

	public Administrador administradorPorCpf(String cpf) {
		String sql = "SELECT administrador.* FROM administrador INNER JOIN pessoa_fis "
				+ "ON administrador.pessoa_fis_id=pessoa_fis.id " + "WHERE pessoa_fis.cpf=?;";
		PreparedStatement stmt = null;
		this.conexao = Conexao.factoryConexao();
		try {
			stmt = conexao.prepareStatement(sql);
			stmt.setString(1, cpf);
			ResultSet rs = stmt.executeQuery();
			Administrador adm = new Administrador();
			if (rs.next()) {
				adm.setId(rs.getInt(1));
				adm.getPessoaFisica().setId(rs.getInt(2));
				adm.setSenha(rs.getString(3));
			}
			return adm;
		} catch (SQLException e1) {
			FacesContext fc = FacesContext.getCurrentInstance();
			FacesMessage fm = new FacesMessage("Falha na operação de consulta: " + e1.getMessage());
			fm.setSeverity(FacesMessage.SEVERITY_ERROR);
			fc.addMessage(null, fm);
			return null;
		} catch (NullPointerException e2) {
			FacesContext fc = FacesContext.getCurrentInstance();
			FacesMessage fm = new FacesMessage("Falha na operação de consulta: " + e2.getMessage());
			fm.setSeverity(FacesMessage.SEVERITY_ERROR);
			fc.addMessage(null, fm);
			return null;
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
				if (this.conexao != null) {
					this.conexao.close();
				}
			} catch (SQLException e3) {
				FacesContext fc = FacesContext.getCurrentInstance();
				FacesMessage fm = new FacesMessage(
						"Falha ao fechar " + "PreparedStatement/Conexão: " + e3.getMessage());
				fm.setSeverity(FacesMessage.SEVERITY_ERROR);
				fc.addMessage(null, fm);
			}
		}
	}

	public boolean atualizarAdm(Administrador adm) {
		String sql = "UPDATE administrador INNER JOIN pessoa_fis " 
				+ "ON administrador.pessoa_fis_id = pessoa_fis.id "
				+ "SET administrador.senha = ?, pessoa_fis.cpf = ?, pessoa_fis.nome = ? " 
				+ "WHERE pessoa_fis.id = ?;";
		PreparedStatement stmt = null;
		this.conexao = Conexao.factoryConexao();
		try {
			stmt = conexao.prepareStatement(sql);
			stmt.setString(1, adm.getSenha());
			stmt.setString(2, adm.getPessoaFisica().getCpf());
			stmt.setString(3, adm.getPessoaFisica().getNome());
			stmt.setInt(4, adm.getPessoaFisica().getId());
			stmt.execute();
			return true;
		} catch (SQLException e1) {
			FacesContext fc = FacesContext.getCurrentInstance();
			FacesMessage fm = new FacesMessage("Falha na atualização: " + e1.getMessage());
			fm.setSeverity(FacesMessage.SEVERITY_ERROR);
			fc.addMessage(null, fm);
			return false;
		} catch (NullPointerException e2) {
			FacesContext fc = FacesContext.getCurrentInstance();
			FacesMessage fm = new FacesMessage("Falha na atualização: " + e2.getMessage());
			fm.setSeverity(FacesMessage.SEVERITY_ERROR);
			fc.addMessage(null, fm);
			return false;
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
				if (this.conexao != null) {
					this.conexao.close();
				}
			} catch (SQLException e3) {
				FacesContext fc = FacesContext.getCurrentInstance();
				FacesMessage fm = new FacesMessage(
						"Falha ao fechar " + "PreparedStatement/Conexão: " + e3.getMessage());
				fm.setSeverity(FacesMessage.SEVERITY_ERROR);
				fc.addMessage(null, fm);
			}
		}
	}
	
	public boolean excluirAdm(Administrador adm) {
		String sql = "DELETE administrador, pessoa_fis FROM administrador "
				+ "INNER JOIN pessoa_fis ON administrador.pessoa_fis_id = pessoa_fis.id "
				+ "WHERE pessoa_fis.cpf = ?;";
		PreparedStatement stmt = null;
		this.conexao = Conexao.factoryConexao();
		try {
			stmt = conexao.prepareStatement(sql);
			stmt.setString(1, adm.getPessoaFisica().getCpf());
			stmt.execute();
			return true;
		} catch (SQLException e1) {
			FacesContext fc = FacesContext.getCurrentInstance();
			FacesMessage fm = new FacesMessage("Falha na exclusão: " + e1.getMessage());
			fm.setSeverity(FacesMessage.SEVERITY_ERROR);
			fc.addMessage(null, fm);
			return false;
		} catch (NullPointerException e2) {
			FacesContext fc = FacesContext.getCurrentInstance();
			FacesMessage fm = new FacesMessage("Falha na exlusão: " + e2.getMessage());
			fm.setSeverity(FacesMessage.SEVERITY_ERROR);
			fc.addMessage(null, fm);
			return false;
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
				if (this.conexao != null) {
					this.conexao.close();
				}
			} catch (SQLException e3) {
				FacesContext fc = FacesContext.getCurrentInstance();
				FacesMessage fm = new FacesMessage(
						"Falha ao fechar " + "PreparedStatement/Conexão: " + e3.getMessage());
				fm.setSeverity(FacesMessage.SEVERITY_ERROR);
				fc.addMessage(null, fm);
			}
		}
	}
	

}
