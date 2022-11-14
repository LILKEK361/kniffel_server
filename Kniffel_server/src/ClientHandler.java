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

import javax.print.DocFlavor.STRING;

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
    public HashMap<Integer, Integer> singel_points = new HashMap<Integer, Integer>();
    public HashMap<String, Integer> combo_points   = new HashMap<String, Integer>();
   
    

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
                        case "start" :
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
                    outBuf.print("["+ (Integer)wurf.get(j) + "] ");


                };
                outBuf.println("");
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

                            outBuf.print("[" + rolled_dices.get(f) + "] ");

                        }
                        outBuf.println("");
                        lines(im);
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
                    
                   
                    outBuf.print("[" + m + "] ");
                }
                outBuf.println("");
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
                    if(wurf_w == "")
                    {
                        
                        right = false;

                    }
                    
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

                        outBuf.print("[" + wurf.get(a) + "] ");

                    }
                    outBuf.println();

                   
                    

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
        check(rolled_dices,inBuf_togameDB);

    }

    public  void check(HashMap<Integer, Integer> to_check, BufferedReader inBuf_check) 
    {

        String im = "========================================";
        int one = 0;
        int two = 0;
        int three = 0;
        int four = 0;
        int five = 0;
        int six = 0;
        String desicon = "";
        try 
        {

            for(int m = 0; m < to_check.size(); m++)
            {
                int roll = to_check.get(m);
                if(roll == 1){

                    one += 1;


                }
                if(roll == 2){

                    two += 1;


                }
                if(roll == 3){

                    three += 1;


                }
                if(roll == 4){

                    four += 1;


                }
                if(roll == 5){

                    five += 1;


                }
                if(roll == 6){

                    five += 1;


                }

            };
       
            
            
            outBuf.println("You have:");
            
            single( one, two, three, four, five, six );
            combos(one, two, three, four, five, six );
            lines(im);
            outBuf.println("You'r choice: Single or Combo? ");
            boolean go_on = false;
            String single_desicon;
            String combo_desicon;
            int annoying_counter = 0;
            while ((desicon = inBuf_check.readLine()) != null && go_on == false   )
            {
                
                if(desicon.equals("Single") || desicon.equals("single"))
                {   outBuf.println("Which Single:");
                    while (( single_desicon = inBuf_check.readLine()) != null &&  go_on == false  )
                    {
                        int int_desicon = Integer.valueOf(single_desicon);   
                        if(singel_points.containsKey(int_desicon) && singel_points.get(int_desicon) != null )
                        {

                            outBuf.println(int_desicon + ": for " + singel_points.get(int_desicon) * int_desicon + "P");
                            go_on = true;

                        }else if(!singel_points.containsKey(int_desicon) || singel_points.get(int_desicon) == null)
                        {
        
                           outBuf.println("You must take another option");
                           annoying_counter += 1;
        
                        }else if(annoying_counter > 4)
                        {   //Bob trys to ruin the fun dont be like Bob or we will find you
                            outBuf.println("What is your problem?");
                            cSocket.close();
        
                        }
                    }
                }else if(desicon.equals("Combo") || desicon.equals("combo"))
                {   
                    outBuf.println("Which combo:");
                    while (( combo_desicon = inBuf_check.readLine()) != null &&  go_on == false  )
                    {
                        
                        if(combo_points.containsKey(combo_desicon) )
                        {

                            outBuf.println(combo_desicon + ": for " + combo_points.get(combo_desicon) + "P");
                            go_on = true;

                        }else if(!combo_points.containsKey(combo_desicon))
                        {
        
                           outBuf.println("You must take another option");
                           annoying_counter += 1;
        
                        }else if(annoying_counter > 4)
                        {   
                            outBuf.println("What is your problem?");
                            cSocket.close();
        
                        }
                    }
                }else if(annoying_counter > 4)
                {   
                    outBuf.println("What is your problem?");
                    cSocket.close();

                }
                else
                {

                   outBuf.println("You must take another option");
                   annoying_counter += 1;

                }
                

            }
            lines(im);



            
        } catch (Exception e) 
        {
            // TODO: handle exception
        }
    }
        
    //checks single points
    public void single( int one, int two, int three, int four, int five, int six)
    {   
        
        singel_points.put(0, 666);

        outBuf.println("Singles:");
        if(one > 0)
        {
            outBuf.println("[1] = " + one + "P");
            singel_points.put(1, one);
        }else{singel_points.put(1, null);}
        if(two > 0)
        {
                outBuf.println("[2] = " + two * 2 + "P");
                singel_points.put(2, two);
        }else{singel_points.put(2, null);}
        if(three > 0)
        {
                outBuf.println("[3] = " + three * 3 + "P");
                singel_points.put(3, three);
        }else{singel_points.put(3, null);}
        if(four > 0)
        {
                outBuf.println("[4] = " + four * 4 + "P");
                singel_points.put(4, four);
        }else{singel_points.put(4, null);}
        if(five > 0)
        {
             outBuf.println("[5] = " + five * 5 + "P");
             singel_points.put(5, five);
        }else{singel_points.put(5, null);}
        if(six > 0)
        {
                outBuf.println("[6] = " + six * 6 + "P");
                singel_points.put(6, six);
        }else{singel_points.put(6, null);}
      
        
       
    }
    //checks if you have a combo
    public void combos(int one, int two,int three, int four, int five, int six)
    {
        HashMap<Integer, Integer> numbers = new HashMap<Integer, Integer>();
        outBuf.println("Combos:");
        numbers.put(0, one);
        numbers.put(1, two);
        numbers.put(2, three);
        numbers.put(3, four);
        numbers.put(4, five);
        numbers.put(5, six);
        int Double = 0;
        int Triple = 0;
        int Quad = 0;
        

        for(int banane = 0; banane < numbers.size(); banane++)
        {

            if(numbers.get(banane) == 2)
            {
                Double = 1 * one   + 2 * two  + 3 * three  + 4 * four  + 5 * five  + 6 * six ;
                outBuf.println("[Double]  = " + (int)Double + "P");
                combo_points.put("Double", Double);

            }

            
            if(numbers.get(banane) == 3)
            {
                Triple = 1 * one   + 2 * two  + 3 * three  + 4 * four  + 5 * five  + 6 * six ;
                outBuf.println("[Triple]  = " + (int)Triple + "P" );
                combo_points.put("Triple", Triple);

            }
            
            if(numbers.get(banane) == 4)
            {
                Quad = 1 * one   + 2 * two  + 3 * three  + 4 * four  + 5 * five  + 6 * six ;
                outBuf.println("[Quad]  = " + Quad + "P" );
                combo_points.put("Quad", Quad);

            }
            if(numbers.get(banane) == 5)
            {    
                //Kniffel is different you see it? 
                
                outBuf.print("{KNIFFEL}  = 50P || We bal" );
                outBuf.println("");
                combo_points.put("KNIFFEL", 50);

            }


        }


        if(one == 1 && two == 1 && three == 1 && four == 1 && five == 1)
        {

        outBuf.println("[Small Street] = 30P");
        combo_points.put("Small Street", 30);


        }

        if(two == 1 && three == 1 && four == 1 && five == 1 && six == 1)
        {

        outBuf.println("[Big Street] = 40P");
        combo_points.put("Big Street", 40);


        }

        for(int Kong = 0; Kong < numbers.size(); Kong++)
        {

            if(numbers.get(Kong) == 3)
            {

                for(int strong = 0; strong < numbers.size(); strong++)
                {

                    if(numbers.get(strong) == 2)
                    {

                        outBuf.println("[Full House] = 25P");
                        combo_points.put("Full House", 25);
                        

                    }

                }

            }

        }

       
    
    }
    
    

}
