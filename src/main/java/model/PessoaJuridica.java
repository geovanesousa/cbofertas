package model;

public class PessoaJuridica {

    private Integer id;
    private String cnpj;
    private String razaSocial;
    private String nomeFantasia;
    private Endereco endereco;

    public PessoaJuridica() {
        this.endereco = new Endereco();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getRazaSocial() {
        return razaSocial;
    }

    public void setRazaSocial(String razaSocial) {
        this.razaSocial = razaSocial;
    }

    public String getNomeFantasia() {
        return nomeFantasia;
    }

    public void setNomeFantasia(String nomeFantasia) {
        this.nomeFantasia = nomeFantasia;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

}
