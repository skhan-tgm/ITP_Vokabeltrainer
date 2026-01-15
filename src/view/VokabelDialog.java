package src.view;

import src.model.Vokabeldatensatz;

import javax.swing.*;
import java.awt.*;

public class VokabelDialog extends JDialog {
    private final JTextField tfFrage = new JTextField(22);
    private final JTextField tfAntwort = new JTextField(22);

    private final JComboBox<String> cbTyp = new JComboBox<>(new String[]{
            "TEXT", "MULTIPLE_CHOICE", "TRUE_FALSE"
    });

    private final JTextField tfKategorie = new JTextField(22);

    private boolean ok = false;

    public VokabelDialog(Window owner, String title, Vokabeldatensatz preset) {
        super(owner, title, ModalityType.APPLICATION_MODAL);
        setSize(460, 270);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());

        add(buildForm(), BorderLayout.CENTER);
        add(buildButtons(), BorderLayout.SOUTH);

        if (preset != null) {
            tfFrage.setText(preset.getFrageninhalt());
            tfAntwort.setText(preset.getKorrekteAntwort());
            tfKategorie.setText(preset.getKategorie());

            String typ = preset.getAbfrageTyp();
            if (typ != null) {
                String t = typ.trim().toUpperCase();
                if (t.equals("MC")) t = "MULTIPLE_CHOICE";
                if (t.equals("TF") || t.equals("TRUEFALSE")) t = "TRUE_FALSE";

                if (t.equals("MULTIPLE_CHOICE")) cbTyp.setSelectedItem("MULTIPLE_CHOICE");
                else if (t.equals("TRUE_FALSE")) cbTyp.setSelectedItem("TRUE_FALSE");
                else cbTyp.setSelectedItem("TEXT");
            } else {
                cbTyp.setSelectedItem("TEXT");
            }
        } else {
            cbTyp.setSelectedItem("TEXT");
            tfKategorie.setText("Allgemein");
        }
    }

    private JComponent buildForm() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 6, 6, 6);
        c.anchor = GridBagConstraints.WEST;

        c.gridx = 0; c.gridy = 0;
        p.add(new JLabel("Frage:"), c);
        c.gridx = 1;
        p.add(tfFrage, c);

        c.gridx = 0; c.gridy = 1;
        p.add(new JLabel("Antwort:"), c);
        c.gridx = 1;
        p.add(tfAntwort, c);

        c.gridx = 0; c.gridy = 2;
        p.add(new JLabel("Abfrage-Typ:"), c);
        c.gridx = 1;
        cbTyp.setPreferredSize(new Dimension(220, 28));
        p.add(cbTyp, c);

        c.gridx = 0; c.gridy = 3;
        p.add(new JLabel("Kategorie:"), c);
        c.gridx = 1;
        p.add(tfKategorie, c);

        return p;
    }

    private JComponent buildButtons() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        p.setBorder(BorderFactory.createEmptyBorder(0, 12, 12, 12));

        JButton cancel = new JButton("Abbrechen");
        JButton save = new JButton("OK");

        cancel.addActionListener(e -> { ok = false; dispose(); });

        save.addActionListener(e -> {
            if (tfFrage.getText().trim().isEmpty() || tfAntwort.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Frage und Antwort dürfen nicht leer sein.");
                return;
            }
            ok = true;
            dispose();
        });

        p.add(cancel);
        p.add(save);
        return p;
    }

    public boolean isOk() { return ok; }
    public String getFrage() { return tfFrage.getText().trim(); }
    public String getAntwort() { return tfAntwort.getText().trim(); }
    public String getTyp() {
        Object o = cbTyp.getSelectedItem();
        return o == null ? "TEXT" : o.toString();
    }
    public String getKategorie() { return tfKategorie.getText().trim(); }
}
