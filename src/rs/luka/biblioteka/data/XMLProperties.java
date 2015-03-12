package rs.luka.biblioteka.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Stack;
import java.util.logging.Level;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import rs.luka.biblioteka.data.XMLProperties.Key;
import rs.luka.biblioteka.data.XMLProperties.Value;
import rs.luka.biblioteka.funkcije.Utils;

/**
 * Format za cuvanje konfiguracije specifican za ovaj program. Podatke smesta u {@link HashMap},
 * koristeci interne tipove podataka, {@link Key} i {@link Value}
 * 
 * @author luka
 * @since 19.2.'15.
 */
public class XMLProperties extends HashMap<XMLProperties.Key, XMLProperties.Value> {

    /**
     * Cuva podatke u dati fajl fajl. Na pocetku ispisuje dati string kao komentar.
     * Format fajla je sledeci:
     * properties tag otvara i zatvara fajl, unutar njega se nalazi sve ostalo
     * config oznacava konfiguracijske podatke koje koristi klasa Config
     * ints i strings oznacavaju podatke za ispis, velicinu i poziciju, koje koristi klasa Konstante
     * Svi podaci se cuvaju unutar entity taga, dok se za ime i detalje koriste atributi
     * (name-ime, desc-opis, type-tip podatka, limit-brojcani limit ako je tip celobrojni).
     * 
     * @param comment komentar koji se ispisuje na pocetku fajla
     * @param file fajl u koji se smestaju podaci
     * @throws ParserConfigurationException
     * @throws TransformerConfigurationException
     * @throws TransformerException 
     * 
     * @since 19.2.'14.
     */
    public void store(String comment, File file) throws ParserConfigurationException, TransformerConfigurationException, TransformerException {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document doc = docBuilder.newDocument();

        
        Element rootElement = doc.createElement("properties");
        doc.appendChild(rootElement);
        Element config = doc.createElement("config");
        rootElement.appendChild(config);
        Element ints = doc.createElement("ints");
        rootElement.appendChild(ints);
        Element strings = doc.createElement("strings");
        rootElement.appendChild(strings);
        
        if(comment!=null && !comment.isEmpty()) {
            Comment comm = doc.createComment(comment);
            rootElement.getParentNode().insertBefore(comm, rootElement);
        }

        super.entrySet().forEach((Entry<Key, Value> entry) -> {
            Key key = entry.getKey();
            Value val = entry.getValue();
            Element el = doc.createElement("entity");
            el.appendChild(doc.createTextNode(val.val));
            el.setAttribute("name", entry.getKey().key);
            el.setAttribute("type", val.type.name());
            if (val.hasDesc()) {
                el.setAttribute("desc", val.description);
            }
            if (val.hasLimit()) {
                el.setAttribute("limit", val.limit.toString());
            }

            if (key.isConfig()) {
                config.appendChild(el);
            } else if (key.isKonstanteInt()) {
                ints.appendChild(el);
            } else if (key.isKonstanteString()) {
                strings.appendChild(el);
            }
        });

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(file);
        transformer.transform(source, result);
    }

    /**
     * Ucitava podatke iz datog (xml) fajla. Za parsiranje koristi {@link SaxHandler}.
     * 
     * @param configFile fajl iz kojeg se ucitavaju podaci
     * @throws FileNotFoundException
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException 
     * 
     * @since 19.2.'15
     */
    public void load(File configFile) throws FileNotFoundException, ParserConfigurationException, SAXException, IOException {
        InputStream in = new FileInputStream(configFile);
        SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
        SaxHandler handler = new SaxHandler();
        parser.parse(in, handler);
    }

    /**
     * Vraca vrednost kljuca sa datim imenom i kategorijom.
     * @param key ime kljuca
     * @param category kategorija kljuca
     * @return vrednost kao {@link Value} objekat
     * 
     * @since 19.2.'15
     */
    public Value get(String key, Category category) {
        Key prop = new Key(key, category);
        return super.get(prop);
    }

    /**
     * Proverava da li postoji kljuc sa datom vrednoscu i kategorijom. Ako je ijedno
     * od ova dva null, vraca false.
     * @param key ime kljuca
     * @param category kategorija kljuca
     * @return true ili false, u zavisnosti od toga da li se u mapi nalazi odgovarajuca vrednost
     * 
     * @since 19.2.'15
     */
    public boolean containsKey(String key, Category category) {
        if (key == null || category == null) {
            return false;
        }
        Key prop = new Key(key, category);
        return super.containsKey(prop);
    }

    /**
     * Vraca vrednost datog kljuca koji se nalazi u config kategoriji. Ovo je isto kao i
     * pozivanje {@link #get(java.lang.String, rs.luka.biblioteka.data.XMLProperties.Category) 
     * koji kao drugi parametar ima {@link Category#config} ili 
     * {@link #getConfigProperty(java.lang.String, rs.luka.biblioteka.data.XMLProperties.Value) }
     * sa null kao drugim parametrom.
     * 
     * @param key kljuc iz configa
     * @return vrednost u {@link Value} objektu
     * 
     * @since 19.2.'15
     */
    public Value getConfigProperty(String key) {
        return get(key, Category.config);
    }

    /**
     * Vraca vrednost datog kljuca koji se nalazi u config kategoriji ako postoji, u 
     * suprotnom vraca def.
     * 
     * @param key kljuc koji se trazi u configu
     * @param def vrednost koja se vraca ako se kljuc ne nalazi u mapi
     * @return vrednost povezana sa datim kljucem
     * 
     * @since 19.2.'15
     */
    public Value getConfigProperty(String key, Value def) {
        return getConfigProperty(key) == null ? def : getConfigProperty(key);
    }

    /**
     * Proverava da li ova mapa sadrzi dati kljuc u config kategoriji.
     * @param key kljuc koji se trazi
     * @return 
     * 
     * @since 19.2.'15
     */
    public boolean containsConfigKey(String key) {
        return containsKey(key, Category.config);
    }

    /**
     * Vraca mapu tipa <String, String> koja sadrzi sve kljuceve i vrednosti u 
     * {@link Category#ints} kategoriji. Radi iteraciju preko mape koristeci stream.
     * @return Mapa sa svim vrednostima koje pripadaju kategoriji ints
     * 
     * @since 19.2.'15
     */
    public Map<String, String> getAllInts() {
        Map<String, String> ints = new HashMap<>();
        super.entrySet().stream().forEach((Entry<Key, Value> entry) -> {
            if (entry.getKey().isKonstanteInt()) {
                ints.put(entry.getKey().key, entry.getValue().getValue());
            }
        });
        return ints;
    }
    
    /**
     * Vraca mapu tipa <String, String> koja sadrzi sve kljuceve i vrednosti u 
     * {@link Category#strings} kategoriji. Radi iteraciju preko mape koristeci stream.
     * @return Mapa sa svim vrednostima koje pripadaju kategoriji strings
     * 
     * @since 19.2.'15
     */
    public Map<String, String> getAllStrings() {
        Map<String, String> strings = new HashMap<>();
        super.entrySet().stream().filter((Entry<Key, Value> entry) -> {
            return entry.getKey().isKonstanteString();
        }).forEach((Entry<Key, Value> entry) -> {
            strings.put(entry.getKey().key, entry.getValue().val);
        });
        return strings;
    }
    
    /**
     * Vraca mapu tipa <String, String> koja sadrzi sve kljuceve i njihov opis. 
     * Radi iteraciju preko mape koristeci stream.
     * @return Mapa sa svim vrednostima koje imaju opis
     * 
     * @since 19.2.'15
     */
    public Map<String, String> getDescriptedKeys() {
        Map<String, String> descs = new HashMap<>();
        super.entrySet().stream().filter((Entry<Key, Value> en) -> {
            return en.getValue().hasDesc();
        }).forEach((Entry<Key, Value> en) -> {
            descs.put(en.getKey().key, en.getValue().description);
        });
        return descs;
    }

    /**
     * Postavlja kljuc {@link Category#config} kategorije koristeci date parametre.
     * @param key ime kljuca
     * @param type tip podatka iz enumeracije {@link Type}
     * @param description opis kljuca
     * @param limit brojcani limit u formatu iz {@link Limit}
     * @param value vrednost kljuca
     * 
     * @since 19.2.'15
     */
    public void setConfigEntry(String key, String type, String description, String limit, String value) {
        super.put(new Key(key, Category.config.name()), new Value(value, description, type, limit));
    }
    
    /**
     * Postavlja kljuc {@link Category#config} kategorije prema datim parametrima
     * @param key ime kljuca
     * @param val vrednost kljuca
     * 
     * @since 19.2.'15
     */
    public void setConfigEntry(String key, Value val) {
        super.put(new Key(key, Category.config.name()), val);
    }
    
    /**
     * Postavlja kljuc {@link Category#ints} kategorije prema datim parametrima
     * @param key ime kljuca
     * @param val vrednost kljuca
     * 
     * @since 19.2.'15
     */
    public void setKonstanta(String key, int val) {
        super.put(new Key(key, Category.ints.name()), new Value(String.valueOf(val), null, Type.unsingnedInt.name(), null));
    }
    
    /**
     * Postavlja kljuc neke konstante prema datim parametrima.
     * @param key ime kljuca, ako je u pitanju string, mora se zavrsavati sa _STRING. U suprotnom
     * ce biti smatran celim brojem. Sufiks _STRING se ne cuva, vec sluzi samo da bi se ova dva
     * tipa razlikovala.
     * @param val vrednost kljuca
     * 
     * @since 19.2.'15
     */
    public void setKonstanta(String key, String val) {
        val = val.toUpperCase();
        if(val.endsWith("_STRING"))
            super.put(new Key(key, Category.strings.name()), new Value(val.replace("_STRING", ""), null, Type.string.name(), null));
        else
            setKonstanta(key, Integer.parseInt(val));
    }

    //==========================================================================
    /**
     * Koristi se za parsiranje XML fajla. Overriduje odgovarajuce metode
     * @see DefaultHandler
     * 
     * @since 19.2.'15
     */
    class SaxHandler extends DefaultHandler {

        /**
         * Prethodni elementi (tagovi), ne smesta entity
         */
        private Stack<String> elements = new Stack<>();
        /**
         * Kljuc koji se trenutno ucitava, postavljen od strane startElement metode
         */
        private Key currentKey;
        /**
         * Vrednost koja se trenutno ucitava, postavljena od strane startElement metode,
         * s namerom da se dopuni u endElement koristeci currentChunk
         */
        private Value currentValue;
        /**
         * Sve sto se nalazi izmedju tagova, a jos nije parsirano
         */
        private StringBuilder currentChunk;

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            if (qName.equals("entity")) {
                currentKey = new Key(attributes.getValue("name"), getCategory());
                currentValue = new Value(null, attributes.getValue("desc"), attributes.getValue("type"), attributes.getValue("limit"));
                currentChunk = new StringBuilder();
            } else {
                elements.push(qName);
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            if (qName.equals("entity")) {
                currentValue.val = currentChunk.toString();
                XMLProperties.this.put(currentKey, currentValue);
                currentKey = null;
                currentValue = null;
                currentChunk = null;
            } else {
                elements.pop();
            }
        }

        @Override
        public void characters(char ch[], int start, int length) throws SAXException {
            if (currentChunk == null) {
                return;
            }
            currentChunk.append(new String(ch, start, length));
        }

        private String getCategory() {
            return elements.peek();
        }
    }

    /**
     * Klasa koja predstavlja kljuc u mapi. Sastoji se od imena kljuca (String) i kategorije
     * kojoj pripada (enum).
     * 
     * @since 19.2.'15
     */
    public static class Key {

        public final String key;
        final Category category;

        /**
         * Konstruktor koji prima dva stringa, prvi oznacava ime kljuca, a drugi ime
         * kategorije. Drugi string se mora poklapati sa jednom vrednoscu iz {@link Category}
         * 
         * @param key ime kljuca
         * @param category ime kategorije
         * 
         * @since 19.2.'15
         */
        Key(String key, String category) {
            this.key = key;
            this.category = Category.valueOf(category);
        }

        /**
         * Konstruktor koji postavlja vrednosti klase prema datim argumentima.
         * @param key ime kljuca
         * @param category kategorija
         * @see Category
         * 
         * @since 19.2.'15
         */
        Key(String key, Category category) {
            this.key = key;
            this.category = category;
        }

        /**
         * Vraca da li ovaj kljuc pripada kategoriji {@link Category#config}
         * @return 
         * 
         * @since 19.2.'15
         */
        boolean isConfig() {
            return Category.config.equals(category);
        }
        /**
         * Vraca da li ovaj kljuc pripada kategoriji {@link Category#ints}
         * @return 
         * 
         * @since 19.2.'15
         */
        boolean isKonstanteInt() {
            return Category.ints.equals(category);
        }
        /**
         * Vraca da li ovaj kljuc pripada kategoriji {@link Category#strings}
         * @return 
         * 
         * @since 19.2.'15
         */
        boolean isKonstanteString() {
            return Category.strings.equals(category);
        }

        @Override
        public boolean equals(Object otherProperty) {
            if (!(otherProperty instanceof Key)) {
                return false;
            }
            Key other = (Key) otherProperty;
            return other.key.equalsIgnoreCase(this.key) && other.category.equals(this.category);
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 97 * hash + Objects.hashCode(this.key);
            hash = 97 * hash + Objects.hashCode(this.category);
            return hash;
        }
    }

    /**
     * Klasa koja predstavlja vrednost kljuca u mapi. Sastoji se od vrednosti kao string,
     * tipa podataka, opisa i numerickog limita.
     * 
     * @since 19.2.'15
     */
    public static class Value {

        /**
         * Koristiti metodu getValue. No exceptions.
         */
        private String val;
        final Type type;
        final String description;
        final Limit limit;

        /**
         * Konstruktor koji postavlja vrednosti prema datim stringovima.
         * @param val vrednost
         * @param desc opis
         * @param type tip podataka iz {@link Type}
         * @param limit limit u formatu {@link Limit} ili null
         * 
         * @since 19.2.'15
         */
        Value(String val, String desc, String type, String limit) {
            this.val = val;
            this.description = desc;
            this.type = Type.valueOf(type);
            this.limit = limit == null ? null : new Limit(limit);
        }
        
        /**
         * Konstruktor koji postavlja vrednosti klase kako su mu date.
         * @param val vrednost
         * @param desc opis vrednosti
         * @param type tip podataka
         * @param limit limit
         * 
         * @since 19.2.'15
         */
        Value(String val, String desc, Type type, Limit limit) {
            this.val = val;
            this.description = desc;
            this.type = type;
            this.limit = limit;
        }

        /**
         * Proverava da li ova vrednost ima opis koji nije prazan, to jest, da li nije
         * null i nije prazan.
         * @return 
         * 
         * @since 19.2.'15
         */
        boolean hasDesc() {
            return description != null && !description.isEmpty();
        }

        /**
         * Proverava da li je ova vrednost tipa {@link Type#unsingnedInt} i da li je limit !=null.
         * @return 
         * 
         * @since 19.2.'15
         */
        boolean hasLimit() {
            return Type.unsingnedInt.equals(type) && limit != null;
        }
        
        /**
         * Vraca (limitiranu) vrednost kao String.
         * @return 
         * 
         * @since 19.2.'15
         */
        public String getValue() {
            if(hasLimit())
                return limit.limit(val);
            else return val;
        }
    }

    /**
     * Oznacava tip podataka. Ima {@link #isValid(java.lang.String)} metodu koja proverava
     * da li je vrednost dobra za dati tip.
     * 
     * @since 19.2.'15
     */
    public static enum Type {

        /**
         * Neoznaceni ceo broj.
         * @see Utils#isUnsignedInteger(java.lang.String) 
         */
        unsingnedInt {
                    @Override
                    public boolean isValid(String val) {
                        return Utils.isUnsignedInteger(val);
                    }
                },
        /**
         * Realan broj.
         * @see Utils#isDouble(java.lang.String) 
         */
        floatingPoint {
                    @Override
                    public boolean isValid(String val) {
                        return Utils.isDouble(val);
                    }
        },
        /**
         * String.
         * isValid uvek vraca true
         */
        string {
                    @Override
                    public boolean isValid(String val) {
                        return true;
                    }
                },
        /**
         * Boolean.
         * isValid je uporedjuje sa "true", "false" ili celim brojem.
         */
        bool {
                    @Override
                    public boolean isValid(String val) {
                        return val.equalsIgnoreCase("true") || val.equalsIgnoreCase("false") || Utils.isInteger(val);
                    }
                },
        /**
         * Boja u RGB formatu.
         * @see Utils#isInteger(java.lang.String) 
         */
        color {
                    @Override
                    public boolean isValid(String val) {
                        return Utils.isInteger(val);
                    }
                },
        /**
         * Graficko okruzenje.
         * System, ocean, nimbus, motif ili win classic (poslednji ne postoji na Linuxu i izazvace crash)
         */
        lookAndFeel {
                    @Override
                    public boolean isValid(String val) {
                        val = val.toLowerCase();
                        return val.equals("system") || val.equals("ocean") || val.equals("nimbus")
                        || val.equals("motif") || val.equals("win classic");
                    }
                },
        /**
         * Nivo logovanja u aplikaciji.
         * @see Level#parse(java.lang.String) 
         */
        level {
                    @Override
                    public boolean isValid(String val) {
                        try {
                            Level.parse(val);
                            return true;
                        } catch (IllegalArgumentException ex) {
                            return false;
                        }
                    }
                },
        /**
         * Prirodni brojevi razdvojeni razmakom.
         * @see Utils#isUnsignedInteger(java.lang.String) 
         */
        delimitedInts {
                    @Override
                    public boolean isValid(String val) {
                        String[] ints = val.split("\\s,\\s");
                        return Arrays.stream(ints).allMatch(Utils::isUnsignedInteger);
                    }
                },
        /**
         * Stil fonta.
         * Plain, bold, italic ili bold italic
         */
        font {
                    @Override
                    public boolean isValid(String val) {
                        val = val.toLowerCase();
                        return val.equals("plain") || val.equals("bold") || val.equals("italic") || val.equals("bold italic");
                    }
                },
        /**
         * Metoda za sortiranje ucenika.
         * ime, raz ili razred
         */
        sortMethod {
                    @Override
                    public boolean isValid(String val) {
                        val =val.toLowerCase();
                        return val.equals("ime") || val.equals("raz") || val.equals("razred");
                    }
        };

        /**
         * Metoda koja proverava da li je vrednost validna za ovaj tip podataka. Svaki clan
         * enumeracije je duzan da je implementira.
         * @param val vrednost koja se proverava
         * @return true ako je validna, false ako nije
         * 
         * @since 19.2.'15
         */
        public abstract boolean isValid(String val);
    }

    /**
     * Kategorija date promenljive. Napravljena kako bi vrednosti configa mogli da imaju
     * ista imena ako se odnose na razlicite stvari, eliminisuci potrebu za prefiksima
     * (k_) i sufiksima (_STRING).
     * 
     * @since 19.2.'15
     */
    public static enum Category {
        /**
         * Konfiguracijski podatak. Kontrolise ponasanje programa. Koristi ga klasa Config.
         */
        config, 
        /**
         * Velicina ili dimenzija graficke komponente. Koristi ga klasa Konstante (preko klase Config).
         */
        ints, 
        /**
         * String koji se prikazuje na ekranu. Koristi ga klasa Konstante (preko klase Config).
         */
        strings
    }
}

/**
 * Sastoji se od minimalne i maksimalne vrednosti i funkcije koja uzima int i
 * vraca validnu vrednost.
 */
class Limit {

    final int MIN;
    final int MAX;

    /**
     * Kreira Limit sa datim MIN- i MAX-om
     *
     * @param MIN minimalna vrednost za int
     * @param MAX maksimala vrednost za int
     */
    public Limit(int MIN, int MAX) {
        this.MAX = MAX;
        this.MIN = MIN;
    }

    /**
     * Kreira limit prema datom stringu, splittuje po ~. Ako je dati String null,
     * "null" ili prazan, postavlja MIN i MAX na krajnje vrednosti.
     * @param IOString String dobijen iz toString metode
     */
    Limit(String IOString) {
        if(IOString==null || IOString.equals("null") || IOString.isEmpty()) {
            MIN=Integer.MIN_VALUE; 
            MAX=Integer.MAX_VALUE;
        }
        else {
            String[] fields = IOString.split("~");
            MIN = Integer.parseInt(fields[0]);
            MAX = Integer.parseInt(fields[1]);
        }
    }

    /**
     * Kreira Limit kojem su MIN i MAX {@link Integer#MIN_VALUE} i
     * {@link Integer#MAX_VALUE}
     */
    Limit() {
        this.MAX = Integer.MAX_VALUE;
        this.MIN = 0;
    }

    /**
     * Kreira limit kojem je MIN data vrednost, a max {@link Integer#MAX_VALUE}.
     *
     * @param MIN
     */
    Limit(int MIN) {
        this.MIN = MIN;
        this.MAX = Integer.MAX_VALUE;
    }

    /**
     * Vraca dati int ako se nalazi izmedju min i max-a. U suprotom, vraca MIN
     * ili MAX, u zavisnosti sta je blize
     *
     * @param val vrednost
     * @return validna vrednost (val, MIN ili MAX)
     */
    public int limit(int val) {
        return Integer.max(Integer.min(val, MAX), MIN);
    }

    /**
     * String wrapper za {@link #limit(int)}. Prihvata float-ove
     *
     * @param val vrednost kao String
     * @return validna vrednost kao String
     */
    public String limit(String val) {
        return String.valueOf(limit((int) Math.floor(Float.valueOf(val))));
    }

    @Override
    public String toString() {
        return MIN + "~" + MAX;
    }
}
