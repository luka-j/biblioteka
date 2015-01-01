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
class UzmiVratiButton extends JButton {
    private static final long serialVersionUID = 1L;
    private final int ucenikIndex;
    private final List<Integer> naslovi = new ArrayList<>();

    /**
     * Konstruiše dugme, ali ga ne prikazuje niti ubacuje u listu postojećih.
     * @param ucIndex index učenika za kog se pravi dugme
     * @param naslovi naslovi selektovanih knjiga
     * @param y
     */
    UzmiVratiButton(int ucIndex, int naslov, int y) {
        super();
        this.setVisible(false);
        ucenikIndex = ucIndex;
        naslovi.add(naslov);
        this.setBounds(UVBUTTON_X, y, UVBUTTON_WIDTH, UVBUTTON_HEIGHT);
        this.setFont(Grafika.getSmallButtonFont());
    }

    /**
     * Postavlja akciju button-a na uzimanje i prikazuje ga.
     */
    public void uzmi() {
        this.setText(UVBUTTON_UZMI_STRING);
        this.addActionListener((ActionEvent e) -> {
            if(this.getActionListeners().length>1)
                return;
            new Uzimanje().uzmi(ucenikIndex);
            Knjige.refresh();
            new Ucenici().pregledUcenika();
        });
        LOGGER.log(Level.FINE, "Dodato dugme za uzimanje br {0}", ucenikIndex);
        this.setVisible(true);
    }

    /**
     * Postavlja akciju button-a na vracanje i prikazuje ga.
     */
    public void vrati() {
        this.setText(UVBUTTON_VRATI_STRING);
        this.addActionListener((ActionEvent e) -> {
            Podaci.vratiViseKnjigaSafe(ucenikIndex, naslovi);
            Knjige.refresh();
            new Ucenici().pregledUcenika();
        });
        LOGGER.log(Level.FINE, "Dodato dugme za vraćanje br {0}", ucenikIndex);
        this.setVisible(true);
    }

    /**
     * Vraca index ucenika na koga se odnosi dugme
     * @return ucenikIndex
     */
    public int getIndex() {
        return ucenikIndex;
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
        if (obj instanceof UzmiVratiButton && ((UzmiVratiButton) obj).getIndex() == ucenikIndex) {
            return true;
        }
        if (obj instanceof Integer && (Integer)obj == ucenikIndex) {
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
        hash = 41 * hash + this.ucenikIndex;
        return hash;
    }
    
    private static final Logger LOGGER = Logger.getLogger(UzmiVratiButton.class.getName());
}
