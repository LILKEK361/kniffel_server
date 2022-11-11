/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.net.Socket;
import java.util.*;




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
    public boolean success = false;
    

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
                            try 
                            {
                                game(inBuf);
                        
                            } catch (Exception e) {
                                // TODO: handle exception
                            }
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

    public void game(BufferedReader inBuf_game) throws Exception
    {   

        HashMap<Integer, Integer> rolled_dices = new HashMap<Integer,Integer>(); 
        HashMap<Integer, Integer> wurf = new HashMap<Integer, Integer>();
        BufferedReader inBuf_dices =inBuf_game;
        
        try {
            
        
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

                
                

                for(int e = 0; e < 5; e++){

                    int roll = (int)(Math.random() * 5 + 1);
                    wurf.put(e, roll);

                }
                
                for(int j = 0; j < wurf.size(); j++)
                {
                    outBuf.println((Integer)wurf.get(j));


                };

                int dice_throws = 1;

                while(dice_throws < 3)
                {

                    dice_roll(inBuf_dices, wurf);
                    rolled_dices = wurf;
                    dice_throws += 1;

                    if(dice_throws == 3)
                    {
                        Thread.sleep(2000);
                        outBuf.println("The dices have fallen: ");
                        for(int f = 0; f < rolled_dices.size(); f++)
                        {

                            outBuf.println("[" + rolled_dices.get(f) + "]");

                        }
                        addtoDB(rolled_dices,inBuf_game);

                    }
                }

               


                ;

            }      
        } catch (Exception e) 
        {
                // TODO: handle exception
                outBuf.println("Errord during rolling: " + e.getMessage());
        }
    } 

    public void dice_roll(BufferedReader inBuf, HashMap<Integer,Integer> wurf) throws Exception
    {
            String im = "========================================";
            lines(im);
                
                outBuf.println("Which dice should be rerolled:");
                
                for(int m = 1; m < 6; m++)
                {
                    
                   
                    outBuf.println("[" + m + "]");
                }
                
                lines(im);
                
                

                String wurf_w;

                int new_roll;
                boolean right = false;
                
                while (( wurf_w = inBuf.readLine()) != null && right == false)
                {
                    if(wurf_w == "exit")
                    {

                        cSocket.close();

                    }
                    wurf_w = String.valueOf(wurf_w);
                    if(wurf_w == "skip" || wurf_w == "")
                    {
                        
                        right = false;

                    }
                    
                    
                    lines(im);
                    
                    for(int a = 0; a < wurf_w.length(); a ++ )
                    {

                       int würfel = Integer.parseInt(String.valueOf(wurf_w.charAt(a)));
                       if(würfel > 0 && würfel < 6)
                       {
                        // Minus one because array starts with 0;
                        new_roll = (int)(Math.random() * 5 + 1);
                        outBuf.println("Dice " + würfel + " was rerolled to: " + new_roll);
                        
                        würfel -= 1;
                        
                        wurf.put(würfel, new_roll);
                        
                        right = true;
                       }else
                       {

                        outBuf.println("This Dice is invald: " + würfel);

                       }
                        

                    } 

                    lines(im);
                    
                    outBuf.println("Current dices: ");
                    
                    
                    for(int a = 0; a < wurf.size();   a++ )
                    {

                        outBuf.println(wurf.get(a));

                    }

                    lines(im);
                    

                    break;

                    


                };

                lines(im);

            
            
        


    }
    
    public  void lines(String line)
    {

        for(int i = 0; i < line.length(); i++)
        {

            outBuf.print("=");

        }
        outBuf.println("");

    }
    
    public void addtoDB(HashMap<Integer, Integer> rolled_dices, BufferedReader inBuf_togameDB)
    {
        check(rolled_dices);



    }

    public  void check(HashMap<Integer, Integer> to_check) {

        
       
        
    }
    
}
