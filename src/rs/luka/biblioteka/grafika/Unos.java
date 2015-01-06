package rs.luka.biblioteka.grafika;

import java.awt.Dimension;
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
import rs.luka.biblioteka.exceptions.Duplikat;
import rs.luka.biblioteka.exceptions.Prazno;
import rs.luka.biblioteka.exceptions.PreviseKnjiga;
import rs.luka.biblioteka.exceptions.VrednostNePostoji;
import static rs.luka.biblioteka.grafika.Konstante.*;

/**
 * Prozor za unos, prikazuje se samo pri prvom pokretanju.
 *
 * @author Luka
 */
public class Unos {

    private static final java.util.logging.Logger LOGGER
            = java.util.logging.Logger.getLogger(Ucenici.class.getName());

    /**
     * Glavni prozor za unos.
     */
    private JFrame win;
    private final Dimension UNOS_SIZE = new Dimension(UNOS_WIDTH, UNOS_HEIGHT);
    private final Dimension BUTTON_SIZE = new Dimension(UNOS_BUTTON_WIDTH, UNOS_BUTTON_HEIGHT);

    //
    //--------------------------------------------------------------------------
    //
    private final Dimension UNOSKNJ_SIZE = new Dimension(UNOSKNJ_WIDTH, UNOSKNJ_HEIGHT);
    private final Rectangle NASLOV_BOUNDS = new Rectangle(UNOSKNJ_TEXT_X, UNOSKNJ_NASLOV_Y,
            UNOSKNJ_LABEL_WIDTH, 2 * UNOSKNJ_LABEL_HEIGHT);
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
            UNOSUC_LABEL_WIDTH, 2 * UNOSUC_LABEL_HEIGHT);
    private final Rectangle KNJIGETF_BOUNDS = new Rectangle(UNOSUC_TEXT_X, UNOSUC_KNJIGETF_Y,
            UNOSUC_TEXTFIELD_WIDTH, UNOSUC_TEXTFIELD_HEIGHT);
    private final Rectangle UC_UNESI_BOUNDS = new Rectangle(UNOSUC_UNESI_X, UNOSUC_UNESI_Y,
            UNOSUC_UNESI_WIDTH, UNOSUC_UNESI_HEIGHT);

    /**
     * Iscrtava prozor za unos knjiga.
     * @param cont ako je true, pri zatvaranju zove {@link #unesiUcenike()}
     */
    public void unesiKnjige(boolean cont) {
        rs.luka.biblioteka.funkcije.Unos.initUnos();
        //---------JFrame&JPanel------------------------------------------------
        final JFrame winKnj = new JFrame(UNOS_KNJIGE_STRING);
        winKnj.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        winKnj.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                rs.luka.biblioteka.funkcije.Unos.finalizeUnos();
                winKnj.dispose();
                if(cont)
                    unesiUcenike();
            }
        });
        winKnj.setSize(UNOSKNJ_SIZE);
        winKnj.setLocationRelativeTo(null);
        winKnj.setResizable(false);
        JPanel pan = new JPanel(null);
        pan.setBackground(Grafika.getBgColor());
        winKnj.setContentPane(pan);
        //----------JLabels&JTextBoxes------------------------------------------
        JLabel naslov = new JLabel(UNOSKNJ_NASLOV_STRING);
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
        JLabel pisac = new JLabel(UNOSKNJ_PISAC_STRING);
        pisac.setBounds(PISAC_BOUNDS);
        pisac.setFont(Grafika.getLabelFont());
        pisac.setForeground(Grafika.getFgColor());
        JTextField pisacText = new JTextField();
        pan.add(pisac);
        pisacText.setBounds(PISACTF_BOUNDS);
        pisac.setFont(Grafika.getLabelFont());
        pisacText.setForeground(Grafika.getFgColor());
        pisacText.setCaretColor(Grafika.getFgColor());
        pisacText.setBackground(Grafika.getTFColor());
        pisacText.setFont(Grafika.getLabelFont());
        pan.add(pisacText);
        JLabel kolicina = new JLabel(UNOSKNJ_KOLICINA_STRING);
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
        JButton but = new JButton(UNOSKNJ_UNESI_STRING);
        but.setFont(Grafika.getButtonFont());
        but.setBounds(KNJ_UNESI_BOUNDS);
        ActionListener ubaci = (ActionEvent ae) -> {
            if ("".equals(nasText.getText())) {
                rs.luka.biblioteka.funkcije.Unos.finalizeUnos();
                winKnj.dispose();
            } else if ("".equals(kolText.getText())) {
                LOGGER.log(Level.INFO, "Polje za količinu pri unosu je prazno");
                showMessageDialog(null, UNOSKNJ_PRAZNO_MSG_STRING, UNOSKNJ_PRAZNO_TITLE_STRING,
                        JOptionPane.ERROR_MESSAGE);
            } else {
                try {
                    int kol = parseInt(kolText.getText());
                    rs.luka.biblioteka.funkcije.Unos.UnosKnj(nasText.getText(), kol, pisacText.getText());
                    nasText.setText("");
                    pisacText.setText("");
                    kolText.setText("");
                    nasText.grabFocus();
                    winKnj.repaint();
                } catch (Prazno ex) {
                    winKnj.dispose();
                } catch (NumberFormatException ex) {
                    LOGGER.log(Level.INFO, "Uneta količina {0} nije broj", kolText.getText());
                    showMessageDialog(null, UNOSKNJ_NFEX_MSG_STRING, UNOSKNJ_NFEX_TITLE_STRING,
                            JOptionPane.ERROR_MESSAGE);
                } catch (Duplikat ex) {
                    LOGGER.log(Level.WARNING, "Naslov {0} već postoji", nasText.getText());
                    showMessageDialog(null, UNOSKNJ_DEX_MSG_STRING, UNOSKNJ_DEX_TITLE_STRING,
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        but.addActionListener(ubaci);
        nasText.addActionListener(ubaci);
        kolText.addActionListener(ubaci);
        pan.add(but);
        //----------setVisible()------------------------------------------------
        winKnj.setVisible(true);
    }

    /**
     * Iscrtava prozor za unos ucenika.
     */
    public void unesiUcenike() {
        if (!Config.hasKey("brKnjiga")) {
            Config.set("brKnjiga", String.valueOf(Dijalozi.brojKnjiga()));
        }
        rs.luka.biblioteka.funkcije.Unos.initUnos();
        //---------JFrame&JPanel------------------------------------------------
        final JFrame winU = new JFrame(UNOS_UCENICI_STRING);
        winU.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        winU.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                rs.luka.biblioteka.funkcije.Unos.finalizeUnos();
                winU.dispose();
            }
        });
        winU.setSize(UNOSUC_SIZE);
        winU.setLocationRelativeTo(null);
        winU.setResizable(false);
        JPanel pan = new JPanel(null);
        pan.setBackground(Grafika.getBgColor());
        winU.setContentPane(pan);
        //----------JLabels&JTextBoxes------------------------------------------
        JLabel ime = new JLabel(UNOSUC_IME_STRING);
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
        JLabel raz = new JLabel(UNOSUC_RAZRED_STRING);
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
        JLabel knj = new JLabel(UNOSUC_KNJIGE_STRING);
        knj.setBounds(KNJIGE_BOUNDS);
        knj.setFont(Grafika.getLabelFont());
        knj.setForeground(Grafika.getFgColor());
        pan.add(knj);
        final JTextField knjText = new JTextField();
        knjText.setBounds(KNJIGETF_BOUNDS);
        knjText.setFont(Grafika.getLabelFont());
        knjText.setForeground(Grafika.getFgColor());
        knjText.setCaretColor(Grafika.getFgColor());
        knjText.setBackground(Grafika.getTFColor());
        //----------ActionListener----------------------------------------------
        ActionListener unesi = (ActionEvent ae) -> {
            if ("".equals(imeText.getText())) {
                rs.luka.biblioteka.funkcije.Unos.finalizeUnos();
                winU.dispose();
            } else {
                try {
                    String[] knjige = knjText.getText().split("\\s*,\\s*");
                    rs.luka.biblioteka.funkcije.Unos.UnosUc(imeText.getText(), knjige,
                            parseUnsignedInt(razText.getText()));
                    imeText.setText("");
                    knjText.setText("");
                    imeText.grabFocus();
                    winU.repaint();
                } catch (NumberFormatException ex) {
                    LOGGER.log(Level.INFO, "Loš razred: {0}", razText.getText());
                    showMessageDialog(null, UNOSUC_NFEX_MSG_STRING, UNOSUC_NFEX_TITLE_STRING,
                            JOptionPane.ERROR_MESSAGE);
                    razText.grabFocus();
                } catch (Prazno ex) {
                    winU.dispose();
                } catch (PreviseKnjiga ex) {
                    LOGGER.log(Level.WARNING, "Uneto previše knjiga: {0}", knjText.getText());
                    JOptionPane.showMessageDialog(null, UNOSUC_PKEX_MSG_STRING, UNOSUC_PKEX_TITLE_STRING,
                            JOptionPane.ERROR_MESSAGE);
                    knjText.grabFocus();
                } catch (Duplikat ex) {
                    LOGGER.log(Level.WARNING, "Učenik {0} već postoji", imeText.getText());
                    showMessageDialog(null, UNOSUC_DEX_MSG_STRING, UNOSUC_DEX_TITLE_STRING,
                            JOptionPane.ERROR_MESSAGE);
                    imeText.grabFocus();
                } catch (VrednostNePostoji ex) {
                    LOGGER.log(Level.WARNING, "Nije uneta jedna od knjiga {0}", knjText.getText());
                    showMessageDialog(null, UNOSUC_VNPEX_MSG_STRING, UNOSUC_VNPEX_TITLE_STRING,
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        knjText.addActionListener(unesi);
        pan.add(knjText);
        imeText.addActionListener(unesi);
        pan.add(imeText);
        razText.addActionListener(unesi);
        pan.add(razText);
        //----------JButton-----------------------------------------------------
        JButton but = new JButton(UNOSUC_UNESI_STRING);
        but.setBounds(UC_UNESI_BOUNDS);
        but.setFont(Grafika.getButtonFont());
        but.addActionListener(unesi);
        pan.add(but);
        //----------setVisible--------------------------------------------------
        winU.setVisible(true);
    }
}
