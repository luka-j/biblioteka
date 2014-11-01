package rs.luka.biblioteka.grafika;

import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.showMessageDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;
import rs.luka.biblioteka.data.Podaci;
import static rs.luka.biblioteka.data.Podaci.getUcenik;
import rs.luka.biblioteka.exceptions.Duplikat;
import rs.luka.biblioteka.exceptions.NemaViseKnjiga;
import rs.luka.biblioteka.exceptions.PreviseKnjiga;
import rs.luka.biblioteka.exceptions.VrednostNePostoji;

/**
 * @author Luka
 */
public class Uzimanje {
    
        private static final java.util.logging.Logger LOGGER =
                java.util.logging.Logger.getLogger(Ucenici.class.getName());

    /**
     * Iscrtava prozor za iznajmljivanje knjiga od biblioteke.
     *
     * @param indexUcenika index ucenika koji uzima knjigu
     */
    public void Uzimanje(int indexUcenika) {
        //----------JFrame&JPanel------------------------------ 
        final JDialog winU = new JDialog();
        winU.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        winU.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        winU.setSize(350, 150);
        winU.setLocationRelativeTo(null);
        winU.setResizable(false);
        JPanel panU = new JPanel(null);
        panU.setBackground(Grafika.getBgColor());
        winU.setContentPane(panU);
        //------------JLabel-----------------------------------
        JLabel knjLab = new JLabel("Unesite naslov knjige koju ucenik uzima:");
        knjLab.setFont(Grafika.getLabelFont());
        knjLab.setForeground(Grafika.getFgColor());
        knjLab.setBounds(20, 10, 300, 25);
        panU.add(knjLab);
        //------------JTextField-------------------------------
        final JTextField knjTF = new JTextField();
        knjTF.setBounds(20, 45, 250, 25);
        knjTF.setFont(Grafika.getLabelFont());
        knjTF.setForeground(Grafika.getFgColor());
        knjTF.setCaretColor(Grafika.getFgColor());
            knjTF.setBackground(Grafika.getTFColor());
        panU.add(knjTF);
        //------------JButton&ActionListener----------------------------------
        JButton in = new JButton("Iznajmi knjigu");
        ActionListener unesi = (ActionEvent e) -> {
            try {
                Podaci.uzmiKnjigu(indexUcenika, Podaci.getKnjiga(knjTF.getText())); //šaljem objekat knjiga, ne naslov
                showMessageDialog(null, "Sve je u redu! "
                        + "Knjiga dodata kod ucenika, i oduzeta od biblioteke", "Uspeh!",
                        JOptionPane.INFORMATION_MESSAGE);
                winU.dispose();
            } catch (VrednostNePostoji ex) {
                switch (ex.getMessage()) {
                    case "Knjiga":
                        LOGGER.log(Level.INFO, "Uneta knjiga {0} ne postoji", knjTF.getText());
                        showMessageDialog(null, "Naslov koji ste uneli ne postoji \n"
                                + "Proverite da li ste ispravno upisali naziv i pokušajte ponovo.",
                                "Nepostojeća knjiga", JOptionPane.ERROR_MESSAGE);
                        break;
                    default: 
                        LOGGER.log(Level.SEVERE, "Nepostojeća VrednostNePostoji "
                                + "u uzimanju: {0}", ex.getMessage());
                        throw new RuntimeException("Invalid VrednostNePostoji vrednost");
                        //menjao ovaj deo. ne bi trebalo da dođe do default, ako dođe, bolje da znam
                }
            } catch (NemaViseKnjiga ex) {
                LOGGER.log(Level.INFO, "Više nema knjiga naslova {0}", knjTF.getText());
                showMessageDialog(null, "Više nema knjiga tog naslova",
                        "Nema knjiga", JOptionPane.ERROR_MESSAGE);
            } catch (Duplikat ex) {
                LOGGER.log(Level.INFO, "Učenik {0} je već iznajmio knjigu {1}",
                                new Object[]{getUcenik(indexUcenika).getIme(), knjTF.getText()});
                showMessageDialog(null, "Učenik je vec iznajmio knjigu tog naslova.",
                        "Duplikat", JOptionPane.ERROR_MESSAGE);
            } catch (PreviseKnjiga ex) {
                LOGGER.log(Level.INFO, "Učenik {0} ima previše knjiga kod sebe",
                        ex.getMessage());
                showMessageDialog(null, "Učenik trenutno ima previše knjiga kod sebe :\n",
                        "Previše knjiga", JOptionPane.ERROR_MESSAGE);
            }
        };
        in.addActionListener(unesi);
        in.setBounds(95, 80, 140, 35);
        panU.add(in);
        knjTF.addActionListener(unesi);
        //------------setVisible--------------------------------
        winU.setVisible(true);
    }
}
