package src.view;

import src.controller.VerwaltungsController;
import src.model.Vokabeldatensatz;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class VerwaltungView extends JPanel {
    private final VerwaltungsController controller;

    private final JComboBox<String> cbKategorie = new JComboBox<>();
    private final JTextField tfSuche = new JTextField(16);

    private final JButton btnNeu = new JButton("Neu");
    private final JButton btnBearbeiten = new JButton("Bearbeiten");
    private final JButton btnLoeschen = new JButton("Löschen");

    private final DefaultTableModel tm = new DefaultTableModel(
            new Object[]{"Frage", "Antwort", "Typ", "Kategorie"}, 0
    ) { @Override public boolean isCellEditable(int r, int c) { return false; } };

    private final JTable table = new JTable(tm);

    // View merkt sich aktuelle Liste (für Auswahl)
    private List<Vokabeldatensatz> currentList = new ArrayList<>();

    public VerwaltungView(VerwaltungsController controller) {
        this.controller = controller;

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        add(buildTop(), BorderLayout.NORTH);
        add(buildCenter(), BorderLayout.CENTER);
        add(buildBottom(), BorderLayout.SOUTH);

        table.setRowHeight(26);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        cbKategorie.addActionListener(e -> controller.applyFilterAndUpdateTable());

        tfSuche.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { controller.applyFilterAndUpdateTable(); }
            @Override public void removeUpdate(DocumentEvent e) { controller.applyFilterAndUpdateTable(); }
            @Override public void changedUpdate(DocumentEvent e) { controller.applyFilterAndUpdateTable(); }
        });

        btnNeu.addActionListener(e -> controller.onNeu());
        btnBearbeiten.addActionListener(e -> controller.onBearbeiten());
        btnLoeschen.addActionListener(e -> controller.onLoeschen());
    }

    private JComponent buildTop() {
        JPanel top = new JPanel(new BorderLayout());

        JLabel h = new JLabel("Vokabelverwaltung");
        h.setFont(h.getFont().deriveFont(Font.BOLD, 16f));
        top.add(h, BorderLayout.WEST);

        JPanel filters = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        cbKategorie.setPreferredSize(new Dimension(160, 28));

        filters.add(new JLabel("Kategorie:"));
        filters.add(cbKategorie);
        filters.add(new JLabel("Suche:"));
        filters.add(tfSuche);

        top.add(filters, BorderLayout.EAST);
        return top;
    }

    private JComponent buildCenter() {
        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createTitledBorder("Einträge"));
        return sp;
    }

    private JComponent buildBottom() {
        JPanel bottom = new JPanel(new BorderLayout());

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        right.add(btnNeu);
        right.add(btnBearbeiten);
        right.add(btnLoeschen);

        bottom.add(right, BorderLayout.EAST);
        return bottom;
    }

    public void setKategorien(List<String> kategorien) {
        Object old = cbKategorie.getSelectedItem();
        cbKategorie.removeAllItems();
        for (String k : kategorien) cbKategorie.addItem(k);
        if (old != null) cbKategorie.setSelectedItem(old);
        else cbKategorie.setSelectedItem("Alle");
    }

    public String getSelectedKategorie() {
        Object o = cbKategorie.getSelectedItem();
        return o == null ? "Alle" : o.toString();
    }

    public String getSearchText() {
        return tfSuche.getText();
    }

    public void updateTable(List<Vokabeldatensatz> list) {
        currentList = list;
        tm.setRowCount(0);
        for (Vokabeldatensatz v : list) {
            tm.addRow(new Object[]{v.getFrageninhalt(), v.getKorrekteAntwort(), v.getAbfrageTyp(), v.getKategorie()});
        }
    }

    public int getSelectedIndex() {
        return table.getSelectedRow();
    }

    public Vokabeldatensatz getSelectedVokabel() {
        int row = table.getSelectedRow();
        if (row < 0) return null;
        if (row >= currentList.size()) return null;
        return currentList.get(row);
    }
}
