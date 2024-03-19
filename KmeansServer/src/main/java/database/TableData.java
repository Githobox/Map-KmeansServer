package database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import database.TableSchema.Column;

/**
 * Classe TableData che si occupa di gestire i dati delle tabelle del database.
 * Si occupa di estrarre i dati da una tabella del database.
 */
public class TableData {
	/**
	 * Oggetto che rappresenta la connessione al database.
	 */
	DbAccess db;
	/**
	 * Costruttore di classe.
	 * Inizializza l'oggetto db.
	 * @param db oggetto che rappresenta la connessione al database.
	 */
	public TableData(DbAccess db) {
		this.db = db;
	}

	/**
	 * Ricava lo schema della tabella con nome table.
	 * Esegue una interrogazione per estrarre le tuple distinte da tale tabella.
	 * Per ogni tupla del resultset crea un oggetto Example e lo aggiunge alla lista
	 * che restituisce.
	 * Per la tupla corrente nel resultset, si estraggono i valori dei singoli
	 * cambi, e li si aggiungono all'oggetto istanza della classe Example che si sta
	 * costruendo.
	 * @param table nome della tabella.
	 * @return lista di oggetti Example.
	 * @throws SQLException eccezione per errore SQL
	 * @throws EmptySetException eccezione per insieme vuoto
	 */
	public List<Example> getDistinctTransazioni(String table) throws SQLException, EmptySetException {

		TableSchema tableSchema = new TableSchema(db, table);
		Statement st = db.getConnection().createStatement();
		ResultSet rs = st.executeQuery("SELECT DISTINCT * FROM " + table + ";");
		if (!rs.next())
			throw new EmptySetException("Il resultset e' vuoto");
		List<Example> transazioni = new ArrayList<Example>();
		while (rs.next()) {
			Example e = new Example();
			for (int i = 0; i < tableSchema.getNumberOfAttributes(); i++) {
				if (tableSchema.getColumn(i).isNumber())
					e.add(rs.getDouble(i+1));
				else
					e.add(rs.getString(i+1));
			}
			transazioni.add(e);
		}
		st.close();
		rs.close();
		return transazioni;
	}
	/**
	 * Formula ed esegue una interrogazione SQL per estrarre i valori distinti
	 * ordinati di column.
	 * Popolare un insieme da restituire valori distinti ordinati in modalità
	 * ascendente.
	 * @param table nome della tabella.
	 * @param column oggetto di tipo Column.
	 * @return insieme di oggetti.
	 * @throws SQLException eccezione per errore SQL
	 */
	public Set<Object> getDistinctColumnValues(String table, Column column) throws SQLException {
		Statement st = db.getConnection().createStatement();
		ResultSet rs = st.executeQuery("SELECT DISTINCT " + column.getColumnName() + " FROM " + table + " ORDER BY "
				+ column.getColumnName() + " ASC;");
		HashSet<Object> columnSet = new HashSet<Object>();
		while (rs.next()) {
			if (column.isNumber())
				columnSet.add(rs.getDouble(column.getColumnName()));
			else
				columnSet.add(rs.getString(column.getColumnName()));
		}
		st.close();
		rs.close();
		return columnSet;
	}
	/**
	 * Formula ed esegue una interrogazione SQL per estrarre il valore aggregato
	 * (minimo o massimo).
	 * Cercato nella colonna di nome column della tabella table.
	 * @param table nome della tabella.
	 * @param column oggetto di tipo Column.
	 * @param aggregate tipo di aggregazione.
	 * @return oggetto.
	 * @throws SQLException eccezione per errore SQL
	 * @throws NoValueException eccezione per valore non presente
	 */
	public Object getAggregateColumnValue(String table, Column column, QUERY_TYPE aggregate)
			throws SQLException, NoValueException {

		Object aggregateValue = null;
		Statement st = db.getConnection().createStatement();
		System.out.println();
		ResultSet rs = st.executeQuery("SELECT " + aggregate + "(" + column.getColumnName() + ") FROM " + table + " ;");
		try {
			if (rs.next()) {
				if (column.isNumber())
					aggregateValue = rs.getDouble(aggregate + "(" + column.getColumnName() + ")");
			} else {
				throw new NoValueException("Il resultset e' vuoto");
			}
		} finally {
			st.close();
			rs.close();
		}
		return aggregateValue;
	}
}
