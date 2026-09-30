package com.library.controllers;
import com.library.database.DBConnection;
import java.sql.Connection;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Alert;

public class MainController {

    @FXML
    private Button buttonAddBook;

    @FXML
    private TableView<?> tablebook;

    @FXML
    private TextField txtAutor;

    @FXML
    private TextField txtBookSearch;

    @FXML
    private TextField txtBookname;

    @FXML
    private TextField txtQuanity;

    @FXML
    private TextField txtisbn;

    public void initialize() {

        try {

            Connection connection = DBConnection.getConnection();

            if (connection != null) {

                System.out.println("Database холбогдсон!");

                Alert alert = new Alert(
                        AlertType.INFORMATION,
                        "холбогдсон байна"
                );

                alert.showAndWait();

            } else {

                System.out.println("Database холбогдсонгүй!");

                Alert alert = new Alert(
                        AlertType.ERROR,
                        "Database холбогдсонгүй"
                );

                alert.showAndWait();
            }

        } catch (SQLException e) {

            System.out.println("Database холболтын алдаа!");
            e.printStackTrace();

            Alert alert = new Alert(
                    AlertType.ERROR,
                    "Database холбогдоход алдаа гарлаа"
            );

            alert.showAndWait();
        }

        System.out.println("MainController initialize ажиллалаа");
    }

    @FXML
    void onAddBookclick(ActionEvent event) {

    }
}