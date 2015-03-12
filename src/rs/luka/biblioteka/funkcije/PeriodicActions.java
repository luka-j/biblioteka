package rs.luka.biblioteka.funkcije;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.logging.Level;
import rs.luka.biblioteka.data.Config;
import javax.swing.JOptionPane;

/**
 *
 * @author luka
 */
public class PeriodicActions {

    private static final java.util.logging.Logger LOGGER
            = java.util.logging.Logger.getLogger(PeriodicActions.class.getName());

    /**
     * Oznacava da li je autosave ukljucen, tj da li je {@link #AUTOSAVE_PERIOD}
     * > 0.
     */
    private static boolean AUTOSAVE = true;
    /**
     * Vreme pokretanja programa.
     */
    private static Date LAST_DATE_CHECK = new Date();
    /**
     * Period na koji se proverava datum, u danima.
     */
    private static float DATE_CHECK_PERIOD = 1;
    /**
     * Broj milisekundi u danu.
     */
    private static final int MS_U_DANU = 86_400_000;
    /**
     * Period na koji se podaci automatski cuvaju, u milisekundama.
     */
    private static int AUTOSAVE_PERIOD;

    /**
     * Radi inicijalizaciju klase (postavlja promenljive po default-u ili config-u
     * i zove {@link #start(int)}. BLOKIRA THREAD JER RADI BESKONACNO.
     *
     * @see #start(int)
     */
    protected static void doPeriodicActions() {
        setDateCheckPeriod(-1);
        if (Config.get("savePeriod").equals("0")) {
            LOGGER.log(Level.FINE, "autosave isključen.");
            AUTOSAVE = false;
            AUTOSAVE_PERIOD = (int) (DATE_CHECK_PERIOD * MS_U_DANU);
        } else {
            try {
                AUTOSAVE_PERIOD = (int) (Float.parseFloat(Config.get("savePeriod")) * 60_000);
                LOGGER.log(Level.CONFIG, "autosave period: {0}", AUTOSAVE_PERIOD);
            } catch (NumberFormatException ex) {
                LOGGER.log(Level.SEVERE, "Greška pri parsiranju autosave perioda iz configa", ex);
            }
        }
        PeriodicActions.start(AUTOSAVE_PERIOD);
    }

    /**
     * Startuje periodicne akcije. Koristi while(true) petlju (beskonacnu) unutar koje
     * se nalazi Thread.sleep(period). Koristi reflekciju da pozove sve metode ove klase
     * koje ne primaju parametre i ne pocinju sa "do" ili "setConfigEntry".
     * BLOKIRA THREAD JER RADI BESKONACNO.
     * 
     * @param period period na koji se periodicne akcije ponavljaju
     */
    private static void start(int period) {
        Method[] methods = PeriodicActions.class.getDeclaredMethods();

        try {
            while (true) {
                Thread.sleep(period);
                LOGGER.log(Level.INFO, "Radim periodične akcije...");
                for (Method method : methods) {
                    if (method.getParameterCount() == 0 && !method.getName().startsWith("do")
                                                        && !method.getName().startsWith("set")) {
                        method.invoke(null);
                    }
                }
            }
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | InterruptedException ex) {
            LOGGER.log(Level.SEVERE, "Greška pri pozivanju periodičnih akcija");
        }
    }

    /**
     * Proverava vreme poslednje provere (ili pokretanja programa ako ih nije
     * bilo) i ako je veće od jednog dana, zove funkciju za proveru datuma. Ako
     * ne, normalno izlazi.
     */
    private static void checkDate() {
        if (new Date().after(new Date(LAST_DATE_CHECK.getTime() + (int) (DATE_CHECK_PERIOD * MS_U_DANU)))) {
            LOGGER.log(Level.FINE, "Automatski proveravam datum...");
            Datumi.proveriDatum();
            LAST_DATE_CHECK = new Date();
        }
    }

    /**
     * Pokušava čuvanje podataka. Ako je neuspešno, prikazuje poruku o grešci, i
     * izlazi (radi exception-handling unutar metode)
     */
    private static void autosave() {
        try {
            if (AUTOSAVE) {
                Save.save();
                LOGGER.log(Level.FINE, "Automatski sačuvao podatke");
            }
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Gre\u0161ka pri automatskom \u010duvanju podataka", ex);
            JOptionPane.showMessageDialog(null, "Gre\u0161ka pri automatskom \u010duvanju podataka", "I/O greska", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Postavlja {@link #DATE_CHECK_PERIOD} prema config-u ako je parametar -1 ili
     * prema prosledjenom floatu ako nije.
     * 
     * @param period period, u danima, na koji se proverava datum iznajmljivanja knjige za svakog ucenika
     */
    protected static void setDateCheckPeriod(float period) {
        if (period == -1) {
            DATE_CHECK_PERIOD = Float.parseFloat(Config.get("datePeriod", "1"));
        } else {
            DATE_CHECK_PERIOD = period;
        }
    }
}
