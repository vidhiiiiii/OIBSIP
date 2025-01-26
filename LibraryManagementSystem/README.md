# Library Management System

## Overview
This **Library Management System** is a console-based application implemented in Java, designed to simulate the functionalities
of managing a library. It includes features like adding, deleting, and updating books, managing users, viewing books, searching
for books, issuing and returning books, and generating reports. It also allows both admin and user access, providing a comprehensive
system for library operations.

The project also integrates with a MySQL database to store books and user data.

---

## Purpose
This project aims to:
- Demonstrate Java programming and object-oriented concepts.
- Show how to interact with a MySQL database using JDBC for persistence.
- Provide a learning experience in system design and database integration.
- Allow practice with handling user input through a console interface.

---

## Features

### Admin Module:
1. **Add New Book**: Add a new book to the library with title, author, category, and quantity.
2. **Delete Book**: Remove a book from the library using its ID.
3. **Update Book**: Modify the quantity of an existing book.
4. **Add New User**: Add a new user with name and email to the system.
5. **Delete User**: Remove a user from the system by user ID.
6. **Generate Report**: Generate a library report listing all books and their quantities.

### User Module:
1. **View Books**: View a list of available books in the library.
2. **Search Books**: Search for books by title.
3. **Issue Book**: Issue a book by its ID, decreasing its quantity.
4. **Return Book**: Return an issued book, increasing its quantity.
5. **Contact Support**: Get contact details for library support.

### General:
- **User Authentication**: Admin can access a special module with restricted functions.
- **Console-Based Interface**: Simple, text-based UI for interacting with the system.

---

## Technologies Used
- **Programming Language**: Java
- **Database**: MySQL
- **Tools**: Java IDE (Eclipse), MySQL Workbench.

---

## How to Run the Project

### Prerequisites
1. **Java Development Kit (JDK)** installed on your system.
2. MySQL database for storing books and users.
3. A text editor or Java IDE (e.g., IntelliJ IDEA, Eclipse).

### Steps
1. Clone the repository to your local machine:
   ```bash
   git clone https://github.com/your-username/library-management-system.git
