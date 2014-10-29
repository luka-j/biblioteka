package rs.luka.biblioteka.funkcije;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import javax.swing.JOptionPane;
import rs.luka.biblioteka.data.Podaci;
import static rs.luka.biblioteka.data.Podaci.addKnjiga;
import static rs.luka.biblioteka.data.Podaci.getBrojKnjiga;
import static rs.luka.biblioteka.data.Podaci.getBrojUcenika;
import static rs.luka.biblioteka.data.Podaci.getKnjiga;
import static rs.luka.biblioteka.data.Podaci.getMaxBrojUcenikKnjiga;
import static rs.luka.biblioteka.data.Podaci.getUcenik;
import static rs.luka.biblioteka.data.Podaci.indexOfNaslov;
import rs.luka.biblioteka.exceptions.Duplikat;
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
    public void vracanje(int indexUcenika, List<Integer> indexKnjiga) {
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
     * zove metodu za brisanje naslova iz liste. Radi exception handling.
     *
     * @param inx index knjige za brisanje
     * @since 16.9.'14.
     */
    public void obrisiNaslov(int inx) {
        String naslov = Podaci.getKnjiga(inx).getNaslov();
        for (int i = 0; i < Podaci.getBrojUcenika(); i++) {
            if (Podaci.getUcenik(i).hasKnjiga(naslov)) {
                LOGGER.log(Level.INFO, "Knjiga zauzeta. Brisanja naslova nije obavljeno");
                JOptionPane.showMessageDialog(null, "Zauzeta knjiga", "Kod ucenika "
                        + Podaci.getUcenik(i).getIme() + " se nalazi ova knjiga\n"
                        + "Kada vrati knjigu, pokusajte ponovo.", JOptionPane.ERROR_MESSAGE);
                return;
            }
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
        for (int i = 0; i < brojKnjiga; i++) {
            if (getKnjiga(i).getNaslov().toLowerCase().startsWith(pocetak.toLowerCase())
                    || getKnjiga(i).getPisac().toLowerCase().startsWith(pocetak)) { //startsWith ili equals?
                inx.add(i);
            }
        }
        if (inx.isEmpty()) {
            for (int i = 0; i < brojKnjiga; i++) {
                if (getKnjiga(i).getNaslov().toLowerCase().contains(pocetak.toLowerCase())) {
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
        ArrayList<Point> inx = new ArrayList<>();
        try {
            inx.ensureCapacity(getKnjiga(indexOfNaslov(knj)).getKolicina() / 8);
        } catch (IndexOutOfBoundsException ex) {
            throw new VrednostNePostoji(VrednostNePostoji.vrednost.Knjiga, ex);
        }
        for (int i = 0; i < getBrojUcenika(); i++) {
            for (int j = 0; j < getMaxBrojUcenikKnjiga(); j++) {
                if (getUcenik(i).getNaslovKnjige(j).equals(knj)) {
                    inx.add(new Point(j, i));
                }
            }
        }
        LOGGER.log(Level.INFO, "Pronađeno {0} rezultata za upit \"{1}\"", 
                new Object[]{inx.size(), knj});
        return inx;
    }
}
