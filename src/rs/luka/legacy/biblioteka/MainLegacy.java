package rs.luka.legacy.biblioteka;

import rs.luka.biblioteka.grafika.Unos;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author Luka
 */
public class MainLegacy {

    /**
     * Lista sa naslovima.
     */
    private static final List<String> n = new ArrayList<>();
    /**
     * Lista sa kolicinama naslova.
     */
    private static final List<Integer> kol = new ArrayList<>();
    /**
     * Lista sa imenom i prezimenom ucenika.
     */
    private static final List pu = new ArrayList();
    /**
     * Lista sa jednom knjigom kod ucenika Note: JEDNOM - ne prvom.
     */
    private static final List<String> knj1 = new ArrayList<>();
    /**
     * Lista sa jednom knjigom kod ucenika Note: JEDNOM - ne prvom.
     */
    private static final List<String> knj2 = new ArrayList<>();
    /**
     * Lista sa jednom knjigom kod ucenika Note: JEDNOM - ne prvom.
     */
    private static final List<String> knj3 = new ArrayList<>();

    public static void mainLegacy(String[] args) {
//        Main utils = new Main();
//        JFrame initWin = new JFrame("Ucitavanje podataka...");
//        initWin.setLocationRelativeTo(null);
//        initWin.setSize(250, 80);
//        initWin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        JPanel initPan = new JPanel();
//        initPan.setBackground(Color.green);
//        initWin.setContentPane(initPan);
//        JLabel initLab = new JLabel("Ucitavanje podataka...");
//        initPan.add(initLab);
//        initWin.setVisible(true);
//        try {
//            Unos unos = new Unos();
//            File knjige = new File("Data\\naslov.txt");
//            File ucenici = new File("Data\\Podaci Ucenika.txt");
//            if (!knjige.exists() && !ucenici.exists()) {
//                unos.Unos();
//                return;
//            }
//            if (!knjige.exists()) {
//                unos.UnosKnjige();
//                return;
//            }
//            if (!ucenici.exists()) {
//                unos.UnosUcenici();
//                return;
//            }
//            try (final Scanner inN = new Scanner(new BufferedReader(new FileReader(knjige)))) {
//                Scanner inU = new Scanner(new BufferedReader(new FileReader(ucenici)));
//                String in;
//                while (!"\n".equals(in = inN.nextLine())) {
//                    utils.AddN(in);
//                }
//                while (!"\n".equals(in = inN.nextLine())) {
//                    utils.AddKol(Integer.parseInt(in));
//                }
//                int countUc = 0;
//                while (!"\n".equals(in = inU.nextLine())) {
//                    countUc++;
//                    utils.addU(in);
//                }
//                try {
//                    for (int i = 0; i < countUc; i++) {
//                        utils.addKnj1(inU.nextLine());
//                    }
//                    inU.nextLine();
//                    for (int i = 0; i < countUc; i++) {
//                        utils.addKnj2(inU.nextLine());
//                    }
//                    inU.nextLine();
//                    for (int i = 0; i < countUc; i++) {
//                        utils.addKnj3(inU.nextLine());
//                    }
//                } catch (NoSuchElementException ex) {
//                    JOptionPane.showMessageDialog(null, "Nedovoljno linija unosa\n" + "Greska pri ucitavanju", "Greska!", JOptionPane.ERROR_MESSAGE);
//                }
//            }
//        } catch (FileNotFoundException ex) {
//            JOptionPane.showMessageDialog(null, "Ucitavanje podataka nije uspelo:\n" + 
//                    "Fajl nije pronadjen.", "Greska pri ucitavanju.", 0);
//            ex.printStackTrace(System.out);
//        }
//        initWin.dispose();
    }
    //
    //---~~~---~~~---~~~---~~~---~~~
    //
    /**
     * @param i index
     * @return n.get(i).toString();
     */
//    public String GetN(int i) {
//        return Main.n.get(i).toString();
//    }
//
//    /**
//     * @param s String koji se trazi
//     * @return indexOf(s)
//     */
//    public int FindN(String s) {
//        return Main.n.indexOf(s);
//    }
//
//    /**
//     * @param i index
//     * @return kol.get(i);
//     */
//    public int GetKol(int i) {
//        return Main.kol.get(i);
//    }
//
//    /**
//     * @return n.size();
//     */
//    public int GetBn() {
//        return Main.n.size();
//    }
//    
//    /**
//     *
//     * @param uc ucenik
//     * @return broj knjiga koje su kod tog ucenika
//     * @since 2.7.'13.
//     */
//    public int getKnjNum(String uc) {
//        final int inx = Main.pu.indexOf(uc);
//        int count = 0;
//        if(!isKnj1Empty(inx)) count++;
//        if(!isKnj2Empty(inx)) count++;
//        if(!isKnj3Empty(inx)) count++;
//        return count;
//    }
//
//    /**
//     * @deprecated non-synchronized. Moze da doda naslov, ali ne i kolicinu, i
//     * time dovesti do gresaka u obe liste
//     * @see AddNas Dodaje string u List n
//     *
//     * @param s string koji treba dodati.
//     */
//    public void AddN(String s) {
//        Main.n.add(s);
//    }
//
//    /**
//     * Dodaje naslov i kolicinu.
//     *
//     * @param nas naslov
//     * @param kol kolicina
//     * @since The end of July
//     */
//    public void addNas(String nas, int kol) {
//        Main.n.add(nas);
//        Main.kol.add(kol);
//    }
//
//    /**
//     * Dodaje ucenika i knjige.
//     *
//     * @param uc ucenik
//     * @param knj1 knjiga
//     * @param knj2 knjiga
//     * @param knj3 knjiga
//     * @since the end of July
//     */
//    public void addUc(String uc, String knj1, String knj2, String knj3) {
//        Main.pu.add(uc);
//        Main.knj1.add(knj1);
//        Main.knj2.add(knj2);
//        Main.knj3.add(knj3);
//    }
//
//    /**
//     *
//     * @param nas naslov
//     * @param kol kolicina
//     * @since the end of July
//     */
//    @SuppressWarnings("empty-statement")
//    public void addNSort(String nas, int kol) {
//        char prvi = nas.charAt(0);
//        int i = 0;
//        for (; prvi <= Main.n.get(i).charAt(0); i++) 
//           ;
//        Main.n.add(i, nas);
//        Main.kol.add(i, kol);
//    }
//
//    /**
//     * @deprecated @see addN(String n)
//     * @see addNas(String nas, int kol)
//     *
//     * Dodaje int u List kol
//     *
//     * @param k int koji treba dodati
//     */
//    public void AddKol(int k) {
//
//        Main.kol.add(k);
//    }
//
//    /**
//     * Smanjuje ili povecava kol za 1
//     *
//     * @param i index
//     * @param b true povecava kol[i] za 1, false smanjuje kol[i] za 1
//     */
//    public void SetKol(int i, boolean b) {
//        int conv = kol.get(i);
//        if (b) {
//            Main.kol.set(i, conv++);
//        }
//        else {
//            Main.kol.set(i, conv--);
//        }
//    }
//
//    /**
//     * @param str case "n" - list n. case "kol" - list kol. case "uc" - list pu.
//     * case knj1-3 - list knj1-3
//     * @return List u obliku String, svaki clan u novom redu.
//     * @throws UnsupportedOperationException - ako se string ne poklapa ni sa
//     * cim.
//     */
//    public String ArrayListToString(String str) {
//        String ret = "";
//        switch (str) {
//            case "n":
//                for (int i = 0; i < Main.n.size(); i++) {
//                ret += Main.n.get(i).toString() + "\n";
//            }
//                return ret;
//            case "kol":
//                for (int i = 0; i < Main.n.size(); i++) {
//                ret += Main.kol.get(i).toString() + "\n";
//            }
//                return ret;
//            case "uc":
//                for (int i = 0; i < Main.pu.size(); i++) {
//                ret += Main.pu.get(i).toString() + "\n";
//            }
//                return ret;
//            case "knj1":
//                for (int i = 0; i < Main.knj1.size(); i++) {
//                ret += Main.knj1.get(i).toString() + "\n";
//            }
//                return ret;
//            case "knj2":
//                for (int i = 0; i < Main.knj2.size(); i++) {
//                ret += Main.knj2.get(i).toString() + "\n";
//            }
//                return ret;
//            case "knj3":
//                for (int i = 0; i < Main.knj3.size(); i++) {
//                ret += Main.knj3.get(i).toString() + "\n";
//            }
//                return ret;
//            default:
//                throw new UnsupportedOperationException("Ne postoji.");
//        }
//    }
//
//    public int FindUc(String uc) {
//        return Main.pu.indexOf(uc);
//    }
//
//    /**
//     * @param i index ucenika
//     * @return knj1.get(i)
//     * @deprecated @see GetKnj(int n, int i)
//     */
//    public String GetKnj1(int i) {
//        return Main.knj1.get(i).toString();
//    }
//
//    /**
//     * @param i index ucenika
//     * @return knj2.get(i)
//     * @deprecated @see GetKnj(int n, int i)
//     */
//    public String GetKnj2(int i) {
//        return Main.knj2.get(i).toString();
//    }
//
//    /**
//     * @param i index ucenika
//     * @return knj3.get(i)
//     * @deprecated @see GetKnj(int n, int i)
//     */
//    public String GetKnj3(int i) {
//        return Main.knj3.get(i).toString();
//    }
//
//    /**
//     * @param n case 1: knj1.get(i); case: knj2.get(i); case 3: knj3.get(i)
//     * @param i index
//     * @return String i-te knjige u n-toj listi
//     * @throws UnsupportedOperationException - ako se int n ne poklapa ni sa
//     * cim.
//     */
//    public String GetKnj(int n, int i) {
//        switch (n) {
//            case 1:
//                return Main.knj1.get(i).toString();
//            case 2:
//                return Main.knj2.get(i).toString();
//            case 3:
//                return Main.knj3.get(i).toString();
//            default:
//                throw new UnsupportedOperationException("Ne postoji.");
//        }
//    }
//    
//    public String GetKnj(int n, String uc) {
//        return GetKnj(n, Main.pu.indexOf(uc));
//    }
//
//    /**
//     * @param i index
//     * @return true ako je polje prazno(tj. ""), false ako nije
//     */
//    public boolean isKnj1Empty(int i) {
//        return "".equals(Main.knj1.get(i).toString());
//    }
//
//    /**
//     * @param i index
//     * @return true ako je polje prazno(tj. ""), false ako nije
//     */
//    public boolean isKnj2Empty(int i) {
//        return "".equals(Main.knj2.get(i).toString());
//    }
//
//    /**
//     * @param i index
//     * @return true ako je polje prazno(tj. ""), false ako nije
//     */
//    public boolean isKnj3Empty(int i) {
//        return "".equals(Main.knj3.get(i).toString());
//    }
//
//    /**
//     * @param i index ucenika
//     * @param knj knjiga koju uzima
//     * @return 0 ako je sve OK. 1 ako je puno.
//     */
//    public int setKnj(int i, String knj) {
//        if (isKnj1Empty(i)) {
//            Main.knj1.set(i, knj);
//            return 0;
//        }
//        if (isKnj2Empty(i)) {
//            Main.knj2.set(i, knj);
//            return 0;
//        }
//        if (isKnj3Empty(i)) {
//            Main.knj3.set(i, knj);
//            return 0;
//        }
//        return 1;
//    }
//
//    /**
//     * @param i index ucenika
//     * @return broj knjiga koje su kod tog ucenika
//     */
//    public int brKnj(int i) {
//        int count = 0;
//        if (!isKnj1Empty(i)) {
//            count++;
//        }
//        if (!isKnj2Empty(i)) {
//            count++;
//        }
//        if (!isKnj3Empty(i)) {
//            count++;
//        }
//        return count;
//    }
//
//    /**
//     * Brise(tj. postavlja na "") knjigu na i-tom mestu liste knj1
//     *
//     * @param i index ucenika
//     */
//    public void clearKnj1(int i) {
//        Main.knj1.set(i, "");
//    }
//
//    /**
//     * Brise(tj. postavlja na "") knjigu na i-tom mestu liste knj2
//     *
//     * @param i index ucenika
//     */
//    public void clearKnj2(int i) {
//        Main.knj2.set(i, "");
//    }
//
//    /**
//     * Brise(tj. postavlja na "") knjigu na i-tom mestu liste knj3
//     *
//     * @param i index ucenika
//     */
//    public void clearKnj3(int i) {
//        Main.knj3.set(i, "");
//    }
//    
//    /**
//     *
//     * @param n lista(knj<tt>n</tt>)
//     * @param i index
//     * @since 2.7.'13.
//     */
//    public void clearKnj(int n, int i) {
//        switch(n) {
//            case 1: clearKnj1(i); break;
//            case 2: clearKnj2(i); break;
//            case 3: clearKnj3(i); break;
//            default: throw new UnsupportedOperationException("Ne postoji.");
//        }
//    }
//    
//    /**
//     * @since 2.7.'13
//     * @param knj naslov knjige
//     * @param index index ucenika
//     */
//    public void clearKnj(String knj, int index) {
//        if(knj.equalsIgnoreCase(Main.knj1.get(index))) {
//            clearKnj1(index);
//            return;
//        } 
//        if(knj.equalsIgnoreCase(Main.knj2.get(index))) {
//            clearKnj2(index);
//            return;
//        }
//        if(knj.equalsIgnoreCase(Main.knj3.get(index))) {
//            clearKnj3(index);
//        }
//    }
//    /**
//     * @return broj ucenika
//     */
//    public int brU() {
//        return Main.pu.size();
//    }
//
//    /**
//     * @deprecated non-synchronized. Moze da doda ucenika, ali ne i knjige, i
//     * time dovesti do gresaka u obe liste.
//     *
//     * Dodaje ucenika.
//     *
//     * @param uc ucenik
//     */
//    public void addU(String uc) {
//        Main.pu.add(uc);
//    }
//
//    /**
//     * @deprecated @see addU(String uc)
//     * @see addUc(String uc, String knj1, String knj2, String knj3)
//     *
//     * Dodaje knjigu u listu knj1
//     *
//     * @param knj1 knjiga
//     */
//    public void addKnj1(String knj1) {
//        Main.knj1.add(knj1);
//    }
//
//    /**
//     * @deprecated @see addU(String uc)
//     * @see addUc(String uc, String knj1, String knj2, String knj3)
//     *
//     * Dodaje knjigu u listu knj2
//     *
//     * @param knj2 knjiga
//     */
//    public void addKnj2(String knj2) {
//        Main.knj2.add(knj2);
//    }
//
//    /**
//     * @deprecated @see addU(String uc)
//     * @see addUc(String uc, String knj1, String knj2, String knj3)
//     *
//     * Dodaje knjigu u listu knj3
//     *
//     * @param knj3 knjiga
//     */
//    public void addKnj3(String knj3) {
//        Main.knj3.add(knj3);
//    }
//    
//    /**
//     *
//     * @param ime ime ucenika
//     * @return 1 - ucenik ne postoji, 0 - sve u redu.
//     * @since 31.6.'13 || 1.7.'13.
//     */
//    public int delUc(String ime) {
//        int inx = Main.pu.indexOf(ime);
//        try {
//        Main.pu.remove(inx);
//        Main.knj1.remove(inx);
//        Main.knj2.remove(inx);
//        Main.knj3.remove(inx);
//        return 0;
//        }
//        catch(IndexOutOfBoundsException ex)  {
//            return 1;
//        }
//    }
}
