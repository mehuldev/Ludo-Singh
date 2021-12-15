package com.example.ludo;

import java.util.ArrayList;

//Upper case
public class player{
    public ArrayList<gotty> gotties = new ArrayList<gotty>(0);
    public int gotty_count = 4;
    public int open_gotties_count = 0;
    //lower case
    public ArrayList<Integer> gotties_at_cell(String cell_id){
        ArrayList<Integer> result = new ArrayList<Integer>(0);
        for(int i = 0; i < 4; i++){
            if(gotties.get(i).pos.equals(cell_id))
                result.add(i);
        }
        return result;
    }
    player(){
        for(int i = 0; i < 4; i++)
            this.gotties.add(new gotty());
    }
}
