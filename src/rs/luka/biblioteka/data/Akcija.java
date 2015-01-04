package rs.luka.biblioteka.data;

/**
 * Opisuje neku akciju u programu. Korišćen za undo.
 * @author luka
 * @since 27.9.'14.
 */
public enum Akcija {
    /**
     * Označava uzimanje neke knjige. Zahteva 2 argumenta (Ucenik, Knjiga).
     */
    UZIMANJE,
    /**
     * Označava vraćanje neke knjige. Zahteva 2 argumenta (Ucenik, Knjiga).
     */
    VRACANJE,
    /**
     * Označava dodavanje novog učenika. Zahteva jedan argument (Ucenik)
     */
    DODAVANJE_UCENIKA,
    /**
     * Označava brisanje učenika. Zahteva jedan argument (Ucenik).
     */
    BRISANJE_UCENIKA,
    /**
     * Označava dodavanje novog naslova. Zahteva jedan argument(Knjiga).
     */
    DODAVANJE_KNJIGE,
    /**
     * Označava brisanje naslova. Zahteva jedan argument(Knjiga).
     */
    BRISANJE_KNJIGE;
    
    /**
     * Proverava da li je ova akcija akcija brisanja. Ovo važi za brisanje knjiga i učenika i uzimanje knjige.
     * @return true ako jeste, false ako nije
     */
    public boolean isBrisanje() {
        return this.name().contains("BRISANJE") || this.equals(Akcija.UZIMANJE);
    }
    /**
     * Proverava da li se ova akcija odnosi na knjigu. Ovo važi za brisanje i dodavanje knjige.
     * @return true ako se odnosi, false u suprotnom
     */
    public boolean isKnjiga() {
        return this.name().contains("KNJIGE");
    }
    /**
     * Proverava da li se ova akcija odnosi na ucenika. Ovo vazi za brisanje i dodavanje učenika.
     * @return true ako se odnosi, false u suprotnom
     */
    public boolean isUcenik() {
        return this.name().contains("UCENIKA");
    }
}
