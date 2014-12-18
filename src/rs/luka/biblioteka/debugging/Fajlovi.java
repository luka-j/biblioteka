package rs.luka.biblioteka.debugging;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;
import java.util.logging.Logger;

/**
 * Test klasa.
 *
 * @author Luka
 */
public class Fajlovi {

    /**
     * Cita fajlove i ispisuje procitano. Test metoda.
     */
    public void procitaj() {
        try {
            System.out.println("Config:");
            try (Scanner config = new Scanner(new FileReader(new File("config.properties")))) {
                while (config.hasNextLine()) {
                    System.out.println(config.nextLine());
                }
            }

            System.out.println("");

            System.out.println("Knjige:");
            try (Scanner knjige = new Scanner(new FileReader(new File("Data\\Knjige.dat")))) {
                while (knjige.hasNextLine()) {
                    System.out.println(knjige.nextLine());
                }
            }

            System.out.println("");

            System.out.println("Ucenici:");
            try (Scanner ucenici = new Scanner(new FileReader(new File("Data\\Ucenici.dat")))) {
                while (ucenici.hasNextLine()) {
                    System.out.println(ucenici.nextLine());
                }
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }
    private static final Logger LOG = Logger.getLogger(Fajlovi.class.getName());
}
