package mining;
import java.io.Serializable;

import data.Data;
import data.OutOfRangeSampleSize;
import data.Tuple;

/**
 * Classe ClusterSet che si occupa di gestire un insieme di cluster.
 * Gli oggetti di questa classe sono costituiti da un insieme di cluster.
 * Implementa i metodi per l'inizializzazione dei centroidi, il calcolo del cluster più vicino.
 */
public class ClusterSet implements Serializable {
    /**
     * Array di cluster.
     */
    private final Cluster[] C;
    /**
     * posizione valida per la memorizzazione di un nuovo cluster in C.
     */
    private int i = 0;
    /**
     * Costruttore della classe ClusterSet.
     * @param k numero di cluster
     * @throws OutOfRangeSampleSize
     */
    ClusterSet(int k) throws OutOfRangeSampleSize {
        try {
            this.C = new Cluster[k];
        } catch (NegativeArraySizeException e) {
            throw new OutOfRangeSampleSize("Il numero di cluster deve essere maggiore di 0");
        }
    }
    /**
     * Assegna c a C[i] e incremente i.
     * @param c cluster
     */
    private void add(Cluster c) {
        this.C[i] = c;
        this.i++;
    }
    /**
     * restituisce C[i].
     * @param i indice del cluster
     * @return cluster
     */
    protected Cluster get(int i) {
        return this.C[i];
    }
    /**
     * Sceglie i centroidi, crea un cluster per ogni centroide e lo memorizza in C.
     * @param data dataset
     * @throws OutOfRangeSampleSize eccezione per dimensione campione fuori range
     */
    protected void initializeCentroids(Data data) throws OutOfRangeSampleSize {
        int centroidIndexes[] = data.sampling(C.length);
        for (int centroide : centroidIndexes) {
            Tuple centroidI = data.getItemSet(centroide);
            add(new Cluster(centroidI));
        }
    }
    /**
     * Calcola la distanza tra la tupla riferita da tuple,.
     * ed il centroide di ciascun cluster in C e restituisce il cluster più vicino.
     * @param tuple tupla
     * @return cluster
     */
    protected Cluster nearestCluster(Tuple tuple) {
        Cluster nearestCluster = C[0];
        double minDistance = tuple.getDistance(C[0].getCentroid());
        double distance;
        for (int i = 1; i < C.length; i++) {
            distance = tuple.getDistance(C[i].getCentroid());
            if (distance < minDistance) {
                minDistance = distance;
                nearestCluster = C[i];
            }
        }
        return nearestCluster;
    }
    /**
     * Identifica e restituisce il cluster a cui la tupla rappresentante l'esempio.
     * identificato da id.
     * Se la tupla non è inclusa in nessun cluster restituisce null.
     * @param id indice della tupla
     * @return cluster
     */
    protected Cluster currentCluster(int id) {
        for (Cluster c : C) {
            if (c.contain(id))
                return c;
        }
        return null;
    }
    /**
     * Calcola il nuovo centroide per ciascun cluster in C.
     * @param data dataset
     */
    protected void updateCentroids(Data data) {
        for (Cluster c : C) {
            c.computeCentroid(data);
        }
    }
    /**
     * Restituisce una stringa fatta da ciascun centroide dell'insieme dei cluster.
     * @return s
     */
    public String toString() {
        String s = "";
        for (int i = 0; i < C.length; i++) {
            s += i + ":" + C[i].toString() + "\n";
        }
        return s;
    }
    /**
     * Restituisce una stringa che descriva lo stato di ciascun cluster in C.
     * @param data dataset
     * @return str
     */
    public String toString(Data data) {
        String str = "";
        for (int i = 0; i < C.length; i++) {
            if (C[i] != null) {
                str += i + ":" + C[i].toString(data) + "\n";
            }
        }
        return str;
    }
}
