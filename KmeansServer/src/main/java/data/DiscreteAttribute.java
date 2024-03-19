package data;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

/**
 * Classe che rappresenta un attributo discreto.
 * Estende la classe astratta Attribute.
 * Implementa l'interfaccia Comparable e Iterable.
 */
public class DiscreteAttribute extends Attribute implements Comparable<DiscreteAttribute>, Iterable<String>{

    /**
     * Array di stringhe che rappresentano i valori discreti che l'attributo può assumere.
     */
    private final TreeSet<String> values;
    /**
     * Costruttore della classe DiscreteAttribute.
     * @param name nome simbolico dell'attributo
     * @param index identificativo dell'attributo
     * @param values array di stringhe che rappresentano i valori discreti che l'attributo può assumere
     */
    DiscreteAttribute(String name, int index, String[] values) {
        super(name, index);
        this.values = new TreeSet<>();
        this.values.addAll(Arrays.asList(values));
    }
    /**
     * Iteratore sui valori discreti dell'attributo.
     * @return values.iterator() è un iteratore sui valori discreti dell'attributo
     */
    public Iterator<String> iterator() {
        return this.values.iterator();
    }
    /**
     * Numero di valori discreti nel domino dell'attributo.
     * @return int è il numero di valori discreti nel domino dell'attributo
     */
    protected int getNumberOfDistinctValues() {
        return values.size();
    }
    /**
     * Metodo che confronta l'attributo corrente con un altro attributo discreto.
     * @param o attributo discreto
     * @return int
     */
    public int compareTo(DiscreteAttribute o) {
        return this.getName().compareTo(o.getName());
    }
    /**
     * Determina il numero di volte che il valore v compare.
     * In corrispondenza dell'attributo corrente (indice di colonna).
     * Negli esempi memorizzati in data e indicizzate (per riga) da idList.
     * @param data oggetto di tipo Data
     * @param idList insieme di indici di riga
     * @param v valore
     * @return count è il numero di volte che il valore v compare
     */
    protected int frequency(Data data, Set<Integer> idList, String v) {
        int count = 0;
        for (int i : idList) {
            if (data.getAttributeValue(i, this.getIndex()).equals(v)) {
                count++;
            }
        }
        return count;
    }
}
