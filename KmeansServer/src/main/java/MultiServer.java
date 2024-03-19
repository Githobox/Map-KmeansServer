import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Classe MultiServer che si occupa di gestire la connessione con più client.
 */
public class MultiServer {
    /**
     * Porta di ascolto del server.
     */
    private int PORT = 8080;
    /**
     * Metodo main della classe MultiServer.
     * @param args argomenti passati da riga di comando
     */
    public static void main(String[] args) {
        MultiServer server = new MultiServer(8080);
        server.run();
    }
    /**
     * Costruttore della classe MultiServer.
     * @param port porta di ascolto del server
     */
    public MultiServer(final int port) {
        this.PORT = port;
    }
    /**
     * Metodo run del Multiserver.
     */
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server in ascolto sulla porta " + PORT + "...");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Nuova connessione accettata da " + clientSocket.getInetAddress());

                ServerOneClient clientHandler = new ServerOneClient(clientSocket);
                clientHandler.start();
            }
        } catch (IOException e) {
            System.out.println("[!] Errori verificati durante l'esecuzione del server");
            // e.printStackTrace();
        }
    }
}
