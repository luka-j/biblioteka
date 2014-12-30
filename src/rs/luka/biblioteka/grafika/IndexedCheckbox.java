package rs.luka.biblioteka.grafika;

import java.awt.Insets;
import javax.swing.JCheckBox;
import javax.swing.border.EmptyBorder;
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
     * Kolona u kojoj se knjiga nalazi (moze biti -1 ako se radi o uceniku).
     */
    private final int kol;
    
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
        super.setFont(Grafika.getLabelFont());
        super.setForeground(Grafika.getFgColor());
        super.setBackground(Grafika.getBgColor());
        super.setBorder(new EmptyBorder(INSET));
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
}
