package nju.hulugame.client.battle.model;

public class Creature {
    protected static int newID=100;
    protected int id; // 独一无二的标记
    protected String name;
    protected int x,y;  // 位置
    protected boolean live = true;
    protected int health;
    protected int attack;
    protected int defence;
    protected int speed;  // 每回合的行动力，可以走几格；
    protected int attackDist;

    public Creature(String n,int x,int y) {
        name = n;
        id = newID++;
        this.x=x;
        this.y=y;
    }

    public String getName() { return name; }
    public int getX() { return x; }
    public int getY() { return y; }
}