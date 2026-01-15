package src.controller;

import src.model.Vokabeldatensatz;

import javax.swing.*;
import java.util.*;
import java.util.stream.Collectors;

public class QuizController {
    private final MainController main;
    private src.view.QuizView view;

    private Vokabeldatensatz current;
    private final Random rnd = new Random();

    // Für TRUE/FALSE: ob die angezeigte Aussage wirklich korrekt ist
    private boolean tfShownIsCorrect = false;

    public QuizController(MainController main) {
        this.main = main;
    }

    public void setView(src.view.QuizView view) {
        this.view = view;
    }

    public void refresh() {
        if (view == null) return;

        Set<String> kats = Arrays.stream(main.getModel().getPool().getVokabeln())
                .map(Vokabeldatensatz::getKategorie)
                .filter(Objects::nonNull)
                .filter(s -> !s.isBlank())
                .collect(Collectors.toCollection(TreeSet::new));

        view.setKategorien(kats.toArray(new String[0]));

        view.setStatus("Bereit.");
        view.setFeedback(" ");
        view.setFrage("Drücke 'Nächste Frage'.");
        view.setModeLabel("Modus: —");
        view.clearAntwort();
        current = null;

        view.setModeText();
    }

    public void nextQuestion() {
        Vokabeldatensatz[] all = main.getModel().getPool().getVokabeln();
        if (all.length == 0) {
            JOptionPane.showMessageDialog(main.getFrame(), "Keine Vokabeln vorhanden.");
            return;
        }

        String kat = view.getSelectedKategorie();
        String typFilter = view.getSelectedTypFilter(); // ✅ NEU

        // Pool nach Kategorie + Typ filtern
        List<Vokabeldatensatz> pool = Arrays.stream(all)
                .filter(v -> kat == null || "Alle".equals(kat) || Objects.equals(v.getKategorie(), kat))
                .filter(v -> matchesTypeFilter(v.getAbfrageTyp(), typFilter))
                .collect(Collectors.toList());

        if (pool.isEmpty()) {
            JOptionPane.showMessageDialog(
                    main.getFrame(),
                    "Keine passenden Vokabeln für:\nKategorie: " + kat + "\nFragetyp: " + typFilter
            );
            return;
        }

        current = pool.get(rnd.nextInt(pool.size()));

        view.setFeedback(" ");
        view.setStatus(" ");

        // Modus nach Vokabel (jetzt passt das immer, weil du vorher gefiltert hast)
        String typ = normalizeType(current.getAbfrageTyp());

        if (typ.equals("MULTIPLE_CHOICE")) {
            view.setModeLabel("Modus: Multiple Choice");
            view.setModeMC();
            view.setFrage(current.getFrageninhalt());
            view.setMCOptions(buildMCOptions(current, all));

        } else if (typ.equals("TRUE_FALSE")) {
            view.setModeLabel("Modus: True / False");
            view.setModeTF();
            buildTrueFalseQuestion(current, all);

        } else {
            view.setModeLabel("Modus: Text");
            view.setModeText();
            view.setFrage(current.getFrageninhalt());
            view.clearAntwort();
        }
    }

    // -------- TEXT --------
    public void checkTextAnswer() {
        if (current == null) return;

        boolean ok = current.h(view.getAntwortText());

        applyResult(ok);
        view.setFeedback(ok ? "✅ Richtig!" : "❌ Falsch. Richtig: " + current.getKorrekteAntwort());
        view.setStatus("Richtig: " + current.getStatistik().getAnzahlRichtig()
                + " | Falsch: " + current.getStatistik().getAnzahlFalsch());

        main.speichernLeise();
        main.onStatsChanged();
    }

    // -------- MC --------
    public void checkMCAnswer(String selected) {
        if (current == null) return;

        boolean ok = selected != null && selected.trim().equalsIgnoreCase(current.getKorrekteAntwort().trim());

        applyResult(ok);
        view.setFeedback(ok ? "✅ Richtig!" : "❌ Falsch. Richtig: " + current.getKorrekteAntwort());
        view.setStatus("Richtig: " + current.getStatistik().getAnzahlRichtig()
                + " | Falsch: " + current.getStatistik().getAnzahlFalsch());

        main.speichernLeise();
        main.onStatsChanged();
    }

    // -------- TRUE/FALSE --------
    public void checkTFAnswer(boolean userSaysTrue) {
        if (current == null) return;

        boolean ok = (userSaysTrue == tfShownIsCorrect);

        applyResult(ok);
        view.setFeedback(ok ? "✅ Richtig!" : "❌ Falsch. Richtig wäre: " + current.getKorrekteAntwort());
        view.setStatus("Richtig: " + current.getStatistik().getAnzahlRichtig()
                + " | Falsch: " + current.getStatistik().getAnzahlFalsch());

        main.speichernLeise();
        main.onStatsChanged();
    }

    // ---------- Helfer ----------

    private void applyResult(boolean ok) {
        if (ok) current.getStatistik().erhöheRichtig();
        else current.getStatistik().erhöheFalsch();
    }

    private boolean matchesTypeFilter(String vocabType, String filterSelection) {
        String f = (filterSelection == null) ? "Alle Typen" : filterSelection.trim();
        if (f.equalsIgnoreCase("Alle Typen")) return true;

        String t = normalizeType(vocabType);
        String fNorm = normalizeType(f);

        return t.equals(fNorm);
    }

    private String normalizeType(String typ) {
        if (typ == null) return "TEXT";
        String t = typ.trim().toUpperCase();
        if (t.equals("MC")) return "MULTIPLE_CHOICE";
        if (t.equals("TF")) return "TRUE_FALSE";
        if (t.equals("TRUEFALSE")) return "TRUE_FALSE";
        if (t.equals("ALLE TYPEN")) return "ALL";
        return t;
    }

    private String[] buildMCOptions(Vokabeldatensatz correct, Vokabeldatensatz[] all) {
        LinkedHashSet<String> opts = new LinkedHashSet<>();
        opts.add(correct.getKorrekteAntwort().trim());

        int tries = 0;
        while (opts.size() < 4 && tries < 200) {
            Vokabeldatensatz v = all[rnd.nextInt(all.length)];
            if (v == null) { tries++; continue; }
            String ans = v.getKorrekteAntwort();
            if (ans == null) { tries++; continue; }
            opts.add(ans.trim());
            tries++;
        }

        while (opts.size() < 4) opts.add("—");

        List<String> list = new ArrayList<>(opts);
        Collections.shuffle(list, rnd);

        return new String[]{list.get(0), list.get(1), list.get(2), list.get(3)};
    }

    private void buildTrueFalseQuestion(Vokabeldatensatz base, Vokabeldatensatz[] all) {
        boolean showCorrect = rnd.nextBoolean();
        String shownAnswer;

        if (showCorrect || all.length < 2) {
            shownAnswer = base.getKorrekteAntwort();
            tfShownIsCorrect = true;
        } else {
            String candidate = null;
            int tries = 0;
            while (tries < 200) {
                Vokabeldatensatz v = all[rnd.nextInt(all.length)];
                if (v == null) { tries++; continue; }
                String ans = v.getKorrekteAntwort();
                if (ans == null) { tries++; continue; }
                if (!ans.trim().equalsIgnoreCase(base.getKorrekteAntwort().trim())) {
                    candidate = ans.trim();
                    break;
                }
                tries++;
            }
            if (candidate == null) {
                candidate = base.getKorrekteAntwort();
                tfShownIsCorrect = true;
            } else {
                tfShownIsCorrect = false;
            }
            shownAnswer = candidate;
        }

        String text = "<html>Ist diese Übersetzung richtig?<br><b>"
                + escape(base.getFrageninhalt())
                + "</b>  →  <b>"
                + escape(shownAnswer)
                + "</b></html>";

        view.setFrage(text);
    }

    private String escape(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }
}