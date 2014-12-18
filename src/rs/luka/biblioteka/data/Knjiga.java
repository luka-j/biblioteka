package rs.luka.biblioteka.data;

import java.util.Objects;
import rs.luka.biblioteka.exceptions.NemaViseKnjiga;
import rs.luka.biblioteka.exceptions.Prazno;

/**
 * @author Luka
 * @since 3.7.'13.
 */
public class Knjiga implements Comparable<Knjiga> {

    /**
     * Naslov knjige.
     */
    private final String naslov;
    /**
     * Trenutni broj knjiga tog naslova u biblioteci.
     */
    private int kolicina;
    /**
     * Pisac knjige.
     */
    private String pisac;
    /**
     * Karakter koji se u fajlovima koristi za razdvajanje polja.
     * @since 23.9.'14.
     */
    private static final char splitChar = '/';
    /**
     * String wrapper za {@link #splitChar}.
     * @since 23.9.'14.
     */
    private static final String splitString = new String(new char[]{splitChar});

    /**
     * Konstruktor koji ne uzima pisca u obzir.
     *
     * @param naslov naslov knjige
     * @param kolicina broj knjiga koje su trenutno u biblioteci
     * @deprecated
     */
    public Knjiga(String naslov, int kolicina) {
        this.naslov = naslov;
        this.kolicina = kolicina;
    }

    /**
     * Konstruktor koji uzima sve vrednosti u obzir.
     *
     * @param naslov naslov knjige
     * @param kolicina broj knjiga koje su trenutno u biblioteci
     * @param pisac opciono, pisac knjige
     */
    public Knjiga(String naslov, int kolicina, String pisac) throws Prazno {
        if(naslov == null || naslov.isEmpty())
            throw new Prazno("Prazan argument prosledjen konstruktoru");
        this.naslov = naslov;
        this.kolicina = kolicina;
        this.pisac = pisac;
    }
    
    /**
     * Konstruktuje objekat na osnovu datog stringa, koji bi trebalo da dolazi iz fajla sa podacima.
     * @param IOString String sa podacima
     * @since 23.9.'14.
     */
    Knjiga(String IOString) {
        String [] fields = IOString.split(splitString);
        this.naslov = fields[0];
        this.kolicina = Integer.parseInt(fields[1]);
        if(fields.length > 2)
            this.pisac = fields[2];
    }
    
    /**
     * Pravi istovetnu koppiju date Knjige.
     * @param knjiga Knjiga za kopiranje
     * @since 3.10.'14.
     */
    Knjiga(Knjiga knjiga) {
        naslov = knjiga.getNaslov(); //primitivne i immutable vrednosti -
        kolicina = knjiga.getKolicina(); //nema potrebe za necim komplikovanijim
        pisac = knjiga.getPisac();
    }

    //GETTERI
    public String getNaslov() {
        return naslov;
    }

    public int getKolicina() {
        return kolicina;
    }

    public String getPisac() {
        return pisac;
    }
    
    /**
     * Vraca objekat kao string, namenjen za upis u fajl.
     * Redosled: naslov + {@link #splitChar} + kolicina + {@link #splitChar} + pisac
     * @return Reprezentaciju objekta u jednom stringu
     * @since 23.9.'14.
     */
    public String getAsIOString() {
        StringBuilder string = new StringBuilder(naslov);
        string.append(splitChar).append(kolicina).append(splitChar);
        if(pisac!=null)
            string.append(pisac);
        return string.toString();
    }

    //SETTERI
    /**
     * Smanjuje kolicinu date knjige
     * @throws NemaViseKnjiga ako je kolicina manja od 1, jer ne moze biti negativna
     */
    public void smanjiKolicinu() throws NemaViseKnjiga {
        if (kolicina < 1) {
            throw new rs.luka.biblioteka.exceptions.NemaViseKnjiga(naslov);
        }
        kolicina--;
    }

    public void povecajKolicinu() {
        kolicina++;
    }

    //OVERRIDES
    /**
     * Uporedjuje knjige na osnovu naslova. Koristi {@link String#compareToIgnoreCase(java.lang.String)}
     * 
     * @param knj Knjiga sa kojom se uporedjuje
     * @return default
     * @since 23.6.2014.
     */
    @Override
    public int compareTo(Knjiga knj) {
        return this.naslov.compareToIgnoreCase(knj.getNaslov());
    }

    /**
     * Vraca user-friendly deskripciju objekta (naslov, kolicinu i pisca).
     * @return objekat kao String
     */
    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        string.append("Naslov: ").append(naslov).append("\nKolicina: ").append(kolicina);
        if (pisac != null && !pisac.isEmpty()) {
            string.append("\nPisac: ").append(pisac);
        }
        return string.toString();
    }
    
    /**
     * Uporedjuje Knjige na osnovu naslova i pisca. 
     * @param knj knjiga
     * @return Ako su oba jednaka, vraca true, u suprotnom false
     */
    @Override
    public boolean equals(Object knj) {
        if(!(knj instanceof Knjiga))
            return false;
        Knjiga knjiga = (Knjiga) knj;
        return knjiga.getNaslov().equals(naslov) && knjiga.getPisac().equals(pisac);
    }

    /**
     * Contract sa equals. Automatski generisano
     * @return hashCode objekta Knjiga, koji se gradi na osnovu naslova i pisca
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.naslov);
        hash = 89 * hash + Objects.hashCode(this.pisac);
        return hash;
    }
}
