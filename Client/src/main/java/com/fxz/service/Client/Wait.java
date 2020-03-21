package com.fxz.service.Client;

import java.util.concurrent.locks.LockSupport;

import sun.misc.Unsafe;

public class Wait {
	public static void main(String[] args) throws InterruptedException {
		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				System.out.println("start waiting...");
				LockSupport.park(this);
				System.out.println("end waiting");
			}
		});
		//thread.start();
		//Thread.sleep(1000_2);
		synchronized (thread) {
			thread.notify();
		}
		System.out.println("before park");
		LockSupport.park();
		System.out.println("after park");
		//LockSupport.unpark(thread);
		//System.out.println("unparked..");
		//Thread.sleep(1000_100);
	}
}
