package com.prism.Product_Finder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.UnsupportedEncodingException;
import java.rmi.ConnectIOException;
import java.sql.*;

/*
CREATE DATABASE shop_image_database DEFAULT CHARACTER SET utf8

****shop table ***
CREATE TABLE SHOP_TABLE(
SHOP_ID bigint(20) unsigned NOT NULL AUTO_INCREMENT,
CATEGORY VARCHAR(255) NOT NULL,
SHOP_NAME VARCHAR(255) NOT NULL,
URL mediumtext NOT NULL,
HTML MEDIUMBLOB NOT NULL,
PARSING_STATUS INT(10) unsigned NOT NULL,
SCRP_DATE datetime,
PRIMARY KEY (SHOP_ID)
)CHARACTER SET utf8;
*/
public class DBConfig {
	public static String DATABASE_NAME = "MYSQL";
//	public static String DATABASE_NAME = "ORACLE";
	public String db_address = "106.245.232.34";
	public String db_port = "3306";
	private String db_name = "shop_image_database";
	private String db_user = "whitepoo";
	private String db_pwd = "zx82qm73";


	public void INSERT_SHOP_TABLE(String CATEGORY,
								  String SHOP_NAME,
								  String URL,
								  String html,
								  int parse_status,
								  String SCRP_DATE)
	{
		try
		{
			Connection con = null;
			Statement stmt = null;
			ResultSet rs = null;
			if(DBConfig.DATABASE_NAME.equals("MYSQL") == true)
			{
				Class.forName("org.gjt.mm.mysql.Driver");
				con = DriverManager.getConnection("jdbc:mysql://"+db_address + ":" + db_port +"/" +db_name +"?useUnicode=true&characterEncoding=utf-8" , db_user, db_pwd);
			}

			FileWriter fw = new FileWriter("/tmp/" + SHOP_NAME+"_html.txt");
			fw.write(html+"\n");
			fw.flush();
			fw.close();

			File Full_file = new File("/tmp/" + SHOP_NAME+"_html.txt");
			FileInputStream Full_inputStream= new FileInputStream(Full_file);

			stmt = con.createStatement();

			PreparedStatement statement = con.prepareStatement("INSERT INTO SHOP_TABLE (CATEGORY, SHOP_NAME, URL, HTML, PARSING_STATUS, SCRP_DATE) VALUES (?,?,?,?,?,?)");

			statement.setString(1, CATEGORY);
			statement.setString(2, SHOP_NAME);
			statement.setString(3, URL);
			statement.setBlob(4, Full_inputStream);
			statement.setInt(5, parse_status);
			statement.setString(6, SCRP_DATE);
			statement.executeUpdate();

//			sql += "INSERT INTO SHOP_TABLE ";
//			sql += "(CATEGORY, SHOP_NAME, URL, HTML, PARSING_STATUS, SCRP_DATE)";
//			sql += "VALUES ( ";
//			sql += "'아쿠아슈즈',"+"";
//			sql += "'(주)롯데닷컴',"+"";
//			sql += "'" + URL+"',";
//			sql += "'--',";
//			sql += parse_status+",";
//			sql += "'" + SCRP_DATE+"')";
//			System.out.println(sql);
//			stmt.executeUpdate(sql);
//			stmt.close();
//			con.close();
			Full_inputStream.close();
			statement.close();
			con.close();

			File html_file = new File("/tmp/" + SHOP_NAME+"_html.txt");
			html_file.delete();

		}
		catch(ClassNotFoundException cnfe)
		{
			System.out.println("oracle.jdbc.driver.OracleDriver?몴占? 筌≪뼚?뱽 占쎈땾 占쎈씨占쎈뮸占쎈빍占쎈뼄.");
		}
			catch(SQLException sql)
		{
			System.out.println(sql.getMessage());
		}
			catch(Exception e)
		{
			System.out.println(e.toString());
		}

	}
	public int GET_SHOP_ID(String CATEGORY,
								  String SHOP_NAME,
								  String URL)
	{
		int shop_ID = -1;
		String sql = "";
		try
		{

			Connection con = null;
			Statement stmt = null;
			ResultSet rs = null;
			if(DBConfig.DATABASE_NAME.equals("MYSQL") == true)
			{
				Class.forName("org.gjt.mm.mysql.Driver");
				con = DriverManager.getConnection("jdbc:mysql://"+db_address + ":" + db_port +"/" +db_name +"?useUnicode=true&characterEncoding=utf-8" , db_user, db_pwd);
			}

			stmt = con.createStatement();

			sql += "SELECT SHOP_ID FROM SHOP_TABLE ";
			sql += "WHERE ";
			sql += "CATEGORY= '" + CATEGORY + "' AND ";
			sql += "SHOP_NAME= '" + SHOP_NAME + "' AND ";
			sql += "URL= '" + URL + "'";
			rs = stmt.executeQuery(sql);
			if(rs.next())
			{
				shop_ID = rs.getInt("SHOP_ID");
			}
			stmt.close();
			con.close();

		}
		catch(ClassNotFoundException cnfe)
		{
			//System.out.println("oracle.jdbc.driver.OracleDriver?몴占? 筌≪뼚?뱽 占쎈땾 占쎈씨占쎈뮸占쎈빍占쎈뼄.");
			return -1;
		}
		catch(SQLException esql)
		{
			//System.out.println(sql.getMessage());
			return -1;
		}
		catch(Exception e)
		{
			//System.out.println(e.toString());
			return -1;
		}
		finally
		{
			//					   //System.out.println("占쎄쉐?⑨옙!!");
			return shop_ID;
		}
	}
	/****REVIEW table ***
	CREATE TABLE REVIEW_TABLE(
	 SHOP_ID bigint(20) unsigned NOT NULL,
	REVIEW mediumtext NOT NULL)CHARACTER SET utf8;
	*/
	public void INSERT_REVIEW_TABLE(int SHOP_ID,
								  String REVIEW)
	{
		try
		{
			Connection con = null;
			Statement stmt = null;
			ResultSet rs = null;
			if(DBConfig.DATABASE_NAME.equals("MYSQL") == true)
			{
				Class.forName("org.gjt.mm.mysql.Driver");
				con = DriverManager.getConnection("jdbc:mysql://"+db_address + ":" + db_port +"/" +db_name +"?useUnicode=true&characterEncoding=utf-8" , db_user, db_pwd);
			}

			stmt = con.createStatement();
			String sql = "";
			sql += "INSERT INTO REVIEW_TABLE ";
			sql += "(SHOP_ID, REVIEW)";
			sql += "VALUES ( ";
			sql += "'" + SHOP_ID+"',";
			sql += "'" + REVIEW+"')";
			stmt.executeUpdate(sql);

			stmt.close();
			con.close();

		}
		catch(ClassNotFoundException cnfe)
		{
			//System.out.println("oracle.jdbc.driver.OracleDriver?몴占? 筌≪뼚?뱽 占쎈땾 占쎈씨占쎈뮸占쎈빍占쎈뼄.");
		}
		catch(SQLException sql)
		{
			//System.out.println(sql.getMessage());
		}
		catch(Exception e)
		{
			//System.out.println(e.toString());
		}
		finally
		{
			//					   //System.out.println("占쎄쉐?⑨옙!!");
		}
	}
	public int CHECK_EXIST_REVIEW(int SHOP_ID,
								   String REVIEW)
	{
		int rtn = 0;
		try
		{
			Connection con = null;
			Statement stmt = null;
			ResultSet rs = null;
			if(DBConfig.DATABASE_NAME.equals("MYSQL") == true)
			{
				Class.forName("org.gjt.mm.mysql.Driver");
				con = DriverManager.getConnection("jdbc:mysql://"+db_address + ":" + db_port +"/" +db_name +"?useUnicode=true&characterEncoding=utf-8" , db_user, db_pwd);
			}

			stmt = con.createStatement();
			String sql = "";
			sql += "SELECT SHOP_ID FROM REVIEW_TABLE ";
			sql += " WHERE SHOP_ID "+ SHOP_ID + " AND REVIEW = '" + REVIEW +"'";
			rs = stmt.executeQuery(sql);
			if(rs.next())
			{
				rtn = 1;
			}

			stmt.close();
			con.close();
			return rtn;
		}
		catch(ClassNotFoundException cnfe)
		{
			//System.out.println("oracle.jdbc.driver.OracleDriver?몴占? 筌≪뼚?뱽 占쎈땾 占쎈씨占쎈뮸占쎈빍占쎈뼄.");
		}
		catch(SQLException sql)
		{
			//System.out.println(sql.getMessage());
		}
		catch(Exception e)
		{
			//System.out.println(e.toString());
		}
		finally
		{
			//					   //System.out.println("占쎄쉐?⑨옙!!");
		}
		return rtn;
	}
	/*
	CREATE TABLE IMAGE_TABLE(SHOP_ID bigint(20) unsigned NOT NULL,
	IMAGE_NAME VARCHAR(40),
	Full_IMAGE_BINARY MEDIUMBLOB,
	Resize_IMAGE_BINARY MEDIUMBLOB
	)CHARACTER SET utf8;
	*/
	public void INSERT_IMAGE_TABLE(int SHOP_ID,
									String image_name,
									String Full_IMAGE,
								   String Resize_IMAGE
								   )
	{
		try
		{
			Connection con = null;
			Statement stmt = null;
			ResultSet rs = null;
			if(DBConfig.DATABASE_NAME.equals("MYSQL") == true)
			{
				Class.forName("org.gjt.mm.mysql.Driver");
				con = DriverManager.getConnection("jdbc:mysql://"+db_address + ":" + db_port +"/" +db_name +"?useUnicode=true&characterEncoding=utf-8" , db_user, db_pwd);
			}
			File Full_file = new File(Full_IMAGE);
			FileInputStream Full_inputStream= new FileInputStream(Full_file);

			File Resize_file = new File(Resize_IMAGE);
			FileInputStream Resize_inputStream= new FileInputStream(Resize_file);

			PreparedStatement statement = con.prepareStatement("INSERT INTO IMAGE_TABLE (SHOP_ID, IMAGE_NAME, Full_IMAGE_BINARY, Resize_IMAGE_BINARY) VALUES (?,?,?,?)");

			statement.setInt(1, SHOP_ID);
			statement.setString(2, image_name);
			statement.setBlob(3, Full_inputStream);
			statement.setBlob(4, Resize_inputStream);
			statement.executeUpdate();
			Resize_inputStream.close();
			Full_inputStream.close();

			statement.close();
			con.close();

			//TEMPORY FILE REMOVE
			Full_file.delete();
			Resize_file.delete();
		}
		catch(ClassNotFoundException cnfe)
		{
			System.out.println("oracle.jdbc.driver.OracleDriver?몴占? 筌≪뼚?뱽 占쎈땾 占쎈씨占쎈뮸占쎈빍占쎈뼄.");
		}
		catch(SQLException sql)
		{
			System.out.println(sql.getMessage());
		}
		catch(Exception e)
		{
			System.out.println(e.toString());
		}

	}

	public int CHECK_IMAGE_TABLE(int SHOP_ID,
								   String image_name)
	{
		int rtn = 0;
		try
		{
			Connection con = null;
			Statement stmt = null;
			ResultSet rs = null;
			if(DBConfig.DATABASE_NAME.equals("MYSQL") == true)
			{
				Class.forName("org.gjt.mm.mysql.Driver");
				con = DriverManager.getConnection("jdbc:mysql://"+db_address + ":" + db_port +"/" +db_name +"?useUnicode=true&characterEncoding=utf-8" , db_user, db_pwd);
			}

			stmt = con.createStatement();
			String sql = "";
			sql += "SELECT SHOP_ID FROM IMAGE_TABLE ";
			sql += " WHERE SHOP_ID ="+ SHOP_ID + " AND IMAGE_NAME = '" + image_name +"'";
			if(SHOP_ID == 2821)
			{
				System.out.println("2821");
			}
			rs = stmt.executeQuery(sql);
			if(rs.next())
			{
				rtn = 1;
			}
			stmt.close();
			con.close();

		}
		catch(ClassNotFoundException cnfe)
		{
			System.out.println("oracle.jdbc.driver.OracleDriver?몴占? 筌≪뼚?뱽 占쎈땾 占쎈씨占쎈뮸占쎈빍占쎈뼄.");
		}
		catch(SQLException sql)
		{
			System.out.println(sql.getMessage());
		}
		catch(Exception e)
		{
			System.out.println(e.toString());
		}

		return rtn;
	}
}

