import java.net.*;

public class client {
    public static void main(String[] args) 
    {
        try 
        {
            Socket socket = new Socket("localhost", 27222);
            new ReadThread(socket, this).start();
     
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Erorr: " + e.getMessage());
        }
    }
    


}

/**
 * Innerclient
 */

