package data;

import java.io.Serializable;
import java.util.Set;
/**
 * Classe Tuple che modella una tupla.
 * Una tupla è un array di Item.
 * Implementa l'interfaccia Serializable.
 */
public class Tuple implements Serializable {
    /**
     * Array di Item.
     */
    private Item[] tuple;
    /**
     * Costruttore della classe Tuple.
     * @param size è un intero
     */
    public Tuple(int size) {
        this.tuple = new Item[size];
    }
    /**
     * Restituisce la dimensione della tupla.
     * @return tuple.length
     */
    public int getLength() {
        return this.tuple.length;
    }
    /**
     * Restituisce l'Item in posizione i.
     * @param i è un intero
     * @return tuple[i]
     */
    public Item get(int i) {
        return this.tuple[i];
    }
    /**
     * Memorizza l'Item c in posizione i.
     * @param c è un Item
     * @param i è un intero
     */
    protected void add(Item c, int i) {
        this.tuple[i] = c;
    }
    /**
     * Determina la distanza tra la tupla riferita da obj e la tupla corrente.
     * @param obj è una tupla
     * @return distance
     */
    public double getDistance(Tuple obj) {
        double distance = 0;
        for (int i = 0; i < this.getLength(); i++)
            distance += this.get(i).distance(obj.get(i).getValue());
        return distance;
    }
    /**
     * Restituisce la media delle distanze tra la tupla corrente e i dati del
     * cluster.
     * @param data è un oggetto Data
     * @param clusteredData è un insieme di interi
     * @return p
     */
    public double avgDistance(Data data, Set<Integer> clusteredData) {
        double p = 0.0, sumD = 0.0;
        for (int i : clusteredData) {
            double d = getDistance(data.getItemSet(i));
            sumD += d;
        }
        p = sumD / clusteredData.size();
        return p;
    }
}
