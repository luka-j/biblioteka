package rs.luka.biblioteka.grafika;

import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;
import rs.luka.biblioteka.funkcije.Utils;


public class Konstante {

    private static final Logger LOG = Logger.getLogger(Konstante.class.getName());
    /**
     * Postavlja konstantu sa datim imenom na datu vrednost
     * @param podesavanje ime konstante, dozvoljava tačke umesto donjih crta
     * @param vrednost vrednost konstante, parsuje se u int
     * @since 29.11.'14.
     */
    public static void set(String podesavanje, String vrednost) {
        if (!Utils.isInteger(vrednost)) {
            LOG.log(Level.WARNING,
                    "set() nije uspeo, vrednost {0} nije ceo broj", vrednost);
            return;
        }
        try {
            podesavanje = podesavanje.replace('.', '_').toUpperCase();
            Field field = Konstante.class.getDeclaredField(podesavanje);
            /*Field modifiersField = Field.class.getDeclaredField("modifiers");
             modifiersField.setAccessible(true);
             modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL); //evil
             field.set(null, Integer.parseInt(vrednost));*/
            field.set(null, Integer.parseInt(vrednost));
            LOG.log(Level.FINE, "Podesio {0} na {1}", new Object[]{podesavanje, vrednost});
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(Konstante.class.getName()).log(Level.WARNING, "set() nije uspeo", ex);
        }
    }

    /*----------Dijalozi.java-------------------------------------------------*/
    static int DIJALOZI_LINE_HEIGHT = 20;
    static int DIJALOZI_SIRINA = 350;
    static int DIJALOZI_FIXED_VISINA = 110;
    static int DIJALOZI_TEXT_X = 10;
    static int DIJALOZI_LABEL_Y = 10;
    static int DIJALOZI_LABEL_WIDTH = 330;
    static int DIJALOZI_TEXTFIELD_FIXED_Y = 25;
    static int DIJALOZI_TEXTFIELD_WIDTH = 300;
    static int DIJALOZI_TEXTFIELD_HEIGHT = 25;
    /*----------Knjige.java---------------------------------------------------*/
    static  String KNJIGE_SEARCH_TEXT = "Pretraži knjige...";
    static int KNJIGE_TOP_INSET = 5;
    static int KNJIGE_LEFT_INSET = 0;
    static int KNJIGE_BOTTOM_INSET = 5;
    static int KNJIGE_RIGHT_INSET = 8;
    static int KNJIGE_SIRINA = 550;
    static int KNJIGE_VISINA = 750;
    static int KNJIGE_PANELS_ALIGNMENT_Y = 0;
    static int KNJIGE_SIDEPAN_WIDTH = 140;
    static int KNJIGE_SIDEPAN_UCENIK_HEIGHT = 26;
    static int KNJIGE_SCROLL_INCREMENT = 16;
    static int KNJIGE_DIVIDER_LOCATION = 90;
    static int KNJIGE_BUTTON_HEIGHT = 35;
    static int KNJIGE_NOVI_WIDTH = 140;
    static int KNJIGE_OBRISI_WIDTH = 130;
    static int KNJIGE_UCSEARCH_WIDTH = 150;
    static int KNJIGE_SEARCHBOX_X = 2;
    static int KNJIGE_SEARCHBOX_Y = 0;
    static int KNJIGE_SEARCHBOX_WIDTH = 135;
    static int KNJIGE_SEARCHBOX_HEIGHT = 30;
    /*static int KNJIGE_UZMI_WIDTH = 140; 
    static int KNJIGE_UZMI_HEIGHT = 23;
    static int KNJIGE_UZMI_X = 0;*/
    /*----------KnjigeUtils.java----------------------------------------------*/
    static int NOVINASLOV_WIDTH = 330;
    static int NOVINASLOV_HEIGHT = 330;
    static int NOVINASLOV_LABELS_X = 15;
    static int NOVINASLOV_LABELS_WIDTH = 300;
    static int NOVINASLOV_LABELS_HEIGHT = 30;
    static int NOVINASLOV_TEXTFIELDS_X = 15;
    static int NOVINASLOV_TEXTFIELDS_WIDTH = 300;
    static int NOVINASLOV_TEXTFIELDS_HEIGHT = 25;
    static int NOVINASLOV_NASLOV_Y = 15;
    static int NOVINASLOV_NASLOVTF_Y = 50;
    static int NOVINASLOV_PISAC_Y = 90;
    static int NOVINASLOV_PISACTF_Y = 125;
    static int NOVINASLOV_KOLICINA_Y = 165;
    static int NOVINASLOV_KOLICINATF_Y = 200;
    static int NOVINASLOV_UNOS_X = 90;
    static int NOVINASLOV_UNOS_Y = 240;
    static int NOVINASLOV_UNOS_WIDTH = 150;
    static int NOVINASLOV_UNOS_HEIGHT = 40;
    static int UCSEARCH_WIDTH = 350;
    static int UCSEARCH_HEIGHT = 130;
    static int UCSEARCH_NASLOV_X = 20;
    static int UCSEARCH_NASLOV_Y = 20;
    static int UCSEARCH_NASLOV_WIDTH = 350;
    static int UCSEARCH_NASLOV_HEIGHT = 25;
    static int UCSEARCH_NASLOVTF_X = 20;
    static int UCSEARCH_NASLOVTF_Y = 50;
    static int UCSEARCH_NASLOVTF_WIDTH = 200;
    static int UCSEARCH_NASLOVTF_HEIGHT = 25;
    static int UCSEARCH_UCVISINA_FIXED_HEIGHT = 138;
    static int UCSEARCH_UCENIK_HEIGHT = 21;
    static int UCSEARCH_PANEL_WIDTH2 = 560;
    static int UCSEARCH_PANEL_FIXED_HEIGHT = 95;
    static int UCSEARCH_WIDTH2 = 580;
    static int UCSEARCH_UCENICI_WIDTH = 300;
    static int UCSEARCH_LABELS_Y2 = 10;
    static int UCSEARCH_UCENICI_X = 10;
    static int UCSEARCH_DATUMI_X = 310;
    static int UCSEARCH_DATUMI_WIDTH = 225;
    static int UCSEARCH_OK_X = 250;
    static int UCSEARCH_OK_FIXED_Y = 50;
    static int UCSEARCH_OK_WIDTH = 55;
    static int UCSEARCH_OK_HEIGHT = 33;
    /*----------Podesavanja.java----------------------------------------------*/
    static int PODESAVANJA_WIDTH = 600;
    static int PODESAVANJA_FIXED_HEIGHT = 100;
    static int PODESAVANJA_HEIGHT_PER_LABEL = 40;
    static int PODESAVANJA_LABEL_X = 20;
    static int PODESAVANJA_LABEL_FIXED_Y = 20;
    static int PODESAVANJA_LABEL_WIDTH = 480;
    static int PODESAVANJA_LABEL_HEIGHT = 17;
    static int PODESAVANJA_TEXTFIELD_X = 460;
    static int PODESAVANJA_TEXTFIELD_FIXED_Y = 15;
    static int PODESAVANJA_TEXTFIELD_WIDTH = 130;
    static int PODESAVANJA_TEXTFIELD_HEIGHT = 25;
    static int PODESAVANJA_BUTTONS_FIXED_Y = 25;
    static int PODESAVANJA_BUTTONS_HEIGHT = 35;
    static int PODESAVANJA_PROMENIBG_X = 20;
    static int PODESAVANJA_PROMENIBG_WIDTH = 190;
    static int PODESAVANJA_PROMENIFG_X = 220;
    static int PODESAVANJA_PROMENIFG_WIDTH = 180;
    static int PODESAVANJA_PROMENITF_X = 410;
    static int PODESAVANJA_PROMENITF_WIDTH = 180;
    static int PODESAVANJA_PROMENIBOJU_WIDTH = 600;
    static int PODESAVANJA_PROMENIBOJU_HEIGHT = 320;
    /*----------Ucenici.java--------------------------------------------------*/
    static  String UCENICI_SEARCH_TEXT = "Pretraži učenike...";
    static int UCENICI_TOP_INSET = 5;
    static int UCENICI_LEFT_INSET = 5;
    static int UCENICI_RIGHT_INSET = 5;
    static int UCENICI_BOTTOM_INSET = 5;
    static int UCENICI_KNJPANEL_WIDTH = 170;
    static int UCENICI_FIXED_WIDTH = 360;
    static int UCENICI_HEIGHT = 600;
    static int UCENICI_SIDEPAN_WIDTH = 150;
    static int UCENICI_HEIGHT_PER_LABEL = 25;
    static int UCENICI_BUTPAN_BUTTON_HEIGHT = 35;
    static int UCENICI_NOVIUC_WIDTH = 200;
    static int UCENICI_DELUC_WIDTH = 150;
    static int UCENICI_NOVAGEN_WIDTH = 250;
    static int UCENICI_BUTPAN_RIGIDAREA_WIDTH = 100;
    static int UCENICI_ICON_HEIGHT = 35;
    static int UCENICI_ICON_WIDTH = UCENICI_ICON_HEIGHT;
    static int UCENICI_SEARCHBOX_X = 0;
    static int UCENICI_SEARCHBOX_Y = 0;
    static int UCENICI_SEARCHBOX_WIDTH = 150;
    static int UCENICI_SEARCHBOX_HEIGHT = 27;
    /*----------UceniciUtils.java---------------------------------------------*/
    static int DODAJUCENIKA_WIDTH = 400;
    static int DODAJUCENIKA_HEIGHT = 250;
    static int DODAJUCENIKA_TEXT_X = 20;
    static int DODAJUCENIKA_LABEL_WIDTH = 350;
    static int DODAJUCENIKA_LABEL_HEIGHT = 25;
    static int DODAJUCENIKA_TEXTFIELD_WIDTH = 250;
    static int DODAJUCENIKA_TEXTFIELD_HEIGHT = 25;
    static int DODAJUCENIKA_IME_Y = 20;
    static int DODAJUCENIKA_IMETF_Y = 50;
    static int DODAJUCENIKA_RAZRED_Y = 90;
    static int DODAJUCENIKA_RAZREDTF_Y = 120;
    static int DODAJUCENIKA_DODAJ_X = 130;
    static int DODAJUCENIKA_DODAJ_Y = 170;
    static int DODAJUCENIKA_DODAJ_WIDTH = 140;
    static int DODAJUCENIKA_DODAJ_HEIGHT = 40;
    static int DODAJGENERACIJU_WIDTH = 610;
    static int DODAJGENERACIJU_HEIGHT = 550;
    static int DODAJGENERACIJU_TEXT_X = 10;
    static int DODAJGENERACIJU_IMENA_Y = 5;
    static int DODAJGENERACIJU_IMENA_WIDTH = 600;
    static int DODAJGENERACIJU_IMENA_HEIGHT = 50;
    static int DODAJGENERACIJU_SCROLL_Y = 60;
    static int DODAJGENERACIJU_SCROLL_WIDTH = 580;
    static int DODAJGENERACIJU_SCROLL_HEIGHT = 400;
    static int DODAJGENERACIJU_UNESI_X = 215;
    static int DODAJGENERACIJU_UNESI_Y = 470;
    static int DODAJGENERACIJU_UNESI_WIDTH = 170;
    static int DODAJGENERACIJU_UNESI_HEIGHT = 40;
    /*----------Unos.java-----------------------------------------------------*/
    static int UNOS_WIDTH = 320;
    static int UNOS_HEIGHT = 110;
    static int UNOS_HGAP = 20;
    static int UNOS_VGAP = 20;
    static int UNOS_BUTTON_WIDTH = 130;
    static int UNOS_BUTTON_HEIGHT = 40;
    static int UNOSKNJ_WIDTH = 400;
    static int UNOSKNJ_HEIGHT = 350;
    static int UNOSKNJ_TEXT_X = 20;
    static int UNOSKNJ_LABEL_WIDTH = 300;
    static int UNOSKNJ_LABEL_HEIGHT = 20;
    static int UNOSKNJ_TEXTFIELD_WIDTH = 300;
    static int UNOSKNJ_TEXTFIELD_HEIGHT = 25;
    static int UNOSKNJ_NASLOV_Y = 20;
    static int UNOSKNJ_NASLOVTF_Y = 65;
    static int UNOSKNJ_PISAC_Y = 105;
    static int UNOSKNJ_PISACTF_Y = 135;
    static int UNOSKNJ_KOLICINA_Y = 175;
    static int UNOSKNJ_KOLICINATF_Y = 205;
    static int UNOSKNJ_UNESI_X = 130;
    static int UNOSKNJ_UNESI_Y = 250;
    static int UNOSKNJ_UNESI_WIDTH = 120;
    static int UNOSKNJ_UNESI_HEIGHT = 40;
    static int UNOSUC_WIDTH = 400;
    static int UNOSUC_HEIGHT = 360;
    static int UNOSUC_TEXT_X = 20;
    static int UNOSUC_LABEL_WIDTH = 360;
    static int UNOSUC_LABEL_HEIGHT = 20;
    static int UNOSUC_TEXTFIELD_WIDTH = 320;
    static int UNOSUC_TEXTFIELD_HEIGHT = 25;
    static int UNOSUC_IME_Y = 20;
    static int UNOSUC_IMETF_Y = 50;
    static int UNOSUC_RAZRED_Y = 95;
    static int UNOSUC_RAZREDTF_Y = 125;
    static int UNOSUC_KNJIGE_Y = 170;
    static int UNOSUC_KNJIGETF_Y = 220;
    static int UNOSUC_UNESI_X = 125;
    static int UNOSUC_UNESI_Y = 270;
    static int UNOSUC_UNESI_WIDTH = 130;
    static int UNOSUC_UNESI_HEIGHT = 40;
    /*----------UzmiVratiButton.java------------------------------------------*/
    static int UVBUTTON_WIDTH = 140;
    static int UVBUTTON_HEIGHT = 23;
    static int UVBUTTON_X = 5;
}
