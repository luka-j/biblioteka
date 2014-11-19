package rs.luka.biblioteka.funkcije;

import java.util.ArrayList;
import static java.util.Arrays.asList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import javax.swing.JOptionPane;
import rs.luka.biblioteka.data.Podaci;
import static rs.luka.biblioteka.data.Podaci.addUcenik;
import static rs.luka.biblioteka.data.Podaci.getBrojUcenika;
import static rs.luka.biblioteka.data.Podaci.getUcenik;
import static rs.luka.biblioteka.data.Podaci.povecajRazred;
import rs.luka.biblioteka.data.Ucenik;
import rs.luka.biblioteka.exceptions.Duplikat;
import rs.luka.biblioteka.exceptions.PreviseKnjiga;
import rs.luka.biblioteka.exceptions.VrednostNePostoji;

/**
 * @since 1.8.'13.
 * @author Luka
 */
public class Ucenici {

    private static final java.util.logging.Logger LOGGER
            = java.util.logging.Logger.getLogger(Ucenici.class.getName());

    /**
     * Metoda za dodavanje ucenika sa praznim knjigama.
     *
     * @param ime ime ucenika. Zove Main.addUc
     * @param raz razred
     * @throws IllegalArgumentException ako niz knjige nije odgovarajuce
     * velicine
     * @since 1.8.'13.
     */
    public void dodajUcenika(String ime, int raz) throws Duplikat {
        String knjige[] = new String[Podaci.getMaxBrojUcenikKnjiga()];
        for (int i = 0; i < knjige.length; i++) {
            knjige[i] = "";
        }
        addUcenik(ime, raz, knjige);
    }

    /**
     * Dodaje ucenika, samo za novu generaciju.
     *
     * @param ime ime ucenika
     * @since 1.8.'13.
     */
    public void dodajUcenika(String ime) throws Duplikat {
        dodajUcenika(ime, Ucenik.getPrviRazred());
    }

    /**
     * @throws VrednostNePostoji ako ucenik nije pronadjen
     * @throws rs.luka.biblioteka.exceptions.PreviseKnjiga ako se kod ucenika
     * nalaze neke knjige
     * @since 2.8.'13.
     * @param inx index ucenika
     */
    public void obrisiUcenika(int inx) throws VrednostNePostoji, PreviseKnjiga {
        Ucenik uc = getUcenik(inx);
        if (uc.getBrojKnjiga() > 0) {
            throw new PreviseKnjiga(inx);
        } else if (uc.getBrojKnjiga() < 0) {
            throw new RuntimeException("Ucenik " + Podaci.getUcenik(inx).getIme()
                    + "ima negativan broj knjiga :/");
        }
        Podaci.obrisiUcenika(inx);
    }

    /**
     * @param novaGen ucenici
     * @since 1.8.'13.
     * @since 25.8.'13.
     */
    public void dodajNovuGen(String novaGen) {
        LOGGER.log(Level.INFO, "Iniciram dodavanje nove generacije...");
        List<String> ucenici = asList(novaGen.split("\\s*,\\s*"));
        ucenici.stream().forEach((ucenik) -> {
            try {
                dodajUcenika(ucenik);
            } catch (Duplikat ex) {
                JOptionPane.showMessageDialog(null, "Uneli ste dva učenika sa istim imenom i prezimenom. "
                        + "Jedan od njih neće biti unet.", "Duplikat", JOptionPane.WARNING_MESSAGE);
            }
        });
        povecajRazred();
        LOGGER.log(Level.INFO, "Nova generacija uspešno dodata i stara je obrisana.");
    }

    /**
     * Metoda za pretragu ucenika. Ako ne pronadje potpuno poklapanje, trazi
     * delimicno(da se u imenu ucenika nalazi String pocetak).
     *
     * @param pocetak prefix ucenika
     * @return ucenik koji pocinje prefix-om, ako ne postoji, sve ucenike koji
     * sadrze pocetak
     */
    public ArrayList<Integer> pretraziUcenike(String pocetak) {
        int brojUcenika = getBrojUcenika();
        ArrayList<Integer> inx = new ArrayList<>();
        inx.ensureCapacity(brojUcenika / 32);
        Iterator<Ucenik> it = Podaci.iteratorUcenika();
        int i = 0;
        while (it.hasNext()) {
            if (it.next().getIme().toLowerCase().startsWith(pocetak.toLowerCase())) {
                inx.add(i);
            }
            i++;
        }
        if (inx.isEmpty()) {
            it = Podaci.iteratorUcenika();
            i = 0;
            while (it.hasNext()) {
                if (it.next().getIme().toLowerCase().contains(pocetak.toLowerCase())) {
                    inx.add(i);
                }
                i++;
            }
        }
        LOGGER.log(Level.INFO, "Pronađeno {0} rezultata za upit \"{1}\"",
                new Object[]{inx.size(), pocetak});
        return inx;
    }
}
