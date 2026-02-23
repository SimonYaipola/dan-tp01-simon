package ca.cegep.gim.todo.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DatabaseConnection {

    private static final String HOST = "209.222.101.116";
    private static final String PORT = "25383";
    private static final String DATABASE = "dan_tp01_simon";

    private static final String USER = "todo_app";
    private static final String PASSWORD = "TP01";

    private static final String URL =
            "jdbc:mariadb://" + HOST + ":" + PORT + "/" + DATABASE
                    + "?useUnicode=true&characterEncoding=utf8";

    private DatabaseConnection() {
        // Utility class
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}