package rs.luka.biblioteka.data;

/**
 * Opisuje neku akciju u programu. Koriscen za undo.
 * @author luka
 * @since 27.9.'14.
 */
public enum Akcija {
    /**
     * Oznacava uzimanje neke knjige. Zahteva 2 argumenta (Ucenik, Knjiga).
     */
    UZIMANJE,
    /**
     * Oznacava vracanje neke knjige. Zahteva 2 argumenta (Ucenik, Knjiga).
     */
    VRACANJE,
    /**
     * Oznacava dodavanje novog ucenika. Zahteva jedan argument (Ucenik)
     */
    DODAVANJE_UCENIKA,
    /**
     * Oznacava brisanje ucenika. Zahteva jedan argument (Ucenik).
     */
    BRISANJE_UCENIKA,
    /**
     * Oznacava dodavanje novog naslova. Zahteva jedan argument(Knjiga).
     */
    DODAVANJE_KNJIGE ,
    /**
     * Oznacava brisanje naslova. Zahteva jedan argument(Knjiga).
     */
    BRISANJE_KNJIGE;
    
    /**
     * Proverava da li je ova akcija akcija brisanja. Ovo vazi za brisanje knjiga i ucenika i uzimanje knjige.
     * @return true ako jeste, false ako nije
     */
    public boolean isBrisanje() {
        return this.name().contains("BRISANJE") || this.equals(Akcija.UZIMANJE);
    }
    /**
     * Proverava da li se ova akcija odnosi na knjigu. Ovo vazi za brisanje i dodavanje knjige.
     * @return true ako se odnosi, false u suprotnom
     */
    public boolean isKnjiga() {
        return this.name().contains("KNJIGE");
    }
    /**
     * Proverava da li se ova akcija odnosi na ucenika. Ovo vazi za brisanje i dodavanje ucenika.
     * @return true ako se odnosi, false u suprotnom
     */
    public boolean isUcenik() {
        return this.name().contains("UCENIKA");
    }
}
