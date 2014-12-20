package rs.luka.biblioteka.grafika;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.swing.JButton;
import rs.luka.biblioteka.data.Podaci;

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
        this.setBounds(Konstante.UVBUTTON_X, y, Konstante.UVBUTTON_WIDTH, Konstante.UVBUTTON_HEIGHT);
        this.setFont(Grafika.getSmallButtonFont());
    }

    /**
     * Postavlja akciju button-a na uzimanje i prikazuje ga.
     */
    public void uzmi() {
        this.setText("Iznajmi knjigu");
        this.addActionListener((ActionEvent e) -> {
            new Uzimanje().uzmi(ucenikIndex);
            new Ucenici();
        });
        this.setVisible(true);
    }

    /**
     * Postavlja akciju button-a na vracanje i prikazuje ga.
     */
    public void vrati() {
        this.setText("Vrati knjigu");
        this.addActionListener((ActionEvent e) -> {
            Podaci.vratiViseKnjigaSafe(ucenikIndex, naslovi);
            new Ucenici();
        });
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
            System.out.println("contains");
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
    
    private static final Logger LOG = Logger.getLogger(UzmiVratiButton.class.getName());
    
}
