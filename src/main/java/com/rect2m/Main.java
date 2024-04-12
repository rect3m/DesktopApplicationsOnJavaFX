package com.rect2m;

import com.rect2m.database.DatabaseManager;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    private DatabaseManager dbManager;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        dbManager = new DatabaseManager();

        primaryStage.setTitle("JavaFX CRUD App");

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        Label nameLabel = new Label("Name:");
        TextField nameField = new TextField();
        gridPane.add(nameLabel, 0, 0);
        gridPane.add(nameField, 1, 0);

        Label emailLabel = new Label("Email:");
        TextField emailField = new TextField();
        gridPane.add(emailLabel, 0, 1);
        gridPane.add(emailField, 1, 1);

        Label phoneLabel = new Label("Phone:");
        TextField phoneField = new TextField();
        gridPane.add(phoneLabel, 0, 2);
        gridPane.add(phoneField, 1, 2);

        Label addressLabel = new Label("Address:");
        TextField addressField = new TextField();
        gridPane.add(addressLabel, 0, 3);
        gridPane.add(addressField, 1, 3);

        Label cityLabel = new Label("City:");
        TextField cityField = new TextField();
        gridPane.add(cityLabel, 0, 4);
        gridPane.add(cityField, 1, 4);

        Label countryLabel = new Label("Country:");
        TextField countryField = new TextField();
        gridPane.add(countryLabel, 0, 5);
        gridPane.add(countryField, 1, 5);

        Label ageLabel = new Label("Age:");
        TextField ageField = new TextField();
        gridPane.add(ageLabel, 0, 6);
        gridPane.add(ageField, 1, 6);

        Button addButton = new Button("Add");
        addButton.setOnAction(event -> {
            String name = nameField.getText();
            String email = emailField.getText();
            String phone = phoneField.getText();
            String address = addressField.getText();
            String city = cityField.getText();
            String country = countryField.getText();
            int age = Integer.parseInt(ageField.getText());
            dbManager.insertUser(name, email, phone, address, city, country,
                    age); // Додавання користувача
            clearFields(nameField, emailField, phoneField, addressField, cityField, countryField,
                    ageField);
        });

        Button updateButton = new Button("Update");
        updateButton.setOnAction(event -> {
            int userId = Integer.parseInt(showInputDialog("Enter User ID to update:"));
            String newName = nameField.getText();
            String newEmail = emailField.getText();
            String newPhone = phoneField.getText();
            String newAddress = addressField.getText();
            String newCity = cityField.getText();
            String newCountry = countryField.getText();
            int newAge = Integer.parseInt(ageField.getText());
            dbManager.updateUser(userId, newName, newEmail, newPhone, newAddress, newCity,
                    newCountry, newAge); // Оновлення користувача
            clearFields(nameField, emailField, phoneField, addressField, cityField, countryField,
                    ageField);
        });

        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(event -> {
            int userId = Integer.parseInt(showInputDialog("Enter User ID to delete:"));
            dbManager.deleteUser(userId); // Видалення користувача
        });

        Button showAllButton = new Button("Show All");
        showAllButton.setOnAction(event -> {
            dbManager.getAllUsers(); // Показати всіх користувачів
        });

        Button loadFakeDataButton = new Button("Load Fake Data");
        loadFakeDataButton.setOnAction(event -> {
            dbManager.insertInitialData(); // Додавання 50 фейкових записів при натисканні кнопки
        });

        gridPane.add(addButton, 0, 7);
        gridPane.add(updateButton, 1, 7);
        gridPane.add(deleteButton, 0, 8);
        gridPane.add(showAllButton, 1, 8);
        gridPane.add(loadFakeDataButton, 0, 9);

        //ПОШУК
        Label searchLabel = new Label("Enter search query:");
        TextField searchField = new TextField(); // Опреділяємо searchField
        Button searchButton = new Button("Search");
        Label resultLabel = new Label();

        gridPane.add(searchLabel, 0, 10);
        gridPane.add(searchField, 1, 10);
        gridPane.add(searchButton, 0, 11);
        gridPane.add(resultLabel, 0, 12);

        searchButton.setOnAction(event -> {
            String query = searchField.getText();

            // Створюємо завдання для пошуку
            Task<Void> searchTask = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    dbManager.searchUsers(query);
                    return null;
                }
            };

            searchTask.setOnSucceeded(e -> {
                resultLabel.setText("Search complete.");
            });

            Thread searchThread = new Thread(searchTask);
            searchThread.setDaemon(true);
            searchThread.start();
        });

        Scene scene = new Scene(gridPane, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void clearFields(TextField... fields) {
        for (TextField field : fields) {
            field.clear();
        }
    }

    private String showInputDialog(String message) {
        return new TextInputDialog().showAndWait().orElse("");
    }
}