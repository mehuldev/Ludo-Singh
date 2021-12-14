package com.example.ludo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ludo.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;


public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
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
    private int curr_player = 0;
    private final String blocks = "rgyb";
    private int curr_die_val = 0;
    private boolean turn_pending = false;
    private ArrayList<player> players = new ArrayList<player>(0);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        binding.diceR.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(curr_player==1)
//                {
//                    curr_player++;
//                    generate_and_disp("diceR");
//                }
//                else
//                {
//                    disp_toast(curr_player);
//                }
//            }
//        });
//
//        binding.diceG.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(curr_player==2)
//                {
//                    curr_player++;
//                    generate_and_disp("diceG");
//                }
//                else
//                {
//                    disp_toast(curr_player);
//                }
//            }
//        });
//
//        binding.diceY.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(curr_player==3)
//                {
//                    curr_player++;
//                    generate_and_disp("diceY");
//                }
//                else
//                {
//                    disp_toast(curr_player);
//                }
//            }
//        });
//
//        binding.diceB.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(curr_player==4)
//                {
//                    curr_player=1;
//                    generate_and_disp("diceB");
//                }
//                else
//                {
//                    disp_toast(curr_player);
//                }
//            }
//        });
        StartGame();
    }

    private void disp_toast(int turn) {
        switch (turn)
        {
            case 1:Toast.makeText(MainActivity.this, "Red's turn", Toast.LENGTH_SHORT).show();
            break;
            case 2:Toast.makeText(MainActivity.this, "Green's turn", Toast.LENGTH_SHORT).show();
            break;
            case 3:Toast.makeText(MainActivity.this, "Yellow's turn", Toast.LENGTH_SHORT).show();
            break;
            default:Toast.makeText(MainActivity.this, "Blue's turn", Toast.LENGTH_SHORT).show();
        }
    }

    private void generate_and_disp(String dice) {
        Random rand = new Random();
        int x = rand.nextInt(6)+1;
        curr_die_val = x;
        int id = this.getResources().getIdentifier(dice,"id",this.getPackageName());
        int im_id = this.getResources().getIdentifier(die_vals_to_image_map.get(x),"drawable",this.getPackageName());
        Animation aniRotate = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate);
        ImageView img = (ImageView) findViewById(id);
        img.startAnimation(aniRotate);
        img.setImageResource(im_id);
    }
    public void playerTap (View v){
//        System.out.println(turn_pending);
        if(!turn_pending)
            return;
        String cell_id = v.getResources().getResourceEntryName(v.getId());
        int gotty_idx = players.get(curr_player).gotty_at_cell(cell_id);
//        System.out.println("Src: "+ cell_id+" "+curr_player+" "+gotty_idx);
        //Click on cell on which there is no gotty of particular color.
        if(gotty_idx == -1)
            return;
        gotty curr_event = players.get(curr_player).gotties.get(gotty_idx);
        int curr_cell = Integer.parseInt(cell_id.substring(1));
        int next_cell = curr_cell+curr_die_val;
//        System.out.println(curr_cell+" "+next_cell);
        //When gotty have to enter the home
        if(curr_player == player_to_turn_map.get(cell_id.charAt(0)) && next_cell > 7 && curr_cell < 7){
            next_cell += 6;
            if(next_cell > 18){
                players.get(curr_player).gotty_count--;
                if(players.get(curr_player).gotty_count == 0)
                    System.out.println("Winner is player: "+curr_player);
                else{
                    //Erase the gotty
                }
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
        }
        String dest_id = blocks.charAt(curr_event.block)
                                +Integer.toString(next_cell);
        place_gotty_at_location(gotty_idx, dest_id);
        curr_event.pos = dest_id;
        players.get(curr_player).gotties.set(gotty_idx,curr_event);
        curr_player = (curr_player+1)%4;
        turn_pending = false;
    }

    public void place_gotty_at_location(int gotty_idx, String dest_id){
//        System.out.println(dest_id);
        String curr = players.get(curr_player).gotties.get(gotty_idx).pos;
        int id1 = this.getResources().getIdentifier(curr,"id",this.getPackageName());
        int id2 = this.getResources().getIdentifier(dest_id,"id",this.getPackageName());
        ImageView img1 = (ImageView) findViewById(id1);
        ImageView img2 = (ImageView) findViewById(id2);
        img1.setImageResource(0);
        switch(curr_player)
        {
            case 0:img2.setImageResource(R.drawable.red_pawn);
                break;
            case 1:img2.setImageResource(R.drawable.green_pawn);
                break;
            case 2:img2.setImageResource(R.drawable.yellow_pawn);
                break;
            default:img2.setImageResource(R.drawable.blue_pawn);
        }

//        System.out.println("dest: "+dest_id);
    }
    public void onClick_start(View v){
        if(!turn_pending)
            return;
        String cell_id = v.getResources().getResourceEntryName(v.getId());
        int gotty_idx = players.get(curr_player).gotty_at_cell(cell_id);
//        System.out.println(gotty_idx+" "+gotty_idx);
        if(gotty_idx == -1)
            return;
        String dest_id = blocks.charAt(curr_player)+"9";
        turn_pending = false;
        place_gotty_at_location(gotty_idx, dest_id);
        players.get(curr_player).gotties.get(gotty_idx).pos = dest_id;
        curr_player = (curr_player+1)%4;
    }

    public void onClick_dice(View v){
        if(turn_pending)
            return;
        String die_id = v.getResources().getResourceEntryName(v.getId());
        String temp = turn_to_die_map.get(curr_player);
        if(die_id.equals(temp)){
            turn_pending = true;
            generate_and_disp(turn_to_die_map.get(curr_player));
        }
        else
            disp_toast(curr_player);
    }

    private void StartGame(){
        curr_player = 0;
        for(int i = 0; i < 4; i++){
            players.add(new player());
            for(int j = 0; j < 4; j++){
                players.get(i).gotties.set(j,new gotty(blocks.charAt(i) + "p" + Integer.toString(j+1),i));
            }
        }
    }
}