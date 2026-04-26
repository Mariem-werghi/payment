module com.example.java {
    requires java.sql;
    requires java.desktop;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.swing;

    // ── PDF iText 5 (automatic module)
    requires itextpdf;

    // ── QR Code ZXing (automatic modules)
    requires com.google.zxing;
    requires com.google.zxing.javase;

    // ── Email JavaMail (automatic module)
    requires java.mail;
    requires activation;
    requires javafx.web;

    opens com.example.java             to javafx.fxml;
    opens com.example.java.controllers to javafx.fxml;
    opens com.example.java.entities    to javafx.base;

    exports com.example.java;
    exports com.example.java.controllers;
    exports com.example.java.entities;
    exports com.example.java.services;
    exports com.example.java.utils;
}
