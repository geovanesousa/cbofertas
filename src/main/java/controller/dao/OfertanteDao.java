package controller.dao;

import controller.jdbc.Conexao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import model.Ofertante;

public class OfertanteDao {

	private Connection conexao;

	public Integer inserirOfertante(Ofertante ofertante) {
		String sql = "INSERT INTO ofertante(pessoa_jur_id, telefone, email, " + "rd_sociais, senha) VALUES (?,?,?,?,?)";
		PreparedStatement stmt = null;
		this.conexao = Conexao.factoryConexao();

		PessoaJuridicaDao pessoaJurDao = new PessoaJuridicaDao();
		Integer pessoaJurId = pessoaJurDao.inserirPessoaJuridica(ofertante.getPessoaJuridica());

		if (pessoaJurId > 0) {

			try {
				stmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				stmt.setString(1, String.valueOf(pessoaJurId));
				stmt.setString(2, ofertante.getTelefone());
				stmt.setString(3, ofertante.getEmail());
				stmt.setString(4, ofertante.getRedesSociais());
				stmt.setString(5, ofertante.getSenha());
				stmt.execute();
				ResultSet rs = stmt.getGeneratedKeys();
				Integer ofertanteId = 0;
				if (rs.next()) {
					ofertanteId = rs.getInt(1);
				}

				return ofertanteId;
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

	public Integer consultarIdOfertante(String cnpj) {
		String sql = "SELECT ofertante.id FROM ofertante INNER JOIN pessoa_jur "
				+ "ON ofertante.pessoa_jur_id=pessoa_jur.id " + "WHERE pessoa_jur.cnpj=?;";
		PreparedStatement stmt = null;
		this.conexao = Conexao.factoryConexao();
		try {
			stmt = conexao.prepareStatement(sql);
			stmt.setString(1, cnpj);
			ResultSet rs = stmt.executeQuery();
			Integer ofertanteId = 0;
			if (rs.next()) {
				ofertanteId = rs.getInt(1);
			}
			return ofertanteId;
		} catch (SQLException e1) {
			FacesContext fc = FacesContext.getCurrentInstance();
			FacesMessage fm = new FacesMessage("Falha na operação de consulta: " + e1.getMessage());
			fm.setSeverity(FacesMessage.SEVERITY_ERROR);
			fc.addMessage(null, fm);
			return 0;
		} catch (NullPointerException e2) {
			FacesContext fc = FacesContext.getCurrentInstance();
			FacesMessage fm = new FacesMessage("Falha na operação de consulta: " + e2.getMessage());
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

	public Ofertante consultarOfertante(String cnpj) {
		String sql = "SELECT ofertante.*, pessoa_jur.*, endereco.* " + "FROM ofertante INNER JOIN pessoa_jur "
				+ "ON ofertante.pessoa_jur_id=pessoa_jur.id " + "INNER JOIN endereco "
				+ "ON endereco.id=pessoa_jur.endereco_id WHERE pessoa_jur.cnpj=?;";
		PreparedStatement stmt = null;
		this.conexao = Conexao.factoryConexao();
		try {
			stmt = conexao.prepareStatement(sql);
			stmt.setString(1, cnpj);
			ResultSet rs = stmt.executeQuery();
			Ofertante of = new Ofertante();
			if (rs.next()) {
				of.setId(rs.getInt(1));
				of.setTelefone(rs.getString(3));
				of.setEmail(rs.getString(4));
				of.setRedesSociais(rs.getString(5));
				of.setSenha(rs.getString(6));
				of.getPessoaJuridica().setId(rs.getInt(7));
				of.getPessoaJuridica().setCnpj(rs.getString(8));
				of.getPessoaJuridica().setRazaSocial(rs.getString(9));
				of.getPessoaJuridica().setNomeFantasia(rs.getString(10));
				of.getPessoaJuridica().getEndereco().setId(rs.getInt(12));
				of.getPessoaJuridica().getEndereco().setCep(rs.getString(13));
				of.getPessoaJuridica().getEndereco().setLogradouro(rs.getString(14));
				of.getPessoaJuridica().getEndereco().setNumero(rs.getString(15));
				of.getPessoaJuridica().getEndereco().setComplemento(rs.getString(16));
				of.getPessoaJuridica().getEndereco().setBairro(rs.getString(17));
				of.getPessoaJuridica().getEndereco().setCidade(rs.getString(18));
				of.getPessoaJuridica().getEndereco().setUf(rs.getString(19));
			}
			return of;
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

	public Ofertante nomeFantasiaDoOfertante(String cnpj) {
		String sql = "SELECT cnpj, rz_social, nm_fantasia FROM pessoa_jur WHERE cnpj=?;";
		PreparedStatement stmt = null;
		this.conexao = Conexao.factoryConexao();
		try {
			stmt = conexao.prepareStatement(sql);
			stmt.setString(1, cnpj);
			ResultSet rs = stmt.executeQuery();
			Ofertante of = new Ofertante();
			if (rs.next()) {
				of.getPessoaJuridica().setCnpj(rs.getString(1));
				of.getPessoaJuridica().setRazaSocial(rs.getString(2));
				of.getPessoaJuridica().setNomeFantasia(rs.getString(3));
			}
			return of;
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

	public boolean atualizarOfertante(Ofertante ofe) {
		String sql = "UPDATE ofertante INNER JOIN pessoa_jur " + "ON ofertante.pessoa_jur_id = pessoa_jur.id "
				+ "INNER JOIN endereco " + "ON endereco.id = pessoa_jur.endereco_id "
				+ "SET ofertante.telefone = ?, ofertante.email = ?, ofertante.rd_sociais = ?, "
				+ "ofertante.senha = ?, pessoa_jur.cnpj = ?, pessoa_jur.rz_social = ?, "
				+ "pessoa_jur.nm_fantasia = ?, endereco.cep = ?, endereco.logradouro = ?, "
				+ "endereco.numero = ?, endereco.complemento = ?, endereco.bairro = ?, "
				+ "endereco.cidade = ?, endereco.uf = ? " + "WHERE pessoa_jur.id = ?;";
		PreparedStatement stmt = null;
		this.conexao = Conexao.factoryConexao();
		try {
			stmt = conexao.prepareStatement(sql);
			stmt.setString(1, ofe.getTelefone());
			stmt.setString(2, ofe.getEmail());
			stmt.setString(3, ofe.getRedesSociais());
			stmt.setString(4, ofe.getSenha());
			stmt.setString(5, ofe.getPessoaJuridica().getCnpj());
			stmt.setString(6, ofe.getPessoaJuridica().getRazaSocial());
			stmt.setString(7, ofe.getPessoaJuridica().getNomeFantasia());
			stmt.setString(8, ofe.getPessoaJuridica().getEndereco().getCep());
			stmt.setString(9, ofe.getPessoaJuridica().getEndereco().getLogradouro());
			stmt.setString(10, ofe.getPessoaJuridica().getEndereco().getNumero());
			stmt.setString(11, ofe.getPessoaJuridica().getEndereco().getComplemento());
			stmt.setString(12, ofe.getPessoaJuridica().getEndereco().getBairro());
			stmt.setString(13, ofe.getPessoaJuridica().getEndereco().getCidade());
			stmt.setString(14, ofe.getPessoaJuridica().getEndereco().getUf());
			stmt.setInt(15, ofe.getPessoaJuridica().getId());
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

	public boolean excluirOfertante(Ofertante ofe) {
		
		String sql = "DELETE endereco FROM endereco INNER JOIN pessoa_jur "
				+ "ON endereco.id = pessoa_jur.endereco_id "
				+ "WHERE pessoa_jur.cnpj = ?;";
		PreparedStatement stmt = null;
		this.conexao = Conexao.factoryConexao();
		try {
			stmt = conexao.prepareStatement(sql);
			stmt.setString(1, ofe.getPessoaJuridica().getCnpj());
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
