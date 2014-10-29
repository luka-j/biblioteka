package rs.luka.biblioteka.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * MultiMap implementacija koja koristi 2 ArrayLista kao osnovu. Jedan sadrzi kljuceve, drugi listu Strigova
 * koji predstavljaju vrednosti. Dozvoljava null vrednosti.
 * @author luka
 * @since 25.10.'14.
 */
public class MultiMap implements Map<String, ArrayList<String>> {
    private static final long serialVersionUID = 1L;
    private final List<String> keys;
    private final List<ArrayList<String>> values;
    
    public MultiMap() {
        keys = new ArrayList<>(18);
        values = new ArrayList<>(18);
    }

    @Override
    public int size() {
        return keys.size();
    }

    @Override
    public boolean isEmpty() {
        return keys.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return keys.contains((String)key);
    }

    /**
     * Proverava da li data vrednost postoji. Smatra ArrayList Stringova i String validnim argumentima
     * (prvo proverava da li je String, ako nije, pretpostavlja da je ArrayList)
     * @param value vrednost koja se proverava
     * @return true ako postoji, false u suprotnom
     */
    @Override
    public boolean containsValue(Object value) {
        if (value instanceof String) {
            return values.stream().anyMatch((vals) -> (vals.stream().anyMatch((val) -> (val.equalsIgnoreCase((String)value)))));
        }
        else
            return values.stream().anyMatch((val) -> (val.equals(value)));
    }

    @Override
    public ArrayList<String> get(Object key) {
        ArrayList<String> copy = new ArrayList<>();
        Collections.copy(copy, values.get(keys.indexOf((String)key)));
        return copy;
    }

    @Override
    public ArrayList<String> put(String key, ArrayList<String> value) {
        if(!keys.contains(key)) {
            keys.add(key);
            values.add(value);
            return null;
        }
        else {
            ArrayList<String> prev = values.get(keys.indexOf(key));
            values.set(keys.indexOf(key), value);
            return prev;
        }
    }

    @Override
    public ArrayList<String> remove(Object key) {
        int inx = keys.indexOf(key);
        ArrayList<String> prev = values.get(inx);
        keys.remove(inx);
        values.remove(inx);
        return prev;
    }

    @Override
    public void putAll(Map<? extends String, ? extends ArrayList<String>> m) {
        throw new UnsupportedOperationException("putAll za MultiMap ne postoji. "
                + "Mrzi me da radim iteraciju preko mape.");
    }

    @Override
    public void clear() {
        keys.clear();
        values.clear();
    }

    @Override
    public Set<String> keySet() {
        return new HashSet<>(keys);
    }

    @Override
    public Collection<ArrayList<String>> values() {
        return Collections.unmodifiableCollection(values);
    }

    @Override
    public Set<Entry<String, ArrayList<String>>> entrySet() {
        throw new UnsupportedOperationException("entrySet za MultiMap ne postoji. "
                + "Nisam siguran kako bih uradio, a ne treba mi.");
    }
    
    /**
     * Povezuje datu vrednost sa kljucem. Ako kljuc ne postoji, kreira ga. U suprotom, dodaje vrednost
     * kod kljuca. Ako vrednost vec postoji, mapa ostaje nepromenjena.
     * @param key kljuc za koji se dodaje vrednost
     * @param val vrednost koja se dodaje
     * @since 25.10.'14.
     */
    public void put(String key, String val) {
        int inx = keys.indexOf(key);
        if(inx==-1) {
            keys.add(key);
            values.add(new ArrayList<>());
            values.get(values.size()-1).add(val);
        }
        else if(!values.get(inx).contains(val)) 
            values.get(inx).add(val);
    }
    
    /**
     * Stavlja novi kljuc u listu. Ako je prethodno postojao kljuc sa istim imenom, brise ga.
     * @param key kljuc
     * @param vals vrednosti za kljuc
     * @since 25.10.'14.
     */
    public void put(String key, String... vals) {
        int inx = keys.indexOf(key);
        if(inx>-1) {
            keys.remove(inx);
            values.remove(inx);
        }
        keys.add(key);
        values.add(new ArrayList<>(Arrays.asList(vals)));
    }
    
    /**
     * Vraca kljuc koji je povezan sa ovom vrednoscu. Ako takav kljuc ne postoji, vraca null.
     * @param value vrednost za koju se trazi kljuc
     * @return kljuc koji sadrzi datu vrednost
     * @since 25.10.'.14
     */
    public String getKey(String value) {
        for(int i=0; i<values.size(); i++)
            for (String val : values.get(i)) {
                if(val.equalsIgnoreCase(value))
                    return keys.get(i);
            }
        return null;
    }
    
    public boolean contains(String str) {
        return containsKey(str) || containsValue(str);
    }
    
    /**
     * Vraca poslednju vrednost za dati kljuc. Ako kljuc ne postoji, vraca null.
     * @param key kljuc za koji se trazi vrednost
     * @return poslednji kljuc u listi
     * @since 26.10.'14.
     */
    public String getLastValue(String key) {
        //return values.get(keys.indexOf(key)).get(values.get(keys.indexOf(key)).size()-1);
        int inx = keys.indexOf(key);
        if(inx==-1)
            return null;
        return getLastValue(inx);
    }
    
    public String getLastValue(int inx) {
        ArrayList<String> vals = values.get(inx);
        return vals.get(vals.size()-1);
    }
}
