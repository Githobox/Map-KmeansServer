package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe che si occupa di gestire la connessione al database.
 * In particolare, si occupa di inizializzare la connessione al database e di
 * chiuderla.
 */
public class DbAccess {
  /**
   * Costante che contiene il nome del driver da utilizzare per la connessione al database.
   */
  String DRIVER_CLASS_NAME = "com.mysql.cj.jdbc.Driver";
  /**
   * Costante che contiene il nome del DBMS a cui ci si vuole connettere.
   */
  private String DBMS = "jdbc:mysql";
  /**
   * Contiene l’identificativo del server su cui risiede la base di dati.
   */
  private String server = "localhost";
  /**
   * Contiene il nome della base di dati.
   */
  private String database = "mapdb";
  /**
   * Costante che contiene la porta su cui il DBMS MySQL accetta le connessioni.
   */
  private final int PORT = 3306;
  /**
   * Contiene il nome dell’utente per l’accesso alla base di dati.
   */
  private String user_id = "MapUser";
  /**
   * Contiene la password di autenticazione per l’utente identificato da USER_ID.
   */
  private String password = "map";
  /**
   * Riferimento alla connessione al database.
   */
  Connection conn;

  /**
   * Costruttore della classe DbAccess.
   * @param server nome del server
   * @param database nome del database
   * @param user_id nome utente per l'accesso al database
   * @param password password per l'accesso al database
   */
  public DbAccess(String server, String database, String user_id, String password) {
    this.server = server;
    this.database = database;
    this.user_id = user_id;
    this.password = password;
  }

  /**
   * Impartisce al class loader l’ordine di caricare il driver mysql,
   * inizializza la connessione riferita da conn.
   * Il metodo solleva e propaga una eccezione di tipo DatabaseConnectionException in caso
   * di fallimento nella connessione al database
   * @throws DatabaseConnectionException eccezione per fallimento connessione al database
   */
  public void initConnection() throws DatabaseConnectionException {
    try {
      Class.forName(DRIVER_CLASS_NAME);
    } catch (ClassNotFoundException e) {
      throw new DatabaseConnectionException("[!] Driver non trovato: " + e.getMessage());
    }
    String connectionString = DBMS + "://" + server + ":" + PORT + "/" + database
        + "?user=" + user_id + "&password=" + password + "&serverTimezone=UTC";
    System.out.println("Connection's String: " + connectionString);
    try {
      conn = DriverManager.getConnection(connectionString);
    } catch (SQLException e) {
      throw new DatabaseConnectionException(
          "[!] SQLException: " + e.getMessage() + "\n"
              + "[!] SQLState: " + e.getSQLState() + "\n"
              + "[!] VendorError: " + e.getErrorCode());
    }
  }

  /**
   * Restituisce la connessione al database
   * @return conn
   */
  public Connection getConnection() {
    return conn;
  }
  /**
   * Chiude la connessione al database
   */
  public void closeConnection() {
    try {
      conn.close();
    } catch (Exception e) {
      System.out.println("Errore nella chiusura della connessione");
    }
  }
}
