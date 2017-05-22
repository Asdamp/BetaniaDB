package com.asdamp.betaniaDB;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;


import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Controller {

    @FXML
    private GridPane latodestro;

    @FXML
    private Button confermaPagamento;

    @FXML
    private DatePicker data;

    @FXML
    private ComboBox<String> modalitaPagamentoIns;
    @FXML
    private MenuItem modificaPersonaButton;

    @FXML
    private MenuItem eliminaPersonaButton;
    @FXML
    private MenuItem eliminaPagamentoButton;
    @FXML
    private Text nomeTxt;

    @FXML
    private Text viaTxt;

    @FXML
    private Text emailTxt;

    @FXML
    private Text telefonoTxt;

    @FXML
    private Text cellulareTxt;

    @FXML
    private CheckBox sostenitoreTxt;

    @FXML
    private Text modalitaTxt;
    @FXML
    private Text pagatoTxt;
    @FXML
    private Text periodicitaTxt;
    @FXML
    private TextField importoIns;
    @FXML
    private MenuItem addContribuente;
    @FXML
    private GridLayout latoDestro;
    @FXML
    private ListView<Contribuente> listaContribuenti;
    @FXML
    private ListView<Pagamento> listaPagamenti;
    @FXML
    public void initialize() {
        ObservableList<Contribuente> wordsList = FXCollections.observableArrayList();
        try {
            wordsList.addAll(DBManager.getInstance().getAllContribuenti());
        } catch (SQLException e) {
            e.printStackTrace();
            errorMsg("Db non trovato", "Impossibile aprire il db. verificare che nella cartella db esista il file pbetaniadb.sqlite");
        } catch (ParseException e) {
            e.printStackTrace();
            errorMsg("Db non trovato", "Errore nel parsing della data. Il db potrebbe essere parzialmente corrotto");

        }
        listaContribuenti.setItems(wordsList);
        listaContribuenti.setCellFactory(param -> new ListCell<Contribuente>() {
            @Override
            protected void updateItem(Contribuente item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null || item.getNome() == null) {
                    setText(null);
                } else {
                    setText(item.getNome());
                }
            }
        });
        ObservableList<String> modalita=FXCollections.observableArrayList();
        modalita.addAll("Conto corrente","Bonifico bancario","Rimessa diretta");
        modalitaPagamentoIns.getItems().addAll(modalita);
    }
    @FXML

    void onClickConfermaPagamento(ActionEvent event){
        LocalDate d=data.getValue();
        String modpag=modalitaPagamentoIns.getSelectionModel().getSelectedItem();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("YYYY-MM-dd");
        String formattedString = d.format(formatter);
        Contribuente c=listaContribuenti.getSelectionModel().getSelectedItem();

        try {
            Double val=Double.parseDouble(importoIns.getText());
            int newId=DBManager.getInstance().addPagamento(c.getId(),val,formattedString,modpag);
            Pagamento p=new Pagamento(newId,val,d,modpag);
            c.getPagamenti().add(p);
            listaPagamenti.getItems().add(p);

            infoMsg("Pagamento inserito","il pagamento è stato inserito con successo per l'utente "+ c.getNome());
        } catch (NumberFormatException e) {
            e.printStackTrace();
            errorMsg("Errore nell'inserimento","Il campo cifra può contenere solo numeri.\nSi prega di usare il . come separatore decimale");
        } catch (SQLException e) {
            e.printStackTrace();
            errorMsg("Errore nell'inserimento","Alcuni campi non sono stati compilati correttamente");
        }

    }

    private void infoMsg(String title,String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);

        alert.showAndWait();
    }

    private void errorMsg(String title,String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);

        alert.setContentText(msg);

        alert.showAndWait();
    }
    @FXML
    void emailATutti(ActionEvent event) {
        String myString = "";
        for(Contribuente c:listaContribuenti.getItems()){
            if(c.getEmail()!=null)
                myString=myString+c.getEmail()+";";
        }
        String toCopy=myString.substring(0,myString.length()-1);
        StringSelection stringSelection = new StringSelection(toCopy);

        Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
        clpbrd.setContents(stringSelection, null);
        infoMsg("Email copiate nella clipboard","Incolla nel tuo client di posta elettronica per avere tutti gli indirizzi mail");
    }

    @FXML
    void indirizziNoMAil(ActionEvent event) {
        String myString = "";
        for(Contribuente c:listaContribuenti.getItems()){
            if(c.getEmail()==null)
                myString=myString+c.getVia()+" "+c.getCivico()+", "+c.getCitta()+", "+c.getCAP()+", "+c.getProvincia()+"\n\n";
        }
        String toCopy=myString.substring(0,myString.length()-1);
        StringSelection stringSelection = new StringSelection(toCopy);

        Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
        clpbrd.setContents(stringSelection, null);
        infoMsg("Indirizzi copiati nella clipboard","Incolla ovunque tu voglia per avere la lista di tutti gli indirizzi delle persone che non hanno un indirizzo mail");
    }
    @FXML
    void indirizziTutti(ActionEvent event) {
        String myString = "";
        for(Contribuente c:listaContribuenti.getItems()){
            myString=myString+c.getVia()+" "+c.getCivico()+", "+c.getCitta()+", "+c.getCAP()+", "+c.getProvincia()+"\n\n";
        }
        String toCopy=myString.substring(0,myString.length()-1);
        StringSelection stringSelection = new StringSelection(toCopy);

        Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
        clpbrd.setContents(stringSelection, null);
        infoMsg(")Indirizzi copiati nella clipboard","Incolla nel tuo client di posta elettronica per avere tutti gli indirizzi");
    }
    @FXML
    void onClickAddContribuente(ActionEvent event) {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("add_contribuente.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage addContribuenteStage=new Stage();
        addContribuenteStage.setTitle("Aggiungi contribuente");
        addContribuenteStage.setScene(new Scene(root, 300, 275));
        addContribuenteStage.show();
    }

    @FXML
    void onClickContribuente(MouseEvent event) {
        Contribuente c=listaContribuenti.getSelectionModel().getSelectedItem();
        nomeTxt.setText(c.getNome());
        if(c.getTelefono()!=null)
            telefonoTxt.setText("Telefono: "+c.getTelefono());
        if(c.getEmail()!=null)
            emailTxt.setText("Email: "+c.getEmail());
        sostenitoreTxt.setSelected(c.isSostenitoreIntestatario());
        if(c.getCellulare()!=null)
            cellulareTxt.setText("Cellulare: "+c.getCellulare());
        viaTxt.setText(c.getVia()+" "+c.getCivico()+", "+c.getCitta()+", "+c.getCAP()+", "+c.getProvincia());
        modalitaTxt.setText(c.getModalitaPagamento());
        periodicitaTxt.setText(c.getMetodoPagamento());

        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy").withLocale(Locale.ITALY);


        ObservableList<Pagamento> pag = FXCollections.observableArrayList();
        pag.addAll(c.getPagamenti());

        listaPagamenti.setItems(pag);
        listaPagamenti.setCellFactory(param -> new ListCell<Pagamento>() {
            @Override
            protected void updateItem(Pagamento item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null ) {
                    setText(null);
                } else {

                    setText(item.getModalitaPagamento()+": "+item.getCifra()+"€ \t "+item.getData().format(formatter));
                }
            }
        });
        double tot=0;
        for(Pagamento p:listaPagamenti.getItems()){
            tot+=p.getCifra();
        }
        pagatoTxt.setText(String.valueOf(tot));


    }

    @FXML
    void eliminaPagamento(ActionEvent event) {
        Pagamento daElim=listaPagamenti.getSelectionModel().getSelectedItem();
        try {
            DBManager.getInstance().eliminaPagamento(daElim);
            listaPagamenti.getItems().remove(daElim);
            infoMsg("Pegamento eliminato","PAgamento eliminato correttamente");
        } catch (SQLException e) {
            errorMsg("Impossibile eliminare il pagamento","Si è verificato un erroe durante l'eliminazione del pagamento");
        }
    }

    @FXML
    void eliminaPersona(ActionEvent event) {
        Contribuente daElim=listaContribuenti.getSelectionModel().getSelectedItem();
        try {
            DBManager.getInstance().eliminaContribuente(daElim);
            listaContribuenti.getItems().remove(daElim);
            infoMsg("COntribuente eliminato","Contribuente eliminato con successo");
        } catch (SQLException e) {
            errorMsg("Impossibile eliminare il contribuente","Si è verificato un errore durante l'eliminazione del contribuente");
        }
    }

    @FXML
    void modificaPersona(ActionEvent event) {

    }


}
