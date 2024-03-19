package data;
/**
 * Eccezione lanciata quando il numero di cluster è fuori dal range consentito.
 * Estende la classe Exception.
 */
public class OutOfRangeSampleSize extends Exception {
    /**
     * Costruttore della classe OutOfRangeSampleSize.
     * Invoca il costruttore della classe Exception.
     */
    public OutOfRangeSampleSize() {
        super("Numero di cluster non valido");
    }
    /**
     * Costruttore della classe OutOfRangeSampleSize.
     * Invoca il costruttore della classe Exception.
     * @param msg messaggio di errore
     */
    public OutOfRangeSampleSize(String msg) {
        super(msg);
    }
}
