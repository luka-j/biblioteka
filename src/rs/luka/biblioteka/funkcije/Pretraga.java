package rs.luka.biblioteka.funkcije;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import rs.luka.biblioteka.data.Knjiga;
import rs.luka.biblioteka.data.Podaci;
import static rs.luka.biblioteka.data.Podaci.getBrojKnjiga;
import static rs.luka.biblioteka.data.Podaci.getBrojUcenika;
import static rs.luka.biblioteka.data.Podaci.getMaxBrojUcenikKnjiga;
import rs.luka.biblioteka.data.Ucenik;
import rs.luka.biblioteka.exceptions.VrednostNePostoji;

/**
 *
 * @author luka
 */
public class Pretraga {
    private static final java.util.logging.Logger LOGGER = 
            java.util.logging.Logger.getLogger(Pretraga.class.getName());
    
     /**
     * Trazi knjige prema odredjenom pocetku. Ako takva knjiga ne postoji, trazi
     * knjige koje sadrze taj string.
     *
     * @param pocetak
     * @return ArrayList sa indexima knjiga koje odgovaraju pretrazi
     * @since '14.
     */
    public static ArrayList<Integer> pretraziKnjige(String pocetak) {
        pocetak = pocetak.toLowerCase();
        ArrayList<Integer> inx = new ArrayList<>(), wider = new ArrayList<>();
        Iterator <Knjiga> it = Podaci.iteratorKnjiga(); int i=0; Knjiga knj;
        while(it.hasNext()) {
            knj = it.next();
            if (knj.getNaslov().toLowerCase().startsWith(pocetak)
                    || knj.getPisac().toLowerCase().startsWith(pocetak)
                    || knj.getPisac().toLowerCase().endsWith(pocetak)) { //startsWith ili equals?
                inx.add(i);
            }
            else if(knj.getNaslov().toLowerCase().contains(pocetak))
                wider.add(i);
            i++;
        }
        if(inx.isEmpty()) {
            LOGGER.log(Level.INFO, "Pronađeno {0} rezultata za upit \"{1}\"",
                    new Object[]{wider.size(), pocetak});
            return wider;
        }
        else {
            LOGGER.log(Level.INFO, "Pronađeno {0} rezultata za upit \"{1}\"",
                    new Object[]{inx.size(), pocetak});
            return inx;
        }
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
    public static ArrayList<Point> pretraziUcenikePoNaslovu(String knj) throws VrednostNePostoji {
        final ArrayList<Point> inx = new ArrayList<>();
        if(!Podaci.naslovExists(knj)) {
            throw new VrednostNePostoji(VrednostNePostoji.vrednost.Knjiga);
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
    
        /**
     * Metoda za pretragu ucenika. Ako ne pronadje potpuno poklapanje, trazi
     * delimicno(da se u imenu ucenika nalazi String pocetak).
     *
     * @param pocetak prefix ucenika
     * @return ucenik koji pocinje prefix-om, ako ne postoji, sve ucenike koji
     * sadrze pocetak. Аko takvi ne postoje, vraća praznu listu.
     */
    public static ArrayList<Integer> pretraziUcenike(String pocetak) {
        pocetak = pocetak.toLowerCase();
        int brojUcenika = getBrojUcenika();
        ArrayList<Integer> inx = new ArrayList<>(), wider = new ArrayList<>();
        inx.ensureCapacity(brojUcenika / 32);
        Iterator<Ucenik> it; Ucenik uc;
        int i = 0;
        for(i=0, it=Podaci.iteratorUcenika(); it.hasNext(); i++) {
            uc=it.next();
            if (uc.getIme().toLowerCase().startsWith(pocetak) ||
                    uc.searchKnjiga(pocetak)) {
                inx.add(i);
            }
            else if(uc.getIme().toLowerCase().startsWith(pocetak))
                wider.add(i);
        }
        if (inx.isEmpty()) {
            LOGGER.log(Level.INFO, "Pronađeno {0} rezultata za upit \"{1}\"",
                new Object[]{wider.size(), pocetak});
            return wider;
        } else {
            LOGGER.log(Level.INFO, "Pronađeno {0} rezultata za upit \"{1}\"",
                new Object[]{inx.size(), pocetak});
            return inx;
        }
    }

    private Pretraga() {
    }
}
