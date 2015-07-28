package com.esp.socket.server;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

import com.esp.socket.bean.Monitor;
import com.esp.socket.util.DateUtil;

public class EspServer {
	public static final int PORT = 1001;//监听的端口号     
    private static  Monitor monitor;
    private String[] monitorinfo;
    private String massge;
    DataOutputStream out;
    DataInputStream input;
    public static void main(String[] args) {    
        System.out.println("服务器启动...\n");    
        EspServer server = new EspServer();    
        monitor =new Monitor();
        
        server.init( );    
    }    
    
    public void init( ) {    
        try {    
            ServerSocket serverSocket = new ServerSocket(PORT);    
            while (true) {    
                // 一旦有堵塞, 则表示服务器与客户端获得了连接    
                Socket client = serverSocket.accept();    
                // 处理这次连接    
                new HandlerThread(client);    
            }    
        } catch (Exception e) {    
            System.out.println("服务器异常: " + e.getMessage());    
        }    
    }    
    
    private class HandlerThread implements Runnable {    
        private Socket socket;    
        public HandlerThread(Socket client) {    
            socket = client;    
            new Thread(this).start();    
        }    
    
        public void run() {    
            try {    
                // 读取客户端数据    
            	input = new DataInputStream(socket.getInputStream());  
                String clientInputStr = input.readUTF();//这里要注意和客户端输出流的写方法对应,否则会抛 EOFException  
                // 处理客户端数据    
                System.out.println("客户端发过来的内容:" + clientInputStr);    
                if(clientInputStr !=null && !clientInputStr.equals("")){
                	if(clientInputStr.indexOf("set")>=0){
	                	monitorinfo = clientInputStr.split(",");
	                	monitor.setId(monitorinfo[1]);   
	                	monitor.setVoltage(monitorinfo[2]); 
	                	monitor.setElectricity(monitorinfo[3]);
	                	monitor.setTime(new Date());
	                	massge="23";
                	} else if(clientInputStr.indexOf("get")>=0){
                		if(monitor.getId()!=null && !monitor.getId().equals("")){
                			long time = DateUtil.mm(new Date(), monitor.getTime());
                			if(time<5){//说明下位机 10秒钟内正常运行
                				massge = monitor.getId()+","+monitor.getVoltage()+","+monitor.getElectricity()+","+monitor.getTime().toGMTString();
                			}else{
                				massge="stop";
                			}
                		}else
                			massge="notstart";
                	} else{
                		massge="ERROR";
                	}
                }
                // 向客户端回复信息    
                 out= new DataOutputStream(socket.getOutputStream());    
//                System.out.print("请输入:\t");    
//                // 发送键盘输入的一行    
//                String s = new BufferedReader(new InputStreamReader(System.in)).readLine();    
                out.writeUTF(massge);    
                  
                  
            } catch (Exception e) {    
                System.out.println("服务器 run 异常: " + e.getMessage());    
            } finally {    
                if (socket != null) {    
                    try {    
                    	out.close();    
                        input.close();  
                        socket.close();    
                    } catch (Exception e) {    
                        socket = null;    
                        System.out.println("服务端 finally 异常:" + e.getMessage());    
                    }    
                }    
            }   
        }    
    }    
}
