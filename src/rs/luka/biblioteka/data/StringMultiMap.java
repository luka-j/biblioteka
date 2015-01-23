package rs.luka.biblioteka.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * StringMultiMap implementacija koja koristi 2 ArrayLista kao osnovu. Jedan sadrzi kljuceve, drugi listu Strigova
 * koji predstavljaju vrednosti. Dozvoljava null vrednosti.
 * @author luka
 * @since 25.10.'14.
 */
public class StringMultiMap extends HashMap<String, ArrayList<String>> {
    
    public StringMultiMap() {
        super();
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
            return super.entrySet().stream().anyMatch((vals) -> (vals.getValue().stream().anyMatch((val) -> (val.equalsIgnoreCase((String)value)))));
        } else if(value instanceof List) {
            return super.containsValue(value);
        } else return false;
    }
    
    /**
     * Povezuje datu vrednost sa kljucem. Ako kljuc ne postoji, kreira ga. U suprotom, dodaje vrednost
     * kod kljuca. Ako vrednost vec postoji, mapa ostaje nepromenjena.
     * @param key kljuc za koji se dodaje vrednost
     * @param val vrednost koja se dodaje
     * @since 25.10.'14.
     */
    public void put(String key, String val) {
        if(this.containsKey(key)) {
            this.get(key).add(val);
        } else {
            ArrayList<String> list = new ArrayList<>();
            list.add(val);
            super.put(key, list);
        }
    }
    
    /**
     * Stavlja novi kljuc u listu. Ako je prethodno postojao kljuc sa istim imenom, brise ga.
     * @param key kljuc
     * @param vals vrednosti za kljuc
     * @since 25.10.'14.
     */
    public void put(String key, String... vals) {
        super.put(key, new ArrayList<>(Arrays.asList(vals)));
    }
    
    public void put(String key, int... vals0) {
        String[] vals = new String[vals0.length];
        for(int i=0; i<vals0.length; i++) {
            vals[i] = String.valueOf(String.valueOf(vals0[i]));
        }
        super.put(key, new ArrayList<>(Arrays.asList(vals)));
    }
    
    /**
     * Vraca kljuc koji je povezan sa ovom vrednoscu. Ako takav kljuc ne postoji, vraca null.
     * @param value vrednost za koju se trazi kljuc
     * @return kljuc koji sadrzi datu vrednost
     * @since 25.10.'.14
     */
    public String getKey(String value) {
        Set<Entry<String, ArrayList<String>>> map = super.entrySet();
        ArrayList<String> vals;
        for(Entry<String, ArrayList<String>> entry : map) {
            vals = entry.getValue();
            for(String val : vals) {
                if(val.equalsIgnoreCase(value))
                    return entry.getKey();
            }
        }
        return null;
    }
    
    public boolean contains(String str) {
        return containsKey(str) || containsValue(str);
    }
}
