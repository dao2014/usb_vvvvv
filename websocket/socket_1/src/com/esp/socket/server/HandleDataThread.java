package com.esp.socket.server;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.esp.socket.bean.Monitor;
import com.esp.socket.util.DateUtil;
import com.esp.socket.util.SocketUtil;

/**
 * 线程处理长连接
 * 
 * @author Martin
 * 
 */
public class HandleDataThread implements Runnable {
	// request from client
	private Socket request;
	private static  Monitor monitor;
	private String[] monitorinfo;
	private String massge;
	// request id
	private int requestID;

	public HandleDataThread(Socket request, int requestID,Monitor monitor) {
		this.request = request;
		this.requestID = requestID;
		this.monitor = monitor;
	}

	@Override
	public void run() {
		try {
			// set timeout:4 seconds
			request.setSoTimeout(10000);
			while (true) {
				// get info from request when getting a socket request
				String reqStr = "";
				massge = "";
				try {
					// if read() get a timeout exception
					reqStr = SocketUtil.readStrFromStream(request
							.getInputStream());
				} catch (SocketTimeoutException e) {
					// then break while loop, stop the service
					System.out.println(SocketUtil.getNowTime() + " : Time is out, request[" + requestID + "] has been closed.");
					break;
				}
				if(reqStr !=null && !reqStr.equals("")){ 
                	if(reqStr.indexOf("set")>=0){
	                	monitorinfo = reqStr.split(","); 
	                	monitor.setId(monitorinfo[1]);   
	                	monitor.setVoltage(monitorinfo[2]); 
	                	monitor.setElectricity(monitorinfo[3]);
	                	monitor.setTc(monitorinfo[4]);
	                	monitor.setTime(new Date());
	                	massge="WiriterOK";
                	} else if(reqStr.indexOf("get")>=0){
                		if(monitor.getId()!=null && !monitor.getId().equals("")){
                			long time = DateUtil.mm(new Date(), monitor.getTime());
                			if(time<10){//说明下位机 10秒钟内正常运行
                				DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");  
                				String str = df.format(monitor.getTime());
                				massge =""+monitor.getId()+","+monitor.getVoltage()+","+monitor.getElectricity()+","+monitor.getTc()+","+str;
                			}else{
                				massge="stop";
                			}
                		}else
                			massge="notstart";
                	} else{
                		massge="ERROR";
                	}
                }
				System.out.println(SocketUtil.getNowTime()
						+ " : request msg [" + reqStr + "].");
					SocketUtil.writeStr2Stream(
							massge, request.getOutputStream());
				Thread.sleep(5000);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {
			if (request != null) {
				try {
					request.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
