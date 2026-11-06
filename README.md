# Inventory-Management-System
Inventory Management System using Java JDBC and MySQL
# Retail Store Inventory Management System

A Java-based Retail Store Inventory Management System using JDBC and MySQL. This project helps manage product information and inventory operations through a database-connected application.

## About the Project

The Retail Store Inventory Management System is designed to manage products in a retail store. It uses Java for application development and MySQL for storing product information. JDBC is used to establish the connection between Java and the MySQL database.

## Technologies Used

- **Programming Language:** Java
- **Database:** MySQL
- **Database Connectivity:** JDBC
- **IDE:** IntelliJ IDEA

## Features

- Add and manage product information
- View available products
- Retrieve products by category
- Update product stock
- Search and update product details
- Store product information in MySQL database

## Database Details

**Database Name:** `retail_db`

**Table Name:** `Product`

| Column Name | Data Type |
|---|---|
| product_id | INT (Primary Key) |
| product_name | VARCHAR(100) |
| category | VARCHAR(50) |
| quantity | INT |
| price | DOUBLE |

The database SQL file is available in the `database` folder.

## Project Setup

### 1. Clone the Repository

```bash
git clone https://github.com/YOUR_USERNAME/Retail-Store-Inventory-Management.git
```

### 2. Open the Project

Open the downloaded project in IntelliJ IDEA.

### 3. Configure MySQL

- Make sure MySQL Server is installed and running.
- Create or import the `retail_db` database.
- Update the database connection details in your Java JDBC connection code.

### 4. Configure JDBC

Make sure the MySQL JDBC Driver (MySQL Connector/J) is added to the project.

### 5. Run the Application

Run the main Java class from IntelliJ IDEA.

## Project Structure

```text
Retail-Store-Inventory-Management/
│
├── src/
│   └── Java source files
│
├── database/
│   └── retail_db.sql
│
└── README.md
```

## Author

Developed as a Java JDBC and MySQL project.

## License

This project is created for educational purposes.
