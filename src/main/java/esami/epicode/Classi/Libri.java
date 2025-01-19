package esami.epicode.Classi;

import esami.epicode.Classi.Enum.Genere;

public class Libri extends Pubblicazioni{
    protected String autore;
    protected Genere genere;

    public Libri(String autore, Genere genere, String isbn, String titolo, int anno, int numPagine){
        super(isbn, titolo, anno, numPagine);
        this.autore=autore;
        this.genere=genere;

    }

    public String getAutore() {
        return autore;
    }

    public Genere getGenere() {
        return genere;
    }


    public void setGenere(Genere genere) {
        this.genere = genere;
    }

    public void setAutore(String autore) {
        this.autore = autore;
    }

    @Override
    public String toString() {
        return  "\n" + "Libro: " + "\n" +
                "autore= " + autore + ',' + "\n" +
                "genere= " + genere + ',' + "\n" +
                "isbn= " + isbn + ',' + "\n" +
                "titolo= " + titolo  + ',' + "\n" +
                "anno= " + anno + ',' + "\n" +
                "numPagine= " + numPagine + "\n" +
                "----------";
    }

}
