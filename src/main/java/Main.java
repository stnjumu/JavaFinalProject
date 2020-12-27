import nju.hulugame.client.GameClient;
import nju.hulugame.server.GameServer;

class Main {
    public static void main(String[] args) {
        boolean isServer=false;
        System.out.println("start");
        if(args.length>=1) {
            if(args.length>1) {
                System.out.println("main()函数输入参数过多，仅使用第一个参数!");
            }
            if(args[0].equals("Server")||args[0].equals("server")||args[0].equals("S")||args[0].equals("s")) {
                isServer=true;
            }
        }
        
        if(isServer) {
            GameServer.main(args);
        }
        else {
            GameClient.main(args);
        }
    }
}