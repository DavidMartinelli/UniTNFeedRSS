package it.unitn.hci.utils;

import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.Collection;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

public class ResponseUtils
{
    private final static String ERROR_404 = "404";
    private final static String ERROR_400 = "400";
    private final static String ERROR_500 = "500";


    private ResponseUtils()
    {
        // static methods only
    }


    public static Response fromException(Exception e)
    {
        if (e instanceof FileNotFoundException) return Response.status(Status.NOT_FOUND).entity(ERROR_404 + ": " + e.getMessage()).build();
        if (e instanceof IllegalArgumentException) return Response.status(Status.BAD_REQUEST).entity(ERROR_400 + ": " + e.getMessage()).build();
        return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ERROR_500 + ": " + e.getMessage()).build();
    }


    public static <T> Response fromObject(T e)
    {
        if (e instanceof Collection<?>) return fromCollection((Collection<?>) e);
        return Response.ok().entity(e).build();
    }


    @SuppressWarnings("unchecked")
    private static <T> Response fromCollection(Collection<T> coll)
    {
        if (coll == null || coll.isEmpty()) return ok();
        T element = coll.iterator().next();
        T[] arr = (T[]) Array.newInstance(element.getClass(), coll.size());
        coll.toArray(arr);
        return ResponseUtils.fromObject(arr);
    }


    public static Response ok()
    {
        return Response.ok().build();
    }
}
