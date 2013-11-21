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
    public List<Feed> getCourseFeeds(@PathParam("coursename") String courseName) throws Exception
    {
        try{
        System.out.print("here" + courseName);
        return DatabaseManager.getFeeds(courseName);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    @GET
    @Path("/departments")
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> getDepartments() throws Exception
    {
        return DatabaseManager.getDepartments();
    }


    @GET
    @Path("/courses/{department}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> getDepartmentCourses(@PathParam("department") String departmentName) throws Exception
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
