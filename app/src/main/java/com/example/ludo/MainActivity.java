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

//string facing
public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    OnlineMode onlineMode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        onlineMode = new OnlineMode(MainActivity.this,binding.getRoot());

    }
    public void onClick_dice(View v){
        onlineMode.onClick_dice(v);
    }

    public void playerTap(View v)
    {
        onlineMode.playerTap(v);
    }

    public void onClick_start(View v)
    {
        onlineMode.onClick_start(v);
    }



}