package Server;

import java.io.File;

public class MessageManagement {
	public Client client;
	
	public enum Type{
		BYTES, STRING, RECEIVING
	}
	
	private Type type;
	
	String stringMessage;
	byte[] bytesMessage;


	public void parseMessage(String message) {
		message = message.replaceAll("/[\\[\\]']+/g", "").replaceAll("\\s{2,}", " ").trim();
		String[] splitMessage = message.split(" ");

		switch (splitMessage[0].toLowerCase()) {
		case "user":
			
			type = Type.STRING;
			stringMessage = doUser(splitMessage[1]);
			break;

		case "acct":
			
			type = Type.STRING;
			stringMessage = doACCT(splitMessage[1]);
			break;

		case "pass":
			
			type = Type.STRING;
			stringMessage = doPass(splitMessage[1]);
			break;

		case "type":
			
			type = Type.STRING;
			stringMessage = doType(splitMessage[1]);
			break;

		case "list":

			type = Type.STRING;
			stringMessage = doList(splitMessage[1]);
			break;
			
		case "cdir":

			type = Type.STRING;
			stringMessage = doCdir(splitMessage[1]);
			break;
			
		case "kill":

			type = Type.STRING;
			stringMessage = doKill(splitMessage[1]);
			break;
			
		case "name":

			type = Type.STRING;
			stringMessage = doName(splitMessage[1]);
			break;
			
		case "tobe":

			type = Type.STRING;
			stringMessage = doTobe(splitMessage[1]);
			break;
			
		case "retr":

			type = Type.STRING;
			stringMessage = doRetr(splitMessage[1]);
			break;
			
		case "send":

			type = Type.BYTES;
			bytesMessage = doSend();
			break;
			
		case "stop":

			type = Type.STRING;
			stringMessage = doStop(splitMessage[1]);
			break;
			
		case "stor":

			type = Type.STRING;
			stringMessage = doStor(splitMessage[1], splitMessage[2]);
			break;
		}

	}
	
	public String getString() {
		return stringMessage;
	}
	
	public byte[] getBytes() {
		return bytesMessage;
	}
	
	public Type getType() {
		return type;
	}
	
	public String getUser() {
		if(client == null || client.getUser() == null) {return "";}
		return client.getUser();
	}
	
	private String doName(String name) {
		if (client == null) {
			return "not logged in with user";
		}
		return client.startNameChange(name);
	}
	
	private String doTobe(String newName) {
		if (client == null) {
			return "not logged in with user";
		}
		return client.tobe(newName);
	}
	
	private String doRetr(String path) {
		if (client == null) {
			return "not logged in with user";
		}
		
		return client.RETR(path);
	} 
	
	private byte[] doSend() {
		return client.send();
	}
	
	private String doStop(String path) {
		return client.stop();
	}
	
	private String doStor(String type, String path) {
		return client.store(type, path);
	}
	
	public File getPath() {
		return client.getPath();
	}

	private String doUser(String username) {
		try {
			client = new Client(username);
			if (client.getUser() != null) {
				if (client.isNoPassword()) {
					client.changeAbsDirectory(DatabaseUtil.getDirectory(username));
					return "!" + client.getUser() + " logged in";
				} else {
					return "+User-id valid, send account and password";
				}
			} else {
				return "-Invalid user-id, try again";
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "ERROR occured probably accessing data base";
		}
	}

	private String doACCT(String ACCT) {
		if (client == null) {
			return "not logged in with user";
		}
		try {
			if (client.setAccount(ACCT)) {
				if (client.isNoPassword()) {
					client.changeAbsDirectory(DatabaseUtil.getDirectory(client.getUser(), ACCT));
					return "! Account valid, logged-in";
				} else {
					return "+Account valid, send password";
				}
			} else {
				return "-Invalid account, try again";
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "ERROR occured probably accessing data base";
		}
	}

	private String doPass(String pass) {
		if (client == null) {
			return "not logged in with user";
		}
		try {
			if (client.validatePassword(pass)) {
				client.changeAbsDirectory(DatabaseUtil.getDirectory(client.getUser(), client.getAccount()));
				return "! Logged in";
			} else {
				return "-Wrong password, try again";
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "ERROR occured probably accessing data base";
		}
	}

	private String doType(String type) {
		if (client == null) {
			return "not logged in with user";
		}

		switch (type) {
		case "A":
			client.setType(Client.Type.ASCII);
			return "+Using Ascii mode";
		case "B":
			client.setType(Client.Type.BINARY);
			return "+Using Binary mode";
		case "C":
			client.setType(Client.Type.CONTINUOUS);
			return "+Using Continuous mode";
		default:
			return "Type not valid";
		}
	}

	private String doList(String type) {
		if (client == null) {
			return "not logged in with user";
		}
		//File f = new File("C:\\Users\\bergn\\Desktop");

		// Populates the array with names of files and directories
		

		return client.listFiles(type.equals("v"));
	}
	
	private String doCdir(String newPath) {
		if (client == null) {//start loggin process
			return "not logged in with user";
		}
		return client.changeDirectory(newPath);
	}
	
	private String doKill(String fileName) {
		if (client == null) {//start loggin process
			return "not logged in with user";
		}
		
		
		return client.deleteFile(fileName);
	}
}
