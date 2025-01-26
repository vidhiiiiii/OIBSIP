package com.management;

import java.sql.*;
import java.util.Scanner;
import java.util.logging.Logger;
import java.util.logging.Level;

public class LibraryManagementSystem {

    private static final Logger LOGGER = Logger.getLogger(LibraryManagementSystem.class.getName());

    private static Connection connect() {
        try {
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/library_db", "root", "VTvt28**");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database connection failed", e);
            return null;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Connection connection = connect();

        if (connection == null) {
            System.out.println("Database connection failed. Please check your settings.");
            return;
        }

        System.out.println("Welcome to the Library Management System!");
        while (true) {
            System.out.println("1. Admin Login\n2. User Access\n3. Exit");
            System.out.print("Enter your choice: ");
            int choice = validateIntegerInput(scanner);
            switch (choice) {
                case 1:
                    adminModule(connection, scanner);
                    break;
                case 2:
                    userModule(connection, scanner);
                    break;
                case 3:
                    System.out.println("Thank you for using the system!");
                    return;
                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
            }
        }
    }

    private static void adminModule(Connection connection, Scanner scanner) {
        System.out.println("Admin Module Accessed");
        System.out.println("1. Add New Book\n2. Delete Book\n3. Update Book\n4. Add New User\n5. Delete User\n6. Generate Report\n7. Logout");
        System.out.print("Enter your choice: ");
        int choice = validateIntegerInput(scanner);
        switch (choice) {
            case 1:
                addNewBook(connection, scanner);
                break;
            case 2:
                deleteBook(connection, scanner);
                break;
            case 3:
                updateBook(connection, scanner);
                break;
            case 4:
                addNewUser(connection, scanner);
                break;
            case 5:
                deleteUser(connection, scanner);
                break;
            case 6:
                generateReport(connection);
                break;
            case 7:
                return;
            default:
                System.out.println("Invalid option. Please try again.");
        }
    }

    private static void userModule(Connection connection, Scanner scanner) {
        System.out.println("User Module Accessed");
        System.out.println("1. View Books\n2. Search Books\n3. Issue Book\n4. Return Book\n5. Contact Support\n6. Logout");
        System.out.print("Enter your choice: ");
        int choice = validateIntegerInput(scanner);
        switch (choice) {
            case 1:
                viewBooks(connection);
                break;
            case 2:
                searchBooks(connection, scanner);
                break;
            case 3:
                issueBook(connection, scanner);
                break;
            case 4:
                returnBook(connection, scanner);
                break;
            case 5:
                contactSupport();
                break;
            case 6:
                return;
            default:
                System.out.println("Invalid option. Please try again.");
        }
    }

    private static int validateIntegerInput(Scanner scanner) {
        while (true) {
            try {
                return scanner.nextInt();
            } catch (Exception e) {
                System.out.print("Invalid input. Please enter a number: ");
                scanner.next(); // clear invalid input
            }
        }
    }

    // Admin module functions
    private static void addNewBook(Connection connection, Scanner scanner) {
        System.out.print("Enter book title: ");
        String title = scanner.next();
        System.out.print("Enter author: ");
        String author = scanner.next();
        System.out.print("Enter category: ");
        String category = scanner.next();
        System.out.print("Enter quantity: ");
        int quantity = scanner.nextInt();

        try {
            String sql = "INSERT INTO books (title, author, category, quantity) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, title);
            statement.setString(2, author);
            statement.setString(3, category);
            statement.setInt(4, quantity);
            statement.executeUpdate();
            System.out.println("New book added successfully!");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error adding new book", e);
        }
    }

    private static void deleteBook(Connection connection, Scanner scanner) {
        System.out.print("Enter book ID to delete: ");
        int bookId = scanner.nextInt();

        try {
            String sql = "DELETE FROM books WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, bookId);
            statement.executeUpdate();
            System.out.println("Book deleted successfully!");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting book", e);
        }
    }

    private static void updateBook(Connection connection, Scanner scanner) {
        System.out.print("Enter book ID to update: ");
        int bookId = scanner.nextInt();
        System.out.print("Enter new quantity: ");
        int quantity = scanner.nextInt();

        try {
            String sql = "UPDATE books SET quantity = ? WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, quantity);
            statement.setInt(2, bookId);
            statement.executeUpdate();
            System.out.println("Book updated successfully!");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating book", e);
        }
    }

    private static void addNewUser(Connection connection, Scanner scanner) {
        System.out.print("Enter user name: ");
        String userName = scanner.next();
        System.out.print("Enter user email: ");
        String email = scanner.next();

        try {
            String sql = "INSERT INTO users (name, email) VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, userName);
            statement.setString(2, email);
            statement.executeUpdate();
            System.out.println("New user added successfully!");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error adding new user", e);
        }
    }

    private static void deleteUser(Connection connection, Scanner scanner) {
        System.out.print("Enter user ID to delete: ");
        int userId = scanner.nextInt();

        try {
            String sql = "DELETE FROM users WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, userId);
            statement.executeUpdate();
            System.out.println("User deleted successfully!");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting user", e);
        }
    }

    private static void generateReport(Connection connection) {
        try {
            String sql = "SELECT * FROM books";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            System.out.println("Library Report:");
            while (resultSet.next()) {
                System.out.println("ID: " + resultSet.getInt("id") + ", Title: " + resultSet.getString("title")
                        + ", Author: " + resultSet.getString("author") + ", Quantity: " + resultSet.getInt("quantity"));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error generating report", e);
        }
    }

    // User module functions
    private static void viewBooks(Connection connection) {
        try {
            String sql = "SELECT * FROM books";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            System.out.println("Available Books:");
            while (resultSet.next()) {
                System.out.println("ID: " + resultSet.getInt("id") + ", Title: " + resultSet.getString("title")
                        + ", Author: " + resultSet.getString("author") + ", Category: " + resultSet.getString("category"));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error viewing books", e);
        }
    }

    private static void searchBooks(Connection connection, Scanner scanner) {
        System.out.print("Enter book title to search: ");
        String title = scanner.next();

        try {
            String sql = "SELECT * FROM books WHERE title LIKE ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, "%" + title + "%");
            ResultSet resultSet = statement.executeQuery();

            System.out.println("Search Results:");
            while (resultSet.next()) {
                System.out.println("ID: " + resultSet.getInt("id") + ", Title: " + resultSet.getString("title")
                        + ", Author: " + resultSet.getString("author"));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error searching books", e);
        }
    }

    private static void issueBook(Connection connection, Scanner scanner) {
        System.out.print("Enter book ID to issue: ");
        int bookId = scanner.nextInt();

        try {
            String sql = "UPDATE books SET quantity = quantity - 1 WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, bookId);
            statement.executeUpdate();
            System.out.println("Book issued successfully!");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error issuing book", e);
        }
    }

    private static void returnBook(Connection connection, Scanner scanner) {
        System.out.print("Enter book ID to return: ");
        int bookId = scanner.nextInt();

        try {
            String sql = "UPDATE books SET quantity = quantity + 1 WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, bookId);
            statement.executeUpdate();
            System.out.println("Book returned successfully!");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error returning book", e);
        }
    }

    private static void contactSupport() {
        System.out.println("Please contact support at: support@library.com");
    }
}
