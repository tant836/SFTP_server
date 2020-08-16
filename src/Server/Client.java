package Server;

import java.io.File;

public class Client {
	enum Type{
		ASCII, BINARY, CONTINUOUS
	}
	
	private String user;
	private String account;
	private boolean noPassword;
	private Type type;
	public String getAccount() {
		return account;
	}

	private File directory;
	
	
	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public boolean isNoPassword() {
		return noPassword;
	}

	public void setNoPassword(boolean noPassword) {
		this.noPassword = noPassword;
	}

	public Client(String user) throws Exception {//add the directory path to this constructor
		
		DatabaseUtil.UserStatus response = DatabaseUtil.checkUser(user);
		
		if(response != DatabaseUtil.UserStatus.WRONG) {
			
			if(response != DatabaseUtil.UserStatus.GOOD) {
				noPassword = true;
			}else {
				noPassword = false;
			}
			
			this.user = user;
		}else {
			this.user = null;
		}
	}
	
	public boolean setAccount(String account) throws Exception {//throw an exception
		if(DatabaseUtil.checkAccount(user, account)) {
			this.account = account;
			return true;
		}else {
			return false;
			//throw error probably
		}
	}
	
	public boolean validatePassword(String password) throws Exception {
		if(DatabaseUtil.validatePassword(user, password)) {
			return true;
		}else {
			return false;
		}
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}
	
	public String listFiles(boolean verbose ) {
		
		// Populates the array with names of files and directories
		File[] files;
		files = directory.listFiles();

		// For each pathname in the pathnames array
		if(verbose) {
			for (File file : files) {
				// Print the names of files and directories
				System.out.println(file.getName() + " " + file.lastModified() + " " + file.length());
			}
		}else {
			for (File file : files) {
				// Print the names of files and directories
				System.out.println(file.getName());
			}
		}
		return "coming soon";
	}
	
	public String changeDirectory(String newDir) {
		//verify directory is ok
		directory = new File(newDir);
		
		return "coming soon";
	}
	
	/*
	public Client user(String user) throws Exception {//more specific about the exception 
		
		if(DatabaseUtil.checkUser(user)) {
			return new Client(user);
		}else {
			return new Client(null);
		}
	}
	*/

}
