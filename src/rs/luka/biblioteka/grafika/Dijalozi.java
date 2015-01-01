package rs.luka.biblioteka.grafika;

import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
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
import rs.luka.biblioteka.funkcije.Utils;
import static rs.luka.biblioteka.grafika.Konstante.*;


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
        num = showOptionDialog(null, DIJALOZI_VISERAZREDA_MSG_STRING, DIJALOZI_VISERAZREDA_TITLE_STRING, 
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, razredi, razredi[0]);
        return num;
    }

    /**
     * Prikazuje Dijaloog za unosenje maksimalnog broja knjiga koje Ucenik sme da ima kod sebe.
     * Radi rekurziju dok god uneta vrednost nije int. Koristi #showTextFieldDialog
     * @see #showTextFieldDialog(java.lang.String, java.lang.String, java.lang.String) 
     * @return int vrednost unete vrednosti.
     */
    public static int brojKnjiga() {
        String brKnjigaStr = showTextFieldDialog(DIJALOZI_BROJKNJIGA_TITLE_STRING, 
                DIJALOZI_BROJKNJIGA_MSG_STRING, "");
        try {
            return Integer.parseInt(brKnjigaStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, DIJALOZI_BROJKNJIGA_NFEX_MSG_STRING,
                    DIJALOZI_BROJKNJIGA_NFEX_TITLE_STRING, JOptionPane.ERROR_MESSAGE);
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
    
    /**
     * JDialog koriscen za prikazivanje info poruka
     * @see #drawInfoWindow(java.lang.String, java.lang.String) 
     * @see #disposeInfoWindow() 
     */
    private static JDialog infoWindow;
    private static final Dimension INFOWINDOW_BOUNDS = 
            new Dimension(DIJALOZI_INFOWINDOW_WIDTH, DIJALOZI_INFOWINDOW_HEGHT);
    /**
     * Iscrtava info poruku sa datim naslovom i porukom koja se koristi kao test u JLabel-u.
     * Koristi default vrednosti umesto onih u klasi Grafika za boje.
     * @param naslov naslov prozora
     * @param poruka text JLabel-a
     */
    public static void drawInfoWindow(String naslov, String poruka) {
        infoWindow = new JDialog();
        infoWindow.setTitle(naslov);
        infoWindow.setSize(INFOWINDOW_BOUNDS);
        infoWindow.setLocationRelativeTo(null);
        infoWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        infoWindow.setAlwaysOnTop(true);
        JPanel infoPan = new JPanel();
        infoPan.setBackground(Color.WHITE);
        infoWindow.setContentPane(infoPan);
        JLabel infoLab = new JLabel(poruka);
        infoPan.add(infoLab);
        infoWindow.setVisible(true);
    }
    /**
     * Zatvara infoWindow dobijen pozivanjem {@link #drawInfoWindow(java.lang.String, java.lang.String)}
     * Ako prozor ne postoji, ignorise poziv.
     */
    public static void disposeInfoWindow() {
        if(infoWindow != null) {
            infoWindow.dispose();
        }
    }

    private Dijalozi() {
    }
}
