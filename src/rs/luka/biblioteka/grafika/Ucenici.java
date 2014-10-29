package rs.luka.biblioteka.grafika;

import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import static java.lang.Integer.parseInt;
import static java.lang.Integer.parseUnsignedInt;
import static java.lang.String.valueOf;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.showMessageDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import rs.luka.biblioteka.data.Config;
import rs.luka.biblioteka.data.Podaci;
import static rs.luka.biblioteka.data.Podaci.getMaxBrojUcenikKnjiga;
import static rs.luka.biblioteka.data.Podaci.getUcenik;
import static rs.luka.biblioteka.data.Podaci.indexOfUcenik;
import rs.luka.biblioteka.data.Ucenik;
import rs.luka.biblioteka.exceptions.PreviseKnjiga;
import rs.luka.biblioteka.exceptions.VrednostNePostoji;

/**
 * @since 1.7.'13.
 * @author Luka
 */
public class Ucenici implements FocusListener {

    private static final java.util.logging.Logger LOGGER
            = java.util.logging.Logger.getLogger(Ucenici.class.getName());

    /**
     * serchBox za pretrazivanje knjiga.
     */
    private final JTextField searchBox = new JTextField("Pretrazi ucenike...");

    private JCheckBox selectAllUc;
    /**
     * Matrix sa knjigama pocinje od [][1], na 0 su labeli "Knjiga".
     */
    private JCheckBox[][] knjige;
    private JCheckBox[] ucenici;

    private static JFrame win; //deo workarounda za restart prozora iz search

    /**
     * Pregled ucenika sa knjigama koje su trenutno kod njih.
     *
     * @since 1.7.'13.
     */
    public void pregledUcenika() {
        win = null; //deo workarounda za restart iz searcha. Traje dugacko ako se dosta puta koristi zbog
        win = new JFrame("Pregled učenika"); //rekurzije. Izbegavati, umesto toga koristiti ponovno pokretanje 
        //----------JFrame&panels-----------------------------------------------
        int sirina, visina;
        sirina = parseInt(Config.get("uceniciS", valueOf(165 * getMaxBrojUcenikKnjiga() + 350)));
        visina = parseInt(Config.get("uceniciV", "600"));
        win.setSize(sirina, visina);
        LOGGER.log(Level.CONFIG, "Postavljam visinu prozora sa učenicima na {0}, širinu na {1}",
                new Object[]{visina, sirina});
        win.setLocationRelativeTo(null);
        //win.setResizable(false);
        win.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        JPanel pan = new JPanel();
        pan.setLayout(new BoxLayout(pan, BoxLayout.X_AXIS));
        pan.setBackground(Grafika.getBgColor());
        pan.setAutoscrolls(true);
        JScrollPane scroll = new JScrollPane(pan);
        scroll.add(scroll.createVerticalScrollBar());
        JPanel butPan = new JPanel();
        butPan.setBackground(Grafika.getBgColor());
        butPan.setLayout(new FlowLayout(FlowLayout.CENTER));
        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, scroll, butPan);
        split.setOneTouchExpandable(false);
        split.setDividerLocation(visina - 90);
        JPanel uceniciPan = new JPanel();
        uceniciPan.setLayout(new BoxLayout(uceniciPan, BoxLayout.Y_AXIS));
        uceniciPan.setBackground(Grafika.getBgColor());
        JPanel[] knjigePan = new JPanel[getMaxBrojUcenikKnjiga()];
        for (int i = 0; i < getMaxBrojUcenikKnjiga(); i++) {
            knjigePan[i] = new JPanel();
            knjigePan[i].setLayout(new BoxLayout(knjigePan[i], BoxLayout.Y_AXIS));
            knjigePan[i].setBackground(Grafika.getBgColor());
        }
        JPanel sidePan = new JPanel(null);
        sidePan.setBackground(Grafika.getBgColor());
        sidePan.setPreferredSize(new Dimension(150, (Podaci.getBrojUcenika() + 1) * 25));
        win.setContentPane(split);
        //----------JLabels,JSeparators&JCheckBoxes-----------------------------
        Podaci.sortUcenike();
        int maxKnjiga = getMaxBrojUcenikKnjiga();
        refreshLabels(); //postavlja labele i ucenike (text)
        uceniciPan.add(selectAllUc);
        pan.add(uceniciPan);

        JSeparator[] ucSeparatori = new JSeparator[Ucenik.getBrojRazreda()];
        for (int i = 0; i < ucSeparatori.length; i++) {
            ucSeparatori[i] = new JSeparator(SwingConstants.HORIZONTAL);
        }
        int[] razredi = Podaci.getGraniceRazreda();
        int razredIterator = 0;
        for (int i = 0; i < Podaci.getBrojUcenika(); i++) {
            uceniciPan.add(ucenici[i]);
            if (i == razredi[razredIterator]) {
                uceniciPan.add(ucSeparatori[razredIterator]);
                razredIterator++;
            }
        }
        JSeparator[][] knjSeparatori = new JSeparator[maxKnjiga][Ucenik.getBrojRazreda()];
        for (JSeparator[] knjSeparatori0 : knjSeparatori) {
            for (int i=0; i<knjSeparatori0.length; i++) { //ne moze preko for : loopa.
                knjSeparatori0[i] = new JSeparator(SwingConstants.HORIZONTAL);
            }
        }
        for (int i = 0; i < maxKnjiga; i++) {
            razredIterator = 0;
            for (int j = 0; j < Podaci.getBrojUcenika() + 1; j++) {
                knjigePan[i].add(knjige[i][j]);
                if (j - 1 == razredi[razredIterator]) {
                    knjigePan[i].add(knjSeparatori[i][razredIterator]);
                    razredIterator++;
                }
            }
            pan.add(knjigePan[i]);
        }
        //----------JButtons----------------------------------------------------
        JButton noviUc = new JButton("Dodati novog ucenika");
        noviUc.setPreferredSize(new Dimension(200, 35));
        noviUc.addActionListener((ActionEvent e) -> {
            dodajNovogUcenika();
        });
        butPan.add(noviUc);
        JButton delUc = new JButton("Obrisati ucenika");
        delUc.setPreferredSize(new Dimension(150, 35));
        delUc.addActionListener((ActionEvent e) -> {
            List<Integer> imena = new LinkedList<>();
            for (int i = 0; i < Podaci.getBrojUcenika(); i++) {
                if (ucenici[i].isSelected()) {
                    imena.add(i);
                }
            }
            if (imena.isEmpty()) {
                obrisiUcenika();
            } else {
                int delCount = 0; //broj obrisanih ucenika, posto se indexi menjaju prilikom brisanja
                for (Integer ime : imena) {
                    try {
                        ime -= delCount;
                        new rs.luka.biblioteka.funkcije.Ucenici().obrisiUcenika(ime);
                        win.dispose();
                        pregledUcenika();
                    } catch (VrednostNePostoji ex) {
                        throw new RuntimeException(ex);
                    } catch (PreviseKnjiga ex) {
                        LOGGER.log(Level.INFO, "Kod učenika {0} se nalaze neke knjige. "
                                + "Brisanje neuspešno", ime);
                        JOptionPane.showMessageDialog(null, "Kod učenika se nalaze neke knjige.\n"
                                + "Kada ih vrati, pokušajte ponovo", "Greska pri brisanju",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        butPan.add(delUc);
        JButton novaGen = new JButton("Uneti novu generaciju");
        novaGen.setPreferredSize(new Dimension(200, 35));
        novaGen.addActionListener((ActionEvent e) -> {
            dodajNovuGeneraciju();
        });
        butPan.add(novaGen);
        JButton[] uzmiBut = new JButton[Podaci.getBrojUcenika()];
        JButton[] vratiBut = new JButton[Podaci.getBrojUcenika()];
        //----------Listeners---------------------------------------------------
        selectAllUc.addItemListener((ItemEvent e) -> {
            for (JCheckBox ucenik : ucenici) {
                ucenik.setSelected(ucenik.isVisible() && selectAllUc.isSelected());
                //Ako je vidljivo i ako je selectAll selektovan.
            }
        });
        for (int i = 0; i < maxKnjiga; i++) {
            final int red = i;
            knjige[i][0].addItemListener((ItemEvent e) -> {
                for (int j = 1; j < Podaci.getBrojUcenika() + 1; j++) {
                    if (knjige[red][j].isVisible()) {
                        if (knjige[red][0].isSelected()) {
                            knjige[red][j].setSelected(true);
                        } else {
                            knjige[red][j].setSelected(false);
                        }
                    }
                }
            });
        }
        for (int i = 0; i < Podaci.getBrojUcenika(); i++) {
            final int kol = i; //uvek ekvivalentno i, final zbog lambde
            ucenici[i].addItemListener((ItemEvent e) -> {
                boolean selected = false;
                for (int k = 0; k < maxKnjiga; k++) {
                    if (knjige[k][kol + 1].isSelected()) {
                        selected = true; //makar jedan selektovan checkbox
                    }
                }
                if (ucenici[kol].isSelected()) {
                    selected = true;
                }
                if (!selected && uzmiBut[kol] != null) { //ako nema selektovanih boxova
                    uzmiBut[kol].setVisible(false); //ukloni dugme sa prozora
                    uzmiBut[kol] = null; //i iz memorije
                    for (int k = 0; k < maxKnjiga; k++) {
                        if (!knjige[k][kol + 1].getText().equals(" ")) {
                            knjige[k][kol + 1].setEnabled(true); //ponovo omogucuje checkboxove za vracanje
                        }
                    }
                    return; //izadji iz listenera
                }
                if (uzmiBut[kol] != null && uzmiBut[kol].isVisible()) //ako je dugme vec tu
                {
                    return; //izadji
                }
                if (vratiBut[kol] != null && vratiBut[kol].isVisible()) {
                    ucenici[kol].setSelected(false); //nemoguce je imati oba dugmeta prikazana
                    return;
                } else {
                    uzmiBut[kol] = new JButton("Iznajmi knjigu");
                    uzmiBut[kol].setSize(140, 23);
                    uzmiBut[kol].setLocation(5, ucenici[kol].getLocationOnScreen().y - sidePan.getLocationOnScreen().y);
                    sidePan.add(uzmiBut[kol]);
                    uzmiBut[kol].addActionListener((ActionEvent ae) -> {
                        new Uzimanje().Uzimanje(kol);
                        win.dispose();
                        pregledUcenika();
                    });
                }
                for (int k = 0; k < maxKnjiga; k++) {
                    if (!knjige[k][kol + 1].getText().equals(" ")) {
                        knjige[k][kol + 1].setEnabled(false); //onemogucuje jcheckboxove za vracanje
                    }
                }
                sidePan.repaint();
            });

            //knjige
            for (int j = 0; j < maxKnjiga; j++) {
                final int red = j; //uvek ekvivalentno j, final zbog lambde
                knjige[red][kol + 1].addItemListener((ItemEvent e) -> {
                    //uzimanje
                    if (knjige[red][kol + 1].getText().equals(" ")) {
                        boolean selected = false;
                        for (int k = 0; k < maxKnjiga; k++) {
                            if (knjige[k][kol + 1].isSelected()) {
                                selected = true; //makar jedan selektovan checkbox
                            }
                        }
                        if (ucenici[kol].isSelected()) {
                            selected = true;
                        }
                        if (!selected) { //ako nema selektovanih boxova
                            uzmiBut[kol].setVisible(false); //ukloni dugme sa prozora
                            uzmiBut[kol] = null; //i iz memorije
                            for (int k = 0; k < maxKnjiga; k++) {
                                if (!knjige[k][kol + 1].getText().equals(" ")) {
                                    knjige[k][kol + 1].setEnabled(true); //ponovo omogucuje checkboxove za vracanje
                                }
                            }
                            return; //izadji iz listenera
                        }
                        if (uzmiBut[kol] != null && uzmiBut[kol].isVisible()) //ako je dugme vec tu
                        {
                            return; //izadji
                        }
                        uzmiBut[kol] = new JButton("Iznajmi knjigu");
                        uzmiBut[kol].setSize(140, 23);
                        uzmiBut[kol].setLocation(5, ucenici[kol].getLocationOnScreen().y - sidePan.getLocationOnScreen().y);
                        uzmiBut[kol].addActionListener((ActionEvent ae) -> {
                            new Uzimanje().Uzimanje(kol); //uzmi knjigu
                            win.dispose(); //moglo bi i elegantnije (refreshLabels)
                            pregledUcenika(); //pretpostavljam, swing mi nije uvek jasan
                        });
                        sidePan.add(uzmiBut[kol]);
                        for (int k = 0; k < maxKnjiga; k++) {
                            if (!knjige[k][kol + 1].getText().equals(" ")) {
                                knjige[k][kol + 1].setEnabled(false); //onemogucuje jcheckboxove za vracanje
                            }
                        }
                    } //vracanje
                    else {
                        boolean selected = false;
                        for (int k = 0; k < maxKnjiga; k++) {
                            if (knjige[k][kol + 1].isSelected()) {
                                selected = true; //makar jedan selektovan checkbox 
                            }
                        }
                        if (!selected) { //ako nema selektovanih boxova
                            vratiBut[kol].setVisible(false); //ukloni dugme s prozora
                            vratiBut[kol] = null; //i iz memorije
                            ucenici[kol].setEnabled(true); //ponovo omogucuje checkboxove za uzimanje
                            for (int k = 0; k < maxKnjiga; k++) {
                                if (knjige[k][kol + 1].getText().equals(" ")) {
                                    knjige[k][kol + 1].setEnabled(true);
                                }
                            }
                            return; //izadji iz listenera
                        }
                        if (vratiBut[kol] != null && vratiBut[kol].isVisible()) //ako je dugme vec tu
                        {
                            return; //izadji
                        }
                        if (uzmiBut[kol] != null && uzmiBut[kol].isVisible()) {
                            knjige[red][kol].setSelected(false); //nemoguce je imati oba dugmeta prikazana
                            return; //moglo bi i intuitivnije (nekakav tooltip)
                        }
                        vratiBut[kol] = new JButton("Vrati knjigu");
                        vratiBut[kol].setSize(140, 23);
                        vratiBut[kol].setLocation(5, ucenici[kol].getLocationOnScreen().y - sidePan.getLocationOnScreen().y);
                        vratiBut[kol].addActionListener((ActionEvent ae) -> {
                            List<Integer> indexi = new LinkedList<>(); //indexi knjiga za vracanje
                            for (int k = 0; k < maxKnjiga; k++) {
                                if (knjige[k][kol + 1].isSelected() && knjige[red][kol + 1].isVisible() && !knjige[k][kol + 1].getText().equals(" ")) {
                                    try {
                                        indexi.add(Podaci.indexOfNaslov(knjige[k][kol + 1].getText()));
                                    } catch (VrednostNePostoji ex) {
                                        throw new RuntimeException(ex); //ako se desi, postoji greska u programu
                                    }
                                }
                            }
                            new rs.luka.biblioteka.funkcije.Knjige().vracanje(kol, indexi); //van for petlje, vraca selektovane knjige
                            win.dispose();
                            pregledUcenika();
                        });
                        sidePan.add(vratiBut[kol]);
                        ucenici[kol].setEnabled(false); //ne mogu i uzimanje i vracanje biti vidljivi u istom trenutku
                        for (int k = 0; k < maxKnjiga; k++) {
                            if (knjige[k][kol + 1].getText().equals(" ")) {
                                knjige[k][kol + 1].setEnabled(false);
                            }
                        }
                    }
                    sidePan.repaint();
                });
            }
        }
        //----------search------------------------------------------------------
        searchBox.addFocusListener(this);
        searchBox.addActionListener((ActionEvent e) -> {
            LOGGER.log(Level.FINE, "Počinjem pretragu (grafički)");
            if (searchBox.getText().isEmpty()) {
                win.dispose(); //lose, ne moze da se radi rekurzija na ovaj nacin iz lambde
                pregledUcenika(); //WORKAROUND! Izbegavati
                return;
            }
            for (JSeparator sep : ucSeparatori) {
                uceniciPan.remove(sep); //remove ili samo reset ??
            }
            for (int i = 0; i < maxKnjiga; i++) {
                for (int j = 0; j < knjSeparatori.length; j++) {
                    knjigePan[i].remove(knjSeparatori[i][j]);
                }
            }

            rs.luka.biblioteka.funkcije.Ucenici funkcije = new rs.luka.biblioteka.funkcije.Ucenici();
            ArrayList<Integer> ucIndexes = funkcije.pretraziUcenike(searchBox.getText());

            Ucenik uc;
            for (int i = 0; i < Podaci.getBrojUcenika(); i++) {
                uc = getUcenik(i);
                if (ucIndexes.contains(i)) {
                    ucenici[i].setText(uc.getIme());
                    ucenici[i].setVisible(true);
                    for (int j = 0; j < maxKnjiga; j++) {
                        if (uc.getNaslovKnjige(j).isEmpty()) {
                            knjige[j][i + 1].setText(" "); //workaround
                            knjige[j][i + 1].setVisible(true);
                        } else {
                            knjige[j][i + 1].setText(uc.getNaslovKnjige(i));
                            knjige[j][i + 1].setVisible(true);
                        }
                    }
                } else {
                    ucenici[i].setVisible(false);
                    ucenici[i].setSelected(false);
                    for (int j = 0; j < maxKnjiga; j++) {
                        knjige[j][i + 1].setVisible(false);
                        knjige[j][i].setSelected(false);
                    }
                }
            }

            int y = (int) (((Podaci.getBrojUcenika() + 1 - ucIndexes.size()) * 24.8) / 2); //priblizno !!
            scroll.getViewport().setViewPosition(new Point(10, y));  //izuzetno los workaround
            searchBox.setLocation(searchBox.getX(), y);

            pan.revalidate();
            pan.repaint();
        });
        searchBox.setBounds(0, 0, 150, 27);
        searchBox.setFont(Grafika.getLabelFont());
            searchBox.setBackground(Grafika.getTFColor());
        searchBox.setForeground(Grafika.getFgColor());
        searchBox.setCaretColor(Grafika.getFgColor());
        sidePan.add(searchBox);

        pan.add(sidePan);
        if (searchBox.isVisible()) {
            win.setVisible(true);
        } else {
            win.dispose();
        }
    }

    /**
     * Prozor za dodavanje novog ucenika.
     *
     * @since 1.7.'13.
     */
    public void dodajNovogUcenika() {
        final rs.luka.biblioteka.funkcije.Ucenici ucenici = new rs.luka.biblioteka.funkcije.Ucenici();
        //---------JFrame&JPanel------------------------------------------------
        JDialog win = new JDialog();
        win.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        win.setTitle("Dodavanje novog ucenika");
        win.setSize(400, 250);
        win.setLocationRelativeTo(null);
        win.setResizable(false);
        win.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        JPanel pan = new JPanel(null);
        pan.setBackground(Grafika.getBgColor());
        win.setContentPane(pan);
        //---------JLabel&JTextField--------------------------------------------
        JLabel ime = new JLabel("Unesite ime ucenika:");
        ime.setBounds(20, 20, 250, 37);
        ime.setBackground(Grafika.getBgColor());
        ime.setForeground(Grafika.getFgColor());
        ime.setFont(Grafika.getLabelFont());
        pan.add(ime);
        final JTextField ucTF = new JTextField();
        ucTF.setBounds(20, 55, 250, 25);
        ucTF.setFont(Grafika.getLabelFont());
            ucTF.setBackground(Grafika.getTFColor());
        ucTF.setForeground(Grafika.getFgColor());
        ucTF.setCaretColor(Grafika.getFgColor());
        pan.add(ucTF);
        JLabel razred = new JLabel("Unesite razred koji ucenik trenutno pohadja:");
        razred.setBounds(20, 95, 300, 37);
        razred.setBackground(Grafika.getBgColor());
        razred.setFont(Grafika.getLabelFont());
        razred.setForeground(Grafika.getFgColor());
        pan.add(razred);
        final JTextField razTF = new JTextField();
        razTF.setBounds(20, 125, 250, 25);
        razTF.setFont(Grafika.getLabelFont());
        razTF.setForeground(Grafika.getFgColor());
        razTF.setCaretColor(Grafika.getFgColor());
            razTF.setBackground(Grafika.getTFColor());
        pan.add(razTF);
        //---------JButton------------------------------------------------------
        JButton but = new JButton("Unesi podatke");
        but.setBounds(130, 170, 140, 40);
        but.addActionListener((ActionEvent e) -> {
            try {
                ucenici.dodajUcenika(ucTF.getText(), parseUnsignedInt(razTF.getText()));
                showMessageDialog(null, "Ucenik dodat!", "Uspeh!", JOptionPane.INFORMATION_MESSAGE);
                win.dispose();
            } catch (NumberFormatException ex) {
                LOGGER.log(Level.INFO, "Razred novog učenika ({0}) je prevelik "
                        + "ili nije broj", razTF.getText());
                showMessageDialog(null, "Razred ucenika je prevelik.", "Los razred", JOptionPane.ERROR_MESSAGE);
            }
        });
        pan.add(but);
        //---------setVisible---------------------------------------------------
        win.setVisible(true);
    }

    /**
     * Prozor za brisanje ucenika.
     *
     * @since 2.7.'13.
     */
    public void obrisiUcenika() {
        final rs.luka.biblioteka.funkcije.Ucenici ucenici = new rs.luka.biblioteka.funkcije.Ucenici();
        //---------JFrame&JPanel------------------------------------------------
        String ucenik = Dijalozi.showTextFieldDialog("Brisanje ucenika", "Unesite ime ucenika "
                + "i pritisnite enter:", "");
        List<Integer> inx = indexOfUcenik(ucenik);
        if (inx.isEmpty()) {
            LOGGER.log(Level.INFO, "Učenik {0} nije pronađen", ucenik);
            showMessageDialog(null, "Ucenik " + ucenik + " nije pronadjen\n"
                    + "Proverite unos  i pokusajte ponovo.", "Ucenik ne postoji.",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            int num = 0;
            if (inx.size() > 1) {
                Dijalozi.viseRazreda(inx);
            }
            try {
                ucenici.obrisiUcenika(inx.get(num));
                showMessageDialog(null, "Ucenik obrisan!", "Uspeh!", JOptionPane.INFORMATION_MESSAGE);
            } catch (VrednostNePostoji ex) {
                throw new RuntimeException("VrednostNePostoji prilikom obrisiUcenika za index "
                        + inx.get(num), ex);
            } catch (PreviseKnjiga ex) {
                LOGGER.log(Level.INFO, "Učenik {0} nije obrisan jer ima preostalih knjiga.", inx.get(num));
                showMessageDialog(null, "Ucenik ima preostalih knjiga.\n"
                        + "Kada vrati, pokusajte ponovo.", "Preostale knjige", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Prozor za dodavanje nove generacije.
     *
     * @since 5.7.'13.
     */
    public void dodajNovuGeneraciju() {
        //---------JFrame&JPanel------------------------------------------------
        JDialog win = new JDialog();
        win.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        win.setTitle("Unos nove generacije.");
        win.setSize(610, 550);
        win.setLocationRelativeTo(null);
        win.setResizable(false);
        win.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        JPanel pan = new JPanel(null);
        pan.setBackground(Grafika.getBgColor());
        win.setContentPane(pan);
        //---------JLabel&JTextPane---------------------------------------------
        JLabel lab = new JLabel("<html>Unesite ucenike nove generacije, odvojene zapetama <br />"
                + "<strong>Nakon dodavanja nove generacije, svi ucenici "
                + "najstarije generacije ce biti obrisani!!!</strong></html>");
        lab.setBounds(10, 5, 600, 50);
        lab.setBackground(Grafika.getBgColor());
        lab.setForeground(Grafika.getFgColor());
        lab.setFont(Grafika.getLabelFont());
        pan.add(lab);
        final JTextPane genTF = new JTextPane();
        final JScrollPane jsp = new JScrollPane(genTF);
        jsp.setBounds(10, 60, 580, 400);
        genTF.setFont(Grafika.getLabelFont());
        genTF.setForeground(Grafika.getFgColor());
        genTF.setCaretColor(Grafika.getFgColor());
            genTF.setBackground(Grafika.getTFColor());
        pan.add(jsp);
        //----------JButton-----------------------------------------------------
        JButton but = new JButton("Unesi novu generaciju");
        but.setBounds(215, 470, 170, 40);
        but.addActionListener((ActionEvent e) -> {
            new rs.luka.biblioteka.funkcije.Ucenici().dodajNovuGen(genTF.getText());
            showMessageDialog(null, "Nova generacija dodata.",
                    "Uspeh!", JOptionPane.INFORMATION_MESSAGE);
        });
        pan.add(but);
        //---------setVisible---------------------------------------------------
        win.setVisible(true);
    }

    public void refreshLabels() {
        int maxKnjiga = getMaxBrojUcenikKnjiga();
        selectAllUc = new JCheckBox("<html>Ucenici:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
                + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
                + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br></html>");
        knjige = new JCheckBox[maxKnjiga][Podaci.getBrojUcenika() + 1];
        selectAllUc.setFont(Grafika.getLabelFont());
        selectAllUc.setForeground(Grafika.getFgColor());
        selectAllUc.setBackground(Grafika.getBgColor());

        ucenici = new JCheckBox[Podaci.getBrojUcenika()];
        for (int i = 0; i < Podaci.getBrojUcenika(); i++) {
            ucenici[i] = new JCheckBox(getUcenik(i).getIme());
            ucenici[i].setFont(Grafika.getLabelFont());
            ucenici[i].setForeground(Grafika.getFgColor());
            ucenici[i].setBackground(Grafika.getBgColor());
            ucenici[i].setMinimumSize(new Dimension(180, 20));
        }

        for (int i = 0; i < maxKnjiga; i++) {
            knjige[i][0] = new JCheckBox("<html>Knjige:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
                    + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br></html>");
            knjige[i][0].setFont(Grafika.getLabelFont());
            knjige[i][0].setBackground(Grafika.getBgColor());
            knjige[i][0].setForeground(Grafika.getFgColor());
            for (int j = 1; j < Podaci.getBrojUcenika() + 1; j++) {
                if (getUcenik(j - 1).isKnjigaEmpty(i)) {
                    knjige[i][j] = new JCheckBox(" "); //workaround, treba mi text zbog visine
                } else {
                    knjige[i][j] = new JCheckBox(getUcenik(j - 1).getNaslovKnjige(i));
                }
                knjige[i][j].setFont(Grafika.getLabelFont());
                knjige[i][j].setForeground(Grafika.getFgColor());
                knjige[i][j].setBackground(Grafika.getBgColor());
            }
        }
        LOGGER.log(Level.FINE, "Postavio labele učenika");
    }

    @Override
    public void focusGained(FocusEvent e) {
        if (searchBox.getText().equals("Pretrazi ucenike...")) {
            searchBox.setText("");
        }
    }

    @Override
    public void focusLost(FocusEvent e) {
        //
    }
}
