package com;

import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;

public class LibraryManagement {

    static final String URL =
        "jdbc:mysql://localhost:3306/library_db";
    static final String USER = "root";
    static final String PASSWORD = "Ambrishh@07";

    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        try (Connection con =
                DriverManager.getConnection(URL, USER, PASSWORD)) {

            System.out.println("Connected to Library Database!");

            int choice;

            do {
                System.out.println("\n===== LIBRARY MANAGEMENT SYSTEM =====");
                System.out.println("1. Add Book");
                System.out.println("2. View All Books");
                System.out.println("3. Search Book");
                System.out.println("4. Register Member");
                System.out.println("5. View All Members");
                System.out.println("6. Issue Book");
                System.out.println("7. Return Book");
                System.out.println("8. View Issued Books");
                System.out.println("9. Exit");
                System.out.print("Enter your choice: ");

                choice = sc.nextInt();
                sc.nextLine();

                try {
                    switch (choice) {
                        case 1:
                            addBook(con);
                            break;
                        case 2:
                            viewBooks(con);
                            break;
                        case 3:
                            searchBook(con);
                            break;
                        case 4:
                            registerMember(con);
                            break;
                        case 5:
                            viewMembers(con);
                            break;
                        case 6:
                            issueBook(con);
                            break;
                        case 7:
                            returnBook(con);
                            break;
                        case 8:
                            viewIssuedBooks(con);
                            break;
                        case 9:
                            System.out.println("Thank you!");
                            break;
                        default:
                            System.out.println("Invalid choice!");
                    }
                } catch (SQLException e) {
                    System.out.println("Database error: " + e.getMessage());
                }

            } while (choice != 9);

        } catch (SQLException e) {
            System.out.println("Connection error: " + e.getMessage());
        }

        sc.close();
    }

    // 1. Add a book
    static void addBook(Connection con) throws SQLException {

        System.out.print("Enter book name: ");
        String name = sc.nextLine();

        System.out.print("Enter author name: ");
        String author = sc.nextLine();

        System.out.print("Enter quantity: ");
        int quantity = sc.nextInt();
        sc.nextLine();

        if (quantity < 0) {
            System.out.println("Quantity cannot be negative!");
            return;
        }

        String sql = "INSERT INTO books (book_name, author, quantity) "
                   + "VALUES (?, ?, ?)";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, author);
            ps.setInt(3, quantity);
            ps.executeUpdate();

            System.out.println("Book added successfully!");
        }
    }

    // 2. View all books
    static void viewBooks(Connection con) throws SQLException {

        String sql = "SELECT * FROM books";

        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                System.out.println(
                    "ID: " + rs.getInt("book_id")
                    + " | Name: " + rs.getString("book_name")
                    + " | Author: " + rs.getString("author")
                    + " | Available: " + rs.getInt("quantity")
                );
            }
        }
    }

    // 3. Search a book by ID
    static void searchBook(Connection con) throws SQLException {

        System.out.print("Enter book ID: ");
        int id = sc.nextInt();
        sc.nextLine();

        String sql = "SELECT * FROM books WHERE book_id = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    System.out.println("Book: " + rs.getString("book_name"));
                    System.out.println("Author: " + rs.getString("author"));
                    System.out.println("Available: " + rs.getInt("quantity"));
                } else {
                    System.out.println("Book not found!");
                }
            }
        }
    }

    // 4. Register a member
    static void registerMember(Connection con) throws SQLException {

        System.out.print("Enter member name: ");
        String name = sc.nextLine();

        System.out.print("Enter phone number: ");
        String phone = sc.nextLine();

        String sql = "INSERT INTO members (member_name, phone) VALUES (?, ?)";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, phone);
            ps.executeUpdate();

            System.out.println("Member registered successfully!");
        }
    }

    // 5. View all members
    static void viewMembers(Connection con) throws SQLException {

        String sql = "SELECT * FROM members";

        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                System.out.println(
                    "Member ID: " + rs.getInt("member_id")
                    + " | Name: " + rs.getString("member_name")
                    + " | Phone: " + rs.getString("phone")
                );
            }
        }
    }

    // 6. Issue a book
    static void issueBook(Connection con) throws SQLException {

        System.out.print("Enter book ID: ");
        int bookId = sc.nextInt();

        System.out.print("Enter member ID: ");
        int memberId = sc.nextInt();
        sc.nextLine();

        boolean oldAutoCommit = con.getAutoCommit();
        con.setAutoCommit(false);

        try {
            int quantity;

            String checkBook = "SELECT quantity FROM books "
                             + "WHERE book_id = ? FOR UPDATE";

            try (PreparedStatement ps = con.prepareStatement(checkBook)) {
                ps.setInt(1, bookId);

                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        throw new SQLException("Book ID not found!");
                    }
                    quantity = rs.getInt("quantity");
                }
            }

            if (quantity <= 0) {
                throw new SQLException("No copies available!");
            }

            String checkMember =
                "SELECT member_id FROM members WHERE member_id = ?";

            try (PreparedStatement ps = con.prepareStatement(checkMember)) {
                ps.setInt(1, memberId);

                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        throw new SQLException("Member ID not found!");
                    }
                }
            }

            String issueSql =
                "INSERT INTO book_issues "
                + "(book_id, member_id, issue_date, return_date) "
                + "VALUES (?, ?, CURDATE(), NULL)";

            try (PreparedStatement ps = con.prepareStatement(issueSql)) {
                ps.setInt(1, bookId);
                ps.setInt(2, memberId);
                ps.executeUpdate();
            }

            String updateSql =
                "UPDATE books SET quantity = quantity - 1 WHERE book_id = ?";

            try (PreparedStatement ps = con.prepareStatement(updateSql)) {
                ps.setInt(1, bookId);
                ps.executeUpdate();
            }

            con.commit();
            System.out.println("Book issued successfully!");

        } catch (SQLException e) {
            con.rollback();
            throw e;
        } finally {
            con.setAutoCommit(oldAutoCommit);
        }
    }

    // 7. Return a book and calculate fine
    static void returnBook(Connection con) throws SQLException {

        System.out.print("Enter issue ID: ");
        int issueId = sc.nextInt();
        sc.nextLine();

        boolean oldAutoCommit = con.getAutoCommit();
        con.setAutoCommit(false);

        try {
            int bookId;
            LocalDate issueDate;

            String sql =
                "SELECT book_id, issue_date, return_date "
                + "FROM book_issues WHERE issue_id = ? FOR UPDATE";

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, issueId);

                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        throw new SQLException("Issue ID not found!");
                    }

                    if (rs.getDate("return_date") != null) {
                        throw new SQLException("This book was already returned!");
                    }

                    bookId = rs.getInt("book_id");
                    issueDate = rs.getDate("issue_date").toLocalDate();
                }
            }

            LocalDate today = LocalDate.now();
            long overdueDays = Math.max(
                0, ChronoUnit.DAYS.between(issueDate.plusDays(14), today)
            );

            long fine = overdueDays * 5;

            String updateIssue =
                "UPDATE book_issues SET return_date = CURDATE() "
                + "WHERE issue_id = ? AND return_date IS NULL";

            try (PreparedStatement ps = con.prepareStatement(updateIssue)) {
                ps.setInt(1, issueId);

                if (ps.executeUpdate() != 1) {
                    throw new SQLException("Return could not be completed!");
                }
            }

            String updateBook =
                "UPDATE books SET quantity = quantity + 1 WHERE book_id = ?";

            try (PreparedStatement ps = con.prepareStatement(updateBook)) {
                ps.setInt(1, bookId);
                ps.executeUpdate();
            }

            con.commit();

            System.out.println("Book returned successfully!");
            System.out.println("Overdue days: " + overdueDays);
            System.out.println("Fine amount: Rs. " + fine);

        } catch (SQLException e) {
            con.rollback();
            throw e;
        } finally {
            con.setAutoCommit(oldAutoCommit);
        }
    }

    // 8. View issued and returned books
    static void viewIssuedBooks(Connection con) throws SQLException {

        String sql =
            "SELECT bi.issue_id, b.book_name, m.member_name, "
            + "bi.issue_date, bi.return_date "
            + "FROM book_issues bi "
            + "JOIN books b ON bi.book_id = b.book_id "
            + "JOIN members m ON bi.member_id = m.member_id "
            + "ORDER BY bi.issue_id";

        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                System.out.println(
                    "Issue ID: " + rs.getInt("issue_id")
                    + " | Book: " + rs.getString("book_name")
                    + " | Member: " + rs.getString("member_name")
                    + " | Issued: " + rs.getDate("issue_date")
                    + " | Returned: " + rs.getDate("return_date")
                );
            }
        }
    }
}