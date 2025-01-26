package com.reservation;

import java.sql.*;
import java.util.Scanner;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.regex.Pattern;
import java.io.FileOutputStream;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;

public class OnlineReservationSystem {

    private static final Logger LOGGER = Logger.getLogger(OnlineReservationSystem.class.getName());
    private static boolean isLoggedIn = false;

    private static Connection connect() {
        try {
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/reservation_db", "root", "VTvt28**");
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

        System.out.println("Welcome to the Online Reservation System!");
        while (true) {
            if (isLoggedIn) {
                System.out.println("1. Reserve Ticket\n2. Cancel Ticket\n3. Search Tickets\n4. Exit");
            } else {
                System.out.println("1. Register\n2. Login\n6. Exit");
            }
            System.out.print("Enter your choice: ");
            int choice = validateIntegerInput(scanner);

            if (isLoggedIn) {
                switch (choice) {
                    case 1:
                        reserveTicket(connection, scanner);
                        break;
                    case 2:
                        cancelTicket(connection, scanner);
                        break;
                    case 3:
                        searchTickets(connection, scanner);
                        break;
                    case 4:
                        if (confirmAction(scanner, "exit the system")) {
                            System.out.println("Thank you for using the system!");
                            return;
                        } else {
                            System.out.println("Exit aborted.");
                        }
                        break;
                    default:
                        System.out.println("Invalid choice. Please enter a valid option.");
                }
            } else {
                switch (choice) {
                    case 1:
                        register(connection, scanner);
                        break;
                    case 2:
                        login(connection, scanner);
                        break;
                    case 6:
                        if (confirmAction(scanner, "exit the system")) {
                            System.out.println("Thank you for using the system!");
                            return;
                        } else {
                            System.out.println("Exit aborted.");
                        }
                        break;
                    default:
                        System.out.println("Invalid choice. Please enter a valid option.");
                }
            }
        }
    }

    private static void register(Connection connection, Scanner scanner) {
        try {
            System.out.print("Enter Username: ");
            String username = validateStringInput(scanner);
            System.out.print("Enter Password: ");
            String password = validateStringInput(scanner);

            PreparedStatement ps = connection.prepareStatement("INSERT INTO users (username, password) VALUES (?, ?)");
            ps.setString(1, username);
            ps.setString(2, password);
            ps.executeUpdate();

            System.out.println("Registration successful! You can now log in.");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error during registration", e);
        }
    }

    private static void login(Connection connection, Scanner scanner) {
        try {
            System.out.print("Enter Username: ");
            String username = validateStringInput(scanner);
            System.out.print("Enter Password: ");
            String password = validateStringInput(scanner);

            PreparedStatement ps = connection.prepareStatement("SELECT * FROM users WHERE username = ? AND password = ?");
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                System.out.println("Login successful. Welcome, " + rs.getString("username") + "!");
                isLoggedIn = true;
            } else {
                System.out.println("Invalid credentials. Please try again.");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error during login", e);
        }
    }

    private static void reserveTicket(Connection connection, Scanner scanner) {
        try {
            System.out.print("Enter Train Number: ");
            int trainNumber = validateIntegerInput(scanner);

            // Fetch train name based on the entered train number
            PreparedStatement psTrain = connection.prepareStatement("SELECT train_name FROM trains WHERE train_number = ?");
            psTrain.setInt(1, trainNumber);
            ResultSet rsTrain = psTrain.executeQuery();

            if (rsTrain.next()) {
                String trainName = rsTrain.getString("train_name");
                System.out.println("Train Name: " + trainName);
            } else {
                System.out.println("No train found with the provided train number.");
                return;
            }

            System.out.print("Enter Class Type: ");
            String classType = validateStringInput(scanner);
            System.out.print("Enter Date of Journey (YYYY-MM-DD): ");
            String dateOfJourney = validateDateInput(scanner);
            System.out.print("Enter From: ");
            String from = validateStringInput(scanner);
            System.out.print("Enter To: ");
            String to = validateStringInput(scanner);

            // Confirm reservation details before finalizing
            System.out.println("Reservation Details:");
            System.out.println("Train Number: " + trainNumber);
            System.out.println("Class Type: " + classType);
            System.out.println("Date of Journey: " + dateOfJourney);
            System.out.println("From: " + from);
            System.out.println("To: " + to);

            if (confirmAction(scanner, "reserve this ticket")) {
                PreparedStatement ps = connection.prepareStatement("INSERT INTO reservations (train_number, class_type, date_of_journey, start_place, destination) VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, trainNumber);
                ps.setString(2, classType);
                ps.setString(3, dateOfJourney);
                ps.setString(4, from);
                ps.setString(5, to);
                ps.executeUpdate();

                ResultSet generatedKeys = ps.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int pnr = generatedKeys.getInt(1);
                    System.out.println("Ticket reserved successfully! Your PNR number is: " + pnr);
                    exportTicketAsPDF(pnr, trainNumber, classType, dateOfJourney, from, to);
                }
            } else {
                System.out.println("Ticket reservation aborted.");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error during ticket reservation", e);
        }
    }

    private static void cancelTicket(Connection connection, Scanner scanner) {
        try {
            System.out.print("Enter PNR Number: ");
            int pnr = validateIntegerInput(scanner);

            // Display reservation details for the provided PNR
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM reservations WHERE pnr = ?");
            ps.setInt(1, pnr);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                System.out.println("Ticket details:");
                System.out.println("PNR: " + rs.getInt("pnr"));
                System.out.println("Train Number: " + rs.getInt("train_number"));
                System.out.println("Class Type: " + rs.getString("class_type"));
                System.out.println("Date of Journey: " + rs.getString("date_of_journey"));
                System.out.println("From: " + rs.getString("start_place"));
                System.out.println("To: " + rs.getString("destination"));
                System.out.println("Are you sure you want to cancel this ticket? (yes/no)");

                String confirmation = validateStringInput(scanner).toLowerCase();
                if ("yes".equals(confirmation)) {
                    PreparedStatement psDelete = connection.prepareStatement("DELETE FROM reservations WHERE pnr = ?");
                    psDelete.setInt(1, pnr);
                    int rows = psDelete.executeUpdate();

                    if (rows > 0) {
                        System.out.println("Ticket canceled successfully!");
                    } else {
                        System.out.println("No ticket found with the provided PNR number.");
                    }
                } else {
                    System.out.println("Ticket cancellation aborted.");
                }
            } else {
                System.out.println("No ticket found with the provided PNR number.");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error during ticket cancellation", e);
        }
    }

    private static void searchTickets(Connection connection, Scanner scanner) {
        try {
            System.out.print("Enter Train Number: ");
            int trainNumber = validateIntegerInput(scanner);

            PreparedStatement ps = connection.prepareStatement("SELECT * FROM reservations WHERE train_number = ?");
            ps.setInt(1, trainNumber);
            ResultSet rs = ps.executeQuery();

            System.out.println("+------+-----------+---------+-------+-----------+");
            System.out.println("| PNR  | Class     | Date    | From  | To        |");
            System.out.println("+------+-----------+---------+-------+-----------+");
            while (rs.next()) {
                System.out.printf("| %-4d | %-9s | %-8s | %-5s | %-9s |%n",
                    rs.getInt("pnr"),
                    rs.getString("class_type"),
                    rs.getString("date_of_journey"),
                    rs.getString("start_place"),
                    rs.getString("destination"));
            }
            System.out.println("+------+-----------+---------+-------+-----------+");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error during ticket search", e);
        }
    }

    private static void exportTicketAsPDF(int pnr, int trainNumber, String classType, String dateOfJourney, String from, String to) {
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream("Ticket_PNR_" + pnr + ".pdf"));
            document.open();

            document.add(new Paragraph("Ticket Details"));
            document.add(new Paragraph("PNR: " + pnr));
            document.add(new Paragraph("Train Number: " + trainNumber));
            document.add(new Paragraph("Class: " + classType));
            document.add(new Paragraph("Date of Journey: " + dateOfJourney));
            document.add(new Paragraph("From: " + from));
            document.add(new Paragraph("To: " + to));

            document.close();
            System.out.println("Ticket exported as PDF successfully!");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error exporting ticket to PDF", e);
        }
    }

    private static boolean confirmAction(Scanner scanner, String action) {
        System.out.print("Are you sure you want to " + action + "? (yes/no): ");
        String confirmation = validateStringInput(scanner).toLowerCase();
        return "yes".equals(confirmation);
    }

    private static int validateIntegerInput(Scanner scanner) {
        while (true) {
            try {
                return scanner.nextInt();
            } catch (Exception e) {
                System.out.print("Invalid input. Please enter a number: ");
                scanner.next();
            }
        }
    }

    private static String validateStringInput(Scanner scanner) {
        while (true) {
            String input = scanner.next();
            if (!input.trim().isEmpty()) {
                return input;
            } else {
                System.out.print("Invalid input. Please enter a non-empty value: ");
            }
        }
    }

    private static String validateDateInput(Scanner scanner) {
        while (true) {
            String date = scanner.next();
            if (Pattern.matches("\\d{4}-\\d{2}-\\d{2}", date)) {
                return date;
            } else {
                System.out.print("Invalid date format. Please use YYYY-MM-DD: ");
            }
        }
    }
}
