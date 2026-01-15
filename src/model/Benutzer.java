package src.model;
import java.io.Serializable;

public class Benutzer implements Serializable {
    private String name;
    private Vokabeldatensatz[] vokabelListe;

    public Benutzer(String name) {
        this.name = name;
        this.vokabelListe = new Vokabeldatensatz[0];
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Vokabeldatensatz[] getVokabelListe() {
        return vokabelListe;
    }

    public void fügeVokabelHinzu(Vokabeldatensatz v) {
        Vokabeldatensatz[] neuesArray = new Vokabeldatensatz[vokabelListe.length + 1];
        for (int i = 0; i < vokabelListe.length; i++) {
            neuesArray[i] = vokabelListe[i];
        }
        neuesArray[neuesArray.length - 1] = v;
        vokabelListe = neuesArray;
    }
}