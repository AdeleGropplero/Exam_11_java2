package esami.epicode;

import com.github.javafaker.Faker;
import esami.epicode.Classi.Enum.Genere;
import esami.epicode.Classi.Enum.Periodicità;
import esami.epicode.Classi.Libri;
import esami.epicode.Classi.PubblicazioneNonTrovataException;
import esami.epicode.Classi.Pubblicazioni;
import esami.epicode.Classi.Riviste;

import java.util.*;
import java.util.stream.Collectors;


public class Archivio {
    private static final Set<Pubblicazioni> archivio = new HashSet<>(); // Variabile di classe
    static Faker faker = new Faker(Locale.ITALY);

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        generateBooks();
        generateRiviste();
        getArchivio();

        while (true) {
            System.out.println("Quale operazione vuoi eseguire?");
            System.out.println("Premi -1- per aggiungere un elemento.");
            System.out.println("Premi -2- per effettuare una ricerca tramite ISBN");
            System.out.println("Premi -3- per rimuovere un elemento dato un codice ISBN");
            System.out.println("Premi -4- per effettuare una ricerca per anno di pubblicazione");
            System.out.println("Premi -5- per effettuare una ricerca tramite autore");
            System.out.println("Premi -6- per effettuare una MODIFICA su un elemento esistente tramite ISBN");
            System.out.println("Premi -7- per accedere alle statistiche del catalogo");
            System.out.println("Premi -0- per chiudere il programma.");

            int operazione = sc.nextInt();
            sc.nextLine();

            switch (operazione) {
                case 1:
                    archivio.add(addPubblicazione(sc));
                    getArchivio();
                    break;

                case 2:
                    try {
                        Pubblicazioni pubblicazione = getByIsbn(sc);  // Cerca se esiste la pubblicazione
                        System.out.println("Pubblicazione cercata: " + pubblicazione);
                    } catch (PubblicazioneNonTrovataException e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case 3:
                    try {
                        removeByIsbn(sc);
                    } catch (PubblicazioneNonTrovataException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 4:
                    try {
                        findByAnno(sc);
                    } catch (PubblicazioneNonTrovataException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 5:
                    try {
                        findByAutore(sc);
                    } catch (PubblicazioneNonTrovataException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 6:
                    try {
                        modifyElement(sc);
                    } catch (PubblicazioneNonTrovataException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 7:
                    try {
                        getStats();
                    } catch (PubblicazioneNonTrovataException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 0:
                    System.out.println("Chiudo...");
                    sc.close();
                    return;

            }
        }


    }

    /*------------------------FINE MAIN------------------------------------------------------------------------------*/
    // Preparazione agli esercizi
    public static void generateBooks() {
        for (int i = 0; i < 3; i++) { //Con faker generiamo n libri da aggiungere in archivio
            Genere randGenere = getRandomGenere(); //leggere nota su Genere(Enum).
            Libri libro = new Libri(
                    faker.book().author(),
                    randGenere,
                    faker.code().isbn10(),
                    faker.book().title(),
                    faker.number().numberBetween(1700, 2025),
                    faker.number().numberBetween(120, 900)
            );
            archivio.add(libro);
        }
    }

    public static void generateRiviste() {
        for (int i = 0; i < 3; i++) {
            Periodicità randPeriodicità = getRandomPeriodicità();
            Riviste rivista = new Riviste(
                    faker.code().isbn10(),
                    faker.book().title(),
                    faker.number().numberBetween(1920, 2025),
                    faker.number().numberBetween(30, 100),
                    randPeriodicità
            );
            archivio.add(rivista);
        }
    }

    public static void getArchivio() {
        System.out.println(archivio);
        System.out.println(" ");
        System.out.println("Pubblicazioni presenti in archivio: " + archivio.size());
        System.out.println(" ");
    }

    public static Genere getRandomGenere() {
        Genere[] generi = Genere.values();
        int randIndex = new Random().nextInt(generi.length); // l'intervallo generato è già [0, generi.length - 1](grazie al bound di nextInt),
        // copre, quindi, tutti gli indici validi dell'array generi.
        return generi[randIndex];
    }

    public static Periodicità getRandomPeriodicità() {
        Periodicità[] periodicità = Periodicità.values();
        int randIndex = new Random().nextInt(periodicità.length);
        return periodicità[randIndex];
    }

    public static Genere defineGenere(java.util.Scanner sc) {
        System.out.println("Inserisci il genere tra:");
        for (Genere g : Genere.values()) {
            System.out.println("- " + g);
        }

        String genereInput = sc.nextLine();
        Genere genere;

        try {
            // Converte l'input dell'utente in un valore enum
            genere = Genere.valueOf(genereInput.toUpperCase());
        } catch (IllegalArgumentException e) {
            // Gestisce il caso di input non valido
            System.out.println("Genere non valido! Verrà usato il genere FICTION di default.");
            genere = Genere.FICTION;
        }

        return genere; // Ritorna il valore del genere
    }

    public static Periodicità definePeriodicità(java.util.Scanner sc) {
        System.out.println("Inserisci la periodicità tra:");
        for (Periodicità periodicità : Periodicità.values()) {
            System.out.println("- " + periodicità);
        }
        String periodicitàInput = sc.nextLine();
        Periodicità periodicità;
        try {
            periodicità = Periodicità.valueOf(periodicitàInput.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Periodicità non valida! Verrà usata MENSILE di default.");
            periodicità = Periodicità.MENSILE;
        }

        return periodicità; // Ritorna il valore del genere
    }




    // Es.1 ------Aggiunta di un elemento------------------------------------------------------------------------------------------
    public static Pubblicazioni addPubblicazione(java.util.Scanner sc) {
        System.out.println("Che tipo di pubblicazione vuoi aggiungere all'archivio? Un libro o una rivista?");
        System.out.println("Premi -1- per aggiungere un libro");
        System.out.println("Premi -2- per aggiungere una rivista");

        int sceltaPub = sc.nextInt();
        sc.nextLine();

        if (sceltaPub == 1) {
            // Aggiunta di un libro
            System.out.println("Inserisci l'autore o l'autrice del libro:");
            String autoreUtente = sc.nextLine();

            System.out.println("Inserisci il titolo del libro:");
            String titolo = sc.nextLine();

            Genere genereSelezionato = defineGenere(sc);

            System.out.println("Inserisci l'anno di pubblicazione:");
            int anno = sc.nextInt();
            sc.nextLine();

            System.out.println("Inserisci il numero di pagine:");
            int numPagine = sc.nextInt();
            sc.nextLine();

            String isbn = generateUniqueIsbn(); //controllo su isbn
            return new Libri(autoreUtente, genereSelezionato, isbn, titolo, anno, numPagine);

        } else if (sceltaPub == 2) {
            // Aggiunta di una rivista
            System.out.println("Inserisci il titolo della rivista:");
            String titolo = sc.nextLine();

            System.out.println("Inserisci l'anno di pubblicazione:");
            int anno = sc.nextInt();
            sc.nextLine();

            System.out.println("Inserisci il numero di pagine:");
            int numPagine = sc.nextInt();
            sc.nextLine();

            String isbn = generateUniqueIsbn();

            Periodicità periodicità = definePeriodicità(sc);

            return new Riviste(isbn, titolo, anno, numPagine, periodicità);

        } else {
            System.out.println("Scelta non valida! Nessuna pubblicazione aggiunta.");
            return null;
        }
    }

    // Funzione per generare un ISBN univoco
    public static String generateUniqueIsbn() {
        String isbn;  // Dichiarazione della variabile ISBN

        // Ciclo infinito finché non si genera un ISBN univoco
        while (true) {
            isbn = faker.code().isbn10();
            //Dal momento che ci sono 110 miliardi di combinazioni possibili dubito
            //che ci sia la possibilità di generare due isbn identici.
            //Motivo per cui ho scelto che venga generato automaticamente invece
            //che fare in modo che sia l'utente a inserirlo manualmente.

            // Prova a verificare se l'ISBN esiste già
            final String finalIsbn = isbn;  // Creiamo una variabile finale per la lambda, ho dovuto fare
            //così perchè non mi prendeva isbn e basta.

            boolean isbnEsistente = archivio.stream()
                    .anyMatch(pubblicazione -> pubblicazione.getIsbn().equals(finalIsbn));
            // Confronto con l'ISBN di qualsiasi pubblicazione, anyMatch restituisce un booleano quindi se
            // non trova una corrispondenza restituisce false e fa uscire dal ciclo while.

            // Se l'ISBN è unico, esce dal ciclo
            if (!isbnEsistente) {
                System.out.println("L'ISBN della pubblicazione è: " + isbn);
                break;
            }
        }

        return isbn;  // Restituisce l'ISBN univoco
    }

    // Es. 2 -----------Ricerca per ISBN + eccezione custom-------------------------------------------------------------------------------------

    public static Pubblicazioni getByIsbn(java.util.Scanner sc) throws PubblicazioneNonTrovataException {
        System.out.println("Inserisci il codice ISBN per cercare una pubblicazione in archivio");
        String utenteIsbn = sc.nextLine();
        return archivio.stream()
                .filter(pubblicazione -> pubblicazione.getIsbn().equals(utenteIsbn))
                .findFirst().orElseThrow(() -> new PubblicazioneNonTrovataException("Pubblicazione con ISBN " + utenteIsbn + " non trovata."));
    }

    // Es. 3 ----------Rimozione tramite ISBN--------------------------------------------------------------------------------------

    public static void removeByIsbn(java.util.Scanner sc) throws PubblicazioneNonTrovataException {
        System.out.println("Inserisci il codice ISBN per rimuovere una pubblicazione in archivio");
        String utenteIsbn = sc.nextLine();
        Pubblicazioni elementoDaRimuovere = archivio.stream().
                filter(pubblicazione -> pubblicazione.getIsbn().equals(utenteIsbn))
                .findFirst().orElseThrow(() -> new PubblicazioneNonTrovataException("Pubblicazione con ISBN " + utenteIsbn + " non trovata."));

        archivio.remove(elementoDaRimuovere); //utilizzo remove(), che è uno dei metodi di set.

        System.out.println("Elemento rimosso dall'archivio: " + elementoDaRimuovere);

        // Stampa archivio aggiornato
        System.out.println("-----------------------------------------");
        System.out.println("ARCHIVIO AGGIORNATO:");
        System.out.println("-----------------------------------------");
        getArchivio();
    }

    // Es. 4 ----------Rimozione tramite anno di pubblicazione--------------------------------------------------------------------------------------
    public static void findByAnno(java.util.Scanner sc) throws PubblicazioneNonTrovataException {
        System.out.println("Inserisci l'anno per cercare una pubblicazione in archivio");
        int utenteAnno = sc.nextInt();
        sc.nextLine();
        List<Pubblicazioni> ricercaAnno = archivio.stream().filter(pubblicazioni -> pubblicazioni.getAnno() == utenteAnno).collect(Collectors.toList());
        if (!ricercaAnno.isEmpty()) {
            System.out.println(ricercaAnno);
        } else {
            throw new PubblicazioneNonTrovataException("Pubblicazione uscita nell'anno " + utenteAnno + " non trovata.");
        }
    }

    // Es. 5 ----------Ricerca tramite autore--------------------------------------------------------------------------------------

    public static void findByAutore(java.util.Scanner sc) throws PubblicazioneNonTrovataException {
        System.out.println("Inserisci l'autore per cercare una pubblicazione in archivio");
        String utenteAutore = sc.nextLine();
        List<Libri> ricercaPerAutore = archivio.stream().filter(pubblicazioni -> pubblicazioni instanceof Libri) //filtro le pubblicazioni per libri
                .map(pubblicazioni -> ((Libri) pubblicazioni)) //faccio il cast dei rimanenti in libri così da poter usare getAutore()
                .filter(libri -> libri.getAutore().equals(utenteAutore)) // i libri restanti li filtro per autore
                .collect(Collectors.toList()); //mi ricavo una nuova lista.
        if (!ricercaPerAutore.isEmpty()) {
            System.out.println(ricercaPerAutore);
        } else {
            throw new PubblicazioneNonTrovataException("Libro dell'autore: " + utenteAutore + " non trovato.");
        }
    }

    // Es. 6 ----------Modifica elemento tramite ISBN--------------------------------------------------------------------------------------
    public static void modifyElement(java.util.Scanner sc) throws PubblicazioneNonTrovataException {
        System.out.println("Inserisci il codice ISBN della pubblicazione che vuoi modificare");
        String utenteIsbn = sc.nextLine();
        Pubblicazioni elementoDaModificare = archivio.stream().
                filter(pubblicazione -> pubblicazione.getIsbn().equals(utenteIsbn))
                .findFirst().orElseThrow(() -> new PubblicazioneNonTrovataException("Pubblicazione con ISBN " + utenteIsbn + " non trovata."));
        System.out.println("Hai selezionato: " + "\n" + elementoDaModificare);
        if (elementoDaModificare instanceof Libri) {
            Libri libro = (Libri) elementoDaModificare; //faccio il cast a libro.
            boolean continua = true; //mi serve per fare in modo che l'utente sia in controllo su quante cose vuole modificare prima di uscire.

            do {
                System.out.println("Cosa vuoi modificare?");
                System.out.println("1. Autore");
                System.out.println("2. Genere");
                System.out.println("3. Titolo");
                System.out.println("4. Anno");
                System.out.println("5. Numero di pagine");
                System.out.println("6. Esci.");

                int scelta = sc.nextInt();
                sc.nextLine();

                switch (scelta) {
                    case 1:
                        System.out.println("Inserisci il nuovo autore:");
                        String nuovoAutore = sc.nextLine();
                        libro.setAutore(nuovoAutore);
                        System.out.println(" ");
                        System.out.println("--- Autore modificato con successo ---");
                        System.out.println(elementoDaModificare);
                        break;
                    case 2:
                        System.out.println("Inserisci il nuovo genere:");
                        Genere nuovoGenere = defineGenere(sc);
                        libro.setGenere(nuovoGenere);
                        System.out.println(" ");
                        System.out.println("--- Genere modificato con successo ---");
                        System.out.println(elementoDaModificare);
                        break;
                    case 3:
                        System.out.println("Inserisci il nuovo titolo:");
                        String nuovoTitolo = sc.nextLine();
                        libro.setTitolo(nuovoTitolo);
                        System.out.println(" ");
                        System.out.println("--- Titolo modificato con successo ---");
                        System.out.println(elementoDaModificare);
                        break;
                    case 4:
                        System.out.println("Inserisci il nuovo anno di pubblicazione:");
                        int nuovoAnno = sc.nextInt();
                        sc.nextLine();
                        libro.setAnno(nuovoAnno);
                        System.out.println(" ");
                        System.out.println("--- Anno di pubblicazione modificato con successo ---");
                        System.out.println(elementoDaModificare);
                        break;
                    case 5:
                        System.out.println("Inserisci il nuovo numero di pagine:");
                        int nuovoNumPagine = sc.nextInt();
                        sc.nextLine();
                        libro.setNumPagine(nuovoNumPagine);
                        System.out.println(" ");
                        System.out.println("--- Numero pagine modificato con successo ---");
                        System.out.println(elementoDaModificare);
                        break;
                    case 6:
                        continua = false;
                        break;
                    default:
                        System.out.println("Per favore inserisci un valore compreso tra 1 e 6");
                }
                if (scelta != 6) {
                    System.out.println("Desideri continuare a modificare altro?");
                    System.out.println("-1- Sì.");
                    System.out.println("-2- No.");
                    int risposta = sc.nextInt();
                    sc.nextLine();
                    if (risposta == 1) {
                        continua = true;
                    } else if (risposta == 2) {
                        continua = false;
                    }else {
                        System.out.println("Scelta non valida. Premi -1- per continuare o -2- per uscire.");
                    }

                }
            } while (continua);
            System.out.println("--- Modifiche terminate ---");
            System.out.println("Elemento aggiornato: " + elementoDaModificare);

            //ora prendiamo il caso della rivista -----------------------------------------------
        }else {
            Riviste rivista = (Riviste) elementoDaModificare;
            boolean continua = true;

            do {
                System.out.println("Cosa vuoi modificare?");

                System.out.println("-1-. Titolo");
                System.out.println("-2- Anno");
                System.out.println("-3- Numero di pagine");
                System.out.println("-4- Periodicità");
                System.out.println("-5- Esci.");

                int scelta = sc.nextInt();
                sc.nextLine();

                switch (scelta) {
                    case 1:
                        System.out.println("Inserisci il nuovo titolo:");
                        String nuovoTitolo = sc.nextLine();
                        rivista.setTitolo(nuovoTitolo);
                        System.out.println("--- Titolo modificato con successo ---");
                        break;
                    case 2:
                        System.out.println("Inserisci il nuovo anno di pubblicazione:");
                        int nuovoAnno = sc.nextInt();
                        sc.nextLine();
                        rivista.setAnno(nuovoAnno);
                        System.out.println("--- Anno di pubblicazione modificato con successo ---");
                        break;
                    case 3:
                        System.out.println("Inserisci il nuovo numero di pagine:");
                        int nuovoNumPagine = sc.nextInt();
                        sc.nextLine();
                        rivista.setNumPagine(nuovoNumPagine);
                        System.out.println("--- Numero di pagine modificato con successo ---");
                        break;
                    case 4:
                        System.out.println("Inserisci il nuovo genere:");
                        Periodicità nuovaPeriodicità = definePeriodicità(sc);
                        rivista.setPeriodicità(nuovaPeriodicità);
                        System.out.println(" ");
                        System.out.println("--- Genere modificato con successo ---");
                        System.out.println(elementoDaModificare);
                        break;
                    case 5:
                        continua = false;
                        break;
                    default:
                        System.out.println("Scelta non valida. Riprova.");
                }
                if (scelta != 5) {
                    System.out.println("Desideri continuare a modificare altro?");
                    System.out.println("Premi -1- se vuoi continuare.");
                    System.out.println("Premi -2- per uscire.");
                    int risposta = sc.nextInt();
                    sc.nextLine();
                    if (risposta == 1) {
                        continua = true;
                    } else if (risposta == 2) {
                        continua = false;
                    }else {
                        System.out.println("Scelta non valida. Premi -1- per continuare o -2- per uscire.");
                    }

                }
            } while (continua);
            System.out.println("--- Modifiche terminate ---");
            System.out.println("Elemento aggiornato: " + elementoDaModificare);
        }
    }

    // Es. 7 ----------Stampa statistiche--------------------------------------------------------------------------------------

    public static void getStats() throws PubblicazioneNonTrovataException {
        long numeroLibri = archivio.stream().filter(pubblicazioni -> pubblicazioni instanceof Libri).count(); //era necessario il long
        long numeroRiviste = archivio.stream().filter(pubblicazioni -> pubblicazioni instanceof Riviste).count();
        int numTotPubblicazioni = archivio.size();
        Pubblicazioni maxNumPagine = archivio.stream().max(Comparator.comparing(Pubblicazioni::getNumPagine))
                                     .orElseThrow(() ->new PubblicazioneNonTrovataException("Pubblicazione non trovata."));
        double averagePagine = archivio.stream().mapToInt(Pubblicazioni::getNumPagine).average()
                                     .orElseThrow(() ->new PubblicazioneNonTrovataException("Pubblicazione non trovata."));

        System.out.println("Ecco le statistiche del tuo archivio:");
        System.out.println("Numero di tutte le pubblicazioni presenti: " + numTotPubblicazioni);
        System.out.println("Di cui libri: " + numeroLibri);
        System.out.println("Di cui riviste: " + numeroRiviste);
        System.out.println("------------------------------------");
        System.out.println("La pubblicazione con il massimo numero di pagine: \n" + maxNumPagine);
        System.out.println("------------------------------------");
        System.out.println("La media delle pagine di tutte le pubblicazioni: " + averagePagine);

    }

}





















