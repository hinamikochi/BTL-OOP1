package com.example.testbtl2.models;

public abstract class Document {
    protected String id;
    protected String title;
    protected String author;
    protected int copiesAvailable;

    public Document(String id, String title, String author, int copiesAvailable) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.copiesAvailable = copiesAvailable;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getCopiesAvailable() {
        return copiesAvailable;
    }

    public void borrow() {
        if (copiesAvailable > 0) {
            copiesAvailable--;
        } else {
            throw new IllegalStateException("No copies available.");
        }
    }

    public void returnDocument() {
        copiesAvailable++;
    }

    public abstract String getInfo();
}

