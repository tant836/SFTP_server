package Server;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;

public class Client {
	enum Type{
		ASCII, BINARY, CONTINUOUS
	}
	
	private String user;
	private String account;
	private boolean noPassword;
	private Type type;
	private Path directory;
	private File fileToBeReNamed;
	private File fileToBeSent;
	private String fileToSave;
	
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
	
	public String startNameChange(String fileName) {
		
		fileToBeReNamed = new File(directory.toFile().getPath() + "/" + fileName);
		
		if(fileToBeReNamed.exists()) {
			return "+File exists";
		}else {
			return "-Can't find " + fileName;
		}
	}
	
	public String tobe(String newName) {
		File newFileName = new File(directory.toFile().getPath() + "/" + newName);
		String oldName = fileToBeReNamed.getName();
		if(fileToBeReNamed != null) {
			if(fileToBeReNamed.renameTo(newFileName)) {
				String tmp = "+" + oldName + " renamed to " + newFileName.getName();
				fileToBeReNamed = null;
				return tmp;
			}else {
				return "- File wasn't renamed because of an error";
			}
		}else {
			return "- File wasn't renamed because of an initial file wasn't selected";
		}
	}

	public String listFiles(boolean verbose ) {//TODO list files differently from folders
		
		// Populates the array with names of files and directories
		File[] files;
		if(directory == null) {
			return null;
		}
		files = directory.toFile().listFiles();
		String message = "+ " + directory.toString() + "\n";
		System.out.println(message);
		Date time;
		String dir = "";
		// For each pathname in the pathnames array
		if(verbose) {
			for (File file : files) {
				// Print the names of files and directories
				//System.out.println(file.getName() + " " + file.lastModified() + " " + file.length());
				time = new Date(file.lastModified());
				if(file.isDirectory()) {
					dir = ">";
				}else {
					dir = "";
				}
				message += dir + file.getName() + " " + time.toGMTString() + " " + (file.length()/1024) + "kB\n";
			}
			//System.out.println(message);
		}else {
			for (File file : files) {
				if(file.isDirectory()) {
					dir = ">";
				}else {
					dir = "";
				}
				// Print the names of files and directories
				//System.out.println(file.getName());
				message += dir + file.getName() + "\n";
			}
			//System.out.println(message);
		}
		return message;
	}
	
	public String changeDirectory(String newDir) {
		//verify directory is ok
		
		if(directory == null) {
			directory = directory.resolve(newDir);
		}else {
			return "-Error occurred";
		}
		return "! Changed working dir to " + directory.toString();
	}
	
	public String changeAbsDirectory(String newDir) {
		//verify directory is ok
		
		directory = FileSystems.getDefault().getPath(newDir);
		
		return "! Changed working dir to " + directory.toString();
	}
	
	public String deleteFile(String path) {
		
		File file_to_delte = directory.resolve(path).toFile();
		
		if(file_to_delte.delete()) {
			return "+ " + path + " deleted";
		}else {
			return "-Not deleted an error occurred";
		}
	}
	
	public String RETR(String path) {
		fileToBeSent = directory.resolve(path).toFile();
		if(fileToBeSent.exists()) {
			return Long.toString(fileToBeSent.length());
		}else {
			return "-File doesn't exist";
		}
	}
	
	public String store(String type, String path) {
		fileToSave = path;
		System.out.println(fileToSave);
		return "+File exists, will create new generation of file";
	}
	
	public File getPath() {
		return directory.resolve(fileToSave).toFile();
	}
	
	public byte[] send() {
		
		if(fileToBeSent == null) {
			return null;
		}
		
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
	
	public String stop() {
		fileToBeSent = null;
		return "+ok, RETR aborted";
	}
	
	public String getAccount() {
		return account;
	}

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
	
	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}
}
