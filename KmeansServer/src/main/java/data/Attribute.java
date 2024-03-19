package data;

import java.io.Serializable;

/**
 * Classe astratta Attribute che rappresenta un attributo generico.
 * Gli attributi sono rappresentati da un nome simbolico e da un identificativo.
 * @see Serializable
 */
public abstract class Attribute implements Serializable {
    /**
     * Nome simbolico dell'attributo.
     */
    private String name;
    /**
     * Identificativo dell'attributo.
     */
    private int index;

    /**
     * Costruttore della classe Attribute.
     * @param name nome simbolico dell'attributo
     * @param index identificativo dell'attributo
     */
    public Attribute(String name, int index) {
        this.name = name;
        this.index = index;
    }
    /**
     * Metodo get per l'attributo nome.
     * @return nome simbolico dell'attributo
     */
    protected String getName() {
        return this.name;
    }
    /**
     * Metodo get per l'attributo index.
     * @return identificativo dell'attributo
     */
    protected int getIndex() {
        return this.index;
    }

    /**
     * Metodo toString per l'attributo name.
     */
    public String toString() {
        return this.getName();
    }
}
