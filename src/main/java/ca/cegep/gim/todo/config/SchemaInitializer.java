package ca.cegep.gim.todo.config;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public final class SchemaInitializer {

    private SchemaInitializer() {}

    public static void initLocalSchemaIfNeeded() {
        if (AppConfig.getDbMode() != DbMode.LOCAL) {
            return;
        }

        try (var conn = DatabaseConnection.getConnection();
             var stmt = conn.createStatement()) {

            String sql = readResource("/schema.sql");


            for (String part : sql.split(";")) {
                String s = part.trim();
                if (!s.isEmpty()) {
                    stmt.execute(s);
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize local schema", e);
        }
    }

    private static String readResource(String path) throws Exception {
        var in = SchemaInitializer.class.getResourceAsStream(path);
        if (in == null) {
            throw new IllegalStateException("Resource not found: " + path);
        }

        StringBuilder sb = new StringBuilder();
        try (var br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append('\n');
            }
        }
        return sb.toString();
    }
}