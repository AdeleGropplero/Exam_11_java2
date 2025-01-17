package esami.epicode.Classi;

public class Libri extends Pubblicazioni{
    protected String autore;
    protected String genere;

    public Libri(String autore, String genere, String isbn, String titolo, int anno, int numPagine){
        super(isbn, titolo, anno, numPagine);
        this.autore=autore;
        this.genere=genere;

    }

    public String getAutore() {
        return autore;
    }

    public String getGenere() {
        return genere;
    }



    @Override
    public String toString() {
        return "Libri{" +
                "autore='" + autore + '\'' +
                ", genere='" + genere + '\'' +
                ", isbn='" + isbn + '\'' +
                ", titolo='" + titolo + '\'' +
                ", anno=" + anno +
                ", numPagine=" + numPagine +
                '}';
    }
}
