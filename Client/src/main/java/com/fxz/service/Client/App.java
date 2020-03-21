package com.fxz.service.Client;


public class App {
	public static void main(String[] args)  {

		
		MyThread thread = new MyThread();
		
		thread.start();
		try {
			thread.join(10000);
		} catch (InterruptedException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		System.out.println("start");
		try {
			Thread.sleep(1000 * 2);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		thread.READ = false;
		thread.STOP = true;
		System.out.println("stop");
		try {
			Thread.sleep(1000 * 2);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println("Stat->" + thread.STAT);

		if (thread.STAT == true) {
			System.out.println("11111");
			try {
				thread.join(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (thread.STAT == true) {
				System.out.println("2222");
				thread.interrupt();
				if (thread.STAT == true) {
					System.out.println("3333");
					try {
						thread.join(200);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (thread.STAT == true) {
						System.out.println("destroy");
						thread.destroy();
					}
				}

			}
		}
		System.out.println("test  send");
		try {
			Thread.sleep(1000 * 100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("exit");
	}

}
