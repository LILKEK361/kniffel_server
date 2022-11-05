package Game;


import java.net.*;
import java.io.*;



public class Server {

	private ServerSocket server;
	public Server(int port) {
		try {
			server = new ServerSocket(port);
			server.setSoTimeout(1000000);
		} catch (SocketException e) {
			
			e.printStackTrace();
		}catch (IOException d) {
			d.printStackTrace();
		}
		
	}

	public void start(){

		while(true){
			try{
				System.out.println("Waiting at:" + server.getLocalPort());
				Socket client = server.accept();
				
				DataInputStream in = new DataInputStream( client.getInputStream());
				
				DataOutputStream output = new DataOutputStream(client.getOutputStream());
				while(true){
				String order = in.readUTF();

				
					switch (order) {
						case "leave":
							output.writeUTF("Aufwiedersehen");
							client.close();
							break;
									
						case "Leave":
							output.writeUTF("Aufwiedersehen");
							client.close();
							break;
						
						case ".start":
							output.writeUTF("Game is starting...");
					}}


				
				
				
				
				

				

			}catch(Exception f){

				f.printStackTrace();
				break;

			}}

	}
   public static void main(String[] args){
		Server s = new Server(1227);
		while(true){

			

		}
   }
   
}