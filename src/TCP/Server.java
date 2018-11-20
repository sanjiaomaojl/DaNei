package TCP;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private ServerSocket server;

    //保存所有客户端输出流的集合
    private List<PrintWriter> allOut;

    public Server() throws Exception {
        server = new ServerSocket(8088);

        allOut = new ArrayList<>();
    }

    /**
     * 将给定的输出流的添加、删除操作
     * @param out
     */
    private synchronized void addOut(PrintWriter out){
        allOut.add(out);
    }

    private synchronized void removeOut(PrintWriter out){
        allOut.remove(out);
    }
    //将给定消息发送给所有客户端
    private synchronized void sendMessage(String message){
        for (PrintWriter out : allOut) {
            out.println(message);
        }
    }

    public void start() {
        try {
            while (true){
                System.out.println("等待客户端连接");
                Socket socket = server.accept();
                System.out.println("一个客服端连接了");
                //启动一个线程，来完成与该客户端的交互
                ClientHandler handler = new ClientHandler(socket);
                Thread t = new Thread(handler);
                t.start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        try {
            Server server = new Server();
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("服务端启动失败");
        }

    }

    /**
     * 该线程负责了一个客服端的交互
     */
    class ClientHandler implements Runnable{
        //该线程处理客服端的Socket
        private Socket socket;

        //客服端的地址信息
        private String host;

        //该用户的昵称
        private String nickName;

        public ClientHandler(Socket socket){

            this.socket = socket;
            //通过Socket可以获取远端计算机的地址信息
            InetAddress address = socket.getInetAddress();
            //获取IP地址
            host = address.getHostAddress();
        }

        @Override
        public void run() {
            PrintWriter pw = null;
            try {
                /*
                socket提供的方法
                InputStream  getInputStream()
                该方法获取一个输入流，从该流读取的数据就是从远端计算机发送来的
                 */
                InputStream in = socket.getInputStream();

                InputStreamReader isr = new InputStreamReader(in,"UTF-8");

                BufferedReader br = new BufferedReader(isr);

                //首先读取的一行字符串为昵称
                nickName = br.readLine();
                sendMessage(nickName+"上线了");

                //通过Socket创建输出流用于将信息发送给客户端
                OutputStream out = socket.getOutputStream();
                OutputStreamWriter osw = new OutputStreamWriter(out,"UTF-8");
                pw = new PrintWriter(osw,true);

                //将该客户端的输出流存入到集合里
                addOut(pw);

                String message;
                //br.readLine()在读取客户端发过来的消息时
                //由于客户端的短线，而其操作系统的不同，这里读取后的
                //结果不同：
                //当windows与客户端断开时：br.readLine()会抛出异常
                //linux的br.readLine()会返回null
                while ((message = br.readLine()) != null){
                    //System.out.println(host+"说："+message);
                    //pw.println(host+"说:"+message);
                    //广播消息
                    sendMessage(nickName+"说:"+message);
                }
            }catch (Exception e){

            }finally {
                //处理当前客户端断开后的逻辑
                //删除输出流
                removeOut(pw);

                sendMessage(nickName+"下线了");
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}








