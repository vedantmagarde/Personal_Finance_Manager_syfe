module personal.finance.manager.client {

    requires javafx.controls;
    requires java.net.http;
    requires com.google.gson;

    opens org.example.models to javafx.base, com.google.gson;

    exports org.example;
    exports org.example.models;
    exports org.example.controllers;
    exports org.example.views;
    exports org.example.dialogs;
    exports org.example.utils;
}