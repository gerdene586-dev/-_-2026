package com.library.controllers;

import com.library.database.DBConnection;
import com.library.models.Book;
import com.library.models.Member;
import com.library.models.Rental;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.geometry.Insets;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private TextField txtBookname;

    @FXML
    private TextField txtAutor;

    @FXML
    private TextField txtisbn;

    @FXML
    private TextField txtQuanity;

    @FXML
    private TextField txtBookSearch;

    @FXML
    private Button buttonAddBook;

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

    @FXML
    private TextField txtMemberSurname;

    @FXML
    private TextField txtMemberName;

    @FXML
    private TextField txtMemberPhone;

    @FXML
    private TextField txtMemberEmail;

    @FXML
    private TableView<Rental> tableRental;

    @FXML
    private TableColumn<Rental, Integer> colRentalId;

    @FXML
    private TableColumn<Rental, String> colMemberName;

    @FXML
    private TableColumn<Rental, String> colBookTitle;

    @FXML
    private TableColumn<Rental, String> colBorrowDate;

    @FXML
    private TableColumn<Rental, String> colDueDate;

    @FXML
    private TableColumn<Rental, String> colReturnDate;

    @FXML
    private TableColumn<Rental, String> colStatus;

    @FXML
    private RadioButton radioAll;

    @FXML
    private RadioButton radioOverdue;

    private final ObservableList<Book> bookList =
            FXCollections.observableArrayList();

    private final ObservableList<Rental> rentalList =
            FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        setupBookTable();
        setupRentalTable();

        loadBooks();
        loadRentals();

        setupBookSearch();
        setupRentalFilter();

        System.out.println("MainController ажиллаж байна.");
    }

    private void setupBookTable() {

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

        tableBook.setItems(bookList);
    }

    private void loadBooks() {

        bookList.clear();

        String sql =
                "SELECT id_ном, ном_гарчиг, зохиогч, ISBN, тоо, available_qty " +
                "FROM `ном` ORDER BY id_ном";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {

                int id = rs.getInt("id_ном");

                String title = rs.getString("ном_гарчиг");

                String author = rs.getString("зохиогч");

                String isbn = rs.getString("ISBN");

                int quantity = rs.getInt("тоо");

                int availableQty = rs.getInt("available_qty");

                Book book = new Book(
                        id,
                        title,
                        author,
                        isbn,
                        quantity,
                        availableQty
                );

                bookList.add(book);
            }

            System.out.println(
                    "Ном амжилттай уншигдлаа. Номын тоо: "
                    + bookList.size()
            );

        } catch (SQLException e) {

            showError(
                    "Ном унших үед алдаа гарлаа",
                    e.getMessage()
            );

            e.printStackTrace();
        }
    }

    @FXML
    private void onAddBookclick() {

        String title = txtBookname.getText().trim();

        String author = txtAutor.getText().trim();

        String isbn = txtisbn.getText().trim();

        String quantityText = txtQuanity.getText().trim();

        if (
                title.isEmpty() ||
                author.isEmpty() ||
                quantityText.isEmpty()
        ) {

            showWarning(
                    "Мэдээлэл дутуу",
                    "Номын нэр, зохиогч, тоог заавал оруулна уу."
            );

            return;
        }

        int quantity;

        try {

            quantity = Integer.parseInt(quantityText);

        } catch (NumberFormatException e) {

            showWarning(
                    "Тоо буруу",
                    "Номын тоо бүхэл тоо байх ёстой."
            );

            return;
        }

        if (quantity <= 0) {

            showWarning(
                    "Тоо буруу",
                    "Номын тоо 0-ээс их байх ёстой."
            );

            return;
        }

        String sql =
                "INSERT INTO `ном` " +
                "(ном_гарчиг, зохиогч, ISBN, тоо, available_qty) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps =
                        conn.prepareStatement(sql)
        ) {

            ps.setString(1, title);
            ps.setString(2, author);

            if (isbn.isEmpty()) {
                ps.setNull(3, Types.VARCHAR);
            } else {
                ps.setString(3, isbn);
            }

            ps.setInt(4, quantity);
            ps.setInt(5, quantity);

            ps.executeUpdate();

            showInfo(
                    "Амжилттай",
                    "Ном амжилттай нэмэгдлээ."
            );

            txtBookname.clear();
            txtAutor.clear();
            txtisbn.clear();
            txtQuanity.clear();

            loadBooks();

        } catch (SQLIntegrityConstraintViolationException e) {

            showWarning(
                    "ISBN давхардсан",
                    "Энэ ISBN-тэй ном аль хэдийн бүртгэгдсэн байна."
            );

        } catch (SQLException e) {

            showError(
                    "Ном нэмэх үед алдаа гарлаа",
                    e.getMessage()
            );

            e.printStackTrace();
        }
    }

    private void setupBookSearch() {

        txtBookSearch.textProperty().addListener(
                (observable, oldValue, newValue) -> {

                    String search = newValue
                            .toLowerCase()
                            .trim();

                    if (search.isEmpty()) {

                        tableBook.setItems(bookList);

                        return;
                    }

                    ObservableList<Book> filtered =
                            FXCollections.observableArrayList();

                    for (Book book : bookList) {

                        String title =
                                book.getTitle() == null
                                ? ""
                                : book.getTitle();

                        String author =
                                book.getAuthor() == null
                                ? ""
                                : book.getAuthor();

                        String isbn =
                                book.getIsbn() == null
                                ? ""
                                : book.getIsbn();

                        if (
                                title.toLowerCase().contains(search)
                                ||
                                author.toLowerCase().contains(search)
                                ||
                                isbn.toLowerCase().contains(search)
                        ) {

                            filtered.add(book);
                        }
                    }

                    tableBook.setItems(filtered);
                }
        );
    }

    @FXML
    private void onAddMemberClick() {

        String surname =
                txtMemberSurname.getText().trim();

        String name =
                txtMemberName.getText().trim();

        String phoneText =
                txtMemberPhone.getText().trim();

        String email =
                txtMemberEmail.getText().trim();

        if (
                surname.isEmpty()
                ||
                name.isEmpty()
        ) {

            showWarning(
                    "Мэдээлэл дутуу",
                    "Овог болон нэрийг оруулна уу."
            );

            return;
        }

        Integer phone = null;

        if (!phoneText.isEmpty()) {

            try {

                phone = Integer.parseInt(phoneText);

            } catch (NumberFormatException e) {

                showWarning(
                        "Утасны дугаар буруу",
                        "Утасны дугаарыг зөвхөн тоогоор оруулна уу."
                );

                return;
            }
        }

        String sql =
                "INSERT INTO member " +
                "(surname, name, phone, email) " +
                "VALUES (?, ?, ?, ?)";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps =
                        conn.prepareStatement(sql)
        ) {

            ps.setString(1, surname);
            ps.setString(2, name);

            if (phone == null) {
                ps.setNull(3, Types.INTEGER);
            } else {
                ps.setInt(3, phone);
            }

            if (email.isEmpty()) {
                ps.setNull(4, Types.VARCHAR);
            } else {
                ps.setString(4, email);
            }

            ps.executeUpdate();

            showInfo(
                    "Амжилттай",
                    "Уншигч амжилттай бүртгэгдлээ."
            );

            txtMemberSurname.clear();
            txtMemberName.clear();
            txtMemberPhone.clear();
            txtMemberEmail.clear();

        } catch (SQLIntegrityConstraintViolationException e) {

            showWarning(
                    "Email давхардсан",
                    "Энэ email аль хэдийн бүртгэгдсэн байна."
            );

        } catch (SQLException e) {

            showError(
                    "Уншигч нэмэх үед алдаа гарлаа",
                    e.getMessage()
            );

            e.printStackTrace();
        }
    }

    private void setupRentalTable() {

        colRentalId.setCellValueFactory(
                new PropertyValueFactory<>("recordId")
        );

        colMemberName.setCellValueFactory(
                new PropertyValueFactory<>("memberName")
        );

        colBookTitle.setCellValueFactory(
                new PropertyValueFactory<>("bookTitle")
        );

        colBorrowDate.setCellValueFactory(
                new PropertyValueFactory<>("borrowDate")
        );

        colDueDate.setCellValueFactory(
                new PropertyValueFactory<>("dueDate")
        );

        colReturnDate.setCellValueFactory(
                new PropertyValueFactory<>("returnDate")
        );

        colStatus.setCellValueFactory(
                new PropertyValueFactory<>("status")
        );

        tableRental.setItems(rentalList);
    }

    private void loadRentals() {

        rentalList.clear();

        String sql =
                "SELECT " +
                "br.id_borrow_records, " +
                "CONCAT(m.surname, ' ', m.name) AS member_name, " +
                "b.ном_гарчиг AS book_title, " +
                "br.borrow_date, " +
                "br.due_date, " +
                "br.return_date, " +
                "br.status " +
                "FROM borrow_records br " +
                "JOIN member m " +
                "ON br.member_id = m.id_member " +
                "JOIN `ном` b " +
                "ON br.book_id = b.id_ном " +
                "ORDER BY br.id_borrow_records DESC";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps =
                        conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {

                int id =
                        rs.getInt("id_borrow_records");

                String memberName =
                        rs.getString("member_name");

                String bookTitle =
                        rs.getString("book_title");

                Timestamp borrowTimestamp =
                        rs.getTimestamp("borrow_date");

                Timestamp dueTimestamp =
                        rs.getTimestamp("due_date");

                Timestamp returnTimestamp =
                        rs.getTimestamp("return_date");

                String status =
                        rs.getString("status");

                String borrowDate =
                        borrowTimestamp == null
                        ? ""
                        : borrowTimestamp.toLocalDateTime()
                                .toString()
                                .replace("T", " ");

                String dueDate =
                        dueTimestamp == null
                        ? ""
                        : dueTimestamp.toLocalDateTime()
                                .toString()
                                .replace("T", " ");

                String returnDate =
                        returnTimestamp == null
                        ? ""
                        : returnTimestamp.toLocalDateTime()
                                .toString()
                                .replace("T", " ");

                Rental rental = new Rental(
                        id,
                        memberName,
                        bookTitle,
                        borrowDate,
                        dueDate,
                        returnDate,
                        status
                );

                rentalList.add(rental);
            }

        } catch (SQLException e) {

            showError(
                    "Түрээс унших үед алдаа гарлаа",
                    e.getMessage()
            );

            e.printStackTrace();
        }
    }

    private void setupRentalFilter() {

        radioAll.setOnAction(event -> loadRentals());

        radioOverdue.setOnAction(
                event -> showOverdueRentals()
        );
    }

    private void showOverdueRentals() {

        ObservableList<Rental> overdue =
                FXCollections.observableArrayList();

        LocalDate today =
                LocalDate.now();

        for (Rental rental : rentalList) {

            String status =
                    rental.getStatus();

            if (
                    status != null
                    &&
                    !status.equals("буцаасан")
                    &&
                    !rental.getDueDate().isEmpty()
            ) {

                try {

                    LocalDate dueDate =
                            LocalDate.parse(
                                    rental.getDueDate()
                                            .substring(0, 10)
                            );

                    if (dueDate.isBefore(today)) {

                        overdue.add(rental);
                    }

                } catch (Exception e) {

                    System.out.println(
                            "Огноо уншихад алдаа: "
                            + rental.getDueDate()
                    );
                }
            }
        }

        tableRental.setItems(overdue);
    }

    @FXML
    private void onBorrowBookClick() {

        ObservableList<Member> members =
                FXCollections.observableArrayList();

        ObservableList<Book> availableBooks =
                FXCollections.observableArrayList();

        loadMembers(members);

        for (Book book : bookList) {

            if (book.getAvailableQty() > 0) {

                availableBooks.add(book);
            }
        }

        if (members.isEmpty()) {

            showWarning(
                    "Уншигч байхгүй",
                    "Эхлээд Уншигч хэсгээс уншигч бүртгэнэ үү."
            );

            return;
        }

        if (availableBooks.isEmpty()) {

            showWarning(
                    "Ном байхгүй",
                    "Одоогоор түрээслэх боломжтой ном алга."
            );

            return;
        }

        ComboBox<Member> memberCombo =
                new ComboBox<>(members);

        memberCombo.setPrefWidth(250);

        ComboBox<Book> bookCombo =
                new ComboBox<>(availableBooks);

        bookCombo.setPrefWidth(250);

        DatePicker dueDatePicker =
                new DatePicker(
                        LocalDate.now().plusDays(7)
                );

        GridPane grid = new GridPane();

        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        grid.add(
                new Label("Уншигч:"),
                0,
                0
        );

        grid.add(
                memberCombo,
                1,
                0
        );

        grid.add(
                new Label("Ном:"),
                0,
                1
        );

        grid.add(
                bookCombo,
                1,
                1
        );

        grid.add(
                new Label("Буцаах огноо:"),
                0,
                2
        );

        grid.add(
                dueDatePicker,
                1,
                2
        );

        Dialog<ButtonType> dialog =
                new Dialog<>();

        dialog.setTitle("Ном түрээслэх");

        dialog.setHeaderText(
                "Ном түрээслэх мэдээлэл"
        );

        dialog.getDialogPane()
                .getButtonTypes()
                .addAll(
                        ButtonType.OK,
                        ButtonType.CANCEL
                );

        dialog.getDialogPane()
                .setContent(grid);

        ButtonType result =
                dialog.showAndWait()
                        .orElse(ButtonType.CANCEL);

        if (result != ButtonType.OK) {

            return;
        }

        Member member =
                memberCombo.getValue();

        Book book =
                bookCombo.getValue();

        LocalDate dueDate =
                dueDatePicker.getValue();

        if (
                member == null
                ||
                book == null
                ||
                dueDate == null
        ) {

            showWarning(
                    "Мэдээлэл дутуу",
                    "Бүх мэдээллийг сонгоно уу."
            );

            return;
        }

        borrowBook(
                member,
                book,
                dueDate
        );
    }

    private void loadMembers(
            ObservableList<Member> members
    ) {

        String sql =
                "SELECT id_member, surname, name " +
                "FROM member " +
                "ORDER BY id_member";

        try (
                Connection conn =
                        DBConnection.getConnection();

                PreparedStatement ps =
                        conn.prepareStatement(sql);

                ResultSet rs =
                        ps.executeQuery()
        ) {

            while (rs.next()) {

                Member member =
                        new Member(
                                rs.getInt("id_member"),
                                rs.getString("surname"),
                                rs.getString("name")
                        );

                members.add(member);
            }

        } catch (SQLException e) {

            showError(
                    "Уншигч унших үед алдаа гарлаа",
                    e.getMessage()
            );

            e.printStackTrace();
        }
    }

    private void borrowBook(
            Member member,
            Book book,
            LocalDate dueDate
    ) {

        Connection conn = null;

        try {

            conn =
                    DBConnection.getConnection();

            conn.setAutoCommit(false);

            String checkSql =
                    "SELECT available_qty " +
                    "FROM `ном` " +
                    "WHERE id_ном = ? " +
                    "FOR UPDATE";

            int available;

            try (
                    PreparedStatement ps =
                            conn.prepareStatement(checkSql)
            ) {

                ps.setInt(
                        1,
                        book.getId()
                );

                try (
                        ResultSet rs =
                                ps.executeQuery()
                ) {

                    if (!rs.next()) {

                        throw new SQLException(
                                "Ном олдсонгүй."
                        );
                    }

                    available =
                            rs.getInt(
                                    "available_qty"
                            );
                }
            }

            if (available <= 0) {

                conn.rollback();

                showWarning(
                        "Ном үлдэгдэлгүй",
                        "Энэ ном одоогоор түрээслэх боломжгүй."
                );

                return;
            }

            String insertSql =
                    "INSERT INTO borrow_records " +
                    "(borrow_date, due_date, status, book_id, member_id) " +
                    "VALUES (NOW(), ?, 'түрээслэсэн', ?, ?)";

            try (
                    PreparedStatement ps =
                            conn.prepareStatement(insertSql)
            ) {

                ps.setTimestamp(
                        1,
                        Timestamp.valueOf(
                                dueDate.atTime(23, 59, 59)
                        )
                );

                ps.setInt(
                        2,
                        book.getId()
                );

                ps.setInt(
                        3,
                        member.getId()
                );

                ps.executeUpdate();
            }

            String updateBookSql =
                    "UPDATE `ном` " +
                    "SET available_qty = available_qty - 1 " +
                    "WHERE id_ном = ?";

            try (
                    PreparedStatement ps =
                            conn.prepareStatement(
                                    updateBookSql
                            )
            ) {

                ps.setInt(
                        1,
                        book.getId()
                );

                ps.executeUpdate();
            }

            conn.commit();

            showInfo(
                    "Амжилттай",
                    "Ном амжилттай түрээслэгдлээ."
            );

            loadBooks();
            loadRentals();

        } catch (SQLException e) {

            if (conn != null) {

                try {

                    conn.rollback();

                } catch (SQLException ignored) {
                }
            }

            showError(
                    "Ном түрээслэх үед алдаа гарлаа",
                    e.getMessage()
            );

            e.printStackTrace();

        } finally {

            if (conn != null) {

                try {

                    conn.setAutoCommit(true);
                    conn.close();

                } catch (SQLException ignored) {
                }
            }
        }
    }

    @FXML
    private void onReturnBookClick() {

        Rental selected =
                tableRental.getSelectionModel()
                        .getSelectedItem();

        if (selected == null) {

            showWarning(
                    "Түрээс сонгоно уу",
                    "Буцаах түрээсийн мөрийг хүснэгтээс сонгоно уу."
            );

            return;
        }

        if (
                selected.getStatus() != null
                &&
                selected.getStatus()
                        .equals("буцаасан")
        ) {

            showWarning(
                    "Аль хэдийн буцаасан",
                    "Энэ ном аль хэдийн буцаагдсан байна."
            );

            return;
        }

        Alert confirm =
                new Alert(
                        Alert.AlertType.CONFIRMATION
                );

        confirm.setTitle("Ном буцаах");

        confirm.setHeaderText(
                "Ном буцаах уу?"
        );

        confirm.setContentText(
                "Ном: "
                + selected.getBookTitle()
                + "\nУншигч: "
                + selected.getMemberName()
        );

        ButtonType result =
                confirm.showAndWait()
                        .orElse(ButtonType.CANCEL);

        if (result != ButtonType.OK) {

            return;
        }

        returnBook(
                selected.getRecordId()
        );
    }

    private void returnBook(
            int recordId
    ) {

        Connection conn = null;

        try {

            conn =
                    DBConnection.getConnection();

            conn.setAutoCommit(false);

            String selectSql =
                    "SELECT book_id, status " +
                    "FROM borrow_records " +
                    "WHERE id_borrow_records = ? " +
                    "FOR UPDATE";

            int bookId;

            String status;

            try (
                    PreparedStatement ps =
                            conn.prepareStatement(
                                    selectSql
                            )
            ) {

                ps.setInt(
                        1,
                        recordId
                );

                try (
                        ResultSet rs =
                                ps.executeQuery()
                ) {

                    if (!rs.next()) {

                        throw new SQLException(
                                "Түрээсийн мэдээлэл олдсонгүй."
                        );
                    }

                    bookId =
                            rs.getInt("book_id");

                    status =
                            rs.getString("status");
                }
            }

            if (
                    status != null
                    &&
                    status.equals("буцаасан")
            ) {

                conn.rollback();

                showWarning(
                        "Алдаа",
                        "Энэ ном аль хэдийн буцаагдсан байна."
                );

                return;
            }

            String updateRentalSql =
                    "UPDATE borrow_records " +
                    "SET return_date = NOW(), " +
                    "status = 'буцаасан' " +
                    "WHERE id_borrow_records = ?";

            try (
                    PreparedStatement ps =
                            conn.prepareStatement(
                                    updateRentalSql
                            )
            ) {

                ps.setInt(
                        1,
                        recordId
                );

                ps.executeUpdate();
            }

            String updateBookSql =
                    "UPDATE `ном` " +
                    "SET available_qty = available_qty + 1 " +
                    "WHERE id_ном = ?";

            try (
                    PreparedStatement ps =
                            conn.prepareStatement(
                                    updateBookSql
                            )
            ) {

                ps.setInt(
                        1,
                        bookId
                );

                ps.executeUpdate();
            }

            conn.commit();

            showInfo(
                    "Амжилттай",
                    "Ном амжилттай буцаагдлаа."
            );

            loadBooks();
            loadRentals();

        } catch (SQLException e) {

            if (conn != null) {

                try {

                    conn.rollback();

                } catch (SQLException ignored) {
                }
            }

            showError(
                    "Ном буцаах үед алдаа гарлаа",
                    e.getMessage()
            );

            e.printStackTrace();

        } finally {

            if (conn != null) {

                try {

                    conn.setAutoCommit(true);
                    conn.close();

                } catch (SQLException ignored) {
                }
            }
        }
    }

    private void showInfo(
            String title,
            String message
    ) {

        Alert alert =
                new Alert(
                        Alert.AlertType.INFORMATION
                );

        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showWarning(
            String title,
            String message
    ) {

        Alert alert =
                new Alert(
                        Alert.AlertType.WARNING
                );

        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showError(
            String title,
            String message
    ) {

        Alert alert =
                new Alert(
                        Alert.AlertType.ERROR
                );

        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}