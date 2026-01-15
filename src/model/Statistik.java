package src.model;
import java.io.Serializable;


public class Statistik implements Serializable {
    private int anzahlRichtig;
    private int anzahlFalsch;

    public Statistik() {
        this.anzahlRichtig = 0;
        this.anzahlFalsch = 0;
    }

    public void erhöheRichtig() {
        anzahlRichtig++;
    }

    public void erhöheFalsch() {
        anzahlFalsch++;
    }

    public int getAnzahlRichtig() {
        return anzahlRichtig;
    }

    public int getAnzahlFalsch() {
        return anzahlFalsch;
    }

    public double berechneErfolg() {
        int gesamt = anzahlRichtig + anzahlFalsch;
        if (gesamt == 0) return 0.0;
        return (double) anzahlRichtig / gesamt * 100.0;
    }
}
