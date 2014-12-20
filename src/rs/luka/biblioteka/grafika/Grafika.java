//1645 linija, 24.8.'14.
//1994 linije, 24.9.'14.
//2141 linija, 25.10.'14.
//2570 linija, 29.11.'14.
//2953 linija, 20.12.'14. (trenutno, dodavanje UVButton)
package rs.luka.biblioteka.grafika;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import static java.lang.Integer.parseInt;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Action;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.showMessageDialog;
import static javax.swing.JOptionPane.showOptionDialog;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import static javax.swing.UIManager.getCrossPlatformLookAndFeelClassName;
import static javax.swing.UIManager.getSystemLookAndFeelClassName;
import static javax.swing.UIManager.setLookAndFeel;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.metal.DefaultMetalTheme;
import static javax.swing.plaf.metal.MetalLookAndFeel.setCurrentTheme;
import rs.luka.biblioteka.data.Config;
import rs.luka.biblioteka.funkcije.Init;
import static rs.luka.biblioteka.funkcije.Utils.parseBoolean;

/**
 *
 * @author luka
 */
public class Grafika {

    private static final java.util.logging.Logger LOGGER = 
            java.util.logging.Logger.getLogger(Grafika.class.getName());

    /**
     * Font koji se koristi za JLabele. Menja se samo preko configa i zvanjem
     * {@link #setVariables} metode za refresh.
     */
    private static Font labelFont;
    /**
     * Font koji se koristi za veliku dugmad (sve osim UVButton).
     */
    private static Font largeButtonFont;
    private static Font smallButtonFont;
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

    static {
        labelFont = new Font("Segoe UI", Font.PLAIN, 15);
        largeButtonFont = new Font("Arial Bold", Font.PLAIN, 13);
        smallButtonFont = new Font("Arial", Font.BOLD, 12);
        bgColor = new Color(40, 255, 40, 220); //zelena
        fgColor = new Color(0); //crna
        TFColor = new Color(-1); //bela
    }

    /**
     * Iscrtava glavni prozor. Pri zatvaranju zove
     * {@link rs.luka.biblioteka.funkcije.Init#exit}
     */
    public static void initGrafika() {
        loadLnF();
        setVariables();
    }

    /**
     * Podesava promenljive iz ove klase prema configu.
     */
    protected static void setVariables() {
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
        if(Config.hasKey("labelFontName")) {
            if(Config.hasKey("labelFontSize"))
                labelFont = new Font(Config.get("labelFontName"), Font.PLAIN, Config.getAsInt("labelFontSize"));
            else
                labelFont = new Font(Config.get("labelFontName"), Font.PLAIN, labelFont.getSize());
        }
        if(Config.hasKey("butFontName")) {
            if(Config.hasKey("butFontSize"))
                largeButtonFont = new Font(Config.get("butFontName"), Font.PLAIN, Config.getAsInt("butFontSize"));
            else
                largeButtonFont = new Font(Config.get("butFontName"), Font.PLAIN, labelFont.getSize());
        }
        if(Config.hasKey("smallButFontName")) {
            if(Config.hasKey("smallButFontSize"))
                smallButtonFont = new Font(Config.get("smallButFontName"), Font.PLAIN, Config.getAsInt("smallButFontSize"));
            else
                smallButtonFont = new Font(Config.get("smallButFontName"), Font.PLAIN, labelFont.getSize());
        }
    }

    /**
     * Ucitava temu (Look and Feel).
     */
    protected static void loadLnF() {
        System.out.println("system LaF: " + getSystemLookAndFeelClassName());
        System.out.println("cross LaF: " + getCrossPlatformLookAndFeelClassName());
        System.out.println("sve");
        for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
            System.out.println(info.getName() + "\t" + info.getClassName());
        }
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
                    break;
                case "win classic":
                    setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel");
            }
            LOGGER.log(Level.CONFIG, "lookAndFeel: {0}", UIManager.getLookAndFeel());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            LOGGER.log(Level.SEVERE, "Greška pri postavljanju teme (Look and Feel).", ex);
            showMessageDialog(null, "Došlo je do greške pri postavljanju teme.",
                    "LookAndFeel greška", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     *
     * @param invoke
     * @param obj
     * @return
     */
    protected static Action generateEmptyResetAction(Method invoke, Object obj) {
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
                    new Ucenici();
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

    protected static Font getButtonFont() {
        return largeButtonFont;
    }
    
    protected static Font getSmallButtonFont() {
        return smallButtonFont;
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

    /**
     *
     * @return
     */
    protected static Color getTFColor() {
        return TFColor;
    }
    
    /**
     * Zatvara prozor sa ucenicima i ponovo poziva {@link Init#init() init}.
     */
    public static void reset() {
        Ucenici.close();
        Init.init();
    }
    
    /**
     * Prikazuje dijalog za cuvanje podataka i zove {@link Init#exit(boolean) exit}
     * sa odgovarajucim argumentom.
     */
    protected static void exit() {
        LOGGER.log(Level.FINE, "Iniciram zatvaranje aplikacije i "
                        + "prikazujem dijalog za čuvanje podataka.");
        String[] opcije = {"Da", "Ne"};
        int sacuvaj = showOptionDialog(null, "Sačuvati izmene?",
                "Izlaz", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, opcije, opcije[0]); //0 za da, 1 za ne, -1 za X
        if (sacuvaj != -1) {
            Init.exit(!parseBoolean(sacuvaj)); //0 oznacava false (sacuvati), a 1 true (brisati)
            //drugim recima, cuva samo ako je odabrana prva opcija
        }
        LOGGER.log(Level.FINE, "Izlaz otkazan. Ostajem u aplikaciji.");
    }

    private Grafika() {
    }
}
