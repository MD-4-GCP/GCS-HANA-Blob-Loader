package de.dietzm.gcs2hana.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.http.HttpServlet;

import de.dietzm.foundation.api.APIResult;
import de.dietzm.foundation.api.APIResultBuilder;
import de.dietzm.foundation.api.APITools;
import de.dietzm.gcs2hana.base.DatabaseOrganizer;
import spark.Request;
import spark.Response;


public class SearchAPI extends HttpServlet {

	private static final long serialVersionUID = 1l;

	public APIResult syncFilesFromGCSToHANA(Request request, Response response) throws Exception {

		String destination = APITools.readEnvAndQueryParameters("HANA_DESTINATION_NAME", request, "Destination");
	
		String searchString = request.queryParams("q");

		try {

			//Get DB connection
			Connection con = DatabaseOrganizer.getInstance(destination).getConnection();
			
			//Prepare the statement itself
			PreparedStatement prpStmt = con
					.prepareStatement("SELECT FILE_ID, FILE_NAME, FILE_PATH, FILE_SIZE, LAST_MODIF, " +
											 "TO_NVARCHAR( SNIPPETS(CONTENT) ) AS PREVIEW, " +
											 "SCORE() AS SCORE " +
											 "FROM DOCS " +
											 "WHERE CONTAINS(*,?, Fuzzy(0.8)) " +
											 "ORDER BY SCORE");
			
			//Fill parameter and execute
			prpStmt.setString(1, searchString);
			ResultSet rs = prpStmt.executeQuery();

			//Convert ResultSet to JSON
			return APIResultBuilder.success(rs);

		} catch (Exception e) {
			return APIResultBuilder.errorAndHalt(500, e.toString());
		}

	}

}
