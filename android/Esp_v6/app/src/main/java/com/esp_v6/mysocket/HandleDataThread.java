package com.esp_v6.mysocket;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.esp_v6.mysocket.util.SocketUtil;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;


/**
 * 线程处理长连�?
 * 
 * @author Martin
 * 
 */
public class HandleDataThread implements Runnable {
	// request from client
	private Socket request;
	private String[] monitorinfo;
	private String massge;
	public boolean isKeepAlive;
	private Handler handler;
	// request id
	private int requestID;

	public HandleDataThread(Socket request, int requestID) {
		isKeepAlive=true;
		this.request = request;
		this.requestID = requestID;
	}
	public HandleDataThread(Socket request,Handler handler) {
		isKeepAlive=true;
		this.handler = handler;
		this.request = request;
	}

	public void setIsKeepAlive(boolean isKeepAlive) {
		this.isKeepAlive = isKeepAlive;
	}

	@Override
	public void run() {
		try {
			// set timeout:4 seconds
			request.setSoTimeout(10000);
			while (isKeepAlive) {
				// get info from request when getting a socket request
				Bundle bundle = new Bundle();
				Message msg = Message.obtain();
				msg.what = 0x11;
				bundle.clear();
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
						reqStr = reqStr.substring(4, reqStr.length());
//	                	monitorinfo = reqStr.split(",");
						bundle.putString("msg", reqStr+"");
						msg.setData(bundle);
						//发送消息 修改UI线程中的组件
						handler.sendMessage(msg);
	                	massge="WiriterOK";
                	}
                }
				System.out.println(SocketUtil.getNowTime()
						+ " : request msg [" + reqStr + "].");
					SocketUtil.writeStr2Stream(
							massge, request.getOutputStream());
				Thread.sleep(1000);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {
			if (request != null) {
				try {
					request.close();
					request.getSoTimeout();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
