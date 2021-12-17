package com.example.ludo;

public class gotty{
    public String pos;
    public int block;
    gotty(){
        this.pos = "";
        this.block = -1;
    }
    gotty(String pos, int block){
        this.pos = pos;
        this.block = block;
    }
    gotty(gotty other){
        this.pos = other.pos;
        this.block = other.block;
    }
}
