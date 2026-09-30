module com.library {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;//SQL хэрэглэх хэрэглэнэ

    opens com.library to javafx.fxml;
    opens com.library.controllers to javafx.fxml;
    //модел классуудыг javafx-ийн tableview-д нээж өгөх
    opens com.library.models to javafx.base,javafx.fxml;

    exports com.library;
}
