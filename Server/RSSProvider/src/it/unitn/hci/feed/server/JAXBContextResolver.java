package it.unitn.hci.feed.server;

import it.unitn.hci.feed.common.models.Course;
import it.unitn.hci.feed.common.models.Department;
import it.unitn.hci.feed.common.models.Feed;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.JAXBContext;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.api.json.JSONJAXBContext;

@SuppressWarnings("rawtypes")
@Provider
public class JAXBContextResolver implements ContextResolver<JAXBContext>
{
    private final JAXBContext context;
    private final Set<Class> types;
    private Class[] ctypes = { Course.class, Department.class, Feed.class }; //your pojo class


    @SuppressWarnings("unchecked")
    public JAXBContextResolver() throws Exception
    {
        System.out.println("ciaocaicoaiociaocaiociaociacoacoaciaociacoaicpia");
        this.types = new HashSet(Arrays.asList(ctypes));
        this.context = new JSONJAXBContext(JSONConfiguration.natural().build(), ctypes); //json configuration
    }


    @Override
    public JAXBContext getContext(Class<?> objectType)
    {
        return (types.contains(objectType)) ? context : null;
    }
}
