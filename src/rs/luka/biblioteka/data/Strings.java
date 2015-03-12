package rs.luka.biblioteka.data;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import static rs.luka.biblioteka.funkcije.Init.dData;
import rs.luka.biblioteka.funkcije.Utils;
import rs.luka.biblioteka.grafika.Konstante;

/**
 * Klasa koja radi inicijalizaciju stringova iz posebnog fajla (strings.properties).
 * Pogledati {@link #stringsMsg} za detaljan opis.
 * @author luka
 * @since 30.12.'14.
 */
public class Strings {
    /**
     * Properties u kojem se nalaze sve vrednosti. Na kraju postavljanja se clear-uje.
     */
    private static final Properties strings = new Properties();
    /**
     * Fajl odakle se učitavaju stringovi (strings.properties).
     */
    private static File stringsFile = null;
    /**
     * Poruka sa opisom funkcije ovog fajla i sintaksom. Hteo sam da je uključim u fajl, ali
     * za sada mi je store() cele mape preskup za tu namenu, a ne koristim ga nigde unutar programa.
     */
    private static final String stringsMsg = "Fajl sadrži sve stringove koji se koristi u programu. "
            + "Overriduje default vrednosti na srpskom. Može se koristiti za prevode ili "
            + "prepravke postojećih poruka. Ne overriduje vrednosti zadate u configu. Ne zahteva "
            + "_STRING na kraju, niti k_ (ili kPrefix) na početku, za razliku od configa. "
            + "Ključevi su uglavnom u formatu IMEKLASE_IMEKOMPONENTE ili IMEKLASE_*EX_MSG ili IMEKLASE_*EX_TITLE"
            + " gde *EX označava prva slova Exceptiona za koji se prikazuje poruka o grešci, a reč "
            + "Exception, ako postoji, se izostavlja (npr. NumberFormatException postaje NFEX ili "
            + "VrednostNePostoji postaje VNPEX). Ako je reč o *Utils klasama (UceniciUtils, KnjigeUtils),"
            + "IMEKLASE predstavlja ime metode u kojoj se string koristi. Za Config klasu (opisi podešavanja) "
            + "sintaksa je CONFIG_PROPERTY[_DESC] (_DESC deo nije obavezan), gde je PROPERTY originalan"
            + "naziv ključa u configu. Prihvata . (tačku) umesto _ (donje crte). Za sva tačna imena "
            + "pogledati Konstante.java, tražiti polja deklarisana sa (public) static String.";
    
    /**
     * Ako {@link #load()} vrati {@link Boolean#TRUE}, radi {@link #set()} i {@link #close()}, u
     * suprotnom tiho izlazi i ignoriše poziv.
     */
    public static void loadStrings() {
        if(load()) {
            set();
            close();
        }
    }
     /**
      * Učitava podatke iz fajla u {@link #strings}. Ako fajl ne postoji ili dođe do greške,
      * vraća false, u suprotnom vraća true.
      * @return true ili false, u zavisnosti od uspeha operacije.
      */
    private static boolean load() {
        String usersetPath = Config.get("stringsPath");
        if(usersetPath != null && !usersetPath.isEmpty()) {
            if(Utils.fileExists(usersetPath))
                stringsFile = new File(usersetPath);
            else if(Utils.fileExists(Utils.getWorkingDir() + usersetPath))
                stringsFile = new File(Utils.getWorkingDir() + usersetPath);
        }
        if(stringsFile==null) {
            if(Utils.fileExists(Utils.getWorkingDir() + "strings.properties"))
                stringsFile = new File(Utils.getWorkingDir() + "strings.properties");
            else if(Utils.fileExists("strings.properties"))
                stringsFile = new File("strings.properties");
            else return false;
        }
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
    
    /**
     * Radi iteraciju kroz {@link #strings}, dodaje _DESC i _STRING po potrebi, zamenjuje
     * tačke donjim crtama i zove {@link Konstante#set(java.lang.String, java.lang.String)}
     * sa datim vrednostima.
     */
    private static void set() {
        strings.forEach((Object key, Object val) -> {
            String keyStr = (String)key;
            keyStr = keyStr.toUpperCase().replace('.', '_');
            if(!keyStr.endsWith("_STRING"))
                keyStr = keyStr.concat("_STRING");
            dData.set(keyStr, (String)val);
        });
    }
    
    /**
     * Clearuje {@link #strings}. Nema veliku upotrebu, tu je da očisti memoriju i ako zatreba
     * još neka završna operacija. 
     */
    private static void close() {
        strings.clear();
    }
}
