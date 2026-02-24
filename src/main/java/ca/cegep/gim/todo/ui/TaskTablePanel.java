package ca.cegep.gim.todo.ui;

import ca.cegep.gim.todo.model.Task;

import java.awt.BorderLayout;
import java.util.List;
import java.util.function.Consumer;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.Color;
import java.awt.Component;
import javax.swing.SwingConstants;
public class TaskTablePanel extends JPanel {

    private final JTable table;
    private final DefaultTableModel model;
    private final Consumer<Task> onRowSelected;

    public TaskTablePanel(Consumer<Task> onRowSelected) {
        this.onRowSelected = onRowSelected;

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Tâches"));

        model = new DefaultTableModel(
                new Object[]{"ID", "Titre", "Priorité", "État", "Date limite", "Description"},
                0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // édition via formulaire
            }
        };

        table = new JTable(model);
        table.setRowHeight(22);
        table.setShowGrid(true);
        table.setGridColor(new Color(230, 230, 230));
        table.setIntercellSpacing(new java.awt.Dimension(8, 2));

        table.getColumnModel().getColumn(2).setCellRenderer(new PriorityRenderer());


        table.getColumnModel().getColumn(3).setCellRenderer(new StatusRenderer());
        table.getColumnModel()
                .getColumn(2) // colonne "Priorité"
                .setCellRenderer(new PriorityRenderer());
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Écouteur de sélection (ListSelectionListener)
        table.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) {
                return;
            }
            int row = table.getSelectedRow();
            if (row < 0) {
                return;
            }

            Task t = new Task();
            t.setId((int) model.getValueAt(row, 0));
            t.setTitle((String) model.getValueAt(row, 1));
            t.setPriority((String) model.getValueAt(row, 2));
            t.setStatus((String) model.getValueAt(row, 3));

            String due = (String) model.getValueAt(row, 4);
            if (due != null && !due.isBlank()) {
                t.setDueDate(java.time.LocalDate.parse(due));
            }

            t.setDescription((String) model.getValueAt(row, 5));

            onRowSelected.accept(t);
        });

        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    public void setTasks(List<Task> tasks) {
        model.setRowCount(0);

        for (Task t : tasks) {
            model.addRow(new Object[]{
                    t.getId(),
                    t.getTitle(),
                    t.getPriority(),
                    t.getStatus(),
                    t.getDueDate() != null ? t.getDueDate().toString() : "",
                    t.getDescription()
            });
        }
    }
    private static class PriorityRenderer extends DefaultTableCellRenderer {

        public PriorityRenderer() {
            setHorizontalAlignment(SwingConstants.CENTER);
        }

        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            if (!isSelected) {
                c.setBackground(Color.WHITE);

                String v = value == null ? "" : value.toString();
                switch (v) {
                    case "HIGH" -> c.setForeground(new Color(170, 0, 0));      // rouge sobre
                    case "MEDIUM" -> c.setForeground(new Color(160, 110, 0));  // jaune/brun sobre
                    case "LOW" -> c.setForeground(new Color(0, 120, 0));       // vert sobre
                    default -> c.setForeground(Color.DARK_GRAY);
                }
            }

            return c;
        }
    }
    private static class StatusRenderer extends DefaultTableCellRenderer {

        public StatusRenderer() {
            setHorizontalAlignment(SwingConstants.CENTER);
        }

        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            if (!isSelected) {
                c.setBackground(Color.WHITE);

                String v = value == null ? "" : value.toString();
                switch (v) {
                    case "TODO" -> c.setForeground(new Color(70, 90, 120));        // bleu/gris
                    case "IN_PROGRESS" -> c.setForeground(new Color(110, 80, 140)); // mauve/gris
                    case "DONE" -> c.setForeground(new Color(0, 110, 70));          // vert/teal
                    default -> c.setForeground(Color.DARK_GRAY);
                }
            }

            return c;
        }
    }
}