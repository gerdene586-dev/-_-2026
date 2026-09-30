package com.library.controllers;

import com.library.database.DBConnection;
import com.library.models.Book;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class MainController {

    @FXML
    private Button buttonAddBook;

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

    @FXML
    private TableView<Book> tableBook;

    @FXML
    private TableColumn<Book, Integer> colId;

    @FXML
    private TableColumn<Book, String> colTitle;

    @FXML
    private TableColumn<Book, String> colAuthor;

    @FXML
    private TableColumn<Book, String> colIsbn;

    @FXML
    private TableColumn<Book, Integer> colQuantity;

    @FXML
    private TableColumn<Book, Integer> colAv;

    private ObservableList<Book> bookList =
            FXCollections.observableArrayList();

    public void initialize() {

        colId.setCellValueFactory(
                new PropertyValueFactory<>("id")
        );

        colTitle.setCellValueFactory(
                new PropertyValueFactory<>("title")
        );

        colAuthor.setCellValueFactory(
                new PropertyValueFactory<>("author")
        );

        colIsbn.setCellValueFactory(
                new PropertyValueFactory<>("isbn")
        );

        colQuantity.setCellValueFactory(
                new PropertyValueFactory<>("quantity")
        );

        colAv.setCellValueFactory(
                new PropertyValueFactory<>("availableQty")
        );

        try {

            Connection connection = DBConnection.getConnection();

            if (connection != null) {

                System.out.println("Database холбогдсон!");

                Alert alert = new Alert(
                        Alert.AlertType.INFORMATION,
                        "Database холбогдсон байна"
                );

                alert.showAndWait();

            } else {

                System.out.println("Database холбогдсонгүй!");

            }

        } catch (SQLException e) {

            System.out.println("Database холболтын алдаа!");
            e.printStackTrace();

        }

        System.out.println("MainController initialize ажиллалаа");

        loadBooksFromDatabase();
    }

    private void loadBooksFromDatabase() {

        bookList.clear();

        String query = "SELECT * FROM book";

        try (
                Connection conn = DBConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)
        ) {

            while (rs.next()) {

                bookList.add(
                        new Book(
                                rs.getInt("book_id"),
                                rs.getString("title"),
                                rs.getString("author"),
                                rs.getString("isbn"),
                                rs.getInt("quantity"),
                                rs.getInt("available_qty")
                        )
                );
            }

            tableBook.setItems(bookList);

            System.out.println(
                    "Ном амжилттай уншигдлаа. Номын тоо: "
                    + bookList.size()
            );

        } catch (SQLException e) {

            e.printStackTrace();

        }
    }

    @FXML
    void onAddBookclick(ActionEvent event) {

    }
}