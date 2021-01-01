package nju.hulugame.client.battle.model;

public class Huluwa extends Creature{

    public Huluwa(int id) {
        if(id==0) {
            // 爷爷
            //super("爷爷",200,10,0,4,3);
            name="爷爷";
            health=200;
            attack=10;
            defence=0;
            speed=4;
            attackDist=3;
        }
        else if(id==1) {
            name="大娃";
            health=300;
            attack=30;
            defence=15;
            speed=3;
            attackDist=1;
        }
    }
    public Huluwa(String name,int x,int y) {
        super(name,x,y);
    }
}