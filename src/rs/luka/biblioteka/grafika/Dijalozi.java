package rs.luka.biblioteka.grafika;

import java.awt.Dialog;
import java.awt.event.ActionEvent;
import static java.lang.String.valueOf;
import java.util.List;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.showOptionDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import static rs.luka.biblioteka.data.Podaci.getUcenik;

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
        //---------JFrame&JPanel------------------------------------------------
        final JDialog win = new JDialog();
        win.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        win.setTitle(naslov);
        win.setSize(350, 135);
        win.setLocationRelativeTo(null);
        win.setResizable(false);
        win.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        JPanel pan = new JPanel(null);
        pan.setBackground(Grafika.getBgColor());
        win.setContentPane(pan);
        //---------JLabel&JTextField--------------------------------------------
        labelText = labelText.replace("\n", "<br>");
        JLabel label = new JLabel("<html>" +labelText);
        label.setBounds(10, 10, 330, 60);
        label.setBackground(Grafika.getBgColor());
        label.setForeground(Grafika.getFgColor());
        label.setFont(Grafika.getLabelFont());
        pan.add(label);
        final JTextField textField = new JTextField();
        textField.setBounds(10, 70, 300, 25);
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
        win.setVisible(true); //modalna operacija
        return dialogReturnValue; //raspored naredbi jako bitan zbog modalnosti:
        //http://stackoverflow.com/a/4089370/2363015
    }
}
