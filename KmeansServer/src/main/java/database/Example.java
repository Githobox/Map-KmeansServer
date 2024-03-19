package database;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe Example che modella un esempio.
 * Un esempio è una lista di oggetti.
 * Implementa l'interfaccia Comparable e Serializable.
 */
public class Example implements Comparable<Example>, Serializable {
	/**
	 *  Lista di oggetti.
	 *
	 */
	private final List<Object> example = new ArrayList<Object>();
	/**
	 * Costruttore di default per la classe Example.
	 */
	public Example() {}
	/**
	 * Metodo add per aggiungere un oggetto alla lista.
	 * @param o è un oggetto
	 */
	public void add(Object o) {
		example.add(o);
	}
	/**
	 * Metodo get per ottenere un oggetto dalla lista.
	 * @param i è un intero
	 * @return example.get(i)
	 */
	public Object get(int i) {
		return example.get(i);
	}
	/**
	 * Metodo compareTo per confrontare due esempi.
	 * @param ex è un esempio
	 * @return ((Comparable) o).compareTo(example.get(i))
	 */
	public int compareTo(Example ex) {

		int i = 0;
		for (Object o : ex.example) {
			if (!o.equals(this.example.get(i)))
				return ((Comparable) o).compareTo(example.get(i));
			i++;
		}
		return 0;
	}
	/**
	 * Metodo toString per la stampa.
	 * @return str
	 */
	public String toString() {
		String str = "";
		for (Object o : example)
			str += o.toString() + " ";
		return str;
	}
}