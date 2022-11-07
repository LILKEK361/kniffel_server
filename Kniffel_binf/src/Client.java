

/**
 * Client
*/

import java.net.*;
import java.io.*;
import java.io.DataInputStream;
import java.util.*;


public class Client {
  private Socket clientSocket;
  private PrintWriter out;
  private BufferedReader in;

  public void startConnection (int port) throws IOException {
      clientSocket = new Socket("localhost", port);
      out = new PrintWriter(clientSocket.getOutputStream(), true);
      in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
  }

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
    }
  }
}