package src.controller;

import src.model.Vokabeldatensatz;
import src.view.VokabelDialog;
import src.view.VerwaltungView;

import javax.swing.*;
import java.util.*;
import java.util.stream.Collectors;

public class VerwaltungsController {

    private final MainController main;
    private VerwaltungView view;

    public VerwaltungsController(MainController main) {
        this.main = main;
    }

    public void setView(VerwaltungView view) {
        this.view = view;
    }

    // Wird aufgerufen, wenn die Verwaltungsansicht geöffnet wird
    public void refresh() {
        if (view == null) return;

        // Kategorien sammeln
        Set<String> kategorien = new TreeSet<>();
        kategorien.add("Alle");

        for (Vokabeldatensatz v : main.getModel().getPool().getVokabeln()) {
            if (v.getKategorie() != null && !v.getKategorie().isBlank()) {
                kategorien.add(v.getKategorie());
            }
        }

        view.setKategorien(new ArrayList<>(kategorien));
        applyFilterAndUpdateTable();
    }

    public void applyFilterAndUpdateTable() {
        String selectedKat = view.getSelectedKategorie();
        String search = view.getSearchText().trim().toLowerCase();

        Vokabeldatensatz[] all = main.getModel().getPool().getVokabeln();

        List<Vokabeldatensatz> filtered = Arrays.stream(all)
                .filter(v -> selectedKat == null
                        || "Alle".equals(selectedKat)
                        || Objects.equals(v.getKategorie(), selectedKat))
                .filter(v -> search.isEmpty()
                        || safe(v.getFrageninhalt()).contains(search)
                        || safe(v.getKorrekteAntwort()).contains(search)
                        || safe(v.getKategorie()).contains(search))
                .collect(Collectors.toList());

        view.updateTable(filtered);
    }

    private String safe(String s) {
        return s == null ? "" : s.toLowerCase();
    }

    // ==========================
    // BUTTON-AKTIONEN
    // ==========================

    public void onNeu() {
        VokabelDialog dlg = new VokabelDialog(main.getFrame(), "Neue Vokabel", null);
        dlg.setVisible(true);
        if (!dlg.isOk()) return;

        Vokabeldatensatz v = new Vokabeldatensatz(
                dlg.getFrage(),
                dlg.getAntwort(),
                dlg.getTyp(),
                dlg.getKategorie().isBlank() ? "Allgemein" : dlg.getKategorie()
        );

        main.getModel().getPool().hinzufügen(v);
        main.speichernLeise();
        refresh();
    }

    public void onBearbeiten() {
        Vokabeldatensatz selected = view.getSelectedVokabel();
        if (selected == null) {
            JOptionPane.showMessageDialog(main.getFrame(), "Bitte zuerst eine Vokabel auswählen.");
            return;
        }

        VokabelDialog dlg = new VokabelDialog(main.getFrame(), "Vokabel bearbeiten", selected);
        dlg.setVisible(true);
        if (!dlg.isOk()) return;

        selected.setFrageninhalt(dlg.getFrage());
        selected.setKorrekteAntwort(dlg.getAntwort());
        selected.setAbfrageTyp(dlg.getTyp());
        selected.setKategorie(dlg.getKategorie().isBlank() ? "Allgemein" : dlg.getKategorie());

        main.speichernLeise();
        refresh();
    }

    public void onLoeschen() {
        Vokabeldatensatz selected = view.getSelectedVokabel();
        if (selected == null) {
            JOptionPane.showMessageDialog(main.getFrame(), "Bitte zuerst eine Vokabel auswählen.");
            return;
        }

        int res = JOptionPane.showConfirmDialog(
                main.getFrame(),
                "Wirklich löschen?",
                "Bestätigen",
                JOptionPane.YES_NO_OPTION
        );

        if (res != JOptionPane.YES_OPTION) return;

        // ⬇⬇⬇ WICHTIG: benötigt die entfernen()-Methode im Model ⬇⬇⬇
        main.getModel().getPool().entfernen(selected);

        main.speichernLeise();
        refresh();
    }
}