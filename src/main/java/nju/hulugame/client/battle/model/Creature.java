package nju.hulugame.client.battle.model;

public class Creature {
    protected String name;
    //protected int x,y;  // 位置
    //protected boolean live = true;
    protected int health;
    protected int attack;
    protected int defence;
    protected int speed;  // 每回合的行动力，可以走几格；
    protected int speedMax; // 每回合的行动力，可以走几格；
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

    public Creature(String name2, int health2, int attack2, int defence2, int speed2, int speedMax2, int attackDist2) {
        this.name=name2;
        this.health=health2;
        this.attack=attack2;
        this.defence=defence2;
        this.speed=speed2;
        this.speedMax=speedMax2;
        this.attackDist=attackDist2;
    }
    
	public String getName() { return name; }
    public int getHealth() { return health; }
    //public int getX() { return x; }
    //public int getY() { return y; }
    public int getAttack() { return attack; }
    public int getDefence() { return defence; }
    public int getSpeed() { return speed; }
    public int getAttackDist() { return attackDist; }
    public void setHealth(int i) { health=i; }
    public void setSpeed(int i) { speed=i; }
    public void initSpeed() { speed=speedMax; }

    public void set(String name2, int health2, int attack2, int defence2, int speed2, int speedMax2, int attackDist2) {
        this.name=name2;
        this.health=health2;
        this.attack=attack2;
        this.defence=defence2;
        this.speed=speed2;
        this.speedMax=speedMax2;
        this.attackDist=attackDist2;
    }
}