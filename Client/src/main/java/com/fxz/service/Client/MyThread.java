package com.fxz.service.Client;

import java.io.BufferedInputStream;
import java.net.Socket;

public class MyThread extends java.lang.Thread {

	public boolean STOP = false;

	public boolean READ = true;

	public boolean STAT=true;
	@Override
	public void run() {
		Socket socket=null;
		while (!STOP) {
			System.out.println("--------");
			try {
				socket = new Socket("127.0.0.1", 5000);
				socket.setSoTimeout(0);
				BufferedInputStream bufferedInputStream = new BufferedInputStream(socket.getInputStream());
				while (READ) {
					try {
						System.out.println(bufferedInputStream.read());
					} catch (Exception e) {
						System.out.println(e.getMessage());
					}
				}
				STAT=false;
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
		try {
			socket.close();
		}catch (Exception e) {
			// TODO: handle exception
		}
	}
}
