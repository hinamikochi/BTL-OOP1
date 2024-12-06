package com.example.testbtl2.models;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BorrowedInfo {
    private String name;
    private String bookTitle;
    private String borrowDate;
    private String returnDate;

    public BorrowedInfo(String name, String bookTitle, String borrowDate, String returnDate) {
        this.name = name;
        this.bookTitle = bookTitle;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
    }

    public String getName() {
        return name;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public String getBorrowDate() {
        return borrowDate;
    }

    public String getReturnDate() {
        return returnDate;
    }
}
