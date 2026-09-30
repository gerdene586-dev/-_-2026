module com.library {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;//SQL хэрэглэх хэрэглэнэ

    opens com.library to javafx.fxml;
    opens com.library.controllers to javafx.fxml;

    exports com.library;
}
