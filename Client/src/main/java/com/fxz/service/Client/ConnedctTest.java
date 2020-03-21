package com.fxz.service.Client;

import java.net.InetSocketAddress;
import java.net.Socket;

public class ConnedctTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		long start = System.currentTimeMillis();
		Thread thread=new Thread() {
			public void run() {
				int times = 0;
				while (times < 60) {
					try {
						Thread.sleep(1000);
						System.out.println(times);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						break;
					}
					times++;
				}
			};
		};
		thread.start();
		try {
			Socket socket = new Socket();
			socket.setKeepAlive(true);
			socket.connect(new InetSocketAddress("192.168.43.14", 22),60000);
			System.out.println("connect");
			System.out.println("times->" + (System.currentTimeMillis() - start) / 1000);
			thread.interrupt();
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getLocalizedMessage());
			System.out.println("times->" + (System.currentTimeMillis() - start) / 1000);
			thread.interrupt();
		}
	}

}
