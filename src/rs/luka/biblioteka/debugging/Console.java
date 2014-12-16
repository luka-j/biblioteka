package rs.luka.biblioteka.debugging;

import rs.luka.legacy.biblioteka.Knjige;
import rs.luka.legacy.biblioteka.Ucenici;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import javax.swing.JOptionPane;
import rs.luka.biblioteka.data.*;
import rs.luka.biblioteka.funkcije.*;
import rs.luka.biblioteka.grafika.Grafika;
import rs.luka.biblioteka.grafika.Dijalozi;

/**
 * @author luka
 */
public class Console {

    private final String config = getAllMethods(Config.class);
    private final String datumi = getAllMethods(Datumi.class);
    private final String podaci = getAllMethods(Podaci.class);
    private final String init = getAllMethods(Init.class);
    private final String knjige = getAllMethods(Knjige.class);
    private final String logger = getAllMethods(Logger.class);
    private final String save = getAllMethods(Save.class);
    private final String ucenici = getAllMethods(Ucenici.class);
    private final String undo = getAllMethods(Undo.class);
    private final String utils = getAllMethods(Utils.class);
    private final String dijalozi = getAllMethods(Dijalozi.class);
    private final String grafika = getAllMethods(Grafika.class);

    public void console() {
        JOptionPane.showMessageDialog(null, "Trenutno radi samo za static metode bez argumenata");
        String komanda = Dijalozi.showTextFieldDialog("Konzola",
                "Unesite komandu u formi funkcija:arg1,arg2,...", "help");
        if(komanda.isEmpty()) 
            return;
        if (komanda.equals("help")) 
            System.out.println(help());
        else {
            String[] split = komanda.split(":");
            Object[] args = new Object[split.length - 1];
            for (int i = 1; i < split.length; i++) {
                args[i - 1] = split[i];
            }
            try {
                if (config.contains(split[0])) {
                    Config.class.getMethod(komanda).invoke(null, args);
                } else if (datumi.contains(split[0])) {
                    Datumi.class.getMethod(komanda).invoke(null, args);
                } else if (podaci.contains(split[0])) {
                    Podaci.class.getMethod(komanda).invoke(null, args);
                } else if (init.contains(split[0])) {
                    Init.class.getMethod(komanda).invoke(null, args);
                } else if (knjige.contains(split[0])) {
                    Knjige.class.getMethod(komanda).invoke(null, args);
                } else if (logger.contains(split[0])) {
                    Logger.class.getMethod(komanda).invoke(null, args);
                } else if (save.contains(split[0])) {
                    Save.class.getMethod(komanda).invoke(null, args);
                } else if (ucenici.contains(split[0])) {
                    Ucenici.class.getMethod(komanda).invoke(null, args);
                } else if (undo.contains(split[0])) {
                    Undo.class.getMethod(komanda).invoke(null, args);
                } else if (utils.contains(split[0])) {
                    Utils.class.getMethod(komanda).invoke(null, args);
                } else if (dijalozi.contains(split[0])) {
                    Dijalozi.class.getMethod(komanda).invoke(null, args);
                } else if (grafika.contains(split[0])) {
                    Grafika.class.getMethod(komanda).invoke(null, args);
                } else {
                    JOptionPane.showMessageDialog(null, "Metoda nije pronadjena");
                    console();
                }
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException ex) {
                JOptionPane.showMessageDialog(null, "Greska pri pozivanju metode: " + ex.getMessage());
                console();
            }
        }
    }

    private String help() {
        StringBuilder appMethods = new StringBuilder();
        appMethods.append(config).append(datumi).append(podaci).append(init).append(knjige).append(logger)
                .append(save).append(ucenici).append(undo).append(utils).append(dijalozi).append(grafika);
        return appMethods.toString();
    }
    
    private String getAllMethods(Class<?> klasa) {
        Method[] metode = klasa.getDeclaredMethods();
        StringBuilder retString = new StringBuilder();
        for (Method metoda : metode) {
            if (Modifier.isPublic(metoda.getModifiers()) && Modifier.isStatic(metoda.getModifiers())) {
                retString.append(metoda.toString()).append('\n');
            }
        }
        return retString.toString();
    }
}
