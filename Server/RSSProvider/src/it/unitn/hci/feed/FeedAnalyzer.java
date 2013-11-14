package it.unitn.hci.feed;

import it.unitn.hci.feed.models.Feed;
import it.unitn.hci.feed.models.Course;

public class FeedAnalyzer
{
    public Feed extract(String body)
    {
        Course subject = Course.GENERIC;
        for (Course s : Course.values())
        {
            if (s == Course.GENERIC) continue;
            
            if (body.toUpperCase().contains(s.toString().replace('_', ' ')))
            {
                subject = s;
                break;
            }
        }

        return new Feed(-1, body, System.currentTimeMillis(), subject);
    }
}
