package rs.luka.biblioteka.data;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.showMessageDialog;
import static rs.luka.biblioteka.data.Strings.loadStrings;
import rs.luka.biblioteka.exceptions.ConfigException;
import rs.luka.biblioteka.funkcije.Utils;
import rs.luka.biblioteka.grafika.Konstante;
import static rs.luka.biblioteka.grafika.Konstante.*;


public class Config {
    private static final java.util.logging.Logger LOGGER = 
            java.util.logging.Logger.getLogger(Config.class.getName());


    /**
     * Properties.
     */
    private static Properties config;
    /**
     * Default vrednosti za config.
     */
    private static final Properties defaults = new Properties();
    /**
     * Fajl odakle se ucitava config.
     */
    private static File configFile;
    /**
     * Komentar za config fajl, koji se upisuje na pocetku fajla.
     */
    private static final String configMsg = "firstRun - true ili false, oznacava da li se "
            + "program pokrece po prvi put. Ako da, otvara prozor za unos podataka.\n"
            + "dateLimit - broj dana koliko ucenik moze da zadrzi knjigu kod sebe. Default je 14\n"
            + "lookAndFeel - generalni izgled prozora i grafickih komponenti. Vrednosti:"
            + "system, ocean, metal, nimbus, motif. Izbegavati nimbus i motif\n"
            + "brKnjiga - maksimalan broj knjiga koje ucenik moze da ima kod sebe\n"
            + "bgBoja - RGB vrednost, pozadinska boja svih prozora\n"
            + "fgBoja - RGB vrednost, boja fonta\n"
            + "TFColor - RGB vrednost, boja polja za unos teksta\n"
            + "logLevel - minimalan nivo poruka koje se loguju. Default je INFO\n"
            + "savePeriod - broj minuta na koji se vrsi automatsko cuvanje podataka\n"
            + "maxUndo - maksimalan broj akcija koje se nalaze u undo stack-u\n"
            + "razredi - String validnih razreda, razdvojenih zapetom\n"
            + "workingDir - radni direktorijum aplikacije\n"
            + "logSizeLimit i logCount - broj i velicina log fajla (fajlova)\n"
            + "label,but i smallBut Font Name/Size/Weight - osobine fontova koriscenih za labele i dugmad\n"
            + "kPrefix - prefix za kljuceve koji oznacavaju da se vrednost odnosi na konstantu,"
            + "default je k_\n"
            + "ucSort - metoda sortiranja ucenika. Default je po razredu (razred/raz), moze i po imenu (ime)\n"
            + "shiftKnjige - odredjuje da li se pri vracanju sve knjige pomeraju ulevo, tako da knjige"
            + "koje se trenutno nalaze kod ucenika popunjavaju prva prazna mesta (poravnate ulevo)\n"
            + "customSize - oznacava da li se velicina prozora odredjuje automatski ili putem konstanti\n"
            + "kazna - kazna za cuvanje knjige predugo, povecava se dnevno (0 za iskljuceno)";
    /**
     * Prefix za konstante. Default je k_, ucitava se odmah po ucitavanju config-a
     */
    private static String KONSTANTE_PREFIX = "k_";
    /**
     * Tipovi podataka ili validne vrednosti za svaki kljuc. Sadrzi sve moguce kljuceve sem konstanti
     * @since 16.1.'14.
     */
    private static final StringMultiMap types = new StringMultiMap();
    /**
     * Opisi kljuceva za prikaz korisniku. Koristi Stringove iz Konstanti.
     * @since 16.1.'14.
     */
    private static final Map<String, String> descriptions = new HashMap<>();
    /**
     * Limiti za sve vrednosti.
     */
    private static final Map<String, Limit> limiti = new HashMap<>();

    //LIMITI ZA CONFIG
    private static final Limit SIRINA = new Limit(100, 20_000);
    private static final Limit VISINA = new Limit(70, 10_000);
    private static final Limit BR_KNJIGA = new Limit(1, 30);
    private static final Limit UC_KNJ_SIZE = new Limit(30);
    private static final Limit DATE_LIMIT = new Limit(1, 365);
    private static final Limit SAVE_PERIOD = new Limit(0, 1440); //1 dan
    private static final Limit UNDO = new Limit();
    private static final Limit LOG_SIZE = new Limit(0, 100_000_000);
    private static final Limit LOG_COUNT = new Limit(0, 10_000);
    private static final Limit LABEL_FONT = new Limit(1, 50);
    private static final Limit BUTTON_FONT = new Limit(1, 30);
    private static final Limit DATE_CHECK_LIMIT = new Limit(0, 30); //1 mesec
    private static final Limit KAZNA_LIMIT = new Limit(0, 1000); //1000 dinara dnevno, vise nego dovoljno

    /**
     * Ucitava config iz fajla u Properties.
     */
    public static void loadConfig() {
        setDefaults();
        loadStrings();
        setDescriptions();
        setLimits();
        setTypes();
        configFile = new File(Utils.getWorkingDir() + "config.properties");
        config = new Properties(defaults);
        String path = null;
        try {
            configFile.createNewFile();
            path = configFile.getCanonicalPath();
            FileReader configFR = new FileReader(configFile);
            Config.config.load(configFR);
            setKPrefix();
            setKonstante();
        } catch (FileNotFoundException ex) {
            showMessageDialog(null, LOADCONFIG_FNFEX_MSG_STRING + path, LOADCONFIG_FNFEX_TITLE_STRING, 
                    ERROR_MESSAGE);
        } catch (IOException ex) {
            showMessageDialog(null, LOADCONFIG_IOEX_MSG_STRING, LOADCONFIG_IOEX_TITLE_STRING, ERROR_MESSAGE);
        }
    }

    /**
     * Podesava hard-coded default vrednosti.
     */
    private static void setDefaults() {
        defaults.setProperty("knjSize", "300");
        defaults.setProperty("ucSize", "550");
        defaults.setProperty("firstRun", "true");
        defaults.setProperty("dateLimit", "14");
        defaults.setProperty("lookAndFeel", "ocean");
        defaults.setProperty("brKnjiga", "3");
        defaults.setProperty("TFBoja", "false");
        defaults.setProperty("logLevel", "INFO");
        defaults.setProperty("savePeriod", "5");
        defaults.setProperty("maxUndo", "50");
        defaults.setProperty("logSizeLimit", "10000000");
        defaults.setProperty("logFileCount", "15");
    }
    
    /**
     * Podesava opise kljuceva za prikaz korisniku.
     * @see #descriptions
     * @since 16.1.'14.
     */
    private static void setDescriptions() {
        descriptions.put("razredi", CONFIG_RAZREDI_DESC);
        descriptions.put("brKnjiga", CONFIG_BRKNJIGA_DESC);
        descriptions.put("dateLimit", CONFIG_DATELIMIT_DESC);
        descriptions.put("workingDir", CONFIG_WORKINGDIR_DESC);
        descriptions.put("savePeriod", CONFIG_SAVEPERIOD_DESC);
        descriptions.put("datePeriod", CONFIG_DATEPERIOD_DESC);
        descriptions.put("logLevel", CONFIG_LOGLEVEL_DESC);
        descriptions.put("logSizeLimit", CONFIG_LOGSIZELIMIT_DESC);
        descriptions.put("logFileCount", CONFIG_LOGFILECOUNT_DESC);
        descriptions.put("lookAndFeel", CONFIG_LOOKANDFEEL_DESC);
        descriptions.put("labelFontName", CONFIG_LABELFONTNAME_DESC);
        descriptions.put("butFontName", CONFIG_BUTFONTNAME_DESC);
        descriptions.put("smallButFontName", CONFIG_SMALLBUTFONTNAME_DESC);
        descriptions.put("kazna", CONFIG_KAZNA_DESC);
    }

    /**
     * Postavlja limite u mapi koristeći vrednosti iz fieldova.
     *
     * @since 11.'14
     * @see #limiti
     */
    private static void setLimits() {
        limiti.put("ucSize", UC_KNJ_SIZE);
        limiti.put("knjSize", UC_KNJ_SIZE);
        limiti.put("dateLimit", DATE_LIMIT);
        limiti.put("knjigeS", SIRINA);
        limiti.put("knjigeV", VISINA);
        limiti.put("uceniciS", SIRINA);
        limiti.put("uceniciV", VISINA);
        limiti.put("brKnjiga", BR_KNJIGA);
        limiti.put("savePeriod", SAVE_PERIOD);
        limiti.put("maxUndo", UNDO);
        limiti.put("logSizeLimit", LOG_SIZE);
        limiti.put("logFileCount", LOG_COUNT);
        limiti.put("labelFontSize", LABEL_FONT);
        limiti.put("butFontSize", BUTTON_FONT);
        limiti.put("smallButFontSize", BUTTON_FONT);
        limiti.put("datePeriod", DATE_CHECK_LIMIT);
        limiti.put("kazna", KAZNA_LIMIT);
    }

    /**
     * Postavlja konstante. Overriduje Strings.
     * @since 16.1.'14.
     */
    private static void setKonstante() {
        config.entrySet().stream().filter((entry) -> (entry.getKey().toString().startsWith(KONSTANTE_PREFIX)))
              .forEach((Entry konstanta) -> (set(konstanta.getKey().toString(), konstanta.getValue().toString())));
    }
    
    /**
     * Postavlja prefix za konstante, ako postoji u configu. Raditi pre resolveKeys.
     */
    private static void setKPrefix() {
        if(config.containsKey("kPrefix"))
            KONSTANTE_PREFIX = config.getProperty("kPrefix");
    }
    
    /**
     * Podesava tipove podataka za sve kljuceve. Ako su moguce samo odredjene vrednosti,
     * ubacuje te vrednosti umesto konkretnog tipa u mapu.
     * @see #types
     * @since 16.1.'14.
     */
    private static void setTypes() {
        types.put("ucSize", "int");
        types.put("knjSize", "int");
        types.put("firstRun", "boolean");
        types.put("dateLimit", "int");
        types.put("lookAndFeel", "ocean", "metal", "system", "nimbus", "motif", "win classic");
        types.put("brKnjiga", "int");
        types.put("bgBoja", "color");
        types.put("fgBoja", "color");
        types.put("TFBoja", "boolean");
        types.put("TFColor", "color");
        types.put("logLevel", "level");
        types.put("savePeriod", "int");
        types.put("maxUndo", "int");
        types.put("razredi", "delimitedInts");
        types.put("workingDir", "url");
        types.put("logSizeLimit", "int");
        types.put("logFileCount", "int");
        types.put("labelFontName", "string");
        types.put("labelFontSize", "int");
        types.put("labelFontWeight", "plain", "bold", "italic", "bold italic");
        types.put("butFontName", "string");
        types.put("butFontSize", "int");
        types.put("butFontWeight", "plain", "bold", "italic", "bold italic");
        types.put("smallButFontName", "string");
        types.put("smallButFontSize", "int");
        types.put("smallButFontWeight", "plain", "bold", "italic", "bold italic");
        types.put("kPrefix", "string");
        types.put("ucSort", "ime", "razred", "raz");
        types.put("datePeriod", "double");
        types.put("shiftKnjige", "boolean");
        types.put("customSize", "boolean");
        types.put("kazna", "int");
    }

    /**
     * Cuva config u fajl.
     */
    private static void storeConfig() {
        try {
            config.store(new FileWriter(configFile), configMsg);
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "I/O greška pri čuvanju konfiguracijskog fajla", ex);
            showMessageDialog(null, STORECONFIG_IOEX_MSG_STRING, STORECONFIG_IOEX_TITLE_STRING, 
                    ERROR_MESSAGE);
        }
    }

    /**
     * Proverava da li config sadrzi dati kljuc. Case-sensitive
     *
     * @param key kljuc koji se trazi
     * @return true ako sadrzi, false u suprotnom
     * @since 25.10'.14.
     */
    public static boolean hasKey(String key) {
        return config.containsKey(key);
    }

    /**
     * Vraca vrednost koja je povezana sa ovim kljucem. Ako ne
     * postoji, vraca default vrednost. Ako ni default ne postoji, vraca null.
     * Case-sensitive
     *
     * @param key kljuc ili sinonim
     * @return String vrednost
     */
    public static String get(String key) {
        if (key == null) {
            return null;
        }
        return config.getProperty(key);
    }

    /**
     * Vraca vrednost koja je povezana sa ovim kljucem. Ako ne
     * postoji, vraca def. Case-sensitive
     *
     * @param key kljuc ili sinonim
     * @param def default vrednost
     * @return vrednost koja je povezana sa datim kljucem ili sinonimom ili def.
     */
    public static String get(String key, String def) {
        return config.getProperty(key, def);
    }

    /**
     * Vraca integer reprezentaciju trazenog kljuca ili.
     *
     * @param key kljuc koji se trazi
     * @return vrednost kljuca kao int.
     * @throws NumberFormatException ako vrednost nije int
     * @see #get(java.lang.String) 
     * @since 25.10'.14.
     */
    public static int getAsInt(String key) {
        String val = get(key);
        if(val==null)
            return 0;
        return Integer.parseInt(val);
    }

    /**
     * Vraca integer reprezentaciju trazenog kljuca. Ako ne postoji, vraca default
     * u int obliku.
     * @param key kljuc cija se vrednost trazi
     * @param def default vrednost
     * @return odgovarajuca vrednost za kljuc
     * @see #get(java.lang.String, java.lang.String) 
     */
    public static int getAsInt(String key, String def) {
        return Integer.parseInt(get(key, def));
    }

    /**
     * Vraca boolean reprezentaciju trazenog kljuca. Ako
     * je vrednost kljuca int, koristi {@link Utils#parseBoolean(int)} da dobije
     * boolean, u suprotnom uporedjuje String sa "true".
     *
     * @param key kljuc koji se trazi ili sinonim.
     * @return boolean koji se dobija na opisani nacin
     * @see #get(java.lang.String) 
     * @since 25.10'.14.
     */
    public static boolean getAsBool(String key) {
        String val = get(key);
        if (val == null) {
            return false;
        }
        if (Utils.isInteger(val)) {
            return Utils.parseBoolean(Integer.parseInt(val));
        }
        return val.equalsIgnoreCase("true");
    }

    /**
     * Postavlja dati kljuc na datu vrednost, ako su oba validna. Ignorise
     * veliko/malo slovo.
     *
     * @param key kljuc
     * @param val vrednost
     * @see #isNameValid(java.lang.String, java.lang.String)
     * @see #checkValue(java.lang.String, java.lang.String) 
     * @since 25.10.'14.
     */
    public static void set(String key, String val) {
        if (key.startsWith(KONSTANTE_PREFIX)) {
            Konstante.set(key.substring(2), val);
            return;
        }
        
        checkValue(key, val); //throwuje ConfigException-e (koji su Runtime)
        if (!isNameValid(key, val)) {
            throw new IllegalArgumentException("Vrednost " + val + " nije validna za kljuc " + key);
        }
        if (limiti.containsKey(key)) {
            config.setProperty(key, limiti.get(key).limit(val));
        } else {
            config.setProperty(key, val);
        }
        LOGGER.log(Level.CONFIG, "{0} podešen na {1}", new String[]{key, val});
        storeConfig();
    }

    /**
     * Proverava da li je data vrednost dozvoljena za dati kljuc. 
     * Koristi vrednosti iz {@link #types} tako sto uzima 0ti clan i:
     * Za int i double zove odgovarajuce metode iz {@link Utils}
     * Za boolean, dozvoljene vrednosti su true, false i broj (pri cemu se 0 tumaci kao false, sve ostalo true)
     * Za color, pokusava {@link Color#decode(java.lang.String)} i vraca true, ako dodje do Exceptiona vraca false
     * Za level isto kao za Color, samo kao metodu za proveru koristi {@link Level#parse(java.lang.String)}
     * Za url i delimitedInts vraca true, jer ocekuje proveru u {@link #checkValue(java.lang.String, java.lang.String)}
     * Za string vraca true, jer su sve vrednosti vec Stringovi
     * Ako ne odgovara nijednom, proverava da li se data vrednost nalazi medju ostalim
     * clanovima liste iz types i vraca rezultat {@link ArrayList#contains(java.lang.Object)}
     * 
     * @param key kljuc u configu
     * @param val vrednost u configu.
     * @return true ako je validna (moze da postoji), false u suprotnom.
     * @since 24.10.'14.
     */
    private static boolean isNameValid(String key, String val) {
        if (key.startsWith(KONSTANTE_PREFIX)) {
            return true;
        }
        if (!types.contains(key)) {
            System.out.println(key + " ne postoji");
            return false;
        }
        val = val.toLowerCase();
        ArrayList<String> type = types.get(key);
        switch(type.get(0)) {
            case "int": return Utils.isInteger(val);
            case "double": return Utils.isDouble(val);
            case "boolean": return val.equals("true") || val.equals("false") || Utils.isInteger(val);
            case "color": try {
                            Color.decode(val);
                            return true;
                        } catch (NumberFormatException ex) {
                            return false;
                        }
            case "level": try {
                            Level.parse(val.toUpperCase());
                            return true;
                        } catch (IllegalArgumentException ex) {
                            return false;
                        }
            case "string": case "url": case "delimitedInts":
                return true; //proverava se u checkValue
            default:
                return type.contains(val);
        }
    }

    /**
     * Proverava da li su vrednosti validne za brKnjiga, razrede i workingDir
     * @param key ako je jedan od 3 koji se proverava, radi proveru, u suprotnom ignorise
     * @param val vrednost kljuca
     */
    private static void checkValue(String key, String val) {
        Iterator<Ucenik> iterator;
        switch (key) {
            case "brKnjiga":
                int valInt = Integer.parseInt(val);
                iterator = Podaci.iteratorUcenika();
                iterator.forEachRemaining((Ucenik uc) -> {
                    if (uc.getBrojKnjiga() > valInt) {
                        throw new ConfigException("brKnjiga");
                    }
                });
                break;
            case "razredi":
                String[] valsStr = val.split(",");
                int[] vals = new int[valsStr.length];
                for (int i = 0; i < vals.length; i++) {
                    vals[i] = Integer.parseInt(valsStr[i].trim());
                }
                iterator = Podaci.iteratorUcenika();
                iterator.forEachRemaining((Ucenik uc) -> {
                    if (!Utils.arrayContains(vals, uc.getRazred())) {
                        throw new ConfigException("razredi");
                    }
                });
                break;
            case "workingDir":
                File folder = new File(val);
                if (!folder.isDirectory() && !folder.mkdir()) {
                    throw new ConfigException("workingDir");
                }
        }
    }

    /**
     * Vraca dati kljuc na default vrednost ili null ako default ne postoji.
     *
     * @param key kljuc koji treba resetovati
     * @since 25.10'.14.
     */
    public static void reset(String key) {
        config.setProperty(key, defaults.getProperty(key));
    }

    /**
     * Vraca nazive svih kljuceva u listi, ako imaju puno ime (ako se nalaze u mapi descriptions).
     *
     * @return ime kljuca koji je user-friendly
     * @since 26.10.'14.
     * @since 23.1.'15.
     */
    public static Set<String> getPodesavanjaKeys() {
        return descriptions.keySet();
    }
    
    /**
     * Vraca opis kljuca sa datim imenom.
     * @param key kljuc 
     * @return opis kljuca iz mape {@link #descriptions}
     */
    public static String getKeyDescription(String key) {
        return descriptions.get(key);
    }
    
    //==========LIMITS==========================================================

    /**
     * Sastoji se od minimalne i maksimalne vrednosti i funkcije koja uzima int i vraca validnu vrednost.
     */
    private static class Limit {

        final int MIN;
        final int MAX;

        /**
         * Kreira Limit sa datim MIN- i MAX-om
         * @param MIN minimalna vrednost za int
         * @param MAX maksimala vrednost za int
         */
        private Limit(int MIN, int MAX) {
            this.MAX = MAX;
            this.MIN = MIN;
        }

        /**
         * Kreira Limit kojem su MIN i MAX {@link Integer#MIN_VALUE} i {@link Integer#MAX_VALUE}
         */
        private Limit() {
            this.MAX = Integer.MAX_VALUE;
            this.MIN = 0;
        }

        /**
         * Kreira limit kojem je MIN data vrednost, a max {@link Integer#MAX_VALUE}.
         * @param MIN
         */
        private Limit(int MIN) {
            this.MIN = MIN;
            this.MAX = Integer.MAX_VALUE;
        }

        /**
         * Vraca dati int ako se nalazi izmedju min i max-a. U suprotom, vraca MIN ili MAX, u zavisnosti
         * sta je blize
         * @param val vrednost
         * @return validna vrednost (val, MIN ili MAX)
         */
        public int limit(int val) {
            return Integer.max(Integer.min(val, MAX), MIN);
        }

        /**
         * String wrapper za {@link #limit(int)}. Prihvata float-ove
         * @param val vrednost kao String
         * @return validna vrednost kao String
         */
        public String limit(String val) {
            return String.valueOf(limit((int)Math.floor(Float.valueOf(val))));
        }
    }
}
