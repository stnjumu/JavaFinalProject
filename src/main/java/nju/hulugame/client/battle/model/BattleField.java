package nju.hulugame.client.battle.model;

import java.util.ArrayList;
import javafx.scene.text.Text;
import javafx.scene.image.ImageView;

public class BattleField {
    // TODO: 将Controller中的itemList转移到这里，未完成；
    private ArrayList<Item> itemList=new ArrayList<>();
    private static String strHealth="Health";
    private static String strSpeed="Speed";


    public void addCreature(int id,int x,int y,Creature creature,ImageView iv) {
        Item item=new Item(id, x, y, creature, iv);
        itemList.add(item);
        //System.out.println("Add new huluwa: "+h.getName());
    }

    private class Item {
        int id; // 唯一标识符；
        int x,y;
        boolean canAttack;
        Creature creature;
        ImageView iv;
        ArrayList<Text> textList=new ArrayList<>();
        public Item(int id,int x,int y,Creature creature,ImageView iv) {
            canAttack=true;
            this.id=id;
            this.x=x;
            this.y=y;
            this.creature=creature;
            this.iv=iv;
        }
        public void addText(Text t) {
            textList.add(t);
        }
        public void setAttack(Boolean a) { canAttack = a; }
        public void updateTexts() {
            for (Text text : textList) {
                if(text.getId()==strHealth) {
                    text.setText(Integer.toString(creature.getHealth()));
                }
                else if(text.getId()==strSpeed) {
                    text.setText(Integer.toString(creature.getSpeed()));
                }
            }
        }
    }
    private Item getItem(int id) {
        for (Item item : itemList) {
            if(item.id==id)
                return item;
        }
        System.out.println("Item Not Found!");
        return null;
    }

    public int getImgSide(ImageView imageView) {
		for (Item item : itemList) {
            if(item.iv==imageView) {
                if(item.id<10)
                    return 0;
                else
                    return 1;
            }
        }
        return -1;
    }
    public int getImgID(ImageView imageView) {
		for (Item item : itemList) {
            if(item.iv==imageView) {
                return item.id;
            }
        }
        return -1;
	}
}