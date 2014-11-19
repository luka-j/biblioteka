package rs.luka.biblioteka.grafika;

import java.awt.Dialog;
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

                    
/**
 *
 * @author luka
 */
public class UceniciUtils {
    
    private static final java.util.logging.Logger LOGGER
            = java.util.logging.Logger.getLogger(UceniciUtils.class.getName());
    
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
        win.setSize(400, 250);
        win.setLocationRelativeTo(null);
        win.setResizable(false);
        win.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        JPanel pan = new JPanel(null);
        pan.setBackground(Grafika.getBgColor());
        win.setContentPane(pan);
        //---------JLabel&JTextField--------------------------------------------
        JLabel ime = new JLabel("Unesite ime ucenika:");
        ime.setBounds(20, 20, 250, 37);
        ime.setBackground(Grafika.getBgColor());
        ime.setForeground(Grafika.getFgColor());
        ime.setFont(Grafika.getLabelFont());
        pan.add(ime);
        final JTextField ucTF = new JTextField();
        ucTF.setBounds(20, 55, 250, 25);
        ucTF.setFont(Grafika.getLabelFont());
        ucTF.setBackground(Grafika.getTFColor());
        ucTF.setForeground(Grafika.getFgColor());
        ucTF.setCaretColor(Grafika.getFgColor());
        pan.add(ucTF);
        JLabel razred = new JLabel("Unesite razred koji ucenik trenutno pohadja:");
        razred.setBounds(20, 95, 300, 37);
        razred.setBackground(Grafika.getBgColor());
        razred.setFont(Grafika.getLabelFont());
        razred.setForeground(Grafika.getFgColor());
        pan.add(razred);
        final JTextField razTF = new JTextField();
        razTF.setBounds(20, 125, 250, 25);
        razTF.setFont(Grafika.getLabelFont());
        razTF.setForeground(Grafika.getFgColor());
        razTF.setCaretColor(Grafika.getFgColor());
        razTF.setBackground(Grafika.getTFColor());
        pan.add(razTF);
        //---------JButton------------------------------------------------------
        JButton but = new JButton("Unesi podatke");
        but.setBounds(130, 170, 140, 40);
        but.addActionListener((ActionEvent e) -> {
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
        pan.add(but);
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
        win.setSize(610, 550);
        win.setLocationRelativeTo(null);
        win.setResizable(false);
        win.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        JPanel pan = new JPanel(null);
        pan.setBackground(Grafika.getBgColor());
        win.setContentPane(pan);
        //---------JLabel&JTextPane---------------------------------------------
        JLabel lab = new JLabel("<html>Unesite ucenike nove generacije, odvojene zapetama <br />"
                + "<strong>Nakon dodavanja nove generacije, svi ucenici "
                + "najstarije generacije ce biti obrisani!!!</strong></html>");
        lab.setBounds(10, 5, 600, 50);
        lab.setBackground(Grafika.getBgColor());
        lab.setForeground(Grafika.getFgColor());
        lab.setFont(Grafika.getLabelFont());
        pan.add(lab);
        final JTextPane genTF = new JTextPane();
        final JScrollPane jsp = new JScrollPane(genTF);
        jsp.setBounds(10, 60, 580, 400);
        genTF.setFont(Grafika.getLabelFont());
        genTF.setForeground(Grafika.getFgColor());
        genTF.setCaretColor(Grafika.getFgColor());
        genTF.setBackground(Grafika.getTFColor());
        pan.add(jsp);
        //----------JButton-----------------------------------------------------
        JButton but = new JButton("Unesi novu generaciju");
        but.setBounds(215, 470, 170, 40);
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
