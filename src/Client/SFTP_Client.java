package Client;

import static org.junit.Assert.assertEquals;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

import org.junit.Test;

import Server.SFTP_Server;

public class SFTP_Client {
	private Socket clientSocket;
	private PrintWriter out;
	private BufferedReader in;
	private boolean receivingFile;
	private boolean receivingFileSize;
	private boolean sendingFile;
	private String fileName;
	DataInputStream dis;
	private int fileSize;
	private byte[] file;
	
	public void startConnection(String ip, int port) throws UnknownHostException, IOException {
		clientSocket = new Socket(ip, port);
		out = new PrintWriter(clientSocket.getOutputStream(), true);
		in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		dis = new DataInputStream(clientSocket.getInputStream());
		receivingFile = false;
		
	}
	
	private String getString(BufferedReader in, int size) throws IOException {
		 int s1 = 0;
		    String formattedString = "";
		    while ((in.ready()) && (s1 = in.read()) != -1) {

		        char character = (char) s1;

		        //System.out.println("Each Character: "+character+" its hexacode(Ascii): "+Integer.toHexString(character));
		        //output : "0a" --> \n 
		        //output : "0d" --> \r

		        formattedString+=character;

		    }
		//System.out.println("String: " + formattedString);
		return formattedString;
	}
	
	private byte[] loadFile(String fileName) {
		File fileToBeSent = new File(fileName);
		
		if(fileToBeSent.exists()) {
			
			byte[] mybytearray = new byte[(int) fileToBeSent.length()];
		    try {
				BufferedInputStream bis = new BufferedInputStream(new FileInputStream(fileToBeSent));
				bis.read(mybytearray, 0, mybytearray.length);
				bis.close();
				return mybytearray;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		}else {
			return null;
		}
		
	}
	
	public String sendMessage(String message) throws IOException {
		
		message = message.replaceAll("/[\\[\\]']+/g", "").replaceAll("\\s{2,}", " ").trim();
		String[] splitMessage = message.split(" ");
		
		try {
		switch (splitMessage[0].toLowerCase()) {
			case "send":
				
				receivingFile = true;
				out.println(message);
				break;
			
			case "retr":
				
				fileName = splitMessage[1];
				receivingFileSize = true;
				out.println(message);
				
				break;
				
			case "stor":
				
				file = loadFile(splitMessage[2]);
				//clientSocket.getOutputStream().write(loadFile(splitMessage[2]));
				sendingFile = true;
				out.println(message);
				String line = in.readLine(); //in.readLine();
				System.out.println("-------- " + line);
				if(line.charAt(0) == '+') {//check the response from the server must be a +
					out.println("size " + file.length);
					line = in.readLine(); //in.readLine();
					System.out.println("-------- " + line);
					if(line.charAt(0) == '+') {//returned a +
						clientSocket.getOutputStream().write(file);
						return "file saved on remote system";
					}else {
						return "error";
					}
				}else {
					return "error";
				}
				
			
			case "size":
				
				
			default:
				out.println(message);
				
		}
		}catch (Exception e) {
			e.printStackTrace();
			return "error with command";
		}
		
		/*
		while(in.ready() && (line = in.readLine()) != null) {
			if(!receivingFile) {
				response += "\n" + line;
			}else {
				response += line;
			}
		}*/
		if(receivingFile) {
			FileOutputStream fos = new FileOutputStream(fileName);
		    BufferedOutputStream bos = new BufferedOutputStream(fos);
			byte[] buffer = new byte[8192];
			long total = 0;
			int count;
			while (total < fileSize && (count = dis.read(buffer, 0, (int)Math.min(buffer.length, fileSize-total))) > 0)
			{
			  bos.write(buffer, 0, count);
			  total += count;
			}
			
			//bos.write(bytes, 0, bytes.length);
		    bos.close();
			receivingFile = false;
			return "file saved as " + fileName;
		}else if(receivingFileSize){
			try {
				String line = in.readLine(); //in.readLine();
				System.out.println(line);
				receivingFileSize = false;
				fileSize = Integer.parseInt(line);
				return "file size: " + line;
			}catch(Exception e) {
				e.printStackTrace();
				return "error saving file";
			}
		}else {
			String line = in.readLine(); //in.readLine();
			String response = line;
			while(in.ready() && (line = in.readLine()) != null) {
				response += "\n" + line;
			}
			return response;
		}
		
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
			
			response = client.sendMessage("list v");
			System.out.println(response);
			Thread.sleep(400);
			
			response = client.sendMessage("retr Capture2.PNG");
			System.out.println(response);
			Thread.sleep(400);
			
			response = client.sendMessage("send");
			System.out.println(response);
			Thread.sleep(400);
			
			response = client.sendMessage("list v");
			System.out.println(response);
			Thread.sleep(400);
			
			response = client.sendMessage("stor old testCap.PNG");
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
