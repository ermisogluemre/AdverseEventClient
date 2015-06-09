package edu.uams.ae.db;

import java.sql.Array;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;

public class dbconnector {
	private String DbType="";
	private String DbUrl="";
	private String DbUsername="";
	private String DbPassword="";
	private String DbName="";
	
	private Connection MainConnection;
	private Statement MainStatement;
	private DatabaseMetaData MainDatabaseMetaData;
	private ResultSet MainResultSet;
	
		
	public dbconnector(String DBTYPE, String URL,String DBName, String Username, String Password)
	{
		this.DbType = DBTYPE;
		this.DbUrl = URL;
		this.DbUsername = Username;
		this.DbPassword = Password;
		this.DbName = DBName;
		
		this.MainConnection = GetConnection();
		try {
			this.MainDatabaseMetaData = this.MainConnection.getMetaData();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			this.MainStatement = this.MainConnection.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	
	private Connection GetConnection()
	{
		String Type = this.DbType.toLowerCase();
		
		if(Type.equals("mysql"))
		{
			return GetMySQLConnection();
		}
		else if(Type.equals("postgresql"))
		{
			return GetPostgreSQLConnection();
		}
		else
		{
			
		}if(Type.equals("mssql"))
		{
			return GetMSSQLConnection();
		}

		return null;
	}
	
	private Connection GetPostgreSQLConnection()
	{
		
			try {
				Class.forName("org.postgresql.Driver"); // Load Postgre Driver
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} //load the driver
		
			try {
				return 	DriverManager.getConnection("jdbc:postgresql://"+this.DbUrl+"/"+this.DbName,this.DbUsername,this.DbPassword);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} //connect to the db
			return null;
		
	}
	
	private Connection GetMSSQLConnection()
	{
		
			try {
				Class.forName("net.sourceforge.jtds.jdbc.Driver"); // Load Postgre Driver
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} //load the driver
		
			try {
				return 	DriverManager.getConnection("jdbc:jtds:sqlserver://"+this.DbUrl+"/"+this.DbName,this.DbUsername,this.DbPassword);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} //connect to the db
			return null;
		
	}
	
	private Connection GetMySQLConnection()
	{
		try {
			Class.forName("com.mysql.jdbc.Driver"); // Load Postgre Driver
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} //load the driver
	
		try {
			return 	DriverManager.getConnection("jdbc:mysql://"+this.DbUrl+"/"+this.DbName,this.DbUsername,this.DbPassword);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} //connect to the db
		return null;
	}
	
	public ResultSet executeQuery(String Statement)
	{
		try {
			this.MainResultSet = this.MainStatement.executeQuery(Statement);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this.MainResultSet;
	}
	
	public void executeUpdate(String Statement)
	{
		try {
			this.MainStatement.executeUpdate(Statement);
		} catch (SQLException e) {             
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void Close()
	{
			
		try {
			if(this.MainResultSet != null)
				this.MainResultSet.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			if(this.MainStatement != null)
				this.MainStatement.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			this.MainConnection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public boolean Next()
	{
		try {
			return this.MainResultSet.next();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public Array getArray(int ColumnID)
	{
		try {
			return this.MainResultSet.getArray(ColumnID);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public Array getArray(String ColumnName)
	{
		try {
			return this.MainResultSet.getArray(ColumnName);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	
	}
	
	public Blob getBlob(int ColumID)
	{
		try {
			return this.MainResultSet.getBlob(ColumID);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}
	
	public Blob getBlob(String ColumName)
	{
		try {
			return this.MainResultSet.getBlob(ColumName);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}
	
	public boolean getBoolean(int ColumnID)
	{
		try {
			return this.MainResultSet.getBoolean(ColumnID);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean getBoolean(String ColumnName)
	{
		try {
			return this.MainResultSet.getBoolean(ColumnName);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public String getString(int ColumnID)
	{
		try {
			return this.MainResultSet.getString(ColumnID);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public String getString(String ColumnName)
	{
		try {
			return this.MainResultSet.getString(ColumnName);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public Date getDate(int ColumnID)
	{
		try {
			return this.MainResultSet.getDate(ColumnID);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public Date getDate(String ColumnName)
	{
		try {
			return this.MainResultSet.getDate(ColumnName);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public Time getTime(int ColumnID)
	{
		try {
			return this.MainResultSet.getTime(ColumnID);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public Time getTime(String ColumnName)
	{
		try {
			return this.MainResultSet.getTime(ColumnName);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public Timestamp getTimeStamp(int ColumnID)
	{
		try {
			return this.MainResultSet.getTimestamp(ColumnID);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public Timestamp getTimeStamp(String ColumnName)
	{
		try {
			return this.MainResultSet.getTimestamp(ColumnName);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public String getFormattedTimeStamp(String ColumnName)
	{
		Timestamp Temp = null;
		try {
			Temp = this.MainResultSet.getTimestamp(ColumnName);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		if(Temp != null)
		{
			String TempString = Temp.toString();
			
			String[] Splitted1 = TempString.split(" ");
			String[] Splitted2 = Splitted1[0].split("-");
			
			return Splitted2[1]+"/"+Splitted2[2]+"/"+Splitted2[0]+"    "+Splitted1[1];
		}
		
		return null;
	}
	
	public float getFloat(int ColumnID)
	{
		try {
			return this.MainResultSet.getFloat(ColumnID);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -99999999;
	}
	
	public float getFloat(String ColumnName)
	{
		try {
			return this.MainResultSet.getFloat(ColumnName);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -99999999;
	}
	
	public long getLong(int ColumnID)
	{
		try {
			return this.MainResultSet.getLong(ColumnID);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -999999999;
	}
	
	public long getLong(String ColumnName)
	{
		try {
			return this.MainResultSet.getLong(ColumnName);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -999999999;
	}
	
	public double getDouble(int ColumnID)
	{
		try {
			return this.MainResultSet.getDouble(ColumnID);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -99999999;
	}
	
	public double getDouble(String ColumnName)
	{
		try {
			return this.MainResultSet.getDouble(ColumnName);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -99999999;
	}
	
	public double getInt(String ColumnName)
	{
		try {
			return this.MainResultSet.getInt(ColumnName);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -99999999;
	}
	

}
