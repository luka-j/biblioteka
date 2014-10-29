package rs.luka.legacy.biblioteka;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import static java.lang.String.valueOf;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;
import rs.luka.biblioteka.data.Knjiga;
import rs.luka.biblioteka.data.Podaci;
import static rs.luka.biblioteka.data.Podaci.getBrojKnjiga;
import static rs.luka.biblioteka.data.Podaci.getBrojUcenika;
import static rs.luka.biblioteka.data.Podaci.getMaxBrojUcenikKnjiga;
import static rs.luka.biblioteka.data.Podaci.getUcenik;
import rs.luka.biblioteka.data.Ucenik;
import rs.luka.biblioteka.funkcije.Utils;

/**
 *
 * @author luka
 * @since pocetak
 */
public class SaveLegacy {
    
    private static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(SaveLegacy.class.getName());

    /**
     * @deprecated nicemu ne sluzi(ostalo od mojih pokusaja pre); buffer za
     * zip/unzip
     */
    private static final int BUFFER = 2_048;

    /**
     * Naslov/Kolicina/, Ucenik/razred/knjige...
     *
     * @throws IOException
     */
    public void save() throws IOException {
        File data = new File("Data");
        if (!data.isDirectory()) {
            data.mkdir();
        }
        saveUcenike();
        saveKnjige();
    }

    public void saveUcenike() throws IOException {
        LOGGER.log(Level.FINE, "Počinjem čuvanje učenika");
        File ucenici = new File(Utils.getWorkingDir() + "Data" + File.separator + "Ucenici.dat");
        if (getBrojUcenika() == 0) {
            LOGGER.log(Level.FINE, "Lista sa učenicima je prazna. Preskačem čuvanje podataka.");
            return;
        }
        StringBuilder write;
        ucenici.createNewFile();
        try (BufferedWriter fwU = new BufferedWriter(new FileWriter(ucenici))) {
            Ucenik uc;
            for (int i = 0; i < getBrojUcenika(); i++) {
                uc = getUcenik(i);
                write = new StringBuilder(uc.getIme());
                write.append("/");
                for (int j = 0; j < getMaxBrojUcenikKnjiga(); j++) {
                    write.append(uc.getNaslovKnjige(j)).append("/");
                }
                write.append(valueOf(uc.getRazred())).append("/");
                for (int j = 0; j < getMaxBrojUcenikKnjiga(); j++) {
                    write.append(uc.getDatumKnjige(j)).append("/");
                }
                write.append("\n");
                fwU.write(write.toString());
            }
        }
        LOGGER.log(Level.INFO, "Sačuvao učenike");
    }

    public void saveKnjige() throws IOException {
        LOGGER.log(Level.FINE, "Počinjem čuvanje knjiga");
        File knjige = new File(Utils.getWorkingDir() + "Data" + File.separator + "Knjige.dat");
        if (getBrojKnjiga() == 0) {
            LOGGER.log(Level.FINE, "Lista sa knjigama je prazna. Preskačem čuvanje knjiga.");
            return;
        }
        knjige.createNewFile();
        try (FileWriter fwN = new FileWriter(knjige); BufferedWriter bwN = new BufferedWriter(fwN)) {
            Knjiga knjiga;
            for (int i = 0; i < getBrojKnjiga(); i++) {
                knjiga = Podaci.getKnjiga(i);
                bwN.write(knjiga.getNaslov() + "/" + knjiga.getKolicina() + "/" + "\n");
            }
        }
        LOGGER.log(Level.INFO, "Sačuvao knjige");
    }

    /**
     * @deprecated ne sluzi nicemu trenutno, niti je potrebna(ostalo od pre)
     * Data.list() ne radi.
     */
    private void zip() {
        try {
            File zip = new File("data.zip");
            if (zip.exists()) {
                zip.delete();
            }
            zip.createNewFile();
            BufferedInputStream bis = null;
            FileOutputStream dest = new FileOutputStream(zip);
            try (ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest))) {
                byte data[] = new byte[BUFFER];
                File Data = new File("Data");
                String files[] = Data.list();
                for (String file : files) {
                    FileInputStream fi = new FileInputStream(file);
                    bis = new BufferedInputStream(fi, BUFFER);
                    ZipEntry entry = new ZipEntry(file);
                    out.putNextEntry(entry);
                    int count;
                    while ((count = bis.read(data, 0,
                            BUFFER)) != -1) {
                        out.write(data, 0, count);
                    }
                    bis.close();
                }
            }
        } catch (IOException ex) {
        }
    }

    /**
     * @deprecated ne sluzi nicemu trenutno(ostalo od mojih prethodnih pokusaja)
     * unZippuje .zip sa podacima, ali {@link #zip() zip metoda} ne radi
     */
    private void unZip() {
        try {
            BufferedOutputStream dest = null;
            BufferedInputStream is = null;
            ZipEntry entry;
            File zipf = new File("data.zip");
            if (zipf.exists()) {
                if (zipf.length() != 0) {
                    ZipFile zipfile = new ZipFile(zipf);
                    Enumeration e = zipfile.entries();
                    while (e.hasMoreElements()) {
                        entry = (ZipEntry) e.nextElement();
                        is = new BufferedInputStream(zipfile.getInputStream(entry));
                        int count;
                        byte data[] = new byte[BUFFER];
                        FileOutputStream fos = new FileOutputStream(entry.getName());
                        dest = new BufferedOutputStream(fos, BUFFER);
                        while ((count = is.read(data, 0, BUFFER))
                                != -1) {
                            dest.write(data, 0, count);
                        }
                        dest.close();
                        is.close();
                    }
                }
            }
        } catch (IOException ex) {
        }
    }
}
