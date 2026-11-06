
package org.kiet;

import java.sql.*;
import java.util.Scanner;

public class InventoryManagement {

    static final String URL =
            "jdbc:mysql://localhost:3306/retail_db";
    static final String USER = "root";
    static final String PASSWORD = "root";

    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        try (Connection con =
                     DriverManager.getConnection(URL, USER, PASSWORD)) {

            System.out.println("MySQL Connected Successfully!");

            int choice;

            do {
                System.out.println("\n===== INVENTORY MANAGEMENT =====");
                System.out.println("1. Insert 3 Products");
                System.out.println("2. Retrieve Products by Category");
                System.out.println("3. Update Product Stock");
                System.out.println("4. Search and Update Product Price");
                System.out.println("5. Total Inventory Value and Delete");
                System.out.println("6. Exit");
                System.out.print("Enter your choice: ");

                choice = sc.nextInt();
                sc.nextLine();

                switch (choice) {
                    case 1:
                        insertProducts(con);
                        break;

                    case 2:
                        retrieveProducts(con);
                        break;

                    case 3:
                        updateStock(con);
                        break;

                    case 4:
                        updatePrice(con);
                        break;

                    case 5:
                        inventoryValueAndDelete(con);
                        break;

                    case 6:
                        System.out.println("Program ended.");
                        break;

                    default:
                        System.out.println("Invalid choice!");
                }

            } while (choice != 6);

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        } finally {
            sc.close();
        }
    }

    // (a) Insert three products using PreparedStatement
    static void insertProducts(Connection con) throws SQLException {

        String sql = "INSERT INTO Product " +
                "(product_id, product_name, category, quantity, price) " +
                "VALUES (?, ?, ?, ?, ?)";

        Object[][] products = {
                {101, "Laptop", "Electronics", 10, 55000.0},
                {102, "Mouse", "Electronics", 15, 500.0},
                {103, "Chair", "Furniture", 25, 2500.0}
        };

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            for (Object[] p : products) {
                ps.setInt(1, (Integer) p[0]);
                ps.setString(2, (String) p[1]);
                ps.setString(3, (String) p[2]);
                ps.setInt(4, (Integer) p[3]);
                ps.setDouble(5, (Double) p[4]);

                try {
                    ps.executeUpdate();
                    System.out.println("Inserted: " + p[1]);
                } catch (SQLIntegrityConstraintViolationException e) {
                    System.out.println("Duplicate product ID: " + p[0]);
                }
            }
        }
    }

    // (b) Retrieve category products with quantity below 20
    // Sort price in descending order
    static void retrieveProducts(Connection con) throws SQLException {

        System.out.print("Enter category: ");
        String category = sc.nextLine();

        String sql = "SELECT * FROM Product " +
                "WHERE category = ? AND quantity < 20 " +
                "ORDER BY price DESC";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, category);

            try (ResultSet rs = ps.executeQuery()) {
                boolean found = false;

                while (rs.next()) {
                    found = true;
                    displayProduct(rs);
                }

                if (!found) {
                    System.out.println("No matching products found.");
                }
            }
        }
    }

    // (c) Add stock to an existing product
    static void updateStock(Connection con) throws SQLException {

        System.out.print("Enter product ID: ");
        int id = sc.nextInt();

        System.out.print("Enter quantity to add: ");
        int qty = sc.nextInt();
        sc.nextLine();

        if (qty <= 0) {
            System.out.println("Quantity must be positive.");
            return;
        }

        String sql = "UPDATE Product " +
                "SET quantity = quantity + ? WHERE product_id = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, qty);
            ps.setInt(2, id);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Stock updated successfully.");
            } else {
                System.out.println("Product does not exist.");
            }
        }
    }

    // (d) Search product and update its price
    static void updatePrice(Connection con) throws SQLException {

        System.out.print("Enter product ID: ");
        int id = sc.nextInt();

        System.out.print("Enter new price: ");
        double price = sc.nextDouble();
        sc.nextLine();

        String search = "SELECT * FROM Product WHERE product_id = ?";

        try (PreparedStatement ps = con.prepareStatement(search)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    System.out.println("Product not found.");
                    return;
                }

                System.out.println("Existing product details:");
                displayProduct(rs);
            }
        }

        if (price <= 0) {
            System.out.println("Price must be greater than zero.");
            return;
        }

        String update = "UPDATE Product SET price = ? " +
                "WHERE product_id = ?";

        try (PreparedStatement ps = con.prepareStatement(update)) {
            ps.setDouble(1, price);
            ps.setInt(2, id);
            ps.executeUpdate();
        }

        System.out.println("Updated product details:");

        try (PreparedStatement ps = con.prepareStatement(search)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    displayProduct(rs);
                }
            }
        }
    }

    // (e) Calculate total inventory value and delete only zero-stock products
    static void inventoryValueAndDelete(Connection con)
            throws SQLException {

        String sql = "SELECT SUM(quantity * price) AS total FROM Product";

        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                System.out.println("Total inventory value: "
                        + rs.getDouble("total"));
            }
        }

        System.out.print("Enter product ID to delete: ");
        int id = sc.nextInt();
        sc.nextLine();

        String delete = "DELETE FROM Product " +
                "WHERE product_id = ? AND quantity = 0";

        try (PreparedStatement ps = con.prepareStatement(delete)) {
            ps.setInt(1, id);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Product deleted successfully.");
            } else {
                System.out.println(
                        "Cannot delete: product not found or quantity is not zero.");
            }
        }
    }

    // Display product details
    static void displayProduct(ResultSet rs) throws SQLException {

        System.out.println(
                "ID: " + rs.getInt("product_id") +
                        ", Name: " + rs.getString("product_name") +
                        ", Category: " + rs.getString("category") +
                        ", Quantity: " + rs.getInt("quantity") +
                        ", Price: " + rs.getDouble("price"));
    }
}
