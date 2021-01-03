package nju.hulugame.client.battle.model;

public class Evil extends Creature{
    public Evil(int id) {
        if(id==10) {
            //蛇精：500血，30攻，10防，3的移动距离,3的攻击距离;
            //super("蛇精",500,20,10,3,3);
            name="蛇精";
            health=500;
            attack=200;
            defence=10;
            speed=3;
            speedMax=3;
            attackDist=3;
        }
        else if(id==11) {
            // 蝎子精
            name="蝎子精";
            health=2000;
            attack=100;
            defence=20;
            speed=2;
            speedMax=2;
            attackDist=1;
        }
    }
    public Evil(String name,int health,int attack,int defence,int speed,int speedMax,int attackDist) {
        super(name,health,attack,defence,speed,speedMax,attackDist);
    }
}