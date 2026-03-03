package ca.cegep.gim.todo.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DatabaseConnection {

    // --- ONLINE (MariaDB distant) ---
    private static final String MARIADB_HOST = "209.222.101.116";
    private static final String MARIADB_PORT = "25383";
    private static final String MARIADB_DATABASE = "dan_tp01_simon";

    private static final String MARIADB_USER = "todo_app";
    private static final String MARIADB_PASSWORD = "TP01";

    private static final String MARIADB_URL =
            "jdbc:mariadb://" + MARIADB_HOST + ":" + MARIADB_PORT + "/" + MARIADB_DATABASE
                    + "?useUnicode=true&characterEncoding=utf8";

    private static final String H2_URL =
            "jdbc:h2:file:./data/tp01;MODE=MySQL;DATABASE_TO_LOWER=TRUE;AUTO_SERVER=TRUE";

    private DatabaseConnection() {}

    public static Connection getConnection() throws SQLException {
        if (AppConfig.getDbMode() == DbMode.LOCAL) {
            return DriverManager.getConnection(H2_URL, "sa", "");
        }
        return DriverManager.getConnection(MARIADB_URL, MARIADB_USER, MARIADB_PASSWORD);
    }
}