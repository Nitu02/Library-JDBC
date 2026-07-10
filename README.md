# 📚 Library Management System

A console-based Library Management System developed using **Java**, **JDBC**, and **MySQL**. This project demonstrates object-oriented programming, JDBC connectivity, SQL operations, transaction management, and report generation.

---

## 🚀 Features

### 👤 User Management

- Add User
- View Users
- Search User by ID
- Search User by Name
- Delete User
- View Total Users

### 📖 Book Management

- Add Book
- View Books
- Search Book by ID
- Search Book by Title
- Search Book by Author
- Delete Book
- View Total Books

### 🔄 Book Transactions

- Issue Book
- Return Book
- Automatic Due Date (15 Days)
- Fine Calculation (₹5 per day)
- Transaction Management using Commit & Rollback

### 📊 Reports

- View Available Books
- View Currently Issued Books
- View Issue History
- View Overdue Books
- Top Readers
- Most Issued Books
- Library Dashboard

---

# 🛠 Technologies Used

- Java
- JDBC
- MySQL
- VS Code
- Git & GitHub

---

# 📂 Project Structure

```
LibraryManagementSystem/
│
├── src/
│   ├── dao/
│   │   ├── UserDAO.java
│   │   ├── BookDAO.java
│   │   └── IssuedBookDAO.java
│   │
│   ├── model/
│   │   ├── User.java
│   │   └── Book.java
│   │
│   ├── util/
│   │   └── DBConnection.java
│   │
│   └── Main.java
│
├── library_db.sql
├── README.md
└── .gitignore
```

---

# 🗄 Database

Database Name

```
library_db
```

Tables

- users
- books
- issued_books

---

# ⚙ Database Configuration

Update the following values inside **DBConnection.java**

```java
private static final String URL =
"jdbc:mysql://localhost:3306/library_db";

private static final String USER = "root";

private static final String PASSWORD = "YourPassword";
```

---

# ▶ How to Run

### Clone Repository

```
git clone https://github.com/YOUR_USERNAME/Library-Management-System.git
```

### Open Project

Open the project using VS Code or IntelliJ IDEA.

### Create Database

Run

```
library_db.sql
```

inside MySQL Workbench.

### Update Database Credentials

Modify

```
DBConnection.java
```

with your MySQL username and password.

### Run

Execute

```
Main.java
```

---

# 📊 Dashboard

The project provides a dashboard displaying

- Total Users
- Total Books
- Available Books
- Issued Books
- Overdue Books
- Top Reader
- Most Issued Book

---

# 📷 Sample Output

```
============== LIBRARY DASHBOARD ==============

Total Users           : 12
Total Books           : 35
Available Books       : 24
Currently Issued      : 11
Overdue Books         : 2

Top Reader            : Rahul (8 Books)

Most Issued Book      : Java Programming (12 Times)

===============================================
```

---

# 📚 JDBC Concepts Covered

- JDBC Connection
- PreparedStatement
- ResultSet
- SQL CRUD Operations
- SQL JOIN
- GROUP BY
- ORDER BY
- COUNT
- LIKE
- Transactions
- Commit
- Rollback
- Exception Handling

---

# 🎯 Learning Outcomes

This project helped in understanding

- Object-Oriented Programming
- Java Collections
- JDBC API
- MySQL Integration
- SQL Query Writing
- Transaction Management
- Report Generation
- DAO Design Pattern

---

# 🔮 Future Enhancements

- Spring Boot Backend
- React Frontend
- Authentication & Authorization
- Email Notifications
- Barcode Scanner
- Book Reservation
- REST APIs

---

# 👨‍💻 Author

**Nitu Sangwan**

GitHub: https://github.com/Nitu02
