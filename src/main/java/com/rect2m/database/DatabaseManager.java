package com.rect2m.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import com.github.javafaker.Faker;
import javafx.scene.control.TextArea;

public class DatabaseManager {

    private final String jdbcURL = "jdbc:h2:./data/test";
    private final String username = "";
    private final String password = "";
    private TextArea textArea;

    public DatabaseManager() {
        createDatabase();
        createTable();
        insertInitialData();
        System.out.println("База даних та таблиця створені успішно!");
    }


    public void setTextArea(TextArea textArea) {
        this.textArea = textArea;
    }

    private void createDatabase() {
        try (Connection connection = DriverManager.getConnection(jdbcURL, username, password)) {
            System.out.println("Connected to database.");
        } catch (SQLException e) {
            System.err.println("Error connecting to database: " + e.getMessage());
        }
    }

    private void createTable() {
        try (Connection connection = DriverManager.getConnection(jdbcURL, username, password);
                Statement statement = connection.createStatement()) {
            String createTableQuery = "CREATE TABLE IF NOT EXISTS users (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "name VARCHAR(255)," +
                    "email VARCHAR(255)," +
                    "phone VARCHAR(20)," +
                    "address VARCHAR(255)," +
                    "city VARCHAR(50)," +
                    "country VARCHAR(50)," +
                    "age INT" +
                    ")";
            statement.executeUpdate(createTableQuery);
            System.out.println("Table 'users' created successfully.");
        } catch (SQLException e) {
            System.err.println("Error creating table: " + e.getMessage());
        }
    }

    public void insertInitialData() {
        Faker faker = new Faker();
        try (Connection connection = DriverManager.getConnection(jdbcURL, username, password);
                Statement statement = connection.createStatement()) {

            String deleteQuery = "DELETE FROM users";
            statement.executeUpdate(deleteQuery);

            for (int i = 0; i < 50; i++) {
                String name = escapeQuotes(faker.name().fullName());
                String email = escapeQuotes(faker.internet().emailAddress());
                String phone = escapeQuotes(faker.phoneNumber().cellPhone());
                String address = escapeQuotes(faker.address().fullAddress());
                String city = escapeQuotes(faker.address().city());
                String country = escapeQuotes(faker.address().country());
                int age = faker.number().numberBetween(18, 80);

                String insertQuery = String.format(
                        "INSERT INTO users (name, email, phone, address, city, country, age) " +
                                "VALUES ('%s', '%s', '%s', '%s', '%s', '%s', %d)", name, email,
                        phone, address, city, country, age);
                statement.executeUpdate(insertQuery);
            }
            System.out.println("50 fake users inserted successfully.");
        } catch (SQLException e) {
            System.err.println("Error inserting initial data: " + e.getMessage());
        }
    }

    private String escapeQuotes(String input) {
        return input.replace("'", "''");
    }

    public void insertUser(String name, String email, String phone, String address, String city,
            String country, int age) {
        try (Connection connection = DriverManager.getConnection(jdbcURL, username, password);
                Statement statement = connection.createStatement()) {
            String insertQuery = String.format(
                    "INSERT INTO users (name, email, phone, address, city, country, age) " +
                            "VALUES ('%s', '%s', '%s', '%s', '%s', '%s', %d)", name, email, phone,
                    address, city, country, age);
            statement.executeUpdate(insertQuery);
            System.out.println("New user inserted successfully.");
        } catch (SQLException e) {
            System.err.println("Error inserting user: " + e.getMessage());
        }
    }

    public void updateUser(int id, String name, String email, String phone, String address,
            String city, String country, int age) {
        try (Connection connection = DriverManager.getConnection(jdbcURL, username, password);
                Statement statement = connection.createStatement()) {
            String updateQuery = String.format(
                    "UPDATE users SET name='%s', email='%s', phone='%s', address='%s', " +
                            "city='%s', country='%s', age=%d WHERE id=%d", name, email, phone,
                    address, city, country, age, id);
            statement.executeUpdate(updateQuery);
            System.out.println("User updated successfully.");
        } catch (SQLException e) {
            System.err.println("Error updating user: " + e.getMessage());
        }
    }

    public void deleteUser(int id) {
        try (Connection connection = DriverManager.getConnection(jdbcURL, username, password);
                Statement statement = connection.createStatement()) {
            String deleteQuery = "DELETE FROM users WHERE id=" + id;
            statement.executeUpdate(deleteQuery);
            System.out.println("User deleted successfully.");
        } catch (SQLException e) {
            System.err.println("Error deleting user: " + e.getMessage());
        }
    }

    public void getAllUsers() {
        try (Connection connection = DriverManager.getConnection(jdbcURL, username, password);
                Statement statement = connection.createStatement()) {
            String selectQuery = "SELECT * FROM users";
            ResultSet resultSet = statement.executeQuery(selectQuery);
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                String phone = resultSet.getString("phone");
                String address = resultSet.getString("address");
                String city = resultSet.getString("city");
                String country = resultSet.getString("country");
                int age = resultSet.getInt("age");

                System.out.println("User ID: " + id);
                System.out.println("Name: " + name);
                System.out.println("Email: " + email);
                System.out.println("Phone: " + phone);
                System.out.println("Address: " + address);
                System.out.println("City: " + city);
                System.out.println("Country: " + country);
                System.out.println("Age: " + age);
                System.out.println();
            }
        } catch (SQLException e) {
            System.err.println("Error getting users: " + e.getMessage());
        }
    }

    public void searchUsers(String query) {
        try (Connection connection = DriverManager.getConnection(jdbcURL, username, password);
                Statement statement = connection.createStatement()) {
            String searchQuery =
                    "SELECT * FROM users WHERE name LIKE '%" + query + "%' OR email LIKE '%" + query
                            + "%'";
            ResultSet resultSet = statement.executeQuery(searchQuery);
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                String phone = resultSet.getString("phone");
                String address = resultSet.getString("address");
                String city = resultSet.getString("city");
                String country = resultSet.getString("country");
                int age = resultSet.getInt("age");

                System.out.println("User ID: " + id);
                System.out.println("Name: " + name);
                System.out.println("Email: " + email);
                System.out.println("Phone: " + phone);
                System.out.println("Address: " + address);
                System.out.println("City: " + city);
                System.out.println("Country: " + country);
                System.out.println("Age: " + age);
                System.out.println();
            }
        } catch (SQLException e) {
            System.err.println("Error searching users: " + e.getMessage());
        }
    }
}
