package data;

/**
 * Classe che rappresenta un attributo continuo.
 * Estende la classe astratta Item.
 */
public class ContinuousItem extends Item {
    /**
     * Costruttore della classe CountinuousItem.
     * @param attribute attributo coinvolto nell'item
     * @param value valore assegnato all'attributo
     */
    public ContinuousItem(Attribute attribute, double value) {
        super(attribute, value);
    }

    /**
     * Determina la distanza in valore assouluto tra il valore scalato memorizzato.
     * nell'Item corrente.
     * E quello scalato associato al parametro a.
     * @param a valore dell'attributo
     */
    double distance(Object a) {
        ContinuousAttribute attribute = (ContinuousAttribute) this.getAttribute();
        return Math.abs(attribute.getScaledValue((double) this.getValue()) - attribute.getScaledValue((double) a));
    }
}
