package src.controller;

import src.model.LernAnwendungModel;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            String name = JOptionPane.showInputDialog(null, "Benutzername:", "Start", JOptionPane.QUESTION_MESSAGE);
            if (name == null || name.trim().isEmpty()) name = "Gast";

            LernAnwendungModel model = new LernAnwendungModel(name);
            // wichtig: beim Start laden
            model.getPool().laden();

            MainController controller = new MainController(model);
            controller.start();
        });
    }
}