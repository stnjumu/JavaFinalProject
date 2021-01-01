package nju.hulugame.client.battle.model;

public class Creature {
    protected String name;
    //protected int x,y;  // 位置
    //protected boolean live = true;
    protected int health;
    protected int attack;
    protected int defence;
    protected int speed;  // 每回合的行动力，可以走几格；
    protected int attackDist; // 正方形的攻击距离，取x,y方向上差值的最大值即可。
    
    public Creature() { }
    public Creature(String n) {
        name = n;
    }

    public Creature(String n,int x,int y) {
        name = n;
        //this.x=x;
        //this.y=y;
    }

    public String getName() { return name; }
    public int getHealth() { return health; }
    //public int getX() { return x; }
    //public int getY() { return y; }
}