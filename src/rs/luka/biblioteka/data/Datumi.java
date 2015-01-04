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
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.INFORMATION_MESSAGE;
import static javax.swing.JOptionPane.showMessageDialog;
import rs.luka.biblioteka.funkcije.Utils;
import static rs.luka.biblioteka.grafika.Konstante.*;

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
    private static long razlika; //field zbog lambde

    /**
     * Proverava da li neki od ucenika zadrzava knjigu predugo. Limit se uzima
     * iz dateLimit polja u configu.
     */
    public static void proveriDatum() {
        Date date = new Date();
        StringBuilder predugo = new StringBuilder();
        try (final FileWriter predugoFW = new FileWriter(new File(Utils.getWorkingDir() + "predugo.txt"))) {
            int limit = parseInt(Config.get("dateLimit"));
            if(limit==0) {
                return;
            }
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
                predugoFW.write("Učenici koji drze knjige predugo kod sebe:\n" + predugoStr);
                showMessageDialog(null, DATUMI_INFO_MSG1_STRING + predugoStr + DATUMI_INFO_MSG2_STRING,
                        DATUMI_INFO_TITLE_STRING, INFORMATION_MESSAGE);
            }
        } catch (FileNotFoundException ex) {
            LOGGER.log(Level.SEVERE, "Konfiguracijski ili fajl sa listom učenika koji"
                    + " imaju knjige predugo kod sebe nije prodađen!", ex);
            showMessageDialog(null, DATUMI_FNFEX_MSG_STRING, DATUMI_FNFEX_TITLE_STRING, ERROR_MESSAGE);
            
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Greška pri čitanju fajla", ex);
            showMessageDialog(null, DATUMI_IOEX_MSG_STRING, DATUMI_IOEX_TITLE_STRING, ERROR_MESSAGE);
        }
    }

    private Datumi() {
    }
}
