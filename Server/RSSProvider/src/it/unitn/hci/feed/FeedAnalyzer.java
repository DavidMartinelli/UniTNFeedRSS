package it.unitn.hci.feed;

import java.util.Set;
import it.unitn.hci.feed.common.models.Course;
import it.unitn.hci.feed.common.models.Feed;

public class FeedAnalyzer
{
    public static Feed extract(String body, long timeStamp) throws Exception
    {
        Course target = null;
        for (Course course : DatabaseManager.getAllCourses())
        {
            final String officialName = course.getName();
            final Set<String> aliases = course.getAliases();
            final String lowerBody = body.toLowerCase();

            aliases.add(officialName);

            for (String alias : aliases)
            {
                if (lowerBody.contains(alias.replace('_', ' ').toLowerCase()))
                {
                    target = course;
                    break;
                }
            }

            if (target != null) break;
        }
        return new Feed(-1, body, timeStamp, target == null ? Course.GENERIC_COURSE: target);
    }
}
