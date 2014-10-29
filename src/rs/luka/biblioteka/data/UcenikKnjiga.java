package rs.luka.biblioteka.data;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 * Klasa za knjige koje se nalaze kod ucenika. Sadrzi naslov knjige i datum kada
 * je izdata.
 *
 * @author Luka
 * @since 7. 17. 2014.
 */
public class UcenikKnjiga {

    /**
     * Naslov knjige koja je izdata uceniku.
     */
    private String naslov;
    /**
     * Datum kada je data knjiga izdata.
     */
    private Date datum;
    /**
     * Karakter koji se u fajlovima koristi za razdvajanje polja. Ne sme biti
     * isti kao splitChar klase Ucenik ili Knjiga
     *
     * @since 23.9.'14.
     */
    private final char splitChar = '@';
    /**
     * String wrapper za {@link #splitChar}.
     *
     * @since 23.9.'14.
     */
    private final String splitString = new String(new char[]{splitChar});

    public UcenikKnjiga(String naslov, Date datum) {
        this.naslov = naslov;
        this.datum = datum;
    }

    /**
     * Konstruktuje objekat na osnovu datog stringa, koji bi trebalo da dolazi
     * iz fajla sa podacima.
     *
     * @param IOString String sa podacima
     * @throws ParseException ako dodje do greske pri parsiranju datuma
     * @since 23.9.'14.
     */
    UcenikKnjiga(String IOString) throws ParseException {
        String[] fields = IOString.split(splitString);
        if (fields.length == 0) {
            naslov = "";
            datum = null;
        } else {
            naslov = fields[0];
            datum = Datumi.df.parse(fields[1]);
        }
    }

    /**
     * Pravi istovetnu kopiju datog objekta.
     *
     * @param uk UcenikKnjiga za kopiranje
     * @since 3.10.'14.
     */
    UcenikKnjiga(UcenikKnjiga uk) {
        naslov = uk.getNaslov();
        if (uk.isEmpty()) {
            datum = null;
        } else {
            datum = new Date(uk.getDatum().getTime());
        }
    }

    //GETTERI
    public String getNaslov() {
        return naslov;
    }

    /**
     * 
     */
    protected Date getDatum() {
        return datum;
    }

    /**
     * @return Datum kao I/O String
     * @see Datumi#df
     */
    protected String getDatumAsString() {
        if (datum == null) {
            return "";
        }
        return Datumi.df.format(datum);
    }

    /**
     * Vraca objekat kao string, namenjen za upis u fajl.
     *
     * @return Reprezentaciju objekta u jednom stringu
     * @since 23.9.'14.
     */
    public String getAsIOString() {
        return naslov + splitString + getDatumAsString();
    }

    //SETTERI
    protected void clear() {
        naslov = "";
        datum = null;
    }

    protected void setNaslov(String naslov) {
        this.naslov = naslov;
        datum = new Date();
    }

    protected void setDatum(Date datum) {
        this.datum = datum;
    }

    //is... METODE
    public boolean isEmpty() {
        return naslov.isEmpty();
    }

    //OVERRIDES
    @Override
    public String toString() {
        DateFormat df = new SimpleDateFormat("dd. MM. yyyy");
        if (datum != null && naslov != null) {
            return naslov + ", iznajmljena " + df.format(datum);
        } else if (datum == null && naslov.isEmpty()) {
            return "Prazno";
        } else {
            throw new ClassFormatError("Ucenik knjiga nije u redu :s");
        }
    }

    /**
     * Vraca true ako je Object vrste UcenikKnjiga, trenutna i UcenikKnjiga sa
     * kojom se uporedjuje prazna ili ako sadrze isti naslov i datum.
     *
     * @param uk UcenikKnjiga sa kojom se uporedjuje
     * @return true ako se vrednosti poklapaju, false u suprotnom
     * @since 28.9.'14.
     */
    @Override
    public boolean equals(Object uk) {
        if (!(uk instanceof UcenikKnjiga)) {
            return false;
        }
        UcenikKnjiga knjiga = (UcenikKnjiga) uk;
        return knjiga.isEmpty() && this.isEmpty()
                || (knjiga.getNaslov().equals(naslov) && knjiga.getDatum().equals(datum));
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + Objects.hashCode(this.naslov);
        hash = 53 * hash + Objects.hashCode(this.datum);
        return hash;
    }
}
