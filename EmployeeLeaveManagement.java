
	package com;

	import java.sql.*;
	import java.util.Scanner;
	import java.time.LocalDate;
	import java.time.temporal.ChronoUnit;

	public class EmployeeLeaveManagement {

	    static final String URL =
	        "jdbc:mysql://localhost:3306/employee_leave_db";
	    static final String USER = "root";
	    static final String PASSWORD = "Ambrishh@07";

	    static Scanner sc = new Scanner(System.in);

	    public static void main(String[] args) {

	        try {
	            Class.forName("com.mysql.cj.jdbc.Driver");

	            while (true) {
	                System.out.println("\n=== EMPLOYEE LEAVE MANAGEMENT ===");
	                System.out.println("1. Register Employee");
	                System.out.println("2. View Employees");
	                System.out.println("3. Apply for Leave");
	                System.out.println("4. Approve/Reject Leave");
	                System.out.println("5. View Leave Requests");
	                System.out.println("6. Check Leave Balance");
	                System.out.println("7. Exit");
	                System.out.print("Enter your choice: ");

	                int choice = sc.nextInt();
	                sc.nextLine();

	                switch (choice) {
	                    case 1:
	                        registerEmployee();
	                        break;
	                    case 2:
	                        viewEmployees();
	                        break;
	                    case 3:
	                        applyLeave();
	                        break;
	                    case 4:
	                        updateLeaveStatus();
	                        break;
	                    case 5:
	                        viewLeaveRequests();
	                        break;
	                    case 6:
	                        checkLeaveBalance();
	                        break;
	                    case 7:
	                        System.out.println("Thank you!");
	                        return;
	                    default:
	                        System.out.println("Invalid choice!");
	                }
	            }

	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }

	    static Connection getConnection() throws SQLException {
	        return DriverManager.getConnection(URL, USER, PASSWORD);
	    }

	    static void registerEmployee() {

	        try (Connection con = getConnection()) {

	            System.out.print("Enter employee name: ");
	            String name = sc.nextLine();

	            System.out.print("Enter email: ");
	            String email = sc.nextLine();

	            System.out.print("Enter department: ");
	            String department = sc.nextLine();

	            String sql = "INSERT INTO employees "
	                       + "(employee_name, email, department) "
	                       + "VALUES (?, ?, ?)";

	            try (PreparedStatement ps =
	                     con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

	                ps.setString(1, name);
	                ps.setString(2, email);
	                ps.setString(3, department);
	                ps.executeUpdate();

	                try (ResultSet rs = ps.getGeneratedKeys()) {
	                    if (rs.next()) {
	                        int employeeId = rs.getInt(1);

	                        String balanceSql =
	                            "INSERT INTO leave_balance "
	                          + "(employee_id, total_leaves, leaves_taken) "
	                          + "VALUES (?, 12, 0)";

	                        try (PreparedStatement bs =
	                                 con.prepareStatement(balanceSql)) {
	                            bs.setInt(1, employeeId);
	                            bs.executeUpdate();
	                        }
	                    }
	                }
	            }

	            System.out.println("Employee registered successfully!");

	        } catch (SQLException e) {
	            System.out.println("Error: " + e.getMessage());
	        }
	    }

	    static void viewEmployees() {

	        String sql = "SELECT * FROM employees";

	        try (Connection con = getConnection();
	             Statement st = con.createStatement();
	             ResultSet rs = st.executeQuery(sql)) {

	            while (rs.next()) {
	                System.out.println(
	                    rs.getInt("employee_id") + " | "
	                  + rs.getString("employee_name") + " | "
	                  + rs.getString("email") + " | "
	                  + rs.getString("department"));
	            }

	        } catch (SQLException e) {
	            System.out.println("Error: " + e.getMessage());
	        }
	    }

	    static void applyLeave() {

	        Connection con = null;

	        try {
	            con = getConnection();
	            con.setAutoCommit(false);

	            System.out.print("Enter employee ID: ");
	            int employeeId = sc.nextInt();
	            sc.nextLine();

	            System.out.print("Enter start date (YYYY-MM-DD): ");
	            LocalDate startDate = LocalDate.parse(sc.nextLine());

	            System.out.print("Enter end date (YYYY-MM-DD): ");
	            LocalDate endDate = LocalDate.parse(sc.nextLine());

	            System.out.print("Enter reason: ");
	            String reason = sc.nextLine();

	            if (endDate.isBefore(startDate)) {
	                System.out.println("End date cannot be before start date.");
	                con.rollback();
	                return;
	            }

	            long days = ChronoUnit.DAYS.between(startDate, endDate) + 1;

	            String balanceSql =
	                "SELECT total_leaves, leaves_taken FROM leave_balance "
	              + "WHERE employee_id = ? FOR UPDATE";

	            try (PreparedStatement bs = con.prepareStatement(balanceSql)) {
	                bs.setInt(1, employeeId);

	                try (ResultSet rs = bs.executeQuery()) {

	                    if (!rs.next()) {
	                        System.out.println("Employee not found.");
	                        con.rollback();
	                        return;
	                    }

	                    int available =
	                        rs.getInt("total_leaves") - rs.getInt("leaves_taken");

	                    if (days > available) {
	                        System.out.println("Not enough leave balance.");
	                        con.rollback();
	                        return;
	                    }
	                }
	            }

	            String sql = "INSERT INTO leave_requests "
	                       + "(employee_id, start_date, end_date, reason, status) "
	                       + "VALUES (?, ?, ?, ?, 'PENDING')";

	            try (PreparedStatement ps = con.prepareStatement(sql)) {
	                ps.setInt(1, employeeId);
	                ps.setDate(2, Date.valueOf(startDate));
	                ps.setDate(3, Date.valueOf(endDate));
	                ps.setString(4, reason);
	                ps.executeUpdate();
	            }

	            con.commit();
	            System.out.println("Leave applied successfully. Status: PENDING");

	        } catch (Exception e) {
	            try {
	                if (con != null) con.rollback();
	            } catch (SQLException ex) {
	                System.out.println(ex.getMessage());
	            }

	            System.out.println("Error: " + e.getMessage());

	        } finally {
	            try {
	                if (con != null) con.close();
	            } catch (SQLException e) {
	                System.out.println(e.getMessage());
	            }
	        }
	    }

	    static void updateLeaveStatus() {

	        Connection con = null;

	        try {
	            con = getConnection();
	            con.setAutoCommit(false);

	            System.out.print("Enter leave request ID: ");
	            int leaveId = sc.nextInt();
	            sc.nextLine();

	            System.out.print("Enter APPROVED or REJECTED: ");
	            String status = sc.nextLine().trim().toUpperCase();

	            if (!status.equals("APPROVED") && !status.equals("REJECTED")) {
	                System.out.println("Invalid status.");
	                con.rollback();
	                return;
	            }

	            String findSql =
	                "SELECT employee_id, start_date, end_date, status "
	              + "FROM leave_requests WHERE leave_id = ? FOR UPDATE";

	            int employeeId;
	            LocalDate startDate;
	            LocalDate endDate;
	            String oldStatus;

	            try (PreparedStatement ps = con.prepareStatement(findSql)) {
	                ps.setInt(1, leaveId);

	                try (ResultSet rs = ps.executeQuery()) {
	                    if (!rs.next()) {
	                        System.out.println("Leave request not found.");
	                        con.rollback();
	                        return;
	                    }

	                    employeeId = rs.getInt("employee_id");
	                    startDate = rs.getDate("start_date").toLocalDate();
	                    endDate = rs.getDate("end_date").toLocalDate();
	                    oldStatus = rs.getString("status");
	                }
	            }

	            if (!oldStatus.equals("PENDING")) {
	                System.out.println("This request has already been processed.");
	                con.rollback();
	                return;
	            }

	            if (status.equals("APPROVED")) {
	                long days =
	                    ChronoUnit.DAYS.between(startDate, endDate) + 1;

	                String balanceSql =
	                    "UPDATE leave_balance SET leaves_taken = leaves_taken + ? "
	                  + "WHERE employee_id = ? "
	                  + "AND total_leaves - leaves_taken >= ?";

	                try (PreparedStatement bs = con.prepareStatement(balanceSql)) {
	                    bs.setLong(1, days);
	                    bs.setInt(2, employeeId);
	                    bs.setLong(3, days);

	                    if (bs.executeUpdate() == 0) {
	                        System.out.println("Insufficient leave balance.");
	                        con.rollback();
	                        return;
	                    }
	                }
	            }

	            String updateSql =
	                "UPDATE leave_requests SET status = ? WHERE leave_id = ?";

	            try (PreparedStatement ps = con.prepareStatement(updateSql)) {
	                ps.setString(1, status);
	                ps.setInt(2, leaveId);
	                ps.executeUpdate();
	            }

	            con.commit();
	            System.out.println("Leave request " + status.toLowerCase() + ".");

	        } catch (Exception e) {
	            try {
	                if (con != null) con.rollback();
	            } catch (SQLException ex) {
	                System.out.println(ex.getMessage());
	            }

	            System.out.println("Error: " + e.getMessage());

	        } finally {
	            try {
	                if (con != null) con.close();
	            } catch (SQLException e) {
	                System.out.println(e.getMessage());
	            }
	        }
	    }

	    static void viewLeaveRequests() {

	        String sql =
	            "SELECT l.leave_id, e.employee_name, l.start_date, "
	          + "l.end_date, l.reason, l.status "
	          + "FROM leave_requests l "
	          + "JOIN employees e ON l.employee_id = e.employee_id";

	        try (Connection con = getConnection();
	             Statement st = con.createStatement();
	             ResultSet rs = st.executeQuery(sql)) {

	            while (rs.next()) {
	                System.out.println(
	                    "Request ID: " + rs.getInt("leave_id")
	                  + " | Employee: " + rs.getString("employee_name")
	                  + " | Start: " + rs.getDate("start_date")
	                  + " | End: " + rs.getDate("end_date")
	                  + " | Reason: " + rs.getString("reason")
	                  + " | Status: " + rs.getString("status"));
	            }

	        } catch (SQLException e) {
	            System.out.println("Error: " + e.getMessage());
	        }
	    }

	    static void checkLeaveBalance() {

	        System.out.print("Enter employee ID: ");
	        int employeeId = sc.nextInt();
	        sc.nextLine();

	        String sql =
	            "SELECT e.employee_name, b.total_leaves, b.leaves_taken "
	          + "FROM employees e "
	          + "JOIN leave_balance b ON e.employee_id = b.employee_id "
	          + "WHERE e.employee_id = ?";

	        try (Connection con = getConnection();
	             PreparedStatement ps = con.prepareStatement(sql)) {

	            ps.setInt(1, employeeId);

	            try (ResultSet rs = ps.executeQuery()) {
	                if (rs.next()) {
	                    int total = rs.getInt("total_leaves");
	                    int taken = rs.getInt("leaves_taken");

	                    System.out.println("Employee: "
	                        + rs.getString("employee_name"));
	                    System.out.println("Total leaves: " + total);
	                    System.out.println("Leaves taken: " + taken);
	                    System.out.println("Available leaves: " + (total - taken));
	                } else {
	                    System.out.println("Employee not found.");
	                }
	            }

	        } catch (SQLException e) {
	            System.out.println("Error: " + e.getMessage());
	        }
	    }
	}

