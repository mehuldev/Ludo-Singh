package com.example.ludo;

public class GameVarModel {
    private int curr_player = 0;
    private int curr_die_val = 0;
    private String  src;
    private String  dest;
    private String  die_id;
    private boolean turn_pending = false;

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
}
