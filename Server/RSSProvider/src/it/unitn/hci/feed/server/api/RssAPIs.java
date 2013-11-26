package it.unitn.hci.feed.server.api;

import java.util.ArrayList;
import it.unitn.hci.feed.DatabaseManager;
import it.unitn.hci.feed.common.models.Course;
import it.unitn.hci.utils.ResponseUtils;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/rssservice")
public class RssAPIs
{

    @GET
    @Path("/{coursename}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFeeds(@PathParam("coursename") String courseName, @QueryParam("lastReceivedId") Long id)
    {
        try
        {
            if (id != null) return ResponseUtils.fromObject(DatabaseManager.getFeeds(id, courseName));
            return ResponseUtils.fromObject(DatabaseManager.getFeeds(courseName));

        }
        catch (Exception e)
        {
            return ResponseUtils.fromException(e);
        }
    }


    @GET
    @Path("/departments/{departmentName}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDepartmentCourses(@PathParam("departmentName") String department)
    {
        try
        {
            // TODO temp hack
            // return ResponseUtils.fromObject(DatabaseManager.getDepartmentCourses(department));

            ArrayList<Course> l = new ArrayList<Course>();
            l.add(new Course(1, "AAAA", 123123, null));
            l.add(new Course(2, "BBBB", 123123, null));
            l.add(new Course(3, "CCCC", 123123, null));

            return ResponseUtils.fromObject(l);
        }
        catch (Exception e)
        {
            return ResponseUtils.fromException(e);
        }
    }


    @GET
    @Path("/departments")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDepartments()
    {
        try
        {
            return ResponseUtils.fromObject(DatabaseManager.getDepartments());
        }
        catch (Exception e)
        {
            return ResponseUtils.fromException(e);
        }
    }

}
