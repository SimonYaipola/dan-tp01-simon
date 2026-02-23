package ca.cegep.gim.todo;

import ca.cegep.gim.todo.dao.TaskDao;
import ca.cegep.gim.todo.model.Task;

import java.time.LocalDate;

public final class DaoTest {

    public static void main(String[] args) {
        TaskDao dao = new TaskDao();

        Task t = new Task(
                "Test DAO",
                "Valider INSERT + SELECT",
                "HIGH",
                "TODO",
                LocalDate.now().plusDays(3)
        );

        int id = dao.addTask(t);
        System.out.println("Inserted id = " + id);

        dao.getAllTasks().forEach(System.out::println);
    }
}