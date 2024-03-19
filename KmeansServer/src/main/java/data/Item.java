package data;
import java.io.Serializable;
import java.util.Set;

/**
 * Classe astratta Item che modella un item.
 * Estende la classe Serializable.
 */
public abstract class Item implements Serializable {
	/**
	 * Attribute attribute: attributo coinvolto nell'item.
	 */
	private final Attribute attribute;
	/**
	 * Object value: valore assegnato all'attributo.
	 */
	private Object value;
	/**
	 * Costruttore della classe Item.
	 * Inizializza gli attributi attribute e value.
	 * @param attribute: attributo coinvolto nell'item.
	 * @param value: valore assegnato all'attributo.
	 */
	public Item(Attribute attribute, Object value) {
		this.attribute = attribute;
		this.value = value;
	}
	/**
	 * Metodo get per l'attributo attribute.
	 * @return attribute: attributo coinvolto nell'item.
	 */
	protected Attribute getAttribute() {
		return this.attribute;
	}
	/**
	 * Metodo get per l'attributo value.
	 * @return value: valore assegnato all'attributo.
	 */
	protected Object getValue() {
		return this.value;
	}
	/**
	 * Metodo toString per l'attributo value.
	 * @return value.toString(): valore assegnato all'attributo.
	 */
	public String toString() {
		return getValue().toString();
	}
	/**
	 * Metodo astratto per il calcolo della distanza.
	 * @param a: oggetto di tipo Object.
	 */
	abstract double distance(Object a);
	/**
	 * Modifica il membro value.
	 * Assegnadogli il valore restituito dal metodo.
	 * computeProprotyper(clusteredData,attribute).
	 * @param data: oggetto di tipo Data.
	 * @param clusteredData: insieme di interi.
	 */
	public void update(Data data, Set<Integer> clusteredData) {
		this.value = data.computePrototype(clusteredData, attribute);
	}
}