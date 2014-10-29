//1645 linija, 24.8.'14.
//1994 linije, 24.9.'14.
//2141 linija, 25.10.'14.
package rs.luka.biblioteka.grafika;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import static java.lang.Integer.parseInt;
import java.util.logging.Level;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.showMessageDialog;
import static javax.swing.JOptionPane.showOptionDialog;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import static javax.swing.UIManager.getCrossPlatformLookAndFeelClassName;
import static javax.swing.UIManager.getSystemLookAndFeelClassName;
import static javax.swing.UIManager.setLookAndFeel;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;
import javax.swing.plaf.metal.DefaultMetalTheme;
import static javax.swing.plaf.metal.MetalLookAndFeel.setCurrentTheme;
import javax.swing.plaf.metal.OceanTheme;
import rs.luka.biblioteka.data.Config;
import static rs.luka.biblioteka.funkcije.Init.exit;
import rs.luka.biblioteka.funkcije.Save;
import rs.luka.biblioteka.funkcije.Undo;
import static rs.luka.biblioteka.funkcije.Utils.parseBoolean;

/**
 *
 * @author luka
 */
public class Grafika {
    
    private static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(Grafika.class.getName());

    /**
     * Font koji se koristi za JLabele. Menja se samo preko configa i zvanjem 
     * {@link #setVariables} metode za refresh.
     */
    private static final Font labelFont = new Font("Segoe UI", Font.PLAIN, 15);
    /**
     * Pozadinska boja za prozore.
     */
    private static Color bgColor = new Color(40, 255, 40, 220);
    /**
     * Boja fonta.
     */
    private static Color fgColor = new Color(0);
    /**
     * Boja textFieldova. 
     */
    private static Color TFColor = new Color(-1);

    private static JPanel pan;

    /**
     * Iscrtava glavni prozor. Pri zatvaranju zove
     * {@link rs.luka.biblioteka.funkcije.Init#exit}
     */
    public static void Grafika() {
        setVariables();
        JFrame win = new JFrame("Biblioteka");
        win.setSize(460, 220);
        win.setResizable(false);
        win.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        win.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                LOGGER.log(Level.FINE, "Iniciram zatvaranje aplikacije i "
                        + "prikazujem dijalog za čuvanje podataka.");
                String[] opcije = {"Da", "Ne"};
                int sacuvaj = showOptionDialog(null, "Sacuvati izmene?",
                        "Izlaz", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                        null, opcije, opcije[0]); //0 za da, 1 za ne, -1 za X
                if (sacuvaj != -1) {
                    LOGGER.log(Level.FINE, "Izlaz otkazan. Ostajem u aplikaciji.");
                    exit(!parseBoolean(sacuvaj)); //0 oznacava false (sacuvati), a 1 true (brisati)
                                            //drugim recima, cuva samo ako je odabrana prva opcija
                }

            }
        });
        win.setLocationRelativeTo(null);
        pan = new JPanel(null);
        pan.setBackground(getBgColor());
        win.setContentPane(pan);
        /**
         * Pregled Knjiga.
         */
        JButton pregledBut = new JButton("Knjige");
        pregledBut.addActionListener((ActionEvent e5) -> {
            new Knjige().pregledKnjiga();
        });
        pregledBut.setBounds(60, 40, 100, 40);
        pregledBut.setFocusable(false);
        pan.add(pregledBut);
        /**
         * Pregled ucenika.
         */
        JButton uc = new JButton("Ucenici");
        uc.addActionListener((ActionEvent e) -> {
            new Ucenici().pregledUcenika();
        });
        uc.setBounds(300, 40, 100, 40);
        uc.setFocusable(false);
        pan.add(uc);
        /**
         * Dugme za cuvanje podataka.
         */
        JButton saveBut = new JButton("Sacuvaj podatke");
        saveBut.addActionListener((ActionEvent e) -> {
            try {
                Save.save();
            } catch (IOException ex) {
                showMessageDialog(null, "Doslo je do greske pri "
                        + "cuvanju fajlova", "I/O Greska", JOptionPane.ERROR_MESSAGE);
            }
        });
        saveBut.setBounds(25, 110, 170, 40);
        saveBut.setFocusable(false);
        pan.add(saveBut);
        /**
         * Dugme za otvaranje prozora za podesavanja.
         */
        JButton podesavanjaBut = new JButton("Podesavanja");
        podesavanjaBut.addActionListener((ActionEvent e) -> {
            new Podesavanja().podesavanja();
        });
        podesavanjaBut.setBounds(285, 110, 130, 40);
        podesavanjaBut.setFocusable(false);
        pan.add(podesavanjaBut);
        
        //----------InputMaps---------------------------------------------------
        pan.getInputMap().put(KeyStroke.getKeyStroke("ctrl Z"), "undo");
        pan.getActionMap().put("undo", new Action() {
            @Override public Object getValue(String key) {return null;}
            @Override public void putValue(String key, Object value) {throw new UnsupportedOperationException();}
            @Override public void setEnabled(boolean b) {}
            @Override public boolean isEnabled() {return true;}
            @Override public void addPropertyChangeListener(PropertyChangeListener listener) {throw new UnsupportedOperationException();}
            @Override public void removePropertyChangeListener(PropertyChangeListener listener) {throw new UnsupportedOperationException();}

            @Override
            public void actionPerformed(ActionEvent e) {
                Undo.undo();
            }
        });
        pan.getInputMap().put(KeyStroke.getKeyStroke("ctrl Y"), "redo");
        pan.getActionMap().put("redo", new Action() {
            @Override public Object getValue(String key) {return null;}
            @Override public void putValue(String key, Object value) {throw new UnsupportedOperationException();}
            @Override public void setEnabled(boolean b) {}
            @Override public boolean isEnabled() {return true;}
            @Override public void addPropertyChangeListener(PropertyChangeListener listener) {throw new UnsupportedOperationException();}
            @Override public void removePropertyChangeListener(PropertyChangeListener listener) {throw new UnsupportedOperationException();}

            @Override
            public void actionPerformed(ActionEvent e) {
                Undo.redo();
            }
        });
        pan.getInputMap().put(KeyStroke.getKeyStroke("ctrl shift T"), "console");
        pan.getActionMap().put("console", new Action() {
            @Override public Object getValue(String key) {return null;}
            @Override public void putValue(String key, Object value) {throw new UnsupportedOperationException();}
            @Override public void setEnabled(boolean b) {}
            @Override public boolean isEnabled() {return true;}
            @Override public void addPropertyChangeListener(PropertyChangeListener listener) {throw new UnsupportedOperationException();}
            @Override public void removePropertyChangeListener(PropertyChangeListener listener) {throw new UnsupportedOperationException();}

            @Override
            public void actionPerformed(ActionEvent e) {
                new rs.luka.biblioteka.debugging.Console().console();
            }
        });
        //----------------------------------------------------------------------
        win.setVisible(true);
    }

    /**
     * Podesava promenljive iz ove klase prema configu.
     */
    private static void setVariables() {
        if (Config.hasKey("bgBoja")) {
            bgColor = new Color(parseInt(Config.get("bgBoja")));
            LOGGER.log(Level.CONFIG, "bgBoja: {0}", bgColor.toString());
        }
        if (Config.hasKey("fgBoja")) {
            fgColor = new Color(parseInt(Config.get("fgBoja")));
            LOGGER.log(Level.CONFIG, "fgBoja: {0}", fgColor.toString());
        }
        if(Config.hasKey("TFColor")) {
            TFColor = new Color(Config.getAsInt("TFColor"));
            LOGGER.log(Level.CONFIG, "TFColor: {0}", TFColor.toString());
        }
    }

    /**
     * Ponovo podesava pozadinsku boju za ovaj prozor.
     */
    protected static void refresh() {
        setVariables();
        pan.setBackground(getBgColor());
        LOGGER.log(Level.FINE, "Boja glavnog prozora refreshovana.");
    }

    /**
     * Ucitava temu (Look and Feel).
     */
    public static void loadLnF() {
        try {
            String LaF = Config.get("lookAndFeel");
            if (LaF.equalsIgnoreCase("system")) {
                setLookAndFeel(getSystemLookAndFeelClassName());
            } else if (LaF.startsWith("cross")) {
                setLookAndFeel(getCrossPlatformLookAndFeelClassName());
                if (LaF.endsWith("Metal")) {
                    setCurrentTheme(new DefaultMetalTheme());
                }
                if (LaF.endsWith("Ocean")) {
                    setCurrentTheme(new OceanTheme());
                }
            }
            LOGGER.log(Level.CONFIG, "lookAndFeel: {0}", UIManager.getLookAndFeel());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            LOGGER.log(Level.SEVERE, "Greška pri postavljanju teme (Look and Feel).", ex);
            showMessageDialog(null, "Došlo je do greške pri postavljanju teme.", 
                    "LookAndFeel greška", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * @return the labelFont
     */
    protected static Font getLabelFont() {
        return labelFont;
    }

    /**
     * @return the bgColor
     */
    protected static Color getBgColor() {
        return bgColor;
    }

    /**
     * @return the fgColor
     */
    protected static Color getFgColor() {
        return fgColor;
    }
    
    protected static Color getTFColor() {
        return TFColor;
    }
}
