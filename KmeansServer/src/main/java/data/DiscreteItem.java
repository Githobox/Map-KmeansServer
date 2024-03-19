package data;

/**
 * Classe che modella un item discreto.
 * Estende la classe Item.
 */
public class DiscreteItem extends Item {
	/**
	 * Costruttore della classe DiscreteItem.
	 * Invoca il costruttore della classe Item.
	 * @param attribute attributo discreto
	 * @param value valore discreto
	 */
	DiscreteItem(DiscreteAttribute attribute, String value) {
		super(attribute, value);
	}
	/**
	 * Metodo che restituisce 0 se il valore dell'attributo è uguale ad a,
	 * altrimenti restituisce 1.
	 * @param a valore discreto
	 * @return 0 se il valore dell'attributo è uguale ad a, 1 altrimenti
	 */
	protected double distance(Object a) {
		return this.getValue().equals(a) ? 0 : 1;
	}
}
