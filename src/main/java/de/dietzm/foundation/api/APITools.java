package de.dietzm.foundation.api;

import spark.Request;

public class APITools {

    public static String readEnvAndQueryParameters(String env, Request req, String param){
        String value = System.getenv(env);
        if(req.queryParams().contains(param)){
            value = req.queryParams(param);
        }
        
        return value;
    }

    public static String readEnvAndQueryParametersOrDefault(String env, Request req, String param, String defaultValue){
        String value = readEnvAndQueryParameters(env, req, param);
        if(value == null || value.equals("")){
            value = defaultValue;
        }

        return value;
    }


}