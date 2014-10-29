package rs.luka.biblioteka.funkcije;

import java.io.File;
import java.io.UnsupportedEncodingException;
import static java.net.URLDecoder.decode;
import java.util.Random;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.showMessageDialog;
import rs.luka.biblioteka.data.Config;

/**
 * Razne static metode koje mogu biti korisne u svakom delu programa.
 *
 * @author luka
 * @since 21.8.'14.
 */
public class Utils {

    /**
     * Direktorijum iz koga je program pokrenut.
     */
    private static String workingDir;

    /**
     * Postavlja workingDir prema configu.
     *
     * @since '14. (podela 24.9.'14)
     */
    public static void setWorkingDir() {
        if (Config.hasKey("workingDir")) {
            workingDir = Config.get("workingDir");
        }
    }

    /**
     * Inicijalizuje workingDir na direktorijum .jar fajla. Ako dodje do greske,
     * postavlja workingDir na user.home.
     *
     * @since 24.9.'14. (podela, razdvajanje od setWorkingDir)
     */
    public static void initWorkingDir() {
        String str = Init.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        try {
            String[] path;
            path = str.split("/"); //samo ovo radi za sve OS, ne pokusavati File.separator ili \\\\
            StringBuilder pathBuild = new StringBuilder();
            for (int i = 0; i < path.length - 1; i++) {
                pathBuild.append(path[i]).append(File.separatorChar);
            }
            str = pathBuild.toString();
            String decoded = decode(str, "UTF-8");
            System.out.println(decoded);
            workingDir = str;
            System.out.println("System props:\n" + System.getProperties().toString());
        } catch (UnsupportedEncodingException ex) {
            showMessageDialog(null, "Neuspešno dekodiran String radnog direktorijuma\n"
                    + "Default lokacija je " + System.getProperty("user.home"), "GREŠKA!", JOptionPane.ERROR_MESSAGE);
            workingDir = System.getProperty("user.home");
        }
    }

    /**
     * parseuje boolean iz datog inta uporedjuci ga sa nulom
     *
     * @param vrednost int vrednost
     * @return boolean vrednost
     */
    public static boolean parseBoolean(int vrednost) {
        return vrednost != 0;
    }

    /**
     *
     * @param length duzina stringa
     * @param space mesto na kome stoji razmak
     * @return
     * @since 8-9.'14.
     */
    public static String generateRandomString(int length, int space) {
        String chars = "0123456789QWERTZUIOPŠĐŽASDFGHJKLČĆYXCVBNM";
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < space; i++) {
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        }
        sb.append(' ');
        for (int i = space; i < length; i++) {
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        }
        return sb.toString();
    }

    /**
     * Proverava da li niz intova sadrzi dati int.
     *
     * @param array niz
     * @param value vrednost koja se trazi
     * @return true ako sadrzi, false ako ne sadrzi
     * @since 8.'14.
     */
    public static boolean arrayContains(int[] array, int value) {
        for (int element : array) {
            if (element == value) {
                return true;
            }
        }
        return false;
    }

    /**
     * Proverava da li dati niz Stringova sadrzi trazeni String.
     *
     * @param array niz koji se pretrazuje
     * @param value vrednost koja se trazi
     * @return true ako sadrzi, false u suprotnom
     * @since 24.10.14.
     */
    public static boolean arrayContains(String[] array, String value) {
        for (String element : array) {
            if (element.equals(value)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @return the workingDir
     */
    public static String getWorkingDir() {
        return workingDir;
    }

    /**
     * Vraca index datog elementa u datom nizu. Ako se element ne nalazi u nizu,
     * vraca -1.
     *
     * @param array niz koji se pretrazuje
     * @param element element ciji index je potreban
     * @return index elementa, ili -1
     * @since 6.10.'14.
     */
    public static int getArrayIndex(int[] array, int element) {
        for (int i = 0; i < array.length; i++) {
            if (element == array[i]) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Proverava da li je dati String ceo broj. Ne koristi Exception-e.
     *
     * @param str String koji se proverava
     * @return true ako jeste, false ako nije
     */
    public static boolean isInteger(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        for (int i = 0; i < str.length(); i++) {
            if (i == 0 && str.charAt(i) == '-') {
                if (str.length() == 1) {
                    return false;
                } else {
                    continue;
                }
            }
            if (Character.digit(str.charAt(i), 10) < 0) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Ako se integer val nalazi u izmedju min i max, vraca ga. Ako je veci, vraca max, u suprotnom, vraca min.
     * @param val vrednost koja je ogranicena intervalom (min, max)
     * @param min minimalna vrednost val
     * @param max maximalna vrednost val
     * @return jedna od val, min i max
     */
    public static int limitedInteger(int val, int min, int max) {
        return Integer.max(Integer.min(val, max), min);
    }
    
    /**
     * String wrapper za {@link #limitedInteger(int, int, int) }. Pretpostavlja da je dati String integer.
     * @throws NumberFormatException ako String val nije integer.
     */
    public static String limitedInteger(String val, int min, int max) {
        return String.valueOf(limitedInteger(Integer.valueOf(val), min, max));
    }
}
