package rs.luka.biblioteka.funkcije;

import java.awt.Font;
import java.awt.Point;
import java.io.File;
import java.io.UnsupportedEncodingException;
import static java.net.URLDecoder.decode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.showMessageDialog;
import rs.luka.biblioteka.data.Config;


public class Utils {
    
    private static final Logger LOGGER = Logger.getLogger(Utils.class.getName());

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
            LOGGER.log(Level.FINE, "Postavljen workingDir prema configu");
        }
        LOGGER.log(Level.WARNING, "workingDir nije postavljen prema configu, koristim default");
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
            LOGGER.log(Level.FINE, "Uspešno dekodiran i postavljen workingDir");
        } catch (UnsupportedEncodingException ex) {
            LOGGER.log(Level.SEVERE, "Neuspešno dekodiran String workingDir-a: UnsupportedEncoding."
                    + "Postavljam workingDir na user.home");
            showMessageDialog(null, "Neuspešno dekodiran String radnog direktorijuma\n"
                    + "Default lokacija je " + System.getProperty("user.home"), "GREŠKA!", JOptionPane.ERROR_MESSAGE);
            workingDir = System.getProperty("user.home");
        }
    }

    /**
     * @return the workingDir
     */
    public static String getWorkingDir() {
        return workingDir;
    }

    /**
     * parseuje boolean iz datog inta uporedjuci ga sa nulom
     * @param vrednost int vrednost
     * @return boolean vrednost
     */
    public static boolean parseBoolean(int vrednost) {
        return vrednost != 0;
    }

    /**
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
        LOGGER.log(Level.FINE, "Vraćam random String {0}", sb);
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
                LOGGER.log(Level.FINE, "Dati niz sadrži datu vrednost");
                return true;
            }
        }
        LOGGER.log(Level.FINE, "Dati niz ne sadrži datu vrednost");
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
            if (element.equalsIgnoreCase(value)) {
                LOGGER.log(Level.FINE, "Dati niz sadrži datu vrednost");
                return true;
            }
        }
        LOGGER.log(Level.FINE, "Dati niz ne sadrži datu vrednost");
        return false;
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
                LOGGER.log(Level.FINE, "Index datog elementa u nizu je {0}", i);
                return i;
            }
        }
        LOGGER.log(Level.FINE, "Dati niz ne sadrži datu vrednost");
        return -1;
    }

    /**
     * Proverava da li je dati String ceo broj. Ne koristi Exception-e.
     *
     * @param str String koji se proverava
     * @return true ako jeste, false ako nije
     */
    public static boolean isInteger(String str) {
        if (str == null || str.isEmpty() || str.length() > 11) {
            LOGGER.log(Level.FINE, "{0} nije ceo broj koji staje u int", str);
            return false;
        }
        for (int i = 0; i < str.length(); i++) {
            if (i == 0 && str.charAt(i) == '-') {
                if (str.length() == 1) {
                    LOGGER.log(Level.FINE, "{0} nije ceo broj, sadrži samo minus (prekratak)", str);
                    return false;
                } else {
                    continue;
                }
            }
            if (Character.digit(str.charAt(i), 10) < 0) {
                LOGGER.log(Level.FINE, "{0} nije ceo broj", str);
                return false;
            }
        }
        return true;
    }
    
    /**
     * Pattern po regex-u datom u dokumentaciji,
     */
    private static final Pattern doubleRegex = Pattern.compile(
            "[\\x00-\\x20]*[+-]?(((((\\p{Digit}+)(\\.)?((\\p{Digit}+)?)([eE][+-]?(\\p{Digit}+))?)|(\\.((\\p{Digit}+))([eE][+-]?(\\p{Digit}+))?)|(((0[xX](\\p{XDigit}+)(\\.)?)|(0[xX](\\p{XDigit}+)?(\\.)(\\p{XDigit}+)))[pP][+-]?(\\p{Digit}+)))[fFdD]?))[\\x00-\\x20]*");
    /**
     * Proverava da li je dati broj double. Koristi regex dat iz dokumentacije
     * (prihvata sve vrednosti, ukljucujuci Infinity, NaN i sl).
     * @param str string koji se proverava
     * @return 
     */
    public static boolean isDouble(String str) {
        return doubleRegex.matcher(str).matches();
    }
    
    /**
     * Pretvara listu Point-ova u listu Integer-a tako što uzima x koordinatu, a ignoriše y.
     * @param list lista koja se treba pretvoriti
     * @return x koordinate u listi
     */
    public static ArrayList<Integer> extractXFromPointList(List<Point> list) {
        ArrayList<Integer> ret = new ArrayList<>(list.size());
        list.stream().forEach((e) -> {
            ret.add(e.x);
        });
        LOGGER.log(Level.FINE, "Uspešno ekstraktovao X iz liste Point-a i stavio u listu Integer-a");
        return ret;
    }
    
    /**
     * Prebrojava koliko puta se u datom String-u pojavljuje dati karakter
     * @param string string koji se proverava
     * @param ch karakter koji se trazi
     * @return broj pojavljivanja
     */
    public static int countCharsInString(String string, char ch) {
        int count=0;
        for(int i=0; i<string.length(); i++) {
            if (string.charAt(i)==ch) {
                count++;
            }
        }
        LOGGER.log(Level.FINE, "Broj karaktera {0} u {1} je {2}", new Object[]{ch, string, count});
        return count;
    }
    
    /**
     * Zakljucuje o kojoj tezini fonta se radi na osnovu stringa. BOLD, ITALIC ili BOLD|ITALIC.
     * Default-uje na PLAIN, ako nije nista od prethodna 3
     * @param str string koji se parsuje
     * @return int vrednost iz klase Font
     */
    public static int parseWeight(String str) {
        str = str.toLowerCase();
        if(str.equals("bold"))
            return Font.BOLD;
        if(str.equals("italic"))
            return Font.ITALIC;
        if(str.startsWith("bold") && str.endsWith("italic"))
            return Font.BOLD | Font.ITALIC;
        else
            return Font.PLAIN;
    }
    
    /**
     * DateFormat koriscen za formatiranje vremena za prikaz
     */
    private static final DateFormat df = new SimpleDateFormat("EEE, dd. MM. yyyy. HH:mm:ss");
    /**
     * Vraca dan u nedelji, datum i vreme za prikaz korisniku.
     * @param time broj milisekundi proteklih od 1.1.1970 (Unix era)
     * @return datum za prikaz
     * @since 14.1.'15.
     */
    public static String getHumanReadableTime(long time) {
        return df.format(new Date(time));
    }
    
    public static Object getKey(Map m, Object val) {
        Set<Entry<Object, Object>> set = m.entrySet();
        Entry<Object, Object> next;
        Iterator<Entry<Object, Object>> it = set.iterator();
        while(it.hasNext()) {
            next = it.next();
            if(next.equals(val))
                return next.getKey();
        }
        return null;
    }
}
