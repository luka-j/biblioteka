//919 linija, 24.8.'14
//1145 linija, 24.9.'14.
//1855 linija, 25.10.'14.
//2110 linija, 29.11.'14.
//2400 linija, 24.12.'14.
//2691 linija, 23.1.'15. (auto, Strings, viseKnjiga, (config) cleanup)
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
import java.util.Collections;
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
import static rs.luka.biblioteka.grafika.Konstante.*;

/**
 * Klasa sa podacima. Sadrzi sve liste, default vrednosti i flagove i metode za
 * postavljanje istih. Zvana "Unos", "Data", "Main" i "Init" ranije. Sve metode
 * su static.
 *
 * @author Luka
 * @since pocetak (23.8.'14)
 */
public final class Podaci {

    private static final java.util.logging.Logger LOGGER = 
            java.util.logging.Logger.getLogger(Podaci.class.getName());

    /**
     * Lista sa Ucenicima.
     */
    private static final ArrayList<Ucenik> ucenici = new UniqueList<>();
    /**
     * Lista sa Knjigama.
     */
    private static final ArrayList<Knjiga> knjige = new UniqueList<>();

    /**
     * default broj ucenika, ako ne postoji u config-u
     */
    private static final int DEF_UC_SIZE = 550;
    /**
     * default broj knjiga, ako ne postoji u config-u
     */
    private static final int DEF_KNJ_SIZE = 300;
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
            new rs.luka.biblioteka.grafika.Unos().unesiKnjige(true);
            return;
        } 
        if (knjigeF.length() == 0) {
                LOGGER.log(Level.FINE, "Lista sa knjigama je prazna. Zovem prozor za unos.");
                new rs.luka.biblioteka.grafika.Unos().unesiKnjige(false);
                return;
        }
        if (uceniciF.length() == 0) {
                LOGGER.log(Level.FINE, "Lista sa učenicima je prazna. Zovem prozor za unos.");
                new rs.luka.biblioteka.grafika.Unos().unesiUcenike();
        }
        try {
                knjigeF.createNewFile();
                uceniciF.createNewFile();
        } catch (IOException ex) {
                LOGGER.log(Level.SEVERE, "I/O greška pri učitavanju podataka", ex);
                showMessageDialog(null, LOADDATA_IOEX_MSG_STRING, LOADDATA_EX_TITLE_STRING, 
                        JOptionPane.ERROR_MESSAGE);
        }
        try (final Scanner inN = new Scanner(new BufferedReader(new FileReader(knjigeF)));
                    final Scanner inU = new Scanner(new BufferedReader(new FileReader(uceniciF)))) {
                ucenici.clear();
                knjige.clear();
                ucenici.ensureCapacity(parseUnsignedInt(Config.get("ucSize", valueOf(DEF_UC_SIZE))));
                knjige.ensureCapacity(parseUnsignedInt(Config.get("knjSize", valueOf(DEF_KNJ_SIZE))));
                try {
                    while (inN.hasNextLine()) {
                        if(!knjige.add(new Knjiga(inN.nextLine()).checkUniqueness()))
                            throw new NotUnique("Greška pri učitavanju knjiga: duplikat ");
                    }
                    while (inU.hasNextLine()) {
                        if(!ucenici.add(new Ucenik(inU.nextLine()).checkUniqueness())) 
                            throw new NotUnique("Greška pri učitavanju učenika: postoji duplikat");
                    }
                    knjige.trimToSize();
                    ucenici.trimToSize();
                } catch (NoSuchElementException ex) {
                    LOGGER.log(Level.SEVERE, "Greška pri učitavanju: premalo linija", ex);
                    showMessageDialog(null, LOADDATA_NSEEX_MSG_STRING, LOADDATA_EX_TITLE_STRING, 
                            JOptionPane.ERROR_MESSAGE);
                } catch (ParseException ex) {
                    LOGGER.log(Level.SEVERE, "Greška pri učitavanju: loš format", ex);
                    showMessageDialog(null, LOADDATA_PEX_MSG_STRING, LOADDATA_EX_TITLE_STRING, 
                            JOptionPane.ERROR_MESSAGE);
                } catch (UnsupportedOperationException | ClassCastException |   //unchecked,
                        NullPointerException | IllegalArgumentException RTex) { //runtime exceptions
                    LOGGER.log(Level.SEVERE, "Greška pri učitavanju", RTex);
                    showMessageDialog(null, LOADDATA_RTEX_MSG_STRING, LOADDATA_EX_TITLE_STRING, 
                            JOptionPane.ERROR_MESSAGE);
                }
        } catch (IOException ex) {
                LOGGER.log(Level.SEVERE, "I/O greška pri učitavanju podataka", ex);
                showMessageDialog(null, LOADDATA_IOEX_MSG_STRING, LOADDATA_EX_TITLE_STRING, 
                        JOptionPane.ERROR_MESSAGE);
        }
        System.gc();
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
            showMessageDialog(null, "Doslo je do greške pri kreiranju backupa.",
                    "I/O greška", JOptionPane.ERROR_MESSAGE);
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
    
    static Knjiga getOriginal(int i) {
        return knjige.get(i);
    }

    /**
     * Getter. Vraca knjigu trazenog naslova. Vraca originale.
     *
     * @param naslov naslov knjige koja se trazi
     * @return Knjiga koja se trazi
     * @throws VrednostNePostoji ako knjiga tog naslova ne postoji
     */
    public static Knjiga getKnjiga(String naslov) throws VrednostNePostoji {
        LOGGER.log(Level.FINER, "Getter: zatražena knjiga sa naslovom {0}", naslov);
        List<Knjiga> knjige = getAllKnjige(naslov);
        if(knjige.isEmpty())
            throw new VrednostNePostoji(vrednost.Knjiga);
        else if(knjige.size()==1)
            return knjige.get(0);
        else
            return knjige.get(Dijalozi.viseKnjiga(knjige));
    }
    
    /**
     * Vraca sve knjige sa datim naslovom
     * @param naslov naslov knjige
     * @return Listu Knjiga sa svim knjigama koje imaju taj naslov
     * @since 6.1.'15.
     */
    public static List<Knjiga> getAllKnjige(String naslov) {
        List<Knjiga> match = new ArrayList<>();
        knjige.stream().filter((knjiga) -> (knjiga.getNaslov().equals(naslov))).forEach((knjiga) -> {
            match.add(knjiga);
        });
        return match;
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
     * @param pisac pisac knjige
     * @return index trazene knjige
     * @throws VrednostNePostoji ako naslov ne postoji
     * @since pocetak
     * @deprecated no occurences
     */
    public static int indexOfNaslov(String naslov, String pisac) throws VrednostNePostoji {
        Knjiga knj;
        try {knj = new Knjiga(naslov, 0, pisac);
        } catch (Prazno | LosFormat ex) {
            throw new VrednostNePostoji(vrednost.Knjiga, ex);
        }
        int index = indexOfKnjiga(knj);
        if(index<0) 
            throw new VrednostNePostoji(VrednostNePostoji.vrednost.Knjiga);
        else return index;
    }
    
    public static int indexOfKnjiga(Knjiga knj) {
        if(knj==null)
            return -1;
        sortKnjige();
        return Collections.binarySearch(knjige, knj, null);
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
        if(ucenik == null)
            return -1;
        sortUcenike();
        return Collections.binarySearch(ucenici, ucenik, null);
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
     * Postavlja flag za uniqueIme u uceniku na i-tom mestu na false
     * @param i index ucenika
     */
    public static void setUcenikNotUnique(int i) {
        ucenici.get(i).setNotUnique();
    }
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
    public static void dodajKnjigu(String nas, int kol, String pisac) 
            throws Duplikat, VrednostNePostoji, LosFormat {
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
        if(knjige.add(knj)) {
        LOGGER.log(Level.INFO, "Naslov dodat: {0}", knj);
        Undo.push(Akcija.DODAVANJE_KNJIGE, new Object[]{knj});
        String knjSize = Config.get("knjSize", "0");
        int knjSizeInt = parseInt(knjSize);
        knjSizeInt++;
        try {Config.set("knjSize", valueOf(knjSizeInt));}
        catch(Exception e) { throw new RuntimeException(e);} //workaround za uncompilable source code
        }
        else 
            throw new Duplikat(vrednost.Knjiga);
    }
    
    /**
     * Dodaje novog ucenika sa datim imenom i prvim razredom (uzima iz klase Ucenik)
     * @param ime ime ucenika 
     * @throws Duplikat ako {@link #dodajUcenika(java.lang.String, int)} throwuje Duplikat.
     */
    public static void dodajUcenika(String ime) throws Duplikat, LosFormat {
        dodajUcenika(ime, Ucenik.getPrviRazred());
    }
    
    /**
     * Dodaje ucenika sa datim razredom i praznim knjigama
     * @param ime ime ucenika
     * @param raz razred koji trenutno pohadja
     * @throws Duplikat ako {@link #dodajUcenika(java.lang.String, int, java.lang.String[])} throwuje Duplikat
     * @see #dodajUcenika(java.lang.String, int, java.lang.String[]) 
     */
    public static void dodajUcenika(String ime, int raz) throws Duplikat, LosFormat {
        String naslovi[] = new String[Podaci.getMaxBrojUcenikKnjiga()];
        for (int i = 0; i < naslovi.length; i++) {
            naslovi[i] = "";
        }
        try{dodajUcenika(ime, raz, naslovi);}
        catch(VrednostNePostoji ex){throw new RuntimeException(ex);} //nikad
    }

    /**
     * addUc, refactored. Konstruktuje objekat Ucenik i zove metodu 
     * {@link #dodajUcenika(rs.luka.biblioteka.data.Ucenik)} sa tim argumentom
     * @param ime ucenik
     * @param razred razred
     * @param naslovi niz UcenikKnjiga sa naslovima koje ucenik ima kod sebe i
     * datumom kada su iznajmljene
     * @throws rs.luka.biblioteka.exceptions.Duplikat ako {@link #dodajUcenika(rs.luka.biblioteka.data.Ucenik)}
     * throwuje duplikat, tj. ako dati objekat vec postoji
     * @throws rs.luka.biblioteka.exceptions.VrednostNePostoji ako knjiga ne postoji
     * @since kraj jula '13.
     */
    public static void dodajUcenika(String ime, int razred, String[] naslovi) throws Duplikat, VrednostNePostoji, LosFormat {
        Knjiga[] knjige = new Knjiga[naslovi.length];
        for(int i=0; i<naslovi.length; i++) {
            if(naslovi[i] == null || naslovi[i].isEmpty())
                knjige[i] = null;
            else
                knjige[i] = getKnjiga(naslovi[i]);
        }
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
        if(ucenici.add(ucenik)) {
            LOGGER.log(Level.INFO, "Učenik dodat: ", new Object[]{ucenik.toString()});
            Undo.push(Akcija.DODAVANJE_UCENIKA, new Object[]{ucenik});
            int ucSize = Config.getAsInt("ucSize", "0");
            ucSize++;
            Config.set("ucSize", valueOf(ucSize));
        }
        else
            throw new Duplikat(vrednost.Ucenik);
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
        uceniciNoveGen.stream().forEach((String ucenik) -> {
            try {
                Podaci.dodajUcenika(ucenik);
            } catch (Duplikat ex) {
                LOGGER.log(Level.INFO, "Učenik {0} već postoji; neće biti dodat", ucenik);
                JOptionPane.showMessageDialog(null, DODAJGENERACIJU_DEX_MSG_STRING, 
                        DODAJGENERACIJU_DEX_TITLE_STRING, JOptionPane.WARNING_MESSAGE);
                //ne zelim da prekidam unos, umesto toga izbacujem gresku i nastavljam
            } catch (LosFormat ex) {
                LOGGER.log(Level.WARNING, "Učenik {0} sadrži nedozvoljen karakter (\"/\")", ucenik);
                JOptionPane.showMessageDialog(null, ucenik + DODAJGENERACIJU_LFEX_MSG_STRING, 
                        DODAJGENERACIJU_LFEX_TITLE_STRING, JOptionPane.WARNING_MESSAGE);
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
     * @throws rs.luka.biblioteka.exceptions.PreviseKnjiga ako {@link #obrisiKnjigu(Knjiga)} 
     * throwuje PreviseKnjiga, tj. ako se kod nekog Ucenika nalazi Knjiga
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
            if(it.next().hasKnjiga(knj)) { 
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
     * @param ucIndex index ucenika
     * @param knjIndex index knjige
     * @throws VrednostNePostoji ako se kod ucenika ne nalazi data knjiga
     * @since 8.'14.
     */
    public static void vratiKnjigu(int ucIndex, int knjIndex) throws VrednostNePostoji {
        Ucenik uc = ucenici.get(ucIndex);
        Knjiga knj = knjige.get(knjIndex);
        int kazna = uc.getIznosKazne(knj);
        if(kazna > 0) {
            int platiti = JOptionPane.showOptionDialog(null, VRACANJE_KAZNA_MSG1_STRING + kazna + VRACANJE_KAZNA_MSG2_STRING, 
                    VRACANJE_KAZNA_TITLE_STRING, JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, 
                    new String[]{DA_STRING, NE_STRING}, null);
            if(platiti!=0)
                return;
        }
        uc.clearKnjiga(knj);
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
     * @see Podaci#vratiKnjigu(int, int)
     * @throws RuntimeException ako dodje do VrednostNePostoji (osigurati da se to ne desi)
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
            uc.setKnjiga(knjige.get(knjIndex));
            LOGGER.log(Level.INFO, "Učenik {0} je iznajmio knjigu {1}", 
                    new Object[]{ucenici.get(ucIndex).getIme(), knjiga.getNaslov()});
            Undo.push(Akcija.UZIMANJE, new Object[]{uc, knj});
    }
    
    /**
     * Postavlja količinu knjige na datom indexu. Zapravo briše tu knjigu iz liste i
     * pravi novu, sa datom količinom, pošto su metode u klasi Knjiga ograničene na +/-1.
     * @param knjIndex index knjige čija se količina menja
     * @param kol nova količina
     * @since 2.1.'15.
     * @throws rs.luka.biblioteka.exceptions.NemaViseKnjiga ako je kol negativan
     */
    public static void setKolicina(int knjIndex, int kol) throws NemaViseKnjiga {
        if(kol<0)
            throw new NemaViseKnjiga(knjige.get(knjIndex));
        Knjiga knj = knjige.get(knjIndex);
        try {
            knjige.remove(knjIndex);
            knjige.add(new Knjiga(knj.getNaslov(), kol, knj.getPisac()));
            LOGGER.log(Level.INFO, "Promenio količinu knjige {0} sa {1} na {2}", 
                    new Object[]{knj.getNaslov(), knj.getKolicina(), kol});
        } catch (Prazno ex) { //pravi kopiju - isti podaci - ne bi trebalo da bude grešaka
            throw new RuntimeException(ex);
        } catch(Exception ex) { //ako nesto krene naopako, da ne izgubim podatke
            knjige.add(knj); //vracam kako je bilo
            throw new RuntimeException(ex); //signalizujem da nesto nije u redu
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
