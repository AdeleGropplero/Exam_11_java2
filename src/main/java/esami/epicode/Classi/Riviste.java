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

    public void setPeriodicità(Periodicità periodicità) {
        this.periodicità = periodicità;
    }

    @Override
    public String toString() {
        return  "\n" +"Rivista:" + "\n" +
                "isbn= " + isbn  + ',' + "\n" +
                "titolo= " + titolo  + ',' + "\n" +
                "anno= " + anno + ',' + "\n" +
                "numPagine= " + numPagine + ',' + "\n" +
                "periodicità= " + periodicità + ',' + "\n" +
                "----------";
    }
}
