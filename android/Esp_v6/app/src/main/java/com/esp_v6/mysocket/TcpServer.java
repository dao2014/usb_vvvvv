package com.esp_v6.mysocket;

import android.os.Handler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2015/7/29.
 */
public class TcpServer {
    private HandleDataThread HandleDataThread;
    private Handler handler;

    public TcpServer(Handler handler){
        this.handler = handler;
    }

    public Handler getHandler() {
        return handler;
    }

    public void startTcpServer(){
        ServerSocket server = null;
        ExecutorService executor = Executors.newCachedThreadPool();
        try {
            // new a socket server
            server = new ServerSocket(1001);
            //just accept 10 request for testing.
            //可以进行开启多个客户端链接
          //  for (int counter = 0; counter < 1000; counter++) {
                // start to listen, this step will be blocked
                Socket request = server.accept();
                // when getting a request, server will start a thread to handle the request.
                // and then keep going to listen.
                HandleDataThread= new HandleDataThread(request,handler);
                executor.execute(HandleDataThread);
          //  }
            if(server!=null)
                server.close();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (server != null) {
                try {
                    server.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void stopTcp(){
        HandleDataThread.setIsKeepAlive(false);
    }
}
