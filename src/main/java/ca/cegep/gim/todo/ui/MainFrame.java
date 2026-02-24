package ca.cegep.gim.todo.ui;

import ca.cegep.gim.todo.dao.TaskDao;
import ca.cegep.gim.todo.model.Task;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

public class MainFrame extends JFrame {

    private final TaskDao taskDao;
    private final TaskFormPanel formPanel;
    private final TaskTablePanel tablePanel;

    public MainFrame() {
        super("Mini gestionnaire de tâches - TP01");

        this.taskDao = new TaskDao();
        this.formPanel = new TaskFormPanel(this::handleAdd, this::handleUpdate, this::handleDelete);
        this.tablePanel = new TaskTablePanel(this::handleRowSelected);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout()); // JFrame -> BorderLayout (WEST/CENTER)
        setJMenuBar(buildMenuBar());

        formPanel.setPreferredSize(new Dimension(360, 600));
        add(formPanel, BorderLayout.WEST);
        add(tablePanel, BorderLayout.CENTER);

        refreshTable();
    }

    private JMenuBar buildMenuBar() {
        JMenuBar bar = new JMenuBar();

        JMenu file = new JMenu("Fichier");
        JMenuItem refresh = new JMenuItem("Rafraîchir");
        refresh.addActionListener(e -> refreshTable());

        JMenuItem quit = new JMenuItem("Quitter");
        quit.addActionListener(e -> dispose());

        file.add(refresh);
        file.addSeparator();
        file.add(quit);

        JMenu edit = new JMenu("Édition");
        JMenuItem clear = new JMenuItem("Vider le formulaire");
        clear.addActionListener(e -> formPanel.clearForm());
        edit.add(clear);

        JMenu help = new JMenu("Aide");
        JMenuItem about = new JMenuItem("À propos");
        about.addActionListener(e -> JOptionPane.showMessageDialog(
                this,
                "TP01 - Mini gestionnaire de tâches\nSwing + MariaDB",
                "À propos",
                JOptionPane.INFORMATION_MESSAGE
        ));
        JMenuItem info = new JMenuItem("Me contacter");
        info.addActionListener(e-> JOptionPane.showMessageDialog(
                this,
                "https://github.com/SimonYaipola",
                "Liens Github",
                JOptionPane.INFORMATION_MESSAGE
        ));

        help.add(about);
        help.add(info);
        bar.add(file);
        bar.add(edit);
        bar.add(help);

        return bar;
    }

    private void refreshTable() {
        List<Task> tasks = taskDao.getAllTasks();
        tablePanel.setTasks(tasks);
    }

    // --- Callbacks venant du FormPanel (boutons) ---

    private void handleAdd(Task task) {
        int id = taskDao.addTask(task);
        if (id > 0) {
            formPanel.clearForm();
            refreshTable();
        } else {
            JOptionPane.showMessageDialog(this, "Impossible d'ajouter la tâche.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleUpdate(Task task) {
        if (task.getId() <= 0) {
            JOptionPane.showMessageDialog(this, "Sélectionne une tâche dans la table avant de modifier.", "Info",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        boolean ok = taskDao.updateTask(task);
        if (ok) {
            formPanel.clearForm();
            refreshTable();
        } else {
            JOptionPane.showMessageDialog(this, "Aucune tâche modifiée (ID introuvable?).", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleDelete(int id) {
        if (id <= 0) {
            JOptionPane.showMessageDialog(this, "Sélectionne une tâche dans la table avant de supprimer.", "Info",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Supprimer la tâche #" + id + " ?",
                "Confirmation",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            boolean ok = taskDao.deleteTask(id);
            if (ok) {
                formPanel.clearForm();
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(this, "Suppression échouée (ID introuvable?).", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // --- Callback venant du TablePanel (sélection de ligne) ---
    private void handleRowSelected(Task task) {
        formPanel.fillFromTask(task);
    }
}