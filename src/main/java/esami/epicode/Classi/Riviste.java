package esami.epicode.Classi;

import esami.epicode.Classi.Enum.Periodicità;

public class Riviste extends Pubblicazioni {
    protected Periodicità periodicità;

    public Riviste(String isbn, String titolo, int anno, int numPagine,Periodicità periodicità ){
        super(isbn, titolo, anno, numPagine);
        this.periodicità=periodicità;
    }

    public Periodicità getPeriodicità() {
        return periodicità;
    }

    @Override
    public String toString() {
        return "Riviste{" +
                "isbn='" + isbn + '\'' +
                ", titolo='" + titolo + '\'' +
                ", anno=" + anno +
                ", numPagine=" + numPagine +
                ", periodicità=" + periodicità +
                '}';
    }
}
