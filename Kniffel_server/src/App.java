import java.io.*;
import java.lang.*;
import java.net.*;
import java.text.*;
import java.util.Date;




public class App implements random_data {
    
    
    
    public static void main(String[] args) throws IOException 
	{
        
        ServerSocket myserverSocket = new ServerSocket(3333);
		// getting client request
        while (true)
        // running infinite loop 
		{
			Socket mynewSocket = null;
			
			try
			{
				// mynewSocket object to receive incoming client requests
				mynewSocket = myserverSocket.accept();
                
				
				System.out.println("A new connection identified : " + mynewSocket);
                // obtaining input and out streams
				DataInputStream ournewDataInputstream = new DataInputStream(mynewSocket.getInputStream());
				DataOutputStream ournewDataOutputstream = new DataOutputStream(mynewSocket.getOutputStream());
				
				System.out.println("Thread assigned");

				Thread myThread = new ClientHandler(mynewSocket, ournewDataInputstream, ournewDataOutputstream);
				// starting
				myThread.start();
				
			}
			catch (Exception e){
				mynewSocket.close();
				e.printStackTrace();
			}
		}
	}
}


class ClientHandler extends Thread 
{
	
	final DataInputStream ournewDataInputstream;
	final DataOutputStream ournewDataOutputstream;
	final Socket mynewSocket;
	public  int clientscount = 0;// Hr.Pieper fragen
    public int maxclients = 5;// Hr.Pieper fragen

	// Constructor
	public ClientHandler(Socket mynewSocket, DataInputStream ournewDataInputstream, DataOutputStream ournewDataOutputstream)
	{
		this.mynewSocket = mynewSocket;
		this.ournewDataInputstream = ournewDataInputstream;
		this.ournewDataOutputstream = ournewDataOutputstream;
        clientscount++;
	}

	@Override
	public void run()
	{
		String receivedString;
		String stringToReturn;
		while (true)
		{
			try {
				ournewDataOutputstream.writeUTF("Choose: [Game | commands | user]..\n"+
							"Or Exit");
				
				// getting answers from client
				receivedString = ournewDataInputstream.readUTF();
				
				if(receivedString.equals("Exit"))
				{
					System.out.println("Client " + this.mynewSocket + " sends exit...");
					System.out.println("Connection closing...");
					this.mynewSocket.close();
					System.out.println("Closed");
					break;
				}else if(receivedString.equals("commands"))
                {
                    ournewDataOutputstream.writeUTF("List of commands:\n"+"commands\n"+"Game\n" );
                    
                }else if(receivedString.equals("user"))
                {
                    ournewDataOutputstream.writeUTF("Du bist alleine" );
                    
                }
                
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		try
		{
			// closing resources
			this.ournewDataInputstream.close();
			this.ournewDataOutputstream.close();
			
		}catch(IOException e){
			e.printStackTrace();
		}
	}
}
