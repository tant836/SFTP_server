package Server;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import Server.MessageManagement.Type;

public class SFTP_Server {
	private ServerSocket serverSocket;
	private Socket outSocket;
	private PrintWriter out;
	private BufferedReader in;
	DataInputStream dis;

	public void start(int port) throws IOException {
		serverSocket = new ServerSocket(port);
		outSocket = serverSocket.accept();
		out = new PrintWriter(outSocket.getOutputStream(), true);
		in = new BufferedReader(new InputStreamReader(outSocket.getInputStream()));
		dis = new DataInputStream(outSocket.getInputStream());
		/*
		 * String greeting = in.readLine(); if ("hello server".equals(greeting)) {
		 * out.println("hello client"); } else { out.println("unrecognized greeting"); }
		 */
	}

	public void startMulti(int port) throws IOException {
		serverSocket = new ServerSocket(port);
		while (true) {
			new ClientHandler(serverSocket.accept()).start();
		}
	}

	public void stop() throws IOException {
		in.close();
		out.close();
		outSocket.close();
		serverSocket.close();
	}

	public static void main(String[] args) {
		SFTP_Server server = new SFTP_Server();
		try {
			server.startMulti(115);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static class ClientHandler extends Thread {
		private Socket outSocket;
		private PrintWriter out;
		private BufferedReader in;
		DataInputStream dis;

		public ClientHandler(Socket socket) {
			this.outSocket = socket;
		}

		public void run() {
			try {
				out = new PrintWriter(outSocket.getOutputStream(), true);
				in = new BufferedReader(new InputStreamReader(outSocket.getInputStream()));
				dis = new DataInputStream(outSocket.getInputStream());

				String inputLine;
				String outputLine;
				MessageManagement msgM = new MessageManagement();
				while (true/*(inputLine = in.readLine()) != null*/) {
					inputLine = in.readLine();
					
					if ("done".equals(inputLine)) {
						out.println("+" + msgM.getUser());
						break;
					}
					String[] inputs = inputLine.split(" ");
					try {
					
						if(inputs[0].equals("size")) {
							//if(inputs.length != 2) {
								System.out.println("---------- " + inputs[0]);
								int fileSize = Integer.parseInt(inputs[1]);
								out.println("+ok, waiting for file");
								System.out.println(msgM.getPath());
								FileOutputStream fos = new FileOutputStream(msgM.getPath());
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
							//}else {
								//out.println("-ERROR incorrect command");
							//}
						}else {
							msgM.parseMessage(inputLine);
							if(msgM.getType() == Type.BYTES) {
								//System.out.println(outputLine);
								
								outSocket.getOutputStream().write(msgM.getBytes());
							}else {
								outputLine = msgM.getString();
								out.println(outputLine);
								//System.out.println(outputLine);
								
							}
						}
					}catch (Exception e) {
						e.printStackTrace();
					}
				}

				in.close();
				out.close();
				outSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
