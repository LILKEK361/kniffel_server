import java.net.*;
import java.nio.file.Paths;
import java.util.*;
import java.io.*;
import java.lang.reflect.Array;
import org.json.simple.JSONObject;


public class App {

   
      
    

	
    public static void main(String[] args) throws Exception {
         try {
             //Server socket + client counter
             ServerSocket server = new ServerSocket(3589);
             int counter = 0;
 
             System.out.println("Server starting...");
 
             while (true) {
 
                 counter ++;
                 Socket serverClient = server.accept(); //Server accept the Client connection
                 
                 ServerClientThread sct = new ServerClientThread(serverClient, counter);
                 sct.start();
 
                 
             }
 
         } catch (Exception e) {
             // TODO: handle exception
             System.out.println(e);
         }
    }
    
}
 
 
class ServerClientThread extends Thread  {
  Socket serverClient;
  int clientNo;
  int squre;

  

 

  ServerClientThread(Socket inSocket,int counter){
  serverClient = inSocket;
  clientNo=counter;
  }
 
  public void run(){
    try{
      Hashtable<String, String> client_list = new Hashtable<String, String>();

      client_list.put(Integer.toString(clientNo), serverClient.toString());
      //Nimmt die Daten auf die zum und vom Server geschickt werdem	
       DataInputStream inStream = new DataInputStream(serverClient.getInputStream());
       DataOutputStream outStream = new DataOutputStream(serverClient.getOutputStream());
       
       String clientMessage="", serverMessage="";
       
       while(!clientMessage.equals(".break")){
         
         
         clientMessage=inStream.readUTF();
 
 
         System.out.println(clientMessage + " has joind!");
         
         serverMessage="Guten Tag: " + clientMessage;
         outStream.writeUTF(serverMessage);
         outStream.flush();
 
         
         break;
 
 
       }
       while(!clientMessage.equals(".break")){
         
         
         clientMessage=inStream.readUTF();
         if(clientMessage.equals(".user") || clientMessage.equals(".users")){
            outStream.writeUTF("Userlist:");
            Set<String> keys = client_list.keySet();
            
            for (Integer i : keys) {
                
                outStream.write(i);
                outStream.writeUTF(client_list.get(i).toString());
                outStream.flush();
            }
 
 
         }else if(clientMessage.equals("leave") || clientMessage.equals(".break")){
          
          
 
 
         };
     
     
     
     
     
     
     }
 
 
       System.out.println("Server closed...");
       outStream.writeUTF("Server closed...");
       outStream.flush();
       
      
       inStream.close();
       outStream.close();
       serverClient.close();
 
     }catch(Exception ex){
       System.out.println(ex);
       
     }finally{
       System.out.println("Client -" + clientNo + " exit!! ");
     }
   
 
}}
 
 