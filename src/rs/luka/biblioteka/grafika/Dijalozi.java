package rs.luka.biblioteka.grafika;

import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import static java.lang.String.valueOf;
import java.util.List;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.showOptionDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import static rs.luka.biblioteka.data.Podaci.getUcenik;
import rs.luka.biblioteka.funkcije.Init;
import rs.luka.biblioteka.funkcije.Utils;
import static rs.luka.biblioteka.grafika.Konstante.*;

/**
 *
 * @author luka
 * @since 17.05.'14.
 */
public class Dijalozi {
    

    /**
     * Prikazuje dijalog za odabir razreda ucenika.
     *
     * @param inx indeksi ucenika za koje se vrsi odabir
     * @return index liste inx koji je odabran.
     * @since 17.05.'14.
     */
    public static int viseRazreda(List<Integer> inx) {
        int num;
        String[] razredi = new String[inx.size()];
        for (int i = 0; i < inx.size(); i++) {
            razredi[i] = valueOf(getUcenik(inx.get(i)).getRazred());
        }
        num = showOptionDialog(null, "Postoji vise ucenika sa tim imenom.\n"
                + "Odaberite razred:", "Odaberite razred", JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, razredi, razredi[0]);
        return num;
    }

    public static int brojKnjiga() {
        /*String brKnjigaStr = JOptionPane.showInputDialog("Unesite maksimalan broj "
                + "knjiga koje ucenik\nmoze da ima kod sebe:");*/
        String brKnjigaStr = showTextFieldDialog("Broj knjiga", "Unesite maksimalan broj " +
                 "knjiga koje\nučenik može da ima kod sebe", "");
        try {
            return Integer.parseInt(brKnjigaStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Uneta količina nije broj.",
                    "Los format", JOptionPane.ERROR_MESSAGE);
            return brojKnjiga();
        }
    }
    
    private static String dialogReturnValue;
    /**
     * Iscrtava dijalog sa jednim labelom i jednim textboxom na koga je 
     * nakacen ActionListener i vraca text koji se nalazio u textboxu pri izlasku iz funkcije.
     * @param naslov naslov dijaloga koji se iscrtava
     * @param labelText sta pise na labelu
     * @param textFieldText inicijalna vrednost textfielda
     * @return vrednost koja se nalazi u textfieldu
     * @since 17.9.'14.
     */
    public static String showTextFieldDialog(String naslov, String labelText, String textFieldText) {
        int lines = Utils.countCharsInString(labelText, '\n') + 1;
        //---------JFrame&JPanel------------------------------------------------
        final JDialog win = new JDialog();
        win.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        win.setTitle(naslov);
        win.setSize(new Dimension(DIJALOZI_SIRINA, DIJALOZI_FIXED_VISINA + lines * DIJALOZI_LINE_HEIGHT));
        win.setLocationRelativeTo(null);
        win.setResizable(false);
        win.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        JPanel pan = new JPanel(null);
        pan.setBackground(Grafika.getBgColor());
        win.setContentPane(pan);
        //---------JLabel&JTextField--------------------------------------------
        labelText = labelText.replace("\n", "<br>");
        JLabel label = new JLabel("<html>" +labelText);
        label.setBounds(DIJALOZI_TEXT_X, DIJALOZI_LABEL_Y, DIJALOZI_LABEL_WIDTH, lines * DIJALOZI_LINE_HEIGHT);
        label.setBackground(Grafika.getBgColor());
        label.setForeground(Grafika.getFgColor());
        label.setFont(Grafika.getLabelFont());
        pan.add(label);
        final JTextField textField = new JTextField();
        textField.setBounds(DIJALOZI_TEXT_X, lines * DIJALOZI_LINE_HEIGHT + DIJALOZI_TEXTFIELD_FIXED_Y, 
                            DIJALOZI_TEXTFIELD_WIDTH, DIJALOZI_TEXTFIELD_HEIGHT);
        textField.setFont(Grafika.getLabelFont());
        textField.setForeground(Grafika.getFgColor());
        textField.setCaretColor(Grafika.getFgColor());
        textField.setBackground(Grafika.getTFColor());
        textField.setText(textFieldText);
        textField.addActionListener((ActionEvent e) -> {
             dialogReturnValue = textField.getText();
             win.dispose();
        });
        pan.add(textField);
        //----------setVisible&return-------------------------------------------
        win.setVisible(true);
        return dialogReturnValue; //raspored naredbi bitan zbog modalnosti:
        //http://stackoverflow.com/a/4089370/2363015
    }
    
    private static JDialog infoWindow;
    public static void drawInfoWindow(String naslov, String poruka) {
        infoWindow = new JDialog();
        infoWindow.setTitle(naslov);
        infoWindow.setSize(250, 80);
        infoWindow.setLocationRelativeTo(null);
        infoWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        infoWindow.setAlwaysOnTop(true);
        JPanel initPan = new JPanel();
        initPan.setBackground(Color.WHITE);
        infoWindow.setContentPane(initPan);
        JLabel initLab = new JLabel(poruka);
        initPan.add(initLab);
        infoWindow.setVisible(true);
    }
    public static void disposeInfoWindow() {
        if(infoWindow != null)
            infoWindow.dispose();
    }
}
