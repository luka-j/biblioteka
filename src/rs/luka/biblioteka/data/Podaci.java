//919 linija, 24.8.'14
//1145 linija, 24.9.'14.
//1855 linija, 25.10.'14.
//2110 linija, 29.11.'14.
//2400 linija, 24.12.'14.
//2418 linija, 27.12.'14. (trenutno, auto)
package rs.luka.biblioteka.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import static java.lang.Integer.parseInt;
import static java.lang.Integer.parseUnsignedInt;
import static java.lang.String.valueOf;
import static java.nio.file.Files.copy;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.util.ArrayList;
import static java.util.Arrays.asList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.logging.Level;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.showMessageDialog;
import rs.luka.biblioteka.debugging.Test;
import rs.luka.biblioteka.exceptions.*;
import rs.luka.biblioteka.exceptions.VrednostNePostoji.vrednost;
import rs.luka.biblioteka.funkcije.Undo;
import rs.luka.biblioteka.funkcije.Utils;
import rs.luka.biblioteka.grafika.Dijalozi;

/**
 * Klasa sa podacima. Sadrzi sve liste, default vrednosti i flagove i metode za
 * postavljanje istih. Zvana "Unos", "Data", "Main" i "Init" ranije. Sve metode
 * su static.
 *
 * @author Luka
 * @since pocetak (23.8.'14)
 */
public class Podaci {

    private static final java.util.logging.Logger LOGGER = 
            java.util.logging.Logger.getLogger(Podaci.class.getName());

    /**
     * Lista sa Ucenicima.
     */
    private static final ArrayList<Ucenik> ucenici = new ArrayList<>();
    /**
     * Lista sa Knjigama.
     */
    private static final ArrayList<Knjiga> knjige = new ArrayList<>();

    /**
     * default broj ucenika, ako ne postoji u config-u
     */
    protected static final int defUcSize = 550;
    /**
     * default broj knjiga, ako ne postoji u config-u
     */
    protected static final int defKnjSize = 200;
    /**
     * Oznacava da li se testira. Ako da, unosi test podatke
     */
    private static final boolean TEST=false;

    /**
     * Zove metodu za backup i potom ucitava sve podatke. 
     * Ako vec neki podaci postoje, overwrituje ih.
     *
     * @see #backup()
     */
    public static void loadData() {
        if(TEST) {
            new Test().testUnos();
            if(true) {
                return;
            }
        }
        backup();
        
        LOGGER.log(Level.FINER, "Počeo sa učitavanjem podataka");
        File knjigeF = new File(Utils.getWorkingDir() + "Data" + File.separator + "Knjige.dat");
        File uceniciF = new File(Utils.getWorkingDir() + "Data" + File.separator + "Ucenici.dat");
        File data = new File(Utils.getWorkingDir() + "Data");
        if (!data.isDirectory()) {
            data.mkdir();
        }
        if (Config.get("firstRun").equals("true") || 
                (knjigeF.length() == 0 && uceniciF.length() == 0)) {
            LOGGER.log(Level.FINE, "Prvo pokretanje ili su liste prazne. "
                    + "Zovem prozor za unos i obustavljam učitavanje podataka.");
            new rs.luka.biblioteka.grafika.Unos().UnosGrafika();
        } else {
            if (knjigeF.length() == 0) {
                LOGGER.log(Level.FINE, "Lista sa knjigama je prazna. Zovem prozor za unos.");
                new rs.luka.biblioteka.grafika.Unos().UnosKnjige();
            }
            if (uceniciF.length() == 0) {
                LOGGER.log(Level.FINE, "Lista sa učenicima je prazna. Zovem prozor za unos.");
                new rs.luka.biblioteka.grafika.Unos().UnosUcenici();
            }
            try (final Scanner inN = new Scanner(new BufferedReader(new FileReader(knjigeF)));
                    final Scanner inU = new Scanner(new BufferedReader(new FileReader(uceniciF)))) {
                knjigeF.createNewFile();
                uceniciF.createNewFile();
                ucenici.clear();
                knjige.clear();
                ucenici.ensureCapacity(parseUnsignedInt(Config.get("ucSize", valueOf(defUcSize))));
                knjige.ensureCapacity(parseUnsignedInt(Config.get("knjSize", valueOf(defKnjSize))));
                try {
                    while (inN.hasNextLine()) {
                        knjige.add(new Knjiga(inN.nextLine()));
                    }
                    while (inU.hasNextLine()) {
                        ucenici.add(new Ucenik(inU.nextLine()));
                    }
                } catch (NoSuchElementException ex) {
                    LOGGER.log(Level.SEVERE, "Greška pri učitavanju: premalo linija", ex);
                    showMessageDialog(null, "Greška pri učitavanju:\n"
                            + "Premalo linija.", "Greška pri učitavanju.", 0);
                } catch (ParseException ex) {
                    LOGGER.log(Level.SEVERE, "Greška pri učitavanju: loš format", ex);
                    showMessageDialog(null, "Greška pri parsiranju datuma ili loš format fajla sa podacima.",
                            "Greška pri učitavanju", JOptionPane.ERROR_MESSAGE);
                } catch (UnsupportedOperationException | ClassCastException | 
                        NullPointerException | IllegalArgumentException RTex) {
                    LOGGER.log(Level.SEVERE, "Greška pri učitavanju", RTex);
                    showMessageDialog(null, "Greška pri učitavanju podataka: loš format",
                            "Greška pri učitavanju", JOptionPane.ERROR_MESSAGE);
                }
            } catch (IOException ex) {
                LOGGER.log(Level.SEVERE, "I/O greška pri učitavanju podataka", ex);
                showMessageDialog(null, "Greška pri učitavanju podataka", "I/O greška", 0);
            }
        }
        LOGGER.log(Level.FINE, "Završio učitavanje podataka");
    }

    /**
     * Radi backup fajlova tako sto stavlja sadasnje vrednosti u novi fajl.
     * Metoda overwrituje stari backup.
     */
    private static void backup() {
        LOGGER.log(Level.FINER, "Počeo backup podataka pri učitavanju");
        File knjigeF = new File(Utils.getWorkingDir() + "Data" + File.separator + "Knjige.dat");
        File uceniciF = new File(Utils.getWorkingDir() + "Data" + File.separator + "Ucenici.dat");
        File data = new File(Utils.getWorkingDir() + "Data");
        File knjigeBackup = new File(Utils.getWorkingDir() + "Data" + File.separator + "Knjige.dat~");
        File uceniciBackup = new File(Utils.getWorkingDir() + "Data" + File.separator + "Ucenici.dat~");
        try {
            if(data.isDirectory())
                data.mkdir();
            knjigeBackup.createNewFile();
            uceniciBackup.createNewFile();
            if (!knjigeF.exists() || !uceniciF.exists()) {
                LOGGER.log(Level.FINE, "Neki od fajlova ne postoji, obustavljam backup");
                return;
            }
            if(knjigeF.length()==0) {
                copy(knjigeBackup.toPath(), knjigeF.toPath(), StandardCopyOption.REPLACE_EXISTING);
                LOGGER.log(Level.WARNING, "Fajl sa podacima o knjigama je bio prazan, "
                        + "zamenio sam ga sa backupom");
            }
            else {
                copy(knjigeF.toPath(), knjigeBackup.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
            if(uceniciF.length()==0) {
                copy(uceniciBackup.toPath(), uceniciF.toPath(), StandardCopyOption.REPLACE_EXISTING);
                LOGGER.log(Level.WARNING, "Fajl sa podacima o ucenicima je bio prazan, "
                        + "zamenio sam ga sa backupom");
            }
            else {
                copy(uceniciF.toPath(), uceniciBackup.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "I/O greška pri kreiranju backup-a", ex);
            showMessageDialog(null, "Doslo je do greske pri kreiranju backupa.",
                    "I/O greska", JOptionPane.ERROR_MESSAGE);
        }
        LOGGER.log(Level.FINE, "Završio backup podataka pri učitavanju");
    }

    //GETTERI
    /**
     * GetUc, refactored. Vraca kopiju Ucenika koji se nalazi na indexu i.
     * @param i index
     * @return Ucenik
     * @since pocetak (23.8.'14.)
     */
    public static Ucenik getUcenik(int i) {
        LOGGER.log(Level.FINER, "Getter: zatražen učenik sa indexa {0}: {1}", new Object[]{i, ucenici.get(i)});
        return new Ucenik(ucenici.get(i));
    }

    /**
     * Getter. Vraca kopiju knjige koja se nalazi na indexu i.
     * @param i index
     * @return Knjiga na indexu i
     * @since 23.8.'14.
     */
    public static Knjiga getKnjiga(int i) {
        LOGGER.log(Level.FINER, "Getter: zatražena knjiga sa indexa {0}: {1}", new Object[]{i, knjige.get(i)});
        return new Knjiga(knjige.get(i));
    }

    /**
     * Getter. Vraca knjigu trazenog naslova.
     *
     * @param naslov naslov knjige koja se trazi
     * @return Knjiga koja se trazi
     * @throws VrednostNePostoji ako knjiga tog naslova ne postoji
     */
    public static Knjiga getKnjiga(String naslov) throws VrednostNePostoji {
        LOGGER.log(Level.FINER, "Getter: zatražena knjiga sa naslovom {0}", naslov);
        return getKnjiga(indexOfNaslov(naslov));
    }

    /**
     * GetBn, refactored. Vraca broj knjiga koje se nalaze u listi sa knjigama.
     *
     * @return n.size();
     * @since sam pocetak
     */
    public static int getBrojKnjiga() {
        LOGGER.log(Level.FINER, "Getter: zatražen broj knjiga: {0}", knjige.size());
        return knjige.size();
    }

    /**
     * getBrojUc, refactored. Vraca broj ucenika koji se nalaze u listi sa
     * ucenicima.
     *
     * @return broj ucenika
     * @since sam pocetak
     */
    public static int getBrojUcenika() {
        LOGGER.log(Level.FINER, "Getter: zatražen broj učenika: {0}", ucenici.size());
        return ucenici.size();
    }

    /**
     * Vraca index knjige, ignorisuci veliko i malo slovo
     *
     * @param naslov naslov knjige
     * @return index trazene knjige
     * @throws VrednostNePostoji ako naslov ne postoji
     * @since pocetak
     */
    public static int indexOfNaslov(String naslov) throws VrednostNePostoji {
        for (int i = 0; i < knjige.size(); i++) {
            if (knjige.get(i).getNaslov().equalsIgnoreCase(naslov)) {
                LOGGER.log(Level.FINER, "Knjiga {0} pronađena, index: {1}", new Object[]{naslov, i});
                return i;
            }
        }
        throw new VrednostNePostoji(VrednostNePostoji.vrednost.Knjiga);
    }

    /**
     * Index odredjenog ucenika.
     *
     * @param ucenik ime ucenika
     * @return listu ucenika sa tim imenom, u vecini slucajeva poredjanu po
     * razredima (ne uvek). Ako ne postoji nijedan ucenik sa tim imenom, vraca
     * se prazna lista.
     * @since pocetak
     */
    public static List<Integer> indexOfUcenik(String ucenik) {
        List<Integer> inx = new ArrayList<>();
        for (int i = 0; i < ucenici.size(); i++) {
            if (ucenici.get(i).getIme().equalsIgnoreCase(ucenik)) {
                LOGGER.log(Level.FINER, "Učenik {0} pronađen, index: {1}", new Object[]{ucenik, i});
                inx.add(i);
            }
        }
        return inx;
    }
    
    /**
     * Zove {@link java.util.List#indexOf(java.lang.Object) List#indexOf} metodu
     * na argumentu i vraca njen rezultat (-1 ako ne postoji).
     * @param ucenik ucenik za uporedjivanje
     * @return index ucenika
     * @since 28.9.'14.
     */
    public static int indexOfUcenik(Ucenik ucenik) {
        return ucenici.indexOf(ucenik);
    }

    /**
     * Vraca ukupan (maksimalan) broj knjiga koje ucenik moze da ima kod sebe.
     * Mora da se poklapa sa config:brKnjiga
     *
     * @return brKnjiga property iz configa ako postoji, ako ne, broj mesta za
     * knjige kod prvog ucenika. Ako je lista sa ucenicima prazna, vraca 0.
     * @since 8.'14.
     */
    public static int getMaxBrojUcenikKnjiga() {
        if (Config.hasKey("brKnjiga")) {
            LOGGER.log(Level.FINER, "uzimam maxBrojUcenikKnjiga iz configa: {0}", 
                    Config.get("brKnjiga"));
            return Config.getAsInt("brKnjiga");
        }
        if (ucenici.isEmpty()) {
            LOGGER.log(Level.FINER, "Nema učenika, maxBrojUcenikKnjiga je 0");
            return 0;
        }
        LOGGER.log(Level.FINER, "uzimam maxBrojUcenikKnjiga iz liste: {0}", ucenici.get(0).getMaxBrojKnjiga());
        return ucenici.get(0).getMaxBrojKnjiga();
    }
    
    /**
     * Vraca indexe ucenika koji su poslednji u listi iz svog razreda (odgovarajuca vrednost za pregledUcenika). 
     * Radi samo ako su ucenici sortirani po razredu!
     * @return granicne indexe ucenika, u nizu int-a.
     * @since 6.10.'14.
     */
    public static int[] getGraniceRazreda() {
        int ukCount=0;
        int[] razredi = new int[Ucenik.validRazred.length];
        for(int i = 0; i<Ucenik.validRazred.length; i++) {
            while(ukCount < ucenici.size() && 
                    ucenici.get(ukCount).getRazred() == Ucenik.validRazred[i]) {
                ukCount++;
            }
            razredi[i] = ukCount-1;
        }
        return razredi;
    }
    
    /**
     * Vraca iterator za listu knjige. 
     * @return knjige.iterator()
     * @since 1.11.'14.
     */
    public static Iterator<Knjiga> iteratorKnjiga() {
        return knjige.iterator();
    }
    
    /**
     * Vraca iterator za listu ucenici.
     * @return ucenici.iterator();
     * @since 1.11.'14.
     */
    public static Iterator<Ucenik> iteratorUcenika() {
        return ucenici.iterator();
    }

    //boolean metode
    /**
     * Proverava da li knjiga sa datim naslovom vec postoji u listi.
     * @param naslov naslov koji se trazi
     * @return true ako postoji, false u suprotnom
     * @since 9.'14.
     */
    public static boolean naslovExists(String naslov) {
        for(Knjiga knjiga : knjige) {
            if(knjiga.getNaslov().equalsIgnoreCase(naslov)) {
                LOGGER.log(Level.FINER, "Naslov {0} postoji", naslov);
                return true;
            }
        }
        LOGGER.log(Level.FINER, "Naslov {0} ne postoji", naslov);
        return false;
    }

    //SETTERI, ADD-eri
    /**
     * addNas, refactored. Pravi objekat Knjiga sa datim podacima i zove
     * {@link #dodajKnjigu(rs.luka.biblioteka.data.Knjiga) } da ubaci objekat u listu.
     *
     * @param nas naslov
     * @param kol kolicina
     * @param pisac pisac knjige
     * @throws rs.luka.biblioteka.exceptions.Duplikat ako naslov vec postoji
     * @throws rs.luka.biblioteka.exceptions.VrednostNePostoji ako konstruktor throwuje VrednostNePosoji
     * tj. ako je naslov "" ili null
     * @since kraj jula '13.
     */
    public static void dodajKnjigu(String nas, int kol, String pisac) throws Duplikat, VrednostNePostoji {
        Knjiga knj = null;
        try {
            knj = new Knjiga(nas, kol, pisac); 
        } catch(Prazno ex) {
            throw new VrednostNePostoji(VrednostNePostoji.vrednost.Knjiga);
        }
        dodajKnjigu(knj);
    }
    
    /**
     * Dodaje dati objekat u listu sa knjigama. Loguje, pushuje objekat u undo stack i menja knjSize u configu.
     * Ako dodje do exceptiona pri podesavanju configa, throwuje RuntimeException (workaround).
     * @param knj knjiga koja se dodaje
     * @throws Duplikat ako knjiga istog naslova vec postoji
     * @since 28.9.'14.
     */
    public static void dodajKnjigu(Knjiga knj) throws Duplikat {
        if(naslovExists(knj.getNaslov())) {
            throw new Duplikat(vrednost.Knjiga);
        }
        knjige.add(knj);
        LOGGER.log(Level.INFO, "Naslov dodat: {0}", knj);
        Undo.push(Akcija.DODAVANJE_KNJIGE, new Object[]{knj});
        String knjSize = Config.get("knjSize", "0");
        int knjSizeInt = parseInt(knjSize);
        knjSizeInt++;
        try {Config.set("knjSize", valueOf(knjSizeInt));}
        catch(Exception e) { throw new RuntimeException(e);} //workaround za uncompilable source code
    }
    
    /**
     * Dodaje novog ucenika sa datim imenom i prvim razredom (uzima iz klase Ucenik)
     * @param ime ime ucenika 
     * @throws Duplikat ako {@link #dodajUcenika(java.lang.String, int)} throwuje Duplikat.
     */
    public static void dodajUcenika(String ime) throws Duplikat {
        Podaci.dodajUcenika(ime, Ucenik.getPrviRazred());
    }
    
    /**
     * Dodaje ucenika sa datim razredom i praznim knjigama
     * @param ime ime ucenika
     * @param raz razred koji trenutno pohadja
     * @throws Duplikat ako {@link #dodajUcenika(java.lang.String, int, java.lang.String[])} throwuje Duplikat
     * @see #dodajUcenika(java.lang.String, int, java.lang.String[]) 
     */
    public static void dodajUcenika(String ime, int raz) throws Duplikat {
        String knjige[] = new String[Podaci.getMaxBrojUcenikKnjiga()];
        for (int i = 0; i < knjige.length; i++) {
            knjige[i] = "";
        }
        Podaci.dodajUcenika(ime, raz, knjige);
    }

    /**
     * addUc, refactored. Konstruktuje objekat Ucenik i zove metodu 
     * {@link #dodajUcenika(rs.luka.biblioteka.data.Ucenik)} sa tim argumentom
     * @param ime ucenik
     * @param razred razred
     * @param knjige niz UcenikKnjiga sa naslovima koje ucenik ima kod sebe i
     * datumom kada su iznajmljene
     * @throws rs.luka.biblioteka.exceptions.Duplikat ako {@link #dodajUcenika(rs.luka.biblioteka.data.Ucenik)}
     * throwuje duplikat, tj. ako dati objekat vec postoji
     * @since kraj jula '13.
     */
    public static void dodajUcenika(String ime, int razred, String[] knjige) throws Duplikat {
        Ucenik uc = new Ucenik(ime, razred, knjige);
        dodajUcenika(uc);
    }
    
    /**
     * Dodaje dati objekat u listu sa ucenicima, loguje akciju i stavlja je u undo stack.
     * Menja ucSize u configu.
     * @param ucenik ucenik koji se dodaje
     * @throws rs.luka.biblioteka.exceptions.Duplikat ako taj ucenik vec postoji
     * @since 28.9.'14.
     */
    public static void dodajUcenika(Ucenik ucenik) throws Duplikat {
        if(ucenici.contains(ucenik)) {
            throw new Duplikat(ucenik.toString() + "već postoji");
        }
        ucenici.add(ucenik);
        
        LOGGER.log(Level.INFO, "Učenik dodat: ", new Object[]{ucenik.toString()});
        Undo.push(Akcija.DODAVANJE_UCENIKA, new Object[]{ucenik});
        String ucSize = Config.get("ucSize", "0");
        int ucSizeInt = parseInt(ucSize);
        ucSizeInt++;
        Config.set("ucSize", valueOf(ucSizeInt));
    }
    
    /**
     * Dodaje novu generaciju, povecava sve razrede i brise one koji nisu validni.
     * 
     * @param novaGen ucenici
     * @since 1.8.'13.
     * @since 25.8.'13.
     */
    public static void dodajNovuGen(String novaGen) {
        LOGGER.log(Level.INFO, "Iniciram dodavanje nove generacije...");
        List<String> uceniciNoveGen = asList(novaGen.split("\\s*,\\s*"));
        uceniciNoveGen.stream().forEach((ucenik) -> {
            try {
                Podaci.dodajUcenika(ucenik, Ucenik.getPrviRazred());
            } catch (Duplikat ex) {
                JOptionPane.showMessageDialog(null, "Uneli ste dva učenika sa istim imenom i prezimenom. "
                        + "Jedan od njih neće biti unet.", "Duplikat", JOptionPane.WARNING_MESSAGE);
            }
        });
        povecajRazred();
        LOGGER.log(Level.INFO, "Nova generacija uspešno dodata i stara je obrisana.");
    }

    /**
     * Povecava razred svih ucenika za 1. SAMO ZA UNOS NOVE GENERACIJE!
     *
     * @since 25.8.'13.
     */
    public static void povecajRazred() {
        LOGGER.log(Level.INFO, "Povećavam razred svih učenika...");
        ucenici.stream().forEach((ucenik) -> {
            ucenik.povecajRazred();
            LOGGER.log(Level.FINEST, "Učeniku {0} povećan razred", ucenik.getIme());
        });
        obrisiGeneraciju();
        LOGGER.log(Level.INFO, "Čišćenje pred novu školsku godinu završeno");
    }

    /**
     * Brise ucenike ciji razred nije validan.
     *
     * @since 26.8.'13.
     */
    private static void obrisiGeneraciju() {
        LOGGER.log(Level.INFO, "Brišem učenike sa nevažećim razredom...");
        ucenici.stream().filter((ucenik) -> Ucenik.isRazredValid(ucenik.getRazred())).forEach((ucenik) -> {
            LOGGER.log(Level.FINEST, "Brišem učenika {0}", ucenik.getIme());
            ucenik = null;
            ucenici.remove(ucenik);
        });
    }

    /**
     * delUc, refactored. Brise ucenika na datom indexu. Loguje akciju, pushuje u undo stack
     * i smanjuje ucSize u configu za 1.
     *
     * @param inx index ucenika
     * @throws rs.luka.biblioteka.exceptions.PreviseKnjiga ako se kod Ucenika nalaze knjige
     * @since 1.7.'13.
     */
    public static void obrisiUcenika(int inx) throws PreviseKnjiga {
        if (ucenici.get(inx).getBrojKnjiga() > 0) {
            throw new PreviseKnjiga(inx);
        }
        Ucenik ucenik = ucenici.get(inx);
        ucenici.remove(inx);
        LOGGER.log(Level.INFO, "Učenik obrisan: {0}", new Object[]{ucenik});
        Undo.push(Akcija.BRISANJE_UCENIKA, new Object[]{ucenik});
        String ucSize = Config.get("ucSize", "0");
        int ucSizeInt = parseInt(ucSize);
        ucSizeInt--;
        Config.set("ucSize", valueOf(ucSizeInt));
    }
    
    /**
     * Trazi index prvog objekta u listi koji je {@link Ucenik#equals(java.lang.Object)}
     * sa datim objektom i zove {@link #obrisiUcenika(int)} da ga obrise. Ako ne postoji, vraca false.
     * 
     * @param ucenik Ucenik za brisanje
     * @return true ako je ucenik pronadjen i obrisan, false u suprotnom
     * @throws rs.luka.biblioteka.exceptions.PreviseKnjiga ako {@link #obrisiUcenika(int)}
     * throwuje PreviseKnjiga, tj. ako se kod Ucenika i dalje nalaze knjige
     */
    public static boolean obrisiUcenika(Ucenik ucenik) throws PreviseKnjiga {
        int inx = ucenici.indexOf(ucenik);
        if(inx>-1) {
            obrisiUcenika(inx);
            return true;
        }
        else {
            return false;
        }
    }
    
    /**
     * Brise naslov iz liste.
     * @param inx index naslova
     * @throws rs.luka.biblioteka.exceptions.PreviseKnjiga ako {@link #obrisiKnjigu(rs.luka.biblioteka.data.Knjiga)}
     * throwuje PreviseKnjiga, tj ako se kod nekog Ucenika nalazi Knjiga
     * @since 17.9.'14.
     */
    public static void obrisiKnjigu(int inx) throws PreviseKnjiga {
        obrisiKnjigu(knjige.get(inx));
    }
    
    /**
     * Brise prvo pojavljivanje objekta koji je equals sa datom Knjigom.
     * @param knj Knjiga za brisanje
     * @return true ako knjiga postoji i obrisana je, false u suprotnom
     * @throws rs.luka.biblioteka.exceptions.PreviseKnjiga ako se kod nekog Ucenika nalazi data Knjiga
     * @since 3.10.'14.
     */
    public static boolean obrisiKnjigu(Knjiga knj) throws PreviseKnjiga {
        Iterator<Ucenik> it = Podaci.iteratorUcenika();
        while(it.hasNext()) {
            if(it.next().hasKnjiga(knj.getNaslov())) { 
                throw new PreviseKnjiga(knj.getNaslov());
            }
        }
        
        if(knjige.remove(knj)) {
            LOGGER.log(Level.INFO, "Naslov {0} obrisan.", knj);
            Undo.push(Akcija.BRISANJE_KNJIGE, new Object[]{knj});
            return true;
        }
        return false;
    }

    /**
     * Vraca knjigu
     *
     * @param ucIndex
     * @param knjIndex
     * @throws VrednostNePostoji 
     * @since 8.'14.
     */
    public static void vratiKnjigu(int ucIndex, int knjIndex) throws VrednostNePostoji {
        Ucenik uc = ucenici.get(ucIndex);
        Knjiga knj = knjige.get(knjIndex);
        uc.clearKnjiga(knjige.get(knjIndex).getNaslov());
        knj.povecajKolicinu();
        LOGGER.log(Level.INFO, "Knjiga {0} vraćena od učenika {1}", 
                new Object[]{knjige.get(knjIndex).getNaslov(), ucenici.get(ucIndex).getIme()});
        Undo.push(Akcija.VRACANJE, new Object[]{uc, knj});
    }
    
    /**
     * Za svaku knjigu u listi, trazi index knjige i zove
     * {@link Podaci#vratiKnjigu(int, int)}. Ako dodje do greske (VrednostNePostoji) throwuje RuntimeException.
     *
     * @param indexUcenika index ucenika
     * @param indexKnjiga lista sa indexima knjiga za vracanje
     * @see Podaci#indexOfNaslov(java.lang.String)
     * @see Podaci#vratiKnjigu(int, int)
     * @since 9.'14.
     */
    public static void vratiViseKnjigaSafe(int indexUcenika, List<Integer> indexKnjiga) {
        for (Integer indexKnjige : indexKnjiga) {
            try {
                Podaci.vratiKnjigu(indexUcenika, indexKnjige);
            } catch (VrednostNePostoji ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    /**
     * Uzima knjigu od biblioteke i daje je uceniku.
     *
     * @param ucIndex index ucenika koji uzima knjigu
     * @param knjiga knjiga koja se uzima
     * @throws rs.luka.biblioteka.exceptions.PreviseKnjiga ako se kod ucenika
     * nalazi previse knjiga
     * @throws rs.luka.biblioteka.exceptions.NemaViseKnjiga ako nema vise knjiga
     * tog naslova
     * @throws rs.luka.biblioteka.exceptions.VrednostNePostoji ako knjiga ne
     * postoji
     * @throws rs.luka.biblioteka.exceptions.Duplikat ako se kod Ucenika vec
     * nalazi knjiga tog naslova
     * @since 8.'14.
     */
    public static void uzmiKnjigu(int ucIndex, Knjiga knjiga) 
            throws PreviseKnjiga, NemaViseKnjiga, VrednostNePostoji, Duplikat {
        int knjIndex = knjige.indexOf(knjiga); //vraca original, zbog equals() metode u Knjiga.java
        if(knjIndex<0) {
            throw new VrednostNePostoji(vrednost.Knjiga);
        }
        Ucenik uc = ucenici.get(ucIndex);
        Knjiga knj = knjige.get(knjIndex);
        try {
            uc.setKnjiga(knjige.get(knjIndex).getNaslov());
            knj.smanjiKolicinu();
            LOGGER.log(Level.INFO, "Učenik {0} je iznajmio knjigu {1}", 
                    new Object[]{ucenici.get(ucIndex).getIme(), knjiga.getNaslov()});
            Undo.push(Akcija.UZIMANJE, new Object[]{uc, knj});
        }
        catch(NemaViseKnjiga ex) { //ako ne može da smanji količinu, moram da vratim kako je bilo
            uc.clearKnjiga(knjiga.getNaslov());
            throw ex;
        }
    }
    
    /**
     * Uzima knjigu od biblioteke i daje je uceniku.
     * 
     * @param knjIndex index knjige koja se iznajmljuje
     * @param ucenik ime ucenika koji uzima knjigu
     * @throws PreviseKnjiga ako se kod ucenika nalazi previse knjiga
     * @throws Duplikat ako se kod ucenika vec nalazi knjiga tog naslova
     * @throws NemaViseKnjiga ako vise nema knjige tog naslova u biblioteci
     * @throws VrednostNePostoji ako ucenik sa tim imenom ne postoji
     */
    public static void uzmiKnjigu(int knjIndex, String ucenik) 
            throws PreviseKnjiga, Duplikat, NemaViseKnjiga, VrednostNePostoji {
        List<Integer> indexes = indexOfUcenik(ucenik);
        int ucIndex;
        if(indexes.isEmpty()) {
            throw new VrednostNePostoji(vrednost.Ucenik);
        }
        if(indexes.size() > 1) {
            ucIndex = indexes.get(Dijalozi.viseRazreda(indexes));
        } else {
            ucIndex = indexes.get(0);
        }
        Ucenik uc = ucenici.get(ucIndex);
        Knjiga knj = knjige.get(knjIndex);
        try {
            uc.setKnjiga(knjige.get(knjIndex).getNaslov());
            knj.smanjiKolicinu();
            LOGGER.log(Level.INFO, "Učenik {0} je iznajmio knjigu {1}", 
                    new Object[]{ucenik, knjige.get(knjIndex).getNaslov()});
            Undo.push(Akcija.UZIMANJE, new Object[]{ucenici.get(ucIndex), knjige.get(knjIndex)});
        }
        catch(NemaViseKnjiga ex) { //ako ne može da smanji količinu, moram da vratim kako je bilo
            uc.clearKnjiga(knj.getNaslov());
            throw ex;
        }
    }
    
    /**
     * Sortira ucenike po default komparatoru (null).
     * @since 7-8.'14.
     */
    public static void sortUcenike() {
        ucenici.sort(null);
        LOGGER.log(Level.FINE, "Učenici sortirani");
    }

    /**
     * Sortira naslove po default komparatoru (null).
     * @since 7-8.'14.
     */
    public static void sortKnjige() {
        knjige.sort(null);
        LOGGER.log(Level.FINE, "Knjige sortirane");
    }

    /**
     * Ne moze da se konstruise objekat, jer ova klasa sadrzi samo static metode.
     */
    private Podaci() {
        throw new IllegalAccessError();
    }
}
