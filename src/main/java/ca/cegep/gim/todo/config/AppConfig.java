package ca.cegep.gim.todo.config;

public final class AppConfig {

    private static DbMode dbMode = DbMode.ONLINE;

    private AppConfig() {}

    public static DbMode getDbMode() {
        return dbMode;
    }

    public static void setDbMode(DbMode mode) {
        if (mode == null) {
            throw new IllegalArgumentException("DbMode cannot be null");
        }
        dbMode = mode;
    }
}