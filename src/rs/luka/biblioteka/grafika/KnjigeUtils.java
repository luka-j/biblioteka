/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.luka.biblioteka.grafika;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import static java.lang.Integer.parseInt;
import static java.lang.Math.min;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.showMessageDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import static rs.luka.biblioteka.data.Podaci.getUcenik;
import rs.luka.biblioteka.exceptions.Duplikat;
import rs.luka.biblioteka.exceptions.Prazno;
import rs.luka.biblioteka.exceptions.VrednostNePostoji;

/**
 *
 * @author luka
 */
public class KnjigeUtils {
    private static final java.util.logging.Logger LOGGER
            = java.util.logging.Logger.getLogger(KnjigeUtils.class.getName());
    
    /**
     * Prozor za upisivanje novog naslova.
     *
     * @since jako davno (pocetak)
     */
    protected void novi() {
        //----------JFrame&JPanel-----------------------------------------------
        final JFrame nnF = new JFrame("Unos novog naslova");
        nnF.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        nnF.setSize(330, 330);
        nnF.setLocationRelativeTo(null);
        nnF.setResizable(false);
        JPanel nnPan = new JPanel(null);
        nnPan.setBackground(Grafika.getBgColor());
        nnF.setContentPane(nnPan);
        //----------JLabels&JTextFields-----------------------------------------
        JLabel nas = new JLabel("Unesite naslov nove knjige:");
        nas.setBounds(15, 15, 300, 30);
        nas.setFont(Grafika.getLabelFont());
        nas.setForeground(Grafika.getFgColor());
        nnPan.add(nas);
        final JTextField nasTF = new JTextField();
        nasTF.setBounds(15, 50, 300, 25);
        nasTF.setFont(Grafika.getLabelFont());
        nasTF.setBackground(Grafika.getTFColor());
        nasTF.setForeground(Grafika.getFgColor());
        nasTF.setCaretColor(Grafika.getFgColor());
        nnPan.add(nasTF);
        JLabel pisac = new JLabel("Unesite pisca knjige:");
        pisac.setBounds(15, 95, 300, 30);
        pisac.setFont(Grafika.getLabelFont());
        pisac.setForeground(Grafika.getFgColor());
        JTextField pisacTF = new JTextField();
        nnPan.add(pisac);
        pisacTF.setBounds(15, 130, 300, 25);
        pisacTF.setFont(Grafika.getLabelFont());
        pisacTF.setForeground(Grafika.getFgColor());
        pisacTF.setCaretColor(Grafika.getFgColor());
        pisacTF.setBackground(Grafika.getTFColor());
        nnPan.add(pisacTF);
        JLabel kolicina = new JLabel("Unesite količinu:");
        kolicina.setBounds(15, 165, 300, 30);
        kolicina.setFont(Grafika.getLabelFont());
        kolicina.setForeground(Grafika.getFgColor());
        nnPan.add(kolicina);
        final JTextField kolTF = new JTextField();
        kolTF.setBounds(15, 200, 300, 25);
        kolTF.setFont(Grafika.getLabelFont());
        kolTF.setForeground(Grafika.getFgColor());
        kolTF.setCaretColor(Grafika.getFgColor());
        kolTF.setBackground(Grafika.getTFColor());

        nnPan.add(kolTF);
        //----------JButton-----------------------------------------------------
        JButton unos = new JButton("Unesi podatke");
        unos.setBounds(90, 240, 150, 40);
        ActionListener listener = (ActionEvent e) -> {
            rs.luka.biblioteka.funkcije.Knjige nn = new rs.luka.biblioteka.funkcije.Knjige();
            try {
                try {
                    nn.ubaciNoviNaslov(nasTF.getText(), parseInt(kolTF.getText()), pisacTF.getText());
                    showMessageDialog(null, "Knjiga dodata!", "Uspeh!", JOptionPane.INFORMATION_MESSAGE);
                    nnF.dispose();
                } catch (VrednostNePostoji ex) {
                    LOGGER.log(Level.INFO, "Polje za unos naslova je prazno pri unosu novog naslova");
                    showMessageDialog(null, "Polje za naslov je prazno.",
                            "Prazno polje", JOptionPane.ERROR_MESSAGE);
                } catch (Duplikat ex) {
                    LOGGER.log(Level.INFO, "Naslov {0} već postoji", nasTF.getText());
                    showMessageDialog(null, "Knjiga tog naslova već postoji",
                            "Duplikat", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                LOGGER.log(Level.INFO, "{0} nije broj", nasTF.getText());
                showMessageDialog(null, "Uneta količina nije broj.",
                        "Loš unos", JOptionPane.ERROR_MESSAGE);
            }
        };
        unos.addActionListener(listener);
        kolTF.addActionListener(listener);
        nnPan.add(unos);
        //----------setVisible--------------------------------------------------
        nnF.setVisible(true);
    }

    /**
     * Prozor za pretragu po ucenicima (kod koga je koja knjiga).
     *
     * @param ime ako nije null, prikazuje prozor koji trazi da se unese ime
     * ucenika, pa zatim pretrazuje i ponovo iscrtava isti prozor sa trazenim
     * podacima. Ako je ime string
     *
     * @since 25.6.'14.
     */
    protected void ucSearch(String ime, int visina) {
        rs.luka.biblioteka.funkcije.Knjige funk = new rs.luka.biblioteka.funkcije.Knjige();
        //-----------JFrame&JPanel------------------------------
        JFrame winS = new JFrame("Pretraga učenika po naslovu");
        winS.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        //winS.setLocation(270, 160);
        winS.setSize(350, 130);
        winS.setLocationRelativeTo(null);
        JPanel panS = new JPanel(null);
        panS.setBackground(Grafika.getBgColor());
        winS.setContentPane(panS);
        //------------JLabel-----------------------------------
        JLabel naslov = new JLabel("Unesite naslov i pritisnite enter:");
        naslov.setFont(Grafika.getLabelFont());
        naslov.setForeground(Grafika.getFgColor());
        naslov.setBounds(20, 20, 350, 25);
        panS.add(naslov);
        //------------JTextField&ActionListener---------------------------------
        JTextField naslovTF = new JTextField();
        ActionListener listener = (ActionEvent e) -> {
            try {
                ArrayList<Point> inx = funk.pretraziUcenike(naslovTF.getText());
                if (inx.isEmpty()) {
                    throw new Prazno("Učenici nemaju tu knjigu");
                }
                int ucVisina = min(visina, 80 + inx.size() * 21 + 58);
                //scrollPan.setBounds(0, 0, 560, ucVisina);
                panS.setLocation(0, 0);
                panS.setPreferredSize(new Dimension(560, 95 + inx.size() * 21));
                winS.setSize(580, ucVisina);
                winS.setLocationRelativeTo(null);
                StringBuilder ucBuild = new StringBuilder();
                StringBuilder dateBuild = new StringBuilder();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd. MM. yyyy");
                inx.stream().map((inx1) -> {
                    ucBuild.append(getUcenik(inx1.y).getIme()).append("<br>");
                    return inx1;
                }).forEach((inx1) -> {
                    dateBuild.append(dateFormat.format(getUcenik(inx1.y).
                            getDatumKnjige(inx1.x))).append("<br>");
                });
                JLabel ucenici = new JLabel();
                ucenici.setSize(new Dimension(300, (inx.size() + 1) * 21));
                ucenici.setLocation(10, 10);
                ucenici.setFont(Grafika.getLabelFont());
                ucenici.setText("<html>Učenici kod kojih je trenutno knjiga:<br>"
                        + ucBuild.toString() + "</html>");
                JLabel datumi = new JLabel();
                datumi.setLocation(310, 10);
                datumi.setSize(new Dimension(225, (inx.size() + 1) * 21));
                datumi.setFont(Grafika.getLabelFont());
                datumi.setText("<html>Datum kada je knjiga iznajmljena:<br>"
                        + dateBuild.toString() + "</html>");

                JButton ok = new JButton("OK");
                ok.setLocation(250, inx.size() * 21 + 50);
                ok.setSize(55, 33);
                ok.addActionListener((ActionEvent ae) -> {
                    winS.dispose();
                });
                panS.removeAll();
                panS.revalidate();
                panS.add(ucenici);
                panS.add(datumi);
                panS.add(ok);
                panS.revalidate();
                panS.repaint();

                JScrollPane scrollPan = new JScrollPane(panS);
                scrollPan.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                scrollPan.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
                winS.setContentPane(scrollPan);
                winS.revalidate();
                scrollPan.setBackground(Grafika.getBgColor());
                winS.repaint();
            } catch (VrednostNePostoji ex) {
                LOGGER.log(Level.INFO, "Naslov {0} nije pronađen", naslovTF.getText());
                showMessageDialog(null, "Knjiga nije pronađena.\n"
                        + "Proverite unos i pokušajte ponovo", "Knjiga ne postoji",
                        JOptionPane.ERROR_MESSAGE);
            } catch (Prazno ex) {
                showMessageDialog(null, "Trenutno se tražena knjiga ne nalazi ni kod koga.",
                        "Niko nije iznajmio knjigu", JOptionPane.INFORMATION_MESSAGE);
            }

        };
        naslovTF.addActionListener(listener);
        naslovTF.setBounds(20, 50, 200, 25);
        naslovTF.setFont(Grafika.getLabelFont());
        naslovTF.setBackground(Grafika.getTFColor());
        naslovTF.setForeground(Grafika.getFgColor());
        naslovTF.setCaretColor(Grafika.getFgColor());
        panS.add(naslovTF);

        //naknadno dodato, najbezbolniji nacin, da ne radim refactoring
        if (ime != null) { //ako je poslat argument koristeci checkbox
            LOGGER.log(Level.FINE, "Iniciram pretragu učenika po knjizi preko checkboxa");
            naslovTF.setText(ime);
            listener.actionPerformed(null);
        }
        //-----------setVisible----------------------------------
        winS.setVisible(true);
    }
}
