package com.aero.rsamessenger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnector
{
	private static final String CONNECTION_STRING = "jdbc:sqlserver://ERTUÐRUL-PC\\SQLSERVER;server:port;databaseName=RsaMessengerDB;Integrated Security=True;";
	// private static final String ROOT_USERNAME = "UserName";
	// private static final String ROOT_PASSWORD = "12345";
	
	// Connection ve Statement nesneleri
	Connection connection = null;
	Statement statement = null;
	
	String errorMessage = "";
	
	public DatabaseConnector()
	{
		try
		{
			// Koda driver sinifini yukluyoruz.
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
			this.errorMessage += e.getMessage();
		}
	}
	
	public boolean JoinUser(User user)
	{
		boolean success = false;
		
		try
		{
			// Baglantiyi aciyoruz.
			connection = DriverManager.getConnection(CONNECTION_STRING);
			
			// Sorgu calistirabilmek icin kullanilan özel ifadelerden biridir statement.
			// Burada statement nesnesini aliyoruz.
			statement = connection.createStatement();
			
			// Kullanici kaydi icin gerekli sorgunun calistirilmasi
			String parameters = user.phoneNumber + ", " + user.emailAdress + ", " + user.publicKey.N() + ", " + user.publicKey.E();
			String sorgu = "INSERT INTO tUsers(phoneNumber, email, publicKeyN, publicKeyE) values(" + parameters + ")";
			statement = connection.createStatement();
			statement.executeUpdate(sorgu);
			
			success = true;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			this.errorMessage += e.getMessage();
			success = false;
		}
		finally
		{
			try
			{
				// Mutlaka her uygulamada veritabani ile isimiz bittiginde baglantiyi kapatmaliyiz.
				// Baglanti ve statementi kapatiyoruz.
				connection.close();
				statement.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
				this.errorMessage += e.getMessage();
			}
		}
		
		return success;
		
	}
	
}
