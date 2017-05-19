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
import javafx.stage.Stage;


import java.awt.*;
import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

public class Controller {
    Statement st;
    @FXML
    public void initialize() {
        ObservableList<Contribuente> wordsList = FXCollections.observableArrayList();
        addAllContribuenti(wordsList);
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
    private GridPane latodestro;

    @FXML
    private Button confermaPagamento;

    @FXML
    private DatePicker data;

    @FXML
    private ComboBox<String> modalitaPagamentoIns;



    @FXML
    private TextField importoIns;
    @FXML
    private MenuItem addContribuente;
    @FXML
    private GridLayout latoDestro;
    @FXML
    private ListView<Contribuente> listaContribuenti;
    @FXML
    void onClickConfermaPagamento(ActionEvent event){
        LocalDate d=data.getValue();
        String modpag=modalitaPagamentoIns.getSelectionModel().getSelectedItem();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("YYYY-MM-dd");
        String formattedString = d.format(formatter);
        Contribuente c=listaContribuenti.getSelectionModel().getSelectedItem();
        try {
            Double val=Double.parseDouble(importoIns.getText());
            st.executeUpdate("Insert into Pagamenti(Cifra,Data,MetodoPagamento,IdPersona) VALUES ('"+val+"','"+formattedString+"','"+modpag+"','"+c.getId()+"');");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Pagamento inserito");
            alert.setHeaderText(null);
            alert.setContentText("Il Pagamento è stato inserito con successo!");

            alert.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Fallito l'inserimento del pagamento");
            alert.setHeaderText("Il pagamento non è stato inseirto. maggiori dettagli in basso");
            alert.setContentText(Arrays.toString(e.getStackTrace()));

            alert.showAndWait();
        }

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

    }
    private void addAllContribuenti(ObservableList<Contribuente> wordsList) {
        Connection c = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:db/pbetaniadb.sqlite");
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        try {
            st=c.createStatement();
            ResultSet res=st.executeQuery("Select * from Persona");
            while(res.next()){
                String nome=res.getString("nome");
                int id=res.getInt("id");
                String via=res.getString("via");
                String civico=res.getString("civico");
                String CAP=res.getString("CAP");
                String citta=res.getString("citta");
                String provincia=res.getString("provincia");
                String telefono=res.getString("telefono");
                String cellulare=res.getString("cellulare");
                boolean sostenitoreIntestatario=res.getBoolean("sostenitoreIntestatario");
                String metodoPagamneto=res.getString("metodoPagamento");
                String modalitapagamento=res.getString("modalitàPagamento");
                String email=res.getString("email");
                wordsList.add(new Contribuente(nome,id,via,civico,CAP,citta,provincia,telefono,cellulare,sostenitoreIntestatario,metodoPagamneto,modalitapagamento,email));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
