package it.unitn.hci.feed;

import it.unitn.hci.utils.StreamUtils;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class CourseAliasReader
{

    private static final String COURSE_SEPARATOR = "=";
    private static final String ALIAS_SEPARATOR = "-";

    private static Map<String, List<String>> mCache;


    private CourseAliasReader()
    {
        // static methods only
    }


    public static Map<String, List<String>> getCache()
    {
        return mCache;
    }


    public static Map<String, List<String>> getAliases() throws Exception
    {
        final File f = new File("resources/default_aliases.txt");
        FileInputStream stream = null;
        try
        {
            stream = new FileInputStream(f);
            final Map<String, List<String>> aliases = new Hashtable<String, List<String>>();
            String currentCourse = null;
            for (String line : StreamUtils.readLines(stream))
            {
                if (line.startsWith(COURSE_SEPARATOR))
                {
                    currentCourse = line.substring(1);
                }
                else if (line.startsWith(ALIAS_SEPARATOR) && currentCourse != null)
                {
                    final String alias = line.substring(1);
                    List<String> a = aliases.get(currentCourse);
                    if (a == null) a = new ArrayList<String>();
                    a.add(alias);
                    aliases.put(currentCourse, a);
                }
            }

            mCache = aliases;
            return aliases;
        }
        finally
        {
            if (stream != null) stream.close();
        }

    }
}
