package rs.luka.biblioteka.grafika;

import java.util.logging.Level;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.showMessageDialog;
import rs.luka.biblioteka.data.Podaci;
import static rs.luka.biblioteka.data.Podaci.getUcenik;
import rs.luka.biblioteka.exceptions.Duplikat;
import rs.luka.biblioteka.exceptions.NemaViseKnjiga;
import rs.luka.biblioteka.exceptions.PreviseKnjiga;
import rs.luka.biblioteka.exceptions.VrednostNePostoji;

/**
 * @author Luka
 */
public class Uzimanje {
    
        private static final java.util.logging.Logger LOGGER =
                java.util.logging.Logger.getLogger(Ucenici.class.getName());

    /**
     * Iscrtava prozor za iznajmljivanje knjiga od biblioteke.
     *
     * @param indexUcenika index ucenika koji uzima knjigu
     */
    public void uzmi(int indexUcenika) {
        String knjiga = Dijalozi.showTextFieldDialog
                        ("Uzimanje knjige", "Unesite naslov knjige koju učenik uzima:", "");
            try {
                Podaci.uzmiKnjigu(indexUcenika, Podaci.getKnjiga(knjiga)); //šaljem objekat knjiga, ne naslov
                showMessageDialog(null, "Sve je u redu! "
                        + "Knjiga dodata kod ucenika, i oduzeta od biblioteke", "Uspeh!",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (VrednostNePostoji ex) {
                switch (ex.getMessage()) {
                    case "Knjiga":
                        LOGGER.log(Level.INFO, "Uneta knjiga {0} ne postoji", knjiga);
                        showMessageDialog(null, "Naslov koji ste uneli ne postoji \n"
                                + "Proverite da li ste ispravno upisali naziv i pokušajte ponovo.",
                                "Nepostojeća knjiga", JOptionPane.ERROR_MESSAGE);
                        break;
                    default: 
                        LOGGER.log(Level.SEVERE, "Nepostojeća VrednostNePostoji "
                                + "u uzimanju: {0}", ex.getMessage());
                        throw new RuntimeException("Invalid VrednostNePostoji vrednost");
                        //menjao ovaj deo. ne bi trebalo da dođe do default, ako dođe, bolje da znam
                }
            } catch (NemaViseKnjiga ex) {
                LOGGER.log(Level.INFO, "Više nema knjiga naslova {0}", knjiga);
                showMessageDialog(null, "Više nema knjiga tog naslova",
                        "Nema knjiga", JOptionPane.ERROR_MESSAGE);
            } catch (Duplikat ex) {
                LOGGER.log(Level.INFO, "Učenik {0} je već iznajmio knjigu {1}",
                                new Object[]{getUcenik(indexUcenika).getIme(), knjiga});
                showMessageDialog(null, "Učenik je vec iznajmio knjigu tog naslova.",
                        "Duplikat", JOptionPane.ERROR_MESSAGE);
            } catch (PreviseKnjiga ex) {
                LOGGER.log(Level.INFO, "Učenik {0} ima previše knjiga kod sebe",
                        ex.getMessage());
                showMessageDialog(null, "Učenik trenutno ima previše knjiga kod sebe :\n",
                        "Previše knjiga", JOptionPane.ERROR_MESSAGE);
            }
    }
}
