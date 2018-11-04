package de.dietzm.gcs2hana.api;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.dietzm.gcs2hana.db.DatabaseOrganizer;


public class SearchAPI extends HttpServlet {

	private static final long serialVersionUID = 1l;

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String searchString = request.getParameter("q");

		try {

			//Get DB connection
			Connection con = DatabaseOrganizer.getInstance().getConnection();
			
			//Prepare the statement itself
			PreparedStatement prpStmt = con
					.prepareStatement("SELECT FILE_ID, FILE_NAME, FILE_PATH, FILE_SIZE, LAST_MODIF, " +
											 "TO_NVARCHAR( SNIPPETS(CONTENT) ) AS PREVIEW, " +
											 "SCORE() AS SCORE " +
											 "FROM FILE_DATA " +
											 "WHERE CONTAINS(*,?, Fuzzy(0.8)) " +
											 "ORDER BY SCORE");
			
			//Fill parameter and execute
			prpStmt.setString(1, searchString);
			ResultSet rs = prpStmt.executeQuery();

			//Convert ResultSet to JSON
			String json = convertResultSetToJSONString(rs);
			
			response.setContentType("applicationm/json");
			response.getWriter().print(json);

		} catch (Exception e) {
			System.out.println("ERROR" + e.toString());
			response.sendError(500);
		}

	}
	
	
	
	
	

	private String convertResultSetToJSONString(ResultSet rs) throws SQLException {

		int columnCount = rs.getMetaData().getColumnCount();

		StringBuffer buffer = new StringBuffer();
		buffer.append("[");

		int cnt = 0;
		
		while (rs.next()) {	
			
			if(cnt++ > 0)
				buffer.append(",");
			
			buffer.append("{");

			for (int i = 1; i <= columnCount; i++) {
				String column = rs.getMetaData().getColumnLabel(i);
				Object value = rs.getObject(i);
				
				
				if(rs.getMetaData().getColumnType(i) == java.sql.Types.NVARCHAR){
					String strVal = value.toString();
					value = strVal.replaceAll("\\\"", "'").replaceAll("\\\\", "/");
				}
				
				
				if (i > 1)
					buffer.append(",");
				
				buffer.append("\"" + column + "\":\"" + value + "\"");
			}
			
			buffer.append("}");
		} 
		
		buffer.append("]");
		
		return buffer.toString();
	}
}
