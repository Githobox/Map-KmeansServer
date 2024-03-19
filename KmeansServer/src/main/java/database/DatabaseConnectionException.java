package database;
/**
 * Eccezione lanciata in caso di errore di connessione al database.
 */
public class DatabaseConnectionException extends Exception {
    /**
     * Costruttore della classe DatabaseConnectionException.
     * Si occupa di lanciare un'eccezione in caso di errore di connessione al database.
     */
    public DatabaseConnectionException() {
        super("Errore di connessione al database");
    }
    /**
     * Costruttore della classe DatabaseConnectionException.
     * Si occupa di lanciare un'eccezione in caso di errore di connessione al database.
     * @param msg messaggio di errore
     */
    public DatabaseConnectionException(String msg) {
        super(msg);
    }
}
