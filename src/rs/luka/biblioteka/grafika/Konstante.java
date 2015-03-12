package rs.luka.biblioteka.grafika;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import rs.luka.biblioteka.data.Config;
import rs.luka.biblioteka.data.XMLProperties;
import rs.luka.biblioteka.funkcije.Utils;

public class Konstante {

    private static final Logger LOGGER = Logger.getLogger(Konstante.class.getName());

    /**
     * Postavlja konstantu sa datim imenom na datu vrednost. Ako nije reč o
     * _STRING-u, zamenjuje tačke donjim crtama i prebacuje podesavanje u velika
     * slova. U suprotnom shvata argumente doslovno (napravljeno pošto se iste
     * promene moraju izvršiti i u {@link rs.luka.biblioteka.data.Strings}, pa
     * da se ne radi ista operacija dvaput).
     *
     * @param podesavanje ime konstante, dozvoljava tačke umesto donjih crta
     * @param vrednost vrednost konstante, parsuje se u int ako je potrebno
     * @since 29.11.'14.
     */
    public final void set(String podesavanje, String vrednost) {
        podesavanje = podesavanje.toUpperCase();
        podesavanje = podesavanje.replace('.', '_'); //_ je default separator, ali dozvoljavam i tacku (u fajlovima)
        Field field, modifiersField;
        try {
            field = Konstante.class.getDeclaredField(podesavanje);
            field.setAccessible(true);
            modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL); //evil
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
            LOGGER.log(Level.WARNING, "set() nije uspeo", ex);
            return;
        }
        try {
            if (podesavanje.endsWith("STRING")) {
                field.set(this, vrednost);
            } else {
                if (!Utils.isInteger(vrednost)) {
                    LOGGER.log(Level.WARNING, "set() nije uspeo, vrednost {0} nije ceo broj", vrednost);
                    return;
                }
                field.set(this, Integer.valueOf(vrednost));
                if(podesavanje.equals("UCENICI_HEIGHT") || podesavanje.equals("UCENICI_FIXED_WIDTH") ||
                        podesavanje.equals("UCENICI_KNJPANEL_WIDTH") || podesavanje.equals("KNJIGE_SIRINA") ||
                        podesavanje.equals("KNJIGE_VISINA"))
                    Config.setUsingCustomSizes();
            }
            LOGGER.log(Level.FINE, "Podesio {0} na {1}", new Object[]{podesavanje, vrednost});
            modifiersField.setInt(field, field.getModifiers() & Modifier.FINAL); //done evil
        } catch (IllegalAccessException ex) {
            Logger.getLogger(Konstante.class.getName()).log(Level.WARNING, "set() nije uspeo", ex);
        }
    }
    
    /**
     * Wrapper za set koji uzima Entry iz mape u XMLProperties umesto stringa sa kljucem i vrednoscu
     * @param k entry
     * @see #set(java.lang.String, java.lang.String) 
     */
    public final void set(Map.Entry<String, String> k) {
        set(k.getKey(), k.getValue());
    }

    final int INVALID = -1;
    /*Radi neke optimizacije na final Stringovima (can't blame it), pa moram da koristim intern() da bi set() funkcionisao*/
    public final String DA_STRING = "Da".intern();
    public final String NE_STRING = "Ne".intern();
    /*----------Config.java---------------------------------------------------*/
    public final String CONFIG_DATELIMIT_STRING = "Broj dana koji učenik sme da zadrži knjigu kod sebe".intern();
    public final String CONFIG_LOOKANDFEEL_STRING = "Izgled aplikacije (system, ocean, nimbus ili motif)".intern();
    public final String CONFIG_BRKNJIGA_STRING = "Najveći broj knjiga koji učenik može da ima kod sebe".intern();
    public final String CONFIG_LOGLEVEL_STRING = "Minimalni nivo logovanja akcija u aplikaciji".intern();
    public final String CONFIG_SAVEPERIOD_STRING = "Interval automatskog čuvanja podataka u minutima".intern();
    public final String CONFIG_MAXUNDO_STRING = "Broj akcija koje se čuvaju za undo".intern();
    public final String CONFIG_RAZREDI_STRING = "Mogući razredi učenika (razdvojeni zapetom)".intern();
    public final String CONFIG_WORKINGDIR_STRING = "Folder u kojem se čuvaju podaci".intern();
    public final String CONFIG_LOGSIZELIMIT_STRING = "Maksimalna veličina log fajla u bajtovima".intern();
    public final String CONFIG_LOGFILECOUNT_STRING = "Maksimalan broj log fajlova".intern();
    public final String CONFIG_LABELFONTNAME_STRING = "Font korišćen za labele".intern();
    public final String CONFIG_BUTFONTNAME_STRING = "Font korišćen za veliku dugmad".intern();
    public final String CONFIG_SMALLBUTFONTNAME_STRING = "Font korišćen za malu dugmad".intern();
    public final String CONFIG_DATEPERIOD_STRING = "Period proveravanja datuma (u danima)".intern();
    public final String CONFIG_KAZNA_STRING = "Dnevna kazna ako učenik ne vrati knjigu na vreme".intern();
    public final String LOADCONFIG_FNFEX_MSG_STRING = "Konfiguracijski fajl nije pronađen. Lokacija: ".intern();
    public final String LOADCONFIG_FNFEX_TITLE_STRING = "Greška pri učitavanju konfiguracije".intern();
    public final String LOADCONFIG_IOEX_MSG_STRING = "Došlo je do greške pri čitanju konfiguracijskog fajla "
            + "ili postavljanju trenutnog direktorijuma".intern();
    public final String LOADCONFIG_IOEX_TITLE_STRING = "I/O Greška".intern();
    public final String XMLCONFIG_PCEX_MSG_STRING = "Loša podešavanja za čitač XML fajla. Operacija nije izvršena.".intern();
    public final String XMLCONFIG_PCEX_TITLE_STRING = "Loša konfiguracija".intern();
    public final String XMLCONFIG_SAXEX_MSG_STRING = "Došlo je do greške pri parsiranju XML-a. \n"
            + "Konfiguracijski fajl nije dobro formatiran.".intern();
    public final String XMLCONFIG_SAXEX_TITLE_STRING = "Greška pri čitanju XML-a".intern();
    public final String XMLCONFIG_TEX_MSG_STRING = "Došlo je do greške pri transformisanju konfiguracije u XML format".intern();
    public final String XMLCONFIG_TEX_TITLE_STRING = "Greška pri transformisanju".intern();
    public final String STORECONFIG_IOEX_MSG_STRING = "Greška pri čuvanju konfiguracijskog fajla.\n"
            + "Najnovije promene podešavanja nisu sačuvane.".intern();
    public final String STORECONFIG_IOEX_TITLE_STRING = "I/O Greška".intern();
    /*----------Datumi.java---------------------------------------------------*/
    public final String DATUMI_INFO_MSG1_STRING = "Učenici koji predugo imaju knjige kod sebe:\n".intern();
    public final String DATUMI_INFO_MSG2_STRING = "Imena su sačuvana u fajl predugo.txt".intern();
    public final String DATUMI_INFO_TITLE_STRING = "Neki učenici nisu vratili knjige na vreme!".intern();
    public final String DATUMI_FNFEX_MSG_STRING = "Konfiguracijski ili fajl sa listom učenika "
            + "koji drže knjige predugo kod sebe nije pronađen.\nProvera datuma je neuspešno obavljena.".intern();
    public final String DATUMI_FNFEX_TITLE_STRING = "Greška pri proveri datuma".intern();
    public final String DATUMI_IOEX_MSG_STRING = "Došlo je do greške pri čitanju konfiguracijskog ili fajla "
            + "sa listom učenika koji imaju knjige predugo kod sebe\nProvera datuma nije urađena".intern();
    public final String DATUMI_IOEX_TITLE_STRING = "I/O greška pri proveri datuma".intern();
    /*----------Podaci.java---------------------------------------------------*/
    public final String LOADDATA_NSEEX_MSG_STRING = "Greška pri učitavanju:\nPremalo linija.".intern();
    public final String LOADDATA_PEX_MSG_STRING = "Greška pri parsiranju datuma ili loš format fajla sa podacima.".intern();
    public final String LOADDATA_RTEX_MSG_STRING = "Greška pri učitavanju podataka: loš format".intern();
    public final String LOADDATA_IOEX_MSG_STRING = "I/O greška pri čitanju podataka.".intern();
    public final String LOADDATA_EX_TITLE_STRING = "Greška pri učitavanju".intern();
    public final String DODAJGENERACIJU_DEX_MSG_STRING = "Uneli ste dva učenika sa istim imenom i prezimenom. "
            + "Jedan od njih neće biti unet.".intern();
    public final String DODAJGENERACIJU_DEX_TITLE_STRING = "Duplikat".intern();
    public final String DODAJGENERACIJU_LFEX_MSG_STRING = " nije validno ime za učenika (sadrži \"/\")\n"
            + "Možete ga naknadno uneti bez nedozvoljenih karaktera.".intern();
    public final String DODAJGENERACIJU_LFEX_TITLE_STRING = "Nedozvoljen karakter".intern();
    public final String VRACANJE_KAZNA_MSG1_STRING = "Učenik je zadržao ovu knjigu predugo kod sebe.\n"
            + "Iznos kazne je ".intern();
    public final String VRACANJE_KAZNA_MSG2_STRING = " dinara. Nastaviti (ako je kazna plaćena)?".intern();
    public final String VRACANJE_KAZNA_TITLE_STRING = "Platiti kaznu".intern();
    /*----------Ucenik.java---------------------------------------------------*/
    public final String UCENIK_SETRAZREDI_NFEX_MSG_STRING = "Podešavanje validnih razreda "
            + "neuspelo zbog lošeg Stringa.\nBiće korišćene default vrednosti".intern();
    public final String UCENIK_SETRAZREDI_NFEX_TITLE_STRING = "Greška pri inicijalizaciji".intern();
    /*----------Init.java-----------------------------------------------------*/
    public final String HANDLER_MSG1_STRING = "Došlo je do neočekivane greške. Detalji:\n".intern();
    public final String HANDLER_MSG2_STRING = "\novi podaci su sačuvani u log.".intern();
    public final String HANDLER_TITLE_STRING = "Nepoznata greška".intern();
    public final String EXIT_IOEX_MSG_STRING = "Došlo je do greške pri čuvanju na disk. "
            + "Podaci nisu sačuvani u celosti.\nPogledajte log za više informacija. Zatvoriti?".intern();
    public final String EXIT_IOEX_TITLE_STRING = "I/O Greška".intern();
    public final String EXIT_TWBL_MSG_STRING = "Došlo je do nepoznate greške pri čuvanju podataka."
            + "\n Pogledajte log za više informacija. Zatvoriti?".intern();
    public final String EXIT_TWBL_TITLE_STRING = "Nepoznata greška".intern();
    /*----------Logger.java---------------------------------------------------*/
    public final String LOGGER_IOEX_MSG_STRING = "I/O greška pri inicijalizaciji loggera.\n"
            + "Neke funkcije logovanja neće raditi".intern();
    public final String LOGGER_IOEX_TITLE_STRING = "I/O Greška".intern();
    public final String LOGGER_SECEX_MSG_STRING = "Sigurnosna greška pri inicijalizaciji loggera.\n"
            + "Neke funkcije logovanja neće raditi".intern();
    public final String LOGGER_SECEX_TITLE_STRING = "Sigurnosna greška".intern();
    /*----------funkcije.Unos.java--------------------------------------------*/
    public final String FINALIZE_IOEX_MSG_STRING = "Došlo je do greške pri čuvanju podataka".intern();
    public final String FINALIZE_IOEX_TITLE_STRING = "I/O greška pri unosu".intern();
    /*----------Dijalozi.java-------------------------------------------------*/
    final int DIJALOZI_LINE_HEIGHT = Grafika.getLabelFont().getSize() + 6;
    final int DIJALOZI_SIRINA = 350;
    final int DIJALOZI_FIXED_VISINA = 110;
    final int DIJALOZI_TEXT_X = 10;
    final int DIJALOZI_LABEL_Y = 10;
    final int DIJALOZI_LABEL_WIDTH = 330;
    final int DIJALOZI_TEXTFIELD_FIXED_Y = 30;
    final int DIJALOZI_TEXTFIELD_WIDTH = 300;
    final int DIJALOZI_TEXTFIELD_HEIGHT = 25;
    final int DIJALOZI_INFOWINDOW_WIDTH = 250;
    final int DIJALOZI_INFOWINDOW_HEGHT = 50;
    final String DIJALOZI_VISERAZREDA_MSG_STRING = "Postoji vise učenika sa tim imenom.\n"
            + "Odaberite razred:".intern();
    final String DIJALOZI_VISERAZREDA_TITLE_STRING = "Odaberite razred".intern();
    final String DIJALOZI_VISEKNJIGA_MSG1_STRING = "Postoji više knjiga sa imenom ".intern();
    final String DIJALOZI_VISEKNJIGA_MSG2_STRING = "\nOdaberite autora:".intern();
    final String DIJALOZI_VISEKNJIGA_TITLE_STRING = "Odaberite autora".intern();
    final String DIJALOZI_BROJKNJIGA_TITLE_STRING = "Broj knjiga".intern();
    final String DIJALOZI_BROJKNJIGA_MSG_STRING = "Unesite maksimalan broj "
            + "knjiga koje\nučenik može da ima kod sebe".intern();
    final String DIJALOZI_BROJKNJIGA_NFEX_MSG_STRING = "Uneta količina nije broj.".intern();
    final String DIJALOZI_BROJKNJIGA_NFEX_TITLE_STRING = "Loš format".intern();
    /*----------Grafika.java--------------------------------------------------*/
    final String LOADLNF_EX_MSG_STRING = "Došlo je do greške pri postavljanju teme.".intern();
    final String LOADLNF_EX_TITLE_STRING = "LookAndFeel greška".intern();
    final String EXIT_MSG_STRING = "Sačuvati izmene?".intern();
    final String EXIT_TITLE_STRING = "Izlaz".intern();
    /*----------IndexedCheckbox.java------------------------------------------*/
    final int CHECKBOX_TOP_INSET = 5;
    final int CHECKBOX_LEFT_INSET = 5;
    final int CHECKBOX_RIGHT_INSET = 5;
    final int CHECKBOX_BOTTOM_INSET = 5;
    /*----------Knjige.java---------------------------------------------------*/
    final int KNJIGE_SIRINA = 550;
    final int KNJIGE_VISINA = 750;
    final int KNJIGE_PANELS_ALIGNMENT_Y = 0;
    final int KNJIGE_SIDEPAN_WIDTH = 150;
    final int KNJIGE_SIDEPAN_UCENIK_HEIGHT = 26;
    final int KNJIGE_SCROLL_INCREMENT = 16;
    final int KNJIGE_DIVIDER_LOCATION = 90;
    final int KNJIGE_BUTTON_HEIGHT = 35;
    final int KNJIGE_NOVI_WIDTH = 140;
    final int KNJIGE_OBRISI_WIDTH = 130;
    final int KNJIGE_UCSEARCH_WIDTH = 150;
    final int KNJIGE_SEARCHBOX_X = 0;
    final int KNJIGE_SEARCHBOX_Y = 0;
    final int KNJIGE_SEARCHBOX_WIDTH = 135;
    final int KNJIGE_SEARCHBOX_HEIGHT = 30;
    final String KNJIGE_SEARCH_STRING = "Pretraži knjige...".intern();
    final String KNJIGE_TITLE_STRING = "Pregled knjiga".intern();
    final String KNJIGE_NASLOVI_STRING = "Naslovi:".intern();
    final String KNJIGE_KOLICINA_STRING = "Količina:".intern();
    final String KNJIGE_PISAC_STRING = "Pisac:".intern();
    final String KNJIGE_NOVI_STRING = "Ubaci novi naslov".intern();
    final String KNJIGE_OBRISI_STRING = "Obriši naslov".intern();
    final String KNJIGE_UCSEARCH_STRING = "Kod koga je naslov...".intern();
    final String KNJIGE_PKEX_MSG1_STRING = "Kod nekog učenika se nalazi knjiga ".intern();
    final String KNJIGE_PKEX_MSG2_STRING = ".\nKada vrati knjigu, pokušajte ponovo.".intern();
    final String KNJIGE_PKEX_TITLE_STRING = "Zauzeta knjiga".intern();
    final String KNJIGE_BRISANJE_DIJALOG_MSG_STRING = "Unesite naslov koji želite da obrišete i pritisnite enter:".intern();
    final String KNJIGE_BRISANJE_DIJALOG_TITLE_STRING = "Brisanje naslova".intern();
    final String KNJIGE_BRISANJE_VNPEX_MSG_STRING = "Naslov koji ste uneli ne postoji.\n"
            + "Proverite unos i pokušajte ponovo".intern();
    final String KNJIGE_BRISANJE_VNPEX_TITLE_STRING = "Greška pri brisanju naslova".intern();
    final String KNJIGE_BRISANJE_PKEX_MSG_STRING = "Kod nekog učenika "
            + " se nalazi ova knjiga\nKada vrati knjigu, pokušajte ponovo.".intern();
    final String KNJIGE_BRISANJE_PKEX_TITLE_STRING = "Zauzeta knjiga".intern();
    final String KNJIGE_UZMI_DIJALOG_TITLE_STRING = "Iznajmljivanje knjige".intern();
    /*----------KnjigeUtils.java----------------------------------------------*/
    final int NOVINASLOV_WIDTH = 330;
    final int NOVINASLOV_HEIGHT = 330;
    final int NOVINASLOV_LABELS_X = 15;
    final int NOVINASLOV_LABELS_WIDTH = 300;
    final int NOVINASLOV_LABELS_HEIGHT = 30;
    final int NOVINASLOV_TEXTFIELDS_X = 15;
    final int NOVINASLOV_TEXTFIELDS_WIDTH = 300;
    final int NOVINASLOV_TEXTFIELDS_HEIGHT = 25;
    final int NOVINASLOV_NASLOV_Y = 15;
    final int NOVINASLOV_NASLOVTF_Y = 50;
    final int NOVINASLOV_PISAC_Y = 90;
    final int NOVINASLOV_PISACTF_Y = 125;
    final int NOVINASLOV_KOLICINA_Y = 165;
    final int NOVINASLOV_KOLICINATF_Y = 200;
    final int NOVINASLOV_UNOS_X = 90;
    final int NOVINASLOV_UNOS_Y = 240;
    final int NOVINASLOV_UNOS_WIDTH = 150;
    final int NOVINASLOV_UNOS_HEIGHT = 40;
    final String NOVINASLOV_TITLE_STRING = "Unos novog naslova".intern();
    final String NOVINASLOV_NASLOV_STRING = "Unesite naslov nove knjige:".intern();
    final String NOVINASLOV_PISAC_STRING = "Unesite pisca knjige:".intern();
    final String NOVINASLOV_KOLICINA_STRING = "Unesite količinu:".intern();
    final String NOVINASLOV_UNOS_STRING = "Unesi podatke".intern();
    final String NOVINASLOV_SUCC_MSG_STRING = "Knjiga dodata!".intern();
    final String NOVINASLOV_SUCC_TITLE_STRING = "Uspeh!".intern();
    final String NOVINASLOV_VNPEX_MSG_STRING = "Polje za naslov je prazno.".intern();
    final String NOVINASLOV_VNPEX_TITLE_STRING = "Prazno polje".intern();
    final String NOVINASLOV_DEX_MSG_STRING = "Knjiga tog naslova već postoji".intern();
    final String NOVINASLOV_DEX_TITLE_STRING = "Duplikat".intern();
    final String NOVINASLOV_NFEX_MSG_STRING = "Uneta količina nije broj.".intern();
    final String NOVINASLOV_NFEX_TITLE_STRING = "Loš unos".intern();
    final String NOVINASLOV_LFEX_MSG_STRING = "Naslov ili pisac sadrži nedozvoljene karaktere (\"/\")".intern();
    final String NOVINASLOV_LFEX_TITLE_STRING = "Nedozvoljen karakter".intern();
    final int UCSEARCH_WIDTH = 350;
    final int UCSEARCH_HEIGHT = 130;
    final int UCSEARCH_NASLOV_X = 20;
    final int UCSEARCH_NASLOV_Y = 20;
    final int UCSEARCH_NASLOV_WIDTH = 350;
    final int UCSEARCH_NASLOV_HEIGHT = 25;
    final int UCSEARCH_NASLOVTF_X = 20;
    final int UCSEARCH_NASLOVTF_Y = 50;
    final int UCSEARCH_NASLOVTF_WIDTH = 200;
    final int UCSEARCH_NASLOVTF_HEIGHT = 25;
    final int UCSEARCH_UCVISINA_FIXED_HEIGHT = 138;
    final int UCSEARCH_UCENIK_HEIGHT = 21;
    final int UCSEARCH_PANEL_WIDTH2 = 560;
    final int UCSEARCH_PANEL_FIXED_HEIGHT = 95;
    final int UCSEARCH_WIDTH2 = 580;
    final int UCSEARCH_UCENICI_WIDTH = 300;
    final int UCSEARCH_LABELS_Y2 = 10;
    final int UCSEARCH_UCENICI_X = 10;
    final int UCSEARCH_DATUMI_X = 310;
    final int UCSEARCH_DATUMI_WIDTH = 225;
    final int UCSEARCH_OK_X = 250;
    final int UCSEARCH_OK_FIXED_Y = 50;
    final int UCSEARCH_OK_WIDTH = 55;
    final int UCSEARCH_OK_HEIGHT = 33;
    final String UCSEARCH_TITLE_STRING = "Pretraga učenika po naslovu".intern();
    final String UCSEARCH_NASLOV_STRING = "Unesite naslov i pritisnite enter:".intern();
    final String UCSEARCH_UCENICI_STRING = "<html>Učenici kod kojih je trenutno knjiga:<br>".intern();
    final String UCSEARCH_DATUMI_STRING = "<html>Datum kada je knjiga iznajmljena:<br>".intern();
    final String UCSEARCH_OK_STRING = "OK".intern();
    final String UCSEARCH_VNPEX_MSG_STRING = "Knjiga nije pronađena.\nProverite unos i pokušajte ponovo".intern();
    final String UCSEARCH_VNPEX_TITLE_STRING = "Knjiga ne postoji".intern();
    final String UCSEARCH_PEX_MSG_STRING = "Trenutno se tražena knjiga ne nalazi ni kod koga.".intern();
    final String UCSEARCH_PEX_TITLE_STRING = "Niko nije iznajmio knjigu".intern();
    final String PROMENIKOL_TITLE_STRING = "Promeni količinu naslova".intern();
    final String PROMENIKOL_MSG_STRING = "Unesite novu količinu i pritisnite enter.\nUnesite znakove + ili -"
            + "ako želite da izvršite\npromenu u odnosu na sadašnji broj knjiga:".intern();
    final String PROMENIKOL_NFEX_MSG_STRING = "Uneta količina nije broj.".intern();
    final String PROMENIKOL_NFEX_TITLE_STRING = "Greška pri unosu".intern();
    final String PROMENIKOL_NVKEX_MSG_STRING = "Nema dovoljno knjiga za traženu promenu.\nProverite knjige"
            + "koje se nalaze kod učenika, pa pokušajte ponovo".intern();
    final String PROMENIKOL_NVKEX_TITLE_STRING = "Nema dovoljno knjiga".intern();
    /*----------Podesavanja.java----------------------------------------------*/
    final int PODESAVANJA_WIDTH = 610;
    final int PODESAVANJA_FIXED_HEIGHT = 100;
    final int PODESAVANJA_HEIGHT_PER_LABEL = 40;
    final int PODESAVANJA_LABEL_X = 20;
    final int PODESAVANJA_LABEL_FIXED_Y = 20;
    final int PODESAVANJA_LABEL_WIDTH = 440;
    final int PODESAVANJA_LABEL_HEIGHT = 21;
    final int PODESAVANJA_TEXTFIELD_X = 460;
    final int PODESAVANJA_TEXTFIELD_FIXED_Y = 15;
    final int PODESAVANJA_TEXTFIELD_WIDTH = 130;
    final int PODESAVANJA_TEXTFIELD_HEIGHT = 25;
    final int PODESAVANJA_BUTTONS_FIXED_Y = 25;
    final int PODESAVANJA_BUTTONS_HEIGHT = 35;
    final int PODESAVANJA_PROMENIBG_X = 20;
    final int PODESAVANJA_PROMENIBG_WIDTH = 190;
    final int PODESAVANJA_PROMENIFG_X = 220;
    final int PODESAVANJA_PROMENIFG_WIDTH = 180;
    final int PODESAVANJA_PROMENITF_X = 410;
    final int PODESAVANJA_PROMENITF_WIDTH = 180;
    final int PODESAVANJA_PROMENIBOJU_WIDTH = 600;
    final int PODESAVANJA_PROMENIBOJU_HEIGHT = 320;
    final String PODESAVANJA_TITLE_STRING = "Podešavanja".intern();
    final String PODESAVANJA_SUCC_MSG_STRING = "Podešavanja su sačuvana i postaće aktivna"
            + "\n pri sledećem pokretanju programa.".intern();
    final String PODESAVANJA_SUCC_TITLE_STRING = "Podešavanja sačuvana".intern();
    final String PODESAVANJA_PKEX_MSG_STRING = "Kod nekih učenika se nalazi više knjiga nego što"
            + "podešavanje\nkoje ste postavili dozvoljava.Vratite knjige i pokušajte ponovo".intern();
    final String PODESAVANJA_PKEX_TITLE_STRING = "Previše knjiga".intern();
    final String PODESAVANJA_NFEX_MSG_STRING = "Neka od unetih vrednosti nije broj."
            + "\nProverite unos i pokušajte ponovo".intern();
    final String PODESAVANJA_NFEX_TITLE_STRING = "Loš unos".intern();
    final String PODESAVANJA_FNFEX_MSG_STRING = "Na datoj putanji radnog direktorijuma nije napravljen "
            + "novi folder.\nProverite da li je putanja ispravna i da li postoje "
            + "odgovarajuće dozvole i pokušajte ponovo.".intern();
    final String PODESAVANJA_FNFEX_TITLE_STRING = "I/O greška".intern();
    final String PODESAVANJA_LFEX_MSG_STRING = "Neki učenici pohađaju razred koji nije određen kao validan\n"
            + "Proverite vrednosti i pokušajte ponovo.".intern();
    final String PODESAVANJA_LFEX_TITLE_STRING = "Loš razred".intern();
    final String PODESAVANJA_IAEX_MSG_STRING = "Neka od unetih vrednosti nije validna. "
            + "Proverite sve i pokušajte ponovo".intern();
    final String PODESAVANJA_IAEX_TITLE_STRING = "Loša vrednost".intern();
    final String PODESAVANJA_BGBOJA_STRING = "Pozadinska boja".intern();
    final String PODESAVANJA_FGBOJA_STRING = "Boja fonta".intern();
    final String PODESAVANJA_TFBOJA_STRING = "Boja polja za unos".intern();
    final String PODESAVANJA_PROMENIBOJU_TITLE_STRING = "Promeni boju komponenti".intern();
    /*----------Ucenici.java--------------------------------------------------*/
    final int UCENICI_KNJPANEL_WIDTH = 170;
    final int UCENICI_FIXED_WIDTH = 360;
    final int UCENICI_HEIGHT = 600;
    final int UCENICI_SIDEPAN_WIDTH = 150;
    final int UCENICI_HEIGHT_PER_LABEL = 25;
    final int UCENICI_BUTPAN_BUTTON_HEIGHT = 35;
    final int UCENICI_NOVIUC_WIDTH = 200;
    final int UCENICI_DELUC_WIDTH = 150;
    final int UCENICI_NOVAGEN_WIDTH = 250;
    final int UCENICI_BUTPAN_RIGIDAREA_WIDTH = 100;
    final int UCENICI_ICON_HEIGHT = 35;
    final int UCENICI_ICON_WIDTH = 35;
    final int UCENICI_SEARCHBOX_X = 0;
    final int UCENICI_SEARCHBOX_Y = 0;
    final int UCENICI_SEARCHBOX_WIDTH = 150;
    final int UCENICI_SEARCHBOX_HEIGHT = 27;
    final String UCENICI_SEARCH_STRING = "Pretraži učenike...".intern();
    final String UCENICI_TITLE_STRING = "Pregled učenika".intern();
    final String UCENICI_UCENICI_STRING = "<html>Učenici:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
            + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
            + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br></html>".intern();
    final String UCENICI_KNJIGE_STRING = "<html>Knjige:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
            + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br></html>".intern();
    final String UCENICI_NOVIUC_STRING = "Dodati novog učenika".intern();
    final String UCENICI_DELUC_STRING = "Obrisati učenika".intern();
    final String UCENICI_NOVAGEN_STRING = "Dodati novu generaciju".intern();
    final String UCENICI_PREGLEDKNJ_TOOLTIP_STRING = "Pregled knjiga".intern();
    final String UCENICI_PREGLEDKNJ_STRING = "Knjige".intern();
    final String UCENICI_SAVE_TOOLTIP_STRING = "Sačuvaj podatke".intern();
    final String UCENICI_SAVE_STRING = "Sačuvaj".intern();
    final String UCENICI_IOEX_MSG_STRING = "Došlo je do greške pri čuvanju fajlova".intern();
    final String UCENICI_IOEX_TITLE_STRING = "I/O Greška".intern();
    final String UCENICI_PODESAVANJA_STRING = "Podešavanja".intern();
    final String UCENICI_PKEX_MSG_STRING = "Kod učenika se nalaze neke knjige.\n"
            + "Kada ih vrati, pokušajte ponovo".intern();
    final String UCENICI_PKEX_TITLE_STRING = "Greška pri brisanju".intern();
    /*----------UceniciUtils.java---------------------------------------------*/
    final int DODAJUCENIKA_WIDTH = 400;
    final int DODAJUCENIKA_HEIGHT = 250;
    final int DODAJUCENIKA_TEXT_X = 20;
    final int DODAJUCENIKA_LABEL_WIDTH = 350;
    final int DODAJUCENIKA_LABEL_HEIGHT = 25;
    final int DODAJUCENIKA_TEXTFIELD_WIDTH = 250;
    final int DODAJUCENIKA_TEXTFIELD_HEIGHT = 25;
    final int DODAJUCENIKA_IME_Y = 20;
    final int DODAJUCENIKA_IMETF_Y = 50;
    final int DODAJUCENIKA_RAZRED_Y = 90;
    final int DODAJUCENIKA_RAZREDTF_Y = 120;
    final int DODAJUCENIKA_DODAJ_X = 130;
    final int DODAJUCENIKA_DODAJ_Y = 170;
    final int DODAJUCENIKA_DODAJ_WIDTH = 140;
    final int DODAJUCENIKA_DODAJ_HEIGHT = 40;
    final String DODAJUCENIKA_TITLE_STRING = "Dodavanje novog učenika".intern();
    final String DODAJUCENIKA_IME_STRING = "Unesite ime učenika:".intern();
    final String DODAJUCENIKA_RAZRED_STRING = "Unesite razred koji učenik trenutno pohađa:".intern();
    final String DODAJUCENIKA_UNESI_STRING = "Unesi podatke".intern();
    final String DODAJUCENIKA_SUCC_MSG_STRING = "Učenik dodat!".intern();
    final String DODAJUCENIKA_SUCC_TITLE_STRING = "Uspeh!".intern();
    final String DODAJUCENIKA_NFEX_MSG_STRING = "Razred učenika je prevelik.".intern();
    final String DODAJUCENIKA_NFEX_TITLE_STRING = "Loš razred".intern();
    final String DODAJUCENIKA_DEX_MSG_STRING = "Već postoji učenik koji ide u isti razred sa istim imenom i "
            + "prezimenom.\nNovi učenik ne može biti dodat".intern();
    final String DODAJUCENIKA_DEX_TITLE_STRING = "Duplikat".intern();
    final String DODAJUCENIKA_LFEX_MSG_STRING = "Uneto ime sadrži nedozvoljene karaktere".intern();
    final String DODAJUCENIKA_LFEX_TITLE_STRING = "Nedozvoljen karakter".intern();
    final String OBRISIUCENIKA_TITLE_STRING = "Brisanje učenika".intern();
    final String OBRISIUCENIKA_MSG_STRING = "Unesite ime učenika i pritisnite enter:".intern();
    final String OBRISIUCENIKA_EMPTY_MSG_STRING = "Učenik nije pronađen\nProverite unos  i pokušajte ponovo.".intern();
    final String OBRISIUCENIKA_EMPTY_TITLE_STRING = "Učenik ne postoji".intern();
    final String OBRISIUCENIKA_SUCC_MSG_STRING = "Učenik obrisan!".intern();
    final String OBRISIUCENIKA_SUCC_TITLE_STRING = "Uspeh!".intern();
    final String OBRISIUCENIKA_PKEX_MSG_STRING = "Učenik ima preostalih knjiga.\nKada vrati, pokusajte ponovo.".intern();
    final String OBRISIUCENIKA_PKEX_TITLE_STRING = "Preostale knjige".intern();
    final int DODAJGENERACIJU_WIDTH = 610;
    final int DODAJGENERACIJU_HEIGHT = 550;
    final int DODAJGENERACIJU_TEXT_X = 10;
    final int DODAJGENERACIJU_IMENA_Y = 5;
    final int DODAJGENERACIJU_IMENA_WIDTH = 600;
    final int DODAJGENERACIJU_IMENA_HEIGHT = 50;
    final int DODAJGENERACIJU_SCROLL_Y = 60;
    final int DODAJGENERACIJU_SCROLL_WIDTH = 580;
    final int DODAJGENERACIJU_SCROLL_HEIGHT = 400;
    final int DODAJGENERACIJU_UNESI_X = 215;
    final int DODAJGENERACIJU_UNESI_Y = 470;
    final int DODAJGENERACIJU_UNESI_WIDTH = 170;
    final int DODAJGENERACIJU_UNESI_HEIGHT = 40;
    final String DODAJGENERACIJU_TITLE_STRING = "Unos nove generacije.".intern();
    final String DODAJGENERACIJU_IMENA_STRING = "<html>Unesite učenike nove generacije, odvojene zapetama <br />"
            + "<strong>Nakon dodavanja nove generacije, svi učenici "
            + "najstarije generacije će biti obrisani!!!</strong></html>".intern();
    final String DODAJGENERACIJU_UNESI_STRING = "Unesi novu generaciju".intern();
    final String DODAJGENERACIJU_SUCC_MSG_STRING = "Nova generacija dodata.".intern();
    final String DODAJGENERACIJU_SUCC_TITLE_STRING = "Uspeh!".intern();
    /*----------Unos.java-----------------------------------------------------*/
    final int UNOS_WIDTH = 320;
    final int UNOS_HEIGHT = 110;
    final int UNOS_HGAP = 20;
    final int UNOS_VGAP = 20;
    final int UNOS_BUTTON_WIDTH = 130;
    final int UNOS_BUTTON_HEIGHT = 40;
    final String UNOS_TITLE_STRING = "Unos".intern();
    final String UNOS_UCENICI_STRING = "Unos učenika".intern();
    final String UNOS_KNJIGE_STRING = "Unos knjiga".intern();
    final String UNOS_IOEX_MSG_STRING = "Došlo je do greške pri pisanju na disk.".intern();
    final String UNOS_IOEX_TITLE_STRING = "I/O greška pri unosu".intern();
    final int UNOSKNJ_WIDTH = 400;
    final int UNOSKNJ_HEIGHT = 340;
    final int UNOSKNJ_TEXT_X = 20;
    final int UNOSKNJ_LABEL_WIDTH = 300;
    final int UNOSKNJ_LABEL_HEIGHT = 20;
    final int UNOSKNJ_TEXTFIELD_WIDTH = 300;
    final int UNOSKNJ_TEXTFIELD_HEIGHT = 25;
    final int UNOSKNJ_NASLOV_Y = 20;
    final int UNOSKNJ_NASLOVTF_Y = 55;
    final int UNOSKNJ_PISAC_Y = 95;
    final int UNOSKNJ_PISACTF_Y = 125;
    final int UNOSKNJ_KOLICINA_Y = 165;
    final int UNOSKNJ_KOLICINATF_Y = 195;
    final int UNOSKNJ_UNESI_X = 130;
    final int UNOSKNJ_UNESI_Y = 240;
    final int UNOSKNJ_UNESI_WIDTH = 120;
    final int UNOSKNJ_UNESI_HEIGHT = 40;
    final String UNOSKNJ_NASLOV_STRING = "Unesite naslov knjige".intern();
    final String UNOSKNJ_PISAC_STRING = "Unesite pisca knjige:".intern();
    final String UNOSKNJ_KOLICINA_STRING = "Unesite količinu:".intern();
    final String UNOSKNJ_UNESI_STRING = "Unesi podatke".intern();
    final String UNOSKNJ_PRAZNO_MSG_STRING = "Polje za količinu je prazno.".intern();
    final String UNOSKNJ_PRAZNO_TITLE_STRING = "Prazno polje".intern();
    final String UNOSKNJ_NFEX_MSG_STRING = "Uneta količina nije broj.".intern();
    final String UNOSKNJ_NFEX_TITLE_STRING = "Loš unos".intern();
    final String UNOSKNJ_DEX_MSG_STRING = "Naslov već postoji.".intern();
    final String UNOSKNJ_DEX_TITLE_STRING = "Dupli unos".intern();
    final String UNOSKNJ_LFEX_MSG_STRING = "Naslov ili pisac sadrži nedozvoljene karaktere (\"/\")".intern();
    final String UNOSKNJ_LFEX_TITLE_STRING = "Nedozvoljen karakter".intern();
    final int UNOSUC_WIDTH = 400;
    final int UNOSUC_HEIGHT = 360;
    final int UNOSUC_TEXT_X = 20;
    final int UNOSUC_LABEL_WIDTH = 360;
    final int UNOSUC_LABEL_HEIGHT = 20;
    final int UNOSUC_TEXTFIELD_WIDTH = 320;
    final int UNOSUC_TEXTFIELD_HEIGHT = 25;
    final int UNOSUC_IME_Y = 20;
    final int UNOSUC_IMETF_Y = 50;
    final int UNOSUC_RAZRED_Y = 95;
    final int UNOSUC_RAZREDTF_Y = 125;
    final int UNOSUC_KNJIGE_Y = 170;
    final int UNOSUC_KNJIGETF_Y = 220;
    final int UNOSUC_UNESI_X = 125;
    final int UNOSUC_UNESI_Y = 270;
    final int UNOSUC_UNESI_WIDTH = 130;
    final int UNOSUC_UNESI_HEIGHT = 40;
    final String UNOSUC_IME_STRING = "Unesite ime učenika:".intern();
    final String UNOSUC_RAZRED_STRING = "Unesite razred u koji učenik ide (brojevima):".intern();
    final String UNOSUC_KNJIGE_STRING = "<html>Unesite knjige koje se trenutno nalaze"
            + " kod učenika, razdvojene zapetom:</html>".intern();
    final String UNOSUC_UNESI_STRING = "Unesi podatke".intern();
    final String UNOSUC_NFEX_MSG_STRING = "Unet razred nije broj ili nije validan".intern();
    final String UNOSUC_NFEX_TITLE_STRING = "Loš razred".intern();
    final String UNOSUC_PKEX_MSG_STRING = "Uneli ste više knjiga od limita koji ste postavili na početku"
            + "\nUčenik nije unesen".intern();
    final String UNOSUC_PKEX_TITLE_STRING = "Previše knjiga".intern();
    final String UNOSUC_DEX_MSG_STRING = "Učenik već postoji.".intern();
    final String UNOSUC_DEX_TITLE_STRING = "Dupli unos".intern();
    final String UNOSUC_VNPEX_MSG_STRING = "Jedna od knjiga nije prethodno uneta. Možete koristiti\t"
            + "\"Ubaci novi naslov\" iz pregleda knjiga da je naknadno dodate.".intern();
    final String UNOSUC_VNPEX_TITLE_STRING = "Knjiga ne postoji".intern();
    final String UNOSUC_LFEX_MSG_STRING = "Ime učenika sadrži nedozvoljene karaktere (\"/\")".intern();
    final String UNOSUC_LFEX_TITLE_STRING = "Nedozvoljen karakter".intern();
    /*----------Uzimanje.java-------------------------------------------------*/
    final String UZIMANJE_TITLE_STRING = "Iznajmljivanje knjige".intern();
    final String UZIMANJE_MSG_STRING = "Unesite naslov knjige koju učenik iznajmljuje:".intern();
    final String UZIMANJE_VNPEX_MSG_STRING = "Naslov koji ste uneli ne postoji \n"
            + "Proverite da li ste ispravno upisali naziv i pokušajte ponovo.".intern();
    final String UZIMANJE_VNPEX_TITLE_STRING = "Nepostojeća knjiga".intern();
    final String UZIMANJE_NVKEX_MSG_STRING = "Više nema knjiga tog naslova".intern();
    final String UZIMANJE_NVKEX_TITLE_STRING = "Nema knjiga".intern();
    final String UZIMANJE_DEX_MSG_STRING = "Učenik je vec iznajmio knjigu tog naslova.".intern();
    final String UZIMANJE_DEX_TITLE_STRING = "Duplikat".intern();
    final String UZIMANJE_PKEX_MSG_STRING = "Učenik trenutno ima previše knjiga kod sebe: \n".intern();
    final String UZIMANJE_PKEX_TITLE_STRING = "Previše knjiga".intern();
    /*----------UzmiVratiButton.java------------------------------------------*/
    final int SMALLBUT_WIDTH = 140;
    final int SMALLBUT_HEIGHT = 23;
    final int SMALLBUT_X = 0;
    final String SMALLBUT_UZMI_STRING = "Iznajmi knjigu".intern();
    final String SMALLBUT_VRATI_STRING = "Vrati knjigu".intern();
    final String SMALLBUT_SETKOL_STRING = "Promeni količinu".intern();
}
