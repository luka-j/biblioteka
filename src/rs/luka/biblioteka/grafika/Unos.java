package rs.luka.biblioteka.grafika;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import static java.lang.Integer.parseInt;
import static java.lang.Integer.parseUnsignedInt;
import java.util.logging.Level;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.showMessageDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;
import rs.luka.biblioteka.data.Config;
import rs.luka.biblioteka.exceptions.Prazno;
import rs.luka.biblioteka.exceptions.PreviseKnjiga;

/**
 * Prozor za unos, prikazuje se samo pri prvom pokretanju.
 *
 * @author Luka
 */
public class Unos {
    
    private static final java.util.logging.Logger LOGGER = 
            java.util.logging.Logger.getLogger(Ucenici.class.getName());

    /**
     * Glavni prozor za unos.
     */
    private JFrame win;

    /**
     * Iscrtava glavni prozor za unos i 2 dugmeta za unos ucenika i knjiga.
     */
    public void UnosGrafika() {
        win = new JFrame("Unos");
        win.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        win.setSize(340, 120);
        win.setLocation(250, 120);
        win.setResizable(false);
        JPanel pan = new JPanel(null);
        pan.setBackground(Grafika.getBgColor());
        win.setContentPane(pan);

        JButton knj = new JButton("Unos knjiga");
        knj.addActionListener((ActionEvent e1) -> {
            UnosKnjige();
        });
        knj.setBounds(25, 20, 130, 40);
        pan.add(knj);

        JButton uc = new JButton("Unos učenika");
        uc.addActionListener((ActionEvent e2) -> {
            UnosUcenici();
        });
        uc.setBounds(180, 20, 130, 40);
        pan.add(uc);
        win.setVisible(true);
    }

    //
    //--------------------------------------------------------------------------
    //
    /**
     * Iscrtava prozor za unos knjiga.
     */
    public void UnosKnjige() {
        final rs.luka.biblioteka.funkcije.Unos unos = new rs.luka.biblioteka.funkcije.Unos();
        //---------JFrame&JPanel------------------------------------------------
        final JFrame winKnj = new JFrame("Unos Knjiga");
        winKnj.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        winKnj.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                unos.finalizeUnos();
                winKnj.dispose();
            }
        });
        winKnj.setSize(400, 350);
        winKnj.setLocationRelativeTo(null);
        winKnj.setResizable(false);
        JPanel pan = new JPanel(null);
        pan.setBackground(Grafika.getBgColor());
        winKnj.setContentPane(pan);
        //----------JLabels&JTextBoxes------------------------------------------
        JLabel naslov = new JLabel("<html>Unesite naslov knjige<br /> "
                + "Ostavite prazno ako nema više knjiga</html>");
        naslov.setBounds(20, 20, 300, 41);
        naslov.setFont(Grafika.getLabelFont());
        naslov.setForeground(Grafika.getFgColor());
        pan.add(naslov);
        final JTextField nasText = new JTextField();
        nasText.setBounds(20, 65, 300, 25);
        nasText.setFont(Grafika.getLabelFont());
        nasText.setForeground(Grafika.getFgColor());
        nasText.setCaretColor(Grafika.getFgColor());
            nasText.setBackground(Grafika.getTFColor());
        pan.add(nasText);
        JLabel pisac = new JLabel("Unesite pisca knjige:");
        pisac.setBounds(20, 105, 300, 30);
        pisac.setFont(Grafika.getLabelFont());
        pisac.setForeground(Grafika.getFgColor());
        JTextField pisacText = new JTextField();
        pan.add(pisac);
        pisacText.setBounds(20, 135, 300, 25);
        pisacText.setForeground(Grafika.getFgColor());
        pisacText.setCaretColor(Grafika.getFgColor());
            pisacText.setBackground(Grafika.getTFColor());
        pan.add(pisacText);
        JLabel kolicina = new JLabel("Unesite količinu:");
        kolicina.setBounds(20, 175, 300, 30);
        kolicina.setFont(Grafika.getLabelFont());
        kolicina.setForeground(Grafika.getFgColor());
        pan.add(kolicina);
        final JTextField kolText = new JTextField();
        kolText.setBounds(20, 205, 300, 25);
        kolText.setFont(Grafika.getLabelFont());
        kolText.setForeground(Grafika.getFgColor());
        kolText.setCaretColor(Grafika.getFgColor());
            kolText.setBackground(Grafika.getTFColor());
        pan.add(kolText);
        //----------JButton&ActionListener--------------------------------------
        JButton but = new JButton("Unesi podatke");
        but.setBounds(130, 255, 120, 40);
        ActionListener ubaci = (ActionEvent ae) -> {
            if ("".equals(nasText.getText())) {
                unos.finalizeUnos();
                winKnj.dispose();
            } else if ("".equals(kolText.getText())) {
                LOGGER.log(Level.INFO, "Polje za količinu pri unosu je prazno");
                showMessageDialog(null, "Polje za kolicinu je prazno.", 
                        "Prazno polje", JOptionPane.ERROR_MESSAGE);
            } else {
                try {
                    int kol = parseInt(kolText.getText());
                    int ret = unos.UnosKnj(nasText.getText(), kol, pisacText.getText());
                    if (ret == 0) {
                        nasText.setText("");
                        pisacText.setText("");
                        kolText.setText("");
                        nasText.grabFocus();
                        winKnj.repaint();
                    }
                } catch(Prazno ex) {
                    winKnj.dispose();
                } catch (NumberFormatException ex) {
                    LOGGER.log(Level.INFO, "Uneta količina {0} nije broj", kolText.getText());
                    showMessageDialog(null, "Uneta kolicina nije broj.",
                            "Loš unos", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        but.addActionListener(ubaci);
        kolText.addActionListener(ubaci);
        pan.add(but);
        //----------setVisible()------------------------------------------------
        winKnj.setVisible(true);
    }

    /**
     * Iscrtava prozor za unos ucenika.
     */
    public void UnosUcenici() {
        if (!Config.hasKey("brKnjiga")) {
            Config.set("brKnjiga", String.valueOf(Dijalozi.brojKnjiga()));
        }
        final rs.luka.biblioteka.funkcije.Unos unos = new rs.luka.biblioteka.funkcije.Unos();
        //---------JFrame&JPanel------------------------------------------------
        final JFrame winU = new JFrame("Unos učenika");
        winU.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        winU.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                unos.finalizeUnos();
                winU.dispose();
            }
        });
        winU.setSize(400, 360);
        winU.setLocationRelativeTo(null);
        winU.setResizable(false);
        JPanel pan = new JPanel(null);
        pan.setBackground(Grafika.getBgColor());
        winU.setContentPane(pan);
        //----------JLabels&JTextBoxes------------------------------------------
        JLabel ime = new JLabel("Unesite ime učenika");
        ime.setBounds(15, 10, 320, 40);
        ime.setFont(Grafika.getLabelFont());
        ime.setForeground(Grafika.getFgColor());
        pan.add(ime);
        final JTextField imeText = new JTextField();
        imeText.setBounds(15, 55, 320, 25);
        imeText.setFont(Grafika.getLabelFont());
        imeText.setForeground(Grafika.getFgColor());
        imeText.setCaretColor(Grafika.getFgColor());
            imeText.setBackground(Grafika.getTFColor());
        pan.add(imeText);
        JLabel raz = new JLabel("Unesite razred u koji učenik ide(brojevima):");
        raz.setBounds(15, 90, 360, 40);
        raz.setFont(Grafika.getLabelFont());
        raz.setForeground(Grafika.getFgColor());
        pan.add(raz);
        final JTextField razText = new JTextField();
        razText.setBounds(15, 130, 320, 25);
        razText.setFont(Grafika.getLabelFont());
        razText.setForeground(Grafika.getFgColor());
        razText.setCaretColor(Grafika.getFgColor());
            razText.setBackground(Grafika.getTFColor());
        pan.add(razText);
        JLabel knj = new JLabel("<html>Unesite knjige koje se trenutno nalaze "
                + "kod učenika, razdvojene zapetom:</html>");
        knj.setBounds(15, 165, 360, 70);
        knj.setFont(Grafika.getLabelFont());
        knj.setForeground(Grafika.getFgColor());
        pan.add(knj);
        final JTextField knjText = new JTextField();
        knjText.setBounds(15, 235, 320, 25);
        //----------ActionListener----------------------------------------------
        ActionListener unesi = (ActionEvent ae) -> {
            if ("".equals(imeText.getText())) {
                unos.finalizeUnos();
                winU.dispose();
            } else {
                try {
                    String[] knjige = knjText.getText().split("\\s*,\\s*");
                    unos.UnosUc(imeText.getText(), knjige,
                            parseUnsignedInt(razText.getText()));
                    imeText.setText("");
                    knjText.setText("");
                    imeText.grabFocus();
                    winU.repaint();
                } catch (NumberFormatException ex) {
                    LOGGER.log(Level.INFO, "Loš razred: {0}", razText.getText());
                    showMessageDialog(null, "Unet razred nije broj ili "
                            + "nije validan", "Loš razred", JOptionPane.ERROR_MESSAGE);
                } catch(Prazno ex) {
                    winU.dispose();
                } catch (PreviseKnjiga ex) {
                    LOGGER.log(Level.WARNING, "Uneto previše knjiga: {0}", knjText.getText());
                    JOptionPane.showMessageDialog(null, "Uneli ste više knjiga od "
                            + "limita koji ste postavili na početku\nUčenik nije unesen", "Previše knjiga",
                            JOptionPane.ERROR_MESSAGE);
                    knjText.grabFocus();
                }
            }
        };
        knjText.addActionListener(unesi);
        knjText.setFont(Grafika.getLabelFont());
        knjText.setForeground(Grafika.getFgColor());
        knjText.setCaretColor(Grafika.getFgColor());
            knjText.setBackground(Grafika.getTFColor());
        pan.add(knjText);
        //----------JButton-----------------------------------------------------
        JButton but = new JButton("Unesi podatke");
        but.setBounds(130, 280, 120, 37);
        but.addActionListener(unesi);
        pan.add(but);
        //----------setVisible--------------------------------------------------
        winU.setVisible(true);
    }
}
