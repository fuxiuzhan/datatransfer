package com.fxz.service.Client;

import java.io.IOException;
import java.net.ServerSocket;

public class ServerSocketTest {

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		final int port = 2049;
		final String addr = "127.0.0.1";
		new Thread() {
			public void run() {
				// System.out.println("ServerSocket1 Start Listening....");
				this.setName("ServerSocket1");
				try {
					ServerSocket serverSocket = new ServerSocket(port);
					serverSocket.setReuseAddress(true);
					System.out.println("get a connect->" + serverSocket.accept());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			};
		}.start();
		new Thread() {
			public void run() {
				System.out.println("ServerSocket2 Start Listening....");
				this.setName("ServerSocket2");
				try {
					ServerSocket serverSocket = new ServerSocket(port);
					serverSocket.setReuseAddress(true);
					System.out.println("get a connect2->" + serverSocket.accept());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			};
		}.start();
		Thread.sleep(1000_1000);
	}

}
