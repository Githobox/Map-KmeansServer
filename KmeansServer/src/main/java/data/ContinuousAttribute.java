package data;

/**
 * Classe astratta ContinuousAttribute che rappresenta un attributo continuo.
 * Gli attributi continui sono rappresentati da un nome simbolico, da un identificativo,
 */
public class ContinuousAttribute extends Attribute {
	/**
	 *  Estremo superiore dell'intervallo di valori che l'atributo può assumere.
	 */
	private double max;
	/**
	 * Estremo inferiore dell'intervallo di valori che l'atributo può assumere.
	 */
	private double min;
	/**
	 * Costruttore della classe ContinuousAttribute.
	 * @param name nome simbolico dell'attributo
	 * @param index identificativo dell'attributo
	 * @param min estremo inferiore dell'intervallo di valori che l'atributo può assumere
	 * @param max estremo superiore dell'intervallo di valori che l'atributo può assumere
	 */
	public ContinuousAttribute(String name, int index, double min, double max) {
		super(name, index);
		this.min = min;
		this.max = max;
	}

	/**
	 * Metodi get Max.
	 * @return estremo superiore dell'intervallo di valori che l'atributo può assumere
	 */
	private double getMax() {
		return this.max;
	}
	/**
	 * Metodo get Min.
	 * @return estremo inferiore dell'intervallo di valori che l'atributo può assumere
	 */
	private double getMin() {
		return this.min;
	}
	/**
	 * Metodo per ottenere il valore normalizzato di un attributo continuo.
	 * @param v valore dell'attributo
	 * @return valore normalizzato
	 */
	protected double getScaledValue(double v) {
		v = (v - this.getMin()) / (this.getMax() - this.getMin());
		return v;
	}
}
