package Game;

/**
 * Client
*/

import java.net.*;
import java.io.*;
import java.io.DataInputStream;
import java.util.*;


public class Client {
   public static void main(String[] args) throws Exception {
   try{
     Socket socket=new Socket("localhost",3589);
     
     DataInputStream inStream=new DataInputStream(socket.getInputStream());
     DataOutputStream outStream=new DataOutputStream(socket.getOutputStream());
     BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
     
     String clientMessage="",serverMessage="";
     
     //Anmeldung
     while(!clientMessage.equals(".break")){
       
      
       System.out.println("Enter your Name :");
       
       //Nimmt einen String aus der Console und packt in in eine Var
       clientMessage=br.readLine();
       
       //Nimmt die Var und schickt diese an den Server
       outStream.writeUTF(clientMessage);
       outStream.flush();

       //Hier nimmt das Programm die Antwort vom Server und packt diese in eine Var 
       serverMessage=inStream.readUTF();
       
       System.out.println(serverMessage);
       if (clientMessage != "") {

         break;
         
       }
     }

     while(!clientMessage.equals(".break")){
       
      
      System.out.println("Please wait for the Admin to start the Game");
      
      //Nimmt einen String aus der Console und packt in in eine Var
      clientMessage=br.readLine();
      
      //Nimmt die Var und schickt diese an den Server
      outStream.writeUTF(clientMessage);
      outStream.flush();

      //Hier nimmt das Programm die Antwort vom Server und packt diese in eine Var 
      serverMessage=inStream.readUTF();
      
      System.out.println(serverMessage);
      if (clientMessage != "" ) {
         System.out.println("break");
        break;
        
      }
    }

    
     
     
     outStream.close();
     
     outStream.close();
    
     socket.close();
   
   }catch(Exception e){
     
      System.out.println(e);
   
   }
   }
 }