package nju.hulugame.client.battle.model;

public class Huluwa extends Creature{

    public Huluwa(int id) {
        if(id==0) {
            // 爷爷
            //super("爷爷",200,10,0,4,3);
            name="爷爷";
            health=200;
            attack=50;
            defence=0;
            speed=4;
            speedMax=4;
            attackDist=3;
        }
        else if(id==1) {
            name="大娃";
            health=300;
            attack=50;
            defence=15;
            speed=2;
            speedMax=2;
            attackDist=1;
        }
        else if(id==2) {
            name="二娃";
            health=300;
            attack=50;
            defence=15;
            speed=2;
            speedMax=2;
            attackDist=1;
        }
        else if(id==3) {
            name="三娃";
            health=100;
            attack=50;
            defence=90;
            speed=2;
            speedMax=2;
            attackDist=1;
        }
        else if(id==4) {
            name="四娃";
            health=200;
            attack=100;
            defence=0;
            speed=3;
            speedMax=3;
            attackDist=3;
        }
        else if(id==5) {
            name="五娃";
            health=200;
            attack=100;
            defence=0;
            speed=3;
            speedMax=3;
            attackDist=3;
        }
        else if(id==6) {
            name="六娃";
            health=300;
            attack=50;
            defence=15;
            speed=4;
            speedMax=4;
            attackDist=1;
        }
        else if(id==7) {
            name="七娃";
            health=100;
            attack=200;
            defence=0;
            speed=3;
            speedMax=3;
            attackDist=3;
        }
    }
    public Huluwa(String name,int x,int y) {
        super(name,x,y);
    }
    public Huluwa(String name,int health,int attack,int defence,int speed,int speedMax,int attackDist) {
        super(name,health,attack,defence,speed,speedMax,attackDist);
    }
}