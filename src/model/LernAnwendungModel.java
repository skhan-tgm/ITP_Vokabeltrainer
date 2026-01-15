package src.model;

import java.io.Serializable;

public class LernAnwendungModel implements Serializable {
    private Benutzer schüler;
    private Vokabelpool pool;

    public LernAnwendungModel(String name) {
        this.schüler = new Benutzer(name);
        this.pool = new Vokabelpool();
    }

    public Benutzer getSchüler() { return schüler; }
    public Vokabelpool getPool() { return pool; }
}