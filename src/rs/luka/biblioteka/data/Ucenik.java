package rs.luka.biblioteka.data;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Objects;
import javax.swing.JOptionPane;
import rs.luka.biblioteka.exceptions.Duplikat;
import rs.luka.biblioteka.exceptions.PreviseKnjiga;
import rs.luka.biblioteka.exceptions.VrednostNePostoji;
import rs.luka.biblioteka.exceptions.VrednostNePostoji.vrednost;
import rs.luka.biblioteka.funkcije.Utils;

/**
 *
 * @author Luka
 * @since 3.7.'13.
 */
public class Ucenik implements Comparable<Ucenik> {

    /**
     * Ime i prezime ucenika.
     */
    private final String ime;
    /**
     * Razred u koji ucenik trenutno ide.
     */
    private int razred;
    /**
     * Knjige koje su trenutno kod ucenika.
     */
    private final UcenikKnjiga[] knjige;
    /**
     * Validni razredi.
     */
    protected static int validRazred[] = {1, 2, 3, 4, 5, 6, 7, 8};
    /**
     * Karakter koji se u fajlovima koristi za razdvajanje polja.
     *
     * @since 23.9.'14.
     */
    private static final char splitChar = '/';
    /**
     * String wrapper za {@link #splitChar}.
     *
     * @since 23.9.'14.
     */
    private static final String splitString = new String(new char[]{splitChar});

    //STATIC:
    /**
     * Postavlja validne razrede prema datom Stringu, koji je razdvojen
     * zapetama. Radi exception-handling.
     *
     * @since 6.10.'14.
     */
    public static void setValidRazred() {
        if (Config.hasKey("razredi")) {
            String razredi = Config.get("razredi");
            String[] split = razredi.split(",");
            try {
                validRazred = new int[split.length];
                for (int i = 0; i < split.length; i++) {
                    validRazred[i] = Integer.parseInt(split[i]);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Podešavanje validnih razreda "
                        + "neuspelo zbog lošeg Stringa.\nBiće korišćene default vrednosti",
                        "Greška pri inicijalizaciji", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Proverava da li je dati razred validan.
     *
     * @param razred razred koji se proverava
     * @return true ako jeste (tj. ako se nalazi u nizu validRazred), false u
     * suprotnom
     * @since 6.10.'14.
     */
    public static boolean isRazredValid(int razred) {
        return Utils.arrayContains(validRazred, razred);
    }

    /**
     * Vraca broj validnih razreda (npr za 7, 8, 1, 2, 3, 4 vraca 6).
     *
     * @return duzina niza validRazred
     * @since 6.10.'14.
     */
    public static int getBrojRazreda() {
        return validRazred.length;
    }

    /**
     * Vraca prvi razred u nizu validnih.
     *
     * @return element na indexu 0 u nizu validRazred
     * @since 17.10.'14.
     */
    public static int getPrviRazred() {
        return validRazred[0];
    }

    //KONSTRUKTORI:
    public Ucenik(String ime, int razred, String[] knjige) {
        this.ime = ime;
        if (Utils.arrayContains(validRazred, razred)) {
            this.razred = razred;
        } else {
            throw new NumberFormatException("Loš razred: " + razred);
        }
        this.knjige = new UcenikKnjiga[Podaci.getMaxBrojUcenikKnjiga()];
        for (int i = 0; i < knjige.length; i++) {
            this.knjige[i] = new UcenikKnjiga(knjige[i], new java.util.Date());
        }
        for (int i = knjige.length; i < this.knjige.length; i++) {
            this.knjige[i] = new UcenikKnjiga("", null);
        }
    }

    /**
     * Konstruktuje objekat na osnovu datog stringa, koji bi trebalo da dolazi
     * iz fajla sa podacima.
     *
     * @param IOString String sa podacima
     * @throws ParseException ako dodje do greske pri parsiranju datuma
     * @since 23.9.'14.
     */
    Ucenik(String IOString) throws ParseException {
        String[] fields = IOString.split(splitString);
        ime = fields[0];
        razred = Integer.parseInt(fields[1]);
        knjige = new UcenikKnjiga[fields.length - 2];
        for (int i = 2; i < fields.length; i++) {
            knjige[i - 2] = new UcenikKnjiga(fields[i]);
        }
    }

    /**
     * Kreira istovetnu kopiju Ucenika.
     *
     * @param ucenik ucenik cija se kopija trazi
     * @since 3.10.'14.
     */
    public Ucenik(Ucenik ucenik) {
        ime = ucenik.getIme();
        razred = ucenik.getRazred();
        knjige = new UcenikKnjiga[ucenik.getMaxBrojKnjiga()];
        for (int i = 0; i < ucenik.getMaxBrojKnjiga(); i++) //ovo je deepcopy, ne koristiti System.arraycopy
        {
            knjige[i] = new UcenikKnjiga(ucenik.getKnjiga(i));
        }
    }

    //GETTERI
    public String getIme() {
        return ime;
    }

    public int getRazred() {
        return razred;
    }
    
    public String getNaslovKnjige(int i) {
        return getKnjiga(i).getNaslov();
    }
    
    public String getDatumKnjige(int i) {
        return getKnjiga(i).getDatumAsString();
    }
    
    public long getTimeKnjige(int i) {
        return getKnjiga(i).getDatum().getTime();
    }
    
    private UcenikKnjiga[] getKnjige() {
        return knjige;
    }

    private UcenikKnjiga getKnjiga(int i) {
        if(knjige.length>i)
            return knjige[i];
        else return new UcenikKnjiga();
    }

    /**
     * Vraca objekat kao string, namenjen za upis u fajl. Redosled: ime +
     * {@link #splitChar} + razred + {@link #splitChar} +
     * knjige[0].{@link UcenikKnjiga#getAsIOString()} + {@link #splitChar} + ...
     * + {@link #splitChar} + knjige[n].{@link UcenikKnjiga#getAsIOString()}
     *
     * @return Reprezentaciju objekta u jednom stringu
     * @since 23.9.'14.
     */
    public String getAsIOString() {
        StringBuilder string = new StringBuilder(ime);
        string.append(splitChar).append(razred);
        for(UcenikKnjiga knjiga :knjige) {
            string.append(splitChar).append(knjiga.getAsIOString());
        }
        for(int i=knjige.length; i<Podaci.getMaxBrojUcenikKnjiga(); i++) {
            string.append(splitChar);
        }
        return string.toString();
    }

    /**
     * @return broj mesta za knjige kod ucenika
     */
    public int getMaxBrojKnjiga() {
        return knjige.length;
    }

    /**
     * @return broj knjiga koje se trenutno nalaze kod ucenika
     */
    public int getBrojKnjiga() {
        int count = 0;
        for (UcenikKnjiga knjige1 : knjige) {
            if (!knjige1.isEmpty()) {
                count++;
            }
        }
        return count;
    }

    //boolean METODE
    public boolean isKnjFull() {
        for (UcenikKnjiga knjige1 : knjige) {
            if (knjige1.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Proverava da li se kod ucenika trenutno nalazi knjiga sa datim naslovom.
     *
     * @param knjiga knjiga koja se testira
     * @return true ako se nalazi, false u suprotnom
     */
    public boolean hasKnjiga(String knjiga) {
        for (UcenikKnjiga knjige0 : knjige) {
            if (knjige0.getNaslov().equalsIgnoreCase(knjiga)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean isKnjigaEmpty(int i) {
        if(knjige.length>i)
            return knjige[i].isEmpty();
        else return false;
    }

    //SETTERI
    /**
     * Ubacuje novu knjigu kod ucenika na sledece prazno mesto.
     *
     * @param naslov naslov knjige
     * @throws PreviseKnjiga ako ucenik kod sebe vec ima maksimum knjiga
     * @throws rs.luka.biblioteka.exceptions.Duplikat ako je Ucenik vec iznajmio
     * knjigu tog naslova
     */
    public void setKnjiga(String naslov) throws PreviseKnjiga, Duplikat {
        for (UcenikKnjiga knjiga : knjige) {
            if (knjiga.getNaslov().equals(naslov)) {
                throw new Duplikat("UcenikKnjiga");
            }
        }
        for (UcenikKnjiga knjige1 : knjige) {
            if (knjige1.isEmpty()) {
                knjige1.setNaslov(naslov);
                return;
            }
        }
        throw new PreviseKnjiga(ime);
    }

    /**
     * Brise knjigu na datom mestu, nakon cega pomera ostale knjige za jedno
     * mesto unazad, tako da ne ostane nijedno prazno mesto. Prazna mesta
     * izazivaju greske sa grafikom za vracanje (poznati bug). 
     * Note (9.'14.): grafika je promenjena, vise bug ne postoji. Metoda je ostala ovakva radi
     * preglednosti grafike.
     *
     * @param mesto mesto na kojem se knjiga nalazi
     */
    public void clearKnjiga(int mesto) {
        knjige[mesto].clear();
        for (int j = mesto + 1; j < knjige.length; j++) {
            knjige[j - 1].setNaslov(knjige[j].getNaslov());
            knjige[j - 1].setDatum(knjige[j].getDatum());
            knjige[j].clear();
        }
    }

    /**
     * Pronalazi knjigu datog naslova i zove {@link #clearKnjiga(int)} metodu.
     *
     * @param naslov naslov knjige
     * @throws VrednostNePostoji ako se kod ucenika ne nalazi nijedna knjiga tog
     * naslova.
     */
    public void clearKnjiga(String naslov) throws VrednostNePostoji {
        for (int i = 0; i < knjige.length; i++) {
            if (knjige[i].getNaslov().equals(naslov)) {
                clearKnjiga(i);
                return;
            }
            if (i == knjige.length) //poslednja iteracija
            {
                throw new VrednostNePostoji(vrednost.drugo); /*ako se nalazi van for
                 petlje, throw se izvrsava svaki put, umesto samo ako knjiga nije pronadjena*/
            }
        }
    }

    /**
     * Povecava razred ucenika za 1.
     */
    protected void povecajRazred() {
        razred++;
    }

    //OVERRIDES
    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        string.append("Ime: ").append(ime).append("\nRazred: ").append(razred).append("\nKnjige:");
        for (UcenikKnjiga knjiga : knjige) {
            string.append(knjiga.toString()).append('\n');
        }
        return string.toString();
    }

    /**
     * compareTo metoda koja sortira ucenike prvo po razredu, zatim po imenu.
     *
     * @param uc {@link Ucenik} sa kojim se uporedjuje
     */
    @Override
    public int compareTo(Ucenik uc) {
        if (Utils.getArrayIndex(validRazred, razred) < Utils.getArrayIndex(validRazred, uc.getRazred())) {
            return -1;
        }
        if (Utils.getArrayIndex(validRazred, razred) > Utils.getArrayIndex(validRazred, uc.getRazred())) {
            return 1;
        }
        return this.ime.compareToIgnoreCase(uc.getIme());
    }

    /**
     * Vraca true ako dati Object pripada klasi Ucenik, ima isto ime, razred i
     * knjige.
     *
     * @param uc Ucenik sa kojim se uporedjuje
     * @return true ako se ucenici poklapaju, false u suprotnom
     * @since 28.9.'14.
     */
    @Override
    public boolean equals(Object uc) {
        if (!(uc instanceof Ucenik)) {
            return false;
        }
        Ucenik ucenik = (Ucenik) uc;
        return ucenik.getIme().equals(ime) && ucenik.getRazred() == razred
                && Arrays.equals(ucenik.getKnjige(), knjige);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.ime);
        hash = 97 * hash + this.razred;
        hash = 97 * hash + Arrays.deepHashCode(this.knjige);
        return hash;
    }
}
