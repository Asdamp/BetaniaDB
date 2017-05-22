package com.asdamp.betaniaDB;


import java.time.LocalDate;
import java.util.Date;

/**
 * Created by Antonio on 20/05/2017.
 */
public class Pagamento {
    private final String metodoPagamento;
    private final LocalDate data;
    private final double cifra;

    public String getModalitaPagamento() {
        return metodoPagamento;
    }

    public LocalDate getData() {
        return data;
    }

    public double getCifra() {
        return cifra;
    }

    public int getId() {
        return id;
    }

    private final int id;

    public Pagamento(int pagid, double cifra, LocalDate data, String metPag) {
        this.id=pagid;
        this.cifra=cifra;
        this.data=data;
        this.metodoPagamento=metPag;
    }
}
