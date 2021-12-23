package com.example.ludo;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class OnlineMode extends GameLogic{
    private DatabaseReference gameSessionReference;
    private GameVarModel prevGameVarModel;
    public OnlineMode(Context context,LinearLayout view) {
        super(context,view);
        prevGameVarModel = new GameVarModel();
        gameSessionReference = FirebaseDatabase.getInstance().getReference();
        super.setUid(FirebaseAuth.getInstance().getCurrentUser().getUid());
        gameSessionReference.child("Room").setValue(super.gameVarModel);
        get_curr_game_session_players();
        StartGame();
        updateData();
    }

    private void get_curr_game_session_players(){
        gameSessionReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                OnlineMode.super.gameVarModel.setUsers(snapshot.child("Room").child("users").getValue(new GenericTypeIndicator<HashMap<String, String>>() {
                }));
                gameSessionReference.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Error: "+error.getDetails());
            }
        });
    }
    public void onClick_dice(View v){
        gameSessionReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                OnlineMode.super.gameVarModel = snapshot.child("Room").getValue(new GenericTypeIndicator<GameVarModel>() {
                });
                OnlineMode.super.onClick_dice(v);
                gameSessionReference.child("Room").setValue(OnlineMode.super.gameVarModel);
                gameSessionReference.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Error.....");
                System.out.println(error.getDetails());
            }
        });
    }

    public void playerTap(View v) {
        super.playerTap(v);
        gameSessionReference.child("Room").setValue(super.gameVarModel);
    }

    public void onClick_start(View v) {
        super.onClick_start(v);
        gameSessionReference.child("Room").setValue(super.gameVarModel);
    }
    public void updateData() {
        ValueEventListener postListner = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                System.out.println("Before"+prevGameVarModel.getCurr_die_val());
                OnlineMode.super.gameVarModel = snapshot.child("Room").getValue(new GenericTypeIndicator<GameVarModel>() {
                });
                if(prevGameVarModel.getDie_id() != OnlineMode.super.gameVarModel.getDie_id())
                    OnlineMode.super.updateDice();
//                if(prevGameVarModel.getSrc() != OnlineMode.super.gameVarModel.getSrc())
//                    OnlineMode.super.place_gotty_at_location(
//                            new gotty(OnlineMode.super.gameVarModel.getSrc(),0),
//                            new gotty(OnlineMode.super.gameVarModel.getDest(),0),
//                            OnlineMode.super.gameVarModel.getCurr_player(),
//                            null);
                prevGameVarModel = new GameVarModel(OnlineMode.super.gameVarModel);
                System.out.println("After"+OnlineMode.super.gameVarModel.getCurr_die_val());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        gameSessionReference.addValueEventListener(postListner);
    }
}
