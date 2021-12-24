package com.example.ludo;

import java.util.ArrayList;
import java.util.HashMap;

public class GameVarModel {
    private int curr_player = 0;
    private int curr_die_val = 0;
    private String  src;
    private String  dest;
    private String  die_id;
    private boolean turn_pending;
    public HashMap<String, String> users;

    public GameVarModel(){
        this.curr_die_val = 0;
        this.curr_player = 0;
        this.src = "";
        this.dest = "";
        this.die_id = "";
        this.turn_pending = false;
        this.users = new HashMap<>();
        this.users.put("player1","Iujfw015VMVlOikZ14OY8LhWEUl1");
        this.users.put("player2","Vl0kGEiQHTcSH8ls5fvpMQqHglo2");
    }
    public GameVarModel(GameVarModel other){
        this.curr_player = other.curr_player;
        this.curr_die_val = other.curr_die_val;
        this.src = other.src;
        this.dest = other.dest;
        this.die_id = other.die_id;
        this.turn_pending = other.turn_pending;
        this.users = new HashMap<>(other.users);
    }
    public int getCurr_player() {
        return curr_player;
    }

    public void setCurr_player(int curr_player) {
        this.curr_player = curr_player;
    }

    public int getCurr_die_val() {
        return curr_die_val;
    }

    public void setCurr_die_val(int curr_die_val) {
        this.curr_die_val = curr_die_val;
    }

    public boolean isTurn_pending() {
        return turn_pending;
    }

    public void setTurn_pending(boolean turn_pending) {
        this.turn_pending = turn_pending;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getDest() {
        return dest;
    }

    public void setDest(String dest) {
        this.dest = dest;
    }

    public String getDie_id() {
        return die_id;
    }

    public void setDie_id(String die_id) {
        this.die_id = die_id;
    }

    public String getUserID(String playerNumber) {
        assert  this.users != null: "HashMap<String,String>users is null";
        assert  !this.users.isEmpty(): "HashMap<String,String>users is empty";
        assert this.users.containsKey(playerNumber): "playerNumber not found in " +
                                                        "HashMap<String,String>users";
        return users.get(playerNumber);
    }

    public void setUsers(HashMap<String, String> u) {
        this.users = new HashMap<>(u);
        System.out.println("Set users:"+this.users);
    }
}
