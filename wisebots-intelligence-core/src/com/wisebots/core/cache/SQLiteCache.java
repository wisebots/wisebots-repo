package com.wisebots.core.cache;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.HashMap;

public class SQLiteCache extends GameCache {

	public final static String CONSTRUCTOR_NAME = "sqlite";

	private Connection conn;


	public SQLiteCache(String game, String botname) {
		try {
			Class.forName("org.sqlite.JDBC");

			this.conn = DriverManager
					.getConnection("jdbc:sqlite:/wisebots/sqlite/" + game + "-"
							+ botname + ".db");
			Statement stm = this.conn.createStatement();
			stm.executeUpdate("DROP TABLE IF EXISTS Qvalues");
			stm.executeUpdate("CREATE TABLE Qvalues (state varchar(50) PRIMARY KEY NOT NULL, qvalue varchar(50));");
			stm.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public int size() {
		int qt = 0;
		Statement stm = null;
		try {
			stm = this.conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT count(*) FROM Qvalues");
			while (rs.next()) {
				qt = rs.getInt("1");
			}
			rs.close();
		} 
		catch (SQLException e) {
			return 0;
		}
		finally{
			try {
				stm.close();
			} 
			catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return qt;
	}

	public void clear() {
		Statement stm = null;
		try {
			stm = this.conn.createStatement();
			stm.executeUpdate("DROP TABLE IF EXISTS Qvalues");
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally{
			try {
				stm.close();
			} 
			catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void set(int[] key, Object obj) {
		Statement stm = null;
		String k = Arrays.toString(key);
		if(obj instanceof Double){
			Double v = (Double)obj;
			if(v.doubleValue() == 0){
				return;
			}
		}

		try {
			stm = this.conn.createStatement();
			boolean verify = verify(k);
			if(!verify){
				stm.executeUpdate("INSERT INTO Qvalues VALUES (\"" + k+ "\"," + obj.toString() + ")");
			}
			else{
				stm.executeUpdate("UPDATE Qvalues SET qvalue = \"" + obj.toString() + "\" WHERE state = \"" + obj.toString() + "\"");
			}
		} 
		catch (SQLException e) {
		      e.printStackTrace();
		}
		finally{
			try {
				stm.close();
			} 
			catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public boolean verify(String key) {
		Statement stm = null;
		try {
			stm = this.conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT qvalue FROM Qvalues where state = \""
							+ key + "\"");
			if (rs.next()) {
				return true;
			}
			rs.close();
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally{
			try {
				stm.close();
			} 
			catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return false;
	}

	public Object get(String key) {
		String qvalue = "0";
		Statement stm = null;
		try {
			stm = this.conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT qvalue FROM Qvalues where state = \""
							+ key + "\"");
			while (rs.next()) {
				qvalue = rs.getString("qvalue");
			}
			rs.close();
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally{
			try {
				stm.close();
			} 
			catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return qvalue;
	}

	public void delete(String key) {

	}

	public HashMap<String, Object> getContent() {
		return new HashMap<String, Object>();
	}

	public void close() {
		try {
			conn.close();
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Object getSpecified(Integer id, String method, int[] fstate) {
		// TODO Auto-generated method stub
		return null;
	}
}
