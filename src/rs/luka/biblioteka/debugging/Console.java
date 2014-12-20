package rs.luka.biblioteka.debugging;

import bsh.EvalError;
import bsh.Interpreter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import javax.swing.JFrame;
import javax.swing.JPanel;
import rs.luka.biblioteka.data.*;
import rs.luka.biblioteka.funkcije.*;
import rs.luka.biblioteka.grafika.*;

/**
 * @author luka
 */
public class Console {

    private final String config = getAllMethods(Config.class);
    private final String datumi = getAllMethods(Datumi.class);
    private final String podaci = getAllMethods(Podaci.class);
    private final String init = getAllMethods(Init.class);
    private final String logger = getAllMethods(Logger.class);
    private final String save = getAllMethods(Save.class);
    private final String pretraga = getAllMethods(Pretraga.class);
    private final String undo = getAllMethods(Undo.class);
    private final String utils = getAllMethods(Utils.class);
    private final String dijalozi = getAllMethods(Dijalozi.class);
    private final String grafika = getAllMethods(Grafika.class);

    /**
     *
     */
    public void console() throws EvalError, IOException {
        JFrame frame = new JFrame();
        JPanel pan = new JPanel(null);
        frame.setLocationRelativeTo(null);
        frame.setSize(500, 500);
        frame.setContentPane(pan);
        bsh.util.JConsole console = new bsh.util.JConsole();
        console.setBounds(0, 0, 500, 500);
        pan.add(console);
        Interpreter interpreter = new Interpreter( console );
        new Thread( interpreter ).start();
        interpreter.print("Konzola za biblioteku. Odavde se mogu pokretati sve public funkcije.\n");
        frame.setVisible(true);
    }
    
    public void fullConsoleWindow() {
        bsh.Console.main(null);
    }

    private String help() {
        StringBuilder appMethods = new StringBuilder();
        appMethods.append(config).append(datumi).append(podaci).append(init).append(pretraga).append(logger)
                .append(save).append(undo).append(utils).append(dijalozi).append(grafika);
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
