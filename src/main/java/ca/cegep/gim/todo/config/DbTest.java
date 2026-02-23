package ca.cegep.gim.todo;

import ca.cegep.gim.todo.config.DatabaseConnection;
import java.sql.ResultSet;
import java.sql.Statement;

public final class DbTest {

    public static void main(String[] args) {
        try (var conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT 1")) {

            rs.next();
            System.out.println(" Connected via DatabaseConnection! Value = " + rs.getInt(1));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}