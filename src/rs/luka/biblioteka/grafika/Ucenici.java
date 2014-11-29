package rs.luka.biblioteka.grafika;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import static java.lang.String.valueOf;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.showMessageDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import rs.luka.biblioteka.data.Config;
import rs.luka.biblioteka.data.Podaci;
import static rs.luka.biblioteka.data.Podaci.getMaxBrojUcenikKnjiga;
import static rs.luka.biblioteka.data.Podaci.getUcenik;
import rs.luka.biblioteka.data.Ucenik;
import rs.luka.biblioteka.debugging.Console;
import rs.luka.biblioteka.exceptions.PreviseKnjiga;
import rs.luka.biblioteka.exceptions.VrednostNePostoji;
import rs.luka.biblioteka.funkcije.Save;
import rs.luka.biblioteka.funkcije.Undo;
import rs.luka.biblioteka.funkcije.Utils;
import static rs.luka.biblioteka.grafika.Grafika.generateEmptyAction;
import static rs.luka.biblioteka.grafika.Konstante.*;

/**
 * @since 1.7.'13.
 * @author Luka
 */
public class Ucenici implements FocusListener {

    private static final java.util.logging.Logger LOGGER
            = java.util.logging.Logger.getLogger(Ucenici.class.getName());

    /**
     * Matrix sa knjigama pocinje od [][1], na 0 su labeli "Knjiga".
     */
    private JCheckBox[][] knjige;
    private JCheckBox[] ucenici;
    private static final String SEARCH_TEXT = UCENICI_SEARCH_TEXT;
    private final Insets INSET = new Insets(UCENICI_TOP_INSET, UCENICI_LEFT_INSET, 
            UCENICI_BOTTOM_INSET, UCENICI_RIGHT_INSET); //ne sme static, da se ne bi prerano inicijalizovala

    public Ucenici() {
        butPan = new JPanel();
        knjSeparatori = new JSeparator[Podaci.getMaxBrojUcenikKnjiga()][Ucenik.getBrojRazreda()];
        ucSeparatori = new JSeparator[Ucenik.getBrojRazreda()];
        knjigePan = new JPanel[getMaxBrojUcenikKnjiga()];
        uceniciPan = new JPanel();
        pan = new JPanel();
        scroll = new JScrollPane(pan);
        split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, scroll, butPan);
        uzmiBut = new JButton[Podaci.getBrojUcenika()];
        vratiBut = new JButton[Podaci.getBrojUcenika()];
        searchBox = new JTextField(SEARCH_TEXT);
        selectAllUc = new JCheckBox("<html>Ucenici:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
                + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
                + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br></html>");
        pregledUcenika();
    }

    /**
     * Pregled ucenika sa knjigama koje su trenutno kod njih. NE POZIVATI OSIM
     * IZ KONSTRUKTORA!
     *
     * @since 1.7.'13.
     */
    private void pregledUcenika() {
        initPanels();
        initText();
        setTextAndSeparators();
        initButtons();
        initIcons();
        initMainListeners();
        initSearchBox();
        win.setVisible(true);
        setInputMaps();
    }
    
    
    private static final int maxKnjiga = getMaxBrojUcenikKnjiga();
    private final JSplitPane split;
    private final JPanel butPan;
    private static final JFrame win = new JFrame("Pregled učenika");
    private final JSeparator[][] knjSeparatori;
    private final JSeparator[] ucSeparatori;
    private final JPanel[] knjigePan;
    private final JPanel uceniciPan;
    private final JScrollPane scroll;
    private final JPanel pan;
    private final JTextField searchBox;
    private final JCheckBox selectAllUc;
    private final JButton[] vratiBut;
    private final JButton[] uzmiBut;
    private final JPanel sidePan = new JPanel(null);
    
    private void initPanels() {
        int sirina, visina;
        sirina = Config.getAsInt("uceniciS", 
                valueOf(UCENICI_KNJPANEL_WIDTH * getMaxBrojUcenikKnjiga() + UCENICI_FIXED_WIDTH));
        visina = Config.getAsInt("uceniciV", valueOf(UCENICI_HEIGHT));
        win.setSize(sirina, visina);
        LOGGER.log(Level.CONFIG, "Postavljam visinu prozora sa učenicima na {0}, širinu na {1}",
                new Object[]{visina, sirina});
        win.setLocationRelativeTo(null);
        //win.setResizable(false);
        win.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        win.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Grafika.exit();
            }
        });
        pan.setLayout(new BoxLayout(pan, BoxLayout.X_AXIS));
        pan.setBackground(Grafika.getBgColor());
        pan.setAutoscrolls(true);
        scroll.add(scroll.createVerticalScrollBar());
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        butPan.setBackground(Grafika.getBgColor());
        butPan.setLayout(new FlowLayout(FlowLayout.CENTER));
        split.setOneTouchExpandable(false);
        split.setDividerLocation(visina - 100);
        uceniciPan.setLayout(new BoxLayout(uceniciPan, BoxLayout.Y_AXIS));
        uceniciPan.setBackground(Grafika.getBgColor());
        uceniciPan.setAlignmentY(0);
        for (int i = 0; i < getMaxBrojUcenikKnjiga(); i++) {
            knjigePan[i] = new JPanel();
            knjigePan[i].setLayout(new BoxLayout(knjigePan[i], BoxLayout.Y_AXIS));
            knjigePan[i].setBackground(Grafika.getBgColor());
            knjigePan[i].setAlignmentY(0);
        }
        sidePan.setBackground(Grafika.getBgColor());
        sidePan.setPreferredSize(new Dimension(UCENICI_SIDEPAN_WIDTH, 
                (Podaci.getBrojUcenika() + 1) * UCENICI_HEIGHT_PER_LABEL));
        sidePan.setAlignmentY(0);
        win.setContentPane(split);
    }
    
    private void setInputMaps() {
        Console console = new rs.luka.biblioteka.debugging.Console();
        Method consoleMethod = null, undoMethod = null, redoMethod=null;
        try {
            consoleMethod = console.getClass().getDeclaredMethod("console", null);
            undoMethod = Undo.class.getDeclaredMethod("undo", null);
            redoMethod = Undo.class.getDeclaredMethod("redo", null);
        } catch (NoSuchMethodException | SecurityException ex) {
            LOGGER.log(Level.SEVERE, "Greška pri kreiranju neke od metoda", ex);
        }
        pan.getInputMap().put(KeyStroke.getKeyStroke("ctrl Z"), "undo");
        pan.getActionMap().put("undo", generateEmptyAction(undoMethod, null));
        pan.getInputMap().put(KeyStroke.getKeyStroke("ctrl Y"), "redo");
        pan.getActionMap().put("redo", generateEmptyAction(redoMethod, null));
        pan.getInputMap().put(KeyStroke.getKeyStroke("ctrl shift T"), "console");
        pan.getActionMap().put("console", generateEmptyAction(consoleMethod, console));
    }
    
    private void initText() {
        Podaci.sortUcenike();
        Ucenik uc;
        knjige = new JCheckBox[maxKnjiga][Podaci.getBrojUcenika() + 1];
        selectAllUc.setFont(Grafika.getLabelFont());
        selectAllUc.setForeground(Grafika.getFgColor());
        selectAllUc.setBackground(Grafika.getBgColor());
        selectAllUc.setBorder(new EmptyBorder(INSET));

        ucenici = new JCheckBox[Podaci.getBrojUcenika()];
        Iterator<Ucenik> it = Podaci.iteratorUcenika();
        for (int i = 0; i < Podaci.getBrojUcenika(); i++) {
            ucenici[i] = new JCheckBox(it.next().getIme());
            ucenici[i].setFont(Grafika.getLabelFont());
            ucenici[i].setForeground(Grafika.getFgColor());
            ucenici[i].setBackground(Grafika.getBgColor());
            ucenici[i].setBorder(new EmptyBorder(INSET));
        }

        for (int i = 0; i < maxKnjiga; i++) {
            knjige[i][0] = new JCheckBox("<html>Knjige:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
                    + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br></html>");
            knjige[i][0].setFont(Grafika.getLabelFont());
            knjige[i][0].setBackground(Grafika.getBgColor());
            knjige[i][0].setForeground(Grafika.getFgColor());
            knjige[i][0].setBorder(new EmptyBorder(INSET));
            it = Podaci.iteratorUcenika();
            for (int j = 1; j < Podaci.getBrojUcenika() + 1; j++) {
                uc = it.next();
                knjige[i][j] = new JCheckBox(" ");
                if(!uc.isKnjigaEmpty(i) && i<uc.getMaxBrojKnjiga()) {
                    knjige[i][j].setText(uc.getNaslovKnjige(i));
                }
                knjige[i][j].setBorder(new EmptyBorder(INSET));
                knjige[i][j].setFont(Grafika.getLabelFont());
                knjige[i][j].setForeground(Grafika.getFgColor());
                knjige[i][j].setBackground(Grafika.getBgColor());
            }
        }
        LOGGER.log(Level.FINE, "Postavio labele učenika"); 
        uceniciPan.add(selectAllUc);
        pan.add(uceniciPan);
    }
    
    private void setTextAndSeparators() {
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
        for (JSeparator[] knjSeparatori0 : knjSeparatori) {
            for (int i = 0; i < knjSeparatori0.length; i++) { //ne moze preko for : loopa.
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
    }
    
    private void initButtons() {
        JButton noviUc = new JButton("Dodati novog ucenika");
        noviUc.setPreferredSize(new Dimension(UCENICI_NOVIUC_WIDTH, UCENICI_BUTPAN_BUTTON_HEIGHT));
        noviUc.addActionListener((ActionEvent e) -> {
            new UceniciUtils().dodajNovogUcenika();
        });
        butPan.add(noviUc);
        JButton delUc = new JButton("Obrisati ucenika");
        delUc.setPreferredSize(new Dimension(UCENICI_DELUC_WIDTH, UCENICI_BUTPAN_BUTTON_HEIGHT));
        delUc.addActionListener((ActionEvent e) -> {
            obrisiUcenika();
        });
        butPan.add(delUc);
        JButton novaGen = new JButton("Uneti novu generaciju");
        novaGen.setPreferredSize(new Dimension(UCENICI_NOVAGEN_WIDTH, UCENICI_BUTPAN_BUTTON_HEIGHT));
        novaGen.addActionListener((ActionEvent e) -> {
            new UceniciUtils().dodajNovuGeneraciju();
        });
        butPan.add(novaGen);
        
        butPan.add(Box.createRigidArea(new Dimension(UCENICI_BUTPAN_RIGIDAREA_WIDTH, 1)));
    }
    
    private void initIcons() {
        JButton pregledBut;
        try {
            BufferedImage buttonIcon = ImageIO.read(new File(Utils.getWorkingDir() + "knjige.png"));
            pregledBut = new JButton(getIconFromImage(buttonIcon, UCENICI_ICON_WIDTH, UCENICI_ICON_HEIGHT));
            pregledBut.setToolTipText("Pregled knjiga");
        } catch(IOException ex) {
            pregledBut = new JButton("Knjige");
            LOGGER.log(Level.SEVERE, "IO greška pri učitavanju slike za dugme za pregled knjiga", ex);
        }
        pregledBut.addActionListener((ActionEvent e) -> {
            new Knjige();
        });
        pregledBut.setFocusable(false);
        //pregledBut.setBackground(Color.WHITE);
        pregledBut.setContentAreaFilled(false); // ??
        pregledBut.setBorder(null);
        butPan.add(pregledBut);
        
        JButton saveBut;
        try {
            BufferedImage buttonIcon = ImageIO.read(new File(Utils.getWorkingDir() + "save.png"));
            saveBut = new JButton(getIconFromImage(buttonIcon, UCENICI_ICON_WIDTH, UCENICI_ICON_HEIGHT));
            saveBut.setToolTipText("Sačuvaj podatke");
        } catch (IOException ex) {
            saveBut = new JButton("Sačuvaj");
            LOGGER.log(Level.SEVERE, "IO greška pri učitavanju slike za dugme za čuvanje", ex);
        }
        saveBut.addActionListener((ActionEvent e) -> {
            try {
                Save.save();
            } catch (IOException ex) {
                showMessageDialog(null, "Došlo je do greške pri "
                        + "čuvanju fajlova", "I/O Greska", JOptionPane.ERROR_MESSAGE);
            }
        });
        saveBut.setFocusable(false);
        saveBut.setBorder(null);
        saveBut.setContentAreaFilled(false);
        butPan.add(saveBut);
        
        JButton podesavanjaBut;
        try {
            BufferedImage buttonIcon = ImageIO.read(new File(Utils.getWorkingDir() + "gear.png"));
            podesavanjaBut = new JButton(getIconFromImage(buttonIcon, UCENICI_ICON_WIDTH, UCENICI_ICON_HEIGHT));
            podesavanjaBut.setToolTipText("Podešavanja");
            
        } catch (IOException ex) {
            podesavanjaBut = new JButton("Podešavanja");
            LOGGER.log(Level.SEVERE, "IO greška pri učitavanju slike za dugme za podešavanja", ex);
        }
        podesavanjaBut.addActionListener((ActionEvent e) -> {
            new Podesavanja().podesavanja();
        });
        podesavanjaBut.setFocusable(false);
        podesavanjaBut.setBorder(null);
        podesavanjaBut.setContentAreaFilled(false);
        butPan.add(podesavanjaBut);
    }
    
    private void initMainListeners() {
        selectAllUc.addItemListener((ItemEvent e) -> {
            selectAllUc();
        });
        for (int i = 0; i < maxKnjiga; i++) {
            final int red = i;
            knjige[i][0].addItemListener((ItemEvent e) -> {
                selectAllKnj(red);
            });
        }
        for (int i = 0; i < Podaci.getBrojUcenika(); i++) {
            final int red = i; //uvek ekvivalentno i, final zbog lambde
            ucenici[i].addItemListener((ItemEvent e) -> {
                uzimanjeKnjige(red);
            });

            //knjige
            for (int j = 0; j < maxKnjiga; j++) {
                final int kol = j; //uvek ekvivalentno j, final zbog lambde
                knjige[kol][red + 1].addItemListener((ItemEvent e) -> {
                    //uzimanje
                    if (knjige[kol][red + 1].getText().equals(" ")) {
                        uzimanjeKnjige(red);
                    } //vracanje
                    else {
                        vracanjeKnjige(kol, red);
                    }
                    sidePan.repaint();
                });
            }
        }
    }
    
    private void initSearchBox() {
        searchBox.addFocusListener(this);
        searchBox.addActionListener((ActionEvent e) -> {
            search();
        });
        searchBox.setBounds(UCENICI_SEARCHBOX_X, UCENICI_SEARCHBOX_Y, 
                UCENICI_SEARCHBOX_WIDTH, UCENICI_SEARCHBOX_HEIGHT);
        searchBox.setFont(Grafika.getLabelFont());
        searchBox.setBackground(Grafika.getTFColor());
        searchBox.setForeground(Grafika.getFgColor());
        searchBox.setCaretColor(Grafika.getFgColor());
        sidePan.add(searchBox);

        pan.add(sidePan);
    }
    
    private ImageIcon getIconFromImage(BufferedImage image, int width, int height) {
        int imageWidth  = image.getWidth();
    int imageHeight = image.getHeight();

    double scaleX = (double)width/imageWidth;
    double scaleY = (double)height/imageHeight;
    AffineTransform scaleTransform = AffineTransform.getScaleInstance(scaleX, scaleY);
    AffineTransformOp bilinearScaleOp = new AffineTransformOp(scaleTransform, AffineTransformOp.TYPE_BILINEAR);

    return new ImageIcon(bilinearScaleOp.filter(
        image,
        new BufferedImage(width, height, image.getType())));
    }

    //----------METODE ZA LISTENERE---------------------------------------------
    /**
     * Radi iteraciju preko svih checkboxova i ako je selectall selektovan i
     * dati checkbox je vidljiv, selektuje ga. Ako to ne vazi, deselektuje
     * checkbox.
     */
    private void selectAllUc() {
        for (JCheckBox ucenik : ucenici) {
            ucenik.setSelected(ucenik.isVisible() && selectAllUc.isSelected());
        }
    }

    private void selectAllKnj(int red) {
        for (int j = 1; j < Podaci.getBrojUcenika() + 1; j++) {
            if (knjige[red][j].isVisible()) {
                if (knjige[red][0].isSelected()) {
                    knjige[red][j].setSelected(true);
                } else {
                    knjige[red][j].setSelected(false);
                }
            }
        }
    }

    private void obrisiUcenika() {
        List<Integer> imena = new LinkedList<>();
        for (int i = 0; i < Podaci.getBrojUcenika(); i++) {
            if (ucenici[i].isSelected()) {
                imena.add(i);
            }
        }
        if (imena.isEmpty()) {
            new UceniciUtils().obrisiUcenika();
        } else {
            int delCount = 0; //broj obrisanih ucenika, posto se indexi menjaju prilikom brisanja
            for (Integer ime : imena) {
                try {
                    ime -= delCount;
                    new rs.luka.biblioteka.funkcije.Ucenici().obrisiUcenika(ime);
                    delCount++;
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
        win.dispose();
        new Ucenici();
    }

    private void uzimanjeKnjige(int red) {
        boolean selected = false;
        for (int k = 0; k < maxKnjiga; k++) {
            if (knjige[k][red + 1].isSelected()) {
                selected = true; //makar jedan selektovan checkbox
            }
        }
        if (ucenici[red].isSelected()) {
            selected = true;
        }
        if (!selected && uzmiBut[red] != null) { //ako nema selektovanih boxova
            uzmiBut[red].setVisible(false); //ukloni dugme sa prozora
            uzmiBut[red] = null; //i iz memorije
            for (int k = 0; k < maxKnjiga; k++) {
                if (!knjige[k][red + 1].getText().equals(" ")) {
                    knjige[k][red + 1].setEnabled(true); //ponovo omogucuje checkboxove za vracanje
                }
            }
            return; //izadji iz listenera
        }
        if (uzmiBut[red] != null && uzmiBut[red].isVisible()) //ako je dugme vec tu
        {
            return; //izadji
        }
        if (vratiBut[red] != null && vratiBut[red].isVisible()) {
            ucenici[red].setSelected(false); //nemoguce je imati oba dugmeta prikazana
            return;
        } else {
            uzmiBut[red] = new JButton("Iznajmi knjigu");
            uzmiBut[red].setSize(UCENICI_BUTTON_WIDTH, UCENICI_BUTTON_HEIGHT);
            uzmiBut[red].setLocation(UCENICI_BUTTON_X, 
                    ucenici[red].getLocationOnScreen().y - sidePan.getLocationOnScreen().y);
            sidePan.add(uzmiBut[red]);
            uzmiBut[red].addActionListener((ActionEvent ae) -> {
                new Uzimanje().Uzimanje(red);
                win.dispose();
                new Ucenici();
            });
        }
        for (int k = 0; k < maxKnjiga; k++) {
            if (!knjige[k][red + 1].getText().equals(" ")) {
                knjige[k][red + 1].setEnabled(false); //onemogucuje jcheckboxove za vracanje
            }
        }
        sidePan.repaint();
    }

    private void vracanjeKnjige(int kol, int red) {
        boolean selected = false;
        for (int k = 0; k < maxKnjiga; k++) {
            if (knjige[k][red + 1].isSelected()) {
                selected = true; //makar jedan selektovan checkbox 
            }
        }
        if (!selected) { //ako nema selektovanih boxova
            vratiBut[red].setVisible(false); //ukloni dugme s prozora
            vratiBut[red] = null; //i iz memorije
            ucenici[red].setEnabled(true); //ponovo omogucuje checkboxove za uzimanje
            for (int k = 0; k < maxKnjiga; k++) {
                if (knjige[k][red + 1].getText().equals(" ")) {
                    knjige[k][red + 1].setEnabled(true);
                }
            }
            return; //izadji iz listenera
        }
        if (vratiBut[red] != null && vratiBut[red].isVisible()) //ako je dugme vec tu
        {
            return; //izadji
        }
        if (uzmiBut[red] != null && uzmiBut[red].isVisible()) {
            knjige[kol][red].setSelected(false); //nemoguce je imati oba dugmeta prikazana
            return; //moglo bi i intuitivnije (nekakav tooltip)
        }
        vratiBut[red] = new JButton("Vrati knjigu");
        vratiBut[red].setSize(UCENICI_BUTTON_WIDTH, UCENICI_BUTTON_HEIGHT);
        vratiBut[red].setLocation(UCENICI_BUTTON_X, 
                ucenici[red].getLocationOnScreen().y - sidePan.getLocationOnScreen().y);
        vratiBut[red].addActionListener((ActionEvent ae) -> {
            List<Integer> indexi = new LinkedList<>(); //indexi knjiga za vracanje
            for (int k = 0; k < maxKnjiga; k++) {
                if (knjige[k][red + 1].isSelected() && knjige[kol][red + 1].isVisible() && !knjige[k][red + 1].getText().equals(" ")) {
                    try {
                        indexi.add(Podaci.indexOfNaslov(knjige[k][red + 1].getText()));
                    } catch (VrednostNePostoji ex) {
                        throw new RuntimeException(ex); //ako se desi, postoji greska u programu
                    }
                }
            }
            new rs.luka.biblioteka.funkcije.Knjige().vracanje(red, indexi); //van for petlje, vraca selektovane knjige
            win.dispose();
            new Ucenici();
        });
        sidePan.add(vratiBut[red]);
        ucenici[red].setEnabled(false); //ne mogu i uzimanje i vracanje biti vidljivi u istom trenutku
        for (int k = 0; k < maxKnjiga; k++) {
            if (knjige[k][red + 1].getText().equals(" ")) {
                knjige[k][red + 1].setEnabled(false);
            }
        }
    }

    private void search() {
        LOGGER.log(Level.FINE, "Počinjem pretragu (grafički)");
        for (JSeparator sep : ucSeparatori) {
            uceniciPan.remove(sep); //remove ili samo reset ??
        }
        for (int i = 0; i < maxKnjiga; i++) {
            for (int j = 0; j < knjSeparatori[i].length; j++) {
                knjigePan[i].remove(knjSeparatori[i][j]);
            }
        }

        rs.luka.biblioteka.funkcije.Ucenici funkcije = new rs.luka.biblioteka.funkcije.Ucenici();
        ArrayList<Integer> ucIndexes = funkcije.pretraziUcenike(searchBox.getText());
        
        if(ucIndexes.isEmpty() && Podaci.naslovExists(searchBox.getText())) {
            try {
                ucIndexes = Utils.extractXFromPointList
                        (new rs.luka.biblioteka.funkcije.Knjige().pretraziUcenike(searchBox.getText()));
            } catch (VrednostNePostoji ex) {/*nikad, zbog provere u if-u*/}
        }

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

        sidePan.setMaximumSize(new Dimension(UCENICI_SIDEPAN_WIDTH, 
                (ucIndexes.size() + 1) * selectAllUc.getHeight()));

        pan.revalidate();
        pan.repaint();
    }

    protected static void close() {
        win.dispose();
    }
    //==========FOCUS============================================================
    @Override
    public void focusGained(FocusEvent e) {
        if (searchBox.getText().equals(SEARCH_TEXT)) {
            searchBox.setText("");
        }
    }

    @Override
    public void focusLost(FocusEvent e) {
        if(searchBox.getText().isEmpty()) {
            searchBox.setText(SEARCH_TEXT);
        }
    }
}
