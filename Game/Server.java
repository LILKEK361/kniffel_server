package Game;


import java.net.*;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;
import java.util.Map;
import java.io.*;



public class Server {


	
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


class ServerClientThread extends Thread {
  Socket serverClient;
  int clientNo;
  int squre;
  List<String> client_list =new ArrayList<String>(); 
  HashMap<Integer, Integer> user_dic = new HashMap<Integer, Integer>();
  
  user_dic.put(clientNo, serverClient);


  ServerClientThread(Socket inSocket,int counter){
    serverClient = inSocket;
    clientNo=counter;
  }
  public void run(){
    try{
      
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

		client_list.add(clientMessage);
		break;


      }
	  while(!clientMessage.equals(".break")){
        
		
		clientMessage=inStream.readUTF();
		if(clientMessage.equals(".user") || clientMessage.equals(".users")){

			for (String i : client_list) {
				outStream.writeUTF(i);
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
  }
  public void clients(){



  }

}

