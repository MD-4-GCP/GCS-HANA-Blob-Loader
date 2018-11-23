package de.dietzm.gcs2hana;


import de.dietzm.foundation.gcp.AuthenticationManager;
import de.dietzm.foundation.gcp.HealthCheckAPI;
import de.dietzm.gcs2hana.services.SyncAPI;
import spark.Spark;


/**
 * Hello world!
 *
 */
public class ServiceStarter 
{
    public static void main( String[] args ) throws Exception
    {   
        Spark.port(8080);
        Spark.init();
        
        AuthenticationManager authMan = new AuthenticationManager();
    
        Spark.path("health", new HealthCheckAPI());
        
        Spark.path("/api", () -> {
        	  
        	//Check Authorization
            Spark.before("/*", (q, a) -> {
            	a.header("Content-Type", "application/json");
            	if(!authMan.isAuthorized(q)) {
        			Spark.halt(401, "Not authorized for API");
        		}
        	});
            
            SyncAPI sys = new SyncAPI();
            Spark.post("/control/syncfiles", sys::syncFilesFromGCSToHANA);

        });
    }


   
}
