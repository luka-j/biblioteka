package rs.luka.biblioteka.data;

import rs.luka.biblioteka.funkcije.Datumi;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.swing.JOptionPane;
import rs.luka.biblioteka.exceptions.Duplikat;
import rs.luka.biblioteka.exceptions.LosFormat;
import rs.luka.biblioteka.exceptions.NemaViseKnjiga;
import rs.luka.biblioteka.exceptions.PreviseKnjiga;
import rs.luka.biblioteka.exceptions.VrednostNePostoji;
import rs.luka.biblioteka.exceptions.VrednostNePostoji.vrednost;
import rs.luka.biblioteka.funkcije.Utils;
import rs.luka.biblioteka.grafika.Konstante;

/**
 *
 * @author Luka
 * @since 3.7.'13.
 */
public class Ucenik implements Comparable<Ucenik> {

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
    private static final String splitString = String.valueOf(splitChar);
    /**
     * Oznacava da li se knjige pomeraju ulevo pri vracanju
     *
     * @since 7.1.'15.
     */
    private static boolean shiftKnjige = true;
    /**
     * Iznos dnevne kazne za predugo zadrzavanje knjige. 
     * @since 23.1.'15.
     */
    private static int iznosKazne;
    /**
     * Prethodni ucenik koji se proveravao putem {@link #checkUniqueness()}, ako su ucenici sortirani po imenu.
     */
    private static Ucenik prevUcenik;
    /**
     * Flag koji oznacava metodu sortiranja.
     */
    private static boolean isSortedByRazred;

    //STATIC:
    public static void initUcenik() {
        setValidRazred();
        setConfig();
    }

    /**
     * Postavlja validne razrede prema datom Stringu, koji je razdvojen
     * zapetama. Radi exception-handling.
     *
     * @since 6.10.'14.
     */
    private static void setValidRazred() {
        if (Config.hasKey("razredi")) {
            String razredi = Config.get("razredi");
            String[] split = razredi.split(",");
            try {
                validRazred = new int[split.length];
                for (int i = 0; i < split.length; i++) {
                    validRazred[i] = Integer.parseInt(split[i]);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, Konstante.UCENIK_SETRAZREDI_NFEX_MSG_STRING,
                        Konstante.UCENIK_SETRAZREDI_NFEX_TITLE_STRING, JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private static void setConfig() {
        shiftKnjige = Config.getAsBool("shiftKnjige");
        isSortedByRazred = sortedByRazred();
        iznosKazne = Config.getAsInt("kazna");
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
    
    /**
     * Vraca da li se ucenici sortiraju po razredu. Ako je false, ucenici se
     * sortiraju po imenu.
     *
     * @return true ili false, u zavisnosti od nacina sortiranja
     */
    public static boolean sortedByRazred() {
        return !Config.hasKey("ucSort") || Config.get("ucSort").equals("razred") || Config.get("ucSort").equals("raz");
    }
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
     * Oznacava da li postoji jos neki ucenik s istim imenom.
     */
    private boolean isImeUnique = true;

    //KONSTRUKTORI:
    public Ucenik(String ime, int razred, Knjiga[] knjige) throws LosFormat {
        this.ime = ime;
        /*if(razred < 1) {
         this.razred = validRazred[0];
         } else*/ if (Utils.arrayContains(validRazred, razred)) {
            this.razred = razred;
        } else {
            throw new NumberFormatException("Loš razred: " + razred);
        }
        if(ime.contains(splitString)) {
            throw new LosFormat("splitString u imenu ucenika");
        }
        this.knjige = new UcenikKnjiga[Podaci.getMaxBrojUcenikKnjiga()];
        for (int i = 0; i < knjige.length; i++) {
            if (knjige[i] == null) {
                this.knjige[i] = new UcenikKnjiga();
            } else {
                this.knjige[i] = new UcenikKnjiga(knjige[i], new java.util.Date()); //throwuje IndexOutOfBounds
            }
        }
        for (int i = knjige.length; i < this.knjige.length; i++) {
            this.knjige[i] = new UcenikKnjiga();
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
        knjige = new UcenikKnjiga[Config.getAsInt("brKnjiga", "3")];
        for (int i = 2; i < fields.length; i++) {
            knjige[i - 2] = new UcenikKnjiga(fields[i]);
        }
        for(int i=fields.length; i<knjige.length + 2; i++) {
            knjige[i-2] = new UcenikKnjiga();
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
            knjige[i] = new UcenikKnjiga(ucenik.getUcenikKnjiga(i));
        }
        isImeUnique = ucenik.isImeUnique;
    }

    //GETTERI
    public String getIme() {
        return ime;
    }

    public String getDisplayName() {
        if (isImeUnique) {
            return ime;
        } else {
            return ime + ", " + razred;
        }
    }

    public int getRazred() {
        return razred;
    }

    public String getNaslovKnjige(int i) {
        return getUcenikKnjiga(i).knjiga.getNaslov();
    }

    public String getDatumKnjige(int i) {
        return getUcenikKnjiga(i).getDatumAsString();
    }

    public long getTimeKnjige(int i) {
        return getUcenikKnjiga(i).getDatum().getTime();
    }

    public Knjiga getKnjiga(int i) {
        return getUcenikKnjiga(i).getKnjiga();
    }

    private UcenikKnjiga getUcenikKnjiga(int i) {
        if (knjige.length > i) {
            return knjige[i];
        } else {
            return new UcenikKnjiga();
        }
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
        for (UcenikKnjiga knjiga : knjige) {
            string.append(splitChar).append(knjiga.getAsIOString());
        }
        for (int i = knjige.length; i < Podaci.getMaxBrojUcenikKnjiga(); i++) {
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
    
    public int getIznosKazne(Knjiga knjiga) {
        if(knjiga == null) return 0;
        for(UcenikKnjiga knj : knjige)
            if(knjiga.equals(knj.getKnjiga()))
                return knj.kazna;
        return -1;
    }

    //boolean METODE
    public boolean isImeUnique() {
        return isImeUnique;
    }

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
    public boolean hasKnjiga(Knjiga knjiga) {
        for (UcenikKnjiga knjige0 : knjige) {
            if (knjiga.equals(knjige0.getKnjiga())) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Pretrazuje da li se knjiga koja pocinje sa datim stringom ili cije je ime pisca
     * dati string, nalazi kod ovog ucenika. 
     * @param str string za pretragu. Mora biti lowercase (mala slova)
     * @return true ako se nalazi, false u suprotnom
     * @throws NullPointerException ako sam opet nesto zaboravio -.-
     */
    public boolean searchKnjiga(String str) {
        for(UcenikKnjiga knjiga : knjige) {
            if(knjiga == null || knjiga.isEmpty())
                continue;
            if(knjiga.getKnjiga().getNaslov().toLowerCase().startsWith(str) ||
                    knjiga.getKnjiga().getPisac().toLowerCase().equals(str))
                return true;
        }
        return false;
    }

    /**
     * Proverava da li je dato mesto za Knjigu kod ucenika prazno
     *
     * @param i mesto
     * @return true ili false, u zavisnosti od toga da li je mesto za knjigu puno ili prazno
     */
    public boolean isKnjigaEmpty(int i) {
        if (i < knjige.length) {
            return knjige[i].isEmpty();
        } else {
            return false;
        }
    }

    //SETTERI
    /**
     * Postavlja {@link #isImeUnique} na false.
     */
    public void setNotUnique() {
        isImeUnique = false;
    }

    /**
     * Proverava da li Ucenik ima isto ime kao i prethodni koji se proveravao
     * ovom metodom i postavlja {@link #isImeUnique} flag. Znatno brze ako su ucenici
     * sortirani po imenu (proverava samo prethodni), sporije ako su sortirani po razredu
     * (proverava sve prethodno unesene).
     *
     * @return this, da dozvoli chaining
     * @since 8.1.'15.
     */
    public Ucenik checkUniqueness() {
        if (!isSortedByRazred) {
            if (prevUcenik != null && prevUcenik.ime.equalsIgnoreCase(ime)) {
                this.isImeUnique = false;
                prevUcenik.isImeUnique = false;
            }
            prevUcenik = this;
        } else {
            List<Integer> indexi = Podaci.indexOfUcenik(ime);
            if (indexi.size() == 1) {
                indexi.stream().forEach((i) -> {
                    Podaci.setUcenikNotUnique(i);
                });
                this.isImeUnique = false;
            } else if(indexi.size() > 1) {
                this.isImeUnique = false;
            }
        }
        return this;
    }

    /**
     * Ubacuje novu knjigu kod ucenika na sledece prazno mesto.
     *
     * @param knjiga knjiga koja se iznajmljuje
     * @throws PreviseKnjiga ako ucenik kod sebe vec ima maksimum knjiga. U
     * message se nalazi spisak naslova
     * @throws rs.luka.biblioteka.exceptions.Duplikat ako je Ucenik vec iznajmio
     * knjigu tog naslova
     * @throws rs.luka.biblioteka.exceptions.NemaViseKnjiga ako nema vise knjiga
     * datog naslova
     */
    public void setKnjiga(Knjiga knjiga) throws PreviseKnjiga, Duplikat, NemaViseKnjiga {
        for (UcenikKnjiga UKnjiga : knjige) {
            if (!UKnjiga.isEmpty() && UKnjiga.getKnjiga().equals(knjiga)) {
                throw new Duplikat("UcenikKnjiga");
            }
        }
        for (UcenikKnjiga knjige1 : knjige) {
            if (knjige1.isEmpty()) {
                knjige1.setKnjiga(knjiga);
                return;
            }
        }
        StringBuilder knj = new StringBuilder(72);
        for (UcenikKnjiga UKnjiga : knjige) {
            knj.append(UKnjiga.toString()).append('\n');
        }
        throw new PreviseKnjiga(knj.toString());
    }

    /**
     * Brise knjigu na datom mestu, nakon cega pomera ostale knjige za jedno
     * mesto unazad, tako da ne ostane nijedno prazno mesto. Prazna mesta
     * izazivaju greske sa grafikom za vracanje (poznati bug). Note (9.'14.):
     * grafika je promenjena, vise bug ne postoji. Metoda je ostala ovakva radi
     * preglednosti grafike.
     *
     * @param mesto mesto na kojem se knjiga nalazi
     */
    private void clearKnjiga(int mesto) {
        knjige[mesto].clear();
        if (shiftKnjige) {
            shiftLeft(mesto);
        }
    }

    /**
     * Pomera sve od datog mesta u levo. Ne koristi funkcije UcenikKnjiga, već
     * direktno radi na podacima (izbegava sve provere i menjanja količine,
     * koristiti s oprezom i redovno update-ovati)
     *
     * @param mesto prazno mesto
     */
    private void shiftLeft(int mesto) {
        if (!knjige[mesto].isEmpty()) {
            throw new RuntimeException("Pokusaj shiftLeft-a nad punim mestom");
        }
        for (int j = mesto + 1; j < knjige.length; j++) {
            knjige[j - 1].knjiga = knjige[j].knjiga;
            knjige[j - 1].datum = knjige[j].datum;
            knjige[j].knjiga = null;
            knjige[j].datum = null;
        }
    }

    /**
     * Pronalazi knjigu datog naslova i zove {@link #clearKnjiga(int)} metodu.
     *
     * @param knjiga knjiga koja se iznajmljuje
     * @throws VrednostNePostoji ako se kod ucenika ne nalazi nijedna knjiga tog
     * naslova.
     */
    public void clearKnjiga(Knjiga knjiga) throws VrednostNePostoji {
        for (int i = 0; i < knjige.length; i++) {
            if (knjige[i].getKnjiga() != null && knjige[i].getKnjiga().equals(knjiga)) {
                clearKnjiga(i);
                return;
            }
            if (i == knjige.length) { //poslednja iteracija 
                throw new VrednostNePostoji(vrednost.drugo); /*ako se nalazi van for
                 petlje, throw se izvrsava svaki put, umesto samo ako knjiga nije pronadjena*/
            }
        }
    }

    /**
     * Povecava razred ucenika na sledeci (po redosledu u validRazred).
     */
    protected void povecajRazred() {
        for (int i = 0; i < validRazred.length; i++) {
            if (razred == validRazred[i] && i + 1 < validRazred.length) {
                razred = validRazred[i + 1];
                break;
            } else {
                razred = -1;
            }
        }
    }
    
    public void setKazna(int brDana, int knjiga) {
        knjige[knjiga].setKazna(brDana * iznosKazne);
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
        if (isSortedByRazred) {
            if (Utils.getArrayIndex(validRazred, razred) < Utils.getArrayIndex(validRazred, uc.getRazred())) {
                return -1;
            }
            if (Utils.getArrayIndex(validRazred, razred) > Utils.getArrayIndex(validRazred, uc.getRazred())) {
                return 1;
            }
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
        if (this == uc) {
            return true;
        }
        if (!(uc instanceof Ucenik)) {
            return false;
        }
        Ucenik ucenik = (Ucenik) uc;
        return ucenik.getIme().equals(this.ime) && ucenik.getRazred() == this.razred;
        //izbacio Arrays.equals, proveriti da li je OK
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.ime);
        hash = 97 * hash + this.razred;
        return hash;
    }

    /**
     * Klasa za knjige koje se nalaze kod ucenika. Sadrzi naslov knjige i datum
     * kada je izdata.
     *
     * @author Luka
     * @since 7. 17. 2014.
     */
    private class UcenikKnjiga {

        /**
         * Naslov knjige koja je izdata uceniku.
         */
        private Knjiga knjiga;

        private final int indexKnjige; //potrebno ??
        /**
         * Datum kada je data knjiga izdata.
         */
        private Date datum;
        private int kazna;
        /**
         * Karakter koji se u fajlovima koristi za razdvajanje polja. Ne sme
         * biti isti kao splitChar klase Ucenik ili Knjiga
         *
         * @since 23.9.'14.
         */
        private static final char splitChar = '@';
        /**
         * String wrapper za {@link #splitChar}.
         *
         * @since 23.9.'14.
         */
        private static final String splitString = "@"; //workaround -.-

        UcenikKnjiga(Knjiga knjiga, Date datum) {
            this.knjiga = knjiga;
            this.indexKnjige = Podaci.indexOfKnjiga(knjiga);
            this.datum = datum;
        }

        /**
         * Konstruktuje objekat na osnovu datog stringa, koji bi trebalo da
         * dolazi iz fajla sa podacima.
         *
         * @param IOString String sa podacima
         * @throws ParseException ako dodje do greske pri parsiranju datuma
         * @since 23.9.'14.
         */
        UcenikKnjiga(String IOString) throws ParseException {
            String[] fields = IOString.split(splitString);
            if (fields.length == 0 || fields[0].equals("-1")) {
                knjiga = null;
                datum = null;
                indexKnjige = -1;
            } else {
                indexKnjige = Integer.parseInt((fields[0]));
                knjiga = Podaci.getOriginal(indexKnjige);
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
            knjiga = uk.getKnjiga();
            indexKnjige = uk.indexKnjige;
            if (uk.isEmpty()) {
                datum = null;
            } else {
                datum = new Date(uk.getDatum().getTime());
            }
        }

        /**
         * Konstruise praznu UcenikKnjiga (sa "" naslovom i null datumom)
         */
        UcenikKnjiga() {
            knjiga = null;
            indexKnjige = -1;
            datum = null;
        }

        //GETTERI
        private Knjiga getKnjiga() {
            return knjiga;
        }

        /**
         * Vraca datum objekta.
         */
        private Date getDatum() {
            return datum;
        }

        /**
         * @return Datum kao I/O String
         * @see Datumi#df
         */
        private String getDatumAsString() {
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
        private String getAsIOString() {
            if(this.isEmpty())
                return "";
            else
                return Podaci.indexOfKnjiga(knjiga) + splitString + getDatumAsString();
        }

        //SETTERI
        /**
         * Brise UcenikKnjiga, tj postavlja naslov i datum na null i povecava
         * kolicinu knjige.
         */
        private void clear() {
            knjiga.povecajKolicinu();
            knjiga = null;
            datum = null;
        }

        /**
         * Postavlja naslov na dati String, datum na trenutni.
         * @param knjiga knjiga koju ucenik iznajmljuje
         */
        private void setKnjiga(Knjiga knjiga) throws NemaViseKnjiga {
            knjiga.smanjiKolicinu();
            this.knjiga = knjiga;
            datum = new Date();
        }
        
        /**
         * Postavlja trenutnu kaznu za predugo zadrzavanje knjige na datu vrednost.
         * @param kazna 
         */
        private void setKazna(int kazna) {
            this.kazna = kazna;
        }

        //is... METODE
        /**
         * Vraca true ako je "" prazan, false u suprotnom
         *
         * @return
         */
        private boolean isEmpty() {
            return knjiga == null;
        }

        //OVERRIDES
        @Override
        public String toString() {
            DateFormat df = new SimpleDateFormat("dd. MM. yyyy");
            if (datum != null && knjiga != null) {
                return knjiga.getNaslov() + " (autora " + knjiga.getPisac() + "), iznajmljena " + df.format(datum);
            } else if (datum == null && knjiga == null) {
                return "Prazno";
            } else {
                throw new ClassFormatError("Ucenik knjiga nije u redu :s");
            }
        }

        /**
         * Vraca true ako je Object vrste UcenikKnjiga, trenutna i UcenikKnjiga
         * sa kojom se uporedjuje prazna ili ako sadrze isti naslov i datum.
         *
         * @param uk UcenikKnjiga sa kojom se uporedjuje
         * @return true ako se vrednosti poklapaju, false u suprotnom
         * @since 28.9.'14.
         */
        @Override
        public boolean equals(Object uk) {
            if (this == uk) {
                return true;
            }
            if (!(uk instanceof UcenikKnjiga)) {
                return false;
            }
            UcenikKnjiga UKnjiga = (UcenikKnjiga) uk;
            return UKnjiga.isEmpty() && this.isEmpty()
                    || (UKnjiga.getKnjiga().equals(knjiga));
        }

        @Override
        public int hashCode() {
            int hash = 5;
            hash = 53 * hash + Objects.hashCode(this.knjiga);
            return hash;
        }
    }
}
