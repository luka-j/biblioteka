package rs.luka.biblioteka.grafika;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import static java.lang.Integer.parseInt;
import static java.lang.Math.min;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.showMessageDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import rs.luka.biblioteka.data.Config;
import rs.luka.biblioteka.data.Knjiga;
import rs.luka.biblioteka.data.Podaci;
import static rs.luka.biblioteka.data.Podaci.getUcenik;
import rs.luka.biblioteka.exceptions.Duplikat;
import rs.luka.biblioteka.exceptions.NemaViseKnjiga;
import rs.luka.biblioteka.exceptions.Prazno;
import rs.luka.biblioteka.exceptions.PreviseKnjiga;
import rs.luka.biblioteka.exceptions.VrednostNePostoji;

/**
 * @author Luka
 */
public class Knjige implements FocusListener {
    
    private static final java.util.logging.Logger LOGGER = 
            java.util.logging.Logger.getLogger(Knjige.class.getName());

    /**
     * searchBox za pretrazivanje knjiga.
     */
    private final JTextField searchBox = new JTextField("Pretrazi knjige...");

    private int sirina, visina;

    /**
     * Pregled knjiga koje su trenutno u biblioteci.
     */
    public void pregledKnjiga() {
        //----------Frames&Panels-----------------------------------------------
        JFrame win = new JFrame("Pregled knjiga");
        win.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        sirina = parseInt(Config.get("knjigeS", "480"));
        visina = parseInt(Config.get("knjigeV", "750"));
        win.setSize(sirina, visina);
        LOGGER.log(Level.CONFIG, "knjigeS: {0}, knjigeV: {1}", new Object[]{sirina, visina});
        win.setLocationRelativeTo(null);
        //win.setResizable(false);
        FlowLayout layout = new FlowLayout(FlowLayout.LEADING);
        layout.setAlignOnBaseline(true);
        JPanel mainPan = new JPanel(layout);
        mainPan.setBackground(Grafika.getBgColor());
        mainPan.setAutoscrolls(true);
        JPanel knjPan = new JPanel(null);
        knjPan.setLayout(new BoxLayout(knjPan, BoxLayout.Y_AXIS));
        knjPan.setBackground(Grafika.getBgColor());
        mainPan.add(knjPan);
        JPanel pisacPan = new JPanel(null);
        pisacPan.setLayout(new BoxLayout(pisacPan, BoxLayout.Y_AXIS));
        pisacPan.setBackground(Grafika.getBgColor());
        mainPan.add(pisacPan);
        JPanel kolPan = new JPanel(null);
        kolPan.setLayout(new BoxLayout(kolPan, BoxLayout.Y_AXIS));
        kolPan.setBackground(Grafika.getBgColor());
        mainPan.add(kolPan);
        JPanel sidePan = new JPanel(null);
        sidePan.setBackground(Grafika.getBgColor());
        sidePan.setPreferredSize(new Dimension(140, (Podaci.getBrojKnjiga() + 1) * 26));
        mainPan.add(sidePan);
        JScrollPane scroll = new JScrollPane(mainPan);
        scroll.add(scroll.createVerticalScrollBar());
        JPanel butPan = new JPanel();
        butPan.setBackground(Grafika.getBgColor());
        butPan.setLayout(new FlowLayout(FlowLayout.CENTER));
        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, scroll, butPan);
        split.setOneTouchExpandable(false);
        split.setDividerLocation(visina - 90);
        win.setContentPane(split);
        //----------JLabel------------------------------------------------------
        JCheckBox selectAll = new JCheckBox("Naslovi:");
        selectAll.setFont(Grafika.getLabelFont());
        selectAll.setForeground(Grafika.getFgColor());
        selectAll.setBackground(Grafika.getBgColor());
        knjPan.add(selectAll);
        JLabel pisacTitle = new JLabel("Pisac:");
        pisacTitle.setFont(Grafika.getLabelFont());
        pisacTitle.setForeground(Grafika.getFgColor()); //bgColor nije neophodan za labele
        pisacPan.add(pisacTitle);
        JLabel kolicinaTitle = new JLabel("Količina:");
        kolicinaTitle.setFont(Grafika.getLabelFont());
        kolicinaTitle.setForeground(Grafika.getFgColor());
        kolPan.add(kolicinaTitle);

        JCheckBox[] knjige = new JCheckBox[Podaci.getBrojKnjiga()];
        JLabel[] kolicina = new JLabel[Podaci.getBrojKnjiga()];
        Component[] kolSpace = new Component[Podaci.getBrojKnjiga()]; //mora u u niz da
        //bi moglo da se obrise dovoljno mesta pri pretrazi (da se poravna sa naslovima)
        JLabel[] pisac = new JLabel[Podaci.getBrojKnjiga()];
        Component[] pisacSpace = new Component[Podaci.getBrojKnjiga()];
        for (int i = 0; i < knjige.length; i++) {
            knjige[i] = new JCheckBox();
            knjige[i].setText(Podaci.getKnjiga(i).getNaslov());
            knjige[i].setFont(Grafika.getLabelFont());
            knjige[i].setForeground(Grafika.getFgColor());
            knjige[i].setBackground(Grafika.getBgColor());
            knjPan.add(knjige[i]);
            
            pisac[i] = new JLabel(Podaci.getKnjiga(i).getPisac());
            pisac[i].setFont(Grafika.getLabelFont());
            pisac[i].setForeground(Grafika.getFgColor());
            pisac[i].setBackground(Grafika.getBgColor());
            pisacSpace[i] = Box.createRigidArea(new Dimension(0,8)); //jcheckbox ima padding 
            //po defaultu, ovo poravnava text kolicine se naslovom. setMinimumSize ne radi.
            pisacPan.add(pisacSpace[i]);
            pisacPan.add(pisac[i]);

            kolicina[i] = new JLabel(String.valueOf(Podaci.getKnjiga(i).getKolicina()));
            kolicina[i].setFont(Grafika.getLabelFont());
            kolicina[i].setForeground(Grafika.getFgColor());
            kolSpace[i] = Box.createRigidArea(new Dimension(0, 8)); //vidi iznad
            kolPan.add(kolSpace[i]);
            kolPan.add(kolicina[i]);
        }
        //----------JButtons-----------------------------------------------------
        JButton novi = new JButton("Ubaci novi naslov");
        novi.setPreferredSize(new Dimension(140, 35));
        novi.addActionListener((ActionEvent e) -> {
            novi();
        });
        butPan.add(novi);
        JButton obrisi = new JButton("Obriši naslov");
        obrisi.setPreferredSize(new Dimension(130, 35));
        obrisi.addActionListener((ActionEvent e) -> {
            boolean selected = false;
            rs.luka.biblioteka.funkcije.Knjige obj = new rs.luka.biblioteka.funkcije.Knjige(); //ostao sam bez inspiracije
            for(int i=0; i<knjige.length; i++) {
                if(knjige[i].isSelected()) {
                    obj.obrisiNaslov(i);
                    selected = true;
                }
            }
            if(!selected) {
                String naslov = Dijalozi.showTextFieldDialog("Brisanje naslova", 
                "Unesite naslov koji želite da obrišete i pritisnite enter:", "");
                try {
                    obj.obrisiNaslov(Podaci.indexOfNaslov(naslov));
                } catch (VrednostNePostoji ex) {
                    LOGGER.log(Level.INFO, "Unet naslov {0} ne postoji", naslov);
                    JOptionPane.showMessageDialog(null, "Naslov koji ste uneli ne postoji.\n"
                            + "Proverite unos i pokušajte ponovo", "Greška pri brisanju naslova", 
                            JOptionPane.ERROR_MESSAGE);
                }
            }
            win.dispose();
            pregledKnjiga();
        });
        butPan.add(obrisi);
        JButton ucSearch = new JButton("Kod koga je naslov...");
        ucSearch.setPreferredSize(new Dimension(150, 35));
        ucSearch.addActionListener((ActionEvent e) -> {
            ucSearch(null);
        });
        butPan.add(ucSearch);
        JButton[] uzmiBut = new JButton[Podaci.getBrojKnjiga()];
        //----------Listeners---------------------------------------------------
        selectAll.addItemListener((ItemEvent e) -> {
            for (JCheckBox knjiga : knjige) {
                knjiga.setSelected(knjiga.isVisible() && selectAll.isSelected());
            }
            //premature optimization?
        });
        for (int i = 0; i < Podaci.getBrojKnjiga(); i++) {
            final int kol = i;
            knjige[i].addItemListener((ItemEvent ie) -> {
                if (knjige[kol].isSelected()) {
                    LOGGER.log(Level.FINER, "Prikazujem dugme za uzimanje br {0}", kol);
                    uzmiBut[kol] = new JButton("Iznajmi knjigu");
                    uzmiBut[kol].setSize(140, 23);
                    uzmiBut[kol].setLocation(5, 
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
            });
        }
        //----------search------------------------- -----------------------------
        searchBox.setPreferredSize(new Dimension(150, 30));
        searchBox.addFocusListener(this);
        searchBox.addActionListener((ActionEvent e) -> {
            rs.luka.biblioteka.funkcije.Knjige funkcije = new rs.luka.biblioteka.funkcije.Knjige();
            ArrayList<Integer> nasIndexes = funkcije.pretraziKnjige(searchBox.getText());
            for (int i = 0; i < Podaci.getBrojKnjiga(); i++) {
                if (nasIndexes.contains(i)) {
                    Knjiga knjiga = Podaci.getKnjiga(i);
                    knjige[i].setText(knjiga.getNaslov());
                    knjige[i].setVisible(true);
                    kolicina[i].setText(String.valueOf(knjiga.getKolicina()));
                    kolicina[i].setVisible(true);
                    kolSpace[i].setVisible(true);
                    pisac[i].setText(knjiga.getPisac());
                    pisac[i].setVisible(true);
                    pisacSpace[i].setVisible(true);
                } else {
                    knjige[i].setSelected(false);
                    knjige[i].setVisible(false);
                    kolicina[i].setVisible(false);
                    kolSpace[i].setVisible(false);
                    pisac[i].setVisible(false);
                    pisacSpace[i].setVisible(false);
                }
            }
            int y = (int) (((Podaci.getBrojKnjiga() + 1 - nasIndexes.size()) * 24.8) / 2); //priblizno !!
            scroll.getViewport().setViewPosition(new Point(10, y));  //izuzetno los workaround
            searchBox.setLocation(searchBox.getX(), y);

            mainPan.revalidate();
            mainPan.repaint();
            LOGGER.log(Level.FINE, "Pretraga obavljena (grafički)");
        });
        searchBox.setFont(Grafika.getLabelFont());
        searchBox.setBackground(Grafika.getTFColor());
        searchBox.setForeground(Grafika.getFgColor());
        searchBox.setCaretColor(Grafika.getFgColor());
        searchBox.setBounds(2, 0, 135, 25);
        sidePan.add(searchBox);
        //----------setVisible--------------------------------------------------
        win.setVisible(true);
    }

    /**
     * Prozor za upisivanje novog naslova.
     * @since jako davno (pocetak)
     */
    private void novi() {
        //----------JFrame&JPanel-----------------------------------------------
        final JFrame nnF = new JFrame("Unos novog naslova");
        nnF.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        nnF.setSize(330, 330);
        nnF.setLocationRelativeTo(null);
        nnF.setResizable(false);
        JPanel nnPan = new JPanel(null);
        nnPan.setBackground(Grafika.getBgColor());
        nnF.setContentPane(nnPan);
        //----------JLabels&JTextFields-----------------------------------------
        JLabel nas = new JLabel("Unesite naslov nove knjige:");
        nas.setBounds(15, 15, 300, 30);
        nas.setFont(Grafika.getLabelFont());
        nas.setForeground(Grafika.getFgColor());
        nnPan.add(nas);
        final JTextField nasTF = new JTextField();
        nasTF.setBounds(15, 50, 300, 25);
        nasTF.setFont(Grafika.getLabelFont());
        nasTF.setBackground(Grafika.getTFColor());
        nasTF.setForeground(Grafika.getFgColor());
        nasTF.setCaretColor(Grafika.getFgColor());
        nnPan.add(nasTF);
        JLabel pisac = new JLabel("Unesite pisca knjige:");
        pisac.setBounds(15, 95, 300, 30);
        pisac.setFont(Grafika.getLabelFont());
        pisac.setForeground(Grafika.getFgColor());
        JTextField pisacTF = new JTextField();
        nnPan.add(pisac);
        pisacTF.setBounds(15, 130, 300, 25);
        pisacTF.setFont(Grafika.getLabelFont());
        pisacTF.setForeground(Grafika.getFgColor());
        pisacTF.setCaretColor(Grafika.getFgColor());
            pisacTF.setBackground(Grafika.getTFColor());
        nnPan.add(pisacTF);
        JLabel kolicina = new JLabel("Unesite količinu:");
        kolicina.setBounds(15, 165, 300, 30);
        kolicina.setFont(Grafika.getLabelFont());
        kolicina.setForeground(Grafika.getFgColor());
        nnPan.add(kolicina);
        final JTextField kolTF = new JTextField();
        kolTF.setBounds(15, 200, 300, 25);
        kolTF.setFont(Grafika.getLabelFont());
        kolTF.setForeground(Grafika.getFgColor());
        kolTF.setCaretColor(Grafika.getFgColor());
            kolTF.setBackground(Grafika.getTFColor());
        
        nnPan.add(kolTF);
        //----------JButton-----------------------------------------------------
        JButton unos = new JButton("Unesi podatke");
        unos.setBounds(90, 240, 150, 40);
        ActionListener listener = (ActionEvent e) -> {
            rs.luka.biblioteka.funkcije.Knjige nn = new rs.luka.biblioteka.funkcije.Knjige();
            try {
                try {
                    nn.ubaciNoviNaslov(nasTF.getText(), parseInt(kolTF.getText()), pisacTF.getText());
                    showMessageDialog(null, "Knjiga dodata!", "Uspeh!", JOptionPane.INFORMATION_MESSAGE);
                    nnF.dispose();
                } catch (VrednostNePostoji ex) {
                    LOGGER.log(Level.INFO, "Polje za unos naslova je prazno pri unosu novog naslova");
                    showMessageDialog(null, "Polje za naslov je prazno.",
                            "Prazno polje", JOptionPane.ERROR_MESSAGE);
                } catch (Duplikat ex) {
                    LOGGER.log(Level.INFO, "Naslov {0} već postoji", nasTF.getText());
                    showMessageDialog(null, "Knjiga tog naslova već postoji",
                            "Duplikat", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                LOGGER.log(Level.INFO, "{0} nije broj", nasTF.getText());
                showMessageDialog(null, "Uneta količina nije broj.",
                        "Loš unos", JOptionPane.ERROR_MESSAGE);
            }
        };
        unos.addActionListener(listener);
        kolTF.addActionListener(listener);
        nnPan.add(unos);
        //----------setVisible--------------------------------------------------
        nnF.setVisible(true);
    }

    /**
     * Prozor za pretragu po ucenicima (kod koga je koja knjiga).
     *
     * @param ime ako nije null, prikazuje prozor koji trazi da se unese ime
     * ucenika, pa zatim pretrazuje i ponovo iscrtava isti prozor sa trazenim
     * podacima. Ako je ime string
     *
     * @since 25.6.'14.
     */
    private void ucSearch(String ime) {
        rs.luka.biblioteka.funkcije.Knjige funk = new rs.luka.biblioteka.funkcije.Knjige();
        //-----------JFrame&JPanel------------------------------
        JFrame winS = new JFrame("Pretraga učenika po naslovu");
        winS.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        //winS.setLocation(270, 160);
        winS.setSize(350, 130);
        winS.setLocationRelativeTo(null);
        JPanel panS = new JPanel(null);
        panS.setBackground(Grafika.getBgColor());
        winS.setContentPane(panS);
        //------------JLabel-----------------------------------
        JLabel naslov = new JLabel("Unesite naslov i pritisnite enter:");
        naslov.setFont(Grafika.getLabelFont());
        naslov.setForeground(Grafika.getFgColor());
        naslov.setBounds(20, 20, 350, 25);
        panS.add(naslov);
        //------------JTextField&ActionListener---------------------------------
        JTextField naslovTF = new JTextField();
        ActionListener listener = (ActionEvent e) -> {
            try {
                ArrayList<Point> inx = funk.pretraziUcenike(naslovTF.getText());
                if (inx.isEmpty()) {
                    throw new Prazno("Učenici nemaju tu knjigu");
                }
                int ucVisina = min(visina, 80 + inx.size() * 21 + 58);
                //scrollPan.setBounds(0, 0, 560, ucVisina);
                panS.setLocation(0, 0);
                panS.setPreferredSize(new Dimension(560, 95 + inx.size() * 21));
                winS.setSize(580, ucVisina);
                winS.setLocationRelativeTo(null);
                StringBuilder ucBuild = new StringBuilder();
                StringBuilder dateBuild = new StringBuilder();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd. MM. yyyy");
                inx.stream().map((inx1) -> {
                    ucBuild.append(getUcenik(inx1.y).getIme()).append("<br>");
                    return inx1;
                }).forEach((inx1) -> {
                    dateBuild.append(dateFormat.format(getUcenik(inx1.y).
                            getDatumKnjige(inx1.x))).append("<br>");
                });
                JLabel ucenici = new JLabel();
                ucenici.setSize(new Dimension(300, (inx.size() + 1) * 21));
                ucenici.setLocation(10, 10);
                ucenici.setFont(Grafika.getLabelFont());
                ucenici.setText("<html>Učenici kod kojih je trenutno knjiga:<br>" 
                        + ucBuild.toString() + "</html>");
                JLabel datumi = new JLabel();
                datumi.setLocation(310, 10);
                datumi.setSize(new Dimension(225, (inx.size() + 1) * 21));
                datumi.setFont(Grafika.getLabelFont());
                datumi.setText("<html>Datum kada je knjiga iznajmljena:<br>" 
                        + dateBuild.toString() + "</html>");

                JButton ok = new JButton("OK");
                ok.setLocation(250, inx.size() * 21 + 50);
                ok.setSize(55, 33);
                ok.addActionListener((ActionEvent ae) -> {
                    winS.dispose();
                });
                panS.removeAll();
                panS.revalidate();
                panS.add(ucenici);
                panS.add(datumi);
                panS.add(ok);
                panS.revalidate();
                panS.repaint();

                JScrollPane scrollPan = new JScrollPane(panS);
                scrollPan.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                scrollPan.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
                winS.setContentPane(scrollPan);
                winS.revalidate();
                scrollPan.setBackground(Grafika.getBgColor());
                winS.repaint();
            } catch (VrednostNePostoji ex) {
                LOGGER.log(Level.INFO, "Naslov {0} nije pronađen", naslovTF.getText());
                showMessageDialog(null, "Knjiga nije pronađena.\n"
                        + "Proverite unos i pokušajte ponovo", "Knjiga ne postoji",
                        JOptionPane.ERROR_MESSAGE);
            } catch (Prazno ex) {
                showMessageDialog(null, "Trenutno se tražena knjiga ne nalazi ni kod koga.",
                        "Niko nije iznajmio knjigu", JOptionPane.INFORMATION_MESSAGE);
            }
            
        };
        naslovTF.addActionListener(listener);
        naslovTF.setBounds(20, 50, 200, 25);
        naslovTF.setFont(Grafika.getLabelFont());
            naslovTF.setBackground(Grafika.getTFColor());
        naslovTF.setForeground(Grafika.getFgColor());
        naslovTF.setCaretColor(Grafika.getFgColor());
        panS.add(naslovTF);

        //naknadno dodato, najbezbolniji nacin, da ne radim refactoring
        if (ime != null) { //ako je poslat argument koristeci checkbox
            LOGGER.log(Level.FINE, "Iniciram pretragu učenika po knjizi preko checkboxa");
            naslovTF.setText(ime);
            listener.actionPerformed(null);
        }
        //-----------setVisible----------------------------------
        winS.setVisible(true);
    }

    @Override
    public void focusGained(FocusEvent e) {
        if (searchBox.getText().equals("Pretrazi knjige...")) {
            searchBox.setText("");
        }
    }

    @Override
    public void focusLost(FocusEvent e) {
    }
}
