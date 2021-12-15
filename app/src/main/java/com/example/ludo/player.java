package com.example.ludo;

import java.util.ArrayList;

public class player{
    public ArrayList<gotty> gotties = new ArrayList<gotty>(0);
    public int gotty_count = 4;
    public int gotty_at_cell(String cell_id){
        for(int i = 0; i < 4; i++){
            if(gotties.get(i).pos.equals(cell_id))
                return i;
        }
        return -1;
    }
    player(){
        for(int i = 0; i < 4; i++)
            this.gotties.add(new gotty());
    }
}
