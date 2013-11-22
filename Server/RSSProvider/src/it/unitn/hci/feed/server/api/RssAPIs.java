package it.unitn.hci.feed.server.api;

import it.unitn.hci.feed.DatabaseManager;
import it.unitn.hci.utils.ResponseUtils;
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
    @Path("/{coursename}/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response signupUser(@PathParam("coursename") String courseName, @PathParam("id") long id)
    {
        try
        {
            return ResponseUtils.fromObject(DatabaseManager.getAllFeedsAfterIdForCourse(id, courseName));
        }
        catch (Exception e)
        {
            return ResponseUtils.fromException(e);
        }
    }


    @GET
    @Path("/{coursename}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCourseFeeds(@PathParam("coursename") String courseName)
    {
        try
        {
            return ResponseUtils.fromObject(DatabaseManager.getFeeds(courseName));
        }
        catch (Exception e)
        {
            return ResponseUtils.fromException(e);
        }
    }


    @GET
    @Path("/departments/{department}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCourseOfDepartments(@PathParam("department") String department)
    {
        try
        {
            return ResponseUtils.fromObject(DatabaseManager.getCoursesOfDepartment(department));
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


    @GET
    @Path("/courses/{department}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDepartmentCourses(@PathParam("department") String departmentName)
    {
        try
        {
            return ResponseUtils.fromObject(DatabaseManager.getDepartmentCourses(departmentName));
        }
        catch (Exception e)
        {
            return ResponseUtils.fromException(e);
        }
    }


    @POST
    @Path("/users/signup")
    @Produces(MediaType.APPLICATION_JSON)
    public Response signupUser(@FormParam("token") String token)
    {
        try
        {
            DatabaseManager.signupUser(token);
            return ResponseUtils.ok();
        }
        catch (Exception e)
        {
            return ResponseUtils.fromException(e);
        }
    }

}
