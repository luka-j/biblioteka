package rs.luka.biblioteka.funkcije;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.logging.Level;
import rs.luka.biblioteka.data.Config;
import javax.swing.JOptionPane;
import rs.luka.biblioteka.data.Datumi;

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
     * Zaustavlja trenutni Thread , zove metode koje moraju periodicno da se ponavljaju i ponavlja isti proces
     * beskonacno. BLOKIRA TRENUTNI THREAD DO ZAUSTAVLJANJA PROGRAMA !!!
     * @see #autosave() 
     * @see #checkDate() 
     */
    protected static void doPeriodicActions() {
        if (Config.get("savePeriod").equals("0")) {
            LOGGER.log(Level.FINE, "autosave isključen.");
            PeriodicActions.setAutosave(false);
            AUTOSAVE_PERIOD = 1_800_000;
        }
        try {
            AUTOSAVE_PERIOD = (int) (Float.parseFloat(Config.get("savePeriod")) * 60_000);
            LOGGER.log(Level.CONFIG, "autosave period: {0}", AUTOSAVE_PERIOD);
        } catch (NumberFormatException ex) {
            LOGGER.log(Level.SEVERE, "Greška pri parsiranju autosave perioda iz configa", ex);
        }
        PeriodicActions.start(AUTOSAVE_PERIOD);
    }
    
    private static void start(int period) {
        setDateCheckPeriod(0);
        Method[] methods = PeriodicActions.class.getDeclaredMethods();

        try {
            while (true) {
                Thread.sleep(period);
                for (Method method : methods) {
                    if (method.getParameterCount() == 0) {
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
            LOGGER.log(Level.INFO, "Automatski proveravam datum...");
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

    protected static void setAutosave(boolean autosave) {
        AUTOSAVE = autosave;
    }
    
    protected static void setDateCheckPeriod(float period) {
        if(period==0) {
             if(Config.hasKey("datePeriod")) {
                 DATE_CHECK_PERIOD = Float.parseFloat(Config.get("datePeriod"));
             }
             else DATE_CHECK_PERIOD = 1;
        }
        else DATE_CHECK_PERIOD = period;
    }
}
