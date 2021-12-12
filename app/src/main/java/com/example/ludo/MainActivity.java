package com.example.ludo;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.ludo.databinding.ActivityMainBinding;

import java.util.HashMap;
import java.util.Random;

public class MainActivity extends AppCompatActivity {


    ActivityMainBinding binding;
    HashMap<Integer,String> map = new HashMap<Integer, String>(){{
        put(1,"one");
        put(2,"two");
        put(3,"three");
        put(4,"four");
        put(5,"five");
        put(6,"six");}};
    int turn = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        LinearLayout mylayout = findViewById(R.id.myLayout);
//        mylayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                System.out.println(v.getId());
//            }
//        });

        binding.diceR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(turn==1)
                {
                    turn++;
                    generate_and_disp("diceR");
                }
                else
                {
                    disp_toast(turn);
                }
            }
        });

        binding.diceG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(turn==2)
                {
                    turn++;
                    generate_and_disp("diceG");
                }
                else
                {
                    disp_toast(turn);
                }
            }
        });

        binding.diceY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(turn==3)
                {
                    turn++;
                    generate_and_disp("diceY");
                }
                else
                {
                    disp_toast(turn);
                }
            }
        });

        binding.diceB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(turn==4)
                {
                    turn=1;
                    generate_and_disp("diceB");
                }
                else
                {
                    disp_toast(turn);
                }
            }
        });




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
        int id = this.getResources().getIdentifier(dice,"id",this.getPackageName());
        int im_id = this.getResources().getIdentifier(map.get(x),"drawable",this.getPackageName());
        Animation aniRotate = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate);
        ImageView img = (ImageView) findViewById(id);
        img.startAnimation(aniRotate);
        img.setImageResource(im_id);
    }
    public void playerTap (View v){
        ImageView img = (ImageView) v;
        System.out.println(img.getResources().getResourceEntryName(v.getId()));
    }
}