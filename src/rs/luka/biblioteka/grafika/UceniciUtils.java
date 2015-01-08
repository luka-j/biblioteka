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
import rs.luka.biblioteka.data.Podaci;
import static rs.luka.biblioteka.data.Podaci.indexOfUcenik;
import rs.luka.biblioteka.exceptions.Duplikat;
import rs.luka.biblioteka.exceptions.PreviseKnjiga;
import rs.luka.biblioteka.exceptions.VrednostNePostoji;
import static rs.luka.biblioteka.grafika.Konstante.*;

                    
/**
 * Sadrzi pomocne metode za Ucenici
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
    
    
    private final Dimension DODAJGEN_SIZE = new Dimension(DODAJGENERACIJU_WIDTH, DODAJGENERACIJU_HEIGHT);
    private final Rectangle IMENA_BOUNDS = new Rectangle(DODAJGENERACIJU_TEXT_X, DODAJGENERACIJU_IMENA_Y, 
            DODAJGENERACIJU_IMENA_WIDTH, DODAJGENERACIJU_IMENA_HEIGHT);
    private final Rectangle SCROLL_BOUNDS = new Rectangle(DODAJGENERACIJU_TEXT_X, DODAJGENERACIJU_SCROLL_Y,
            DODAJGENERACIJU_SCROLL_WIDTH, DODAJGENERACIJU_SCROLL_HEIGHT);
    private final Rectangle UNESIGEN_BOUNDS = new Rectangle(DODAJGENERACIJU_UNESI_X, DODAJGENERACIJU_UNESI_Y,
            DODAJGENERACIJU_UNESI_WIDTH, DODAJGENERACIJU_UNESI_HEIGHT);
    
    /**
     * Prozor za dodavanje novog ucenika.
     *
     * @since 1.7.'13.
     */
    public void dodajNovogUcenika() {
        //---------JFrame&JPanel------------------------------------------------
        JDialog win = new JDialog();
        win.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        win.setTitle(DODAJUCENIKA_TITLE_STRING);
        win.setSize(DODAJUC_SIZE);
        win.setLocationRelativeTo(null);
        win.setResizable(false);
        win.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        JPanel pan = new JPanel(null);
        pan.setBackground(Grafika.getBgColor());
        win.setContentPane(pan);
        //---------JLabel&JTextField--------------------------------------------
        JLabel ime = new JLabel(DODAJUCENIKA_IME_STRING);
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
        JLabel razred = new JLabel(DODAJUCENIKA_RAZRED_STRING);
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
        JButton unesi = new JButton(DODAJUCENIKA_UNESI_STRING);
        unesi.setFont(Grafika.getButtonFont());
        unesi.setBounds(UNESI_BOUNDS);
        unesi.addActionListener((ActionEvent e) -> {
            try {
                Podaci.dodajUcenika(ucTF.getText(), parseUnsignedInt(razTF.getText()));
                showMessageDialog(null, DODAJUCENIKA_SUCC_MSG_STRING, DODAJUCENIKA_SUCC_TITLE_STRING, 
                        JOptionPane.INFORMATION_MESSAGE);
                win.dispose();
                new Ucenici().pregledUcenika();
            } catch (NumberFormatException ex) {
                LOGGER.log(Level.INFO, "Razred novog učenika ({0}) je prevelik "
                        + "ili nije broj", razTF.getText());
                showMessageDialog(null, DODAJUCENIKA_NFEX_MSG_STRING, DODAJUCENIKA_NFEX_TITLE_STRING, 
                        JOptionPane.ERROR_MESSAGE);
            } catch (Duplikat ex) {
                LOGGER.log(Level.INFO, "Već postoji učenik sa istim imenom i prezimenom i razredom {0} i {1}\n"
                        + "Novi učenik nije dodat.", new Object[]{ucTF.getText(), razTF.getText()});
                showMessageDialog(null, DODAJUCENIKA_DEX_MSG_STRING, DODAJUCENIKA_DEX_TITLE_STRING, 
                        JOptionPane.ERROR_MESSAGE);
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
        final rs.luka.legacy.biblioteka.Ucenici ucenici = new rs.luka.legacy.biblioteka.Ucenici();
        //---------JFrame&JPanel------------------------------------------------
        String ucenik = Dijalozi.showTextFieldDialog(OBRISIUCENIKA_TITLE_STRING, OBRISIUCENIKA_MSG_STRING, "");
        if (ucenik == null || ucenik.equals("null") || ucenik.isEmpty()) {
            return;
        }
        List<Integer> inx = indexOfUcenik(ucenik);
        if (inx.isEmpty()) {
            LOGGER.log(Level.INFO, "Učenik {0} nije pronađen", ucenik);
            showMessageDialog(null, OBRISIUCENIKA_EMPTY_MSG_STRING, OBRISIUCENIKA_EMPTY_TITLE_STRING,
                    JOptionPane.ERROR_MESSAGE);
        } else {
            int num = 0;
            if (inx.size() > 1) {
                Dijalozi.viseRazreda(inx);
            }
            try {
                ucenici.obrisiUcenika(inx.get(num));
                showMessageDialog(null, OBRISIUCENIKA_SUCC_MSG_STRING, OBRISIUCENIKA_SUCC_TITLE_STRING, 
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (VrednostNePostoji ex) {
                throw new RuntimeException("VrednostNePostoji prilikom obrisiUcenika za index "
                        + inx.get(num), ex);
            } catch (PreviseKnjiga ex) {
                LOGGER.log(Level.INFO, "Učenik {0} nije obrisan jer ima preostalih knjiga.", inx.get(num));
                showMessageDialog(null, OBRISIUCENIKA_PKEX_MSG_STRING, OBRISIUCENIKA_PKEX_TITLE_STRING, 
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Prozor za dodavanje nove generacije.
     *
     * @since 5.7.'13.
     */
    public void dodajNovuGeneraciju() {
        //---------JFrame&JPanel------------------------------------------------
        JDialog win = new JDialog();
        win.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        win.setTitle(DODAJGENERACIJU_TITLE_STRING);
        win.setSize(DODAJGEN_SIZE);
        win.setLocationRelativeTo(null);
        win.setResizable(false);
        win.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        JPanel pan = new JPanel(null);
        pan.setBackground(Grafika.getBgColor());
        win.setContentPane(pan);
        //---------JLabel&JTextPane---------------------------------------------
        JLabel imena = new JLabel(DODAJGENERACIJU_IMENA_STRING);
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
        JButton but = new JButton(DODAJGENERACIJU_UNESI_STRING);
        but.setFont(Grafika.getButtonFont());
        but.setBounds(UNESIGEN_BOUNDS);
        but.addActionListener((ActionEvent e) -> {
            Podaci.dodajNovuGen(genTF.getText());
            showMessageDialog(null, DODAJGENERACIJU_SUCC_MSG_STRING,
                    DODAJGENERACIJU_SUCC_TITLE_STRING, JOptionPane.INFORMATION_MESSAGE);
        });
        pan.add(but);
        //---------setVisible---------------------------------------------------
        win.setVisible(true);
    }
}
