package src.view;

import src.controller.QuizController;

import javax.swing.*;
import java.awt.*;

public class QuizView extends JPanel {
    private final QuizController controller;

    private final JComboBox<String> cbKategorie = new JComboBox<>();

    // Typ-Filter: nur bestimmte Fragearten
    private final JComboBox<String> cbTypFilter = new JComboBox<>(new String[]{
            "Alle Typen", "TEXT", "MULTIPLE_CHOICE", "TRUE_FALSE"
    });

    private final JButton btnNext = new JButton("Nächste Frage");

    private final JLabel lblFrage = new JLabel("—");
    private final JLabel lblMode = new JLabel("Modus: —");

    // TEXT
    private final JTextField tfAntwort = new JTextField(22);
    private final JButton btnCheck = new JButton("Prüfen");

    // MULTIPLE CHOICE
    private final JPanel mcPanel = new JPanel(new GridLayout(2, 2, 10, 10));
    private final JButton[] mcButtons = new JButton[]{
            new JButton("A"), new JButton("B"), new JButton("C"), new JButton("D")
    };

    // TRUE/FALSE
    private final JPanel tfPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
    private final JButton btnTrue = new JButton("True ✅");
    private final JButton btnFalse = new JButton("False ❌");

    private final JLabel lblFeedback = new JLabel(" ");
    private final JLabel lblStatus = new JLabel("Bereit.");

    public QuizView(QuizController controller) {
        this.controller = controller;

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        add(buildTop(), BorderLayout.NORTH);
        add(buildCenter(), BorderLayout.CENTER);
        add(buildBottom(), BorderLayout.SOUTH);

        btnNext.addActionListener(e -> controller.nextQuestion());
        btnCheck.addActionListener(e -> controller.checkTextAnswer());

        for (JButton b : mcButtons) {
            b.setFocusPainted(false);
            b.addActionListener(e -> controller.checkMCAnswer(b.getText()));
            mcPanel.add(b);
        }

        btnTrue.setFocusPainted(false);
        btnFalse.setFocusPainted(false);
        btnTrue.addActionListener(e -> controller.checkTFAnswer(true));
        btnFalse.addActionListener(e -> controller.checkTFAnswer(false));
        tfPanel.add(btnTrue);
        tfPanel.add(btnFalse);

        setModeText();
    }

    private JComponent buildTop() {
        JPanel top = new JPanel(new BorderLayout());

        JLabel h = new JLabel("Quiz");
        h.setFont(h.getFont().deriveFont(Font.BOLD, 16f));
        top.add(h, BorderLayout.WEST);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        cbKategorie.setPreferredSize(new Dimension(160, 28));
        cbTypFilter.setPreferredSize(new Dimension(170, 28));

        right.add(new JLabel("Kategorie:"));
        right.add(cbKategorie);

        right.add(new JLabel("Fragetyp:"));
        right.add(cbTypFilter);

        right.add(btnNext);
        top.add(right, BorderLayout.EAST);

        return top;
    }

    private JComponent buildCenter() {
        JPanel center = new JPanel(new BorderLayout(0, 10));

        JPanel head = new JPanel(new BorderLayout());
        lblFrage.setBorder(BorderFactory.createTitledBorder("Frage"));
        lblFrage.setFont(lblFrage.getFont().deriveFont(Font.PLAIN, 18f));
        head.add(lblFrage, BorderLayout.CENTER);

        lblMode.setBorder(BorderFactory.createEmptyBorder(6, 0, 0, 0));
        head.add(lblMode, BorderLayout.SOUTH);

        center.add(head, BorderLayout.NORTH);

        JPanel textPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        textPanel.setBorder(BorderFactory.createTitledBorder("Antwort (Text)"));
        textPanel.add(new JLabel("Eingabe:"));
        textPanel.add(tfAntwort);
        textPanel.add(btnCheck);

        mcPanel.setBorder(BorderFactory.createTitledBorder("Antwortmöglichkeiten (Multiple Choice)"));
        tfPanel.setBorder(BorderFactory.createTitledBorder("True / False"));

        JPanel modes = new JPanel();
        modes.setLayout(new BoxLayout(modes, BoxLayout.Y_AXIS));
        modes.add(textPanel);
        modes.add(Box.createVerticalStrut(10));
        modes.add(mcPanel);
        modes.add(Box.createVerticalStrut(10));
        modes.add(tfPanel);

        center.add(modes, BorderLayout.CENTER);

        lblFeedback.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        center.add(lblFeedback, BorderLayout.SOUTH);

        return center;
    }

    private JComponent buildBottom() {
        JPanel bottom = new JPanel(new BorderLayout());
        bottom.add(lblStatus, BorderLayout.WEST);
        return bottom;
    }

    // ---------- API ----------

    public void setKategorien(String[] kategorien) {
        cbKategorie.removeAllItems();
        cbKategorie.addItem("Alle");
        for (String k : kategorien) cbKategorie.addItem(k);
        cbKategorie.setSelectedItem("Alle");
    }

    public String getSelectedKategorie() {
        Object o = cbKategorie.getSelectedItem();
        return o == null ? "Alle" : o.toString();
    }

    public String getSelectedTypFilter() {
        Object o = cbTypFilter.getSelectedItem();
        return o == null ? "Alle Typen" : o.toString();
    }

    public void setFrage(String text) { lblFrage.setText(text); }
    public String getAntwortText() { return tfAntwort.getText(); }
    public void clearAntwort() { tfAntwort.setText(""); }
    public void setFeedback(String text) { lblFeedback.setText(text); }
    public void setStatus(String text) { lblStatus.setText(text); }
    public void setModeLabel(String text) { lblMode.setText(text); }

    public void setModeText() {
        tfAntwort.setEnabled(true);
        btnCheck.setEnabled(true);
        mcPanel.setVisible(false);
        tfPanel.setVisible(false);
        revalidate();
        repaint();
    }

    public void setModeMC() {
        tfAntwort.setEnabled(false);
        btnCheck.setEnabled(false);
        mcPanel.setVisible(true);
        tfPanel.setVisible(false);
        revalidate();
        repaint();
    }

    public void setModeTF() {
        tfAntwort.setEnabled(false);
        btnCheck.setEnabled(false);
        mcPanel.setVisible(false);
        tfPanel.setVisible(true);
        revalidate();
        repaint();
    }

    public void setMCOptions(String[] options) {
        for (int i = 0; i < mcButtons.length; i++) {
            mcButtons[i].setText(options[i]);
            mcButtons[i].setEnabled(!"—".equals(options[i]));
        }
    }
}
