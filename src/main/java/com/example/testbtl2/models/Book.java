package com.example.testbtl2.models;
import java.time.LocalDate;

public class Book extends Document {
    private String genre;
    private LocalDate borrowDate;
    private LocalDate returnDate;


    public Book(String id, String title, String author, int copiesAvailable, String genre) {
        super(id, title, author, copiesAvailable);
        this.genre = genre;
    }

    public String getGenre() {
        return genre;
    }

    // Phương thức để lấy thông tin mượn
    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    // Phương thức để lấy thông tin ngày trả
    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setBorrowDate(LocalDate borrowDate) {
        this.borrowDate = borrowDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }
    @Override
    public String getInfo() {
        return String.format("Book[ID=%s, Title=%s, Author=%s, Genre=%s, Copies=%d]",
                id, title, author, genre, copiesAvailable);
    }
}