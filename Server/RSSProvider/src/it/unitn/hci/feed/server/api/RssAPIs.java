package it.unitn.hci.feed.server.api;

import it.unitn.hci.feed.DatabaseManager;
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
    @Path("/{courseId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFeeds(@PathParam("courseId") int courseId, @QueryParam("lastReceivedId") Long id)
    {
        try
        {
            if (id != null) return ResponseUtils.fromObject(DatabaseManager.getFeeds(id, courseId));
            return ResponseUtils.fromObject(DatabaseManager.getFeeds(courseId));

        }
        catch (Exception e)
        {
            return ResponseUtils.fromException(e);
        }
    }


    @GET
    @Path("/departments/{departmentId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDepartmentCourses(@PathParam("departmentId") int departmentId)
    {
        try
        {
            return ResponseUtils.fromObject(DatabaseManager.getCourses(departmentId));
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
