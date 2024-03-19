package data;
import database.*;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.*;

/**
 * Classe Data che si occupa di gestire i dati di addestramento.
 * I dati sono memorizzati in una matrice di oggetti, dove ogni riga modella una
 * transazione.
 */
public class Data implements Serializable {
	/**
	 *  Matrice di oggetti dove ogni riga modella una transazione.
	 */
	private final List<Example> data;
	/**
	 * cardianlità dell'insieme di transazioni (numero di righe della matrice).
	 */
	private final int numberOfExamples;
	/**
	 * Vettore degli attributi in ciascuna tupla.
	 */
	private final List<Attribute> attributeSet;

	/**
	 * Costruttore della classe Data.
	 * Si occupa di caricare i dati di addestramento da una tabella della base di
	 * dati.
	 * Il nome della tabella è passato come parametro.
	 * @param server   nome del server
	 * @param database nome del database
	 * @param table    nome della tabella
	 * @param userId   nome utente per l'accesso al database
	 * @param password password per l'accesso al database
	 * @throws SQLException eccezione per errore SQL
	 * @throws NoValueException eccezione per valore non presente
	 * @throws EmptySetException eccezione per insieme vuoto
	 */
	public Data(String server, String database, String table, String userId, String password)
			throws SQLException, NoValueException, EmptySetException {
		DbAccess db = new DbAccess(server, database, userId, password);
		try {
			db.initConnection();
		} catch (DatabaseConnectionException e) {
			System.out.println("Errore di connessione al database");
			e.printStackTrace();
		}

		TableSchema tableSchema = new TableSchema(db, table);
		TableData tableData = new TableData(db);
		
		data = tableData.getDistinctTransazioni(table);

		numberOfExamples = tableData.getDistinctTransazioni(table).size();

		attributeSet = new LinkedList<Attribute>();

		for (int i = 0; i < tableSchema.getNumberOfAttributes(); i++) {
			TableSchema.Column column = tableSchema.getColumn(i);
			String columnName = column.getColumnName();
			if (column.isNumber()) {
				ContinuousAttribute continuousAttribute = new ContinuousAttribute(columnName, i,
						(double) tableData.getAggregateColumnValue(table, column, QUERY_TYPE.MIN),
						(double) tableData.getAggregateColumnValue(table, column, QUERY_TYPE.MAX));
				attributeSet.add(continuousAttribute);
			} else {
				String[] distinctValues =new String[tableData.getDistinctColumnValues(table, column).size()];
				tableData.getDistinctColumnValues(table,column).toArray(distinctValues);

				DiscreteAttribute discreteAttribute = new DiscreteAttribute(columnName, i, distinctValues);
				attributeSet.add(discreteAttribute);
			}
		}

		db.closeConnection();
	}
	/**
	 * Restituisce il numero di esempi.
	 * @return numero di esempi
	 */
	public int getNumberOfExamples() {
		return numberOfExamples;
	}
	/**
	 * Restituisce il numero di attributi.
	 * @return numero di attributi
	 */
	public int getNumberOfAttributes() {
		return attributeSet.size();
	}
	/**
	 * Restituisce lo schema dei dati.
	 * @param index indice di colonna
	 * @return schema dei dati
	 */
	protected Attribute getAttribute(int index) {
		return attributeSet.get(index);
	}
	/**
	 * Restituisce il valore di data in posizione exampleIndex, attributeIndex.
	 * @param exampleIndex   indice di riga
	 * @param attributeIndex indice di colonna
	 * @return valore di data in posizione exampleIndex, attributeIndex
	 */
	public Object getAttributeValue(int exampleIndex, int attributeIndex) {
		return data.get(exampleIndex).get(attributeIndex);
	}
	/**
	 * Crea un istanza di Tuple che modelli la transazione con indice di riga index
	 * in data.
	 * Restituisce il riferimento a tale istanza.
	 * @param index indice di riga
	 * @return riferimento a tale istanza
	 */
	public Tuple getItemSet(int index) {
		Tuple tuple = new Tuple(attributeSet.size());
        for (int i = 0; i < attributeSet.size(); i++) {
            if (attributeSet.get(i) instanceof ContinuousAttribute) {
                tuple.add(new ContinuousItem(attributeSet.get(i), (Double)
                        data.get(index).get(attributeSet.get(i).getIndex())), i);
            } else if (attributeSet.get(i) instanceof DiscreteAttribute) {
                tuple.add(new DiscreteItem((DiscreteAttribute) attributeSet.get(i), (String)
                        data.get(index).get(attributeSet.get(i).getIndex())), i);
            }
        }
        return tuple;
	}
	/**
	 * Restituisce il numero di cluster da generare.
	 * @param k numero di cluster
	 * @return numero di cluster
	 * @throws OutOfRangeSampleSize eccezione per dimensione campione fuori range
	 */
	public int[] sampling(int k) throws OutOfRangeSampleSize {
		if (k < 0 || k > data.size()) {
			throw new OutOfRangeSampleSize("Inserire un numero di cluster compreso tra 1 e " + this.data.size() + "");
		}
		int centroidIndexes[] = new int[k];
		// choose k random different centroids in data.
		Random rand = new Random();
		rand.setSeed(System.currentTimeMillis());
		for (int i = 0; i < k; i++) {
			boolean found;
			int c;
			do {
				found = false;
				c = rand.nextInt(getNumberOfExamples());
				// verify that centroid[c] is not equal to a centroide
				// already stored in CentroidIndexes
				for (int j = 0; j < i; j++)
					if (compare(centroidIndexes[j], c)) {
						found = true;
						break;
					}
			} while (found);
			centroidIndexes[i] = c;
		}
		return centroidIndexes;
	}
	/**
	 * Restituisce true se le due righe di data sono uguali.
	 * @param i indice di riga
	 * @param j indice di riga
	 * @return true se le due righe di data sono uguali
	 */
	private boolean compare(int i, int j) {
		for (int k = 0; k < attributeSet.size(); k++)
		if (!(this.data.get(i).get(k).equals(data.get(j).get(k)))) {
			return false;
		}
	return true;
	}

	/**
	 * Usa lo RTTI per determinare se attribute riferisce una istanza di
	 * ContinuousAttribute o di DiscreteAttribute.
	 * @param idList    insieme di indici di riga
	 * @param attribute attributo
	 * @return valore prototipo
	 */
	public Object computePrototype(Set<Integer> idList, Attribute attribute) {
		if (attribute instanceof ContinuousAttribute)
			return computePrototype(idList, (ContinuousAttribute) attribute);
		else
			return computePrototype(idList, (DiscreteAttribute) attribute);
	}
	/**
	 * Determina il valore che occorre più frequentemente per attribute nel
	 * sottoinsieme di dati individuato da idList.
	 * @param idList    insieme di indici di riga
	 * @param attribute attributo
	 * @return valore prototipo
	 */
	private String computePrototype(Set<Integer> idList, DiscreteAttribute attribute) {
        Iterator<String> it = attribute.iterator();
        String first = it.next();
        int max = attribute.frequency(this, idList, first);
        int tmp;
        String prototype = first;
        String tmp_string;
        while (it.hasNext()) {
            tmp_string = it.next();
            tmp = attribute.frequency(this, idList, tmp_string);
            if (tmp > max) {
                max = tmp;
                prototype = tmp_string;
            }
        }
        return prototype;
	}

	/**
	 * Determina il valore prototipo come media dei valori osservati per attribute
	 * nelle transazioni di data.
	 * Aventi indice di riga in idList.
	 * @param idList    insieme di indici di riga
	 * @param attribute attributo
	 * @return valore prototipo
	 */
	private Double computePrototype(Set<Integer> idList, ContinuousAttribute attribute) {
		double sum = 0;
		for (Integer i : idList)
			sum += (double) data.get(i).get(attribute.getIndex());
		return sum / idList.size();
	}
	/**
	 * Costruisce una stringa in cui memorizza lo schema della tabella e le
	 * transazioni memorizzate in data.
	 * @return stringa in cui memorizza lo schema della tabella e le transazioni memorizzate in data
	 */
	public String toString() {
		String s = new String();
		s = s + "N,";
		for (int i = 0; i < this.getNumberOfAttributes(); i++) {
			s += attributeSet.get(i).toString() + ",";
		}
		s = s + "\n";
		for (int i = 0; i < getNumberOfExamples(); i++) {
			s += (i + 1) + ":";
			for (int j = 0; j < getNumberOfAttributes(); j++) {
				s += getAttributeValue(i, j) + ",";
			}
			s = s + "\n";
		}
		return s;
	}
}
