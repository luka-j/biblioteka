package rs.luka.biblioteka.funkcije;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import rs.luka.biblioteka.data.Knjiga;
import rs.luka.biblioteka.data.Podaci;
import static rs.luka.biblioteka.data.Podaci.addKnjiga;
import static rs.luka.biblioteka.data.Podaci.getBrojKnjiga;
import static rs.luka.biblioteka.data.Podaci.getKnjiga;
import static rs.luka.biblioteka.data.Podaci.getMaxBrojUcenikKnjiga;
import static rs.luka.biblioteka.data.Podaci.indexOfNaslov;
import rs.luka.biblioteka.data.Ucenik;
import rs.luka.biblioteka.exceptions.Duplikat;
import rs.luka.biblioteka.exceptions.PreviseKnjiga;
import rs.luka.biblioteka.exceptions.VrednostNePostoji;

/**
 *
 * @author Luka
 * @since 2.7.'13.
 */
public class Knjige {

    private static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(Knjige.class.getName());

    /**
     * Upisuje novi naslov u memoriju.
     *
     * @param nas naslov
     * @param kol kolicina
     * @param pisac pisac
     * @throws rs.luka.biblioteka.exceptions.VrednostNePostoji ako je string nas
     * prazan
     * @throws rs.luka.biblioteka.exceptions.Duplikat ako naslov vec postoji
     * @since pocetak (9.'14.)
     */
    public void ubaciNoviNaslov(String nas, int kol, String pisac) throws VrednostNePostoji, Duplikat {
        if ("".equals(nas)) {
            throw new VrednostNePostoji(VrednostNePostoji.vrednost.Knjiga);
        }
        addKnjiga(nas, kol, pisac);
    }

    /**
     * Za svaku knjigu u listi, trazi index knjige i zove
     * {@link Podaci#vratiKnjigu(int, int)}
     *
     * @param indexUcenika index ucenika
     * @param indexKnjiga lista sa indexima knjiga za vracanje
     * @see Podaci#indexOfNaslov(java.lang.String)
     * @see Podaci#vratiKnjigu(int, int)
     * @since 9.'14.
     */
    public void vratiKnjigu(int indexUcenika, List<Integer> indexKnjiga) {
        for (Integer indexKnjige : indexKnjiga) {
            try {
                Podaci.vratiKnjigu(indexUcenika, indexKnjige);
            } catch (VrednostNePostoji ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    /**
     * Proverava da li se knjiga datog naslova nalazi kod nekog ucenika, ako ne
     * zove metodu za brisanje naslova iz liste.
     *
     * @param inx index knjige za brisanje
     * @throws rs.luka.biblioteka.exceptions.PreviseKnjiga ako se kod nekog ucenika nalazi data knjiga.
     * @since 16.9.'14.
     */
    public void obrisiNaslov(int inx) throws PreviseKnjiga {
        String naslov = Podaci.getKnjiga(inx).getNaslov();
        Iterator<Ucenik> it = Podaci.iteratorUcenika();
        while(it.hasNext()) {
            if(it.next().hasKnjiga(naslov)) 
                throw new PreviseKnjiga(inx);
        }
        Podaci.obrisiKnjigu(inx);
    }

    /**
     * Trazi knjige prema odredjenom pocetku. Ako takva knjiga ne postoji, trazi
     * knjige koje sadrze taj string.
     *
     * @param pocetak
     * @return ArrayList sa indexima knjiga koje odgovaraju pretrazi
     * @since '14.
     */
    public ArrayList<Integer> pretraziKnjige(String pocetak) {
        int brojKnjiga = getBrojKnjiga();
        ArrayList<Integer> inx = new ArrayList<>();
        inx.ensureCapacity(brojKnjiga / 16);
        Iterator <Knjiga> it = Podaci.iteratorKnjiga(); int i=0; Knjiga knj;
        while(it.hasNext()) {
            knj = it.next();
            if (knj.getNaslov().toLowerCase().startsWith(pocetak.toLowerCase())
                    || knj.getPisac().toLowerCase().startsWith(pocetak)) { //startsWith ili equals?
                inx.add(i);
            }
            i++;
        }
        if (inx.isEmpty()) {
            it = Podaci.iteratorKnjiga(); i=0;
            while(it.hasNext()) {
                if (it.next().getNaslov().toLowerCase().contains(pocetak.toLowerCase())) {
                    inx.add(i);
                }
            }
        }
        LOGGER.log(Level.INFO, "Pronađeno {0} rezultata za upit \"{1}\"",
                new Object[]{inx.size(), pocetak});
        return inx;
    }

    /**
     * Vraca ucenike koji imaju knjigu (argument) u ArrayList.
     *
     * @param knj naslov knjige
     * @return Listu sa indexima knjige i u kom slotu se ta knjiga nalazi
     * @throws rs.luka.biblioteka.exceptions.VrednostNePostoji ako knjiga sa tim
     * naslovom ne postoji
     * @since 25.6.'14.
     */
    public ArrayList<Point> pretraziUcenike(String knj) throws VrednostNePostoji {
        final ArrayList<Point> inx = new ArrayList<>();
        try {
            inx.ensureCapacity(getKnjiga(indexOfNaslov(knj)).getKolicina() / 8);
        } catch (IndexOutOfBoundsException ex) {
            throw new VrednostNePostoji(VrednostNePostoji.vrednost.Knjiga, ex);
        }
        int i=0;
        Iterator<Ucenik> it= Podaci.iteratorUcenika(); Ucenik uc;
        while(it.hasNext()) {
            uc=it.next();
            for (int j = 0; j < getMaxBrojUcenikKnjiga(); j++) {
                if (uc.getNaslovKnjige(j).equals(knj)) {
                    inx.add(new Point(j, i));
                }
            }
            i++;
        }
        LOGGER.log(Level.INFO, "Pronađeno {0} rezultata za upit \"{1}\"",
                new Object[]{inx.size(), knj});
        return inx;
    }
}
