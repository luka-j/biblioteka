package rs.luka.biblioteka.data;

import java.util.Objects;
import rs.luka.biblioteka.exceptions.LosFormat;
import rs.luka.biblioteka.exceptions.NemaViseKnjiga;
import rs.luka.biblioteka.exceptions.Prazno;

/**
 * @author Luka
 * @since 3.7.'13.
 */
public class Knjiga implements Comparable<Knjiga> {
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
     * Prethodna knjiga koja se proveravala putem {@link #isNaslovUnique()}
     */
    private static Knjiga prevKnjiga;

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
     * Oznacava da li postoji jos neka knjiga s istim naslovom.
     */
    private boolean isNaslovUnique = true;

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
     * @throws rs.luka.biblioteka.exceptions.Prazno ako je naslov null ili ""
     */
    public Knjiga(String naslov, int kolicina, String pisac) throws Prazno, LosFormat {
        if(naslov == null || naslov.isEmpty()) {
            throw new Prazno("Prazan argument prosledjen konstruktoru");
        }
        if(naslov.contains(splitString) || pisac.contains(splitString)) {
            throw new LosFormat("splitString u imenu naslova ili pisca");
        }
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
    
    public boolean isNaslovUnique() {
        return isNaslovUnique;
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
        if(pisac!=null) {
            string.append(pisac);
        }
        return string.toString();
    }
    
    public String getDisplayName() {
        if(isNaslovUnique) {
            return naslov;
        }
        else return naslov + ", " + pisac;
    }

    //SETTERI
    /**
     * Proverava da li prethodna knjiga ima isti naslov i postavlja flag {@link #isNaslovUnique}
     * @return this, da dozvoli chaining
     */
    protected Knjiga checkUniqueness() {
        if(prevKnjiga != null && prevKnjiga.naslov.equalsIgnoreCase(naslov)) {
            this.isNaslovUnique = false;
            prevKnjiga.isNaslovUnique = false;
        }
        prevKnjiga = this;
        return this;
    }
    
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
        System.out.println("Povecavam kolicinu knjige " + this + " na " + kolicina);
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
        if(knj==null)
            return -1;
        int compNaslov = this.naslov.compareToIgnoreCase(knj.naslov);
        if(compNaslov != 0)
            return compNaslov;
        else return this.pisac.compareToIgnoreCase(knj.pisac);
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
        /*if(this==knj)
            return true;*/
        if(!(knj instanceof Knjiga)) {
            return false;
        }
        Knjiga knjiga = (Knjiga) knj;
        return knjiga.getNaslov().equalsIgnoreCase(naslov) && knjiga.getPisac().equalsIgnoreCase(pisac);
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
