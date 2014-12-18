package rs.luka.legacy.biblioteka;

import java.io.IOException;
import java.util.List;
import javax.swing.JOptionPane;
import rs.luka.biblioteka.data.Podaci;
import rs.luka.biblioteka.funkcije.Save;

/**
 *
 * @author root
 */
public class UnosLegacy {

    /**
     * @deprecated @see grafika.UnosGrafika
     */
    public void UnosKnjige() {
        boolean end = false, broj = false;
        String nStr, kStr;
        int count = 0, k = 0;
        while (!end) {
            nStr = JOptionPane.showInputDialog("Unesite naziv " + (count + 1) + ". naslova"
                    + "\n Ostavite polje prazno ako vise nema naslova");

            if ("".equals(nStr)) {
                end = true;
            } else {
                //Main.n.add(nStr);
                broj = false;
                while (!broj) {
                    kStr = JOptionPane.showInputDialog("Unesite broj knjiga " /*          + Main.n.get(count)*/);
                    try {
                        k = Integer.parseInt(kStr);
                        broj = true;
                    } catch (NumberFormatException e2) {
                        JOptionPane.showMessageDialog(null, "Neispravan unos \n"
                                + e2.getMessage());
                        broj = false;
                    }
                }
            }
            //Main.kol.add(k);

            count++;
        }
    }

    /**
     * @deprecated koristiti metodu sa swing grafikom
     * @see grafika.UnosGrafika Unos metoda za ucenike
     */
    public void UnosUcenici() {
//        Save SR = new Save();
        String ime, knj;
        boolean end = false;
        while (!end) {
            ime = JOptionPane.showInputDialog("Unesite ime i prezime sledeceg ucenika \nOstavite prazno ako vise nema ucenika");
            // Main.pu.add(ime);
            if ("".equals(ime)) {
                end = true;
            } else {
                knj = JOptionPane.showInputDialog("Unesite prvu knjigu koje je trenutno kod tog ucenika. \nOstavite prazno ako ucenik nije iznajmio nijednu knjigu");
                if (!"".equals(knj)) {
                    // Main.knj1.add(knj);
                    knj = JOptionPane.showInputDialog("Unesite drugu knjigu koja je trenutno kod tog ucenika. \nOstavite prazno ako ucenik nije iznajmio vise knjiga");
                    if (!"".equals(knj)) {
                        //  Main.knj2.add(knj);
                        knj = JOptionPane.showInputDialog("Unesite trecu knjigu koja je trenutno kod tog ucenika. \nOstavite prazno ako ucenik nije iznajmio vise knjiga");
                        if (!"".equals(knj)) {
                            //    Main.knj3.add(knj);
                            JOptionPane.showMessageDialog(null, "Kod ucenika su trenutno 3 knjige.");
                        } else {
                            //  Main.knj3.add("");
                        }
                    } else {
                        //   Main.knj2.add("");
                        //   Main.knj3.add("");
                    }
                } else {
                    // Main.knj1.add("");
                    // Main.knj2.add("");
                    // Main.knj3.add("");
                }
            }
        }
        try {
            Save.saveKnjige();
        } catch (IOException ex) {
//            Logger.getLogger(Podaci.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @param s ime liste
     * @return lista
     * @deprecated Koristiti ArraylistToString. Uradjeno sa if, itd., a ne sa
     * switch n vraca ArrayList n(naslove) kol vraca ArrayList kol(kolicina) pu
     * vraca ArrayList pu(prodaci ucenika, ime i prezime) knj1 vraca ArrayList
     * knj1(prva knjiga koja je kod i-tog ucenika knj2 vraca ArrayList
     * knj2(druga knjiga koja je kod i-tog ucenika sve ostalo vraca ArrayList
     * knj3(treca knjiga koja je kod i-tog ucenika)
     */
    public List GetList(String s) {
        return null;
        /* if ("n".equals(s)) {
         return Collections.unmodifiableList(Main.n);
         }
         if ("kol".equals(s)) {
         return Collections.unmodifiableList(Main.kol);
         }
         if ("uc".equals(s)) {
         return Collections.unmodifiableList(Main.pu);
         }
         if ("knj1".equals(s)) {
         return Collections.unmodifiableList(Main.knj1);
         }
         if ("knj2".equals(s)) {
         return Collections.unmodifiableList(Main.knj2);
         }
         return Collections.unmodifiableList(Main.knj3);
         */ }
}
