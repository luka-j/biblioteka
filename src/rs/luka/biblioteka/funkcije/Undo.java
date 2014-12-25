package rs.luka.biblioteka.funkcije;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import rs.luka.biblioteka.data.*;
import rs.luka.biblioteka.exceptions.Duplikat;
import rs.luka.biblioteka.exceptions.NemaViseKnjiga;
import rs.luka.biblioteka.exceptions.PreviseKnjiga;
import rs.luka.biblioteka.exceptions.VrednostNePostoji;

/**
 *
 * @author luka
 * @since 27.9.'14.
 */
public class Undo {

    private static final java.util.logging.Logger LOGGER
            = java.util.logging.Logger.getLogger(Undo.class.getName());

    /**
     * Prethodne akcije u programu.
     */
    private static final List<UndoAkcija> stack = new ArrayList<>();
    /**
     * Maksimalna velicina stacka. Uzima se iz configa pri inicijalizaciji.
     */
    private static int stackDepth;
    /**
     * Oznacava element nad kojim se vrsi sledeca operacija.
     */
    private static int pointer;
    /**
     * Oznacava da li je program inicijalizovan. Ne moze se raditi undo pre
     * inicijalizacije programa (ne bi trebalo da predstavlja problem za sada).
     */
    private static boolean init = false;
    /**
     * Oznacava da li se trenutno vrsi undo operacija. Ako da, blokira unose u
     * listu.
     */
    private static boolean inProgress = false;

    /**
     * Ucitava velicinu undo stacka i postavlja init na true.
     */
    public static void initUndo() {
        pointer = 0;
        stackDepth = Integer.parseInt(Config.get("maxUndo"));
        init = true;
    }

    /**
     * Proverava da li je Undo inicijalizovan i validnost date akcije i stavlja
     * je u stack.
     *
     * @param akcija akcija
     * @param data podaci o akciji, koji se proveravaju za validnost tipa
     * podataka
     * @see #isValid(rs.luka.biblioteka.data.Akcija, java.lang.Object[])
     * @since 27.9.'14.
     */
    public static void push(Akcija akcija, Object[] data) {
        if (!init) {
            return;
        }
        if (inProgress) {
            LOGGER.log(Level.FINE, "Undo se trenutno izvršava i ne dozvoljava nove unose u stack.");
            return;
        }
        for(int i=pointer; i<stack.size(); i++) {
            //brise eventualne kandidate za redo
            stack.remove(i);
        }
        stack.add(new UndoAkcija(akcija, data));
        if (stack.size() > stackDepth) {
            stack.remove(0);
            LOGGER.log(Level.FINER, "Undo stack pun; brišem poslednju akciju.");
        }
        pointer = stack.size();
        LOGGER.log(Level.FINE, "Akcija {0} dodata u undo stack.", akcija);
    }
    
    /**
     * Radi undo akcije odredjene indexom pointer-1. Tokom procesa zabranjuje nove unose u undo stack 
     * putem flaga. Zove {@link UndoData#undo() undo funkciju objekta}. Nakon toga, smanjuje pointer.
     * @since 15.10.'14.
     */
    public static void undo() {
        if (pointer==0) {
            return;
        }
        inProgress = true;
        String akcija = stack.get(pointer-1).undo();
        pointer--;
        inProgress = false;
        LOGGER.log(Level.INFO, "Undo završen za prethodnu akciju {0}", akcija);
    }
    
    /**
     * Radi redo akcije odredjene indexom pointer. Tokom procesa zabranjuje nove unose u undo stack
     * putem flaga. Zove {@link UndoData#redo() redo funkciju objekta}. Nakon toga, povecava pointer.
     * @since 15.10.'14.
     */
    public static void redo() {
        if(stack.size()==pointer) {
            return;
        }
        inProgress=true;
        String akcija = stack.get(pointer).redo();
        pointer++;
        inProgress=false;
        LOGGER.log(Level.INFO, "Redo završen za akciju {0}", akcija);
    }

    /**
     * Ne moze se inicijalizovati.
     */
    private Undo() {
        throw new IllegalAccessError();
    }
    
    //===========UNDOAKCIJA=====================================================
    
    static class UndoAkcija {
    /**
     * Akcija koja je ubacena u stack.
     */
    private Akcija akcija;
    /**
     * Ucenik na koga se odnosi dodavanje, brisanje, uzimanje ili vracanje.
     */
    private Ucenik ucenik;
    /**
     * Knjiga na koju se odnosi dodavanje, brisanje, uzimanje ili vracanje.
     */
    private Knjiga knjiga;
    /**
     * -1.
     * int kod za undo operaciju. Mnozenjem dodaje predznak - na broj.
     */
    private static final int UNDO = -1;
    /**
     * 1.
     * int kod za redo operaciju. Mnozenjem ne menja broj.
     */
    private static final int REDO = 1;
    /**
     * 4 (arbitrarni broj).
     * int kod za dodavanje. Nebitno koji je, dok god je razlicit od 0.
     */
    private static final int DODAVANJE = 4;
    /**
     * -DODAVANJE.
     * int kod za brisanje. Uvek suprotan od koda za DODAVANJE.
     */
    private static final int BRISANJE = DODAVANJE*(-1);
    /**
     * Field koji se koristi da naznaci koja operacija je u toku. 
     * Proizvod UNDO ili REDO i BRISANJE ili DODAVANJE.
     */
    private static int doWhat = 0;

    /**
     * Konstruktor. Proverava validnost podataka i ubacuje ih u odgovarajuce fieldove.
     * @param akcija akcija za undo
     * @param objs podaci za undo. Ako niz sadrzi dva elementa (uzimanje i vracanje), prvi je Ucenik
     * @throws UnsupportedOperationException ako podaci nisu zadovoljavajuci
     * @since 15.10.'14.
     */
    UndoAkcija(Akcija akcija, Object[] objs) {
        if (akcija.equals(Akcija.BRISANJE_KNJIGE)
                || akcija.equals(Akcija.DODAVANJE_KNJIGE) && objs[0] instanceof Knjiga) {
            this.akcija = akcija;
            knjiga = (Knjiga) objs[0];
        } else if (akcija.equals(Akcija.BRISANJE_UCENIKA)
                || akcija.equals(Akcija.DODAVANJE_UCENIKA) && objs[0] instanceof Ucenik) {
            this.akcija = akcija;
            ucenik = (Ucenik) objs[0];
        } else if (akcija.equals(Akcija.UZIMANJE) || akcija.equals(Akcija.VRACANJE)
                && objs[0] instanceof Ucenik && objs[1] instanceof Knjiga) {
            this.akcija = akcija;
            this.ucenik = (Ucenik) objs[0];
            this.knjiga = (Knjiga) objs[1];
        } else {
            throw new UnsupportedOperationException("Losi podaci za undo");
        }
    }
    
    /**
     * Vrsi undo ove akcije. Podesava doWhat na UNDO i zove {@link UndoAkcija#pickAndDo()}
     * @return naziv akcije na kojoj je radjen undo.
     * @since 15.10.'14.
     */
    protected String undo() {
        doWhat = UNDO;
        pickAndDo();
        doWhat = 0;
        return akcija.toString();
    }

    /**
     * Vrsi redo ove akcije. Podesava doWhat na REDO i zove {@link UndoAkcija#pickAndDo()}
     * @return naziv akcije na kojoj je radjen redo.
     * @since 15.10.'14.
     */
    protected String redo() {
        doWhat = REDO;
        pickAndDo();
        doWhat = 0;
        return akcija.toString();
    }

    /**
     * Mnozi doWhat sa odgovarajucim kodom i zove odgovarajucu metodu za undo/redo.
     * Oslanja se na metode iz {@link Akcija}
     * @since 15.10.'14
     */
    private void pickAndDo() {
        try {
            if(akcija.isBrisanje()) {
                doWhat*=BRISANJE;
            } else { 
                doWhat*=DODAVANJE;
            } 
            if(akcija.isKnjiga()) {
                knjige();
            } else if(akcija.isUcenik()) {
                ucenici();
            } else {
                uzimanjeVracanje();
            }
        } catch (VrednostNePostoji | PreviseKnjiga | NemaViseKnjiga ex) {
            throw new RuntimeException("Exception prilikom undo procesa", ex);
        } catch(Duplikat ex) {
            LOG.log(Level.FINE, "Duplikat pri undo");
            //smem ignorisati ???
        }
    }

    /**
     * Metoda za undo i redo dodavanja i brisanja knjige. Operacija koja se vrsi 
     * (dodavanje ili brisanje) odredjena je doWhat kodom
     * @throws Duplikat 
     * @since 15.10.'14.
     */
    private void knjige() throws Duplikat, PreviseKnjiga {
        if (doWhat == DODAVANJE) {
            Podaci.dodajKnjigu(knjiga);
        } else if (doWhat == BRISANJE) {
            Podaci.obrisiKnjigu(knjiga);
        }
    }

    /**
     * Metoda za undo i redo dodavanja i brisanja ucenika. Operacija koja se vrsi 
     * (dodavanje ili brisanje) odredjena je doWhat kodom
     * @since 15.10.'14.
     */
    private void ucenici() throws Duplikat, PreviseKnjiga {
        if (doWhat == DODAVANJE) {
            Podaci.dodajUcenika(ucenik);
        } else if (doWhat == BRISANJE) {
            Podaci.obrisiUcenika(ucenik);
        }
    }

    /**
     * Metoda za undo i redo uzimanja i vracanja. Operacija koja se vrsi (dodavanje ili brisanje, 
     * tj. uzimanje ili vracanje) odredjena je doWhat kodom. Pravi modifikacije direktno na objektu.
     * Ove promene ne ulaze u undo stack niti se loguju.
     * @throws VrednostNePostoji
     * @throws PreviseKnjiga
     * @throws Duplikat
     * @throws NemaViseKnjiga 
     * @since 15.10.'14.
     */
    private void uzimanjeVracanje() throws VrednostNePostoji, PreviseKnjiga, Duplikat, NemaViseKnjiga {
        if (doWhat == BRISANJE) {
            ucenik.setKnjiga(knjiga.getNaslov());
            knjiga.smanjiKolicinu();
        } else if (doWhat == DODAVANJE) {
            ucenik.clearKnjiga(knjiga.getNaslov());
            knjiga.povecajKolicinu();
        }
    }
    private static final Logger LOG = Logger.getLogger(UndoAkcija.class.getName());
}
}
