import java.util.Scanner;
import java.io.*;
import java.net.*;


public class Client
{
    public static void main(String[] args) throws IOException
    {
        try
        {
            Scanner eingabe = new Scanner(System.in);
            
            
            
            // establishing the connection 
            Socket client_socket = new Socket("localhost", 3333);           
            DataInputStream ournewDataInputstream = new DataInputStream(client_socket.getInputStream());
            DataOutputStream ournewDataOutputstream = new DataOutputStream(client_socket.getOutputStream());
            
            // In the following loop, the client and client handle exchange data.
            
            String in;

            while (true)
            {
                in = ournewDataInputstream.readUTF();

               
                System.out.println(in);
                String clientmsg = eingabe.nextLine();
                ournewDataOutputstream.writeUTF(clientmsg);


                // Exiting from a while loo should be done when a client gives an exit message.
                if(clientmsg.equals("Exit"))
                {
                    System.out.println("Connection closing... : " + client_socket);
                    client_socket.close();
                    System.out.println("Closed");
                    break;
                }
                
                // printing date or time as requested by client
                String newresuiltReceivedString ="";
                if(clientmsg.equals("commands"))
                {
                    

                    newresuiltReceivedString = ournewDataInputstream.readUTF();

                   
                    System.out.println(newresuiltReceivedString);
                }
                
            }
            
            eingabe.close();
            ournewDataInputstream.close();
            ournewDataOutputstream.close();
        }catch(Exception e){
            e.printStackTrace();
            System.out.println(e);
        }
    }
}