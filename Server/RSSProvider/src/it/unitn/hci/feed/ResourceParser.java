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

public class ResourceParser
{

    private static final char COURSE_SEPARATOR = '=';
    private static final char ALIAS_SEPARATOR = '-';
    private static final char DEPARTMENT_SEPARATOR = '#';
    private static final String EOF = "EOF";


    private ResourceParser()
    {
        // static methods only
    }


    public static List<Department> getAliases() throws Exception
    {
        final File f = new File("resources/default_aliases.txt");
        FileInputStream stream = null;
        try
        {
            stream = new FileInputStream(f);

            String currentDepartment = null;
            String url = null;
            String currentCourse = null;

            List<Department> departments = new ArrayList<Department>();
            Set<Course> courses = new HashSet<Course>();
            Set<String> aliases = new HashSet<String>();

            int state = -1; // 0: department mode; 1: course mode

            List<String> lines = StreamUtils.readLines(stream);
            lines.add(EOF);
            
            for (String line : lines)
            {
                if (line.replace(" ", "").isEmpty()) continue;

                if (line.charAt(0) == DEPARTMENT_SEPARATOR || line.equals(EOF))
                {
                    if (state != -1)
                    {
                        Department d = new Department(currentDepartment, "TODOTODOELMExICO", url, courses);
                        departments.add(d);

                        courses = new HashSet<Course>();
                    }

                    if (line.equals(EOF)) break;

                    String[] part = line.substring(1).split("!");
                    currentDepartment = part[0];
                    url = part[1];
                    state = 0;
                }
                else if (line.charAt(0) == COURSE_SEPARATOR)
                {
                    if (state == 1)
                    {
                        courses.add(new Course(-1, currentCourse, Course.generateRandomColor(), aliases));
                        aliases = new HashSet<String>();
                    }
                    currentCourse = line.substring(1);
                    state = 1;
                }
                else if (line.charAt(0) == ALIAS_SEPARATOR)
                {
                    final String alias = line.substring(1);
                    aliases.add(alias);
                }
            }

            return departments;
        }
        finally
        {
            if (stream != null) stream.close();
        }

    }


    public static void main(String[] argv) throws Exception
    {
        List<Department> list = getAliases();
        for (Department department : list)
            System.out.println("\n=======\n" + department);
        System.out.println(list.size());
    }
}
