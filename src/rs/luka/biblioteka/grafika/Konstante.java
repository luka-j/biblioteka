package rs.luka.biblioteka.grafika;

import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;
import rs.luka.biblioteka.funkcije.Utils;

public class Konstante {

    private static final Logger LOG = Logger.getLogger(Konstante.class.getName());

    /**
     * Postavlja konstantu sa datim imenom na datu vrednost. Ako nije reč o _STRING-u,
     * zamenjuje tačke donjim crtama i prebacuje podesavanje u velika slova. U suprotnom
     * shvata argumente doslovno (napravljeno pošto se iste promene moraju izvršiti i u 
     * {@link rs.luka.biblioteka.data.Strings}, pa da se ne radi ista operacija dvaput).
     *
     * @param podesavanje ime konstante, dozvoljava tačke umesto donjih crta
     * @param vrednost vrednost konstante, parsuje se u int
     * @since 29.11.'14.
     */
    public static void set(String podesavanje, String vrednost) {
        if (!podesavanje.endsWith("STRING") && !podesavanje.endsWith("DESC")) {
            if (!Utils.isInteger(vrednost)) {
                LOG.log(Level.WARNING,
                        "set() nije uspeo, vrednost {0} nije ceo broj", vrednost);
                return;
            }
            podesavanje = podesavanje.toUpperCase().replace('.', '_');
            try {
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
        } else {
            try {
                Field field = Konstante.class.getDeclaredField(podesavanje);
                field.set(null, vrednost);
            } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException ex) {
                LOG.log(Level.WARNING, "set() za STRING nije uspeo", ex);
            }
        }
    }

    static final int INVALID = -1;
    /*----------Config.java---------------------------------------------------*/
    public static String CONFIG_DATELIMIT_DESC = "Broj dana koji učenik sme da zadrži knjigu kod sebe";
    public static String CONFIG_LOOKANDFEEL_DESC = "Izgled aplikacije (system, ocean, nimbus ili motif)";
    public static String CONFIG_BRKNJIGA_DESC = "Najveći broj knjiga koji učenik može da ima kod sebe";
    public static String CONFIG_LOGLEVEL_DESC = "Minimalni nivo logovanja akcija u aplikaciji";
    public static String CONFIG_SAVEPERIOD_DESC = "Interval automatskog čuvanja podataka u minutima";
    public static String CONFIG_MAXUNDO_DESC = "Broj akcija koje se čuvaju za undo";
    public static String CONFIG_RAZREDI_DESC = "Mogući razredi učenika (razdvojeni zapetom)";
    public static String CONFIG_WORKINGDIR_DESC = "Folder u kojem se čuvaju podaci";
    public static String CONFIG_LOGSIZELIMIT_DESC = "Maksimalna veličina log fajla u bajtovima";
    public static String CONFIG_LOGFILECOUNT_DESC = "Maksimalan broj log fajlova";
    public static String CONFIG_LABELFONTNAME_DESC = "Font korišćen za labele";
    public static String CONFIG_BUTFONTNAME_DESC = "Font korišćen za veliku dugmad";
    public static String CONFIG_SMALLBUTFONTNAME_DESC = "Font korišćen za malu dugmad";
    public static String CONFIG_DATEPERIOD_DESC = "Period proveravanja datuma (u danima)";
    public static String LOADCONFIG_FNFEX_MSG_STRING = "Konfiguracijski fajl nije pronađen. Lokacija: ";
    public static String LOADCONFIG_FNFEX_TITLE_STRING = "Greška pri učitavanju konfiguracije";
    public static String LOADCONFIG_IOEX_MSG_STRING = "Došlo je do greške pri čitanju konfiguracijskog fajla "
            + "ili postavljanju trenutnog direktorijuma";
    public static String LOADCONFIG_IOEX_TITLE_STRING = "I/O Greška";
    public static String STORECONFIG_IOEX_MSG_STRING = "Greška pri čuvanju konfiguracijskog fajla.\n"
            + "Najnovije promene podešavanja nisu sačuvane.";
    public static String STORECONFIG_IOEX_TITLE_STRING = "I/O Greška";
    /*----------Datumi.java---------------------------------------------------*/
    public static String DATUMI_INFO_MSG1_STRING = "Učenici koji predugo imaju knjige kod sebe:\n";
    public static String DATUMI_INFO_MSG2_STRING = "Imena su sačuvana u fajl predugo.txt";
    public static String DATUMI_INFO_TITLE_STRING = "Neki učenici nisu vratili knjige na vreme!";
    public static String DATUMI_FNFEX_MSG_STRING = "Konfiguracijski ili fajl sa listom učenika "
            + "koji drže knjige predugo kod sebe nije pronađen.\nProvera datuma je neuspešno obavljena.";
    public static String DATUMI_FNFEX_TITLE_STRING = "Greška pri proveri datuma";
    public static String DATUMI_IOEX_MSG_STRING = "Došlo je do greške pri čitanju konfiguracijskog ili fajla "
            + "sa listom učenika koji imaju knjige predugo kod sebe\nProvera datuma nije urađena";
    public static String DATUMI_IOEX_TITLE_STRING = "I/O greška pri proveri datuma";
    /*----------Podaci.java---------------------------------------------------*/
    public static String LOADDATA_NSEEX_MSG_STRING = "Greška pri učitavanju:\nPremalo linija.";
    public static String LOADDATA_PEX_MSG_STRING = "Greška pri parsiranju datuma ili loš format fajla sa podacima.";
    public static String LOADDATA_RTEX_MSG_STRING = "Greška pri učitavanju podataka: loš format";
    public static String LOADDATA_IOEX_MSG_STRING = "I/O greška pri čitanju podataka.";
    public static String LOADDATA_EX_TITLE_STRING = "Greška pri učitavanju";
    public static String DODAJGENERACIJU_DEX_MSG_STRING = "Uneli ste dva učenika sa istim imenom i prezimenom. "
            + "Jedan od njih neće biti unet.";
    public static String DODAJGENERACIJU_DEX_TITLE_STRING = "Duplikat";
    /*----------Ucenik.java---------------------------------------------------*/
    public static String UCENIK_SETRAZREDI_NFEX_MSG_STRING = "Podešavanje validnih razreda "
            + "neuspelo zbog lošeg Stringa.\nBiće korišćene default vrednosti";
    public static String UCENIK_SETRAZREDI_NFEX_TITLE_STRING = "Greška pri inicijalizaciji";
    /*----------Init.java-----------------------------------------------------*/
    public static String HANDLER_MSG1_STRING = "Došlo je do neočekivane greške. Detalji:\n";
    public static String HANDLER_MSG2_STRING = "\novi podaci su sačuvani u log.";
    public static String HANDLER_TITLE_STRING = "Nepoznata greška";
    public static String EXIT_IOEX_MSG_STRING = "Došlo je do greške pri čuvanju na disk. "
            + "Podaci nisu sačuvani u celosti.\nPogledajte log za više informacija. Zatvoriti?";
    public static String EXIT_IOEX_TITLE_STRING = "I/O Greška";
    public static String EXIT_TWBL_MSG_STRING = "Došlo je do nepoznate greške pri čuvanju podataka."
            + "\n Pogledajte log za više informacija. Zatvoriti?";
    public static String EXIT_TWBL_TITLE_STRING = "Nepoznata greška";
    /*----------Logger.java---------------------------------------------------*/
    public static String LOGGER_IOEX_MSG_STRING = "I/O greška pri inicijalizaciji loggera.\n"
            + "Neke funkcije logovanja neće raditi";
    public static String LOGGER_IOEX_TITLE_STRING = "I/O Greška";
    public static String LOGGER_SECEX_MSG_STRING = "Sigurnosna greška pri inicijalizaciji loggera.\n"
            + "Neke funkcije logovanja neće raditi";
    public static String LOGGER_SECEX_TITLE_STRING = "Sigurnosna greška";
    /*----------funkcije.Unos.java--------------------------------------------*/
    public static String FINALIZE_IOEX_MSG_STRING = "Došlo je do greške pri čuvanju podataka";
    public static String FINALIZE_IOEX_TITLE_STRING = "I/O greška pri unosu";
    /*----------Dijalozi.java-------------------------------------------------*/
    static int DIJALOZI_LINE_HEIGHT = Grafika.getLabelFont().getSize()+6;
    static int DIJALOZI_SIRINA = 350;
    static int DIJALOZI_FIXED_VISINA = 110;
    static int DIJALOZI_TEXT_X = 10;
    static int DIJALOZI_LABEL_Y = 10;
    static int DIJALOZI_LABEL_WIDTH = 330;
    static int DIJALOZI_TEXTFIELD_FIXED_Y = 30;
    static int DIJALOZI_TEXTFIELD_WIDTH = 300;
    static int DIJALOZI_TEXTFIELD_HEIGHT = 25;
    static int DIJALOZI_INFOWINDOW_WIDTH = 250;
    static int DIJALOZI_INFOWINDOW_HEGHT = 50;
    static String DIJALOZI_VISERAZREDA_MSG_STRING = "Postoji vise učenika sa tim imenom.\n"
            + "Odaberite razred:";
    static String DIJALOZI_VISERAZREDA_TITLE_STRING = "Odaberite razred";
    static String DIJALOZI_VISEKNJIGA_MSG1_STRING = "Postoji vise knjiga sa imenom ";
    static String DIJALOZI_VISEKNJIGA_MSG2_STRING = "\nOdaberite autora:";
    static String DIJALOZI_VISEKNJIGA_TITLE_STRING = "Odaberite autora";
    static String DIJALOZI_BROJKNJIGA_TITLE_STRING = "Broj knjiga";
    static String DIJALOZI_BROJKNJIGA_MSG_STRING = "Unesite maksimalan broj "
            + "knjiga koje\nučenik može da ima kod sebe";
    static String DIJALOZI_BROJKNJIGA_NFEX_MSG_STRING = "Uneta količina nije broj.";
    static String DIJALOZI_BROJKNJIGA_NFEX_TITLE_STRING = "Loš format";
    /*----------IndexedCheckbox.java------------------------------------------*/
    static int CHECKBOX_TOP_INSET = 5;
    static int CHECKBOX_LEFT_INSET = 5;
    static int CHECKBOX_RIGHT_INSET = 5;
    static int CHECKBOX_BOTTOM_INSET = 5;
    /*----------Knjige.java---------------------------------------------------*/
    static int KNJIGE_SIRINA = 550;
    static int KNJIGE_VISINA = 750;
    static int KNJIGE_PANELS_ALIGNMENT_Y = 0;
    static int KNJIGE_SIDEPAN_WIDTH = 150;
    static int KNJIGE_SIDEPAN_UCENIK_HEIGHT = 26;
    static int KNJIGE_SCROLL_INCREMENT = 16;
    static int KNJIGE_DIVIDER_LOCATION = 90;
    static int KNJIGE_BUTTON_HEIGHT = 35;
    static int KNJIGE_NOVI_WIDTH = 140;
    static int KNJIGE_OBRISI_WIDTH = 130;
    static int KNJIGE_UCSEARCH_WIDTH = 150;
    static int KNJIGE_SEARCHBOX_X = 0;
    static int KNJIGE_SEARCHBOX_Y = 0;
    static int KNJIGE_SEARCHBOX_WIDTH = 135;
    static int KNJIGE_SEARCHBOX_HEIGHT = 30;
    static String KNJIGE_SEARCH_STRING = "Pretraži knjige...";
    static String KNJIGE_TITLE_STRING = "Pregled knjiga";
    static String KNJIGE_NASLOVI_STRING = "Naslovi:";
    static String KNJIGE_KOLICINA_STRING = "Količina:";
    static String KNJIGE_PISAC_STRING = "Pisac:";
    static String KNJIGE_NOVI_STRING = "Ubaci novi naslov";
    static String KNJIGE_OBRISI_STRING = "Obriši naslov";
    static String KNJIGE_UCSEARCH_STRING = "Kod koga je naslov...";
    static String KNJIGE_PKEX_MSG1_STRING = "Kod nekog učenika se nalazi knjiga ";
    static String KNJIGE_PKEX_MSG2_STRING = ".\nKada vrati knjigu, pokušajte ponovo.";
    static String KNJIGE_PKEX_TITLE_STRING = "Zauzeta knjiga";
    static String KNJIGE_BRISANJE_DIJALOG_MSG_STRING = "Unesite naslov koji želite da obrišete i pritisnite enter:";
    static String KNJIGE_BRISANJE_DIJALOG_TITLE_STRING = "Brisanje naslova";
    static String KNJIGE_BRISANJE_VNPEX_MSG_STRING = "Naslov koji ste uneli ne postoji.\n"
            + "Proverite unos i pokušajte ponovo";
    static String KNJIGE_BRISANJE_VNPEX_TITLE_STRING = "Greška pri brisanju naslova";
    static String KNJIGE_BRISANJE_PKEX_MSG_STRING = "Kod nekog učenika "
            + " se nalazi ova knjiga\nKada vrati knjigu, pokušajte ponovo.";
    static String KNJIGE_BRISANJE_PKEX_TITLE_STRING = "Zauzeta knjiga";
    static String KNJIGE_UZMI_DIJALOG_TITLE_STRING = "Iznajmljivanje knjige";
    static String KNJIGE_UZMI_DIJALOG_MSG_STRING = "Unesite ime učenika koji iznajmljuje knjigu i pritisnite enter:";
    static String KNJIGE_UZMI_SUCC_MSG_STRING = "Učenik je uspešno iznajmio knjigu";
    static String KNJIGE_UZMI_SUCC_TITLE_STRING = "Uspeh!";
    static String KNJIGE_UZMI_EX_TITLE_STRING = "Greška pri iznajmljivanju";
    static String KNJIGE_UZMI_PKEX_MSG_STRING = "Kod učenika se trenutno nalazi previše knjiga";
    static String KNJIGE_UZMI_DEX_MSG_STRING = "Kod učenika se već nalazi knjiga tog naslova";
    static String KNJIGE_UZMI_NVKEX_MSG_STRING = "Nema više knjiga tog naslova";
    static String KNJIGE_UZMI_VNPEX_MSG_STRING = "Učenik nije pronađen.\nProverite unos i pokušajte ponovo";
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
    static String NOVINASLOV_TITLE_STRING = "Unos novog naslova";
    static String NOVINASLOV_NASLOV_STRING = "Unesite naslov nove knjige:";
    static String NOVINASLOV_PISAC_STRING = "Unesite pisca knjige:";
    static String NOVINASLOV_KOLICINA_STRING = "Unesite količinu:";
    static String NOVINASLOV_UNOS_STRING = "Unesi podatke";
    static String NOVINASLOV_SUCC_MSG_STRING = "Knjiga dodata!";
    static String NOVINASLOV_SUCC_TITLE_STRING = "Uspeh!";
    static String NOVINASLOV_VNPEX_MSG_STRING = "Polje za naslov je prazno.";
    static String NOVINASLOV_VNPEX_TITLE_STRING = "Prazno polje";
    static String NOVINASLOV_DEX_MSG_STRING = "Knjiga tog naslova već postoji";
    static String NOVINASLOV_DEX_TITLE_STRING = "Duplikat";
    static String NOVINASLOV_NFEX_MSG_STRING = "Uneta količina nije broj.";
    static String NOVINASLOV_NFEX_TITLE_STRING = "Loš unos";
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
    static String UCSEARCH_TITLE_STRING = "Pretraga učenika po naslovu";
    static String UCSEARCH_NASLOV_STRING = "Unesite naslov i pritisnite enter:";
    static String UCSEARCH_UCENICI_STRING = "<html>Učenici kod kojih je trenutno knjiga:<br>";
    static String UCSEARCH_DATUMI_STRING = "<html>Datum kada je knjiga iznajmljena:<br>";
    static String UCSEARCH_OK_STRING = "OK";
    static String UCSEARCH_VNPEX_MSG_STRING = "Knjiga nije pronađena.\nProverite unos i pokušajte ponovo";
    static String UCSEARCH_VNPEX_TITLE_STRING = "Knjiga ne postoji";
    static String UCSEARCH_PEX_MSG_STRING = "Trenutno se tražena knjiga ne nalazi ni kod koga.";
    static String UCSEARCH_PEX_TITLE_STRING = "Niko nije iznajmio knjigu";
    static String PROMENIKOL_TITLE_STRING = "Promeni količinu naslova";
    static String PROMENIKOL_MSG_STRING = "Unesite novu količinu i pritisnite enter.\nUnesite znakove + ili -"
            + "ako želite da izvršite\npromenu u odnosu na sadašnji broj knjiga:";
    static String PROMENIKOL_NFEX_MSG_STRING = "Uneta količina nije broj.";
    static String PROMENIKOL_NFEX_TITLE_STRING = "Greška pri unosu";
    static String PROMENIKOL_NVKEX_MSG_STRING = "Nema dovoljno knjiga za traženu promenu.\nProverite knjige"
            + "koje se nalaze kod učenika, pa pokušajte ponovo";
    static String PROMENIKOL_NVKEX_TITLE_STRING = "Nema dovoljno knjiga";
    /*----------Podesavanja.java----------------------------------------------*/
    static int PODESAVANJA_WIDTH = 600;
    static int PODESAVANJA_FIXED_HEIGHT = 100;
    static int PODESAVANJA_HEIGHT_PER_LABEL = 40;
    static int PODESAVANJA_LABEL_X = 20;
    static int PODESAVANJA_LABEL_FIXED_Y = 20;
    static int PODESAVANJA_LABEL_WIDTH = 480;
    static int PODESAVANJA_LABEL_HEIGHT = 21;
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
    static String PODESAVANJA_TITLE_STRING = "Podešavanja";
    static String PODESAVANJA_SUCC_MSG_STRING = "Podešavanja su sačuvana i postaće aktivna"
            + "\n pri sledećem pokretanju programa.";
    static String PODESAVANJA_SUCC_TITLE_STRING = "Podešavanja sačuvana";
    static String PODESAVANJA_PKEX_MSG_STRING = "Kod nekih učenika se nalazi više knjiga nego što"
            + "podešavanje\nkoje ste postavili dozvoljava.Vratite knjige i pokušajte ponovo";
    static String PODESAVANJA_PKEX_TITLE_STRING = "Previše knjiga";
    static String PODESAVANJA_NFEX_MSG_STRING = "Neka od unetih vrednosti nije broj."
            + "\nProverite unos i pokušajte ponovo";
    static String PODESAVANJA_NFEX_TITLE_STRING = "Loš unos";
    static String PODESAVANJA_FNFEX_MSG_STRING = "Na datoj putanji radnog direktorijuma nije napravljen "
            + "novi folder.\nProverite da li je putanja ispravna i da li postoje "
            + "odgovarajuće dozvole i pokušajte ponovo.";
    static String PODESAVANJA_FNFEX_TITLE_STRING = "I/O greška";
    static String PODESAVANJA_LFEX_MSG_STRING = "Neki učenici pohađaju razred koji nije određen kao validan\n"
            + "Proverite vrednosti i pokušajte ponovo.";
    static String PODESAVANJA_LFEX_TITLE_STRING = "Loš razred";
    static String PODESAVANJA_IAEX_MSG_STRING = "Neka od unetih vrednosti nije validna. "
            + "Proverite sve i pokušajte ponovo";
    static String PODESAVANJA_IAEX_TITLE_STRING = "Loša vrednost";
    static String PODESAVANJA_BGBOJA_STRING = "Pozadinska boja";
    static String PODESAVANJA_FGBOJA_STRING = "Boja fonta";
    static String PODESAVANJA_TFBOJA_STRING = "Boja polja za unos";
    static String PODESAVANJA_PROMENIBOJU_TITLE_STRING = "Promeni boju komponenti";
    /*----------Ucenici.java--------------------------------------------------*/
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
    static int UCENICI_ICON_WIDTH = 35;
    static int UCENICI_SEARCHBOX_X = 0;
    static int UCENICI_SEARCHBOX_Y = 0;
    static int UCENICI_SEARCHBOX_WIDTH = 150;
    static int UCENICI_SEARCHBOX_HEIGHT = 27;
    static String UCENICI_SEARCH_STRING = "Pretraži učenike...";
    static String UCENICI_TITLE_STRING = "Pregled učenika";
    static String UCENICI_UCENICI_STRING = "<html>Učenici:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
            + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
            + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br></html>";
    static String UCENICI_KNJIGE_STRING = "<html>Knjige:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
            + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br></html>";
    static String UCENICI_NOVIUC_STRING = "Dodati novog učenika";
    static String UCENICI_DELUC_STRING = "Obrisati učenika";
    static String UCENICI_NOVAGEN_STRING = "Dodati novu generaciju";
    static String UCENICI_PREGLEDKNJ_TOOLTIP_STRING = "Pregled knjiga";
    static String UCENICI_PREGLEDKNJ_STRING = "Knjige";
    static String UCENICI_SAVE_TOOLTIP_STRING = "Sačuvaj podatke";
    static String UCENICI_SAVE_STRING = "Sačuvaj";
    static String UCENICI_IOEX_MSG_STRING = "Došlo je do greške pri čuvanju fajlova";
    static String UCENICI_IOEX_TITLE_STRING = "I/O Greška";
    static String UCENICI_PODESAVANJA_STRING = "Podešavanja";
    static String UCENICI_PKEX_MSG_STRING = "Kod učenika se nalaze neke knjige.\n"
            + "Kada ih vrati, pokušajte ponovo";
    static String UCENICI_PKEX_TITLE_STRING = "Greška pri brisanju";
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
    static String DODAJUCENIKA_TITLE_STRING = "Dodavanje novog učenika";
    static String DODAJUCENIKA_IME_STRING = "Unesite ime učenika:";
    static String DODAJUCENIKA_RAZRED_STRING = "Unesite razred koji učenik trenutno pohađa:";
    static String DODAJUCENIKA_UNESI_STRING = "Unesi podatke";
    static String DODAJUCENIKA_SUCC_MSG_STRING = "Učenik dodat!";
    static String DODAJUCENIKA_SUCC_TITLE_STRING = "Uspeh!";
    static String DODAJUCENIKA_NFEX_MSG_STRING = "Razred učenika je prevelik.";
    static String DODAJUCENIKA_NFEX_TITLE_STRING = "Loš razred";
    static String DODAJUCENIKA_DEX_MSG_STRING = "Već postoji učenik koji ide u isti razred sa istim imenom i "
            + "prezimenom.\nNovi učenik ne može biti dodat";
    static String DODAJUCENIKA_DEX_TITLE_STRING = "Duplikat";
    static String OBRISIUCENIKA_TITLE_STRING = "Brisanje učenika";
    static String OBRISIUCENIKA_MSG_STRING = "Unesite ime učenika i pritisnite enter:";
    static String OBRISIUCENIKA_EMPTY_MSG_STRING = "Učenik nije pronađen\nProverite unos  i pokušajte ponovo.";
    static String OBRISIUCENIKA_EMPTY_TITLE_STRING = "Učenik ne postoji";
    static String OBRISIUCENIKA_SUCC_MSG_STRING = "Učenik obrisan!";
    static String OBRISIUCENIKA_SUCC_TITLE_STRING = "Uspeh!";
    static String OBRISIUCENIKA_PKEX_MSG_STRING = "Učenik ima preostalih knjiga.\nKada vrati, pokusajte ponovo.";
    static String OBRISIUCENIKA_PKEX_TITLE_STRING = "Preostale knjige";
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
    static String DODAJGENERACIJU_TITLE_STRING = "Unos nove generacije.";
    static String DODAJGENERACIJU_IMENA_STRING = "<html>Unesite učenike nove generacije, odvojene zapetama <br />"
            + "<strong>Nakon dodavanja nove generacije, svi učenici "
            + "najstarije generacije će biti obrisani!!!</strong></html>";
    static String DODAJGENERACIJU_UNESI_STRING = "Unesi novu generaciju";
    static String DODAJGENERACIJU_SUCC_MSG_STRING = "Nova generacija dodata.";
    static String DODAJGENERACIJU_SUCC_TITLE_STRING = "Uspeh!";
    /*----------Unos.java-----------------------------------------------------*/
    static int UNOS_WIDTH = 320;
    static int UNOS_HEIGHT = 110;
    static int UNOS_HGAP = 20;
    static int UNOS_VGAP = 20;
    static int UNOS_BUTTON_WIDTH = 130;
    static int UNOS_BUTTON_HEIGHT = 40;
    static String UNOS_TITLE_STRING = "Unos";
    static String UNOS_UCENICI_STRING = "Unos učenika";
    static String UNOS_KNJIGE_STRING = "Unos knjiga";
    static String UNOS_IOEX_MSG_STRING = "Došlo je do greške pri pisanju na disk.";
    static String UNOS_IOEX_TITLE_STRING = "I/O greška pri unosu";
    static int UNOSKNJ_WIDTH = 400;
    static int UNOSKNJ_HEIGHT = 340;
    static int UNOSKNJ_TEXT_X = 20;
    static int UNOSKNJ_LABEL_WIDTH = 300;
    static int UNOSKNJ_LABEL_HEIGHT = 20;
    static int UNOSKNJ_TEXTFIELD_WIDTH = 300;
    static int UNOSKNJ_TEXTFIELD_HEIGHT = 25;
    static int UNOSKNJ_NASLOV_Y = 20;
    static int UNOSKNJ_NASLOVTF_Y = 55;
    static int UNOSKNJ_PISAC_Y = 95;
    static int UNOSKNJ_PISACTF_Y = 125;
    static int UNOSKNJ_KOLICINA_Y = 165;
    static int UNOSKNJ_KOLICINATF_Y = 195;
    static int UNOSKNJ_UNESI_X = 130;
    static int UNOSKNJ_UNESI_Y = 240;
    static int UNOSKNJ_UNESI_WIDTH = 120;
    static int UNOSKNJ_UNESI_HEIGHT = 40;
    static String UNOSKNJ_NASLOV_STRING = "Unesite naslov knjige";
    static String UNOSKNJ_PISAC_STRING = "Unesite pisca knjige:";
    static String UNOSKNJ_KOLICINA_STRING = "Unesite količinu:";
    static String UNOSKNJ_UNESI_STRING = "Unesi podatke";
    static String UNOSKNJ_PRAZNO_MSG_STRING = "Polje za količinu je prazno.";
    static String UNOSKNJ_PRAZNO_TITLE_STRING = "Prazno polje";
    static String UNOSKNJ_NFEX_MSG_STRING = "Uneta količina nije broj.";
    static String UNOSKNJ_NFEX_TITLE_STRING = "Loš unos";
    static String UNOSKNJ_DEX_MSG_STRING = "Naslov već postoji.";
    static String UNOSKNJ_DEX_TITLE_STRING = "Dupli unos";
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
    static String UNOSUC_IME_STRING = "Unesite ime učenika:";
    static String UNOSUC_RAZRED_STRING = "Unesite razred u koji učenik ide (brojevima):";
    static String UNOSUC_KNJIGE_STRING = "<html>Unesite knjige koje se trenutno nalaze"
            + " kod učenika, razdvojene zapetom:</html>";
    static String UNOSUC_UNESI_STRING = "Unesi podatke";
    static String UNOSUC_NFEX_MSG_STRING = "Unet razred nije broj ili nije validan";
    static String UNOSUC_NFEX_TITLE_STRING = "Loš razred";
    static String UNOSUC_PKEX_MSG_STRING = "Uneli ste više knjiga od limita koji ste postavili na početku"
            + "\nUčenik nije unesen";
    static String UNOSUC_PKEX_TITLE_STRING = "Previše knjiga";
    static String UNOSUC_DEX_MSG_STRING = "Učenik već postoji.";
    static String UNOSUC_DEX_TITLE_STRING = "Dupli unos";
    static String UNOSUC_VNPEX_MSG_STRING = "Jedna od knjiga nije prethodno uneta. Možete koristiti\t"
            + "\"Ubaci novi naslov\" iz pregleda knjiga da je naknadno dodate.";
    static String UNOSUC_VNPEX_TITLE_STRING = "Knjiga ne postoji";
    /*----------Uzimanje.java-------------------------------------------------*/
    static String UZIMANJE_TITLE_STRING = "Iznajmljivanje knjige";
    static String UZIMANJE_MSG_STRING = "Unesite naslov knjige koju učenik iznajmljuje:";
    static String UZIMANJE_VNPEX_MSG_STRING = "Naslov koji ste uneli ne postoji \n"
            + "Proverite da li ste ispravno upisali naziv i pokušajte ponovo.";
    static String UZIMANJE_VNPEX_TITLE_STRING = "Nepostojeća knjiga";
    static String UZIMANJE_NVKEX_MSG_STRING = "Više nema knjiga tog naslova";
    static String UZIMANJE_NVKEX_TITLE_STRING = "Nema knjiga";
    static String UZIMANJE_DEX_MSG_STRING = "Učenik je vec iznajmio knjigu tog naslova.";
    static String UZIMANJE_DEX_TITLE_STRING = "Duplikat";
    static String UZIMANJE_PKEX_MSG_STRING = "Učenik trenutno ima previše knjiga kod sebe: \n";
    static String UZIMANJE_PKEX_TITLE_STRING = "Previše knjiga";
    /*----------UzmiVratiButton.java------------------------------------------*/
    static int SMALLBUT_WIDTH = 140;
    static int SMALLBUT_HEIGHT = 23;
    static int SMALLBUT_X = 0;
    static String SMALLBUT_UZMI_STRING = "Iznajmi knjigu";
    static String SMALLBUT_VRATI_STRING = "Vrati knjigu";
    static String SMALLBUT_SETKOL_STRING = "Promeni količinu";
}
