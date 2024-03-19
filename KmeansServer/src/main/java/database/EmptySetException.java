package database;

/**
 * Eccezione lanciata quando il resultset è vuoto.
 * estende Exception
 */
public class EmptySetException extends Exception {
    /**
     * Costruttore della classe EmptySetException.
     */
    public EmptySetException() {
        super("Il resultset è vuoto");
    }
    /**
     * Costruttore della classe EmptySetException.
     * @param msg messaggio di errore
     */
    public EmptySetException(String msg) {
        super(msg);
    }
}
