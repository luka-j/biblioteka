package rs.luka.biblioteka.debugging;

import bsh.EvalError;
import bsh.Interpreter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * @author luka
 */
public class Console {

    /**
     * Crta bsh konzolu.
     * 
     * @throws bsh.EvalError ako dodje do greske u Interpreter-u bsh-a
     * @throws java.io.IOException ??
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
    
    /**
     * Crta bsh desktop.
     */
    public void fullConsoleWindow() {
        bsh.Console.main(null);
    }
    
    /**
     * Vraca sve metode neke klase.
     * @param klasa klasa koja se pretrazuje
     * @return public static metode te klase, razdvojene \n
     */
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
