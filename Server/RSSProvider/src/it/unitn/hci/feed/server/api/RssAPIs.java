package it.unitn.hci.feed.server.api;

import java.util.List;
import it.unitn.hci.feed.DatabaseManager;
import it.unitn.hci.feed.common.models.Feed;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/rssservice")
public class RssAPIs
{
    @GET
    @Path("/{coursename}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Feed> getCourseFeeds(@PathParam("courseame") String courseName)
    {
        return DatabaseManager.getFeeds(courseName);
    }


    @GET
    @Path("/departments")
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> getDepartments()
    {
        return DatabaseManager.getDepartments();
    }


    @GET
    @Path("/courses/{department}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> getDepartmentCourses(@PathParam("department") String departmentName)
    {
        return DatabaseManager.getDepartmentCourses(departmentName);
    }


    @POST
    @Path("/users/signup")
    @Produces(MediaType.APPLICATION_JSON)
    public Response signupUser(@FormParam("token") String token)
    {
        DatabaseManager.signupUser(token);
        return Response.ok().build();
    }
}
