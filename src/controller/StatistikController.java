package src.controller;

import src.model.Vokabeldatensatz;

import javax.swing.*;
import java.util.*;

public class StatistikController {
    private final MainController main;
    private src.view.StatistikView view;

    public StatistikController(MainController main) {
        this.main = main;
    }

    public void setView(src.view.StatistikView view) {
        this.view = view;
    }

    public void refresh() {
        if (view == null) return;

        // Gesamt + Kategorie sammeln
        int totalR = 0;
        int totalF = 0;

        Map<String, int[]> perKat = new TreeMap<>(); // kat -> [r,f]

        for (Vokabeldatensatz v : main.getModel().getPool().getVokabeln()) {
            int r = v.getStatistik().getAnzahlRichtig();
            int f = v.getStatistik().getAnzahlFalsch();
            totalR += r;
            totalF += f;

            String kat = v.getKategorie();
            if (kat == null || kat.isBlank()) kat = "Allgemein";

            perKat.putIfAbsent(kat, new int[]{0, 0});
            perKat.get(kat)[0] += r;
            perKat.get(kat)[1] += f;
        }

        double quote = (totalR + totalF) == 0 ? 0.0 : (totalR * 100.0) / (totalR + totalF);
        view.setTotals(totalR, totalF, quote);

        // Lernziel
        int goalTarget = main.getGoalTarget();
        int currentCorrect = totalR; // einfach: Ziel basiert auf Gesamt-richtig (persistiert über Datei)
        view.setGoal(goalTarget, currentCorrect);

        // Kategorie-Tabelle
        Object[][] rows = new Object[perKat.size()][4];
        int i = 0;
        for (var entry : perKat.entrySet()) {
            String kat = entry.getKey();
            int r = entry.getValue()[0];
            int f = entry.getValue()[1];
            int g = r + f;
            double q = g == 0 ? 0.0 : (r * 100.0) / g;

            rows[i][0] = kat;
            rows[i][1] = r;
            rows[i][2] = f;
            rows[i][3] = String.format("%.1f%%", q);
            i++;
        }
        view.setCategoryRows(rows);
    }

    // ✅ Lernziel setzen
    public void onSetGoal() {
        String in = JOptionPane.showInputDialog(main.getFrame(),
                "Wie viele richtige Antworten ist dein Lernziel?\n(Beispiel: 20)",
                main.getGoalTarget() <= 0 ? "" : String.valueOf(main.getGoalTarget()));

        if (in == null) return;
        in = in.trim();
        if (in.isEmpty()) return;

        try {
            int goal = Integer.parseInt(in);
            if (goal <= 0) {
                JOptionPane.showMessageDialog(main.getFrame(), "Bitte eine Zahl > 0 eingeben.");
                return;
            }
            main.setGoalTarget(goal);
            refresh();
            main.updateGoalMini();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(main.getFrame(), "Bitte eine gültige Zahl eingeben.");
        }
    }
}