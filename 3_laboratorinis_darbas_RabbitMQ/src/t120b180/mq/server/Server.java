package t120b180.mq.server;

import java.util.Scanner;

public class Server {
	public static void main(String[] args) {
		Server self = new Server();
		self.run();
	}
	
	private void run() {
		try {
			MQService service = new MQService();
			
			//wait for shutdown signal
			System.out.println("Started, press [Enter] for shutdown.");
			(new Scanner(System.in)).nextLine();
		}
		catch(Exception | Error e) {
			System.out.println("Unhandled exception caught: " + e.getMessage());
			e.printStackTrace();
		}
	}
}
