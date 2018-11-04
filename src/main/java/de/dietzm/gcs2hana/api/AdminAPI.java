package de.dietzm.gcs2hana.api;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.dietzm.gcs2hana.loader.FileLoaderAndTransferer;

public class AdminAPI extends HttpServlet {
	private static final long serialVersionUID = 1L;
      
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String action = request.getParameter("action");
		
		if(action == null)
			action = "";
		
		if(action.equalsIgnoreCase("reindex")){
			FileLoaderAndTransferer flt = new FileLoaderAndTransferer("C:\\Users\\D043918\\Documents\\20 HANA Knowledge\\");
			flt.setOutput(response.getWriter());
			try {
				flt.executeFileLoading();
			} catch (Exception e) {
				response.sendError(500);
			}
		} 
		
	}

	

}
