package rs.luka.biblioteka.grafika;

import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;
import rs.luka.biblioteka.funkcije.Utils;

/**
 * Sadrži sve konstante vezane za grafiku
 * @author luka
 * @since 11.'14.
 */
public class Konstante {

    /**
     * Postavlja konstantu sa datim imenom na datu vrednost
     * @param podesavanje ime konstante, dozvoljava tačke umesto donjih crta
     * @param vrednost vrednost konstante, parsuje se u int
     * @since 29.11.'14.
     */
    public static void set(String podesavanje, String vrednost) {
        if (!Utils.isInteger(vrednost)) {
            Logger.getLogger(Konstante.class.getName()).log(Level.WARNING,
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
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(Konstante.class.getName()).log(Level.WARNING, "set() nije uspeo", ex);
        }
    }

    /*----------Dijalozi.java-------------------------------------------------*/
    protected static int DIJALOZI_LINE_HEIGHT = 20;
    protected static int DIJALOZI_SIRINA = 350;
    protected static int DIJALOZI_FIXED_VISINA = 110;
    protected static int DIJALOZI_TEXT_X = 10;
    protected static int DIJALOZI_LABEL_Y = 10;
    protected static int DIJALOZI_LABEL_WIDTH = 330;
    protected static int DIJALOZI_TEXTFIELD_FIXED_Y = 25;
    protected static int DIJALOZI_TEXTFIELD_WIDTH = 300;
    protected static int DIJALOZI_TEXTFIELD_HEIGHT = 25;
    /*----------Knjige.java---------------------------------------------------*/
    protected static  String KNJIGE_SEARCH_TEXT = "Pretraži knjige...";
    protected static int KNJIGE_TOP_INSET = 5;
    protected static int KNJIGE_LEFT_INSET = 0;
    protected static int KNJIGE_BOTTOM_INSET = 5;
    protected static int KNJIGE_RIGHT_INSET = 8;
    protected static int KNJIGE_SIRINA = 550;
    protected static int KNJIGE_VISINA = 750;
    protected static int KNJIGE_PANELS_ALIGNMENT_Y = 0;
    protected static int KNJIGE_SIDEPAN_WIDTH = 140;
    protected static int KNJIGE_SIDEPAN_UCENIK_HEIGHT = 26;
    protected static int KNJIGE_SCROLL_INCREMENT = 16;
    protected static int KNJIGE_DIVIDER_LOCATION = 90;
    protected static int KNJIGE_BUTTON_HEIGHT = 35;
    protected static int KNJIGE_NOVI_WIDTH = 140;
    protected static int KNJIGE_OBRISI_WIDTH = 130;
    protected static int KNJIGE_UCSEARCH_WIDTH = 150;
    protected static int KNJIGE_SEARCHBOX_X = 2;
    protected static int KNJIGE_SEARCHBOX_Y = 0;
    protected static int KNJIGE_SEARCHBOX_WIDTH = 135;
    protected static int KNJIGE_SEARCHBOX_HEIGHT = 30;
    protected static int KNJIGE_UZMI_WIDTH = 140;
    protected static int KNJIGE_UZMI_HEIGHT = 23;
    protected static int KNJIGE_UZMI_X = 0;
    /*----------KnjigeUtils.java----------------------------------------------*/
    protected static int NOVINASLOV_WIDTH = 330;
    protected static int NOVINASLOV_HEIGHT = 330;
    protected static int NOVINASLOV_LABELS_X = 15;
    protected static int NOVINASLOV_LABELS_WIDTH = 300;
    protected static int NOVINASLOV_LABELS_HEIGHT = 30;
    protected static int NOVINASLOV_TEXTFIELDS_X = 15;
    protected static int NOVINASLOV_TEXTFIELDS_WIDTH = 300;
    protected static int NOVINASLOV_TEXTFIELDS_HEIGHT = 25;
    protected static int NOVINASLOV_NASLOV_Y = 15;
    protected static int NOVINASLOV_NASLOVTF_Y = 50;
    protected static int NOVINASLOV_PISAC_Y = 90;
    protected static int NOVINASLOV_PISACTF_Y = 125;
    protected static int NOVINASLOV_KOLICINA_Y = 165;
    protected static int NOVINASLOV_KOLICINATF_Y = 200;
    protected static int NOVINASLOV_UNOS_X = 90;
    protected static int NOVINASLOV_UNOS_Y = 240;
    protected static int NOVINASLOV_UNOS_WIDTH = 150;
    protected static int NOVINASLOV_UNOS_HEIGHT = 40;
    protected static int UCSEARCH_WIDTH = 350;
    protected static int UCSEARCH_HEIGHT = 130;
    protected static int UCSEARCH_NASLOV_X = 20;
    protected static int UCSEARCH_NASLOV_Y = 20;
    protected static int UCSEARCH_NASLOV_WIDTH = 350;
    protected static int UCSEARCH_NASLOV_HEIGHT = 25;
    protected static int UCSEARCH_NASLOVTF_X = 20;
    protected static int UCSEARCH_NASLOVTF_Y = 50;
    protected static int UCSEARCH_NASLOVTF_WIDTH = 200;
    protected static int UCSEARCH_NASLOVTF_HEIGHT = 25;
    protected static int UCSEARCH_UCVISINA_FIXED_HEIGHT = 138;
    protected static int UCSEARCH_UCENIK_HEIGHT = 21;
    protected static int UCSEARCH_PANEL_WIDTH2 = 560;
    protected static int UCSEARCH_PANEL_FIXED_HEIGHT = 95;
    protected static int UCSEARCH_WIDTH2 = 580;
    protected static int UCSEARCH_UCENICI_WIDTH = 300;
    protected static int UCSEARCH_LABELS_Y2 = 10;
    protected static int UCSEARCH_UCENICI_X = 10;
    protected static int UCSEARCH_DATUMI_X = 310;
    protected static int UCSEARCH_DATUMI_WIDTH = 225;
    protected static int UCSEARCH_OK_X = 250;
    protected static int UCSEARCH_OK_FIXED_Y = 50;
    protected static int UCSEARCH_OK_WIDTH = 55;
    protected static int UCSEARCH_OK_HEIGHT = 33;
    /*----------Podesavanja---------------------------------------------------*/
    protected static int PODESAVANJA_WIDTH = 600;
    protected static int PODESAVANJA_FIXED_HEIGHT = 100;
    protected static int PODESAVANJA_HEIGHT_PER_LABEL = 40;
    protected static int PODESAVANJA_LABEL_X = 20;
    protected static int PODESAVANJA_LABEL_FIXED_Y = 20;
    protected static int PODESAVANJA_LABEL_WIDTH = 480;
    protected static int PODESAVANJA_LABEL_HEIGHT = 17;
    protected static int PODESAVANJA_TEXTFIELD_X = 460;
    protected static int PODESAVANJA_TEXTFIELD_FIXED_Y = 15;
    protected static int PODESAVANJA_TEXTFIELD_WIDTH = 130;
    protected static int PODESAVANJA_TEXTFIELD_HEIGHT = 25;
    protected static int PODESAVANJA_BUTTONS_FIXED_Y = 25;
    protected static int PODESAVANJA_BUTTONS_HEIGHT = 35;
    protected static int PODESAVANJA_PROMENIBG_X = 20;
    protected static int PODESAVANJA_PROMENIBG_WIDTH = 190;
    protected static int PODESAVANJA_PROMENIFG_X = 220;
    protected static int PODESAVANJA_PROMENIFG_WIDTH = 180;
    protected static int PODESAVANJA_PROMENITF_X = 410;
    protected static int PODESAVANJA_PROMENITF_WIDTH = 180;
    protected static int PODESAVANJA_PROMENIBOJU_WIDTH = 600;
    protected static int PODESAVANJA_PROMENIBOJU_HEIGHT = 320;
    /*----------Ucenici-------------------------------------------------------*/
    protected static  String UCENICI_SEARCH_TEXT = "Pretraži učenike...";
    protected static int UCENICI_TOP_INSET = 5;
    protected static int UCENICI_LEFT_INSET = 5;
    protected static int UCENICI_RIGHT_INSET = 5;
    protected static int UCENICI_BOTTOM_INSET = 5;
    protected static int UCENICI_KNJPANEL_WIDTH = 170;
    protected static int UCENICI_FIXED_WIDTH = 360;
    protected static int UCENICI_HEIGHT = 600;
    protected static int UCENICI_SIDEPAN_WIDTH = 150;
    protected static int UCENICI_HEIGHT_PER_LABEL = 25;
    protected static int UCENICI_BUTPAN_BUTTON_HEIGHT = 35;
    protected static int UCENICI_NOVIUC_WIDTH = 200;
    protected static int UCENICI_DELUC_WIDTH = 150;
    protected static int UCENICI_NOVAGEN_WIDTH = 250;
    protected static int UCENICI_BUTPAN_RIGIDAREA_WIDTH = 100;
    protected static int UCENICI_ICON_HEIGHT = 35;
    protected static int UCENICI_ICON_WIDTH = UCENICI_ICON_HEIGHT;
    protected static int UCENICI_SEARCHBOX_X = 0;
    protected static int UCENICI_SEARCHBOX_Y = 0;
    protected static int UCENICI_SEARCHBOX_WIDTH = 150;
    protected static int UCENICI_SEARCHBOX_HEIGHT = 27;
    protected static int UCENICI_BUTTON_WIDTH = 140;
    protected static int UCENICI_BUTTON_HEIGHT = 23;
    protected static int UCENICI_BUTTON_X = 5;
    /*----------UceniciUtils--------------------------------------------------*/
    protected static int DODAJUCENIKA_WIDTH = 400;
    protected static int DODAJUCENIKA_HEIGHT = 250;
    protected static int DODAJUCENIKA_TEXT_X = 20;
    protected static int DODAJUCENIKA_LABEL_WIDTH = 350;
    protected static int DODAJUCENIKA_LABEL_HEIGHT = 25;
    protected static int DODAJUCENIKA_TEXTFIELD_WIDTH = 250;
    protected static int DODAJUCENIKA_TEXTFIELD_HEIGHT = 25;
    protected static int DODAJUCENIKA_IME_Y = 20;
    protected static int DODAJUCENIKA_IMETF_Y = 50;
    protected static int DODAJUCENIKA_RAZRED_Y = 90;
    protected static int DODAJUCENIKA_RAZREDTF_Y = 120;
    protected static int DODAJUCENIKA_DODAJ_X = 130;
    protected static int DODAJUCENIKA_DODAJ_Y = 170;
    protected static int DODAJUCENIKA_DODAJ_WIDTH = 140;
    protected static int DODAJUCENIKA_DODAJ_HEIGHT = 40;
    protected static int DODAJGENERACIJU_WIDTH = 610;
    protected static int DODAJGENERACIJU_HEIGHT = 550;
    protected static int DODAJGENERACIJU_TEXT_X = 10;
    protected static int DODAJGENERACIJU_IMENA_Y = 5;
    protected static int DODAJGENERACIJU_IMENA_WIDTH = 600;
    protected static int DODAJGENERACIJU_IMENA_HEIGHT = 50;
    protected static int DODAJGENERACIJU_SCROLL_Y = 60;
    protected static int DODAJGENERACIJU_SCROLL_WIDTH = 580;
    protected static int DODAJGENERACIJU_SCROLL_HEIGHT = 400;
    protected static int DODAJGENERACIJU_UNESI_X = 215;
    protected static int DODAJGENERACIJU_UNESI_Y = 470;
    protected static int DODAJGENERACIJU_UNESI_WIDTH = 170;
    protected static int DODAJGENERACIJU_UNESI_HEIGHT = 40;
    /*----------Unos----------------------------------------------------------*/
    protected static int UNOS_WIDTH = 320;
    protected static int UNOS_HEIGHT = 110;
    protected static int UNOS_HGAP = 20;
    protected static int UNOS_VGAP = 20;
    protected static int UNOS_BUTTON_WIDTH = 130;
    protected static int UNOS_BUTTON_HEIGHT = 40;
    protected static int UNOSKNJ_WIDTH = 400;
    protected static int UNOSKNJ_HEIGHT = 350;
    protected static int UNOSKNJ_TEXT_X = 20;
    protected static int UNOSKNJ_LABEL_WIDTH = 300;
    protected static int UNOSKNJ_LABEL_HEIGHT = 20;
    protected static int UNOSKNJ_TEXTFIELD_WIDTH = 300;
    protected static int UNOSKNJ_TEXTFIELD_HEIGHT = 25;
    protected static int UNOSKNJ_NASLOV_Y = 20;
    protected static int UNOSKNJ_NASLOVTF_Y = 65;
    protected static int UNOSKNJ_PISAC_Y = 105;
    protected static int UNOSKNJ_PISACTF_Y = 135;
    protected static int UNOSKNJ_KOLICINA_Y = 175;
    protected static int UNOSKNJ_KOLICINATF_Y = 205;
    protected static int UNOSKNJ_UNESI_X = 130;
    protected static int UNOSKNJ_UNESI_Y = 250;
    protected static int UNOSKNJ_UNESI_WIDTH = 120;
    protected static int UNOSKNJ_UNESI_HEIGHT = 40;
    protected static int UNOSUC_WIDTH = 400;
    protected static int UNOSUC_HEIGHT = 360;
    protected static int UNOSUC_TEXT_X = 20;
    protected static int UNOSUC_LABEL_WIDTH = 360;
    protected static int UNOSUC_LABEL_HEIGHT = 20;
    protected static int UNOSUC_TEXTFIELD_WIDTH = 320;
    protected static int UNOSUC_TEXTFIELD_HEIGHT = 25;
    protected static int UNOSUC_IME_Y = 20;
    protected static int UNOSUC_IMETF_Y = 50;
    protected static int UNOSUC_RAZRED_Y = 95;
    protected static int UNOSUC_RAZREDTF_Y = 125;
    protected static int UNOSUC_KNJIGE_Y = 170;
    protected static int UNOSUC_KNJIGETF_Y = 220;
    protected static int UNOSUC_UNESI_X = 125;
    protected static int UNOSUC_UNESI_Y = 270;
    protected static int UNOSUC_UNESI_WIDTH = 130;
    protected static int UNOSUC_UNESI_HEIGHT = 40;
}
