package rs.luka.legacy.biblioteka;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.showMessageDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;
import static rs.luka.biblioteka.data.Podaci.getUcenik;
import static rs.luka.biblioteka.data.Podaci.indexOfUcenik;
import rs.luka.biblioteka.exceptions.Prazno;
import rs.luka.biblioteka.exceptions.VrednostNePostoji;
import rs.luka.biblioteka.grafika.Dijalozi;
import rs.luka.biblioteka.grafika.Grafika;

/**
 * @author Luka
 */
public class Vracanje {

    /**
     * JTextField sa imenom ucenika
     */
    private JTextField ucTF;
    /**
     * JFrame glavnog prozora.
     */
    private JFrame winV;

    public void Vracanje() {
        //-----------JFrame&JPanel------------------------------ 
        winV = new JFrame("Vracanje");
        winV.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        winV.setLocation(270, 160);
        winV.setSize(350, 130);
        winV.setResizable(false);
        JPanel panV = new JPanel(null);
        //panV.setBackground(Grafika.bgColor);
        winV.setContentPane(panV);
        //------------JLabel-----------------------------------
        JLabel ucLab = new JLabel("Unesite ime ucenika i pritisnite enter:");
        //ucLab.setFont(Grafika.labelFont);
        //ucLab.setForeground(Grafika.fgColor);
        ucLab.setBounds(20, 20, 350, 25);
        panV.add(ucLab);
        //------------JTextField-------------------------------
        ucTF = new JTextField();
        ucTF.addActionListener((ActionEvent e) -> {
            CheckBox();
        });
        ucTF.setBounds(20, 50, 200, 25);
        //ucTF.setFont(Grafika.labelFont);
        //ucTF.setForeground(Grafika.fgColor);
        //ucTF.setCaretColor(Grafika.fgColor);
        //if(Grafika.textFieldColor)
        //    ucTF.setBackground(Grafika.bgColor);
        panV.add(ucTF);
        //-----------setVisible----------------------------------
        winV.setVisible(true);
    }

    //
    //=================================================================
    //----------Part-2--------------------------------------
    //
    /**
     * Field zbog lambde
     */
    private int num = 0;

    /**
     * Prozor za selektovanje knjiga nakon unosa imena ucenika.
     */
    public void CheckBox() {
        final String uc = ucTF.getText();
        //DISPOSE-----------------------------------------
        winV.dispose();
        //---JFrame&JPanel--------------------------
        final JFrame win2 = new JFrame("Vracanje knjige");
        win2.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        win2.setBounds(270, 160, 350, 250); //test nums
        win2.setResizable(false);
        final JPanel pan2 = new JPanel(null);
        //pan2.setBackground(Grafika.bgColor);
        win2.setContentPane(pan2);
        //---JLabel---------------------------------
        JLabel knj = new JLabel("Kod ucenika su trenutno sledece knjige:");
        //knj.setFont(Grafika.labelFont);
        //knj.setForeground(Grafika.fgColor);
        knj.setBounds(20, 20, 300, 20);
        pan2.add(knj);
        //---JCheckBox-------------------------------
        final List<Integer> inx = indexOfUcenik(uc);
        if (inx.isEmpty()) {
            showMessageDialog(null, "Ucenik nije pronadjen.\n"
                    + "Proverite unos i pokusajte ponovo.", "Ucenik ne postoji.",
                    JOptionPane.ERROR_MESSAGE);
            win2.dispose();
            Vracanje();
        }
        if (inx.size() > 1) {
            num = Dijalozi.viseRazreda(inx);
        }
        final int knjNum = getUcenik(inx.get(num)).getBrojKnjiga();
        final JCheckBox[] knjJCB = new JCheckBox[knjNum];
        for (int i = 0; i < knjNum; i++) {
            knjJCB[i] = new JCheckBox(getUcenik(inx.get(num)).getNaslovKnjige(i));
            knjJCB[i].setBounds(20, 50 + i * 25, 300, 20);
            //knjJCB[i].setBackground(Grafika.bgColor);
            //knjJCB[i].setFont(Grafika.labelFont);
            //knjJCB[i].setForeground(Grafika.fgColor);
            pan2.add(knjJCB[i]);
        }
        //---JButton-------------------------------------
        JButton in = new JButton("Vrati knjige");
        in.setBounds(150, 155, 160, 37);
        in.addActionListener((ActionEvent e) -> {
            rs.luka.legacy.biblioteka.Knjige vr = new rs.luka.legacy.biblioteka.Knjige();
            List<String> knjList = new ArrayList<>();
            for (int i = 0; i < knjNum; i++) {
                if (knjJCB[i].isSelected()) {
                    knjList.add(knjJCB[i].getText());
                }
            }
            //try {
            //vr.vracanje(inx.get(num), knjList);
            showMessageDialog(null, "Knjiga vracena biblioteci!", "Uspeh!",
                    JOptionPane.INFORMATION_MESSAGE);
            win2.dispose();
            //}
            //catch(VrednostNePostoji ex) {
            showMessageDialog(null, "Ucenik nije pronadjen.\n"
                    + "Proverite unos i pokusajte ponovo.", "Ucenik ne postoji.",
                    JOptionPane.ERROR_MESSAGE);
            //}
            //catch(PraznaLista ex) {
            showMessageDialog(null, "Niste odabrali nijednu knjigu", "Los odabir",
                    JOptionPane.WARNING_MESSAGE);
            //}
        });
        pan2.add(in);
        win2.setVisible(true);
    }
}
