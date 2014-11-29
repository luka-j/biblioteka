package rs.luka.biblioteka.grafika;

import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import static java.lang.Integer.parseUnsignedInt;
import java.util.List;
import java.util.logging.Level;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.showMessageDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.WindowConstants;
import static rs.luka.biblioteka.data.Podaci.indexOfUcenik;
import rs.luka.biblioteka.exceptions.Duplikat;
import rs.luka.biblioteka.exceptions.PreviseKnjiga;
import rs.luka.biblioteka.exceptions.VrednostNePostoji;
import static rs.luka.biblioteka.grafika.Konstante.*;

                    
/**
 *
 * @author luka
 */
public class UceniciUtils {
    private static final java.util.logging.Logger LOGGER
            = java.util.logging.Logger.getLogger(UceniciUtils.class.getName());
    
    private final Dimension DODAJUC_SIZE = new Dimension(DODAJUCENIKA_WIDTH, DODAJUCENIKA_HEIGHT);
    private final Rectangle IME_BOUNDS = new Rectangle(DODAJUCENIKA_TEXT_X, DODAJUCENIKA_IME_Y,
                                                       DODAJUCENIKA_LABEL_WIDTH, DODAJUCENIKA_LABEL_HEIGHT);
    private final Rectangle IMETF_BOUNDS = new Rectangle(DODAJUCENIKA_TEXT_X, 
            DODAJUCENIKA_IMETF_Y, DODAJUCENIKA_TEXTFIELD_WIDTH, DODAJUCENIKA_TEXTFIELD_HEIGHT);
    private final Rectangle RAZRED_BOUNDS = new Rectangle(DODAJUCENIKA_TEXT_X, DODAJUCENIKA_RAZRED_Y, 
            DODAJUCENIKA_LABEL_WIDTH, DODAJUCENIKA_LABEL_HEIGHT);
    private final Rectangle RAZREDTF_BOUNDS = new Rectangle(DODAJUCENIKA_TEXT_X, DODAJUCENIKA_RAZREDTF_Y, 
            DODAJUCENIKA_TEXTFIELD_WIDTH, DODAJUCENIKA_TEXTFIELD_HEIGHT);
    private final Rectangle UNESI_BOUNDS = new Rectangle(DODAJUCENIKA_DODAJ_X, DODAJUCENIKA_DODAJ_Y, 
            DODAJUCENIKA_DODAJ_WIDTH, DODAJUCENIKA_DODAJ_HEIGHT);
    
    /**
     * Prozor za dodavanje novog ucenika.
     *
     * @since 1.7.'13.
     */
    public void dodajNovogUcenika() {
        final rs.luka.biblioteka.funkcije.Ucenici ucenici = new rs.luka.biblioteka.funkcije.Ucenici();
        //---------JFrame&JPanel------------------------------------------------
        JDialog win = new JDialog();
        win.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        win.setTitle("Dodavanje novog ucenika");
        win.setSize(DODAJUC_SIZE);
        win.setLocationRelativeTo(null);
        win.setResizable(false);
        win.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        JPanel pan = new JPanel(null);
        pan.setBackground(Grafika.getBgColor());
        win.setContentPane(pan);
        //---------JLabel&JTextField--------------------------------------------
        JLabel ime = new JLabel("Unesite ime ucenika:");
        ime.setBounds(IME_BOUNDS);
        ime.setBackground(Grafika.getBgColor());
        ime.setForeground(Grafika.getFgColor());
        ime.setFont(Grafika.getLabelFont());
        pan.add(ime);
        final JTextField ucTF = new JTextField();
        ucTF.setBounds(IMETF_BOUNDS);
        ucTF.setFont(Grafika.getLabelFont());
        ucTF.setBackground(Grafika.getTFColor());
        ucTF.setForeground(Grafika.getFgColor());
        ucTF.setCaretColor(Grafika.getFgColor());
        pan.add(ucTF);
        JLabel razred = new JLabel("Unesite razred koji ucenik trenutno pohadja:");
        razred.setBounds(RAZRED_BOUNDS);
        razred.setBackground(Grafika.getBgColor());
        razred.setFont(Grafika.getLabelFont());
        razred.setForeground(Grafika.getFgColor());
        pan.add(razred);
        final JTextField razTF = new JTextField();
        razTF.setBounds(RAZREDTF_BOUNDS);
        razTF.setFont(Grafika.getLabelFont());
        razTF.setForeground(Grafika.getFgColor());
        razTF.setCaretColor(Grafika.getFgColor());
        razTF.setBackground(Grafika.getTFColor());
        pan.add(razTF);
        //---------JButton------------------------------------------------------
        JButton unesi = new JButton("Unesi podatke");
        unesi.setBounds(UNESI_BOUNDS);
        unesi.addActionListener((ActionEvent e) -> {
            try {
                ucenici.dodajUcenika(ucTF.getText(), parseUnsignedInt(razTF.getText()));
                showMessageDialog(null, "Ucenik dodat!", "Uspeh!", JOptionPane.INFORMATION_MESSAGE);
                win.dispose();
            } catch (NumberFormatException ex) {
                LOGGER.log(Level.INFO, "Razred novog učenika ({0}) je prevelik "
                        + "ili nije broj", razTF.getText());
                showMessageDialog(null, "Razred ucenika je prevelik.", "Los razred", JOptionPane.ERROR_MESSAGE);
            } catch (Duplikat ex) {
                LOGGER.log(Level.INFO, "Već postoji učenik sa istim imenom i prezimenom i razredom {0} i {1}"
                        + "Novi učenik nije dodat.", new Object[]{ucTF.getText(), razTF.getText()});
                showMessageDialog(null, "Već postoji učenik koji ide u isti razred sa istim imenom i prezimenom"
                        + "Novi učenik ne može biti dodat", "Duplikat", JOptionPane.ERROR_MESSAGE);
            }
        });
        pan.add(unesi);
        //---------setVisible---------------------------------------------------
        win.setVisible(true);
    }

    /**
     * Prozor za brisanje ucenika.
     *
     * @since 2.7.'13.
     */
    public void obrisiUcenika() {
        final rs.luka.biblioteka.funkcije.Ucenici ucenici = new rs.luka.biblioteka.funkcije.Ucenici();
        //---------JFrame&JPanel------------------------------------------------
        String ucenik = Dijalozi.showTextFieldDialog("Brisanje ucenika", "Unesite ime ucenika "
                + "i pritisnite enter:", "");
        if(ucenik == null || ucenik.equals("null")) return;
        List<Integer> inx = indexOfUcenik(ucenik);
        if (inx.isEmpty()) {
            LOGGER.log(Level.INFO, "Učenik {0} nije pronađen", ucenik);
            showMessageDialog(null, "Ucenik " + ucenik + " nije pronadjen\n"
                    + "Proverite unos  i pokusajte ponovo.", "Ucenik ne postoji.",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            int num = 0;
            if (inx.size() > 1) {
                Dijalozi.viseRazreda(inx);
            }
            try {
                ucenici.obrisiUcenika(inx.get(num));
                showMessageDialog(null, "Ucenik obrisan!", "Uspeh!", JOptionPane.INFORMATION_MESSAGE);
            } catch (VrednostNePostoji ex) {
                throw new RuntimeException("VrednostNePostoji prilikom obrisiUcenika za index "
                        + inx.get(num), ex);
            } catch (PreviseKnjiga ex) {
                LOGGER.log(Level.INFO, "Učenik {0} nije obrisan jer ima preostalih knjiga.", inx.get(num));
                showMessageDialog(null, "Ucenik ima preostalih knjiga.\n"
                        + "Kada vrati, pokusajte ponovo.", "Preostale knjige", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private final Dimension DODAJGEN_SIZE = new Dimension(DODAJGENERACIJU_WIDTH, DODAJGENERACIJU_HEIGHT);
    private final Rectangle IMENA_BOUNDS = new Rectangle(DODAJGENERACIJU_TEXT_X, DODAJGENERACIJU_IMENA_Y, 
            DODAJGENERACIJU_IMENA_WIDTH, DODAJGENERACIJU_IMENA_HEIGHT);
    private final Rectangle SCROLL_BOUNDS = new Rectangle(DODAJGENERACIJU_TEXT_X, DODAJGENERACIJU_SCROLL_Y,
            DODAJGENERACIJU_SCROLL_WIDTH, DODAJGENERACIJU_SCROLL_HEIGHT);
    private final Rectangle UNESIGEN_BOUNDS = new Rectangle(DODAJGENERACIJU_UNESI_X, DODAJGENERACIJU_UNESI_Y,
            DODAJGENERACIJU_UNESI_WIDTH, DODAJGENERACIJU_UNESI_HEIGHT);
    
    /**
     * Prozor za dodavanje nove generacije.
     *
     * @since 5.7.'13.
     */
    public void dodajNovuGeneraciju() {
        //---------JFrame&JPanel------------------------------------------------
        JDialog win = new JDialog();
        win.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        win.setTitle("Unos nove generacije.");
        win.setSize(DODAJGEN_SIZE);
        win.setLocationRelativeTo(null);
        win.setResizable(false);
        win.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        JPanel pan = new JPanel(null);
        pan.setBackground(Grafika.getBgColor());
        win.setContentPane(pan);
        //---------JLabel&JTextPane---------------------------------------------
        JLabel imena = new JLabel("<html>Unesite ucenike nove generacije, odvojene zapetama <br />"
                + "<strong>Nakon dodavanja nove generacije, svi učenici "
                + "najstarije generacije će biti obrisani!!!</strong></html>");
        imena.setBounds(IMENA_BOUNDS);
        imena.setBackground(Grafika.getBgColor());
        imena.setForeground(Grafika.getFgColor());
        imena.setFont(Grafika.getLabelFont());
        pan.add(imena);
        final JTextPane genTF = new JTextPane();
        final JScrollPane jsp = new JScrollPane(genTF);
        jsp.setBounds(SCROLL_BOUNDS);
        genTF.setFont(Grafika.getLabelFont());
        genTF.setForeground(Grafika.getFgColor());
        genTF.setCaretColor(Grafika.getFgColor());
        genTF.setBackground(Grafika.getTFColor());
        pan.add(jsp);
        //----------JButton-----------------------------------------------------
        JButton but = new JButton("Unesi novu generaciju");
        but.setBounds(UNESIGEN_BOUNDS);
        but.addActionListener((ActionEvent e) -> {
            new rs.luka.biblioteka.funkcije.Ucenici().dodajNovuGen(genTF.getText());
            showMessageDialog(null, "Nova generacija dodata.",
                    "Uspeh!", JOptionPane.INFORMATION_MESSAGE);
        });
        pan.add(but);
        //---------setVisible---------------------------------------------------
        win.setVisible(true);
    }
}
