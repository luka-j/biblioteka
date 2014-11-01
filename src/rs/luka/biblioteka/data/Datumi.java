package rs.luka.biblioteka.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import static java.lang.Integer.parseInt;
import static java.lang.String.valueOf;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.logging.Level;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.showMessageDialog;
import rs.luka.biblioteka.funkcije.Utils;

/**
 *
 * @author luka
 * @since 23.8.'14.
 */
public class Datumi {
    
    private static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(Datumi.class.getName());
    
    private static final int SEKUNDI_U_DANU = 86_400_000;
    /**
     * DateFormat koriscen za I/O.
     */
    public static final SimpleDateFormat df = new SimpleDateFormat("ddMMyy");
    private static long razlika;

    /**
     * Proverava da li neki od ucenika zadrzava knjigu predugo. Limit se uzima
     * iz dateLimit polja u configu.
     */
    public static void proveriDatum() {
        Date date = new Date();
        StringBuilder predugo = new StringBuilder();
        try (final FileWriter predugoFW = new FileWriter(new File(Utils.getWorkingDir() + "predugo.txt"))) {
            int limit = parseInt(Config.get("dateLimit"));
            if(limit==0) return;
            Iterator<Ucenik> it = Podaci.iteratorUcenika();
            it.forEachRemaining((Ucenik uc) -> {
                for (int j = 0; j < uc.getMaxBrojKnjiga(); j++) {
                    if (!uc.isKnjigaEmpty(j) && (razlika = 
                            date.getTime() / SEKUNDI_U_DANU - uc.getTimeKnjige(j) / SEKUNDI_U_DANU) > limit) {
                        predugo.append(uc.getIme()).append(" : ").append(uc.getNaslovKnjige(j)).append(", ")
                                .append(valueOf(razlika)).append(" dana").append('\n');
                    }
                }
            });
            String predugoStr = predugo.toString();
            if (!predugoStr.isEmpty()) {
                LOGGER.log(Level.FINE, "Provera datuma gotova. Neki učenici imaju knjige predugo kod sebe.");
                showMessageDialog(null, "Učenici koji predugo imaju knjige kod sebe:\n"
                        + predugoStr + "Imena su sačuvana u fajl predugo.txt",
                        "Neki učenici nisu vratili knjige na vreme!", JOptionPane.INFORMATION_MESSAGE);
                predugoFW.write("Učenici koji drze knjige predugo kod sebe:\n" + predugoStr);
            }
        } catch (FileNotFoundException ex) {
            LOGGER.log(Level.SEVERE, "Konfiguracijski ili fajl sa listom učenika koji"
                    + " imaju knjige predugo kod sebe nije prodađen!", ex);
            showMessageDialog(null, "Konfiguracijski ili fajl sa listom učenika "
                    + "koji drže knjige predugo kod sebe nije pronađen.\nProvera "
                    + "datuma je neuspešno obavljena.", "Greška", JOptionPane.ERROR_MESSAGE);
            
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Greška pri čitanju fajla", ex);
            showMessageDialog(null, "Doslo je do greške pri čitanju konfiguracijskog"
                    + " ili fajla sa listom učenika koji imaju knjige predugo kod sebe\n"
                    + "Provera datuma nije uradjena", "I/O Greška", JOptionPane.ERROR_MESSAGE);
        }
    }
}
