package rs.luka.biblioteka.data;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import rs.luka.biblioteka.funkcije.Utils;
import rs.luka.biblioteka.grafika.Konstante;

/**
 *
 * @author luka
 */
public class Strings {
    private static final Properties strings = new Properties();
    private static File stringsFile;
    private static final String stringsMsg = "Fajl sadrži sve stringove koji se koristi u programu. "
            + "Overriduje default vrednosti na srpskom. Može se koristiti za prevode ili "
            + "prepravke postojećih poruka. Overriduje vrednosti zadate u configu. Ne zahteva "
            + "_STRING na kraju, niti k_ (ili kPrefix) na početku, za razliku od configa. "
            + "Ključevi su uglavnom u formatu IMEKLASE_IMEKOMPONENTE ili IMEKLASE_*EX_MSG ili IMEKLASE_*EX_TITLE"
            + " gde *EX označava prva slova Exceptiona za koji se prikazuje poruka o grešci, a reč "
            + "Exception, ako postoji, se izostavlja (npr. NumberFormatException postaje NFEX ili "
            + "VrednostNePostoji postaje VNPEX). Ako je reč o *Utils klasama (UceniciUtils, KnjigeUtils),"
            + "IMEKLASE predstavlja ime metode u kojoj se string koristi. Za sva tačna imena pogledati"
            + "Konstante.java, tražiti polja deklarisana sa static String.";
    
    public static void loadStrings() {
        if(load()) {
            set();
            close();
        }
    }
    
    private static boolean load() {
        stringsFile = new File(Utils.getWorkingDir() + "strings.properties");
        if(!stringsFile.exists() || stringsFile.length()==0)
            return false;
        FileReader configFR;
        try {
            configFR = new FileReader(stringsFile);
            strings.load(configFR);
            return true;
        } catch (IOException ex) {
            Logger.getLogger(Strings.class.getName()).log(Level.WARNING, "Greška pri čitanju stringova", ex);
            return false;
        }
    }
    
    private static void set() {
        strings.forEach((Object key, Object val) -> {
            String keyStr = (String)key;
            keyStr = keyStr.toUpperCase().replace('_', '.');
            if(!keyStr.endsWith("_STRING")) {
                keyStr = keyStr.concat("_STRING");
            }
            Konstante.set(keyStr, (String)val);
        });
    }
    
    private static void close() {
        strings.clear();
    }
}
