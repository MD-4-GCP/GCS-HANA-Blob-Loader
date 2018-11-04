package de.dietzm.runsongcp;

import spark.Request;
import spark.Response;
import spark.Route;

public class AnonymousWelcomeService implements Route {

    @Override
    public Object handle(Request request, Response response) throws Exception {
        return "Welcome Anonymous";
    }
}
