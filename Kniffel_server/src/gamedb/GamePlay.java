package gamedb;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;

import gamedb.*;




public class GamePlay {
    
    private GameDB GameDB;
    public PrintWriter w;
    public BufferedReader inReader;
    
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
    String options = "[1] [2] [3] [4] [5]";
    String choices;


    public String[] dice_sheet = {"1","2","3","4","5","6","Bonuspoints","Double","Tripel","Quad","Kniffel","Full House","Small Street","Big Street","Final Points"};
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
        String input_check;

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
            nicknames.put(GameDB.getConnectedUserNichname(user_socket), "player_%d_sheet".formatted(player_counter));

        }
        if(player_counter <= 5)
        {

            GameDB.sendln("Current player: 5/5");
            for(DataConnectedUser k : GameDB.connectedUserList)
            {

                GameDB.sendln(k.getNickname() + " is online");

            }

            
            GameDB.sendln(kokos);;
            




            while(dice_throws > 0 && !error)
            {

                for(DataConnectedUser Bananaboy : GameDB.connectedUserList)
                {   
                    this.user_socket = Bananaboy.getSocket();
                    inReader =  new BufferedReader(new InputStreamReader(user_socket.getInputStream()));
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
                    
                    GameDB.sendln(throwed_dices);
                    GameDB.sendln(kokos);
                    GameDB.sendln("Which dice should be rerolled:");
                    GameDB.sendln(options);
                    boolean ok = false;
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

                                int btw = (int)(Math.random()*5*1);
                                int ch = Integer.parseInt(String.valueOf(choices.charAt(l)));
                                
                                GameDB.sendln("Dice [" + ch + "] was rerolled to :" + btw);
                                
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

                                int btw = (int)(Math.random()*5*1);
                                int ch = Integer.parseInt(String.valueOf(choices.charAt(l)));
                                
                                GameDB.sendln("Dice [" + ch + "] was rerolled to :" + btw);
                                
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
                    }                    GameDB.sendln(throwed_dices);
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

                                int btw = (int)(Math.random()*5*1);
                                int ch = Integer.parseInt(String.valueOf(choices.charAt(l)));
                                
                                GameDB.sendln("Dice [" + ch + "] was rerolled to :" + btw);
                                
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
                break;

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

        singels(ones,tows,threes,fours,fives,sixs);


    }

    public void singels(int ones, int  tows, int threes,int fours,int fives,int sixs)
    {



    }
}