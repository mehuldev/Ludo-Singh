package com.example.ludo;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class GameLogic extends View{

    HashMap<Integer,String> die_vals_to_image_map = new HashMap<Integer, String>(){{
        put(1,"one");
        put(2,"two");
        put(3,"three");
        put(4,"four");
        put(5,"five");
        put(6,"six");
    }};
    HashMap<Integer,String> turn_to_die_map = new HashMap<Integer, String >(){{
        put(0,"diceR");
        put(1,"diceG");
        put(2, "diceY");
        put(3, "diceB");
    }};
    HashMap<Character, Integer> player_to_turn_map = new HashMap<Character, Integer>(){{
        put('r', 0);
        put('g', 1);
        put('y', 2);
        put('b', 3);
    }};
    private final String blocks = "rgyb";
    private final boolean DEBUG = true;
    private String uid;
    private ArrayList<player> players = new ArrayList<player>(0);
    Context context;
    LinearLayout root;
    GameVarModel gameVarModel;

    public GameLogic(Context context, LinearLayout root) {
        super(context);
        this.context = context;
        this.root = root;
        gameVarModel = new GameVarModel();
    }

    public void disp_toast(int turn) {
        switch (turn)
        {
            case 0:
                Toast.makeText(context,
                        "Red's turn", Toast.LENGTH_SHORT).show();
                break;
            case 1:Toast.makeText(context,
                    "Green's turn", Toast.LENGTH_SHORT).show();
                break;
            case 2:Toast.makeText(context,
                    "Yellow's turn", Toast.LENGTH_SHORT).show();
                break;
            default:Toast.makeText(context,
                    "Blue's turn", Toast.LENGTH_SHORT).show();
        }
    }
    //generateAndDisp
    public void displayDice(String dice, View view) {
        int id = context.getResources().getIdentifier(dice,"id",context.getPackageName());
        int im_id = context.getResources().getIdentifier(die_vals_to_image_map.get(gameVarModel.getCurr_die_val()),
                "drawable",context.getPackageName());
        Animation aniRotate = AnimationUtils.loadAnimation(context,R.anim.rotate);
        ImageView img = (ImageView) view.findViewById(id);
        img.startAnimation(aniRotate);
        img.setImageResource(im_id);
    }
    public void playerTap (View v){
//        System.out.println(turn_pending);
        if(!gameVarModel.isTurn_pending())
            return;
        String cell_id = v.getResources().getResourceEntryName(v.getId());
        ArrayList<Integer> gotties_at_loc_index = players.get(gameVarModel.getCurr_player()).gotties_at_cell(cell_id);
//        System.out.println("Src: "+ cell_id+" "+curr_player+" "+gotty_idx);
        //Click on cell on which there is no gotty of particular color.
        if(gotties_at_loc_index.size() == 0)
            return;
        gotty curr_event = new gotty(players.get(gameVarModel.getCurr_player()).gotties.get(gotties_at_loc_index.get(0)));
        int curr_cell = Integer.parseInt(cell_id.substring(1));
        int next_cell = curr_cell+gameVarModel.getCurr_die_val();
//        System.out.println(curr_cell+" "+next_cell);
        //When gotty have to enter the home
        if(gameVarModel.getCurr_player() == player_to_turn_map.get(cell_id.charAt(0)) && next_cell > 7 && curr_cell < 7){
            next_cell += 6;
            if(next_cell > 18){
                players.get(gameVarModel.getCurr_player()).gotty_count--;
                //Erase the gotty
                //
                if(players.get(gameVarModel.getCurr_player()).gotty_count == 0)
                    System.out.println("Winner is player: "+gameVarModel.getCurr_player());
            }
        }
        //When gotty has to go to next block
        else if(next_cell > 13){
            curr_event.block++;
            curr_event.block %= 4;
            next_cell -= 13;
        }
        //When gotty remains in same block
        else{
            //Do nothing extra
        }
        String dest_id = blocks.charAt(curr_event.block)
                +Integer.toString(next_cell);
        //If next cell is not "safe" cell
        if(next_cell != 9 && next_cell != 4) {
            //Player index at destination
            for (int i = 0; i < 4; i++) {
                if (i == gameVarModel.getCurr_player())
                    continue;
                //Gotty index at destination
                ArrayList<Integer> defeated_gotties_index = players.get(i).gotties_at_cell(dest_id);
                if(defeated_gotties_index.size() == 1 || defeated_gotties_index.size() == 3){
                    ArrayList<String> empty_loc_at_start = new ArrayList<>(0);
                    for(int j = 0; j < 4; j++){
                        String loc = blocks.charAt(i)+"p"+Integer.toString(j+1);
                        //If no gotty is found at loc.
                        if(players.get(i).gotties_at_cell(loc).size() == 0)
                            empty_loc_at_start.add(loc);
                    }
                    assert empty_loc_at_start.size() > 0: "emptyPlaces size is 0" ;
                    int j = 0;
                    for(Integer k: defeated_gotties_index){
                        gotty temp = new gotty(empty_loc_at_start.get(j), i);
                        place_gotty_at_location(players.get(i).gotties.get(k), temp, i,v);
                        players.get(i).gotties.set(k, temp);
                        j++;
                        players.get(i).open_gotties_count--;
                    }
                    break;
                }
            }
        }
        curr_event.pos = dest_id;
        for(int i: gotties_at_loc_index){
            place_gotty_at_location(players.get(gameVarModel.getCurr_player()).gotties.get(i), curr_event, gameVarModel.getCurr_player(),v);
            players.get(gameVarModel.getCurr_player()).gotties.set(i, curr_event);
        }
        if(gameVarModel.getCurr_die_val() != 6)
            gameVarModel.setCurr_player((gameVarModel.getCurr_player()+1)%4);
        gameVarModel.setTurn_pending(false);
    }

    public void place_gotty_at_location(gotty src, gotty dest, int player_idx, View view){
        if(DEBUG)
            System.out.println("src: "+src.pos+" dest: "+dest.pos);
        this.gameVarModel.setSrc(src.pos);
        this.gameVarModel.setDest(dest.pos);
        int id1 = context.getResources().getIdentifier(src.pos,"id",context.getPackageName());
        int id2 = context.getResources().getIdentifier(dest.pos,"id",context.getPackageName());
        ImageView img1 = (ImageView) root.findViewById(id1);
        ImageView img2 =  (ImageView) root.findViewById(id2);
        img1.setImageResource(0);
        switch(player_idx) {
            case 0:img2.setImageResource(R.drawable.red_pawn);
                break;
            case 1:img2.setImageResource(R.drawable.green_pawn);
                break;
            case 2:img2.setImageResource(R.drawable.yellow_pawn);
                break;
            default:img2.setImageResource(R.drawable.blue_pawn);
        }
    }
    public void onClick_start(View v){
        if(!gameVarModel.isTurn_pending())
            return;
        if(!DEBUG && gameVarModel.getCurr_die_val() != 6){
            gameVarModel.setTurn_pending(false);
            gameVarModel.setCurr_player((gameVarModel.getCurr_player()+1)%4);
            return;
        }
        String cell_id = v.getResources().getResourceEntryName(v.getId());
        ArrayList<Integer> temp = players.get(gameVarModel.getCurr_player()).gotties_at_cell(cell_id);
        if(temp.size() == 0)
            return;
        int gotty_at_loc_index = temp.get(0);
        String dest_id = blocks.charAt(gameVarModel.getCurr_player())+"9";
        gameVarModel.setTurn_pending(false);
        place_gotty_at_location(players.get(gameVarModel.getCurr_player()).gotties.get(gotty_at_loc_index),
                new gotty(dest_id, gameVarModel.getCurr_player()),gameVarModel.getCurr_player(),v);
        players.get(gameVarModel.getCurr_player()).gotties.get(gotty_at_loc_index).pos = dest_id;
        players.get(gameVarModel.getCurr_player()).open_gotties_count++;
    }

    public void onClick_dice(View v){
        if(!gameVarModel.getUserID("player"+
                Integer.toString((Integer)gameVarModel.getCurr_player()+1)).equals(this.getUid())){
            disp_toast(gameVarModel.getCurr_player());
            return;
        }

        //Multiple Click on dice by player whose turn is pending
        if(gameVarModel.isTurn_pending())
            return;
        //The dice which is clicked
        String die_id = v.getResources().getResourceEntryName(v.getId());
        //Person whose turn is to roll the die
        String temp = turn_to_die_map.get(gameVarModel.getCurr_player());
        if(die_id.equals(temp)){
            gameVarModel.setTurn_pending(true);
            generateDiceVal();
            displayDice(turn_to_die_map.get(gameVarModel.getCurr_player()),v);
            if(!DEBUG && gameVarModel.getCurr_die_val()!= 6 && players.get(gameVarModel.getCurr_player()).open_gotties_count == 0){
                gameVarModel.setCurr_player((gameVarModel.getCurr_player()+1)%4);
                gameVarModel.setTurn_pending(false);
            }
        }
        else
            disp_toast(gameVarModel.getCurr_player());
    }

    public void updateDice(){
        String dice_id = "dice"+Character.toUpperCase(
                        blocks.charAt(gameVarModel.getCurr_player()));
        displayDice(dice_id, root.findViewById(context.getResources().getIdentifier(
                            dice_id,"id",context.getPackageName())));
    }
    public void StartGame(){
        gameVarModel.setCurr_player(0);
        gameVarModel.setTurn_pending(false);
        for(int i = 0; i < 4; i++){
            players.add(new player());
            for(int j = 0; j < 4; j++){
                players.get(i).gotties.set(j,new gotty(blocks.charAt(i) + "p" + Integer.toString(j+1),i));
            }
        }
    }
    public player getPlayer(int i){
        return this.players.get(i);
    }
    private void generateDiceVal(){
        Random rand = new Random();
        int x = rand.nextInt(6)+1;
        gameVarModel.setCurr_die_val(x);
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
