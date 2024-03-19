import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;

import data.Data;
import data.OutOfRangeSampleSize;
import database.EmptySetException;
import database.NoValueException;
import mining.KmeansMiner;

/**
 * Classe ServerOneClient che si occupa di gestire la connessione con un client.
 */
public class ServerOneClient extends Thread {
    /**
     * Socket per la connessione con il client.
     */
    Socket socket;
    /**
     * Stream di input per la comunicazione con il client.
     */
    ObjectInputStream in;
    /**
     * Stream di output per la comunicazione con il client.
     */
    ObjectOutputStream out;
    /**
     * Oggetto KmeansMiner per l'esecuzione dell'algoritmo k-means.
     */
    KmeansMiner kmeans;
    /**
     * Oggetto Data per la memorizzazione dei dati.
     */
    Data data;

    /**
     * Costruttore della classe ServerOneClient.
     * Inizializza gli attributi socket, out, in e kmeans.
     * E avvia il thread.
     * @param s socket per la connessione con il client.
     * @throws IOException eccezione per errore di I/O
     */
    public ServerOneClient(Socket s) throws IOException {
        socket = s;
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
    }
    /**
     * Rscrive il metodo run della superclasse Thread.
     * Si occupa di gestire la richiesta del client.
     * Riceve la scelta dell'operazione dal client.
     * Processa la richiesta del client.
     * Nel caso 1, riceve i parametri di connessione al database dal client.
     * Nel caso 2, riceve il numero di cluster dal client e esegue l'algoritmo k-means.
     * Nel caso 3, riceve il nome del file dal client e salva i dati su file.
     * Nel caso 4, riceve il nome del file dal client e carica i dati da file.
     * Nel caso 5, chiude la connessione.
     */
    public void run() {
        String risposta;
        int scelta;
        int k, numberOfIterations;
        try {
            while (true) {
                risposta = "OK";
                // Ricevi la scelta dell'operazione dal client
                scelta = (int) in.readObject();
                if (scelta == 5)
                    break;
                System.out.println("Scelta: " + scelta);
                // Processa la richiesta del client
                switch (scelta) {
                    case 1: // Opzione storeTableFromDb
                    {
                        try {
                            /*
                             * Operazione di caricamento dei dati da un database
                             * Ricevi i parametri di connessione al database dal client
                             */
                            String server = (String) in.readObject();
                            String db = (String) in.readObject();
                            String tableName = (String) in.readObject();
                            String username = (String) in.readObject();
                            String password = (String) in.readObject();
                            try {
                                data = new Data(server, db, tableName, username, password);
                            } catch (SQLException | NoValueException | EmptySetException e) {
                                risposta = "[!] Errore di connessione al database";
                                out.writeObject(risposta);
                            }
                        } catch (Exception e) {
                            risposta = "[!] Errore durante il caricamento dei dati";
                            out.writeObject(risposta);
                            // e.printStackTrace();
                            break;
                        }
                        try {
                            out.writeObject(risposta);
                            if (risposta.equals("OK"))
                                out.writeObject(data.toString());
                        } catch (IOException e) {
                            risposta = "[!] Errore nella comunicazione con il client";
                            out.writeObject(risposta);
                            break;
                        }
                        break;
                    }
                    case 2: // Operazione learningFromDbTable
                    {

                        /*
                         * Ricevi il numero di cluster dal client
                         * Esegui l'algoritmo k-means
                         * Invia il risultato al client
                         */
                        try {
                            k = (int) in.readObject();
                            kmeans = new KmeansMiner(k);
                            numberOfIterations = kmeans.kmeans(data);
                            System.out.println("[-] Numero di iterazioni: " + numberOfIterations);
                        } catch (OutOfRangeSampleSize e) {
                            risposta = "[!]" + e.getMessage();
                            out.writeObject(risposta);
                            break;
                        } catch (Exception e) {
                            risposta = "[!] Errore durante l'esecuzione dell'algoritmo k-means";
                            out.writeObject(risposta);
                            break;
                        }
                        try {
                            out.writeObject(risposta);
                            // Invia il numero di iterazioni al client prima del risultato
                            out.writeObject(k);
                            risposta = "Numero di iterazioni: " + k + "\n";
                            out.writeObject(risposta + kmeans.getC().toString(data));
                        } catch (IOException e) {
                            risposta = "[!] Errore nella comunicazione con il client";
                            out.writeObject(risposta);
                            break;
                        }
                        break;
                    }
                    
                    case 3: // Opzione storeClusterInFile
                    /*
                     * Operazione di salvataggio dei dati su file
                     * Ricevi il nome del file dal client
                     * Salva i dati su file
                     */
                    {
                        try {
                            String fileName = (String) in.readObject();
                            System.out.println("[-] Salvataggio su file: " + fileName);
                            kmeans.salvaKmeansMiner("KmeansServer\\KmeansServer\\DataStore\\" + fileName + ".dat");
                            out.writeObject(risposta);
                        } catch (Exception e) {
                            risposta = "[!] Errore durante il salvataggio dei dati";
                            out.writeObject(risposta);
                            break;
                        }
                        break;
                    }
                    case 4: // Opzione learningFromFile
                    {
                        try {
                            /*
                             * Operazione di caricamento dei dati da un file
                             * Ricevi il nome del file dal client
                             * Carica i dati da file
                             * Invia i dati al client
                             */
                            String fileName = (String) in.readObject();
                            k = (int) in.readObject();
                            //System.out.println("[-] Caricamento da file: " + numberOfIterations);
                            System.out.println("[-] Caricamento da file: " + fileName);
                            kmeans = new KmeansMiner("KmeansServer\\KmeansServer\\DataStore\\" + fileName + ".dat");
                        } catch (IOException | ClassNotFoundException e) {
                            risposta = "[!] Errore durante il caricamento dei dati";
                            out.writeObject(risposta);
                            break;
                        }
                        out.writeObject(risposta);
                        try {
                            // Invia il numero di iterazioni prima del risultato
                            out.writeObject(k);
                            out.writeObject("[-] Elenco Centroidi:\n" + kmeans.getC().toString());
                        } catch (IOException e) {
                            risposta = "[!] Errore nella comunicazione con il client";
                            out.writeObject(risposta);
                            break;
                        }
                        break;
                    }
                    // Opzione di chiusura della connessione
                    default:
                        risposta = "[!] Errore nella scelta dell'operazione";
                        out.writeObject(risposta);
                        break;
                }
            }
        } catch (Exception e) {
            System.out.println("[!] Errore di natura generica");
        } finally {
            // Chiudi le risorse in caso di terminazione del thread
            try {
                risposta = "[-] Connessione Terminata";
                out.writeObject(risposta);
                System.out.println(risposta);
                out.close();
                in.close();
                socket.close();
            } catch (IOException e) {
                System.out.println("[!] Errore nella chiusura delle risorse");
            }
        }
    }
}
