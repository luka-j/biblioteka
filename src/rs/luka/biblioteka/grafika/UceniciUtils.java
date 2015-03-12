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
import rs.luka.biblioteka.exceptions.LosFormat;
import rs.luka.biblioteka.exceptions.PreviseKnjiga;
import rs.luka.biblioteka.exceptions.VrednostNePostoji;
import rs.luka.biblioteka.funkcije.Init;

                    
/**
 * Sadrzi pomocne metode za Ucenici
 * 
 * @author luka
 */
public class UceniciUtils {
    private static final java.util.logging.Logger LOGGER
            = java.util.logging.Logger.getLogger(UceniciUtils.class.getName());
    
    private final Dimension DODAJUC_SIZE = new Dimension(Init.dData.DODAJUCENIKA_WIDTH, Init.dData.DODAJUCENIKA_HEIGHT);
    private final Rectangle IME_BOUNDS = new Rectangle(Init.dData.DODAJUCENIKA_TEXT_X, Init.dData.DODAJUCENIKA_IME_Y,
                                                       Init.dData.DODAJUCENIKA_LABEL_WIDTH, Init.dData.DODAJUCENIKA_LABEL_HEIGHT);
    private final Rectangle IMETF_BOUNDS = new Rectangle(Init.dData.DODAJUCENIKA_TEXT_X, 
            Init.dData.DODAJUCENIKA_IMETF_Y, Init.dData.DODAJUCENIKA_TEXTFIELD_WIDTH, Init.dData.DODAJUCENIKA_TEXTFIELD_HEIGHT);
    private final Rectangle RAZRED_BOUNDS = new Rectangle(Init.dData.DODAJUCENIKA_TEXT_X, Init.dData.DODAJUCENIKA_RAZRED_Y, 
            Init.dData.DODAJUCENIKA_LABEL_WIDTH, Init.dData.DODAJUCENIKA_LABEL_HEIGHT);
    private final Rectangle RAZREDTF_BOUNDS = new Rectangle(Init.dData.DODAJUCENIKA_TEXT_X, Init.dData.DODAJUCENIKA_RAZREDTF_Y, 
            Init.dData.DODAJUCENIKA_TEXTFIELD_WIDTH, Init.dData.DODAJUCENIKA_TEXTFIELD_HEIGHT);
    private final Rectangle UNESI_BOUNDS = new Rectangle(Init.dData.DODAJUCENIKA_DODAJ_X, Init.dData.DODAJUCENIKA_DODAJ_Y, 
            Init.dData.DODAJUCENIKA_DODAJ_WIDTH, Init.dData.DODAJUCENIKA_DODAJ_HEIGHT);
    
    
    private final Dimension DODAJGEN_SIZE = new Dimension(Init.dData.DODAJGENERACIJU_WIDTH, Init.dData.DODAJGENERACIJU_HEIGHT);
    private final Rectangle IMENA_BOUNDS = new Rectangle(Init.dData.DODAJGENERACIJU_TEXT_X, Init.dData.DODAJGENERACIJU_IMENA_Y, 
            Init.dData.DODAJGENERACIJU_IMENA_WIDTH, Init.dData.DODAJGENERACIJU_IMENA_HEIGHT);
    private final Rectangle SCROLL_BOUNDS = new Rectangle(Init.dData.DODAJGENERACIJU_TEXT_X, Init.dData.DODAJGENERACIJU_SCROLL_Y,
            Init.dData.DODAJGENERACIJU_SCROLL_WIDTH, Init.dData.DODAJGENERACIJU_SCROLL_HEIGHT);
    private final Rectangle UNESIGEN_BOUNDS = new Rectangle(Init.dData.DODAJGENERACIJU_UNESI_X, Init.dData.DODAJGENERACIJU_UNESI_Y,
            Init.dData.DODAJGENERACIJU_UNESI_WIDTH, Init.dData.DODAJGENERACIJU_UNESI_HEIGHT);
    
    /**
     * Prozor za dodavanje novog ucenika.
     *
     * @since 1.7.'13.
     */
    public void dodajNovogUcenika() {
        //---------JFrame&JPanel------------------------------------------------
        JDialog win = new JDialog();
        win.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        win.setTitle(Init.dData.DODAJUCENIKA_TITLE_STRING);
        win.setSize(DODAJUC_SIZE);
        win.setLocationRelativeTo(null);
        win.setResizable(false);
        win.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        JPanel pan = new JPanel(null);
        pan.setBackground(Grafika.getBgColor());
        win.setContentPane(pan);
        //---------JLabel&JTextField--------------------------------------------
        JLabel ime = new JLabel(Init.dData.DODAJUCENIKA_IME_STRING);
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
        JLabel razred = new JLabel(Init.dData.DODAJUCENIKA_RAZRED_STRING);
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
        JButton unesi = new JButton(Init.dData.DODAJUCENIKA_UNESI_STRING);
        unesi.setFont(Grafika.getButtonFont());
        unesi.setBounds(UNESI_BOUNDS);
        unesi.addActionListener((ActionEvent e) -> {
            try {
                Podaci.dodajUcenika(ucTF.getText(), parseUnsignedInt(razTF.getText()));
                showMessageDialog(null, Init.dData.DODAJUCENIKA_SUCC_MSG_STRING, Init.dData.DODAJUCENIKA_SUCC_TITLE_STRING, 
                        JOptionPane.INFORMATION_MESSAGE);
                win.dispose();
                new Ucenici().pregledUcenika();
            } catch (NumberFormatException ex) {
                LOGGER.log(Level.INFO, "Razred novog učenika ({0}) je prevelik "
                        + "ili nije broj", razTF.getText());
                showMessageDialog(null, Init.dData.DODAJUCENIKA_NFEX_MSG_STRING, Init.dData.DODAJUCENIKA_NFEX_TITLE_STRING, 
                        JOptionPane.ERROR_MESSAGE);
            } catch (Duplikat ex) {
                LOGGER.log(Level.INFO, "Već postoji učenik sa istim imenom i prezimenom i razredom {0} i {1}\n"
                        + "Novi učenik nije dodat.", new Object[]{ucTF.getText(), razTF.getText()});
                showMessageDialog(null, Init.dData.DODAJUCENIKA_DEX_MSG_STRING, Init.dData.DODAJUCENIKA_DEX_TITLE_STRING, 
                        JOptionPane.ERROR_MESSAGE);
            } catch (LosFormat ex) {
                LOGGER.log(Level.INFO, "{0} nije validno ime (sadrži nedozvoljene karatere)", ucTF.getText());
                showMessageDialog(null, Init.dData.DODAJUCENIKA_LFEX_MSG_STRING, 
                        Init.dData.DODAJUCENIKA_LFEX_TITLE_STRING, JOptionPane.ERROR_MESSAGE);
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
        String ucenik = Dijalozi.showTextFieldDialog(Init.dData.OBRISIUCENIKA_TITLE_STRING, Init.dData.OBRISIUCENIKA_MSG_STRING, "");
        if (ucenik == null || ucenik.equals("null") || ucenik.isEmpty()) {
            return;
        }
        List<Integer> inx = indexOfUcenik(ucenik);
        if (inx.isEmpty()) {
            LOGGER.log(Level.INFO, "Učenik {0} nije pronađen", ucenik);
            showMessageDialog(null, Init.dData.OBRISIUCENIKA_EMPTY_MSG_STRING, Init.dData.OBRISIUCENIKA_EMPTY_TITLE_STRING,
                    JOptionPane.ERROR_MESSAGE);
        } else {
            int num = 0;
            if (inx.size() > 1) {
                Dijalozi.viseRazreda(inx);
            }
            try {
                ucenici.obrisiUcenika(inx.get(num));
                showMessageDialog(null, Init.dData.OBRISIUCENIKA_SUCC_MSG_STRING, Init.dData.OBRISIUCENIKA_SUCC_TITLE_STRING, 
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (VrednostNePostoji ex) {
                throw new RuntimeException("VrednostNePostoji prilikom obrisiUcenika za index "
                        + inx.get(num), ex);
            } catch (PreviseKnjiga ex) {
                LOGGER.log(Level.INFO, "Učenik {0} nije obrisan jer ima preostalih knjiga.", inx.get(num));
                showMessageDialog(null, Init.dData.OBRISIUCENIKA_PKEX_MSG_STRING, Init.dData.OBRISIUCENIKA_PKEX_TITLE_STRING, 
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
        win.setTitle(Init.dData.DODAJGENERACIJU_TITLE_STRING);
        win.setSize(DODAJGEN_SIZE);
        win.setLocationRelativeTo(null);
        win.setResizable(false);
        win.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        JPanel pan = new JPanel(null);
        pan.setBackground(Grafika.getBgColor());
        win.setContentPane(pan);
        //---------JLabel&JTextPane---------------------------------------------
        JLabel imena = new JLabel(Init.dData.DODAJGENERACIJU_IMENA_STRING);
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
        JButton but = new JButton(Init.dData.DODAJGENERACIJU_UNESI_STRING);
        but.setFont(Grafika.getButtonFont());
        but.setBounds(UNESIGEN_BOUNDS);
        but.addActionListener((ActionEvent e) -> {
            Podaci.dodajNovuGen(genTF.getText());
            showMessageDialog(null, Init.dData.DODAJGENERACIJU_SUCC_MSG_STRING,
                    Init.dData.DODAJGENERACIJU_SUCC_TITLE_STRING, JOptionPane.INFORMATION_MESSAGE);
        });
        pan.add(but);
        //---------setVisible---------------------------------------------------
        win.setVisible(true);
    }
}
