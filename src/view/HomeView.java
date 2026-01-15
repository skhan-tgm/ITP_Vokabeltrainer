package src.view;

import src.controller.MainController;

import javax.swing.*;
import java.awt.*;

public class HomeView extends JPanel {
    private final MainController controller;
    private final JLabel lblUser = new JLabel("Benutzer: -");

    public HomeView(MainController controller) {
        this.controller = controller;

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JLabel title = new JLabel("Hauptmenü");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 18f));
        add(title, BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout());
        center.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        lblUser.setFont(lblUser.getFont().deriveFont(Font.PLAIN, 16f));
        center.add(lblUser, BorderLayout.NORTH);

        JTextArea info = new JTextArea(
                "Tipps:\n" +
                        "- In 'Verwaltung' kannst du Vokabeln hinzufügen.\n" +
                        "- In 'Quiz' kannst du üben (Kategorie + Fragetyp wählbar).\n" +
                        "- In 'Statistik' siehst du deinen Fortschritt & Lernziel.\n"
        );
        info.setEditable(false);
        info.setOpaque(false);
        center.add(info, BorderLayout.CENTER);

        add(center, BorderLayout.CENTER);
    }

    public void refresh() {
        lblUser.setText("Benutzer: " + controller.getModel().getSchüler().getName());
    }
}
