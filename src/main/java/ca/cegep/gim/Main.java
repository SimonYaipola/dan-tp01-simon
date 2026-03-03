package ca.cegep.gim;

import ca.cegep.gim.todo.config.AppConfig;
import ca.cegep.gim.todo.config.DbMode;
import ca.cegep.gim.todo.config.SchemaInitializer;
import ca.cegep.gim.todo.ui.MainFrame;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public final class Main {

    private Main() {}

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}

            DbMode mode = askDbMode();
            if (mode == null) {
                return; // l'utilisateur a fermé la fenêtre
            }

            AppConfig.setDbMode(mode);

            // Si LOCAL: créer la table via schema.sql automatiquement
            SchemaInitializer.initLocalSchemaIfNeeded();

            new MainFrame().setVisible(true);
        });
    }

    private static DbMode askDbMode() {
        Object[] options = {"Online (MariaDB)", "Local (H2 / localhost)"};

        int choice = JOptionPane.showOptionDialog(
                null,
                "Choisis la connexion de base de données :",
                "Connexion",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );

        if (choice == JOptionPane.CLOSED_OPTION) {
            return null;
        }
        return (choice == 1) ? DbMode.LOCAL : DbMode.ONLINE;
    }
}