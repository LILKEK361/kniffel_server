

/**
 * Client
*/

import java.net.*;
import java.io.*;
import java.io.DataInputStream;
import java.util.*;


public class Client {
<<<<<<< HEAD
  private Socket clientSocket;
  private PrintWriter out;
  private BufferedReader in;
=======
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
>>>>>>> parent of b583ec1 (Test 01)

  public void startConnection (int port) throws IOException {
      clientSocket = new Socket("localhost", port);
      out = new PrintWriter(clientSocket.getOutputStream(), true);
      in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
  }

<<<<<<< HEAD
  public String sendMessage(String msg) throws IOException {
      out.println(msg);
      String resp = in.readLine();
      return resp;
  }

  public void stopConnection()throws IOException {
      in.close();
      out.close();
      clientSocket.close();
  }
  public static void main(String[] args) {
    try {
      int port = 6666;
      System.out.println("Starting connection....");
      startConnection(port);

    } catch (Exception e) {
      // TODO: handle exception
=======
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
      
>>>>>>> parent of b583ec1 (Test 01)
    }
  }
}