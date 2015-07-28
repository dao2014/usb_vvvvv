package com.esp.socket.server;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.esp.socket.bean.Monitor;

/**
 * filename:SocketServer.java
 * author:martin
 * comment:socketserver
 */

/**
 * @author martin
 * 
 */
public class SocketServer {
	private static  Monitor monitor;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ServerSocket server = null;
		ExecutorService executor = Executors.newCachedThreadPool();
		try {
			// new a socket server
			server = new ServerSocket(1001);
			monitor = new Monitor( );
			//just accept 10 request for testing.
			for (int counter = 0; counter < 1000; counter++) {
				// start to listen, this step will be blocked
				Socket request = server.accept();
				
				// when getting a request, server will start a thread to handle the request.
				// and then keep going to listen.
				executor.execute(new HandleDataThread(request, counter,monitor));
			}

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

}
