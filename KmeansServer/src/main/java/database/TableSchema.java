package database;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Classe TableSchema che si occupa di gestire lo schema delle tabelle del
 * database. Si occupa di estrarre lo schema di una tabella del database.
 */
public class TableSchema {
	/**
	 * Oggetto che rappresenta la connessione al database.
	 */
	DbAccess db;
	/**
	 * Classe che rappresenta una colonna della tabella.
	 */
	public class Column {
		/**
		 * Nome della colonna.
		 */
		private String name;
		/**
		 * Tipo della colonna.
		 */
		private String type;
		/**
		 * Costruttore di classe.
		 * Inizializza l'oggetto con i parametri passati.
		 * @param name nome della colonna.
		 * @param type tipo della colonna.
		 */
		Column(String name, String type) {
			this.name = name;
			this.type = type;
		}
		/**
		 * Metodo get per il nome della colonna.
		 * @return nome della colonna.
		 */
		public String getColumnName() {
			return name;
		}
		/**
		 * Metodo get per il tipo della colonna per vedere se è stringa o numero.
		 * @return true se è stringa, false altrimenti.
		 */
		public boolean isNumber() {
			return type.equals("number");
		}
		/**
		 * Metodo toString per la stampa.
		 * @return stringa da stampare.
		 */
		public String toString() {
			return name + ":" + type;
		}
	}
	/**
	 * Lista di colonne della tabella.
	 */
	List<Column> tableSchema = new ArrayList<>();
	/**
	 * Costruttore di classe.
	 * Inizializza l'oggetto db e la lista tableSchema.
	 * Esegue una interrogazione per estrarre lo schema della tabella con nome.
	 * @param db oggetto che rappresenta la connessione al database.
	 * @param tableName nome della tabella.
	 * @throws SQLException eccezione per errore SQL
	 */
	public TableSchema(DbAccess db, String tableName) throws SQLException {
		this.db = db;
		HashMap<String, String> mapSQL_JAVATypes = new HashMap<String, String>();
		// http://java.sun.com/j2se/1.3/docs/guide/jdbc/getstart/mapping.html
		mapSQL_JAVATypes.put("CHAR", "string");
		mapSQL_JAVATypes.put("VARCHAR", "string");
		mapSQL_JAVATypes.put("LONGVARCHAR", "string");
		mapSQL_JAVATypes.put("BIT", "string");
		mapSQL_JAVATypes.put("SHORT", "number");
		mapSQL_JAVATypes.put("INT", "number");
		mapSQL_JAVATypes.put("LONG", "number");
		mapSQL_JAVATypes.put("FLOAT", "number");
		mapSQL_JAVATypes.put("DOUBLE", "number");

		Connection con = db.getConnection();
		DatabaseMetaData meta = con.getMetaData();
		ResultSet res = meta.getColumns(null, null, tableName, null);

		while (res.next()) {

			if (mapSQL_JAVATypes.containsKey(res.getString("TYPE_NAME")))
				tableSchema.add(new Column(
						res.getString("COLUMN_NAME"),
						mapSQL_JAVATypes.get(res.getString("TYPE_NAME"))));
		}
		res.close();
	}

	/**
	 * Metodo get per il numero di attributi della tabella.
	 * @return numero di attributi della tabella.
	 */
	public int getNumberOfAttributes() {
		return tableSchema.size();
	}

	/**
	 * metodo get per la colonna di indice index.
	 * @param index indice della colonna.
	 * @return colonna di indice index.
	 */
	public Column getColumn(int index) {
		return tableSchema.get(index);
	}
}
