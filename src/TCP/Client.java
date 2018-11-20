package TCP;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket socket;

    public Client() throws Exception {
        System.out.println("正在连接服务端...");
        this.socket = new Socket("localhost", 8088);
        System.out.println("已与服务端建立连接");
    }

    public void start() {
        try {
            Scanner sc = new Scanner(System.in);

            //定义昵称
            String nickName;
            while (true){
                System.out.println("请输入用户名");
                nickName = sc.nextLine();
                if (nickName.length()>0)
                    break;
                System.out.println("输入有误");
            }
            System.out.println("欢迎你，"+nickName);

            /**
             * socket提供的方法：OutputStream getOutputStream
             * 获取一个字节输出流，通过该流写出的数据会被发送至远端计算机
             */
            OutputStream out = socket.getOutputStream();

            OutputStreamWriter osw = new OutputStreamWriter(out,"UTF-8");

            PrintWriter pw = new PrintWriter(osw,true);

            //先将昵称发送给服务器
            pw.println(nickName);

            /**
             * 启动读取服务器发送过来的消息的线程
             */
            ServerHandler handler = new ServerHandler();
            Thread t = new Thread(handler);
            t.start();

            //将字符串发送至服务端
            while (true){
                pw.println(sc.nextLine());
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            Client client = new Client();
            client.start();
        } catch (Exception var2) {
            var2.printStackTrace();
            System.out.println("客服端启动失败");
        }

    }

    /**
     * 该线程用来读取服务端发送过来的消息
     * 并输出到客户端控制台显示。
     */
    class ServerHandler implements Runnable{
        @Override
        public void run() {
            try{
                InputStream in = socket.getInputStream();
                InputStreamReader isr = new InputStreamReader(in,"UTF-8");
                BufferedReader br = new BufferedReader(isr);
                String message;
                while ((message = br.readLine()) != null){
                    System.out.println(message);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}

