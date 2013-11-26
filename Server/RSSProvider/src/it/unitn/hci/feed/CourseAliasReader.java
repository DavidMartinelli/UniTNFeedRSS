package it.unitn.hci.feed;

import it.unitn.hci.feed.common.models.Course;
import it.unitn.hci.feed.common.models.Department;
import it.unitn.hci.utils.StreamUtils;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CourseAliasReader
{

    private static final String COURSE_SEPARATOR = "=";
    private static final String ALIAS_SEPARATOR = "-";
    private static final String DEPARTMENT_SEPARATOR = "<>";


    private CourseAliasReader()
    {
        // static methods only
    }


    public static List<Course> getAliases() throws Exception
    {
        final File f = new File("resources/default_aliases.txt");
        FileInputStream stream = null;
        try
        {
            stream = new FileInputStream(f);
            List<Course> courses = null;
            String currentCourse = null;
            List<Department> department = null;
            Set<String> aliases = null;
            for (String line : StreamUtils.readLines(stream))
            {
                if (line.replace(" ", "").isEmpty()) continue;

                if (line.startsWith(COURSE_SEPARATOR))
                {
                    if (courses == null) courses = new ArrayList<Course>();
                    else courses.add(new Course(-1, currentCourse, -1, aliases, department));

                    department = new ArrayList<Department>();
                    aliases = new HashSet<String>();
                    final String[] split = line.split(DEPARTMENT_SEPARATOR);
                    for (int i = 0; i < split.length; i++)
                    {
                        if (i == 0) currentCourse = split[0].substring(1);
                        else department.add(new Department(split[i], null, null));
                    }
                }
                else if (line.startsWith(ALIAS_SEPARATOR) && currentCourse != null)
                {
                    final String alias = line.substring(1);
                    aliases.add(alias);
                }
            }

            if (courses == null) return null;
            else courses.add(new Course(-1, currentCourse, -1, aliases, department));

            return courses;
        }
        finally
        {
            if (stream != null) stream.close();
        }

    }


    public static void main(String[] argv) throws Exception
    {
        List<Course> list = getAliases();
        System.out.println(list);
        System.out.println(list.size());
    }
}
