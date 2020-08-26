package Server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;



public class DatabaseUtil {
	Connection c;
	Statement statement;
	
	enum UserStatus {
		  GOOD,
		  NEED_PASSWORD,
		  WRONG
	}
	
	public static UserStatus checkUser(String user) throws Exception {//synchronize at some point make another method that can call one of these functions so that it's properly synchronized
		Connection c;
		PreparedStatement statement;
		
		try {
			
	         Class.forName("org.sqlite.JDBC");
	         c = DriverManager.getConnection("jdbc:sqlite:user_data.db");
	         
	         String sql = "SELECT * FROM user_pass WHERE USER=?;";
	         statement = c.prepareStatement(sql);
	         statement.setString(1, user);
	         ResultSet rs = statement.executeQuery();
	         //statement.executeUpdate(sql);
	         
	         if(rs.next()) {
	        	 //rs.close();
	        	 if(rs.getString("PASS").contentEquals("") || rs.getString("PASS").contentEquals("null")) {
	        		 statement.close();
			         c.close();
		        	 return UserStatus.GOOD;
	        	 }else {
	        		 statement.close();
			         c.close();
		        	 return UserStatus.NEED_PASSWORD;
	        	 }
	         }else {
	        	 //rs.close();
	        	 statement.close();
		         c.close();
	        	 return UserStatus.WRONG;
	         }
	         
	         
	         
	      } catch ( Exception e ) {
	    	  e.printStackTrace();
	         System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	         System.exit(0);
	      }
		
		return UserStatus.WRONG;
	}
	
	public static String getDirectory(String user, String account) {//TODO: change these to actually do something
		return "\\Users\\bergn\\Desktop";
	}
	
	public static String getDirectory(String user) {
		return "\\Users\\bergn\\Desktop";
	}
	
	public static boolean checkAccount(String user, String account) throws Exception{
		
		Connection c;
		PreparedStatement statement;
		
		try {
			
	         Class.forName("org.sqlite.JDBC");
	         c = DriverManager.getConnection("jdbc:sqlite:user_data.db");
	         
	         String sql = "SELECT * FROM user_acc WHERE USER=? and ACC=?;";
	         statement = c.prepareStatement(sql);
	         statement.setString(1, user);
	         statement.setString(2, account);
	         ResultSet rs = statement.executeQuery();
	         //statement.executeUpdate(sql);
	         
	         if(rs.next()) {
	        	 //rs.close();
	        	 statement.close();
		         c.close();
	        	 return true;
	         }else {
	        	 //rs.close();
	        	 statement.close();
		         c.close();
	        	 return false;
	         }
	         
	         
	         
	      } catch ( Exception e ) {
	    	  e.printStackTrace();
	         System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	         System.exit(0);
	      }
		
		return false;
	}
	
	public static boolean validatePassword(String user, String password) throws Exception {
		
		Connection c;
		PreparedStatement statement;
		
		try {
			
	         Class.forName("org.sqlite.JDBC");
	         c = DriverManager.getConnection("jdbc:sqlite:user_data.db");
	         
	         String sql = "SELECT * FROM user_pass WHERE USER=? and PASS=?;";
	         statement = c.prepareStatement(sql);
	         statement.setString(1, user);
	         statement.setString(2, password);
	         ResultSet rs = statement.executeQuery();
	         //statement.executeUpdate(sql);
	         
	         if(rs.next()) {
	        	 //rs.close();
	        	 statement.close();
		         c.close();
	        	 return true;
	         }else {
	        	 //rs.close();
	        	 statement.close();
		         c.close();
	        	 return false;
	         }
	         
	         
	         
	      } catch ( Exception e ) {
	    	  e.printStackTrace();
	         System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	         System.exit(0);
	      }
		
		return false;
	}
}
