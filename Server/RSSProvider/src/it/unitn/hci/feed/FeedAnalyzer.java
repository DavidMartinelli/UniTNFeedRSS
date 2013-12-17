package it.unitn.hci.feed;

import java.util.Collection;
import it.unitn.hci.feed.common.models.Alias;
import it.unitn.hci.feed.common.models.Course;
import it.unitn.hci.feed.common.models.Department;
import it.unitn.hci.feed.common.models.Feed;

public class FeedAnalyzer
{
    public static Feed extract(String body, long timeStamp, Department department) throws Exception
    {
        Course target = null;
        for (Course course : department.getCourses())
        {
            final String officialName = course.getName();
            final Collection<Alias> aliases = course.getAliases();
            final String lowerBody = body.toLowerCase();

            if (lowerBody.contains(officialName.replace('_', ' ').toLowerCase()))
            {
                target = course;
            }
            else
            {
                for (Alias alias : aliases)
                {
                    if (lowerBody.contains(alias.getValue().replace('_', ' ').toLowerCase()))
                    {
                        target = course;
                        break;
                    }
                }
            }

            if (target != null) break;
        }
        return new Feed(-1, body, timeStamp, target == null ? DatabaseManager.getGenericDepartmentCourse(department) : target);
    }
}
