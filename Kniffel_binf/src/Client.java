

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
     ObjectInputStream oInputStream = new ObjectInputStream(socket.getInputStream());
     
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
       break;
     }

     while(!clientMessage.equals(".break")){
     
        if(!inStream.equals(String)){

          System.out.println(inStream);
          Thread.sleep(100);

        }else if(!oInputStream.equals("")){

          System.out.println(oInputStream);
          Thread.sleep(100);

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