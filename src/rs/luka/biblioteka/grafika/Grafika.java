//1645 linija, 24.8.'14.
//1994 linije, 24.9.'14.
//2141 linija, 25.10.'14.
//2570 linija, 29.11.'14.
//3000 linija, 25.12.'14. (dodavanje UVButton)
//3432 linija, 23.1.'15. (auto. Strings, SmallButton, ICheckbox cleanup)
//3440 linija, 19.2.'15.
package rs.luka.biblioteka.grafika;

import java.awt.Color;
import java.awt.Font;
import java.awt.Window;
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
import rs.luka.biblioteka.funkcije.Utils;

/**
 *
 * @author luka
 */
public class Grafika {

    private static final java.util.logging.Logger LOGGER
            = java.util.logging.Logger.getLogger(Grafika.class.getName());

    /**
     * Font koji se koristi za JLabele. Menja se samo preko configa i zvanjem
     * {@link #setVariables} metode za refresh.
     */
    private static Font labelFont;
    /**
     * Font koji se koristi za veliku dugmad (sve osim UVButton).
     */
    private static Font largeButtonFont;
    /**
     * Font koji se koristi za UVButton-e (i svu ostalu malu dugmad).
     */
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
        smallButtonFont = new Font("Dialog", Font.BOLD, 12);
        bgColor = new Color(40, 255, 40, 220); //zelena
        fgColor = new Color(0); //crna
        TFColor = new Color(-1); //bela
    }
    /**
     * Ucitava podatke vezane za grafiku (lookAndFeel, boje i fontove).
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
        if (Config.hasKey("labelFontName")) {
            if (Config.hasKey("labelFontSize")) {
                if (Config.hasKey("labelFontWeight")) {
                    labelFont = new Font(Config.get("labelFontName"),
                            Utils.parseWeight(Config.get("labelFontWeight")), Config.getAsInt("labelFontSize"));
                } else {
                    labelFont = new Font(Config.get("labelFontName"), labelFont.getStyle(),
                            Config.getAsInt("labelFontSize"));
                }
            } else {
                labelFont = new Font(Config.get("labelFontName"), labelFont.getStyle(), labelFont.getSize());
            }
        }
        if (Config.hasKey("butFontName")) {
            if (Config.hasKey("butFontSize")) {
                if (Config.hasKey("butFontWeight")) {
                    largeButtonFont = new Font(Config.get("butFontName"),
                            Utils.parseWeight(Config.get("butFontWeight")), Config.getAsInt("butFontSize"));
                } else {
                    largeButtonFont = new Font(Config.get("butFontName"), largeButtonFont.getStyle(),
                            Config.getAsInt("butFontSize"));
                }
            } else {
                largeButtonFont = new Font(Config.get("butFontName"), largeButtonFont.getStyle(), 
                        largeButtonFont.getSize());
            }
        }
        if (Config.hasKey("smallButFontName")) {
            if (Config.hasKey("smallButFontSize")) {
                if (Config.hasKey("smallButFontWeight")) {
                    smallButtonFont = new Font(Config.get("smallButFontName"),
                            Utils.parseWeight(Config.get("smallButFontWeight")),
                            Config.getAsInt("smallButFontSize"));
                } else {
                    smallButtonFont = new Font(Config.get("smallButFontName"), smallButtonFont.getStyle(), 
                            Config.getAsInt("smallButFontSize"));
                }
            } else {
                smallButtonFont = new Font(Config.get("smallButFontName"), smallButtonFont.getStyle(), 
                        smallButtonFont.getSize());
            }
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
            showMessageDialog(null, Init.dData.LOADLNF_EX_MSG_STRING, Init.dData.LOADLNF_EX_TITLE_STRING, JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Generise praznu akciju, koja na actionPerformed() izvrsava metodu invoke na objektu obj.
     * @param invoke metoda koju treba izvrsiti
     * @param obj objekat ciju metodu treba izvrsiti
     * @return prazna akcija sa opisanom actionPerformed() metodom
     * @deprecated nepotrebna
     */
    protected static Action generateEmptyResetAction(Method invoke, Object obj) {
        return new Action() {
            @Override public Object getValue(String key) {return null;}
            @Override public void putValue(String key, Object value) {throw new UnsupportedOperationException();}
            @Override public void setEnabled(boolean b) {}
            @Override public boolean isEnabled() {return true;}
            @Override public void addPropertyChangeListener(PropertyChangeListener l) {
                throw new UnsupportedOperationException();}
            @Override public void removePropertyChangeListener(PropertyChangeListener listener) {
                throw new UnsupportedOperationException();}
            
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    invoke.invoke(obj);
                    new Ucenici().pregledUcenika();
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                    Logger.getLogger(Grafika.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
    }

    protected static Font getLabelFont() {
        return labelFont;
    }

    protected static Font getButtonFont() {
        return largeButtonFont;
    }

    protected static Font getSmallButtonFont() {
        return smallButtonFont;
    }

    protected static Color getBgColor() {
        return bgColor;
    }

    protected static Color getFgColor() {
        return fgColor;
    }

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
     * Prikazuje dijalog za cuvanje podataka i zove
     * {@link Init#exit(boolean) exit} sa odgovarajucim argumentom.
     */
    protected static void exit() {
        LOGGER.log(Level.FINE, "Iniciram zatvaranje aplikacije i "
                + "prikazujem dijalog za čuvanje podataka.");
        String[] opcije = {Init.dData.DA_STRING, Init.dData.NE_STRING};
        int sacuvaj = showOptionDialog(null, Init.dData.EXIT_MSG_STRING, Init.dData.EXIT_TITLE_STRING, 
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, opcije, opcije[0]); 
        //0 za da, 1 za ne, -1 za X
        if (sacuvaj != Init.dData.INVALID) {
            for(Window win : Window.getWindows())
                win.dispose(); //workaround
            Init.exit(sacuvaj==0);
        }
        else
            LOGGER.log(Level.FINE, "Izlaz otkazan. Ostajem u aplikaciji.");
    }

    private Grafika() {
    }
}
