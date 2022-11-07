import java.net.*;

import java.util.*;
import java.io.*;
import java.net.*;


public class App {
  private ServerSocket serverSocket;
  private Socket clientSocket;
  private PrintWriter out;
  private BufferedReader in;

  public void start(int port) throws IOException {
      serverSocket = new ServerSocket(port);
      clientSocket = serverSocket.accept();
      
      out = new PrintWriter(clientSocket.getOutputStream(), true);
      in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
      
      String greeting = in.readLine();
          if ("hello server".equals(greeting)) {
              out.println("hello client");
          }
          else {
              out.println("unrecognised greeting");
          }
  }

  public void stop() throws IOException {
      in.close();
      out.close();
      clientSocket.close();
      serverSocket.close();
  }
  public static void main(String[] args) {
      try {
        App server=new App();
        server.start(6666);
    
      } catch ( IOException e) {
        // TODO: handle exception
      }
    }
      
}