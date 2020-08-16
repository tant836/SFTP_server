package Server;

import java.io.File;

public class MessageManagement {
	public Client client;

	public String parseMessage(String message) {
		message = message.replaceAll("/[\\[\\]']+/g", "").replaceAll("\\s{2,}", " ").trim();
		String[] splitMessage = message.split(" ");

		switch (splitMessage[0].toLowerCase()) {
		case "user":

			return doUser(splitMessage[1]);

		case "acct":

			return doACCT(splitMessage[1]);

		case "pass":

			return doPass(splitMessage[1]);

		case "type":

			return doType(splitMessage[1]);

		case "list":

			return doList(splitMessage[1]);
			
		case "cdir":

			return doCdir(splitMessage[1]);
			
		case "kill":

			return doKill(splitMessage[1]);

		}
		return "void";

	}

	private String doUser(String username) {
		try {
			client = new Client(username);
			if (client.getUser() != null) {
				if (client.isNoPassword()) {
					client.changeDirectory(DatabaseUtil.getDirectory(username));
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
					client.changeDirectory(DatabaseUtil.getDirectory(client.getUser(), ACCT));
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
				client.changeDirectory(DatabaseUtil.getDirectory(client.getUser(), client.getAccount()));
				return "! Logged in";
			} else {
				return "-Wrong password";
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
		if (client == null) {
			return "not logged in with user";
		}
		return client.changeDirectory(newPath);
	}
	
	private String doKill(String fileName) {
		return "";
	}
}
