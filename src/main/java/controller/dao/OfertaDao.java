package controller.dao;

import controller.jdbc.Conexao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import model.Oferta;
public class OfertaDao {

	private Connection conexao;

	public Integer inserirOferta(Oferta oferta) {
		String sql = "INSERT INTO oferta(nm_oferta, descricao, categoria, " + "preco, ofertante_id) VALUES (?,?,?,?,?)";
		PreparedStatement stmt = null;
		this.conexao = Conexao.factoryConexao();
		OfertanteDao ofertanteDao = new OfertanteDao();
		oferta.getOfertante()
				.setId(ofertanteDao.consultarIdOfertante(oferta.getOfertante().getPessoaJuridica().getCnpj()));

		if (oferta.getOfertante().getId() > 0) {

			try {
				stmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				stmt.setString(1, oferta.getNomeOferta());
				stmt.setString(2, oferta.getDescricao());
				stmt.setString(3, oferta.getCategoria());
				stmt.setDouble(4, oferta.getPreco());
				stmt.setString(5, String.valueOf(oferta.getOfertante().getId()));
				stmt.execute();
				ResultSet rs = stmt.getGeneratedKeys();
				Integer rOfertanteId = 0;
				if (rs.next()) {
					rOfertanteId = rs.getInt(1);
				}

				return rOfertanteId;
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
				} catch (SQLException e4) {
					FacesContext fc = FacesContext.getCurrentInstance();
					FacesMessage fm = new FacesMessage(
							"Falha ao fechar " + "PreparedStatement/Conexão: " + e4.getMessage());
					fm.setSeverity(FacesMessage.SEVERITY_ERROR);
					fc.addMessage(null, fm);
				}
			}
		} else {
			FacesContext fc = FacesContext.getCurrentInstance();
			FacesMessage fm = new FacesMessage("Ofertante não encontrado! " + "Confira o CNPJ.");
			fm.setSeverity(FacesMessage.SEVERITY_ERROR);
			fc.addMessage(null, fm);
			return 0;
		}

	}

	public List<Oferta> consultarOfertaPorBairro(String bairro) {
		String sql = "SELECT oferta.nm_oferta, oferta.descricao, oferta.preco, pessoa_jur.nm_fantasia,"
				+ " endereco.logradouro, endereco.numero, endereco.complemento,"
				+ " ofertante.telefone, ofertante.email, ofertante.rd_sociais FROM oferta " + "INNER JOIN ofertante "
				+ "ON oferta.ofertante_id=ofertante.id " + "INNER JOIN pessoa_jur "
				+ "ON ofertante.pessoa_jur_id=pessoa_jur.id " + "INNER JOIN endereco "
				+ "ON pessoa_jur.endereco_id=endereco.id " + "WHERE endereco.bairro=?";
		PreparedStatement stmt = null;
		this.conexao = Conexao.factoryConexao();
		try {
			stmt = conexao.prepareStatement(sql);
			stmt.setString(1, bairro);
			ResultSet rs = stmt.executeQuery();
			List<Oferta> lista = new ArrayList<Oferta>();
			while (rs.next()) {
				Oferta o = new Oferta();
				o.setNomeOferta(rs.getString(1));
				o.setDescricao(rs.getString(2));
				o.setPreco(rs.getDouble(3));
				o.getOfertante().getPessoaJuridica().setNomeFantasia(rs.getString(4));
				o.getOfertante().getPessoaJuridica().getEndereco().setLogradouro(rs.getString(5));
				o.getOfertante().getPessoaJuridica().getEndereco().setNumero(rs.getString(6));
				o.getOfertante().getPessoaJuridica().getEndereco().setComplemento(rs.getString(7));
				o.getOfertante().setTelefone(rs.getString(8));
				o.getOfertante().setEmail(rs.getString(9));
				o.getOfertante().setRedesSociais(rs.getString(10));
				lista.add(o);
			}
			return lista;
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
	
	public Oferta consultarOfertaPorId(Integer id){
		String sql = "SELECT * FROM oferta WHERE id=?";
		PreparedStatement stmt = null;
		this.conexao = Conexao.factoryConexao();
		try {
			stmt = conexao.prepareStatement(sql);
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();
			Oferta o = new Oferta();
			if(rs.next()) {
				o.setId(rs.getInt(1));
				o.setNomeOferta(rs.getString(2));
				o.setDescricao(rs.getString(3));
				o.setCategoria(rs.getString(4));
				o.setPreco(rs.getDouble(5));
			}
			return o;
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
	
	public List<Oferta> consultarOfertaCnpj(String cnpj) {
		String sql = "SELECT oferta.id, oferta.nm_oferta, oferta.preco "
				+ "FROM oferta INNER JOIN ofertante "
				+ "ON oferta.ofertante_id=ofertante.id "
				+ "INNER JOIN pessoa_jur "
				+ "ON ofertante.pessoa_jur_id=pessoa_jur.id "
				+ "WHERE pessoa_jur.cnpj=?";
		PreparedStatement stmt = null;
		this.conexao = Conexao.factoryConexao();
		try {
			stmt = conexao.prepareStatement(sql);
			stmt.setString(1, cnpj);
			ResultSet rs = stmt.executeQuery();
			List<Oferta> lista = new ArrayList<Oferta>();
			while (rs.next()) {
				Oferta o = new Oferta();
				o.setId(rs.getInt(1));
				o.setNomeOferta(rs.getString(2));
				o.setPreco(rs.getDouble(3));
				lista.add(o);
			}
			return lista;
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

	public Map<String, String> consultarCidadesPorUf(String uf) {
		String sql = "SELECT DISTINCT cidade FROM endereco " + "WHERE uf=?";
		PreparedStatement stmt = null;
		this.conexao = Conexao.factoryConexao();
		try {
			stmt = conexao.prepareStatement(sql);
			stmt.setString(1, uf);
			ResultSet rs = stmt.executeQuery();
			Map<String, String> cidades = new HashMap<String, String>();
			while (rs.next()) {
				cidades.put(rs.getString(1), rs.getString(1));
			}
			return cidades;
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

	public Map<String, String> consultarBairrosPorCidade(String cidade) {
		String sql = "SELECT DISTINCT bairro FROM endereco " + "WHERE cidade=?";
		PreparedStatement stmt = null;
		this.conexao = Conexao.factoryConexao();
		try {
			stmt = conexao.prepareStatement(sql);
			stmt.setString(1, cidade);
			ResultSet rs = stmt.executeQuery();
			Map<String, String> bairros = new HashMap<String, String>();
			while (rs.next()) {
				bairros.put(rs.getString(1), rs.getString(1));
			}
			return bairros;
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

	public boolean atualizarOferta(Oferta oferta) {
		String sql = "UPDATE oferta SET oferta.nm_oferta = ?, " + "oferta.descricao = ?, oferta.categoria = ?, "
				+ "oferta.preco = ? " + "WHERE oferta.id = ?;";
		PreparedStatement stmt = null;
		this.conexao = Conexao.factoryConexao();
		try {
			stmt = conexao.prepareStatement(sql);
			stmt.setString(1, oferta.getNomeOferta());
			stmt.setString(2, oferta.getDescricao());
			stmt.setString(3, oferta.getCategoria());
			stmt.setDouble(4, oferta.getPreco());
			stmt.setInt(5, oferta.getId());
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

	public boolean excluirOferta(Oferta oferta) {
		String sql = "DELETE FROM oferta WHERE id = ?;";
		PreparedStatement stmt = null;
		this.conexao = Conexao.factoryConexao();
		try {
			stmt = conexao.prepareStatement(sql);
			stmt.setInt(1, oferta.getId());
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
	
	public boolean excluirOferta(Integer id) {
		String sql = "DELETE FROM oferta WHERE id = ?;";
		PreparedStatement stmt = null;
		this.conexao = Conexao.factoryConexao();
		try {
			stmt = conexao.prepareStatement(sql);
			stmt.setInt(1, id);
			stmt.execute();
			FacesContext fc = FacesContext.getCurrentInstance();
			FacesMessage fm = new FacesMessage("Excluída com sucesso!");
			fm.setSeverity(FacesMessage.SEVERITY_INFO);
			fc.addMessage(null, fm);
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
