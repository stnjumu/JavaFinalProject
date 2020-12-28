package nju.hulugame.client.battle.model;

import java.util.ArrayList;

public class BattleField {
    
    ArrayList<Huluwa> huluList=new ArrayList<>();
    ArrayList<Evil> evilList=new ArrayList<>();


    public void addHulu(Huluwa h) {
        huluList.add(h);
        System.out.println("Add new huluwa: "+h.getName());
    }
    public void addEvil(Evil e) {
        evilList.add(e);
        System.out.println("Add new evil: "+e.getName());
    }

}