package com.example.ludo;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OnlineMode{
    FirebaseDatabase database;
    GameLogic gameLogic;
    GameVarModel gameVarModel;

    public OnlineMode(Context context,LinearLayout view) {
        gameLogic = new GameLogic(context,view);
        gameVarModel = new GameVarModel();
        gameLogic.StartGame();
    }

    public void onClick_dice(View v){
        gameLogic.onClick_dice(v);
    }

    public void playerTap(View v)
    {
        gameLogic.playerTap(v);
    }

    public void onClick_start(View v)
    {
        gameLogic.onClick_start(v);
    }

}
