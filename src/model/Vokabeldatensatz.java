package src.model;

import java.io.Serializable;

public class Vokabeldatensatz implements Serializable {
    private String frageninhalt;
    private String korrekteAntwort;
    private String abfrageTyp;
    private String kategorie;
    private Statistik statistik;

    public Vokabeldatensatz(String frageninhalt, String korrekteAntwort, String abfrageTyp, String kategorie) {
        this.frageninhalt = frageninhalt;
        this.korrekteAntwort = korrekteAntwort;
        this.abfrageTyp = abfrageTyp;
        this.kategorie = kategorie;
        this.statistik = new Statistik();
    }

    public String getFrageninhalt() {
        return frageninhalt;
    }

    public void setFrageninhalt(String frageninhalt) {
        this.frageninhalt = frageninhalt;
    }

    public String getKorrekteAntwort() {
        return korrekteAntwort;
    }

    public void setKorrekteAntwort(String korrekteAntwort) {
        this.korrekteAntwort = korrekteAntwort;
    }

    public String getAbfrageTyp() {
        return abfrageTyp;
    }

    public void setAbfrageTyp(String abfrageTyp) {
        this.abfrageTyp = abfrageTyp;
    }

    public String getKategorie() {
        return kategorie;
    }

    public void setKategorie(String kategorie) {
        this.kategorie = kategorie;
    }

    public Statistik getStatistik() {
        return statistik;
    }

    public boolean h(String eingabe) {
        if (eingabe == null) return false;
        return korrekteAntwort.trim().equalsIgnoreCase(eingabe.trim());
    }
}