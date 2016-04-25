package controller.dao;

import controller.jdbc.Conexao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import model.Endereco;

public class EnderecoDao {
    
    private Connection conexao;
    
    public Integer inserirEndereco(Endereco endereco) {
        String sql = "INSERT INTO endereco(cep, logradouro, numero, "
                + "complemento, bairro, cidade, uf) VALUES (?,?,?,?,?,?,?)";
        PreparedStatement stmt = null;
        this.conexao = Conexao.factoryConexao();

        try {
            stmt = conexao.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, endereco.getCep());
            stmt.setString(2, endereco.getLogradouro());
            stmt.setString(3, String.valueOf(endereco.getNumero()));
            stmt.setString(4, endereco.getComplemento());
            stmt.setString(5, endereco.getBairro());
            stmt.setString(6, endereco.getCidade());
            stmt.setString(7, endereco.getUf());
            stmt.execute();
            ResultSet rs = stmt.getGeneratedKeys();
            Integer enderecoId = 0;
            if (rs.next()) {
                enderecoId = rs.getInt(1);
            }
            
            return enderecoId;
        } catch (SQLException e1) {
            FacesContext fc = FacesContext.getCurrentInstance();
            FacesMessage fm = new FacesMessage("Falha na operação de inserção: "
                    + e1.getMessage());
            fm.setSeverity(FacesMessage.SEVERITY_ERROR);
            fc.addMessage(null, fm);
            return 0;
        } catch (NullPointerException e2) {
            FacesContext fc = FacesContext.getCurrentInstance();
            FacesMessage fm = new FacesMessage("Falha na operação de inserção: "
                    + e2.getMessage());
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
                FacesMessage fm = new FacesMessage("Falha ao fechar "
                        + "PreparedStatement/Conexão: " + e3.getMessage());
                fm.setSeverity(FacesMessage.SEVERITY_ERROR);
                fc.addMessage(null, fm);
            }
        }
    }
    
}
