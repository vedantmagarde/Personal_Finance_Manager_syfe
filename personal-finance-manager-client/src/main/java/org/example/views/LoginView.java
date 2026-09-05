package org.example.views;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import org.example.controllers.LoginController;
import org.example.utils.Utilitie;
import org.example.utils.ViewNavigator;

public class LoginView {

        private Label expenseTrackerLabel = new Label("Personal Finance Manager");

        private TextField usernameField = new TextField();

        private PasswordField passwordField = new PasswordField();

        private Button loginButton = new Button("Login");

        private Label signupLabel = new Label("Don't have an account? Click Here");

        public void show() {

                Scene scene = createScene();

                scene.getStylesheets().add(
                                getClass()
                                                .getResource("/style.css")
                                                .toExternalForm());

                new LoginController(this);

                ViewNavigator.switchViews(scene);
        }

        private Scene createScene() {

                VBox mainContainerBox = new VBox();

                mainContainerBox
                                .getStyleClass()
                                .add("login-background");

                mainContainerBox.setAlignment(
                                Pos.CENTER);

                /* MAIN LOGIN CARD */

                VBox loginCard = new VBox(35);

                loginCard.getStyleClass().add(
                                "login-card");

                loginCard.setAlignment(
                                Pos.CENTER);

                /* TITLE */

                expenseTrackerLabel
                                .getStyleClass()
                                .add("login-title");

                /* LOGIN FORM */

                VBox loginFormBox = createLoginFormBox();

                loginCard
                                .getChildren()
                                .addAll(
                                                expenseTrackerLabel,
                                                loginFormBox);

                mainContainerBox
                                .getChildren()
                                .add(loginCard);

                return new Scene(
                                mainContainerBox,
                                Utilitie.APP_WIDTH,
                                Utilitie.APP_HEIGHT);
        }

        private VBox createLoginFormBox() {

                VBox loginFormVBox = new VBox(30);

                loginFormVBox
                                .getStyleClass()
                                .add("login-card");

                loginFormVBox.setAlignment(
                                Pos.CENTER);

                usernameField
                                .getStyleClass()
                                .add("login-field");

                usernameField.setPromptText(
                                "Enter Email");

                usernameField.setMaxWidth(640);

                passwordField
                                .getStyleClass()
                                .add("login-field");

                passwordField.setPromptText(
                                "Enter Password");

                passwordField.setMaxWidth(640);

                loginButton
                                .getStyleClass()
                                .add("login-button");

                loginButton.setMaxWidth(640);

                usernameField.setOnAction(
                                event -> loginButton.fire());

                passwordField.setOnAction(
                                event -> loginButton.fire());

                signupLabel
                                .getStyleClass()
                                .add("login-link");

                loginFormVBox
                                .getChildren()
                                .addAll(
                                                usernameField,
                                                passwordField,
                                                loginButton,
                                                signupLabel);

                return loginFormVBox;
        }

        public Label getExpenseTrackerLabel() {

                return expenseTrackerLabel;
        }

        public void setExpenseTrackerLabel(
                        Label expenseTrackerLabel) {

                this.expenseTrackerLabel = expenseTrackerLabel;
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

        public Button getLoginButton() {

                return loginButton;
        }

        public void setLoginButton(
                        Button loginButton) {

                this.loginButton = loginButton;
        }

        public Label getSignupLabel() {

                return signupLabel;
        }

        public void setSignupLabel(
                        Label signupLabel) {

                this.signupLabel = signupLabel;
        }
}