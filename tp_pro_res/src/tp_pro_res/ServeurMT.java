package tp_pro_res;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ServeurMT extends Thread {
	private boolean isActive = true; 
	private int nbClient = 0;
	public static void main(String[] args) {
		new ServeurMT().start();
		
	}
	
	@Override
	public void run() {
		try {
			ServerSocket ss = new ServerSocket(1234);
			//System.out.println("Démarrage du server...");
			while(isActive) {
				Socket socket = ss.accept();
				++nbClient;
				new Conversation(socket,nbClient).start();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	class Conversation extends Thread {
		private Socket socketClient;
		private int numClient;
		public Conversation(Socket s,int num) {
			this.socketClient = s;
			this.numClient = num;
		}
		
		@Override
		public void run() {
		
			try {
				InputStream is = socketClient.getInputStream();
				InputStreamReader isr = new InputStreamReader(is);
				BufferedReader br = new BufferedReader(isr);
				
				PrintWriter pw = new PrintWriter(socketClient.getOutputStream(), true);
				String ipClient = socketClient.getRemoteSocketAddress().toString();
				pw.println("Bien venue vous etes le client numero "+numClient);
				System.out.println("Connexion du client numero "+numClient+", IP = "+ipClient);
				
				while(true) {
					String req = br.readLine();
					String reponse = "Length = "+req.length();
					pw.println(reponse);
				}
			}catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

}
