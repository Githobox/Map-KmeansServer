package mining;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import data.Data;
import data.OutOfRangeSampleSize;

/**
 * Classe KmeansMiner che si occupa di eseguire l'algoritmo k-means.
 * Implementa l'interfaccia Serializable per poter salvare e caricare i dati.
 */
public class KmeansMiner implements Serializable {
    /**
     * ClusterSet C.
     * Contiene i cluster ottenuti dall'algoritmo k-means.
     */
    private final ClusterSet C;
    /**
     * Costruttore della classe KMeansMiner.
     * Inizializza C con k cluster.
     * @param k numero di cluster.
     * @throws OutOfRangeSampleSize eccezione per dimensione campione fuori range
     */
    public KmeansMiner(int k) throws OutOfRangeSampleSize {
        this.C = new ClusterSet(k);
    }
    /**
     * Costruttore della classe KMeansMiner.
     * Apre il file fileName e ne legge il contenuto e lo memorizza in C.
     * @param fileName nome del file.
     * @throws IOException eccezione per errore di I/O
     * @throws ClassNotFoundException eccezione per errore di cast
     */
    public KmeansMiner(String fileName) throws IOException, ClassNotFoundException {
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName));
            this.C = (ClusterSet) in.readObject();
            in.close();
        } catch (IOException e) {
            throw new IOException("[!] Errore di I/O");
        } catch (ClassNotFoundException e) {
            throw new ClassNotFoundException("[!] Errore di cast");
        }
    }
    /**
     * Restituisce C.
     * @return C.
     */
    public ClusterSet getC() {
        return this.C;
    }
    /**
     * Esegue l'algoritmo k-means eseguendo i passi dello pseudo-codice.
     * 1.Scelta casuale di centroidi per k cluster.
     * 2.Assegnazione di ciascuna riga della matrice in data al cluster avente
     * centroide più vicino all'esempio.
     * 3.Calcolo dei nuovi centroidi per ciascun cluster.
     * 4.Ripete i passi 2 e 3. finché due iterazioni consecutive non restituiscano
     * centroidi uguali.
     * @param data insieme di dati.
     * @return numero di iterazioni.
     * @throws OutOfRangeSampleSize eccezione per dimensione campione fuori range
     */
    public int kmeans(Data data) throws OutOfRangeSampleSize {
        int numberOfIterations = 0;
        // STEP 1
        C.initializeCentroids(data);
        boolean changedCluster;
        do {
            numberOfIterations++;
            // STEP 2
            changedCluster = false;
            for (int i = 0; i < data.getNumberOfExamples(); i++) {
                Cluster nearestCluster = C.nearestCluster(data.getItemSet(i));
                Cluster oldCluster = C.currentCluster(i);
                boolean currentChange = nearestCluster.addData(i);
                if (currentChange)
                    changedCluster = true;
                // rimuovo la tupla dal vecchio cluster
                if (currentChange && oldCluster != null)
                    // il nodo va rimosso dal suo vecchio cluster
                    oldCluster.removeTuple(i);
            }
            // STEP 3
            C.updateCentroids(data);
        } while (changedCluster);
        return numberOfIterations;
    }
    /**
     * Apre il file identificato da fileName e vi scrive il contenuto di C.
     * @param fileName nome del file.
     * @throws FileNotFoundException eccezione per file non trovato
     * @throws IOException eccezione per errore di I/O
     */
    public void salvaKmeansMiner(String fileName) throws FileNotFoundException,IOException {
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName));
            out.writeObject(this.C);
            out.close();
        } catch (IOException e) {
            throw new IOException("[!] Errore di I/O");
        }
    }
}