# Online Reservation System

## Overview
This project is a **learning-based Online Reservation System** built in Java. It simulates an online train reservation system, where users can perform actions such as reserving tickets, canceling tickets, and viewing ticket details.

The system includes features such as user authentication (registration and login), reservation of train tickets, cancellation, and the option to search for tickets by train number. It also allows exporting ticket details to a PDF file.

---

## Purpose
The goal of this project is to:
- Achieve hands-on experience in Java programming.
- Implement object-oriented principles with real-world concepts.
- Practice using JDBC for database interaction.
- Learn how to work with external libraries (e.g., iText for PDF generation).

---

## Features
1. **User Authentication**  
   - Register and login functionality to securely manage user access.

2. **Reservation Operations**  
   - **Reserve Ticket**: Reserve a ticket for a train based on user input for train number, class type, date, and travel details.
   - **Cancel Ticket**: Cancel a reservation using the PNR number.
   - **Search Tickets**: Search and view tickets reserved by train number.

3. **Export to PDF**  
   - Reservation details are exported to a PDF file upon successful reservation.

4. **Database Integration**  
   - JDBC is used to interact with a MySQL database for storing user details, reservations, and train data.

5. **Console-Based Interface**  
   - A simple text-based interface for ease of use and understanding.

---

## Technologies Used
- **Programming Language**: Java
- **Database**: MySQL (JDBC)
- **Libraries**: iText for generating PDFs
- **Tools**: Java IDE (Eclipse), MySQL, Git for version control

---

## How to Run the Project

### Prerequisites
1. **Java Development Kit (JDK)** installed on your system.
2. A **MySQL database** setup (or any SQL-compatible database).
3. **iText PDF Library** added to your project.
4. A text editor or IDE (e.g., IntelliJ IDEA, Eclipse).

### Steps
1. Clone the repository to your local machine:
   ```bash
   git clone https://github.com/your-username/online-reservation-system.git
