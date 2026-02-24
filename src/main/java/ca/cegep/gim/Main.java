package ca.cegep.gim;

import ca.cegep.gim.todo.ui.MainFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public final class Main {

    private Main() {}

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
            }
            new MainFrame().setVisible(true);
        });
    }
}