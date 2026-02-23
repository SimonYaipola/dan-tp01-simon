package ca.cegep.gim;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public final class DbConnectionTest {

    private static final String URL = "jdbc:mariadb://209.222.101.116:25383/dan_tp01_simon";
    private static final String USER = "todo_app";
    private static final String PASSWORD = "TP01";

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT 1")) {

            rs.next();
            System.out.println("✅ Connected! Test value = " + rs.getInt(1));

        } catch (Exception e) {
            System.out.println("❌ Connection failed:");
            e.printStackTrace();
        }
    }
}