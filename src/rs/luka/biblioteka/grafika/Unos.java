package rs.luka.biblioteka.grafika;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Rectangle;
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
import static rs.luka.biblioteka.grafika.Konstante.*;

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
    private final Dimension UNOS_SIZE = new Dimension(UNOS_WIDTH, UNOS_HEIGHT);
    private final Dimension BUTTON_SIZE = new Dimension(UNOS_BUTTON_WIDTH, UNOS_BUTTON_HEIGHT);

    /**
     * Iscrtava glavni prozor za unos i 2 dugmeta za unos ucenika i knjiga.
     */
    public void UnosGrafika() {
        win = new JFrame("Unos");
        win.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        win.setSize(UNOS_SIZE);
        win.setLocationRelativeTo(null);
        win.setResizable(false);
        win.setAlwaysOnTop(true);
        JPanel pan = new JPanel(new FlowLayout(FlowLayout.LEADING, UNOS_HGAP, UNOS_VGAP));
        pan.setBackground(Grafika.getBgColor());
        win.setContentPane(pan);

        JButton knj = new JButton("Unos knjiga");
        knj.addActionListener((ActionEvent e1) -> {
            UnosKnjige();
        });
        knj.setPreferredSize(BUTTON_SIZE);
        pan.add(knj);

        JButton uc = new JButton("Unos učenika");
        uc.addActionListener((ActionEvent e2) -> {
            UnosUcenici();
        });
        uc.setPreferredSize(BUTTON_SIZE);
        pan.add(uc);
        win.setVisible(true);
    }

    //
    //--------------------------------------------------------------------------
    //
    
    private final Dimension UNOSKNJ_SIZE = new Dimension(UNOSKNJ_WIDTH, UNOSKNJ_HEIGHT);
    private final Rectangle NASLOV_BOUNDS = new Rectangle(UNOSKNJ_TEXT_X, UNOSKNJ_NASLOV_Y, 
            UNOSKNJ_LABEL_WIDTH, 2*UNOSKNJ_LABEL_HEIGHT);
    private final Rectangle NASLOVTF_BOUNDS = new Rectangle(UNOSKNJ_TEXT_X, UNOSKNJ_NASLOVTF_Y,
            UNOSKNJ_TEXTFIELD_WIDTH, UNOSKNJ_TEXTFIELD_HEIGHT);
    private final Rectangle PISAC_BOUNDS = new Rectangle(UNOSKNJ_TEXT_X, UNOSKNJ_PISAC_Y, 
            UNOSKNJ_LABEL_WIDTH, UNOSKNJ_LABEL_HEIGHT);
    private final Rectangle PISACTF_BOUNDS = new Rectangle(UNOSKNJ_TEXT_X, UNOSKNJ_PISACTF_Y,
            UNOSKNJ_TEXTFIELD_WIDTH, UNOSKNJ_TEXTFIELD_HEIGHT);
    private final Rectangle KOLICINA_BOUNDS = new Rectangle(UNOSKNJ_TEXT_X, UNOSKNJ_KOLICINA_Y, 
            UNOSKNJ_LABEL_WIDTH, UNOSKNJ_LABEL_HEIGHT);
    private final Rectangle KOLICINATF_BOUNDS = new Rectangle(UNOSKNJ_TEXT_X, UNOSKNJ_KOLICINATF_Y,
            UNOSKNJ_TEXTFIELD_WIDTH, UNOSKNJ_TEXTFIELD_HEIGHT);
    private final Rectangle KNJ_UNESI_BOUNDS = new Rectangle(UNOSKNJ_UNESI_X, UNOSKNJ_UNESI_Y, 
            UNOSKNJ_UNESI_WIDTH, UNOSKNJ_UNESI_HEIGHT);
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
        winKnj.setSize(UNOSKNJ_SIZE);
        winKnj.setLocationRelativeTo(null);
        winKnj.setResizable(false);
        winKnj.setAlwaysOnTop(true);
        JPanel pan = new JPanel(null);
        pan.setBackground(Grafika.getBgColor());
        winKnj.setContentPane(pan);
        //----------JLabels&JTextBoxes------------------------------------------
        JLabel naslov = new JLabel("<html>Unesite naslov knjige<br /> "
                + "Ostavite prazno ako nema više knjiga</html>");
        naslov.setBounds(NASLOV_BOUNDS);
        naslov.setFont(Grafika.getLabelFont());
        naslov.setForeground(Grafika.getFgColor());
        pan.add(naslov);
        final JTextField nasText = new JTextField();
        nasText.setBounds(NASLOVTF_BOUNDS);
        nasText.setFont(Grafika.getLabelFont());
        nasText.setForeground(Grafika.getFgColor());
        nasText.setCaretColor(Grafika.getFgColor());
            nasText.setBackground(Grafika.getTFColor());
        pan.add(nasText);
        JLabel pisac = new JLabel("Unesite pisca knjige:");
        pisac.setBounds(PISAC_BOUNDS);
        pisac.setFont(Grafika.getLabelFont());
        pisac.setForeground(Grafika.getFgColor());
        JTextField pisacText = new JTextField();
        pan.add(pisac);
        pisacText.setBounds(PISACTF_BOUNDS);
        pisacText.setForeground(Grafika.getFgColor());
        pisacText.setCaretColor(Grafika.getFgColor());
            pisacText.setBackground(Grafika.getTFColor());
        pan.add(pisacText);
        JLabel kolicina = new JLabel("Unesite količinu:");
        kolicina.setBounds(KOLICINA_BOUNDS);
        kolicina.setFont(Grafika.getLabelFont());
        kolicina.setForeground(Grafika.getFgColor());
        pan.add(kolicina);
        final JTextField kolText = new JTextField();
        kolText.setBounds(KOLICINATF_BOUNDS);
        kolText.setFont(Grafika.getLabelFont());
        kolText.setForeground(Grafika.getFgColor());
        kolText.setCaretColor(Grafika.getFgColor());
            kolText.setBackground(Grafika.getTFColor());
        pan.add(kolText);
        //----------JButton&ActionListener--------------------------------------
        JButton but = new JButton("Unesi podatke");
        but.setBounds(KNJ_UNESI_BOUNDS);
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

    private final Dimension UNOSUC_SIZE = new Dimension(UNOSUC_WIDTH, UNOSUC_HEIGHT);
    private final Rectangle IME_BOUNDS = new Rectangle(UNOSUC_TEXT_X, UNOSUC_IME_Y, 
            UNOSUC_LABEL_WIDTH, UNOSUC_LABEL_HEIGHT);
    private final Rectangle IMETF_BOUNDS = new Rectangle(UNOSUC_TEXT_X, UNOSUC_IMETF_Y, 
            UNOSUC_TEXTFIELD_WIDTH, UNOSUC_TEXTFIELD_HEIGHT);
    private final Rectangle RAZRED_BOUNDS = new Rectangle(UNOSUC_TEXT_X, UNOSUC_RAZRED_Y, 
            UNOSUC_LABEL_WIDTH, UNOSUC_LABEL_HEIGHT);
    private final Rectangle RAZREDTF_BOUNDS = new Rectangle(UNOSUC_TEXT_X, UNOSUC_RAZREDTF_Y, 
            UNOSUC_TEXTFIELD_WIDTH, UNOSUC_TEXTFIELD_HEIGHT);
    private final Rectangle KNJIGE_BOUNDS = new Rectangle(UNOSUC_TEXT_X, UNOSUC_KNJIGE_Y, 
            UNOSUC_LABEL_WIDTH, 2*UNOSUC_LABEL_HEIGHT);
    private final Rectangle KNJIGETF_BOUNDS = new Rectangle(UNOSUC_TEXT_X, UNOSUC_KNJIGETF_Y, 
            UNOSUC_TEXTFIELD_WIDTH, UNOSUC_TEXTFIELD_HEIGHT);
    private final Rectangle UC_UNESI_BOUNDS = new Rectangle(UNOSUC_UNESI_X, UNOSUC_UNESI_Y, 
            UNOSUC_UNESI_WIDTH, UNOSUC_UNESI_HEIGHT);
    
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
        winU.setSize(UNOSUC_SIZE);
        winU.setLocationRelativeTo(null);
        winU.setResizable(false);
        winU.setAlwaysOnTop(true);
        JPanel pan = new JPanel(null);
        pan.setBackground(Grafika.getBgColor());
        winU.setContentPane(pan);
        //----------JLabels&JTextBoxes------------------------------------------
        JLabel ime = new JLabel("Unesite ime učenika");
        ime.setBounds(IME_BOUNDS);
        ime.setFont(Grafika.getLabelFont());
        ime.setForeground(Grafika.getFgColor());
        pan.add(ime);
        final JTextField imeText = new JTextField();
        imeText.setBounds(IMETF_BOUNDS);
        imeText.setFont(Grafika.getLabelFont());
        imeText.setForeground(Grafika.getFgColor());
        imeText.setCaretColor(Grafika.getFgColor());
            imeText.setBackground(Grafika.getTFColor());
        pan.add(imeText);
        JLabel raz = new JLabel("Unesite razred u koji učenik ide(brojevima):");
        raz.setBounds(RAZRED_BOUNDS);
        raz.setFont(Grafika.getLabelFont());
        raz.setForeground(Grafika.getFgColor());
        pan.add(raz);
        final JTextField razText = new JTextField();
        razText.setBounds(RAZREDTF_BOUNDS);
        razText.setFont(Grafika.getLabelFont());
        razText.setForeground(Grafika.getFgColor());
        razText.setCaretColor(Grafika.getFgColor());
            razText.setBackground(Grafika.getTFColor());
        pan.add(razText);
        JLabel knj = new JLabel("<html>Unesite knjige koje se trenutno nalaze"
                + " kod učenika, razdvojene zapetom:</html>");
        knj.setBounds(KNJIGE_BOUNDS);
        knj.setFont(Grafika.getLabelFont());
        knj.setForeground(Grafika.getFgColor());
        pan.add(knj);
        final JTextField knjText = new JTextField();
        knjText.setBounds(KNJIGETF_BOUNDS);
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
        but.setBounds(UC_UNESI_BOUNDS);
        but.addActionListener(unesi);
        pan.add(but);
        //----------setVisible--------------------------------------------------
        winU.setVisible(true);
    }
}
