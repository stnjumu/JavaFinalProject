package nju.hulugame.client.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import nju.hulugame.client.battle.model.Creature;
import nju.hulugame.client.battle.model.Evil;
import nju.hulugame.client.battle.model.Huluwa;

import java.util.ArrayList;


public class CreatureTest {
    @Test
    public void testCreature() {
        // Item和模型相关的简单测试；

        ArrayList<Item> itemList=new ArrayList<>();
        // 新建两个带有生命值、移动力显示Text的Item加入列表；
        Creature huluwa=new Huluwa("大娃", 1, 1, 1, 1, 1, 1);
        Item itemHuluwa = new Item(1, 1, 1, huluwa);
        Creature evil =new Evil("蛇精",10,10,10,10,10,10);
        Item itemEvil =new Item(10, 0, 0, evil);

        itemList.add(itemHuluwa);
        itemList.add(itemEvil);

        assertEquals(true, itemHuluwa.canAttack);
        assertEquals(true, itemEvil.canAttack);
        assertEquals(2,itemList.size());
        assertEquals("大娃", itemList.get(0).creature.getName());
        assertEquals(1, itemList.get(0).creature.getHealth());
        assertEquals(1, itemList.get(0).creature.getAttack());
        assertEquals(1, itemList.get(0).creature.getDefence());
        assertEquals(1, itemList.get(0).creature.getSpeed());
        assertEquals(1, itemList.get(0).creature.getAttackDist());  
        assertEquals(itemEvil, itemList.get(1));

        itemList.add(new Item(11,0,0,new Evil(11)));
        assertEquals(2000, itemList.get(2).creature.getHealth());
        /*
        // 比较复杂的测试，与模型和图形化界面的参数有关的测试；
        // 舍弃测试，javafx相关启动不了，未完成；
        Text textHuluwaHealth=new Text(Integer.toString(huluwa.getHealth()));
        textHuluwaHealth.setId(strHealth);
        itemHuluwa.addText(textHuluwaHealth);
        Text textHuluwaSpeed=new Text(Integer.toString(huluwa.getSpeed()));
        textHuluwaSpeed.setId(strSpeed);
        itemHuluwa.addText(textHuluwaSpeed);
        itemList.add(itemHuluwa);


        Evil evil =new Evil("蛇精",10,10,10,10,10,10);
        Item itemEvil =new Item(10, 10, 10, evil, new ImageView());
        Text textEvilHealth=new Text(Integer.toString(evil.getHealth()));
        itemEvil.addText(textEvilHealth);
        textEvilHealth.setId(strHealth);
        Text textEvilSpeed=new Text(Integer.toString(evil.getSpeed()));
        itemEvil.addText(textEvilSpeed);
        textEvilSpeed.setId(strSpeed);
        itemList.add(itemEvil);

        assertEquals(1, itemList.get(0).creature.getHealth());
        assertEquals(1, itemList.get(0).creature.getSpeed());
        itemList.get(0).creature.setHealth(2);
        itemList.get(0).creature.setSpeed(3);
        itemList.get(0).updateTexts();
        // 测试上一句updateTexts是否正确；
        assertEquals("2",itemList.get(0).textList.get(0).getText());
        assertEquals("3",itemList.get(0).textList.get(1).getText());


        assertEquals(10, itemList.get(1).creature.getHealth());
        assertEquals(10, itemList.get(1).creature.getSpeed());
        itemList.get(1).creature.setHealth(20);
        itemList.get(1).creature.setSpeed(30);
        itemList.get(0).updateHealthTexts();
        // 测试上一句updateHealthTexts是否正确；
        assertEquals("20",itemList.get(1).textList.get(0).getText());
        // Speed的Text不应该改变；
        assertEquals("10",itemList.get(1).textList.get(1).getText());
        */
    }

    // 简化版Item，去掉了图形化相关；
    private class Item {
        int id; // 唯一标识符；
        int x,y;
        boolean canAttack;
        Creature creature;
        public Item(int id,int x,int y, Creature creature) {
            canAttack=true;
            this.id=id;
            this.x=x;
            this.y=y;
            this.creature=creature;
        }
    }
}