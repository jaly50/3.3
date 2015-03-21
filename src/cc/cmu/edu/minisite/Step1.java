package cc.cmu.edu.minisite;

import io.undertow.server.HttpServerExchange;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;

public class Step1 {
	  static private List<Connection> connectionPool = new ArrayList<Connection>();  
		static private String jdbcDriver="com.mysql.jdbc.Driver";
		static private String jdbcURL ="jdbc:mysql://localhost:3306/step1";
		static private String tableName="user";
		 //  Database credentials
		   static final String user = "jialic";
		   static final String password = "scarlett";
		public static JSONObject getStep1(HttpServerExchange exchange) {
			//System.out.println(exchange.getQueryString());
			Map<String, Deque<String>> paras = exchange.getQueryParameters();
			try {
				String id = paras.get("id").getFirst();

				String pwd = paras.get("pwd").getFirst();
                
				String responseValue = querySQL(id,pwd);
				
				
				JSONObject response = new JSONObject();
				response.put("name", responseValue);
				
				
	            return response;
			} catch (Exception e) {
				System.out.println(e);
			}
			
			return null;
		}
		private static String querySQL(String id, String pwd) {
			Connection con = null;
	        try {
	        	con = getConnection();
	        	System.out.println("conncet to the database");
	        	PreparedStatement pstmt = con.prepareStatement("SELECT username FROM " + tableName + " WHERE id=? and pwd=?");
	        	pstmt.setInt(1,Integer.parseInt(id));
	        	pstmt.setString(2,pwd);
	        	ResultSet rs = pstmt.executeQuery();
	        	
	        	String username=null;
	            if (rs.next()) {
	            	username=rs.getString(1);
	            }
	        	
	        	
	        	rs.close();
	        	pstmt.close();
	        	releaseConnection(con);
	        	 return username;
	            
	        } catch (Exception e) {
	            try { if (con != null) con.close(); } catch (SQLException e2) { /* ignore */ }
	        	try {
					throw new Exception(e);
				} catch (Exception e1) {
					
					e1.printStackTrace();
					
				}
	        	return null;
	        }
		
		}
  
		
		private synchronized static  Connection getConnection() throws Exception {
			synchronized (connectionPool) {
			if (connectionPool.size() > 0) {
				return connectionPool.remove(connectionPool.size()-1);
			}
			}
			
	        try {
	            Class.forName(jdbcDriver);
	        } catch (ClassNotFoundException e) {
	            throw new Exception(e);
	        }

	        try {
	            return DriverManager.getConnection(jdbcURL,user,password);
	        } catch (SQLException e) {
	            throw new Exception(e);
	        }
			
		}
		
		private synchronized static  void releaseConnection(Connection con) {
			synchronized (connectionPool) {
			connectionPool.add(con);
			}
		}
}
