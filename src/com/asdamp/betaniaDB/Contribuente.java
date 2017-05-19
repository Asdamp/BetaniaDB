package com.asdamp.betaniaDB;

/**
 * Created by anton on 17/05/2017.
 */
public class Contribuente {
    private String nome;
    private int id;
    private String via;
    private String civico;
    private String CAP;
    private String citta;
    private String provincia;
    private String telefono;
    private String cellulare;
    private boolean sostenitoreIntestatario;
    private String metodoPagamento;
    private String modalitaPagamento;
    private String email;

    public Contribuente(String nome, int id, String via, String civico, String CAP, String citta, String provincia, String telefono, String cellulare, boolean sostenitoreIntestatario, String metodoPagamento, String modalitaPagamento, String email) {
        this.nome = nome;
        this.id = id;
        this.via = via;
        this.civico = civico;
        this.CAP = CAP;
        this.citta = citta;
        this.provincia = provincia;
        this.telefono = telefono;
        this.cellulare = cellulare;
        this.sostenitoreIntestatario = sostenitoreIntestatario;
        this.metodoPagamento = metodoPagamento;
        this.modalitaPagamento = modalitaPagamento;
        this.email = email;
    }

    public String getNome() {

        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVia() {
        return via;
    }

    public void setVia(String via) {
        this.via = via;
    }

    public String getCivico() {
        return civico;
    }

    public void setCivico(String civico) {
        this.civico = civico;
    }

    public String getCAP() {
        return CAP;
    }

    public void setCAP(String CAP) {
        this.CAP = CAP;
    }

    public String getCitta() {
        return citta;
    }

    public void setCitta(String citta) {
        this.citta = citta;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCellulare() {
        return cellulare;
    }

    public void setCellulare(String cellulare) {
        this.cellulare = cellulare;
    }

    public boolean isSostenitoreIntestatario() {
        return sostenitoreIntestatario;
    }

    public void setSostenitoreIntestatario(boolean sostenitoreIntestatario) {
        this.sostenitoreIntestatario = sostenitoreIntestatario;
    }

    public String getMetodoPagamento() {
        return metodoPagamento;
    }

    public void setMetodoPagamento(String metodoPagamento) {
        this.metodoPagamento = metodoPagamento;
    }

    public String getModalitaPagamento() {
        return modalitaPagamento;
    }

    public void setModalitaPagamento(String modalitaPagamento) {
        this.modalitaPagamento = modalitaPagamento;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
