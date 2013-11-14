package it.unitn.hci.feed;

import it.unitn.hci.feed.models.Feed;
import it.unitn.hci.feed.models.Subject;

public class FeedAnalyzer
{
    public Feed extract(String body)
    {
        Subject subject = Subject.GENERIC;
        for (Subject s : Subject.values())
        {
            if (s == Subject.GENERIC) continue;
            
            if (body.toUpperCase().contains(s.toString().replace('_', ' ')))
            {
                subject = s;
                break;
            }
        }

        return new Feed(-1, body, System.currentTimeMillis(), subject);
    }
}
