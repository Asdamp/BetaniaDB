package com.asdamp.betaniaDB;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class UtenteController {

    @FXML
    private TextField nome;

    @FXML
    private TextField via;

    @FXML
    private TextField civico;

    @FXML
    private TextField cap;

    @FXML
    private TextField citta;

    @FXML
    private TextField provincia;

    @FXML
    private TextField telefono;

    @FXML
    private TextField cellulare;

    @FXML
    private TextField email;

    @FXML
    private CheckBox insestatario;

    @FXML
    private ComboBox<?> periodicita;

    @FXML
    private ComboBox<?> modalita;

    @FXML
    private Button conferma;


    @FXML
    void Salva(ActionEvent event) {

    }

}
