package org.example.views;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;

import org.example.animations.LoadingAnimationPane;
import org.example.controllers.DashboardController;
import org.example.models.MonthlyFinance;
import org.example.utils.Utilitie;
import org.example.utils.ViewNavigator;

import java.math.BigDecimal;
import java.time.Year;

public class DashboardView {

        private String email;

        private LoadingAnimationPane loadingAnimationPane;

        private Label currentBalanceLabel;
        private Label currentBalance;

        private Label totalIncomeLabel;
        private Label totalIncome;

        private Label totalExpenseLabel;
        private Label totalExpense;

        private ComboBox<Integer> yearComboBox;

        private Button addTransactionButton;
        private Button viewChartButton;

        private VBox recentTransactionBox;

        private ScrollPane recentTransactionsScrollPane;

        private MenuItem createCategoryMenuItem;
        private MenuItem viewCategoriesMenuItem;
        private MenuItem logoutMenuItem;

        private TableView<MonthlyFinance> transactionTable;

        private TableColumn<MonthlyFinance, String> monthColumn;
        private TableColumn<MonthlyFinance, BigDecimal> incomeColumn;
        private TableColumn<MonthlyFinance, BigDecimal> expenseColumn;

        public DashboardView(String email) {

                this.email = email;

                loadingAnimationPane = new LoadingAnimationPane(
                                Utilitie.APP_WIDTH,
                                Utilitie.APP_HEIGHT);

                currentBalanceLabel = new Label("Current Balance:");
                totalIncomeLabel = new Label("Total Income:");
                totalExpenseLabel = new Label("Total Expense:");

                addTransactionButton = new Button("+");

                currentBalance = new Label("$0.00");
                totalIncome = new Label("$0.00");
                totalExpense = new Label("$0.00");
        }

        public void show() {

                Scene scene = createScene();

                scene.getStylesheets().add(
                                getClass()
                                                .getResource("/style.css")
                                                .toExternalForm());

                new DashboardController(this);

                scene.widthProperty().addListener(
                                new ChangeListener<Number>() {

                                        @Override
                                        public void changed(
                                                        ObservableValue<? extends Number> observableValue,
                                                        Number number,
                                                        Number t1) {

                                                loadingAnimationPane.resizeWidth(
                                                                t1.doubleValue());

                                                resizeTableWidthColumns();
                                        }
                                });

                scene.heightProperty().addListener(
                                new ChangeListener<Number>() {

                                        @Override
                                        public void changed(
                                                        ObservableValue<? extends Number> observableValue,
                                                        Number number,
                                                        Number t1) {

                                                loadingAnimationPane.resizeHeight(
                                                                t1.doubleValue());
                                        }
                                });

                ViewNavigator.switchViews(scene);
        }

        private Scene createScene() {

                MenuBar menuBar = createMenuBar();

                VBox mainContainer = new VBox();

                mainContainer.getStyleClass().addAll(
                                "main-background");

                VBox mainContainerWrapper = new VBox(25);

                mainContainerWrapper.getStyleClass().addAll(
                                "dashboard-padding");

                VBox.setVgrow(
                                mainContainerWrapper,
                                Priority.ALWAYS);

                HBox balanceSummaryBox = createBalanceSummaryBox();

                GridPane contentGridPane = createContentGridPane();

                VBox.setVgrow(
                                contentGridPane,
                                Priority.ALWAYS);

                mainContainerWrapper.getChildren().addAll(
                                balanceSummaryBox,
                                contentGridPane);

                mainContainer.getChildren().addAll(
                                menuBar,
                                mainContainerWrapper,
                                loadingAnimationPane);

                return new Scene(
                                mainContainer,
                                Utilitie.APP_WIDTH,
                                Utilitie.APP_HEIGHT);
        }

        private MenuBar createMenuBar() {

                MenuBar menuBar = new MenuBar();

                Menu fileMenu = new Menu("☰ More");

                createCategoryMenuItem = new MenuItem("➕ Create Category");
                viewCategoriesMenuItem = new MenuItem("📂 View Categories");
                logoutMenuItem = new MenuItem("🚪 Logout");

                fileMenu.getItems().addAll(
                                createCategoryMenuItem,
                                viewCategoriesMenuItem,
                                logoutMenuItem);

                menuBar.getMenus().add(fileMenu);

                return menuBar;
        }

        private HBox createBalanceSummaryBox() {

                HBox balanceSummaryBox = new HBox(20);

                VBox currentBalanceBox = new VBox(10);

                currentBalanceBox.getStyleClass().addAll(
                                "summary-card",
                                "balance-card");

                currentBalanceLabel.getStyleClass().addAll(
                                "text-size-md",
                                "text-white");

                currentBalance.getStyleClass().addAll(
                                "text-size-lg",
                                "text-white",
                                "text-weight-700");

                currentBalanceBox.getChildren().addAll(
                                currentBalanceLabel,
                                currentBalance);

                HBox.setHgrow(
                                currentBalanceBox,
                                Priority.ALWAYS);

                VBox totalIncomeBox = new VBox(10);

                totalIncomeBox.getStyleClass().addAll(
                                "summary-card",
                                "income-card");

                totalIncomeLabel.getStyleClass().addAll(
                                "text-size-md",
                                "text-white");

                totalIncome.getStyleClass().addAll(
                                "text-size-lg",
                                "text-white",
                                "text-weight-700");

                totalIncomeBox.getChildren().addAll(
                                totalIncomeLabel,
                                totalIncome);

                HBox.setHgrow(
                                totalIncomeBox,
                                Priority.ALWAYS);

                VBox totalExpenseBox = new VBox(10);

                totalExpenseBox.getStyleClass().addAll(
                                "summary-card",
                                "expense-card");

                totalExpenseLabel.getStyleClass().addAll(
                                "text-size-md",
                                "text-white");

                totalExpense.getStyleClass().addAll(
                                "text-size-lg",
                                "text-white",
                                "text-weight-700");

                totalExpenseBox.getChildren().addAll(
                                totalExpenseLabel,
                                totalExpense);

                HBox.setHgrow(
                                totalExpenseBox,
                                Priority.ALWAYS);

                balanceSummaryBox.getChildren().addAll(
                                currentBalanceBox,
                                totalIncomeBox,
                                totalExpenseBox);

                return balanceSummaryBox;
        }

        private GridPane createContentGridPane() {

                GridPane gridPane = new GridPane();

                gridPane.setHgap(20);

                ColumnConstraints columnConstraint = new ColumnConstraints();

                columnConstraint.setPercentWidth(50);

                gridPane.getColumnConstraints().addAll(
                                columnConstraint,
                                columnConstraint);

                VBox transactionsTableSummaryBox = new VBox(20);

                transactionsTableSummaryBox.getStyleClass().add(
                                "panel");

                HBox yearComboBoxAndChartButtonBox = createYearComboBoxAndChartButtonBox();

                VBox transactionTableContentBox = createTransactionsTableContentBox();

                VBox.setVgrow(
                                transactionTableContentBox,
                                Priority.ALWAYS);

                transactionsTableSummaryBox
                                .getChildren()
                                .addAll(
                                                yearComboBoxAndChartButtonBox,
                                                transactionTableContentBox);

                VBox recentTransactionsVBox = createRecentTransactionsVBox();

                recentTransactionsVBox.getStyleClass().add(
                                "panel");

                GridPane.setVgrow(
                                recentTransactionsVBox,
                                Priority.ALWAYS);

                gridPane.add(
                                transactionsTableSummaryBox,
                                0,
                                0);

                gridPane.add(
                                recentTransactionsVBox,
                                1,
                                0);

                return gridPane;
        }

        private HBox createYearComboBoxAndChartButtonBox() {

                HBox hbox = new HBox(15);

                yearComboBox = new ComboBox<>();

                yearComboBox.getStyleClass().addAll(
                                "text-size-md");

                yearComboBox.setValue(
                                Year.now().getValue());

                viewChartButton = new Button("View Chart");

                viewChartButton.getStyleClass().addAll(
                                "primary-button",
                                "text-size-md");

                hbox.getChildren().addAll(
                                yearComboBox,
                                viewChartButton);

                return hbox;
        }

        private VBox createTransactionsTableContentBox() {

                VBox vbox = new VBox();

                transactionTable = new TableView<>();

                VBox.setVgrow(
                                transactionTable,
                                Priority.ALWAYS);

                monthColumn = new TableColumn<>("Month");

                monthColumn.setCellValueFactory(
                                new PropertyValueFactory<>("month"));

                incomeColumn = new TableColumn<>("Income");

                incomeColumn.setCellValueFactory(
                                new PropertyValueFactory<>("income"));

                expenseColumn = new TableColumn<>("Expense");

                expenseColumn.setCellValueFactory(
                                new PropertyValueFactory<>("expense"));

                transactionTable.getColumns().addAll(
                                monthColumn,
                                incomeColumn,
                                expenseColumn);

                vbox.getChildren().addAll(
                                transactionTable);

                resizeTableWidthColumns();

                return vbox;
        }

        private VBox createRecentTransactionsVBox() {

                VBox recentTransactionsVBox = new VBox(15);

                HBox recentTransactionLabelAndAddBtnBox = new HBox();

                Label recentTransactionsLabel = new Label("Recent Transactions");

                recentTransactionsLabel.getStyleClass().addAll(
                                "text-size-md",
                                "text-white",
                                "text-weight-700");

                Region labelAndButtonSpaceRegion = new Region();

                HBox.setHgrow(
                                labelAndButtonSpaceRegion,
                                Priority.ALWAYS);

                addTransactionButton.getStyleClass().addAll(
                                "primary-button",
                                "text-size-md");

                recentTransactionLabelAndAddBtnBox
                                .getChildren()
                                .addAll(
                                                recentTransactionsLabel,
                                                labelAndButtonSpaceRegion,
                                                addTransactionButton);

                recentTransactionBox = new VBox(10);

                recentTransactionsScrollPane = new ScrollPane(
                                recentTransactionBox);

                recentTransactionsScrollPane.setFitToWidth(true);
                recentTransactionsScrollPane.setFitToHeight(true);

                VBox.setVgrow(
                                recentTransactionsScrollPane,
                                Priority.ALWAYS);

                recentTransactionsVBox.getChildren().addAll(
                                recentTransactionLabelAndAddBtnBox,
                                recentTransactionsScrollPane);

                return recentTransactionsVBox;
        }

        private void resizeTableWidthColumns() {

                Platform.runLater(
                                new Runnable() {

                                        @Override
                                        public void run() {

                                                double colsWidth = transactionTable.getWidth()
                                                                * 0.335;

                                                monthColumn.setPrefWidth(colsWidth);
                                                incomeColumn.setPrefWidth(colsWidth);
                                                expenseColumn.setPrefWidth(colsWidth);
                                        }
                                });
        }

        public MenuItem getCreateCategoryMenuItem() {
                return createCategoryMenuItem;
        }

        public void setCreateCategoryMenuItem(
                        MenuItem createCategoryMenuItem) {
                this.createCategoryMenuItem = createCategoryMenuItem;
        }

        public MenuItem getViewCategoriesMenuItem() {
                return viewCategoriesMenuItem;
        }

        public MenuItem getLogoutMenuItem() {
                return logoutMenuItem;
        }

        public String getEmail() {
                return email;
        }

        public Button getAddTransactionButton() {
                return addTransactionButton;
        }

        public VBox getRecentTransactionBox() {
                return recentTransactionBox;
        }

        public LoadingAnimationPane getLoadingAnimationPane() {
                return loadingAnimationPane;
        }

        public TableView<MonthlyFinance> getTransactionTable() {
                return transactionTable;
        }

        public TableColumn<MonthlyFinance, String> getMonthColumn() {
                return monthColumn;
        }

        public TableColumn<MonthlyFinance, BigDecimal> getIncomeColumn() {
                return incomeColumn;
        }

        public TableColumn<MonthlyFinance, BigDecimal> getExpenseColumn() {
                return expenseColumn;
        }

        public ComboBox<Integer> getYearComboBox() {
                return yearComboBox;
        }

        public Label getCurrentBalance() {
                return currentBalance;
        }

        public Label getTotalIncome() {
                return totalIncome;
        }

        public Label getTotalExpense() {
                return totalExpense;
        }

        public Button getViewChartButton() {
                return viewChartButton;
        }
}