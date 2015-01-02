package rs.luka.biblioteka.grafika;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import rs.luka.biblioteka.data.Podaci;
import static rs.luka.biblioteka.grafika.Konstante.*;

/**
 * Opisuje dugme na sidePan-u. Može biti za uzimanje ili za vracanje.
 * @since 29.11.'14.
 */
class SmallButton extends JButton {
    private static final long serialVersionUID = 1L;
    private final int index;
    private final List<Integer> naslovi = new ArrayList<>();

    /**
     * Konstruiše dugme, ali ga ne prikazuje niti ubacuje u listu postojećih.
     * @param ucIndex index učenika za kog se pravi dugme
     * @param naslovi naslovi selektovanih knjiga
     * @param y
     */
    SmallButton(int ucIndex, int naslov, int y) {
        super();
        this.setVisible(false);
        index = ucIndex;
        naslovi.add(naslov);
        this.setBounds(SMALLBUT_X, y, SMALLBUT_WIDTH, SMALLBUT_HEIGHT);
        this.setFont(Grafika.getSmallButtonFont());
    }

    /**
     * Postavlja akciju button-a na uzimanje i prikazuje ga.
     */
    public void uzmi() {
        this.setText(SMALLBUT_UZMI_STRING);
        this.addActionListener((ActionEvent e) -> {
            if(this.getActionListeners().length>1)
                return;
            new Uzimanje().uzmi(index);
            Knjige.refresh();
            new Ucenici().pregledUcenika();
        });
        LOGGER.log(Level.FINE, "Dodato dugme za uzimanje br {0}", index);
        this.setVisible(true);
    }

    /**
     * Postavlja akciju button-a na vracanje i prikazuje ga.
     */
    public void vrati() {
        this.setText(SMALLBUT_VRATI_STRING);
        this.addActionListener((ActionEvent e) -> {
            Podaci.vratiViseKnjigaSafe(index, naslovi);
            Knjige.refresh();
            new Ucenici().pregledUcenika();
        });
        LOGGER.log(Level.FINE, "Dodato dugme za vraćanje br {0}", index);
        this.setVisible(true);
    }
    
    public void setKol() {
        this.setText(SMALLBUT_SETKOL_STRING);
        this.addActionListener((ActionEvent e) -> {
            new KnjigeUtils().promeniKolicinu(index);
            Podaci.sortKnjige();
            Knjige.refresh();
        });
        this.setVisible(true);
    }

    /**
     * Vraca index ucenika na koga se odnosi dugme
     * @return ucenikIndex
     */
    public int getIndex() {
        return index;
    }

    /**
     * Dodaje naslov za vracanje
     * @param naslov 
     */
    public void addNaslovZaVracanje(int naslov) {
        naslovi.add(naslov);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SmallButton && ((SmallButton) obj).getIndex() == index) {
            return true;
        }
        if (obj instanceof Integer && (Integer)obj == index) {
            return true;
        }
        /*if(obj instanceof JTextField) {
            StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
            for(StackTraceElement el : stackTrace) {
                System.out.println(el);
            }
        }*/
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + this.index;
        return hash;
    }
    
    private static final Logger LOGGER = Logger.getLogger(SmallButton.class.getName());
}
