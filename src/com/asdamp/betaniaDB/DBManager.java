package com.asdamp.betaniaDB;

import java.sql.*;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Created by Antonio on 20/05/2017.
 */
public class DBManager {
    private Connection c;
    private ArrayList<Contribuente> listcompleta=new ArrayList<>();
    private static DBManager ourInstance = new DBManager();

    public static DBManager getInstance() {
        return ourInstance;
    }

    private DBManager() {

        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:db/pbetaniadb.sqlite");

        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }

    }

    public ArrayList<Contribuente> getListcompleta() {
        return listcompleta;
    }

    public ArrayList<Contribuente> getAllContribuenti() throws SQLException, ParseException {
        listcompleta.clear();
        ResultSet res = c.createStatement().executeQuery("Select * from Persona");
        while (res.next()) {
            String nome = res.getString("nome");
            int id = res.getInt("id");
            String via = res.getString("via");
            String civico = res.getString("civico");
            String CAP = res.getString("CAP");
            String citta = res.getString("citta");
            String provincia = res.getString("provincia");
            String telefono = res.getString("telefono");
            String cellulare = res.getString("cellulare");
            boolean sostenitoreIntestatario = res.getBoolean("sostenitoreIntestatario");
            String metodoPagamneto = res.getString("metodoPagamento");
            String modalitapagamento = res.getString("modalitàPagamento");
            String email = res.getString("email");
            ArrayList<Pagamento> pagamenti=getPagamenti(id);
            listcompleta.add(new Contribuente(nome, id, via, civico, CAP, citta, provincia, telefono, cellulare, sostenitoreIntestatario, metodoPagamneto, modalitapagamento, email,pagamenti));
        }

        return listcompleta;
    }

    private ArrayList<Pagamento> getPagamenti(int id) throws SQLException, ParseException {
        ArrayList<Pagamento> pagamenti=new ArrayList<>();
        ResultSet res=c.createStatement().executeQuery("Select * from Pagamenti where IdPersona="+id+" ORDER by Data");
        while (res.next()){
            double cifra= res.getDouble("cifra");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            formatter = formatter.withLocale( Locale.ITALY );  // Locale specifies human language for translating, and cultural norms for lowercase/uppercase and abbreviations and such. Example: Locale.US or Locale.CANADA_FRENCH
            LocalDate data = LocalDate.parse(res.getString("data"), formatter);

            int pagid=res.getInt("idPagamento");
            String modPag=res.getString("metodoPagamento");

            pagamenti.add(new Pagamento(pagid,cifra,data,modPag));
        }
        return pagamenti;
    }

    public int addPagamento(int persId, Double val, String formattedString, String modpag) throws SQLException {
        PreparedStatement st= c.prepareStatement("Insert into Pagamenti(Cifra,Data,MetodoPagamento,IdPersona) VALUES ('"+val+"','"+formattedString+"','"+modpag+"','"+persId+"');",Statement.RETURN_GENERATED_KEYS);
        st.executeUpdate();
        try (ResultSet generatedKeys = st.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                return (int) generatedKeys.getLong(1);
            }
            else {
                throw new SQLException("Creating user failed, no ID obtained.");
            }
        }
    }

    public void eliminaPagamento(Pagamento daElim) throws SQLException {
        Statement st=c.createStatement();
        st.executeUpdate("DELETE from Pagamenti where IdPagamento ="+daElim.getId());
    }

    public void eliminaContribuente(Contribuente daElim) throws SQLException {
        for(Pagamento p:daElim.getPagamenti()){
            eliminaPagamento(p);
        }
        Statement st=c.createStatement();
        st.executeUpdate("DELETE from Persona where Id ="+daElim.getId());
    }
}
