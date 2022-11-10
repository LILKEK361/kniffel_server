import java.io.*;

import java.net.*;





public class App  {
    
    public static int counter = 0;
    
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
				if(counter < 5)
				{
				mynewSocket = myserverSocket.accept();
				counter ++;
				
				System.out.println("A new connection identified : " + mynewSocket);
                // obtaining input and out streams
				DataInputStream ournewDataInputstream = new DataInputStream(mynewSocket.getInputStream());
				DataOutputStream ournewDataOutputstream = new DataOutputStream(mynewSocket.getOutputStream());
				
				System.out.println("Thread assigned");

				Thread myThread = new ClientHandler(mynewSocket, ournewDataInputstream, ournewDataOutputstream,counter);
				// starting
				
					if(counter == 5)
					{
					myThread.start();
					}
				}else{

					mynewSocket = myserverSocket.accept();
					DataOutputStream ournewDataOutputstream = new DataOutputStream(mynewSocket.getOutputStream());
					ournewDataOutputstream.writeUTF("5/5");
					mynewSocket.close();

				}
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
	

	public int maxclients = 5;
	final DataInputStream ournewDataInputstream;
	final DataOutputStream ournewDataOutputstream;
	final Socket mynewSocket;
	public int counter;


	// Constructor
	public ClientHandler(Socket mynewSocket, DataInputStream ournewDataInputstream, DataOutputStream ournewDataOutputstream, int counter)
	{
		this.mynewSocket = mynewSocket;
		this.ournewDataInputstream = ournewDataInputstream;
		this.ournewDataOutputstream = ournewDataOutputstream;
		this.counter = counter;
		
        
	}

	@Override
	public void run()
	{	
	
		String receivedString;
		String stringToReturn = "";
		while (true)
		{
			try {


				 
				ournewDataOutputstream.writeUTF("Hello: your are Client: %s ".formatted(counter));
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
					String user = String.format(" %s / %d", counter, maxclients);
                    ournewDataOutputstream.writeUTF(user);
                    
                }
                
			} catch (IOException e) {
				e.printStackTrace();
				counter = counter -1;
			}
		}
		
		try
		{
			// closing resources
			this.ournewDataInputstream.close();
			this.ournewDataOutputstream.close();
			counter = counter -1;
			
		}catch(IOException e){
			e.printStackTrace();
			counter = counter -1;
		}
	}
}
