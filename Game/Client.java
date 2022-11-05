package Game;

/**
 * Client
*/

import java.net.*;
import java.io.*;
import java.io.DataInputStream;
import java.util.*;


public class Client {

   public static void main(String[] args)throws UnknownHostException, IOException, ClassNotFoundException, InterruptedException {
      
      for(int i = 0; i < 10; i++) {
         Socket client = new Socket("localhost", 1227);
         
         DataOutputStream output = new DataOutputStream(client.getOutputStream());
         
         Scanner s = new Scanner(System.in);

         String message = s.nextLine();

         output.writeUTF(message);


         DataInputStream input = new DataInputStream(client.getInputStream());
			System.out.println(input.readUTF());
         
         
         

         
      }
   }
   
}