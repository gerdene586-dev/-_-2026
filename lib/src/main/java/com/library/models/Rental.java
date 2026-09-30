package com.library.models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Rental {

    private final IntegerProperty recordId;
    private final StringProperty memberName;
    private final StringProperty bookTitle;
    private final StringProperty borrowDate;
    private final StringProperty dueDate;
    private final StringProperty returnDate;
    private final StringProperty status;


    public Rental(
            int recordId,
            String memberName,
            String bookTitle,
            String borrowDate,
            String dueDate,
            String returnDate,
            String status
    ) {

        this.recordId =
                new SimpleIntegerProperty(recordId);

        this.memberName =
                new SimpleStringProperty(memberName);

        this.bookTitle =
                new SimpleStringProperty(bookTitle);

        this.borrowDate =
                new SimpleStringProperty(borrowDate);

        this.dueDate =
                new SimpleStringProperty(dueDate);

        this.returnDate =
                new SimpleStringProperty(returnDate);

        this.status =
                new SimpleStringProperty(status);
    }


    // =========================
    // RECORD ID
    // =========================

    public int getRecordId() {
        return recordId.get();
    }

    public IntegerProperty recordIdProperty() {
        return recordId;
    }


    // =========================
    // MEMBER NAME
    // =========================

    public String getMemberName() {
        return memberName.get();
    }

    public StringProperty memberNameProperty() {
        return memberName;
    }


    // =========================
    // BOOK TITLE
    // =========================

    public String getBookTitle() {
        return bookTitle.get();
    }

    public StringProperty bookTitleProperty() {
        return bookTitle;
    }


    // =========================
    // BORROW DATE
    // =========================

    public String getBorrowDate() {
        return borrowDate.get();
    }

    public StringProperty borrowDateProperty() {
        return borrowDate;
    }


    // =========================
    // DUE DATE
    // =========================

    public String getDueDate() {
        return dueDate.get();
    }

    public StringProperty dueDateProperty() {
        return dueDate;
    }


    // =========================
    // RETURN DATE
    // =========================

    public String getReturnDate() {
        return returnDate.get();
    }

    public StringProperty returnDateProperty() {
        return returnDate;
    }


    // =========================
    // STATUS
    // =========================

    public String getStatus() {
        return status.get();
    }

    public StringProperty statusProperty() {
        return status;
    }
}