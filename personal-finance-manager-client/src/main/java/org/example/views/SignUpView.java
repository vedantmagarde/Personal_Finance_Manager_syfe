package org.example.views;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import org.example.controllers.SignUpController;
import org.example.utils.Utilitie;
import org.example.utils.ViewNavigator;

public class SignUpView {

        private Label expenseTrackerLabel = new Label("Personal Finance Manager");

        private TextField nameField = new TextField();

        private TextField usernameField = new TextField();

        private PasswordField passwordField = new PasswordField();

        private PasswordField rePasswordField = new PasswordField();

        private Button registerButton = new Button("Register");

        private Label loginLabel = new Label("Already have an account? Login here");

        public void show() {

                Scene scene = createScene();

                scene.getStylesheets().add(
                                getClass()
                                                .getResource("/style.css")
                                                .toExternalForm());

                new SignUpController(this);

                ViewNavigator.switchViews(scene);
        }

        private Scene createScene() {

                VBox mainContainer = new VBox();

                mainContainer
                                .getStyleClass()
                                .add("login-background");

                mainContainer.setAlignment(
                                Pos.CENTER);

                VBox signUpCard = new VBox(35);

                signUpCard
                                .getStyleClass()
                                .add("login-card");

                signUpCard.setAlignment(
                                Pos.CENTER);

                expenseTrackerLabel
                                .getStyleClass()
                                .add("login-title");

                VBox signUpFormContainer = createSignUpForm();

                signUpCard
                                .getChildren()
                                .addAll(
                                                expenseTrackerLabel,
                                                signUpFormContainer);

                mainContainer
                                .getChildren()
                                .add(signUpCard);

                return new Scene(
                                mainContainer,
                                Utilitie.APP_WIDTH,
                                Utilitie.APP_HEIGHT);
        }

        private VBox createSignUpForm() {

                VBox signUpForm = new VBox(24);

                signUpForm.setAlignment(
                                Pos.CENTER);

                nameField
                                .getStyleClass()
                                .add("login-field");

                nameField.setPromptText(
                                "Enter Name");

                usernameField
                                .getStyleClass()
                                .add("login-field");

                usernameField.setPromptText(
                                "Enter Email");

                passwordField
                                .getStyleClass()
                                .add("login-field");

                passwordField.setPromptText(
                                "Enter Password");

                rePasswordField
                                .getStyleClass()
                                .add("login-field");

                rePasswordField.setPromptText(
                                "Re-Enter Password");

                registerButton
                                .getStyleClass()
                                .add("login-button");

                nameField.setOnAction(
                                event -> registerButton.fire());

                usernameField.setOnAction(
                                event -> registerButton.fire());

                passwordField.setOnAction(
                                event -> registerButton.fire());

                rePasswordField.setOnAction(
                                event -> registerButton.fire());

                loginLabel
                                .getStyleClass()
                                .add("login-link");

                signUpForm
                                .getChildren()
                                .addAll(
                                                nameField,
                                                usernameField,
                                                passwordField,
                                                rePasswordField,
                                                registerButton,
                                                loginLabel);

                return signUpForm;
        }

        public Label getExpenseTrackerLabel() {

                return expenseTrackerLabel;
        }

        public void setExpenseTrackerLabel(
                        Label expenseTrackerLabel) {

                this.expenseTrackerLabel = expenseTrackerLabel;
        }

        public TextField getNameField() {

                return nameField;
        }

        public void setNameField(
                        TextField nameField) {

                this.nameField = nameField;
        }

        public TextField getUsernameField() {

                return usernameField;
        }

        public void setUsernameField(
                        TextField usernameField) {

                this.usernameField = usernameField;
        }

        public PasswordField getPasswordField() {

                return passwordField;
        }

        public void setPasswordField(
                        PasswordField passwordField) {

                this.passwordField = passwordField;
        }

        public PasswordField getRePasswordField() {

                return rePasswordField;
        }

        public void setRePasswordField(
                        PasswordField rePasswordField) {

                this.rePasswordField = rePasswordField;
        }

        public Button getRegisterButton() {

                return registerButton;
        }

        public void setRegisterButton(
                        Button registerButton) {

                this.registerButton = registerButton;
        }

        public Label getLoginLabel() {

                return loginLabel;
        }

        public void setLoginLabel(
                        Label loginLabel) {

                this.loginLabel = loginLabel;
        }
}