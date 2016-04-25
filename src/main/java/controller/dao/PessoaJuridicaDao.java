package controller.dao;

import controller.jdbc.Conexao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import model.PessoaJuridica;

public class PessoaJuridicaDao {

    private Connection conexao;

    public Integer inserirPessoaJuridica(PessoaJuridica pessoaJuridica) {

        String sql = "INSERT INTO pessoa_jur(cnpj, rz_social, nm_fantasia, "
                + "endereco_id) VALUES (?,?,?,?)";
        PreparedStatement stmt = null;
        this.conexao = Conexao.factoryConexao();

        EnderecoDao enderecoDao = new EnderecoDao();
        Integer enderecoId = enderecoDao.inserirEndereco(
                pessoaJuridica.getEndereco());
        if (enderecoId > 0) {

            try {
                stmt = conexao.prepareStatement(sql,
                        Statement.RETURN_GENERATED_KEYS);
                stmt.setString(1, pessoaJuridica.getCnpj());
                stmt.setString(2, pessoaJuridica.getRazaSocial());
                stmt.setString(3, pessoaJuridica.getNomeFantasia());
                stmt.setString(4, String.valueOf(enderecoId));
                stmt.execute();
                ResultSet rs = stmt.getGeneratedKeys();
                Integer pessoaJurId = 0;
                if (rs.next()) {
                    pessoaJurId = rs.getInt(1);
                }

                return pessoaJurId;
            } catch (SQLException e1) {
                FacesContext fc = FacesContext.getCurrentInstance();
                FacesMessage fm = new FacesMessage(
                        "Falha na operação de inserção: "
                        + e1.getMessage());
                fm.setSeverity(FacesMessage.SEVERITY_ERROR);
                fc.addMessage(null, fm);
                return 0;
            } catch (NullPointerException e2) {
                FacesContext fc = FacesContext.getCurrentInstance();
                FacesMessage fm = new FacesMessage(
                        "Falha na operação de inserção: "
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
        return 0;
    }
}
