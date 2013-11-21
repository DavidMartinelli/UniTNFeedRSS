package it.unitn.hci.feed.server.api;

import java.util.List;
import it.unitn.hci.feed.DatabaseManager;
import it.unitn.hci.feed.common.models.Feed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/RssService")
public class TestAPI
{
    @GET
    @Path("/{courseName}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Feed> test(@PathParam("courseName") String courseName)
    {
        return DatabaseManager.getFeeds(courseName);
    }
}
