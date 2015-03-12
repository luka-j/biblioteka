package rs.luka.biblioteka.data;

import java.util.ArrayList;
import java.util.Comparator;
import rs.luka.biblioteka.exceptions.NotUnique;

/**
 * Lista koja ne dozvoljava duplikate. Koristi ArrayList kao osnovu. Overriduje
 * {@link ArrayList#add(java.lang.Object)}, {@link ArrayList#add(int, java.lang.Object)} i
 * {@link ArrayList#set(int, java.lang.Object)}, tako da ukljuce proveru jedinstvenosti
 * elementa koji se postavlja ili dodaje na datu poziciju.
 * 
 * @author luka
 * @param <E> klasa elemenata u listi
 * @see ArrayList
 * @since 3.1.'15.
 */
public class UniqueList<E> extends ArrayList<E> {
    private int prevModCount;
    
    /**
     * {@inheritDoc}
     * @return true ako je sve OK, false ako je doslo do greske (ili element vec postoji)
     */
    @Override
    public boolean add(E e) {
        if(isUnique(e)) {
            super.add(e);
            return true;
        }
        return false;
    }
    
    /**
     * {@inheritDoc}
     * @throws NotUnique ako element vec postoji u listi
     */
    @Override
    public void add(int index, E element) {
        if(isUnique(element)) {
            super.add(index, element);
        }
        else 
            throw new NotUnique("Element " + element + " nije jedinstven");
    }
    
    /**
     * {@inheritDoc}
     * @throws NotUnique ako element vec postoji u listi
     */
    @Override
    public E set(int index, E element) {
        if(isUnique(element)) {
            return super.set(index, element);
        }
        else 
            throw new NotUnique("Element " + element + " nije jedinstven.");
    }
    
    /**
     * Proverava da li je lista sortirana od poslednje modifikacije (gleda modCount).
     * Ako nije, vraca se bez izmena, u suprotnom radi sortiranje.
     * 
     * {@inheritDoc}
     */
    @Override
    public void sort(Comparator<? super E> c) {
        if(prevModCount == modCount)
            return;
        else 
            super.sort(c);
        prevModCount = modCount;
    }
    
    /**
     * Proverava da li ova lista vec sadrzi dati element.
     * @param e element koji se proverava
     * @return ako sadrzi, false (oznacava da nije jedinstven), u suprotnom true (jedinstven je)
     */
    private boolean isUnique(E e) {
        return !this.contains(e);
    }
}
