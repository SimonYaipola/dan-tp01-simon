package ca.cegep.gim.todo.ui;

import ca.cegep.gim.todo.model.Task;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Objects;
import java.util.function.Consumer;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class TaskFormPanel extends JPanel {

    private final JTextField idField = new JTextField();
    private final JTextField titleField = new JTextField();
    private final JTextArea descriptionArea = new JTextArea(6, 20);
    private final JComboBox<String> priorityBox = new JComboBox<>(new String[]{"LOW", "MEDIUM", "HIGH"});
    private final JComboBox<String> statusBox = new JComboBox<>(new String[]{"TODO", "IN_PROGRESS", "DONE"});
    private final JTextField dueDateField = new JTextField();

    private final Consumer<Task> onAdd;
    private final Consumer<Task> onUpdate;
    private final Consumer<Integer> onDelete;

    public TaskFormPanel(Consumer<Task> onAdd, Consumer<Task> onUpdate, Consumer<Integer> onDelete) {
        this.onAdd = Objects.requireNonNull(onAdd);
        this.onUpdate = Objects.requireNonNull(onUpdate);
        this.onDelete = Objects.requireNonNull(onDelete);

        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createTitledBorder("Détails de la tâche"));

        idField.setEditable(false);

        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);

        dueDateField.setToolTipText("Format: YYYY-MM-DD (ex: 2026-02-26)");

        JButton addBtn = new JButton("Ajouter");
        JButton updateBtn = new JButton("Modifier");
        JButton deleteBtn = new JButton("Supprimer");
        JButton clearBtn = new JButton("Vider");

        addBtn.addActionListener(e -> onAdd.accept(readTaskFromForm(false)));
        updateBtn.addActionListener(e -> onUpdate.accept(readTaskFromForm(true)));
        deleteBtn.addActionListener(e -> onDelete.accept(readIdFromForm()));
        clearBtn.addActionListener(e -> clearForm());

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 8, 6, 8);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;

        int row = 0;

        add(new JLabel("ID"), gbc(c, 0, row, 1));
        add(idField, gbc(c, 1, row++, 2));

        add(new JLabel("Titre *"), gbc(c, 0, row, 1));
        add(titleField, gbc(c, 1, row++, 2));

        add(new JLabel("Description *"), gbc(c, 0, row, 1));
        c.fill = GridBagConstraints.BOTH;
        c.weighty = 1;
        add(new JScrollPane(descriptionArea), gbc(c, 1, row++, 2));
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weighty = 0;

        add(new JLabel("Priorité *"), gbc(c, 0, row, 1));
        add(priorityBox, gbc(c, 1, row++, 2));

        add(new JLabel("État *"), gbc(c, 0, row, 1));
        add(statusBox, gbc(c, 1, row++, 2));

        add(new JLabel("Date limite *"), gbc(c, 0, row, 1));
        add(dueDateField, gbc(c, 1, row++, 2));

        // Boutons
        add(addBtn, gbc(c, 0, row, 1));
        add(updateBtn, gbc(c, 1, row, 1));
        add(deleteBtn, gbc(c, 2, row++, 1));

        c.gridwidth = 3;
        add(clearBtn, gbc(c, 0, row, 3));
    }

    public void clearForm() {
        idField.setText("");
        titleField.setText("");
        descriptionArea.setText("");
        priorityBox.setSelectedItem("MEDIUM");
        statusBox.setSelectedItem("TODO");
        dueDateField.setText("");
    }

    public void fillFromTask(Task task) {
        if (task == null) {
            return;
        }
        idField.setText(String.valueOf(task.getId()));
        titleField.setText(nullToEmpty(task.getTitle()));
        descriptionArea.setText(nullToEmpty(task.getDescription()));
        priorityBox.setSelectedItem(nullToEmpty(task.getPriority()));
        statusBox.setSelectedItem(nullToEmpty(task.getStatus()));
        dueDateField.setText(task.getDueDate() != null ? task.getDueDate().toString() : "");
    }

    private int readIdFromForm() {
        try {
            String txt = idField.getText().trim();
            return txt.isEmpty() ? -1 : Integer.parseInt(txt);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private Task readTaskFromForm(boolean includeId) {
        String title = titleField.getText().trim();
        String description = descriptionArea.getText().trim();
        String priority = (String) priorityBox.getSelectedItem();
        String status = (String) statusBox.getSelectedItem();
        String dueTxt = dueDateField.getText().trim();

        if (title.isEmpty() || description.isEmpty() || dueTxt.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Titre, description et date limite sont obligatoires.", "Validation",
                    JOptionPane.WARNING_MESSAGE);
            return new Task(); // retourne un objet vide; MainFrame va refuser update sans ID, etc.
        }

        LocalDate due;
        try {
            due = LocalDate.parse(dueTxt);
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Date invalide. Format attendu: YYYY-MM-DD", "Validation",
                    JOptionPane.WARNING_MESSAGE);
            return new Task();
        }

        Task task = new Task(title, description, priority, status, due);

        if (includeId) {
            int id = readIdFromForm();
            task.setId(id);
        }

        return task;
    }

    private static GridBagConstraints gbc(GridBagConstraints base, int x, int y, int w) {
        GridBagConstraints c = (GridBagConstraints) base.clone();
        c.gridx = x;
        c.gridy = y;
        c.gridwidth = w;
        return c;
    }

    private static String nullToEmpty(String s) {
        return s == null ? "" : s;
    }
}