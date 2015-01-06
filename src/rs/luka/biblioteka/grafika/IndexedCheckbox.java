package rs.luka.biblioteka.grafika;

import java.awt.Insets;
import javax.swing.JCheckBox;
import javax.swing.border.EmptyBorder;
import rs.luka.biblioteka.data.Knjiga;
import rs.luka.biblioteka.data.Podaci;
import static rs.luka.biblioteka.grafika.Konstante.*;

/**
 * JCheckBox koji cuva index i kolonu knjige (ako je za vracanje) unutar objekta.
 * @author luka
 */
public class IndexedCheckbox extends JCheckBox {
    private final Insets INSET = new Insets(CHECKBOX_TOP_INSET, CHECKBOX_LEFT_INSET,
            CHECKBOX_BOTTOM_INSET, CHECKBOX_RIGHT_INSET);
    /**
     * Index (red) ucenika ili knjige.
     */
    private final int index;
    /**
     * Kolona u kojoj se knjiga ucenika (UcenikKnjiga) nalazi, moze biti -1 ako se radi o Uceniku ili Knjizi.
     */
    private final int kol;
    /**
     * Ako se koristi za knjigu, index knjige (zbog uzimanja/vracanja, knjige sa istim imenom)
     */
    private int knjIndex;
    
    /**
     * Konstruktuje novi objekat prema datim parametrima. Ako je u pitanju JCheckBox
     * za ucenika, kol se ignorise. Postavlja font, boje i inset unutar konstruktora.
     * @param text Text checkboxa
     * @param index index (red) na kojem se JCheckBox nalazi, tj. koji je po redu.
     * @param kol ako se radi o knjizi, kolona u kojoj se nalazi. U suprotnom se ignorise.
     */
    public IndexedCheckbox(String text, int index, int kol) {
        super(text);
        this.index = index;
        this.kol = kol;
        knjIndex = -1;
        this.setFont(Grafika.getLabelFont());
        this.setForeground(Grafika.getFgColor());
        this.setBackground(Grafika.getBgColor());
        this.setBorder(new EmptyBorder(INSET));
    }
    
    public IndexedCheckbox(Knjiga knj, int red, int kol) {
        super(" ");
        if(knj != null) {
            super.setText(knj.getNaslov());
        }
        this.index = red;
        this.kol = kol;
        this.setFont(Grafika.getLabelFont());
        this.setForeground(Grafika.getFgColor());
        this.setBackground(Grafika.getBgColor());
        this.setBorder(new EmptyBorder(INSET));
        knjIndex = Podaci.indexOfNaslov(knj);
    }
    
    /**
     * Vraca index (red) checkboxa
     * @return {@link #index}
     */
    public int getIndex() {
        return index;
    }
    
    /**
     * Vraca kolonu u kojoj se knjiga nalazi (ili koja god vrednost je prosledjena
     * konstruktoru ako se radi o uceniku)
     * @return {@link #kol}
     */
    public int getKol() {
        return kol;
    }
    
    /**
     * Vraca index knjige na koju se ovaj checkbox odnosi.
     * @return {@link #knjIndex}
     */
    public int getKnjIndex() {
        return knjIndex;
    }
    
    /**
     * Postavlja text checkboxa na naslov ove knjige i index na index ove knjige u listi.
     * @param knj knjiga na koju se ovaj checkbox odnosi
     */
    public void setKnjiga(Knjiga knj) {
        this.setText(knj.getNaslov());
        this.knjIndex = Podaci.indexOfNaslov(knj);
    }
}
