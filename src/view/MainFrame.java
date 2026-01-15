package src.view;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private final JButton btnHome = new JButton("Hauptmenü");
    private final JButton btnVerwaltung = new JButton("Verwaltung");
    private final JButton btnQuiz = new JButton("Quiz");
    private final JButton btnStatistik = new JButton("Statistik");

    private final JButton btnSpeichern = new JButton("Speichern");
    private final JButton btnBeenden = new JButton("Beenden");

    // Theme Toggle
    private final JToggleButton btnTheme = new JToggleButton("Dark Mode");

    // Mini Ziel-Anzeige
    private final JLabel lblGoalMini = new JLabel("Ziel: nicht gesetzt");

    private final JPanel cards = new JPanel(new CardLayout());

    public MainFrame() {
        super("Vokabeltrainer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 700);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        add(buildTopBar(), BorderLayout.NORTH);
        add(buildNav(), BorderLayout.WEST);
        add(cards, BorderLayout.CENTER);
    }

    private JComponent buildTopBar() {
        JPanel top = new JPanel(new BorderLayout());
        top.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));

        JLabel title = new JLabel("Vokabeltrainer");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 18f));
        top.add(title, BorderLayout.WEST);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        right.add(lblGoalMini);
        right.add(btnTheme);
        right.add(btnSpeichern);
        right.add(btnBeenden);

        top.add(right, BorderLayout.EAST);
        return top;
    }

    private JComponent buildNav() {
        JPanel nav = new JPanel();
        nav.setLayout(new BoxLayout(nav, BoxLayout.Y_AXIS));
        nav.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        nav.setPreferredSize(new Dimension(180, 0));

        styleNav(btnHome);
        styleNav(btnVerwaltung);
        styleNav(btnQuiz);
        styleNav(btnStatistik);

        nav.add(new JLabel("Navigation"));
        nav.add(Box.createVerticalStrut(10));
        nav.add(btnHome);
        nav.add(Box.createVerticalStrut(8));
        nav.add(btnVerwaltung);
        nav.add(Box.createVerticalStrut(8));
        nav.add(btnQuiz);
        nav.add(Box.createVerticalStrut(8));
        nav.add(btnStatistik);
        nav.add(Box.createVerticalGlue());

        return nav;
    }

    private void styleNav(JButton b) {
        b.setFocusPainted(false);
        b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
    }

    public void addCard(String name, JComponent comp) {
        cards.add(comp, name);
    }

    public void showCard(String name) {
        ((CardLayout) cards.getLayout()).show(cards, name);
    }

    public JButton getBtnHome() { return btnHome; }
    public JButton getBtnVerwaltung() { return btnVerwaltung; }
    public JButton getBtnQuiz() { return btnQuiz; }
    public JButton getBtnStatistik() { return btnStatistik; }
    public JButton getBtnSpeichern() { return btnSpeichern; }
    public JButton getBtnBeenden() { return btnBeenden; }

    public JToggleButton getBtnTheme() { return btnTheme; }

    public void setGoalMiniText(String text) {
        lblGoalMini.setText(text);
    }
}
