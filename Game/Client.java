package Game;

/**
 * Client
*/

import java.net.*;
import java.io.*;
import java.io.DataInputStream;
import java.util.*;

public class Client {

   public static void main(String[] args) {
      
      try {
         Socket client = new Socket("localhost", 1227);
         
         DataOutputStream output = new DataOutputStream(client.getOutputStream());
         
         Scanner s = new Scanner(System.in);

         String text = s.nextLine();

         output.writeUTF(text);


         DataInputStream in = new DataInputStream(client.getInputStream());
			System.out.println(in.readUTF());
         
         
         

         
      } catch (IOException e) {
         
         e.printStackTrace();
      }
   }
   
}