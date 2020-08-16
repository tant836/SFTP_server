package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SFTP_Server {
	private ServerSocket serverSocket;
	private Socket outSocket;
	private PrintWriter out;
	private BufferedReader in;

	public void start(int port) throws IOException {
		serverSocket = new ServerSocket(port);
		outSocket = serverSocket.accept();
		out = new PrintWriter(outSocket.getOutputStream(), true);
		in = new BufferedReader(new InputStreamReader(outSocket.getInputStream()));
		String greeting = in.readLine();
		if ("hello server".equals(greeting)) {
			out.println("hello client");
		} else {
			out.println("unrecognized greeting");
		}
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

		public ClientHandler(Socket socket) {
			this.outSocket = socket;
		}

		public void run() {
			try {
				out = new PrintWriter(outSocket.getOutputStream(), true);
				in = new BufferedReader(new InputStreamReader(outSocket.getInputStream()));

				String inputLine;
				String outputLine;
				MessageManagement msgM = new MessageManagement();
				while (true/*(inputLine = in.readLine()) != null*/) {
					inputLine = in.readLine();
					outputLine = msgM.parseMessage(inputLine);
					if ("done".equals(inputLine)) {
						out.println("+");
						break;
					}
					out.println(outputLine);
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
