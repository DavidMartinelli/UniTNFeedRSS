package it.unitn.hci.feed;

import java.util.List;
import java.util.Map.Entry;
import it.unitn.hci.feed.common.models.Course;
import it.unitn.hci.feed.common.models.Feed;

public class FeedAnalyzer
{
    public static Feed extract(String body)
    {
        String subject = Course.GENERIC_COURSE_NAME;
        for (Entry<String, List<String>> course : CourseAliasReader.getCache().entrySet())
        {
            final String officialName = course.getKey();
            final List<String> aliases = course.getValue();
            aliases.add(officialName);

            final String lowerBody = body.toLowerCase();
            for (String alias : aliases)
            {
                if (lowerBody.contains(alias.replace('_', ' ').toLowerCase()))
                {
                    subject = alias;
                    break;
                }
            }
            
            if (!subject.equals(Course.GENERIC_COURSE_NAME)) break;
        }
        return new Feed(-1, body, System.currentTimeMillis(), Course.notCached(subject));
    }
}
