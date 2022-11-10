/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.*;

import Game.kniffel;
import gamedb.*;

/**
 * Class to handle client connections
 *
 * @author carst
 */
public class ClientHandler implements Runnable {

    private static final ArrayList<DataConnectedUser> DataConnectedUser = null;
    private boolean shutdown = false;
    private final Socket cSocket;
    private final GameDB gameDB;
    private PrintWriter outBuf = null;

    ClientHandler(Socket cSocket, GameDB gameDB) {
        this.cSocket = cSocket;
        this.gameDB = gameDB;
    }

    @Override
    public void run() {
        try {

            BufferedReader inBuf = new BufferedReader(new InputStreamReader(cSocket.getInputStream()));
            outBuf = new PrintWriter(this.cSocket.getOutputStream(), true);

            outBuf.println("==== Welcome at kniffel server ====");
            
            try {
                gameDB.addConnectedUser(cSocket);
                
                outBuf.println("Type help for further information.");
                outBuf.println("server: ok");
                outBuf.println("");

                String cInString;

                while (((cInString = inBuf.readLine()) != null) && (shutdown == false)) {

                    cInString = cInString.trim();
                    String parsedData[] = cInString.split(" ");

                    switch (parsedData[0]) {
                        case "help":
                            handleCmdHelp();
                            break;
                        case "rename":
                            handleCmdRename(cInString);
                            break;
                        case "users":
                            handleCmdUsers();
                            break;
                        case "user" : 
                            handleCmdUsers();
                            break;
                        case "identify":
                            handleCmdIdentify();
                            break;
                        case "message":
                            handleCmdMessage(cInString);
                            break;
                        case "exit":
                            handleCmdExit();
                            shutdown = true;
                            break;
                        case "start":
                            game();
                            break;
                        default:
                            outBuf.println("error: unknown command " + parsedData[0]);
                    }
                    outBuf.println("server: ok");
                    outBuf.println("");
                }
                gameDB.deleteConnectedUser(cSocket);
                cSocket.close();

            } catch (GameDBToMuchPlayersException ex) {
                /**
                 * if addConnectedUser of game database throws this error then
                 * client has to deny new users
                 */
                outBuf.println("error: to much users are be connected");
                outBuf.println("byebye");
                outBuf.println("server: okay");
                cSocket.close();
            }
        } catch (IOException ex) {
            System.out.println("exception: " + ex.toString());
        }
    }

    /**
     * method to handle exit command
     */
    private void handleCmdExit() {
        outBuf.println("server: exit");
        outBuf.println("byebye");
    }

    /**
     * method to handle help command
     */
    private void handleCmdHelp() {
        outBuf.println("server: help");
        outBuf.println("exit - close server connection");
        outBuf.println("help - informs about all server commands");
        outBuf.println("identify - show your identification");
        outBuf.println("message <text> - send <text> to all connected users");
        outBuf.println("rename <nickname> - rename user to <nickname>");
        outBuf.println("users - show all users");
    }

    /**
     * method to handle rename command
     */
    private void handleCmdRename(String cmdString) {
        String trimedCmdString = cmdString.replaceFirst("rename", "");
        trimedCmdString = trimedCmdString.trim();
        try {
            outBuf.println("server: rename");
            gameDB.renameConnectedUser(cSocket, trimedCmdString);
        } catch (GameDBUserExistsException ex) {
            outBuf.println("error: user not exists");
        } catch (GameDBUnsupportedCharacters ex) {
            outBuf.println("error: only alphanumeric characters are allowed in the nickname.");
        }
    }

    /**
     * method to handle users command
     */
    private void handleCmdUsers() {
        try {
            outBuf.println("server: users");
            for (int index = 0; index < gameDB.getNumberOfConnectedUsers(); index++) {
                outBuf.println(gameDB.getConnectedUserNichname(index));
            }
        } catch (GameDBUnknownUserException ex) {
            outBuf.println("error: user not exists");
        }
    }

    /**
     * method to handle message command
     * @param message
     */
    private void handleCmdMessage(String message) {

        String trimedInputString = message.replaceFirst("message", "");
        trimedInputString = trimedInputString.trim();

        try {
            outBuf.println("server: message");
            for (int index = 0; index < gameDB.getNumberOfConnectedUsers(); index++) {

                Socket socket = gameDB.getConnectedUserSocket(index);

                if (socket != cSocket) {
                    PrintWriter sLocalOutString = new PrintWriter(socket.getOutputStream(), true);

                    sLocalOutString.println("server: message from " + gameDB.getConnectedUserNichname(cSocket));
                    sLocalOutString.println(trimedInputString);
                    sLocalOutString.println("server: ok");
                    sLocalOutString.println("");
                }
            }
        } catch (GameDBUnknownUserException ex) {
            outBuf.println("error: user not exists");
        } catch (IOException ex) {
            System.out.println("exception: " + ex.toString());
        }
    }

    /**
     * method to handle identity command
     */
    private void handleCmdIdentify() 
    {
        try 
        {
            outBuf.println("server: identify");
            outBuf.println(gameDB.getConnectedUserNichname(cSocket));
        } catch (GameDBUnknownUserException ex) 
        {
            outBuf.println("error: user not exists");
        }
    }

    public void game()
    {
        String im = "========================================";
        outBuf.println("Current player: ");
        for (int i = 0; i < gameDB.getNumberOfConnectedUsers(); i++)
        {

            outBuf.println(gameDB.getConnectedUserNichname(i));


        }
        
        

        for(int i = 0; i < gameDB.getNumberOfConnectedUsers(); i++) 
        {

            lines(im);
            outBuf.println("Throw || Player: " + gameDB.getConnectedUserNichname(i));
            lines(im);

            int würfel1 = (int)(Math.random() * 5 + 1);
            int würfel2 = (int)(Math.random() * 5 + 1);
            int würfel3 = (int)(Math.random() * 5 + 1);
            int würfel4 = (int)(Math.random() * 5 + 1);
            int würfel5 = (int)(Math.random() * 5 + 1);

            ArrayList<Integer> wurf = new ArrayList<Integer>();

            wurf.add(würfel1);
            wurf.add(würfel2);
            wurf.add(würfel3);
            wurf.add(würfel4);
            wurf.add(würfel5);
            
            for(int j = 0; j < wurf.size(); j++)
            {
                outBuf.println((Integer)wurf.get(j));


            };
            



        }
        
        


    }
    
    public void lines(String line)
    {

        for(int i = 0; i < line.length(); i++)
        {

            outBuf.print("=");

        }
        outBuf.println("");

    }
    
}
