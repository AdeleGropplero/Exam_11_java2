package esami.epicode.Classi;

public abstract class Pubblicazioni {
    protected String isbn;
    protected String titolo;
    protected int anno;
    protected int numPagine;

    public Pubblicazioni(String isbn, String titolo, int anno, int numPagine) {
        this.isbn = isbn;
        this.titolo = titolo;
        this.anno = anno;
        this.numPagine = numPagine;
    }

    public String getIsbn() {
        return this.isbn;
    }

    public String getTitolo() {
        return this.titolo;
    }

    public int getAnno() {
        return this.anno;
    }

    public int getNumPagine() {
        return this.numPagine;
    }

}
