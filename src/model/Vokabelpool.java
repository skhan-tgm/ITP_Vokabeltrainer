package src.model;
import java.io.*;
import java.util.Scanner;
import java.util.Random;

public class Vokabelpool implements Serializable {
    private Vokabeldatensatz[] vokabeln;
    private final String DATEI = "vokabeln.txt";

    public Vokabelpool() {
        this.vokabeln = new Vokabeldatensatz[0];
    }

    public void hinzufügen(Vokabeldatensatz v) {
        Vokabeldatensatz[] neu = new Vokabeldatensatz[vokabeln.length + 1];
        for (int i = 0; i < vokabeln.length; i++) {
            neu[i] = vokabeln[i];
        }
        neu[neu.length - 1] = v;
        vokabeln = neu;
    }

    public void speichern() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(DATEI))) {
            for (Vokabeldatensatz v : vokabeln) {
                writer.println(
                        v.getFrageninhalt() + ";" +
                                v.getKorrekteAntwort() + ";" +
                                v.getAbfrageTyp() + ";" +
                                v.getKategorie() + ";" +
                                v.getStatistik().getAnzahlRichtig() + ";" +
                                v.getStatistik().getAnzahlFalsch()
                );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void laden() {
        File f = new File(DATEI);
        if (!f.exists()) return;

        vokabeln = new Vokabeldatensatz[0];

        try (Scanner scanner = new Scanner(f)) {
            while (scanner.hasNextLine()) {
                String zeile = scanner.nextLine();
                String[] teile = zeile.split(";");

                if (teile.length == 6) {
                    Vokabeldatensatz v = new Vokabeldatensatz(teile[0], teile[1], teile[2], teile[3]);

                    int richtig = Integer.parseInt(teile[4]);
                    int falsch = Integer.parseInt(teile[5]);

                    for (int i = 0; i < richtig; i++) v.getStatistik().erhöheRichtig();
                    for (int i = 0; i < falsch; i++) v.getStatistik().erhöheFalsch();

                    hinzufügen(v);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Vokabeldatensatz[] getVokabeln() {
        return vokabeln;
    }

    public Vokabeldatensatz getZufälligeVokabel() {
        if (vokabeln.length == 0) return null;
        return vokabeln[new Random().nextInt(vokabeln.length)];
    }
}