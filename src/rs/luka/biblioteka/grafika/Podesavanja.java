package rs.luka.biblioteka.grafika;

import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import static java.lang.String.valueOf;
import java.util.ArrayList;
import java.util.logging.Level;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.showMessageDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import rs.luka.biblioteka.data.Config;
import rs.luka.biblioteka.exceptions.ConfigException;
import rs.luka.biblioteka.exceptions.LosFormat;
import rs.luka.biblioteka.exceptions.PreviseKnjiga;
import static rs.luka.biblioteka.grafika.Grafika.getBgColor;
import static rs.luka.biblioteka.grafika.Konstante.*;

/**
 * Klasa za podesavanja - grafiku. Front-end za menjanje config-a.
 *
 * @author lukatija
 * @since 19.8.'14.
 */
public class Podesavanja {
    
    private static final java.util.logging.Logger LOGGER = 
            java.util.logging.Logger.getLogger(Podesavanja.class.getName());

    //Sve komponente prozora podesavanja.
    private JPanel pan;
    private JLabel[] labels;
    private JTextField[] textfields;

    /**
     * Iscrtava prozor sa podesavanjima.
     */
    public void podesavanja() {
        //----------JFrame&JPanel-----------------------------------------------
        JFrame win = new JFrame("Podešavanja");
        win.setResizable(false);
        win.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        win.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    sacuvaj();
                    showMessageDialog(null, "Podešavanja su sačuvana i "
                            + "postaće aktivna\n pri sledećem pokretanju programa.",
                            "Podešavanja sačuvana", JOptionPane.INFORMATION_MESSAGE);
                    win.dispose();
                } catch (PreviseKnjiga ex) {
                    LOGGER.log(Level.WARNING, "Kod nekih učenika se nalazi više "
                            + "knjiga nego što novo podešavanje dozvoljava. "
                            + "Podešavanje za broj knjiga nije sačuvano");
                    showMessageDialog(null, "Kod nekih učenika se nalazi "
                            + "više knjiga nego sto\npodešavanje koje ste postavili dozvoljava.\n"
                            + "Vratite knjige i pokusajte ponovo", "Previše knjiga",
                            JOptionPane.ERROR_MESSAGE);
                } catch (NumberFormatException ex) {
                    LOGGER.log(Level.INFO, "Neka od unetih vrednosti podešavanja "
                            + "nije broj. Podešavanja ne izlaze.");
                    showMessageDialog(pan, "Neka od unetih vrednosti nije broj."
                            + "\nProverite unos i pokušajte ponovo", "Loš unos",
                            JOptionPane.ERROR_MESSAGE);
                } catch (FileNotFoundException ex) {
                    LOGGER.log(Level.WARNING, "Na datoj putanji nije napravljen folder."
                            + "IO greška ili loša putanja.");
                    showMessageDialog(null, "Na datoj putanji radnog direktorijuma nije napravljen "
                            + "novi folder.\nProverite da li je putanja ispravna i da li postoje "
                            + "odgovarajuće dozvole i pokušajte ponovo.", "I/O greška", 
                            JOptionPane.ERROR_MESSAGE);
                } catch (LosFormat ex) {
                    LOGGER.log(Level.INFO, "Postoje učenici sa razredom koji po novom podešavanju nije validan");
                    showMessageDialog(null, "Neki učenici pohađaju razred koji nije određen kao validan\n"
                            + "Proverite vrednosti i pokušajte ponovo.", "Loš razred",JOptionPane.ERROR_MESSAGE);
                } catch(IllegalArgumentException ex) {
                    LOGGER.log(Level.WARNING, "Neka od vrednosti podešavanja nije validna\n");
                    showMessageDialog(null, "Neka od unetih vrednosti nije validna. "
                            + "Proverite sve i pokušajte ponovo", "Loša vrednost", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        pan = new JPanel(null);
        pan.setBackground(getBgColor());
        win.setContentPane(pan);
        //---------JLabels&JTextFields------------------------------------------
        ArrayList<String> names = Config.getUserFriendlyNames();
        win.setSize(PODESAVANJA_WIDTH, PODESAVANJA_FIXED_HEIGHT + names.size()*PODESAVANJA_HEIGHT_PER_LABEL); 
        win.setLocationRelativeTo(null); //win.setLocation                     !!
        labels = new JLabel[names.size()];
        textfields = new JTextField[names.size()];
        for(int i=0; i<labels.length; i++) {
            labels[i] = new JLabel(names.get(i));
            labels[i].setBounds(PODESAVANJA_LABEL_X, PODESAVANJA_LABEL_FIXED_Y+i*PODESAVANJA_HEIGHT_PER_LABEL,
                    PODESAVANJA_LABEL_WIDTH, PODESAVANJA_LABEL_HEIGHT);
            labels[i].setFont(Grafika.getLabelFont());
            labels[i].setForeground(Grafika.getFgColor());
            pan.add(labels[i]);
            
            textfields[i] = new JTextField(Config.get(names.get(i)));
            textfields[i].setBounds(PODESAVANJA_TEXTFIELD_X, PODESAVANJA_TEXTFIELD_FIXED_Y +
                 i * PODESAVANJA_HEIGHT_PER_LABEL, PODESAVANJA_TEXTFIELD_WIDTH, PODESAVANJA_TEXTFIELD_HEIGHT);
            textfields[i].setFont(Grafika.getLabelFont());
            textfields[i].setForeground(Grafika.getFgColor());
            textfields[i].setCaretColor(Grafika.getFgColor());
                textfields[i].setBackground(Grafika.getTFColor());
            pan.add(textfields[i]);
        }
        //----------JButtons&JCheckBoxes----------------------------------------
        JButton promeniBojuBut = new JButton("Pozadinska boja");
        promeniBojuBut.addActionListener((ActionEvent e3) -> {
            promeniBoju("bg");
        });
        promeniBojuBut.setBounds(PODESAVANJA_PROMENIBG_X, PODESAVANJA_BUTTONS_FIXED_Y + names.size() *
                PODESAVANJA_HEIGHT_PER_LABEL, PODESAVANJA_PROMENIBG_WIDTH, PODESAVANJA_BUTTONS_HEIGHT);
        pan.add(promeniBojuBut);
        JButton promeniFgBojuBut = new JButton("Boja fonta");
        promeniFgBojuBut.addActionListener((ActionEvent e) -> {
            promeniBoju("fg");
        });
        promeniFgBojuBut.setBounds(PODESAVANJA_PROMENIFG_X, PODESAVANJA_BUTTONS_FIXED_Y + names.size() *
                PODESAVANJA_HEIGHT_PER_LABEL, PODESAVANJA_PROMENIFG_WIDTH, PODESAVANJA_BUTTONS_HEIGHT);
        pan.add(promeniFgBojuBut);
        JButton promeniTFBojuBut = new JButton("Boja polja za unos");
        promeniTFBojuBut.addActionListener((ActionEvent e) -> {
            promeniBoju("tf");
        });
        promeniTFBojuBut.setBounds(PODESAVANJA_PROMENITF_X, PODESAVANJA_BUTTONS_FIXED_Y + names.size() *
                PODESAVANJA_HEIGHT_PER_LABEL, PODESAVANJA_PROMENITF_WIDTH, PODESAVANJA_BUTTONS_HEIGHT);
        pan.add(promeniTFBojuBut);
        win.setVisible(true);
    }

    /**
     * Prozor sa JColorChooserom koji pri zatvaranju cuva boju u config i
     * refreshuje glavni i prozor podesavanja.
     *
     * @param str bg za pozadinsku, fg za boju fonta
     */
    private void promeniBoju(String str) {
        //---------JFrameJColorChooser------------------------------------------
        JFrame win = new JFrame("Promeni pozadinsku boju");
        win.setSize(PODESAVANJA_PROMENIBOJU_WIDTH, PODESAVANJA_PROMENIBOJU_HEIGHT);
        win.setResizable(false);
        win.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        win.setLocationRelativeTo(null);
        final JColorChooser colorChooser = new JColorChooser(getBgColor());
        colorChooser.setDragEnabled(true);
        win.setContentPane(colorChooser);
        //---------windowClosing------------------------------------------------
        win.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                switch (str) {
                    case "bg":
                        Config.set("bgBoja", valueOf(colorChooser.getColor().getRGB()));
                        LOGGER.log(Level.CONFIG, "Pozadinska boja podešena: {0}",
                                colorChooser.getColor());
                        break;
                    case "fg":
                        LOGGER.log(Level.CONFIG, "Boja fonta podešena: {0}",
                                colorChooser.getColor());
                        Config.set("fgBoja", valueOf(colorChooser.getColor().getRGB()));
                        break;
                    case "tf":
                        LOGGER.log(Level.CONFIG, "Boja polja podešena: {0}",
                                colorChooser.getColor());
                        Config.set("TFColor", valueOf(colorChooser.getColor().getRGB()));
                }
                refresh();
                win.dispose();
            }
        });
        win.setVisible(true);
    }

    /**
     * Cuva sva podesavanja u config fajl. Poziva se pri zatvaranju prozora sa
     * podesavanjima.
     *
     * @throws PreviseKnjiga
     */
    private void sacuvaj() throws PreviseKnjiga, FileNotFoundException, IllegalArgumentException, LosFormat {
        try {
            for(int i=0; i<labels.length; i++) {
                if(!textfields[i].getText().isEmpty()) {
                    Config.set(labels[i].getText(), textfields[i].getText());
                }
            }
        }
        catch(ConfigException ex) {
            switch(ex.getMessage()) {
                case "brKnjiga": throw new PreviseKnjiga(ex);
                case "razredi": throw new LosFormat("razredi", ex);
                case "workingDir": throw new FileNotFoundException();
                default: throw new IllegalArgumentException(ex);
            }
        }
    }

    /**
     * Ponovo postavlja boje svih komponenata prozora.
     */
    protected void refresh() {
        Grafika.setVariables();
        pan.setBackground(getBgColor());
        for(int i=0; i<labels.length; i++) {
            labels[i].setForeground(Grafika.getFgColor());
            textfields[i].setForeground(Grafika.getFgColor());
            textfields[i].setCaretColor(Grafika.getFgColor());
                textfields[i].setBackground(Grafika.getTFColor());
        }
        pan.repaint();
        LOGGER.log(Level.FINE, "Refreshovao boje prozora Podesavanja");
    }
}
