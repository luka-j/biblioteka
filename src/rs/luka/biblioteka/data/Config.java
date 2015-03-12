/* Spisak podešavanja koje program koristi (poređani po vremenu postanka):
 * ucSize i knjSize: unsignedInt. Veličina lista za podatke. Podešava se automatski.
 * Koristi ih klasa Podaci za inicijalnu veličinu lista. Legacy
 * firstRun: bool. Označava da li se program pokreće prvi put. Podešava se automatski.
 * Koristi ga klasa Podaci u loadData da odredi da li će da pokrene dijalog za unos
 * dateLimit: unsignedInt, 1-365. Broj dana koje učenik sme da zadrži knjigu kod sebe. 
 * Podešava korisnik. Default je 14.
 * lookAndFeel: lookAndFeel {system, ocean, nimbus, motif, win classic}. Java LookAndFeel programa.
 * Podešava korisnik. Default je ocean.
 * brKnjiga: unsignedInt, 1-30. Broj knjiga koje učenik može da ima kod sebe u jednom trenutku.
 * Podešava korisnik. Default je 3.
 * bgBoja, fgBoja i TFColor: color (int ili jedna od predefinisanih boja). Određuje pozadinsku,
 * boju teksta i polja za unos redom. Podešava korisnik preko JColorChooser-a. Default je zelena, crna i bela.
 * logLevel: logLevel (int ili jedan od predefinisanih nivoa). Određuje minimalni nivo logovanja
 * u aplikaciji. Predefinisani nivoi: OFF, FINEST, FINER, FINE, INFO, WARNING, SEVERE.
 * Podešava korisnik. Menja se za vreme trajanja undo akcije automatski. Default je INFO.
 * savePeriod: unsignedInt, 0-1440: označava period izvršavanja metodi iz PeriodicActions
 * u minutima. Podešava korisnik. Default je 5.
 * maxUndo: unsignedInt. Broj akcija koje se čuvaju na stack-u za undo. 
 * Podešava se iz fajla. Default je 50.
 * razredi: delimitedInts (neoznačeni celi brojevi razdvojeni zapetom). Dozvoljene vrednosti za 
 * razred učenika. Podešava korisnik.
 * workingDir: url (putanja do fajla, validan direktorijum). Direktorijum u koji treba smeštati podatke i
 * u kojem se nalaze potrebni fajlovi. Podešava korisnik. Default je putanja iz koje je pokrenut program.
 * logSizeLimit: unsignedInt, 0-100000000. Maksimalna veličina log fajla u bajtovima.
 * Podešava korisnik. Default je 10000000 (10MB).
 * logFileCount: unsignedInt, 0-10000. Maksimalan broj log fajlova. Pri svakom pokretanju se kreira novi.
 * Podešava korisnik. Default je 15.
 * labelFontName, butFontName i smallButFontName: string. Ime fonta koji program treba da koristi
 * za labele, veliku dugmad i malu dugmad (uzimanje/vraćanje/promeni količinu) resp. Font mora biti
 * prisutan na sistemu ili u sklopu JVM. Podešava korisnik.
 * labelFontSize, butFontSize, smallButFontSize: unsignedInt, 1-50. Veličina gore pomenutih fontova.
 * Podešava se iz fajla.
 * labelFontWeight, butFontWeight, smallButFontWeight: font {plain, bold, italic, bold italic}.
 * Stil fonta. Podešava se iz fajla.
 * ucSort: sortMethod {ime, razred, raz}. Način na koji se sortiraju učenici, prvi označava da
 * se sortiraju po imenu, ne obraćajući pažnju na razred, drugi i treći su ekvivalentni i 
 * uzimaju razred u obzir. Ako je prva metoda izabrana, separatori su onemogućeni. 
 * Podešava se iz fajla. Default je razred.
 * datePeriod: floatingPoint, 1-30. Broj dana na koji se proverava da li su svi učenici vratili
 * iznajmljene knjige na vreme. Ova akcija je deo PeriodicActions klase i izvršava se
 * u intervalu savePeriod. Podešava korisnik. Default je 1.
 * shiftKnjige: bool. Označava da li se knjige učenika pomeraju ulevo nakon vraćanja. Nema
 * bitnije razlike, sem estetske. Podešava se iz fajla. Default je true.
 * customSize: bool. Označava da li se koriste veličine prozora koje je korisnik specifikovao
 * u sklopu int konstanti. Podešava se automatski.
 * kazna: unsignedInt, 0-500. Kazna ako učenik ne vrati knjigu na vreme. Uvećava se svakog
 * dana za dati iznos. Podešava korisnik. Default je 0.
 * stringsPath: string. Putanja do fajla koji sadrži lokalizaciju u properties formatu (ključ=vrednost)
 * Config overriduje strings fajl. Podešava se iz fajla. Default je strings.properties.
 * showDateDialog: bool. Označava da li treba prikazivati dijalog o kašnjenju pri pokretanju
 * programa. Ako ne, prikazivaće se samo kada učenik vrati knjigu. Učenici će i dalje biti 
 * upisivani u fajl kao i do sada. Podešava se iz fajla. Default je true.
 */
package rs.luka.biblioteka.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.showMessageDialog;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.xml.sax.SAXException;
import static rs.luka.biblioteka.data.Strings.loadStrings;
import rs.luka.biblioteka.data.XMLProperties.Type;
import rs.luka.biblioteka.data.XMLProperties.Value;
import rs.luka.biblioteka.exceptions.ConfigException;
import rs.luka.biblioteka.funkcije.Init;
import static rs.luka.biblioteka.funkcije.Init.dData;
import rs.luka.biblioteka.funkcije.Utils;


public class Config {
    private static final java.util.logging.Logger LOGGER = 
            java.util.logging.Logger.getLogger(Config.class.getName());


    /**
     * Properties.
     */
    private static XMLProperties config;
    /**
     * Default vrednosti za config.
     */
    private static final Properties defaults = new Properties();
    private static final XMLProperties defs = new XMLProperties();
    /**
     * Fajl odakle se ucitava config.
     */
    private static File configFile;
    /**
     * Stari komentar za config fajl, koji je sadrzao opise vrednosti.
     * @deprecated 
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
     * Komentar za config fajl, koji opisuje format podataka u XML fajlu.
     * @since 18.2.'15.
     */
    private static final String configDesc = "\nKonfiguracijski fajl cuva podatke u XML formatu, "
            + "koristeci sledece elemente:\n<properties> - root elemenat, u njemu se nalaze svi ostali\n"
            + "<config> - konfiguracijska podesavanja. Ovde spada sve sto kontrolise ponasanje programa\n"
            + "<strings> - stringovi za prikaz. Imena uzeta iz Konstante.java, bez sufiksa _STRING\n"
            + "<ints> - velicine i pozicije grafickih komponenata. Imena iz Konstante.java\n"
            + "<entity> - vrednost. Opisana je atributima, i to:\n"
            + "\tname - ime podesavanja, obavezno polje\n"
            + "\tdesc - opis podesavanja, ako postoji prikazuje se u prozoru Podesavanja (graficki)\n"
            + "\ttype - tip podataka, jedna od vrednosti iz Type\n"
            + "\tlimit - limit za brojcane vrednosti, u formatu MIN~MAX\n"
            + "Lose vrednosti mogu izazvati crashovanje pri nekim uslovima.\n";
    /**
     * Tipovi podataka ili validne vrednosti za svaki kljuc. Sadrzi sve moguce kljuceve sem konstanti
     * @since 16.1.'14.
     */
    private static final Map<String, XMLProperties.Type> types = new HashMap<>();
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
    private static final Limit KAZNA_LIMIT = new Limit(0, 500); //500 dinara dnevno, vise nego dovoljno

    /**
     * Ucitava config iz fajla u Properties.
     */
    public static void loadConfig() {
        setDefaults();
        setDescriptions();
        setLimits();
        setTypes();
        configFile = new File(Utils.getWorkingDir() + "config.xml");
        config = new XMLProperties();
        String path = null;
        try {
            configFile.createNewFile();
            if(configFile.length()==0) {
                doFirstTimeInit();
                return;
            }
            path = configFile.getCanonicalPath();
            config.load(configFile);
        } catch (FileNotFoundException ex) {
            loadStrings(); //stringovi nisu učitani, a došlo je do greške pri učiravanju configa: 
                           //fallback, učitavam iz strings fajla poruke o grešci koje prikazujem
            LOGGER.log(Level.SEVERE, "Konfiguracijski fajl nije pronađen: {0}", path);
            showMessageDialog(null, Init.dData.LOADCONFIG_FNFEX_MSG_STRING + path, Init.dData.LOADCONFIG_FNFEX_TITLE_STRING, 
                    ERROR_MESSAGE);
        } catch (IOException ex) {
            loadStrings();
            LOGGER.log(Level.SEVERE, "I/O greška pri čitanju konfiguracijskog fajla", ex);
            showMessageDialog(null, Init.dData.LOADCONFIG_IOEX_MSG_STRING, Init.dData.LOADCONFIG_IOEX_TITLE_STRING, ERROR_MESSAGE);
        } catch (ParserConfigurationException ex) {
            loadStrings();
            LOGGER.log(Level.SEVERE, "Loša konfiguracija parsera", ex);
            showMessageDialog(null, Init.dData.XMLCONFIG_PCEX_MSG_STRING, Init.dData.XMLCONFIG_PCEX_TITLE_STRING, ERROR_MESSAGE);
        } catch (SAXException ex) {
            loadStrings();
            LOGGER.log(Level.SEVERE, "Greška pri parsiranju XMLa", ex);
            showMessageDialog(null, Init.dData.XMLCONFIG_SAXEX_MSG_STRING, Init.dData.XMLCONFIG_SAXEX_TITLE_STRING, ERROR_MESSAGE);
        }
        loadStrings();
        setKonstante(); //config overriduje strings
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
        descriptions.put("razredi", Init.dData.CONFIG_RAZREDI_STRING);
        descriptions.put("brKnjiga", Init.dData.CONFIG_BRKNJIGA_STRING);
        descriptions.put("dateLimit", Init.dData.CONFIG_DATELIMIT_STRING);
        descriptions.put("workingDir", Init.dData.CONFIG_WORKINGDIR_STRING);
        descriptions.put("savePeriod", Init.dData.CONFIG_SAVEPERIOD_STRING);
        descriptions.put("datePeriod", Init.dData.CONFIG_DATEPERIOD_STRING);
        descriptions.put("logLevel", Init.dData.CONFIG_LOGLEVEL_STRING);
        descriptions.put("logSizeLimit", Init.dData.CONFIG_LOGSIZELIMIT_STRING);
        descriptions.put("logFileCount", Init.dData.CONFIG_LOGFILECOUNT_STRING);
        descriptions.put("lookAndFeel", Init.dData.CONFIG_LOOKANDFEEL_STRING);
        descriptions.put("labelFontName", Init.dData.CONFIG_LABELFONTNAME_STRING);
        descriptions.put("butFontName", Init.dData.CONFIG_BUTFONTNAME_STRING);
        descriptions.put("smallButFontName", Init.dData.CONFIG_SMALLBUTFONTNAME_STRING);
        descriptions.put("kazna", Init.dData.CONFIG_KAZNA_STRING);
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
     * @since 16.1.'15.
     */
    private static void setKonstante() {
        Map<String, String> ints = config.getAllInts();
        Map<String, String> strings = config.getAllStrings();
        ints.entrySet().stream().forEach(dData::set);
        strings.entrySet().stream().forEach((Entry<String, String> entry) -> {
            dData.set(entry.getKey() + "_STRING", entry.getValue());
        });
    }
    
    public static void setUsingCustomSizes() {
        config.put(new XMLProperties.Key("customSize", XMLProperties.Category.config), 
                new XMLProperties.Value("1", null, XMLProperties.Type.bool, null));
    }
    
    /**
     * Podesava tipove podataka za sve kljuceve. Ako su moguce samo odredjene vrednosti,
     * ubacuje te vrednosti umesto konkretnog tipa u mapu.
     * @see #types
     * @since 16.1.'15.
     */
    private static void setTypes() {
        types.put("ucSize", Type.unsingnedInt);
        types.put("knjSize", Type.unsingnedInt);
        types.put("firstRun", Type.bool);
        types.put("dateLimit", Type.unsingnedInt);
        types.put("lookAndFeel", Type.lookAndFeel);
        types.put("brKnjiga", Type.unsingnedInt);
        types.put("bgBoja", Type.color);
        types.put("fgBoja", Type.color);
        types.put("TFColor", Type.color);
        types.put("logLevel", Type.level);
        types.put("savePeriod", Type.unsingnedInt);
        types.put("razredi", Type.delimitedInts);
        types.put("workingDir", Type.string);
        types.put("logSizeLimit", Type.unsingnedInt);
        types.put("logFileCount", Type.unsingnedInt);
        types.put("labelFontName", Type.string);
        types.put("butFontName", Type.string);
        types.put("smallButFontName", Type.string);
        types.put("datePeriod", Type.floatingPoint);
        types.put("kazna", Type.unsingnedInt);
    }

    /**
     * Cuva config u fajl.
     * @see XMLProperties#store(java.lang.String) 
     */
    private static void storeConfig() {
        try {
            config.store(configDesc, configFile);
        } catch (ParserConfigurationException ex) {
            LOGGER.log(Level.SEVERE, "Loša konfiguracija parsera", ex);
            showMessageDialog(null, Init.dData.XMLCONFIG_PCEX_MSG_STRING, Init.dData.XMLCONFIG_PCEX_TITLE_STRING, ERROR_MESSAGE);
        } catch (TransformerException ex) {
            LOGGER.log(Level.SEVERE, "Greška pri transformisanju u XML", ex);
            showMessageDialog(null, Init.dData.XMLCONFIG_TEX_MSG_STRING, Init.dData.XMLCONFIG_TEX_TITLE_STRING, ERROR_MESSAGE);
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
        return config.containsConfigKey(key);
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
        return config.getConfigProperty(key)==null ? defaults.getProperty(key) : config.getConfigProperty(key).getValue();
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
        return config.containsConfigKey(key) ? config.getConfigProperty(key).getValue() : def;
    }

    /**
     * Vraca integer reprezentaciju trazenog kljuca ili 0 ako kljuc ne postoji.
     *
     * @param key kljuc koji se trazi
     * @return vrednost kljuca kao int.
     * @throws NumberFormatException ako vrednost nije int
     * @see #get(java.lang.String) 
     * @since 25.10'.14.
     */
    public static int getAsInt(String key) {
        String val = get(key);
        return val==null ? 0 : Integer.parseInt(val);
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
     * boolean, u suprotnom uporedjuje String sa "true". Ako kljuc ne postoji, vraca false.
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
     * Postavlja dati kljuc na datu vrednost, ako su oboje validni. Ignorise
     * veliko/malo slovo.
     *
     * @param key kljuc
     * @param valString vrednost
     * @see #isNameValid(java.lang.String, java.lang.String)
     * @see #checkValue(java.lang.String, java.lang.String) 
     * @since 25.10.'14.
     */
    public static void setConfigEntry(String key, String valString) {
        checkValue(key, valString); //throwuje ConfigException-e (koji su Runtime)
        Value val, oldVal;
        Limit limit = limiti.containsKey(key) ? limiti.get(key) : null;
        if(config.containsConfigKey(key)) {
            oldVal = config.getConfigProperty(key);
            val = new Value(valString, oldVal.description, oldVal.type, oldVal.limit);
        }
        else
            val = new Value(valString, descriptions.get(key), types.get(key), limit);
        if (!val.type.isValid(valString)) {
            throw new IllegalArgumentException("Vrednost " + valString + " nije validna za kljuc " + key);
        }
        config.setConfigEntry(key, val);
        LOGGER.log(Level.CONFIG, "{0} podešen na {1}", new String[]{key, valString});
        storeConfig();
    }

    /**
     * Proverava da li su vrednosti validne za brKnjiga, razrede i workingDir.
     * Throwuje ConfigException sa imenom kljuca u opisu.
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
        //config.setProperty(key, defaults.getProperty(key));
    }

    /**
     * Vraca nazive svih kljuceva u listi, ako imaju puno ime (ako se nalaze u mapi descriptions).
     *
     * @return ime kljuca koji je user-friendly
     * @since 26.10.'14.
     * @since 23.1.'15.
     */
    public static Set<String> getPodesavanjaKeys() {
        descriptions.putAll(config.getDescriptedKeys());
        return descriptions.keySet();
    }
    
    /**
     * Vraca opis kljuca sa datim imenom.
     * @param key kljuc 
     * @return opis kljuca iz mape {@link #descriptions}
     */
    public static String getKeyDescriptions(String key) {
        return descriptions.get(key);
    }
    
    public static void doFirstTimeInit() {
        
    }
}
 