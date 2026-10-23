package twitter;

import java.time.Instant;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Main {

    public static void main(String[] args) {
        try {
            assert false;
            throw new Error("Always run main and tests with assertions enabled");
        } catch (AssertionError ae) { }

        // Offline sample tweets (the MIT server is dead). Reconstructed to match
        // the follower counts described in the Lab 07 guidebook.
        final List<Tweet> tweets = Arrays.asList(
            new Tweet(1, "alyssa", "is it reasonable to talk about rivest so much?", Instant.parse("2016-02-17T10:00:00Z")),
            new Tweet(2, "bbitdiddle", "rivest talk in 30 minutes #hype", Instant.parse("2016-02-17T11:00:00Z")),
            new Tweet(3, "alyssa", "@bbitdiddle saving you a seat in 32-123", Instant.parse("2016-02-17T11:20:00Z")),
            new Tweet(4, "evaluator", "@alyssa are you coming to office hours?", Instant.parse("2016-02-17T12:00:00Z")),
            new Tweet(5, "cy_d_fect", "anyone started pset 1 yet? mail me at cy@mit.edu", Instant.parse("2016-02-17T13:40:00Z")),
            new Tweet(6, "cy_d_fect", "@bbitdiddle @rivest can you explain problem 3?", Instant.parse("2016-02-17T14:00:00Z")),
            new Tweet(7, "alyssa", "test-first programming actually saved me an hour on the PSET", Instant.parse("2016-02-17T16:30:00Z")),
            new Tweet(8, "evaluator", "@rivest great lecture today", Instant.parse("2016-02-17T16:45:00Z")),
            new Tweet(9, "ben-b", "@alyssa @rivest @bbitdiddle see you all at the review session", Instant.parse("2016-02-17T17:00:00Z")),
            new Tweet(10, "louis_r", "@Alyssa did you finish ps1?", Instant.parse("2016-02-17T17:30:00Z")),
            new Tweet(11, "cy_d_fect", "@ALYSSA thanks for the tip", Instant.parse("2016-02-17T18:00:00Z")),
            new Tweet(12, "rivest", "reminder: no lecture on monday", Instant.parse("2016-02-17T18:30:00Z")),
            new Tweet(13, "louis_r", "@louis_r note to self: start early", Instant.parse("2016-02-17T19:00:00Z"))
        );

        System.err.println("fetched " + tweets.size() + " tweets");

        final Timespan span = Extract.getTimespan(tweets);
        System.err.println("ranging from " + span.getStart() + " to " + span.getEnd());

        final Set<String> mentionedUsers = Extract.getMentionedUsers(tweets);
        System.err.println("covers " + mentionedUsers.size() + " Twitter users");

        System.err.println("\ntweets written by alyssa:");
        for (Tweet t : Filter.writtenBy(tweets, "alyssa")) {
            System.err.println(t.toString());
        }

        System.err.println("\ntweets containing 'rivest' or 'pset':");
        for (Tweet t : Filter.containing(tweets, Arrays.asList("rivest", "pset"))) {
            System.err.println(t.toString());
        }

        // ---- Lab 07: social network ----
        final Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(tweets);
        final List<String> influencers = SocialNetwork.influencers(followsGraph);

        System.err.println("\ntop 10 influencers:");
        for (int i = 0; i < Math.min(10, influencers.size()); i++) {
            String user = influencers.get(i);
            int count = 0;
            for (Set<String> follows : followsGraph.values()) {
                Set<String> lower = new HashSet<>();
                for (String name : follows) {
                    lower.add(name.toLowerCase());
                }
                if (lower.contains(user.toLowerCase())) {
                    count++;
                }
            }
            System.err.println((i + 1) + ". " + user + " (" + count + " followers)");
        }
    }
}