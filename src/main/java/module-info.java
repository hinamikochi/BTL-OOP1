module com.example.testbtl2 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires org.json;

    // Export các package cần thiết cho các module khác
    exports com.example.testbtl2.ui;
    exports com.example.testbtl2.models; // Đảm bảo rằng package models được xuất khẩu

    // Mở package com.example.testbtl2 cho javafx.fxml và javafx.base
    opens com.example.testbtl2 to javafx.fxml, javafx.base;
}
