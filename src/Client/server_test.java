package Client;

import static org.junit.Assert.assertEquals;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.junit.Test;

public class server_test {
	@Test
	public void test_user_login_with_acc_and_password() {
		SFTP_Client client = new SFTP_Client();
		try {
			client.startConnection("127.0.0.1", 115);
			String response = client.sendMessage("user dkfsl");// non-existent user
			assertEquals("-Invalid user-id, try again", response);
			
			response = client.sendMessage("user tom");//existing user
			assertEquals("+User-id valid, send account and password", response);
			
			response = client.sendMessage("acct sddsf");// non-existent account
			assertEquals("-Invalid account, try again", response);
			
			response = client.sendMessage("acct acc1");
			assertEquals("+Account valid, send password", response);
			
			response = client.sendMessage("pass pasddss");//incorrect password
			assertEquals("-Wrong password, try again", response);
			
			response = client.sendMessage("pass pass");
			assertEquals("! Logged in", response);
			
			response = client.sendMessage("done");
			assertEquals('+', response.charAt(0));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void test_cdir() {
		SFTP_Client client = new SFTP_Client();
		try {
			client.startConnection("127.0.0.1", 115);
			String response = client.sendMessage("user tom");//existing user
			assertEquals("+User-id valid, send account and password", response);
			
			response = client.sendMessage("acct acc1");
			assertEquals("+Account valid, send password", response);
			
			response = client.sendMessage("pass pass");
			assertEquals("! Logged in", response);
			
			response = client.sendMessage("list f");
			assertEquals("+ .\\userTomTestDir\n" + 
					">folder\n" + 
					"test.txt\n" + 
					"testCap.PNG\n", response);
			
			response = client.sendMessage("cdir folder");
			assertEquals("! Changed working dir to .\\userTomTestDir\\folder", response);
			
			response = client.sendMessage("list v");
			assertEquals("+ .\\userTomTestDir\\folder\n" + 
					"Capture2.PNG 28 Aug 2020 01:12:03 GMT 78kB\n", response);
			
			response = client.sendMessage("cdir ddjs");
			assertEquals("-Can't connect to directory because: directory not found", response);
			
			response = client.sendMessage("done");
			assertEquals('+', response.charAt(0));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void test_reName() {
		SFTP_Client client = new SFTP_Client();
		try {
			client.startConnection("127.0.0.1", 115);
			String response = client.sendMessage("user tom");//existing user
			assertEquals("+User-id valid, send account and password", response);
			
			response = client.sendMessage("acct acc1");
			assertEquals("+Account valid, send password", response);
			
			response = client.sendMessage("pass pass");
			assertEquals("! Logged in", response);
			
			response = client.sendMessage("list f");
			assertEquals("+ .\\userTomTestDir\n" + 
					">folder\n" + 
					"test.txt\n" + 
					"testCap.PNG\n", response);
			
			response = client.sendMessage("name testCap.PNG");
			assertEquals("+File exists", response);
			
			response = client.sendMessage("tobe newName.PNG");
			assertEquals("+testCap.PNG renamed to newName.PNG", response);
			
			response = client.sendMessage("list f");
			assertEquals("+ .\\userTomTestDir\n" + 
					">folder\n" + 
					"newName.PNG\n" + 
					"test.txt\n", response);
			
			response = client.sendMessage("name newName.PNG");
			assertEquals("+File exists", response);
			
			response = client.sendMessage("tobe testCap.PNG");
			assertEquals("+newName.PNG renamed to testCap.PNG", response);
			
			response = client.sendMessage("done");
			assertEquals('+', response.charAt(0));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void test_kill() {
		SFTP_Client client = new SFTP_Client();
		try {
			client.startConnection("127.0.0.1", 115);
			String response = client.sendMessage("user tom");//existing user
			assertEquals("+User-id valid, send account and password", response);
			
			response = client.sendMessage("acct acc1");
			assertEquals("+Account valid, send password", response);
			
			response = client.sendMessage("pass pass");
			assertEquals("! Logged in", response);
			
			response = client.sendMessage("list f");
			assertEquals("+ .\\userTomTestDir\n" + 
					">folder\n" + 
					"test.txt\n" + 
					"testCap.PNG\n", response);
			
			File file = new File("./userTomTestDir/testCap.PNG");
			byte[] mybytearray = new byte[(int) file.length()];
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
			bis.read(mybytearray, 0, mybytearray.length);
			bis.close();
			
			response = client.sendMessage("kill testCap.PNG");
			assertEquals("+testCap.PNG deleted", response);
			
			response = client.sendMessage("list f");
			assertEquals("+ .\\userTomTestDir\n" + 
					">folder\n" + 
					"test.txt\n", response);
			
			FileOutputStream fos = new FileOutputStream(file);
		    BufferedOutputStream bos = new BufferedOutputStream(fos);
		    bos.write(mybytearray);
		    bos.close();
		    
		    response = client.sendMessage("list f");
			assertEquals("+ .\\userTomTestDir\n" + 
					">folder\n" + 
					"test.txt\n" + 
					"testCap.PNG\n", response);
			
			response = client.sendMessage("done");
			assertEquals('+', response.charAt(0));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void test_retr() {
		SFTP_Client client = new SFTP_Client();
		try {
			client.startConnection("127.0.0.1", 115);
			String response = client.sendMessage("user tom");//existing user
			assertEquals("+User-id valid, send account and password", response);
			
			response = client.sendMessage("acct acc1");
			assertEquals("+Account valid, send password", response);
			
			response = client.sendMessage("pass pass");
			assertEquals("! Logged in", response);
			
			response = client.sendMessage("list f");
			assertEquals("+ .\\userTomTestDir\n" + 
					">folder\n" + 
					"test.txt\n" + 
					"testCap.PNG\n", response);
			
			response = client.sendMessage("retr testCap.PNG");
			assertEquals("file size: 80040", response);
			
			response = client.sendMessage("send");
			assertEquals("file saved as testCap.PNG", response);
			
			File file = new File("testCap.PNG");
			assertEquals(file.exists(), true);
			assertEquals(file.delete(), true);
			
			response = client.sendMessage("done");
			assertEquals('+', response.charAt(0));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void test_stor() {//needs this to take into account the stor type
		SFTP_Client client = new SFTP_Client();
		try {
			client.startConnection("127.0.0.1", 115);
			String response = client.sendMessage("user tom");//existing user
			assertEquals("+User-id valid, send account and password", response);
			
			response = client.sendMessage("acct acc1");
			assertEquals("+Account valid, send password", response);
			
			response = client.sendMessage("pass pass");
			assertEquals("! Logged in", response);
			
			response = client.sendMessage("list f");
			assertEquals("+ .\\userTomTestDir\n" + 
					">folder\n" + 
					"test.txt\n" + 
					"testCap.PNG\n", response);
			
			response = client.sendMessage("stor new test.jpg");
			assertEquals("+File does not exist, will create new file\n" + 
					"+ok, waiting for file", response);
			
			response = client.sendMessage("list f");
			assertEquals("+ .\\userTomTestDir\n" + 
					">folder\n" + 
					"test.jpg\n" + 
					"test.txt\n" + 
					"testCap.PNG\n", response);
			
			response = client.sendMessage("kill test.jpg");
			assertEquals("+test.jpg deleted", response);
			
			response = client.sendMessage("list f");
			assertEquals("+ .\\userTomTestDir\n" + 
					">folder\n" + 
					"test.txt\n" + 
					"testCap.PNG\n", response);
			
			response = client.sendMessage("done");
			assertEquals('+', response.charAt(0));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}

/*
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
*/