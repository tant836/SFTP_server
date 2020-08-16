package Client;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import org.junit.Test;

import Server.SFTP_Server;

public class SFTP_Client {
	private Socket clientSocket;
	private PrintWriter out;
	private BufferedReader in;
	
	public void startConnection(String ip, int port) throws UnknownHostException, IOException {
		clientSocket = new Socket(ip, port);
		out = new PrintWriter(clientSocket.getOutputStream(), true);
		in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		
	}
	
	public String sendMessage(String message) throws IOException {
		out.println(message);
		String response = in.readLine();
		return response;
	}
	
	public void stopConection() throws IOException {
		in.close();
		out.close();
		clientSocket.close();
	}
	
	public static void main(String[] args) {
		SFTP_Client client = new SFTP_Client();
		try {
			client.startConnection("127.0.0.1", 115);
			
			String response = client.sendMessage("user tom");
			System.out.println(response);
			Thread.sleep(400);
			
			response = client.sendMessage("acct acc1");
			System.out.println(response);
			Thread.sleep(400);
			
			response = client.sendMessage("pass pass");
			System.out.println(response);
			Thread.sleep(400);
			
			response = client.sendMessage("list f");
			System.out.println(response);
			Thread.sleep(400);
			
			response = client.sendMessage("done");
			System.out.println(response);
			Thread.sleep(100);
			
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	} 
}
