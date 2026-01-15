package src.view;

import src.controller.StatistikController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class StatistikView extends JPanel {
    private final StatistikController controller;

    private final JLabel lblRichtig = new JLabel("0");
    private final JLabel lblFalsch = new JLabel("0");
    private final JLabel lblQuote = new JLabel("0.0%");

    private final JLabel lblGoal = new JLabel("Lernziel: nicht gesetzt");
    private final JProgressBar goalBar = new JProgressBar(0, 100);
    private final JButton btnSetGoal = new JButton("Ziel setzen");

    private final DefaultTableModel tm = new DefaultTableModel(
            new Object[]{"Kategorie", "Richtig", "Falsch", "Quote"}, 0
    ) { @Override public boolean isCellEditable(int r, int c) { return false; } };

    private final JTable table = new JTable(tm);

    public StatistikView(StatistikController controller) {
        this.controller = controller;

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        add(buildTop(), BorderLayout.NORTH);
        add(buildCenter(), BorderLayout.CENTER);
        add(buildBottom(), BorderLayout.SOUTH);

        table.setRowHeight(26);
        goalBar.setStringPainted(true);

        btnSetGoal.addActionListener(e -> controller.onSetGoal());
    }

    private JComponent buildTop() {
        JPanel top = new JPanel(new BorderLayout());

        JLabel h = new JLabel("Statistik");
        h.setFont(h.getFont().deriveFont(Font.BOLD, 16f));
        top.add(h, BorderLayout.WEST);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        right.add(btnSetGoal);
        top.add(right, BorderLayout.EAST);

        return top;
    }

    private JComponent buildCenter() {
        JPanel center = new JPanel(new BorderLayout(0, 12));

        JPanel total = new JPanel(new GridBagLayout());
        total.setBorder(BorderFactory.createTitledBorder("Gesamt"));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8, 10, 8, 10);
        c.anchor = GridBagConstraints.WEST;

        c.gridx = 0; c.gridy = 0; total.add(new JLabel("Richtig:"), c);
        c.gridx = 1; total.add(lblRichtig, c);

        c.gridx = 0; c.gridy = 1; total.add(new JLabel("Falsch:"), c);
        c.gridx = 1; total.add(lblFalsch, c);

        c.gridx = 0; c.gridy = 2; total.add(new JLabel("Quote:"), c);
        c.gridx = 1; total.add(lblQuote, c);

        JPanel goal = new JPanel(new BorderLayout(8, 8));
        goal.setBorder(BorderFactory.createTitledBorder("Lernziel"));
        goal.add(lblGoal, BorderLayout.NORTH);
        goal.add(goalBar, BorderLayout.CENTER);

        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createTitledBorder("Pro Kategorie"));

        JPanel upper = new JPanel(new BorderLayout(0, 12));
        upper.add(total, BorderLayout.NORTH);
        upper.add(goal, BorderLayout.CENTER);

        center.add(upper, BorderLayout.NORTH);
        center.add(sp, BorderLayout.CENTER);

        return center;
    }

    private JComponent buildBottom() {
        JPanel bottom = new JPanel(new BorderLayout());
        bottom.add(new JLabel("Tipp: Setze ein Lernziel (z.B. 20 richtige Antworten)."), BorderLayout.WEST);
        return bottom;
    }

    // Controller -> View
    public void setTotals(int richtig, int falsch, double quote) {
        lblRichtig.setText(String.valueOf(richtig));
        lblFalsch.setText(String.valueOf(falsch));
        lblQuote.setText(String.format("%.1f%%", quote));
    }

    public void setGoal(int target, int current) {
        if (target <= 0) {
            lblGoal.setText("Lernziel: nicht gesetzt");
            goalBar.setValue(0);
            goalBar.setString("0%");
            return;
        }
        lblGoal.setText("Lernziel: " + current + " / " + target + " richtige Antworten");
        int pct = (int) Math.round(Math.min(100.0, (current * 100.0) / target));
        goalBar.setValue(pct);
        goalBar.setString(pct + "%");
    }

    public void setCategoryRows(Object[][] rows) {
        tm.setRowCount(0);
        for (Object[] r : rows) tm.addRow(r);
    }
}
