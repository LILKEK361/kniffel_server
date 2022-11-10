
import gamedb.GameDB;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class KniffelServer {

    /**
     * port number for TCP server communication
     */
    private static final int PORT_NUMBER = 27222;

    /**
     * maximal number of players
     */
    private static final int MAX_PLAYERS = 5;

    /**
     * maximal number of connecting users
     */
    private static final int MAX_CONNECTING_USERS = 20;

    public static void main(String[] args) {

        /**
         * game database variable
         */
        GameDB gameDB;

        /**
         * because every client used the game database, it can only be generated
         * centrally and passed on to each instance.
         */
        gameDB = new GameDB(MAX_CONNECTING_USERS, MAX_PLAYERS);

        try {
            /**
             * creating new tcp server socket
             */
            ServerSocket sSocket = new ServerSocket(PORT_NUMBER);

            while (true) {

                /**
                 * socket vor client communikation
                 */
                Socket cSocket;

                try {
                    
                    /**
                     * wait until a new client is connected
                     */
                    cSocket = sSocket.accept();
                    
                    /**
                     * handling a new client in a own thread
                     * attention: a thread can not throw a exection
                     */
                    Thread tClient;
                    
                    tClient = new Thread(new ClientHandler(cSocket, gameDB));
                    
                    tClient.start();

                } catch (IOException ex) {
                    /**
                     * if accepting new client connection throws this error then
                     * close application
                     */
                    System.out.print("exception: while accepting socket ");
                    System.out.println(ex.toString());
                    System.exit(-1);
                }
            }

        } catch (IOException e) {
            /**
             * if creating new tcp server socket throws this error then close
             * application
             */
            System.out.println("exception: " + e.getMessage());
            System.exit(-1);
        }
    }
}
