package controller.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public class Conexao {

    /*private final static String URL = "jdbc:mysql://127.7.149.130:3306/cbofertas";
    private final static String USUARIO = "adminqcWiBnU";
    private final static String SENHA = "7Iu7Arr62-Az";
    private final static String DRIVER ="com.mysql.jdbc.Driver";*/
    
    private final static String URL = "jdbc:mysql://127.0.0.1:3306/cbofertas";
    private final static String USUARIO = "root";
    private final static String SENHA = "614398";
    private final static String DRIVER ="com.mysql.jdbc.Driver";

    public static Connection factoryConexao() {
        Connection conexao = null;
        try {
            Class.forName(DRIVER);
            conexao = DriverManager.getConnection(URL, USUARIO, SENHA);
            return conexao;
        } catch (SQLException e1) {
            FacesContext fc = FacesContext.getCurrentInstance();
            FacesMessage fm = new FacesMessage("Falha na conexão com o "
                    + "banco de dados: "+e1.getMessage());
            fm.setSeverity(FacesMessage.SEVERITY_ERROR);
            fc.addMessage(null, fm);
            return conexao;
        }catch(ClassNotFoundException e2){
            FacesContext fc = FacesContext.getCurrentInstance();
            FacesMessage fm = new FacesMessage("Falha na conexão com o "
                    + "banco de dados: "+e2.getMessage());
            fm.setSeverity(FacesMessage.SEVERITY_ERROR);
            fc.addMessage(null, fm);
            return conexao;
        }
    }

}
