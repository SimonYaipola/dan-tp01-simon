package ca.cegep.gim.todo.dao;

import ca.cegep.gim.todo.config.DatabaseConnection;
import ca.cegep.gim.todo.model.Task;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TaskDao {

    public int addTask(Task task) {
        String sql = """
                INSERT INTO task (title, description, priority, status, due_date)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (var conn = DatabaseConnection.getConnection();
             var stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, task.getTitle());
            stmt.setString(2, task.getDescription());
            stmt.setString(3, task.getPriority());
            stmt.setString(4, task.getStatus());
            stmt.setDate(5, Date.valueOf(task.getDueDate()));

            stmt.executeUpdate();

            try (var keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
            return -1;

        } catch (Exception e) {
            throw new RuntimeException("Failed to add task", e);
        }
    }

    public List<Task> getAllTasks() {
        String sql = """
                SELECT id, title, description, priority, status, due_date, created_at, updated_at
                FROM task
                ORDER BY due_date ASC, id DESC
                """;

        List<Task> tasks = new ArrayList<>();

        try (var conn = DatabaseConnection.getConnection();
             var stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                tasks.add(mapRowToTask(rs));
            }
            return tasks;

        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch tasks", e);
        }
    }

    public boolean updateTask(Task task) {
        String sql = """
                UPDATE task
                SET title = ?, description = ?, priority = ?, status = ?, due_date = ?
                WHERE id = ?
                """;

        try (var conn = DatabaseConnection.getConnection();
             var stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, task.getTitle());
            stmt.setString(2, task.getDescription());
            stmt.setString(3, task.getPriority());
            stmt.setString(4, task.getStatus());
            stmt.setDate(5, Date.valueOf(task.getDueDate()));
            stmt.setInt(6, task.getId());

            return stmt.executeUpdate() == 1;

        } catch (Exception e) {
            throw new RuntimeException("Failed to update task id=" + task.getId(), e);
        }
    }

    public boolean deleteTask(int id) {
        String sql = "DELETE FROM task WHERE id = ?";

        try (var conn = DatabaseConnection.getConnection();
             var stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() == 1;

        } catch (Exception e) {
            throw new RuntimeException("Failed to delete task id=" + id, e);
        }
    }

    private static Task mapRowToTask(ResultSet rs) throws Exception {
        Task task = new Task();

        task.setId(rs.getInt("id"));
        task.setTitle(rs.getString("title"));
        task.setDescription(rs.getString("description"));
        task.setPriority(rs.getString("priority"));
        task.setStatus(rs.getString("status"));

        Date due = rs.getDate("due_date");
        task.setDueDate(due != null ? due.toLocalDate() : LocalDate.now());

        var created = rs.getTimestamp("created_at");
        if (created != null) {
            task.setCreatedAt(created.toLocalDateTime());
        }

        var updated = rs.getTimestamp("updated_at");
        if (updated != null) {
            task.setUpdatedAt(updated.toLocalDateTime());
        }

        return task;
    }
}