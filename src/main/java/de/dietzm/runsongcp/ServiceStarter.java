package de.dietzm.runsongcp;


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
        
        Spark.get("/hello", new AnonymousWelcomeService());
        
        Spark.get("/hello/:name", (request, response) -> {
            return "Welcome " + request.params(":name");
        });
    }


   
}
