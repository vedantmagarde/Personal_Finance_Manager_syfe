package org.example.components;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;

import org.example.controllers.DashboardController;
import org.example.dialogs.CreateOrEditTransactionDialog;
import org.example.models.Transaction;
import org.example.utils.SqlUtil;

public class TransactionComponent extends HBox {

        private Label transactionCategoryLabel;
        private Label transactionNameLabel;
        private Label transactionDateLabel;
        private Label transactionAmountLabel;

        private Button editButton;
        private Button delButton;

        private DashboardController dashboardController;
        private Transaction transaction;

        public TransactionComponent(
                        DashboardController dashboardController,
                        Transaction transaction) {

                this.dashboardController = dashboardController;
                this.transaction = transaction;

                setSpacing(16);
                setAlignment(Pos.CENTER_LEFT);

                getStyleClass().add("transaction-card");

                VBox categoryNameDateSection = createCategoryNameDateSection();

                Region region = new Region();

                HBox.setHgrow(
                                region,
                                Priority.ALWAYS);

                transactionAmountLabel = new Label(
                                "₹" + transaction.getTransactionAmount());

                if (transaction.getTransactionType()
                                .equalsIgnoreCase("expense")) {

                        transactionAmountLabel
                                        .getStyleClass()
                                        .add("transaction-expense");

                } else {

                        transactionAmountLabel
                                        .getStyleClass()
                                        .add("transaction-income");
                }

                HBox actionButtonSection = createActionButtons();

                getChildren().addAll(
                                categoryNameDateSection,
                                region,
                                transactionAmountLabel,
                                actionButtonSection);
        }

        private VBox createCategoryNameDateSection() {

                VBox categoryNameDateSection = new VBox(4);

                if (transaction.getTransactionCategory() == null) {

                        transactionCategoryLabel = new Label("Undefined");

                        transactionCategoryLabel
                                        .getStyleClass()
                                        .add("transaction-date");

                } else {

                        transactionCategoryLabel = new Label(
                                        transaction
                                                        .getTransactionCategory()
                                                        .getCategoryName());

                        transactionCategoryLabel
                                        .getStyleClass()
                                        .add("transaction-category");

                        transactionCategoryLabel.setTextFill(
                                        Paint.valueOf(
                                                        "#" +
                                                                        transaction
                                                                                        .getTransactionCategory()
                                                                                        .getCategoryColor()));
                }

                transactionNameLabel = new Label(
                                transaction.getTransactionName());

                transactionNameLabel.getStyleClass().add(
                                "transaction-title");

                transactionDateLabel = new Label(
                                transaction
                                                .getTransactionDate()
                                                .toString());

                transactionDateLabel.getStyleClass().add(
                                "transaction-date");

                categoryNameDateSection
                                .getChildren()
                                .addAll(
                                                transactionCategoryLabel,
                                                transactionNameLabel,
                                                transactionDateLabel);

                return categoryNameDateSection;
        }

        private HBox createActionButtons() {

                HBox actionButtonSection = new HBox(16);

                actionButtonSection.setAlignment(
                                Pos.CENTER);

                editButton = new Button("Edit");

                editButton.getStyleClass().addAll(
                                "edit-button",
                                "text-size-md");

                editButton.setOnMouseClicked(
                                new EventHandler<MouseEvent>() {

                                        @Override
                                        public void handle(
                                                        MouseEvent mouseEvent) {

                                                new CreateOrEditTransactionDialog(
                                                                dashboardController,
                                                                TransactionComponent.this,
                                                                true).showAndWait();
                                        }
                                });

                delButton = new Button("Del");

                delButton.getStyleClass().addAll(
                                "danger-button",
                                "text-size-md");

                delButton.setOnMouseClicked(
                                new EventHandler<MouseEvent>() {

                                        @Override
                                        public void handle(
                                                        MouseEvent mouseEvent) {

                                                if (!SqlUtil.deleteTransactionById(
                                                                transaction.getId())) {
                                                        return;
                                                }

                                                setVisible(false);
                                                setManaged(false);

                                                if (getParent() instanceof VBox) {

                                                        ((VBox) getParent())
                                                                        .getChildren()
                                                                        .remove(
                                                                                        TransactionComponent.this);
                                                }

                                                dashboardController.fetchUserData();
                                        }
                                });

                actionButtonSection.getChildren().addAll(
                                editButton,
                                delButton);

                return actionButtonSection;
        }

        public Transaction getTransaction() {
                return transaction;
        }

        public Label getTransactionCategoryLabel() {
                return transactionCategoryLabel;
        }

        public Label getTransactionNameLabel() {
                return transactionNameLabel;
        }

        public Label getTransactionDateLabel() {
                return transactionDateLabel;
        }

        public Label getTransactionAmountLabel() {
                return transactionAmountLabel;
        }
}