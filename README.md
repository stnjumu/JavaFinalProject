# JavaFinalProject
## 作者：穆朋朋 171830583，单人实现。
### 联系方式：1282807292@qq.com

## 使用说明：
### 编译：
代码使用Maven管理，在pom.xml所在目录可直接运行命令
```
    mvn clean test package
```
来编译获得jar包: hulugame-*版本号*.jar

目前jar包名为：hulugame-1.0.0.jar
### 启动游戏
一局游戏需要启动1个服务器和2个客户端;使用Server, server, S或者s中任意参数都可启动服务器；不使用参数可启动客户端；
```
java -jar ./target/hulugame-1.0.0.jar S     // 启动服务器
java -jar ./target/hulugame-1.0.0.jar       
java -jar ./target/hulugame-1.0.0.jar       // 分别启动2个客户端
```
服务器端没有图形界面，客户端图形界面如下图：

![图形界面](https://github.com/stnjumu/JavaFinalProjectPictures/blob/main/images/%E7%95%8C%E9%9D%A2.png?raw=true)
### 连接服务器
点击“连接服务器”按钮即可连接本机的服务器，或者可以修改按钮左边的IP地址，以连接其他IP的服务器；

### 读取存档
点击界面右上角的“>>”按钮，弹出界面：

![弹出界面](https://github.com/stnjumu/JavaFinalProjectPictures/blob/main/images/%E5%BC%B9%E5%87%BA%E7%95%8C%E9%9D%A2.png?raw=true)

点击“读取存档”按钮即可读取默认存档，之后自动进行战斗至结束；(默认存档mainReplay.rp即我选择的比较精彩的战斗)

如果需要载入其他存档，例如: xx.rp，需要先将文件放入目录：src/main/resources/replay/下，并在输入框中输入存档文件名xx后点击“读取存档”。

### 联机对战
两个客户端都连接服务器端，先连接服务器端的会分配为葫芦娃阵营，另一个会是妖精阵营。

注意：读取存档和联机对战请不要同时进行。

### 界面及操作说明
双方都**按E键**后会创建出双方生物，战斗开始。下图为一个格子中的生物：

![蛇精](https://github.com/stnjumu/JavaFinalProjectPictures/blob/main/images/%E7%94%9F%E7%89%A9%E5%9B%BE%E7%89%87.png?raw=true)

**左键点击**己方生物图片可以选中该生物，可以点击多个生物以选中多个己方生物，**按wasd键**对使所有已选生物上下左右移动。

生物左上角显示**移动力**，归0后生物此回合无法移动，但可以攻击。

生物右上方显示**血量**，归0后生物死亡。

**右键点击**己方生物取消选中，**右键双击**取消选择所有己方生物。

**左键点击**敌方生物图片可以使选中的己方生物尝试攻击该生物，一旦在射程内就发动攻击，攻击后发动攻击的生物本回合中无法再攻击或移动，左上角移动力点数标记变为“S”。

**按E键**提议结束此回合，双方都提议结束回合则本回合结束，进入下一回合，重置所有移动力并可再次攻击。

一方生物全部死亡后**游戏结束**，游戏结束时提示胜利或失败，并断开与服务器连接，失败界面如下：

![失败界面](https://github.com/stnjumu/JavaFinalProjectPictures/blob/main/images/%E7%BB%93%E6%9D%9F%E7%95%8C%E9%9D%A2.png?raw=true)

点击“重新开始”按钮可以再次读取存档或连接服务器。

### 图示说明
1. 开始站位及生物介绍：

![开始站位](https://github.com/stnjumu/JavaFinalProjectPictures/blob/main/images/%E5%88%9D%E5%A7%8B%E7%AB%99%E4%BD%8D.png?raw=true)

双方生物在左右两侧的为远程攻击，攻击距离为3，我的攻击距离设定比较简单：x或y方向上距离中的最大值小于等于3即可，所以远程攻击覆盖范围是7*7的正方形。

中间的生物为近战攻击，攻击距离为1。

设定上远程攻击生物攻击高，血少防低，而近战攻击生物相反。

2. 选中多人并进行移动，及两个客户端同步：

![多人移动](https://github.com/stnjumu/JavaFinalProjectPictures/blob/main/images/%E5%A4%9A%E4%BA%BA%E7%A7%BB%E5%8A%A8.gif?raw=true)

3. 移动并攻击：

![移动并攻击](https://github.com/stnjumu/JavaFinalProjectPictures/blob/main/images/%E7%A7%BB%E5%8A%A8%E6%94%BB%E5%87%BB.gif?raw=true)

4. 单人移动的碰撞检测

![碰撞检测](https://github.com/stnjumu/JavaFinalProjectPictures/blob/main/images/%E7%A7%BB%E5%8A%A8%E6%A3%80%E6%B5%8B.gif?raw=true)

上图中操作为：鼠标选中隐身的葫芦娃，按两次W尝试向上移动，但不会移动，按S向下移动，按W向上移动，按D向右移动。

**注意**：多人移动的碰撞检测未能实现，在下面的“拓展功能介绍”中有所介绍。

## 实验要求完成情况
### 基本要求
基本要求全部完成。

我为了简化操作，实现了一个拓展功能——多选己方生物，同时移动和攻击。
### 拓展功能介绍
左键点击多个图片选中多个生物，**右键点击**己方生物取消选中该生物，**右键双击**取消选择所有己方生物。
实现这个功能使移动时的位置冲突检测有些困难，所以未实现多选情况下的碰撞检测，多个生物同时移动时可能出现多个生物在同一位置的情况。

当然如“图示说明”中演示的，我确保了一次选中一个图片进行移动的位置冲突检测，不会移动到已有其他生物的位置。
### 战斗回放功能
功能基本实现，但与要求略有不同。我的实现不是按L键弹出文件对话框，而是在文本框中输入文件名，点击按钮后加载特定位置的存档文件。

当然，战斗回放过程中无法操控任何生物。

### 图形化
仅使用JavaFx;
### 多线程协同
此次实验涉及到多个线程，例如一个线程为JavaFx图形界面，一个线程负责接收来自服务器的UDP包。两个线程都会对存有模型的ArrayList进行增删改查等操作，如果对操作不加限制，容易出现线程不安全问题，ArrayList的下标或迭代器很容易超限或找不到对象。

我限制能对这些ArrayList的增删元素操作只能由JavaFx进程执行，其他线程想要进行增删操作只需将代码通过Platform.runlater()提交给JavaFx线程，这样可以避免出现上述线程不安全问题。

### 单元测试
1. GameServerTest.java
测试UDP包的发送与接收
2. FileWriteReadTest.java
测试文件读写
3. CreatureTest.java
简单测试Creature, Huluwa, Evil的实例的创建与使用

## 实现思路
### 网络模块的设计
此部分参考了老师推荐的项目：https://www.cnblogs.com/tanshaoshenghao/p/10708586.html

采用Server和Client分开的设计。客户端先使用TCP协议连接服务器，双方获得对方ip、端口，并分配战斗阵营后，采用UDP协议进行之后数据包的发送和接收，这样可有效降低延迟。

图形界面触发一个事件，例如移动，先在本机进行判断是否可以移动，是则会发送包含对应Msg的UDP包给Server端，而Server端功能比较简单，收到Msg就转发给所有Client，Client收到Msg后才对模型和图形界面进行改动。

### 图形化设计模式
采用MVC设计模式：Model-View-Controller。

利用控制器作为桥梁使模型和图形界面代码分离，View部分只负责图形界面，触发事件后交给Controller负责，上级GameClient收到消息后，消息也交给Controller来处理，改动模型和更新图形界面。

简化与网络、读写文件等模块的对接，只需要和Controller打交道就行，降低了模块间的耦合度。

### 其他
大量使用try-catch语句来进行异常处理，使程序不容易闪退。
大量使用lamda表达式，使代码更简洁易读。

## 不足
图形化界面比较简陋，战斗方式单一，生物信息显示不全，没有操作提示，多生物同时移动的碰撞检测机制待实现。

请不要在联机对战和读取存档同时进行，这部分的冲突检测未实现，两者会同时在一个场景中进行，场面会非常乱。