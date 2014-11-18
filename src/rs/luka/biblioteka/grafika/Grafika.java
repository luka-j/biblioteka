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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.showMessageDialog;
import static javax.swing.JOptionPane.showOptionDialog;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import static javax.swing.UIManager.getCrossPlatformLookAndFeelClassName;
import static javax.swing.UIManager.getSystemLookAndFeelClassName;
import static javax.swing.UIManager.setLookAndFeel;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;
import javax.swing.plaf.metal.DefaultMetalTheme;
import static javax.swing.plaf.metal.MetalLookAndFeel.setCurrentTheme;
import rs.luka.biblioteka.data.Config;
import rs.luka.biblioteka.debugging.Console;
import rs.luka.biblioteka.funkcije.Init;
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
    private static final Font labelFont;
    /**
     * Pozadinska boja za prozore.
     */
    private static Color bgColor;
    /**
     * Boja fonta.
     */
    private static Color fgColor;
    /**
     * Boja textFieldova.
     */
    private static Color TFColor;

    private static final JFrame win;
    private static JPanel pan;

    static {
        win = new JFrame("Biblioteka");
        labelFont = new Font("Segoe UI", Font.PLAIN, 15);
        bgColor = new Color(40, 255, 40, 220); //zelena
        fgColor = new Color(0); //crna
        TFColor = new Color(-1); //bela
    }

    /**
     * Iscrtava glavni prozor. Pri zatvaranju zove
     * {@link rs.luka.biblioteka.funkcije.Init#exit}
     */
    public static void glavniProzor() {
        setVariables();
        win.setSize(460, 220);
        win.setResizable(false);
        win.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        win.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                LOGGER.log(Level.FINE, "Iniciram zatvaranje aplikacije i "
                        + "prikazujem dijalog za čuvanje podataka.");
                String[] opcije = {"Da", "Ne"};
                int sacuvaj = showOptionDialog(null, "Sačuvati izmene?",
                        "Izlaz", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                        null, opcije, opcije[0]); //0 za da, 1 za ne, -1 za X
                if (sacuvaj != -1) {
                    exit(!parseBoolean(sacuvaj)); //0 oznacava false (sacuvati), a 1 true (brisati)
                    //drugim recima, cuva samo ako je odabrana prva opcija
                }
                LOGGER.log(Level.FINE, "Izlaz otkazan. Ostajem u aplikaciji.");
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
            new Knjige();
        });
        pregledBut.setBounds(60, 40, 100, 40);
        pregledBut.setFocusable(false);
        pan.add(pregledBut);
        /**
         * Pregled ucenika.
         */
        JButton uc = new JButton("Učenici");
        uc.addActionListener((ActionEvent e) -> {
            new Ucenici();
        });
        uc.setBounds(300, 40, 100, 40);
        uc.setFocusable(false);
        pan.add(uc);
        /**
         * Dugme za cuvanje podataka.
         */
        JButton saveBut = new JButton("Sačuvaj podatke");
        saveBut.addActionListener((ActionEvent e) -> {
            try {
                Save.save();
            } catch (IOException ex) {
                showMessageDialog(null, "Došlo je do greške pri "
                        + "čuvanju fajlova", "I/O Greska", JOptionPane.ERROR_MESSAGE);
            }
        });
        saveBut.setBounds(25, 110, 170, 40);
        saveBut.setFocusable(false);
        pan.add(saveBut);
        /**
         * Dugme za otvaranje prozora za podesavanja.
         */
        JButton podesavanjaBut = new JButton("Podešavanja");
        podesavanjaBut.addActionListener((ActionEvent e) -> {
            new Podesavanja().podesavanja();
        });
        podesavanjaBut.setBounds(285, 110, 130, 40);
        podesavanjaBut.setFocusable(false);
        pan.add(podesavanjaBut);

        //----------InputMaps---------------------------------------------------
        Console console = new rs.luka.biblioteka.debugging.Console();
        Method consoleMethod = null, undoMethod = null, redoMethod=null;
        try {
            consoleMethod = console.getClass().getDeclaredMethod("console", null);
            undoMethod = Undo.class.getDeclaredMethod("undo", null);
            redoMethod = Undo.class.getDeclaredMethod("redo", null);
        } catch (NoSuchMethodException | SecurityException ex) {
            LOGGER.log(Level.SEVERE, "Greška pri kreiranju neke od metoda", ex);
        }
        pan.getInputMap().put(KeyStroke.getKeyStroke("ctrl Z"), "undo");
        pan.getActionMap().put("undo", generateAction(undoMethod, null));
        pan.getInputMap().put(KeyStroke.getKeyStroke("ctrl Y"), "redo");
        pan.getActionMap().put("redo", generateAction(redoMethod, null));
        pan.getInputMap().put(KeyStroke.getKeyStroke("ctrl shift T"), "console");
        pan.getActionMap().put("console", generateAction(consoleMethod, console));
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
        if (Config.hasKey("TFColor")) {
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
            LaF = LaF.toLowerCase();
            switch (LaF) {
                case "system":
                    setLookAndFeel(getSystemLookAndFeelClassName());
                    break;
                case "ocean":
                    setLookAndFeel(getCrossPlatformLookAndFeelClassName());
                    break;
                case "metal":
                    setLookAndFeel(getCrossPlatformLookAndFeelClassName());
                    setCurrentTheme(new DefaultMetalTheme());
                    break;
                case "nimbus":
                    setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
                    break;
                case "motif":
                    setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
            }
            System.out.println("system LaF: " + getSystemLookAndFeelClassName());
            System.out.println("cross LaF: " + getCrossPlatformLookAndFeelClassName());
            System.out.println("sve");
            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                System.out.println(info.getName() + "\t" + info.getClassName());
            }
            LOGGER.log(Level.CONFIG, "lookAndFeel: {0}", UIManager.getLookAndFeel());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            LOGGER.log(Level.SEVERE, "Greška pri postavljanju teme (Look and Feel).", ex);
            showMessageDialog(null, "Došlo je do greške pri postavljanju teme.",
                    "LookAndFeel greška", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private static Action generateAction(Method invoke, Object obj) {
        return new Action() {
            @Override public Object getValue(String key) {return null;}
            @Override public void putValue(String key, Object value) {throw new UnsupportedOperationException();}
            @Override public void setEnabled(boolean b) { }
            @Override public boolean isEnabled() {return true;}
            @Override public void addPropertyChangeListener(PropertyChangeListener l) 
            {throw new UnsupportedOperationException();}
            @Override public void removePropertyChangeListener(PropertyChangeListener listener) 
            {throw new UnsupportedOperationException();}

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    invoke.invoke(obj);
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                    Logger.getLogger(Grafika.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
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

    public static void reset() {
        Init.init();
    }
}
