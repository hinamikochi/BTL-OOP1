package com.example.testbtl2.models;

public class Member extends User {
    private int borrowedCount;

    public Member(String id, String name) {
        super(id, name);
        this.borrowedCount = 0;
    }

    public void borrowDocument() {
        borrowedCount++;
    }

    public void returnDocument() {
        borrowedCount--;
    }

    @Override
    public String getUserInfo() {
        return String.format("Member[ID=%s, Name=%s, BorrowedCount=%d]", id, name, borrowedCount);
    }
}