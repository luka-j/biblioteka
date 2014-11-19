package rs.luka.biblioteka.grafika;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import static java.lang.Integer.parseInt;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import rs.luka.biblioteka.data.Config;
import rs.luka.biblioteka.data.Knjiga;
import rs.luka.biblioteka.data.Podaci;
import rs.luka.biblioteka.exceptions.Duplikat;
import rs.luka.biblioteka.exceptions.NemaViseKnjiga;
import rs.luka.biblioteka.exceptions.PreviseKnjiga;
import rs.luka.biblioteka.exceptions.VrednostNePostoji;

/**
 * @author Luka
 */
public class Knjige implements FocusListener {

    private static final java.util.logging.Logger LOGGER
            = java.util.logging.Logger.getLogger(Knjige.class.getName());

    private static final String SEARCH_TEXT = "Pretraži knjige";
    private static final Insets INSET = new Insets(5, 0, 5, 8);
    /**
     * searchBox za pretrazivanje knjiga.
     */
    private final JTextField searchBox = new JTextField(SEARCH_TEXT);

    private int sirina, visina;
    
    private final JButton[] uzmiBut;
    private final JLabel[] pisac;
    private final JLabel[] kolicina;
    private final JCheckBox[] knjige;
    private final JLabel kolicinaTitle;
    private final JLabel pisacTitle;
    private final JCheckBox selectAll;
    private final JSplitPane split;
    private final JPanel butPan;
    private final JScrollPane scroll;
    private final JPanel sidePan;
    private final JPanel kolPan;
    private final JPanel pisacPan;
    private final JPanel knjPan;
    private final JPanel mainPan;
    private final JFrame win;

    public Knjige() {
        uzmiBut = new JButton[Podaci.getBrojKnjiga()];
        pisac = new JLabel[Podaci.getBrojKnjiga()];
        kolicina = new JLabel[Podaci.getBrojKnjiga()];
        knjige = new JCheckBox[Podaci.getBrojKnjiga()];
        kolicinaTitle = new JLabel("Količina:");
        pisacTitle = new JLabel("Pisac:");
        selectAll = new JCheckBox("Naslovi:");
        win = new JFrame("Pregled knjiga");
        butPan = new JPanel();
        sidePan = new JPanel(null);
        kolPan = new JPanel(null);
        pisacPan = new JPanel(null);
        knjPan = new JPanel(null);
        mainPan = new JPanel(null);
        scroll = new JScrollPane(mainPan);
        split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, scroll, butPan);
        pregledKnjiga();
    }

    /**
     * Pregled knjiga koje su trenutno u biblioteci.
     */
    private void pregledKnjiga() {
        initPanels();
        initText();
        initButtons();
        initOtherListeners();
        initSearchBox();
        
        win.setVisible(true);
    }

    private void initPanels() {
        win.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        sirina = parseInt(Config.get("knjigeS", "550"));
        visina = parseInt(Config.get("knjigeV", "750"));
        win.setSize(sirina, visina);
        LOGGER.log(Level.CONFIG, "knjigeS: {0}, knjigeV: {1}", new Object[]{sirina, visina});
        win.setLocationRelativeTo(null);
        mainPan.setLayout(new BoxLayout(mainPan, BoxLayout.X_AXIS));
        mainPan.setBackground(Grafika.getBgColor());
        mainPan.setAutoscrolls(true);
        knjPan.setLayout(new BoxLayout(knjPan, BoxLayout.Y_AXIS));
        knjPan.setBackground(Grafika.getBgColor());
        knjPan.setAlignmentY(0);
        mainPan.add(knjPan);
        pisacPan.setLayout(new BoxLayout(pisacPan, BoxLayout.Y_AXIS));
        pisacPan.setBackground(Grafika.getBgColor());
        pisacPan.setAlignmentY(0);
        mainPan.add(pisacPan);
        kolPan.setLayout(new BoxLayout(kolPan, BoxLayout.Y_AXIS));
        kolPan.setBackground(Grafika.getBgColor());
        kolPan.setAlignmentY(0);
        mainPan.add(kolPan);
        sidePan.setBackground(Grafika.getBgColor());
        sidePan.setPreferredSize(new Dimension(140, (Podaci.getBrojKnjiga() + 1) * 26));
        sidePan.setAlignmentY(0);
        mainPan.add(sidePan);
        scroll.add(scroll.createVerticalScrollBar());
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        butPan.setBackground(Grafika.getBgColor());
        butPan.setLayout(new FlowLayout(FlowLayout.CENTER));
        split.setOneTouchExpandable(false);
        split.setDividerLocation(visina - 90);
        win.setContentPane(split);
    }

    private void initText() {
        selectAll.setFont(Grafika.getLabelFont());
        selectAll.setForeground(Grafika.getFgColor());
        selectAll.setBackground(Grafika.getBgColor());
        selectAll.setBorder(new EmptyBorder(INSET));
        knjPan.add(selectAll);
        pisacTitle.setFont(Grafika.getLabelFont());
        pisacTitle.setForeground(Grafika.getFgColor()); //bgColor nije neophodan za labele
        pisacTitle.setBorder(new EmptyBorder(INSET));
        pisacPan.add(pisacTitle);
        kolicinaTitle.setFont(Grafika.getLabelFont());
        kolicinaTitle.setForeground(Grafika.getFgColor());
        kolicinaTitle.setBorder(new EmptyBorder(INSET));
        kolPan.add(kolicinaTitle);
        //----------------------------------------------------------------------
        Iterator<Knjiga> it = Podaci.iteratorKnjiga();
        Knjiga knj;
        for (int i = 0; i < knjige.length; i++) {
            knj = it.next();
            knjige[i] = new JCheckBox();
            knjige[i].setText(knj.getNaslov());
            knjige[i].setBorder(new EmptyBorder(INSET));
            knjige[i].setMinimumSize(new Dimension(300, 30));
            knjige[i].setFont(Grafika.getLabelFont());
            knjige[i].setForeground(Grafika.getFgColor());
            knjige[i].setBackground(Grafika.getBgColor());
            knjPan.add(knjige[i]);

            pisac[i] = new JLabel(knj.getPisac());
            pisac[i].setBorder(new EmptyBorder(INSET));
            pisac[i].setFont(Grafika.getLabelFont());
            pisac[i].setForeground(Grafika.getFgColor());
            pisac[i].setBackground(Grafika.getBgColor());
            pisacPan.add(pisac[i]);

            kolicina[i] = new JLabel(String.valueOf(knj.getKolicina()));
            kolicina[i].setBorder(new EmptyBorder(INSET));
            kolicina[i].setFont(Grafika.getLabelFont());
            kolicina[i].setForeground(Grafika.getFgColor());
            kolPan.add(kolicina[i]);
        }
    }

    private void initButtons() {
        JButton novi = new JButton("Ubaci novi naslov");
        novi.setPreferredSize(new Dimension(140, 35));
        novi.addActionListener((ActionEvent e) -> {
            new KnjigeUtils().novi();
        });
        butPan.add(novi);
        JButton obrisi = new JButton("Obriši naslov");
        obrisi.setPreferredSize(new Dimension(130, 35));
        obrisi.addActionListener((ActionEvent e) -> {
            obrisiNaslov();
        });
        butPan.add(obrisi);
        JButton ucSearch = new JButton("Kod koga je naslov...");
        ucSearch.setPreferredSize(new Dimension(150, 35));
        ucSearch.addActionListener((ActionEvent e) -> {
            new KnjigeUtils().ucSearch(getFirstSelected(), visina);
        });
        butPan.add(ucSearch);
    }

    private void initOtherListeners() {
        selectAll.addItemListener((ItemEvent e) -> {
            selectAll();
        });
        for (int i = 0; i < Podaci.getBrojKnjiga(); i++) {
            final int kol = i;
            knjige[i].addItemListener((ItemEvent ie) -> {
                uzmiKnjigu(kol);
            });
        }
    }

    private void initSearchBox() {
        searchBox.setPreferredSize(new Dimension(150, 30));
        searchBox.addFocusListener(this);
        searchBox.addActionListener((ActionEvent e) -> {
            search();
        });
        searchBox.setFont(Grafika.getLabelFont());
        searchBox.setBackground(Grafika.getTFColor());
        searchBox.setForeground(Grafika.getFgColor());
        searchBox.setCaretColor(Grafika.getFgColor());
        searchBox.setBounds(2, 0, 135, 25);
        sidePan.add(searchBox);
    }

    private void obrisiNaslov() {
        boolean selected = false;
        rs.luka.biblioteka.funkcije.Knjige inst = new rs.luka.biblioteka.funkcije.Knjige(); //ostao sam bez inspiracije
        for (int i = 0, realI = 0; i < knjige.length; i++, realI++) {
            if (knjige[i].isSelected()) {
                selected = true;
                try {
                    inst.obrisiNaslov(realI);
                    realI--;
                } catch (PreviseKnjiga ex) {
                    LOGGER.log(Level.INFO, "Knjiga zauzeta. Brisanja naslova nije obavljeno");
                    JOptionPane.showMessageDialog(null, "Kod nekog učenika " + " se nalazi knjiga " 
                            + knjige[i].getText()+ ".\nKada vrati knjigu, pokušajte ponovo.",
                            "Zauzeta knjiga", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        if (!selected) {
            String naslov = Dijalozi.showTextFieldDialog("Brisanje naslova",
                    "Unesite naslov koji želite da obrišete i pritisnite enter:", "");
            try {
                inst.obrisiNaslov(Podaci.indexOfNaslov(naslov));
            } catch (VrednostNePostoji ex) {
                LOGGER.log(Level.INFO, "Unet naslov {0} ne postoji", naslov);
                JOptionPane.showMessageDialog(null, "Naslov koji ste uneli ne postoji.\n"
                        + "Proverite unos i pokušajte ponovo", "Greška pri brisanju naslova",
                        JOptionPane.ERROR_MESSAGE);
            } catch (PreviseKnjiga ex) {
                LOGGER.log(Level.INFO, "Knjiga zauzeta. Brisanja naslova nije obavljeno");
                JOptionPane.showMessageDialog(null, "Zauzeta knjiga", "Kod nekog ucenika "
                        + " se nalazi ova knjiga\n"
                        + "Kada vrati knjigu, pokusajte ponovo.", JOptionPane.ERROR_MESSAGE);
            }
        }
        win.dispose();
        new Knjige();
    }

    private void selectAll() {
        for (JCheckBox knjiga : knjige) {
            knjiga.setSelected(knjiga.isVisible() && selectAll.isSelected());
        }
    }

    private void uzmiKnjigu(int kol) {
        if (knjige[kol].isSelected()) {
            LOGGER.log(Level.FINER, "Prikazujem dugme za uzimanje br {0}", kol);
            uzmiBut[kol] = new JButton("Iznajmi knjigu");
            uzmiBut[kol].setSize(140, 23);
            uzmiBut[kol].setLocation(0,
                    knjige[kol].getLocationOnScreen().y - sidePan.getLocationOnScreen().y);
            uzmiBut[kol].addActionListener((ActionEvent ae) -> {
                String ucenik = Dijalozi.showTextFieldDialog("Iznajmljivanje knjige",
                        "Unesite ime učenika koji iznajmljuje knjigu i pritisnite enter:", "");
                try {
                    Podaci.uzmiKnjigu(kol, ucenik);
                } catch (PreviseKnjiga ex) {
                    LOGGER.log(Level.INFO, "Kod učenika {0} se "
                            + "trenutno nalazi previše knjiga", ucenik);
                    JOptionPane.showMessageDialog(null, "Kod učenika se "
                            + "trenutno nalazi previše knjiga",
                            "Greška pri iznajmljivanju", JOptionPane.ERROR_MESSAGE);
                } catch (Duplikat ex) {
                    LOGGER.log(Level.INFO, "Kod učenika {0} se već nalazi "
                            + "knjiga naslova {1}", new Object[]{ucenik, Podaci.getKnjiga(kol).getNaslov()});
                    JOptionPane.showMessageDialog(null, "Kod učenika se "
                            + "već nalazi knjiga tog naslova",
                            "Greška pri iznajmljivanju", JOptionPane.ERROR_MESSAGE);
                } catch (NemaViseKnjiga ex) {
                    LOGGER.log(Level.INFO, "Nema više knjiga naslova {0} "
                            + "u biblioteci", Podaci.getKnjiga(kol).getNaslov());
                    JOptionPane.showMessageDialog(null, "Nema više knjiga"
                            + " tog naslova", "Greška pri iznajmljivanju",
                            JOptionPane.ERROR_MESSAGE);
                } catch (VrednostNePostoji ex) {
                    LOGGER.log(Level.INFO, "Učenik {0} nije pronađen", ucenik);
                    JOptionPane.showMessageDialog(null, "Učenik nije pronađen.\n"
                            + "Proverite unos i pokušajte ponovo",
                            "Greška pri iznajmljivanju", JOptionPane.ERROR_MESSAGE);
                }
            });
            uzmiBut[kol].setVisible(true);
            sidePan.add(uzmiBut[kol]);
            sidePan.repaint();
            LOGGER.log(Level.FINE, "Dugme za uzimanje br. {0} prikazano.", kol);
        } else {
            uzmiBut[kol].setVisible(false);
            uzmiBut[kol] = null;
            LOGGER.log(Level.FINE, "Dugme za uzimanje br. {0} obrisano", kol);
        }
    }

    private void search() {
        rs.luka.biblioteka.funkcije.Knjige funkcije = new rs.luka.biblioteka.funkcije.Knjige();
        ArrayList<Integer> nasIndexes = funkcije.pretraziKnjige(searchBox.getText());
        for (int i = 0; i < Podaci.getBrojKnjiga(); i++) {
            if (nasIndexes.contains(i)) {
                Knjiga knjiga = Podaci.getKnjiga(i);
                knjige[i].setText(knjiga.getNaslov());
                knjige[i].setVisible(true);
                kolicina[i].setText(String.valueOf(knjiga.getKolicina()));
                kolicina[i].setVisible(true);
                pisac[i].setText(knjiga.getPisac());
                pisac[i].setVisible(true);
            } else {
                knjige[i].setSelected(false);
                knjige[i].setVisible(false);
                kolicina[i].setVisible(false);
                pisac[i].setVisible(false);
            }
        }
        
        sidePan.setMaximumSize(new Dimension(140, (nasIndexes.size() +1) * selectAll.getHeight()));
        LOGGER.log(Level.FINE, "Pretraga obavljena (grafički)");
    }
    
    private String getFirstSelected() {
        for(JCheckBox knjiga : knjige) {
            if(knjiga.isSelected() && knjiga.isVisible())
                return knjiga.getText();
        }
        return null;
    }

    @Override
    public void focusGained(FocusEvent e) {
        if (searchBox.getText().equals(SEARCH_TEXT)) {
            searchBox.setText("");
        }
    }

    @Override
    public void focusLost(FocusEvent e) {
        if(searchBox.getText().isEmpty()) {
            searchBox.setText(SEARCH_TEXT);
        }
    }
}
