package it.unitn.hci.feed.server.api;

import it.unitn.hci.feed.PollingEngine;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import com.google.gson.Gson;

@Path("/RssService")
public class TestAPI
{
    @GET
    @Path("/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public String test(@PathParam("id") String id)
    {
        return new Gson().toJson(PollingEngine.getCache()).toString();
    }
}
