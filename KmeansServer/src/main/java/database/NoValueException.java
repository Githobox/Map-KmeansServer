package database;

/**
 * Eccezione lanciata in caso di assenza di valore all'interno di un resultset.
 * estende Exception.
 */
public class NoValueException extends Exception {
    /**
     * Costruttore della classe NoValueException.
     */
    public NoValueException() {
        super("Assenza di valore all'interno di un resultset");
    }
    /**
     * Costruttore della classe NoValueException.
     * @param msg messaggio di errore
     */
    public NoValueException(String msg) {
        super(msg);
    }
}
