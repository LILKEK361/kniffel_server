package gamedb;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.CharBuffer;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import javax.management.modelmbean.DescriptorSupport;

import gamedb.*;




public class GamePlay{
    
    private GameDB GameDB;
    public PrintWriter w;
    public BufferedReader inReader;
    public CharBuffer inCharbuffer = CharBuffer.allocate(1024);
    
    //Data
    public HashMap<String, Integer> player_1_sheet = new HashMap<String, Integer>();
    public HashMap<String, Integer> player_2_sheet = new HashMap<String, Integer>();
    public HashMap<String, Integer> player_3_sheet = new HashMap<String, Integer>();
    public HashMap<String, Integer> player_4_sheet = new HashMap<String, Integer>();
    public HashMap<String, Integer> player_5_sheet = new HashMap<String, Integer>();
    public HashMap<String, String> nicknames = new HashMap<String, String>();

    //Dice Saves and Choice
    public HashMap<Integer, Integer> dice_throw = new HashMap<Integer, Integer>();
    String throwed_dices;
    String options = "[1][2][3][4][5]";
    String choices;
    int choice;


    public String[] dice_sheet = {"1","2","3","4","5","6","Bonuspoints", "", "Double","Tripel","Quad","Kniffel","Full House","Small Street","Big Street","Final Points"};
    
    public Socket user_socket;
    public int player_counter = 0  ;
    public boolean error = false;
    public int dice_throws = 13;
    public int round = 1;
    String kokos = "==================================";


    

    public HashMap<String, Integer> get_data(HashMap<String, Integer> create_sheet)
    {

        for(int i = 0; i < dice_sheet.length; i++)
        {

            create_sheet.put(dice_sheet[i], null);

        }




        return create_sheet;
    }



    public void game(GameDB gameDB) throws Exception
    {   
        String input_check = "";

        this.GameDB = gameDB;

        player_1_sheet = get_data(player_1_sheet);
        player_2_sheet = get_data(player_2_sheet);
        player_3_sheet = get_data(player_3_sheet);
        player_4_sheet = get_data(player_4_sheet);
        player_5_sheet = get_data(player_5_sheet);

        

        for(DataConnectedUser n : GameDB.connectedUserList) 
        {
            this.user_socket = n.getSocket();
            inReader =  new BufferedReader(new InputStreamReader(user_socket.getInputStream()));
            w = new PrintWriter(this.user_socket.getOutputStream(), true);

            w.println("Pls press Enter to Enter");
            if((input_check = inReader.readLine()) != null)
            {
                
                player_counter ++;
            }
            String player_sheet = "player_"+player_counter+"_sheet";
            nicknames.put(GameDB.getConnectedUserNichname(user_socket),player_sheet );

        }
        if(player_counter <= 5)
        {

            GameDB.sendln("Current players: "+ GameDB.getNumberOfConnectedUsers() + "/" + GameDB.getNumberOfConnectedUsers() );
            for(DataConnectedUser k : GameDB.connectedUserList)
            {

                GameDB.sendln(k.getNickname() + " is online");

            }

            
            GameDB.sendln(kokos);
            




            while(dice_throws > 0)
            {

                for(DataConnectedUser Bananaboy : GameDB.connectedUserList)
                {   
                    this.user_socket = Bananaboy.getSocket();
                    InputStreamReader r = new InputStreamReader(user_socket.getInputStream());
                    inReader =  new BufferedReader(r);
                    w = new PrintWriter(this.user_socket.getOutputStream(), true);
                    
                    GameDB.sendln("THROW: " + round +" || THROWS LEFT: " + dice_throws);
                    GameDB.sendln("Player:" + GameDB.getConnectedUserNichname(user_socket) );
                    GameDB.sendln(kokos);

                    for(int d = 0; d < 5; d++)
                    {

                        int dice = (int)(Math.random() * 5 + 1);
                        dice_throw.put(d, dice);

                    }

                    throwed_dices = "["+  dice_throw.get(0) + "] " +"[" + dice_throw.get(1) + "] "+ "[" + dice_throw.get(2) + "] "+ "["+ dice_throw.get(3) + "] "+"[" + dice_throw.get(4) + "]";
                    
                    GameDB.sendln("Current dices:");
                    GameDB.sendln(throwed_dices);
                    GameDB.sendln(kokos);
                    GameDB.sendln("Which dice should be rerolled:");
                    GameDB.sendln(options);
                    Boolean ok = false;
                    while((choices = inReader.readLine()) != null && !error && ok == false)
                    {

                        choices = choices.trim();
                        GameDB.sendln(kokos);

                        
                        if(choices.equals("") && choices.equals(null))
                        {
                            ok = true;
                            break;
                        }
                        if(choices.length() <= 5)
                        {
                            
                            for(int l = 0; l < choices.length(); l++)
                            {

                                int btw = (int)(Math.random()*5+1);
                                int ch = Integer.parseInt(String.valueOf(choices.charAt(l)));
                                
                                GameDB.sendln("Dice [" + ch + "] was rerolled to: " + btw);
                                
                                ch -= 1;
                                dice_throw.put(ch,btw);

                                
                                
                            }
                            
                            ok = true;
                            break;

                        }else
                        {

                            w.println("You tipped to many!!");

                        }
                        ok = true;
                    }
                    throwed_dices = "["+  dice_throw.get(0) + "] " +"[" + dice_throw.get(1) + "] "+ "[" + dice_throw.get(2) + "] "+ "["+ dice_throw.get(3) + "] "+"[" + dice_throw.get(4) + "]";
                    GameDB.sendln("Current dices:");
                    GameDB.sendln(throwed_dices);
                    GameDB.sendln(kokos);
                    GameDB.sendln("Which dice should be rerolled:");
                    GameDB.sendln(options);
                    ok = false;
                    while((choices = inReader.readLine()) != null && !error && ok == false)
                    {

                        choices = choices.trim();
                        GameDB.sendln(kokos);

                        
                        if(choices.equals("") && choices.equals(null))
                        {
                            ok = true;
                            break;
                        }
                        if(choices.length() <= 5)
                        {
                            
                            for(int l = 0; l < choices.length(); l++)
                            {

                                int btw = (int)(Math.random()*5+1);
                                int ch = Integer.parseInt(String.valueOf(choices.charAt(l)));
                                
                                GameDB.sendln("Dice [" + ch + "] was rerolled to: " + btw);
                                
                                ch -= 1;
                                dice_throw.put(ch,btw);

                                
                                
                            }
                            
                            ok = true;
                            break;

                        }else
                        {

                            w.println("You tipped to many!!");

                        }
                        ok = true;
                    }
                    throwed_dices = "["+  dice_throw.get(0) + "] " +"[" + dice_throw.get(1) + "] "+ "[" + dice_throw.get(2) + "] "+ "["+ dice_throw.get(3) + "] "+"[" + dice_throw.get(4) + "]";
                    GameDB.sendln("Current dices:");
                    GameDB.sendln(throwed_dices);
                    GameDB.sendln(kokos);
                    GameDB.sendln("Which dice should be rerolled:");
                    GameDB.sendln(options);
                    ok = false;
                    while((choices = inReader.readLine()) != null && !error && ok == false)
                    {

                        choices = choices.trim();
                        GameDB.sendln(kokos);

                        
                        if(choices.equals("") && choices.equals(null))
                        {
                            ok = true;
                            break;
                        }
                        if(choices.length() <= 5)
                        {
                            
                            for(int l = 0; l < choices.length(); l++)
                            {

                                int btw = (int)(Math.random()*5+1);
                                int ch = Integer.parseInt(String.valueOf(choices.charAt(l)));
                                
                                GameDB.sendln("Dice [" + ch + "] was rerolled to: " + btw);
                                
                                ch -= 1;
                                dice_throw.put(ch,btw);

                                
                                
                            }
                            
                            ok = true;
                            break;

                        }else
                        {

                            w.println("You tipped to many!!");

                        }
                        ok = true;
                    }




                    check(dice_throw);

                    
                    


                }
               
                round ++;
                dice_throws--;
               

            }


        }


    }

    public void check(HashMap<Integer, Integer> dice_throw) throws Exception
    {   

        int ones = 0;
        int tows = 0;
        int threes = 0;
        int fours = 0;
        int fives = 0;
        int sixs = 0;

        for(int w = 0; w < dice_throw.size(); w++)
        {

            if(dice_throw.get(w) == 1  )
            {

                ones++;

            }else if(dice_throw.get(w) == 2 )
            {

                tows++;

            }else if(dice_throw.get(w) == 3 )
            {

                threes++;

            }else if(dice_throw.get(w) == 4 )
            {

                fours++;

            }else if(dice_throw.get(w) == 5 )
            {

                fives++;

            }else if(dice_throw.get(w) == 6  )
            {

                sixs++;

            }

        }
        
        GameDB.sendln("The dices have fallen:");
        
        GameDB.sendln("Singles:");
        HashMap<Integer, Integer> single_points = singels(ones,tows,threes,fours,fives,sixs);
        
        GameDB.sendln("Combos:");
        HashMap<String, Integer> combo_points =  combos(ones,tows,threes,fours,fives,sixs);

        

        add_to_db(single_points, combo_points);

    }

    public HashMap<Integer, Integer> singels(int ones, int  tows, int threes,int fours,int fives,int sixs) throws Exception
    {
        HashMap<Integer, Integer> singel_points = new HashMap<Integer, Integer>();
        if(ones > 0)
        {
            GameDB.sendln("[1] = " + ones * 1 + "P");
            singel_points.put(0,ones * 1);
        }else {singel_points.put(0, null);}
        if(tows > 0)
        {
            GameDB.sendln("[2] = " + tows * 2 + "P");
            singel_points.put(1, tows * 2);
        }else {singel_points.put(1, null);}
        if(threes > 0)
        {
            GameDB.sendln("[3] = " + threes * 3 + "P");
            singel_points.put(2,threes * 3);
        }else {singel_points.put(2, null);}
        if(fours > 0)
        {
            GameDB.sendln("[4] = " + fours * 4 + "P");
            singel_points.put(3,fours * 4);
        }else {singel_points.put(3, null);}
        if(fives > 0)
        {
            GameDB.sendln("[5] = " + fives * 5 + "P");
            singel_points.put(4,fives * 5);;
        }else {singel_points.put(4, null);}
        if(sixs > 0)
        {
            GameDB.sendln("[6] = " + sixs * 6 + "P");
            singel_points.put(5,sixs * 6);
        }else {singel_points.put(5, null);}

        return singel_points;
        
    }

    public HashMap<String, Integer> combos(int ones, int  tows, int threes,int fours,int fives,int sixs) throws Exception{
        
        HashMap<Integer, Integer> numbers = new HashMap<Integer, Integer>();
        HashMap<String, Integer> combo_points = new HashMap<String, Integer>();
        numbers.put(0, ones);
        numbers.put(1, tows);
        numbers.put(2, threes);
        numbers.put(3, fours);
        numbers.put(4, fives);
        numbers.put(5, sixs);
        int Double = 0;
        int Triple = 0;
        int Quad = 0;
        for(int banane = 0; banane < numbers.size(); banane++)
        {

            if(numbers.get(banane) == 2)
            {
                Double = 1 * ones  + 2 * tows  + 3 * threes + 4 * fours + 5 * fives + 6 * sixs;
                GameDB.sendln("[Double]  = " + (int)Double + "P");
                combo_points.put("Double", Double);

            }

            
            if(numbers.get(banane) == 3)
            {
                Triple = 1 * ones  + 2 * tows  + 3 * threes + 4 * fours + 5 * fives + 6 * sixs;
                GameDB.sendln("[Triple]  = " + (int)Triple + "P" );
                combo_points.put("Triple", Triple);

            }
            
            if(numbers.get(banane) == 4)
            {
                Quad = 1 * ones  + 2 * tows  + 3 * threes + 4 * fours + 5 * fives + 6 * sixs;
                GameDB.sendln("[Quad]  = " + Quad + "P" );
                combo_points.put("Quad", Quad);

            }
            if(numbers.get(banane) == 5)
            {    
                //Kniffel is different you see it? 
                
                GameDB.sendln("{KNIFFEL}  = 50P || We bal" );
                GameDB.sendln("");
                combo_points.put("KNIFFEL", 50);

            }


        }


        if(ones== 1 && tows == 1 && threes== 1 && fours== 1 && fives== 1)
        {

            GameDB.sendln("[Small Street] = 30P");
            combo_points.put("Small Street", 30);


        }

        if(tows == 1 && threes== 1 && fours== 1 && fives== 1 && sixs== 1)
        {

            GameDB.sendln("[Big Street] = 40P");
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

                        GameDB.sendln("[Full House] = 25P");
                        combo_points.put("Full House", 25);
                        

                    }

                }

            }

        }

       
    
        return combo_points;

        
    }

    public void add_to_db(HashMap<Integer, Integer> singles, HashMap<String, Integer> combos) throws Exception
    {   
        GameDB.sendln(kokos);
        GameDB.sendln(GameDB.getConnectedUserNichname(user_socket) + " + Dicesheet");
        GameDB.sendln("");
        int max_length = 15;
        String empty = "";
        int points;
        boolean cookie = false;
        String choice;
        switch (nicknames.get(GameDB.getConnectedUserNichname(user_socket))) 
        {
           
            case "player_1_sheet":
                for (String s : dice_sheet)
                {
                    for(int o = s.length();  o < max_length; o++)
                    {empty += " ";}
                    if(player_1_sheet.get(s) != null){points = player_1_sheet.get(s);}else{points = 0;}
                    if(s.equals("Bonuspoints") || s.equals("Final Points")){GameDB.sendln(kokos); };
                    GameDB.sendln("["+ s + "]" + empty +  "|| " + points);
                    
                    
                    empty = "";
                }
                while((choice = inReader.readLine()) != null )
                {

                    char[]  bytes = choice.toCharArray();
                    if(bytes.length == 1)
                    {
                       while(cookie == false)
                       {
                            
                                int dice = bytes[0] -1;
                            
                                if(options.contains(String.valueOf(bytes[0])) && singles.get(dice) != null && player_1_sheet.get(String.valueOf(bytes[0])) == null)
                                {
                                    
                                    player_1_sheet.put(String.valueOf(bytes[0]), singles.get(dice));
                                    cookie = true;

                                }else
                                {

                                    w.println("Thats not a valid dice:");

                                }

                            
                        }
                    }else if(bytes.length > 1)
                    {
                        while(cookie == false)
                        {
                            String ppp = bytes.toString();

                            if(options.contains(ppp) && combos.get(ppp) != null && player_1_sheet.get(ppp) == null )
                            {

                                player_1_sheet.put(ppp, combos.get(ppp));

                                cookie = true;
                            }




                        }

                    }



                }
                break;
            case "player_2_sheet":
                break;
            case "player_3_sheet":
                break;
            case "player_4_sheet":
                break;
            case "player_5_sheet":
                break;
        
        }
        GameDB.sendln("Waiting....");
        GameDB.sendln(kokos);
        Thread.sleep(5000);
    }
}



