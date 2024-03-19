package mining;

import java.io.Serializable;
import java.util.Set;
import java.util.HashSet;

import data.Data;
import data.Tuple;

/**
 * Classe Cluster che rappresenta un cluster.
 * Un cluster e' rappresentato da un centroide e da un insieme di transazioni.
 * Implementa l'interfaccia Serializable.
 */
public class Cluster implements Serializable {
	/**
	 * Centroide del cluster.
	 * E' rappresentato da una tupla.
	 */
	private Tuple centroid;
	/**
	 * Insieme di transazioni clusterizzate.
	 * E' rappresentato da un insieme di interi.
	 */
	private Set<Integer> clusteredData;

	/**
	 * Costruttore della classe Cluster.
	 * @param centroid centroide del cluster
	 */
	Cluster(Tuple centroid) {
		this.centroid = centroid;
		this.clusteredData = new HashSet<>();
	}

	/**
	 * Restituisce il centroide del cluster.
	 * @return centroid del cluster
	 */
	protected Tuple getCentroid() {
		return centroid;
	}

	/**
	 * Calcola il centroide del cluster.
	 * @param data dataset
	 */
	protected void computeCentroid(Data data) {
		for (int i = 0; i < centroid.getLength(); i++) {
			centroid.get(i).update(data, clusteredData);
		}
	}
	/**
	 * Restituisce true se la tupla cambia il cluster.
	 * @param id indice della tupla
	 * @return data
	 */
	protected boolean addData(int id) {
		return clusteredData.add(id);
	}

	/**
	 * verifica se una transazione e' clusterizzata nell'array corrente.
	 * @param id indice della tupla
	 * @return data è true se la transazione è clusterizzata nell'array corrente
	 */
	protected boolean contain(int id) {
		return clusteredData.contains(id);
	}

	/**
	 * Rimuove le tuple che hanno cambiato il cluster.
	 * @param id indice della tupla
	 */
	protected void removeTuple(int id) {
		clusteredData.remove(id);
	}

	/**
	 * Converte il cluster in una stringa.
	 * @return str
	 */
	public String toString() {
		String str = "Centroid=(";
		for (int i = 0; i < centroid.getLength(); i++)
			str += centroid.get(i) + ",";
		str += ")";
		return str;
	}

	/**
	 * Converte il cluster in una stringa.
	 * @param data dataset
	 * @return str
	 */
	public String toString(Data data) {
		String str = "Centroid=(";
		for (int i = 0; i < centroid.getLength(); i++)
			str += centroid.get(i) + ",";
		str += ")\nExamples:\n";

		for (int i : clusteredData) {
			str += " [";
			for (int j = 0; j < data.getNumberOfAttributes(); j++)
				str += data.getAttributeValue(i, j) + " ";
			str += "] dist=" + getCentroid().getDistance(data.getItemSet(i)) + "\n";
		}
		str += "\nAvgDistance=" + getCentroid().avgDistance(data, clusteredData) + "\n";
		return str;
	}
}
