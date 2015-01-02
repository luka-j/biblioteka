package rs.luka.biblioteka.grafika;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
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
import javax.swing.ActionMap;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
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
import rs.luka.biblioteka.funkcije.Pretraga;
import rs.luka.biblioteka.funkcije.Save;
import rs.luka.biblioteka.funkcije.Undo;
import rs.luka.biblioteka.funkcije.Utils;
import static rs.luka.biblioteka.grafika.Grafika.generateEmptyResetAction;
import static rs.luka.biblioteka.grafika.Konstante.*;

/**
 * @since 1.7.'13.
 * @author Luka
 */
public class Ucenici implements FocusListener {

    private static final java.util.logging.Logger LOGGER
            = java.util.logging.Logger.getLogger(Ucenici.class.getName());
    private static final int maxKnjiga = getMaxBrojUcenikKnjiga();
    private static final JFrame win = new JFrame();

    /**
     * Dispose-uje {@link #win}
     */
    protected static void close() {
        win.dispose();
    }

    /**
     * Matrix sa knjigama pocinje od [][1], na 0 su labeli "Knjiga".
     */
    private IndexedCheckbox[][] knjige;
    private IndexedCheckbox[] ucenici;
    private final Insets INSET = new Insets(CHECKBOX_TOP_INSET, CHECKBOX_LEFT_INSET,
            CHECKBOX_BOTTOM_INSET, CHECKBOX_RIGHT_INSET); //ne sme static, da se ne bi prerano inicijalizovala
    private final JSplitPane split;
    private final JPanel butPan;
    private final JSeparator[][] knjSeparatori;
    private final JSeparator[] ucSeparatori;
    private final JPanel[] knjigePan;
    private final JPanel uceniciPan;
    private final JScrollPane scroll;
    private final JPanel pan;
    private final JTextField searchBox;
    private final JCheckBox selectAllUc;
    //private final JButton[] vratiBut;
    //private final JButton[] uzmiBut;
    private final LinkedList<UzmiVratiButton> buttons;
    private final JPanel sidePan;

    /**
     * Konstruktuje sve komponenta prozora i zove {@link #pregledUcenika()}
     */
    public Ucenici() {
        win.setTitle(UCENICI_TITLE_STRING);
        butPan = new JPanel();
        sidePan = new JPanel(null);
        if(Ucenik.sortedByRazred()) {
            knjSeparatori = new JSeparator[Podaci.getMaxBrojUcenikKnjiga()][Ucenik.getBrojRazreda()-1];
            ucSeparatori = new JSeparator[Ucenik.getBrojRazreda() - 1]; 
        }
        else {
            knjSeparatori=null;
            ucSeparatori=null;
        }
        knjigePan = new JPanel[getMaxBrojUcenikKnjiga()];
        uceniciPan = new JPanel();
        pan = new JPanel();
        scroll = new JScrollPane(pan);
        split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, scroll, butPan);
        buttons = new LinkedList<>() /*{
                 private static final long serialVersionUID = 1L;
                 @Override
                 public boolean contains(Object obj) {
                 if (this.stream().anyMatch((el) -> (el.equals(obj)))) {
                 return true;
                 }
                 return false;
                 }
                 }*/;
        //uzmiBut = new JButton[Podaci.getBrojUcenika()];
        //vratiBut = new JButton[Podaci.getBrojUcenika()];
        searchBox = new JTextField(UCENICI_SEARCH_STRING);
        selectAllUc = new JCheckBox(UCENICI_UCENICI_STRING);
    }

    /**
     * Pregled ucenika sa knjigama koje su trenutno kod njih. NE POZIVATI OSIM
     * IZ KONSTRUKTORA!
     *
     * @since 1.7.'13.
     */
    public void pregledUcenika() {
        initPanels();
        initText();
        setTextAndSeparators();
        initButtons();
        initIcons();
        initMainListeners();
        win.setVisible(true);
        initSearchBox();
        setInputMaps();
    }
    
    /**
     * Inicijalizuje prozor i panele. Postavalja closeOperation, boju, font, tekst, scroll.
     */
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
        sidePan.setLayout(null);
        sidePan.setBackground(Grafika.getBgColor());
//        sidePan.setPreferredSize(new Dimension(UCENICI_SIDEPAN_WIDTH,
//                (Podaci.getBrojUcenika() + 1) * UCENICI_HEIGHT_PER_LABEL));
//        sidePan.setBorder(BorderFactory.createLineBorder(Color.RED));
        sidePan.setAlignmentY(0);
        win.setContentPane(split);
    }

    /**
     * Postavlja precice na tastaturi.
     */
    private void setInputMaps() {
        InputMap inputMap = pan.getInputMap();
        ActionMap actionMap = pan.getActionMap();
        Console console = new rs.luka.biblioteka.debugging.Console();
        Method consoleMethod = null, undoMethod = null, redoMethod = null, fullConsoleMethod = null;
        try {
            consoleMethod = console.getClass().getDeclaredMethod("console", null);
            undoMethod = Undo.class.getDeclaredMethod("undo", null);
            redoMethod = Undo.class.getDeclaredMethod("redo", null);
            fullConsoleMethod = console.getClass().getDeclaredMethod("fullConsoleWindow", null);
        } catch (NoSuchMethodException | SecurityException ex) {
            LOGGER.log(Level.SEVERE, "Greška pri kreiranju neke od metoda", ex);
        }
        inputMap.put(KeyStroke.getKeyStroke("ctrl Z"), "undo");
        actionMap.put("undo", generateEmptyResetAction(undoMethod, null));
        inputMap.put(KeyStroke.getKeyStroke("ctrl Y"), "redo");
        actionMap.put("redo", generateEmptyResetAction(redoMethod, null));
        inputMap.put(KeyStroke.getKeyStroke("ctrl shift T"), "console");
        actionMap.put("console", generateEmptyResetAction(consoleMethod, console));
        inputMap.put(KeyStroke.getKeyStroke("ctrl alt T"), "fullconsole");
        actionMap.put("fullconsole", generateEmptyResetAction(fullConsoleMethod, console));
    }

    /**
     * Postavlja text, boje i font za JCheckBox-ove.
     */
    private void initText() {
        Podaci.sortUcenike();
        Ucenik uc;
        knjige = new IndexedCheckbox[maxKnjiga][Podaci.getBrojUcenika() + 1];
        selectAllUc.setFont(Grafika.getLabelFont());
        selectAllUc.setForeground(Grafika.getFgColor());
        selectAllUc.setBackground(Grafika.getBgColor());
        selectAllUc.setBorder(new EmptyBorder(INSET));

        ucenici = new IndexedCheckbox[Podaci.getBrojUcenika()];
        Iterator<Ucenik> it = Podaci.iteratorUcenika();
        for (int i = 0; i < Podaci.getBrojUcenika(); i++) {
            ucenici[i] = new IndexedCheckbox(it.next().getIme(), i, INVALID);
        }

        for (int i = 0; i < maxKnjiga; i++) {
            knjige[i][0] = new IndexedCheckbox(UCENICI_KNJIGE_STRING, 1, i);
            it = Podaci.iteratorUcenika();
            for (int j = 1; j < Podaci.getBrojUcenika() + 1; j++) {
                uc = it.next();
                knjige[i][j] = new IndexedCheckbox(" ", j-1, i);
                if (!uc.isKnjigaEmpty(i) && i < uc.getMaxBrojKnjiga()) {
                    knjige[i][j].setText(uc.getNaslovKnjige(i));
                }
            }
        }
        LOGGER.log(Level.FINE, "Postavio labele učenika");
        uceniciPan.add(selectAllUc);
        pan.add(uceniciPan);
    }

    /**
     * Dodaje JCheckBoxove na panel i postavlja separatore.
     */
    private void setTextAndSeparators() {
        if(ucSeparatori != null)
            for (int i = 0; i < ucSeparatori.length; i++) {
                ucSeparatori[i] = new JSeparator(SwingConstants.HORIZONTAL);
            }
        int[] razredi = Podaci.getGraniceRazreda();
        int razredIterator = 0;
        for (int i = 0; i < Podaci.getBrojUcenika(); i++) {
            uceniciPan.add(ucenici[i]);
            if (ucSeparatori != null && i == razredi[razredIterator] && i != Podaci.getBrojUcenika()-1) {
                uceniciPan.add(ucSeparatori[razredIterator]);
                razredIterator++;
            }
        }
        if(knjSeparatori != null)
            for (JSeparator[] knjSeparatori0 : knjSeparatori) {
                for (int i = 0; i < knjSeparatori0.length; i++) { //ne moze preko for : loopa.
                    knjSeparatori0[i] = new JSeparator(SwingConstants.HORIZONTAL);
                }
            }
        for (int i = 0; i < maxKnjiga; i++) {
            razredIterator = 0;
            for (int j = 0; j < Podaci.getBrojUcenika() + 1; j++) {
                knjigePan[i].add(knjige[i][j]);
                if (knjSeparatori != null && j - 1 == razredi[razredIterator] && j != Podaci.getBrojUcenika()) {
                    knjigePan[i].add(knjSeparatori[i][razredIterator]);
                    razredIterator++;
                }
            }
            pan.add(knjigePan[i]);
        }
    }

    /**
     * Inicijalizuje i dodaje glavnu dugmad na butPan i RigidArea (razmak)
     */
    private void initButtons() {
        JButton noviUc = new JButton(UCENICI_NOVIUC_STRING);
        noviUc.setPreferredSize(new Dimension(UCENICI_NOVIUC_WIDTH, UCENICI_BUTPAN_BUTTON_HEIGHT));
        noviUc.setFont(Grafika.getButtonFont());
        noviUc.addActionListener((ActionEvent e) -> {
            new UceniciUtils().dodajNovogUcenika();
        });
        butPan.add(noviUc);
        JButton delUc = new JButton(UCENICI_DELUC_STRING);
        delUc.setFont(Grafika.getButtonFont());
        delUc.setPreferredSize(new Dimension(UCENICI_DELUC_WIDTH, UCENICI_BUTPAN_BUTTON_HEIGHT));
        delUc.addActionListener((ActionEvent e) -> {
            obrisiUcenika();
        });
        butPan.add(delUc);
        JButton novaGen = new JButton(UCENICI_NOVAGEN_STRING);
        novaGen.setFont(Grafika.getButtonFont());
        novaGen.setPreferredSize(new Dimension(UCENICI_NOVAGEN_WIDTH, UCENICI_BUTPAN_BUTTON_HEIGHT));
        novaGen.addActionListener((ActionEvent e) -> {
            new UceniciUtils().dodajNovuGeneraciju();
        });
        butPan.add(novaGen);

        butPan.add(Box.createRigidArea(new Dimension(UCENICI_BUTPAN_RIGIDAREA_WIDTH, 1)));
    }

    /**
     * Postavlja ikonice i actionListener-e za iste.
     */
    private void initIcons() {
        JButton pregledBut;
        try {
            BufferedImage buttonIcon = ImageIO.read(new File(Utils.getWorkingDir() + "knjige.png"));
            pregledBut = new JButton(getIconFromImage(buttonIcon, UCENICI_ICON_WIDTH, UCENICI_ICON_HEIGHT));
            pregledBut.setToolTipText(UCENICI_PREGLEDKNJ_TOOLTIP_STRING);
        } catch (IOException ex) {
            pregledBut = new JButton(UCENICI_PREGLEDKNJ_STRING);
            LOGGER.log(Level.SEVERE, "IO greška pri učitavanju slike za dugme za pregled knjiga", ex);
        }
        pregledBut.addActionListener((ActionEvent e) -> {
            new Knjige().pregledKnjiga();
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
            saveBut.setToolTipText(UCENICI_SAVE_TOOLTIP_STRING);
        } catch (IOException ex) {
            saveBut = new JButton(UCENICI_SAVE_STRING);
            LOGGER.log(Level.SEVERE, "IO greška pri učitavanju slike za dugme za čuvanje", ex);
        }
        saveBut.addActionListener((ActionEvent e) -> {
            try {
                Save.save();
            } catch (IOException ex) {
                showMessageDialog(null, UCENICI_IOEX_MSG_STRING, UCENICI_IOEX_TITLE_STRING, JOptionPane.ERROR_MESSAGE);
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
            podesavanjaBut.setToolTipText(UCENICI_PODESAVANJA_STRING);

        } catch (IOException ex) {
            podesavanjaBut = new JButton(UCENICI_PODESAVANJA_STRING);
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

    /**
     * Postavlja listenere za selectAll, uzmiKnjigu i vratiKnjigu.
     */
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
        ItemListener uzimanjeListener = (ItemEvent e) -> {
            uzimanjeKnjige((IndexedCheckbox)e.getItem());
            sidePan.repaint();
        };
        ItemListener vracanjeListener = (ItemEvent e) -> {
            vracanjeKnjige((IndexedCheckbox)e.getItem());
            sidePan.repaint();
        };
        for (int i = 0; i < Podaci.getBrojUcenika(); i++) {
            ucenici[i].addItemListener(uzimanjeListener);

            //knjige
            for (int j = 0; j < maxKnjiga; j++) {
                if(knjige[j][i+1].getText().equals(" "))
                    knjige[j][i+1].addItemListener(uzimanjeListener);
                else
                    knjige[j][i+1].addItemListener(vracanjeListener);
            }
        }
    }

    /**
     * Inicijalizuje searchbox. Postavlje tekst, font, boju i listener-e.
     * Postavlja i velicinu sidePan-a (workaround)
     */
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
        if(Podaci.getBrojUcenika()>0)
            sidePan.setPreferredSize(new Dimension(UCENICI_SIDEPAN_WIDTH,
                    ucenici[Podaci.getBrojUcenika() - 1].getLocationOnScreen().y + UCENICI_HEIGHT_PER_LABEL));
        //NE RADI
        pan.add(sidePan);
    }

    /**
     * Vraca i resize-uje ImageIcon prema datim width i height, prema datoj image
     * @param image slika iz koje treba Icon
     * @param width sirina Icon-a
     * @param height visina Icon-a
     * @return Icon
     */
    private ImageIcon getIconFromImage(BufferedImage image, int width, int height) {
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();

        double scaleX = (double) width / imageWidth;
        double scaleY = (double) height / imageHeight;
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
            ucenik.setSelected(ucenik.isVisible() && selectAllUc.isSelected() && ucenik.isEnabled());
        }
    }

    /**
     * Postavlja sve vidljive i omogucene JCheckBox-ove na selected ili unselected.
     * @param red 
     */
    private void selectAllKnj(int red) {
        for (int j = 1; j < Podaci.getBrojUcenika() + 1; j++) {
            if (knjige[red][j].isVisible() && knjige[red][j].isEnabled()) {
                knjige[red][j].setSelected(knjige[red][0].isSelected());
            }
        }
    }

    /**
     * Brise selektova ucenike. Ako ne postoje, zove {@link UceniciUtils#obrisiUcenika()}.
     */
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
                    Podaci.obrisiUcenika(ime);
                    delCount++;
                } catch (PreviseKnjiga ex) {
                    LOGGER.log(Level.INFO, "Kod učenika {0} se nalaze neke knjige. "
                            + "Brisanje neuspešno", ime);
                    JOptionPane.showMessageDialog(null, UCENICI_PKEX_MSG_STRING, UCENICI_PKEX_MSG_STRING,
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        win.dispose();
        new Ucenici().pregledUcenika();
    }

    /**
     * Uzima knjigu za selektovanog ucenika.
     * @param red 
     */
    private void uzimanjeKnjige(IndexedCheckbox box) {
        boolean selected = false;
        int red = box.getIndex();
        int knjRed = red+1;
        for (int k = 0; k < maxKnjiga; k++) {
            if (knjige[k][knjRed].isSelected()) {
                selected = true; //makar jedan selektovan checkbox
            }
        }
        if (ucenici[red].isSelected()) {
            selected = true;
        }
        int index = buttons.indexOf(new UzmiVratiButton(red, INVALID, INVALID));
        if (index!=INVALID) { //contains radi argument.equals(element)
            if (!selected) { //ako nema selektovanih boxova, a dugme postoji
                sidePan.remove(index + 1); //ukloni dugme, +1 zato sto se na prvom mestu nalazi searchBox
                buttons.remove(index); //ukloni dugme iz liste
                for (int k = 0; k < maxKnjiga; k++) {
                    if (!knjige[k][knjRed].getText().equals(" ")) {
                        knjige[k][knjRed].setEnabled(true); //ponovo omogucuje checkboxove za vracanje
                    }
                }
                sidePan.repaint();
                return; //izadji iz listenera
            } else {
                return;
            }
        }
        UzmiVratiButton button = new UzmiVratiButton(red, INVALID,
                ucenici[red].getLocationOnScreen().y - sidePan.getLocationOnScreen().y);
        setSidePanSize(red);
        button.uzmi();
        buttons.add(button);
        sidePan.add(button);
        for (int k = 0; k < maxKnjiga; k++) {
            if (!knjige[k][knjRed].getText().equals(" ")) {
                knjige[k][knjRed].setEnabled(false); //onemogucuje jcheckboxove za vracanje
            }
        }
        sidePan.repaint();
    }

    /**
     * Vraca selektovanu knjigu za selektovanog ucenika
     * @param kol kolona u kojoj je knjige
     * @param red red u kojoj su ucenik i knjiga
     */
    private void vracanjeKnjige(IndexedCheckbox box) {
        boolean selected = false;
        int knjIndex = INVALID;
        int kol = box.getKol();
        int red = box.getIndex();
        int knjRed = red+1;
        try {
            knjIndex = Podaci.indexOfNaslov(knjige[kol][knjRed].getText());
        } catch (VrednostNePostoji ex) {
            throw new RuntimeException(ex);
        }

        for (int k = 0; k < maxKnjiga; k++) {
            if (knjige[k][knjRed].isSelected()) {
                selected = true; //makar jedan selektovan checkbox 
            }
        }
        if (!selected) { //ako nema selektovanih boxova
            int index = buttons.indexOf(new UzmiVratiButton(red, INVALID, INVALID));
            //contains radi argument.equals(element), pa argument mora da bude UzmiVratiButton
            sidePan.remove(index + 1); //ukloni dugme
            buttons.remove(index);
            ucenici[red].setEnabled(true); //ponovo omogucuje checkboxove za uzimanje
            for (int k = 0; k < maxKnjiga; k++) {
                if (knjige[k][knjRed].getText().equals(" ")) {
                    knjige[k][knjRed].setEnabled(true);
                }
            }
            return; //izadji iz listenera
        }
        for(UzmiVratiButton btn : buttons) { //ako je dugme vec tu
            if(btn.equals(red)) {
                btn.addNaslovZaVracanje(knjIndex);
                return; //izadji
            }
        }
        UzmiVratiButton button = new UzmiVratiButton(red, knjIndex,
                ucenici[red].getLocationOnScreen().y - sidePan.getLocationOnScreen().y);
        button.vrati();
        setSidePanSize(red);
        buttons.add(button);
        sidePan.add(button);
        ucenici[red].setEnabled(false); //ne mogu i uzimanje i vracanje biti vidljivi u istom trenutku
        for (int k = 0; k < maxKnjiga; k++) { //onemogućuje checkboxove za uzimanje
            if (knjige[k][knjRed].getText().equals(" ")) {
                knjige[k][knjRed].setEnabled(false);
            }
        }
    }
    
    /**
     * Postavlja size sidePan-a. WORKAROUND
     * @param red 
     */
    private void setSidePanSize(int red) { //WORKAROUND, pri scrollu se vraca na staro
        if(red==INVALID || sidePan.getHeight() < 
            ucenici[red].getLocationOnScreen().y - sidePan.getLocationOnScreen().y + UCENICI_HEIGHT_PER_LABEL) {
            sidePan.setSize(UCENICI_SIDEPAN_WIDTH,
                ucenici[red].getLocationOnScreen().y - sidePan.getLocationOnScreen().y + UCENICI_HEIGHT_PER_LABEL);
        }
    }

    /**
     * Radi pretragu.
     */
    private void search() {
        LOGGER.log(Level.FINE, "Počinjem pretragu (grafički)");
        if(ucSeparatori != null) {
            for (JSeparator sep : ucSeparatori) {
                uceniciPan.remove(sep); //remove ili samo reset ??
            }
            for (int i = 0; i < maxKnjiga; i++) {
                for (JSeparator knjSeparatori1 : knjSeparatori[i]) {
                    knjigePan[i].remove(knjSeparatori1);
                }
            }
        }
        ArrayList<Integer> ucIndexes = Pretraga.pretraziUcenike(searchBox.getText());
        if (Podaci.naslovExists(searchBox.getText())) {
            try {
                ucIndexes.addAll(Utils.extractXFromPointList(Pretraga.pretraziUcenikePoNaslovu(searchBox.getText())));
            } catch (VrednostNePostoji ex) {/*nikad, zbog provere u if-u*/
                throw new RuntimeException(ex);
            }
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

    //==========FOCUS============================================================

    @Override
    public void focusGained(FocusEvent e) {
        if (searchBox.getText().equals(UCENICI_SEARCH_STRING)) {
            searchBox.setText("");
        }
    }
    @Override
    public void focusLost(FocusEvent e) {
        if (searchBox.getText().isEmpty()) {
            searchBox.setText(UCENICI_SEARCH_STRING);
        }
    }

}
