import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.FilterQuery;

public final class TweetGet {
	private static OperateDB db;
	private static TweetFilter filter;
	private static TweetGet tg;
	
	private TweetGet() {
		
	}
	public static void start() {
		db = new OperateDB();
		try {
			db.connect();
		} catch (Exception e) {
			Common.writeLog("Database Error", e);
		}
		tg = new TweetGet();
		tg.getTweetStream();
    }
	public static void stop() {
		if (db != null) {
			db.close();
		}
	}
    private void getTweetStream() {
    	ConfigurationBuilder cb = new ConfigurationBuilder();
    	cb.setDebugEnabled(true)
          .setOAuthConsumerKey(Credentials.oauthConsumerKey)
          .setOAuthConsumerSecret(Credentials.oauthConsumerSecret)
          .setOAuthAccessToken(Credentials.oauthAccessToken)
          .setOAuthAccessTokenSecret(Credentials.oauthAccessTokenSecret);
    	
    	filter = new TweetFilter();
        TwitterStream twitterStream = new TwitterStreamFactory(cb.build()).getInstance();
        StatusListener listener = new StatusListener() {
            @Override
            public void onStatus(Status status) {
            	if (status.getGeoLocation() != null) {
            		String keyword = filter.keywordMatch(status.getText());
            		TweetData tweetData = new TweetData(status.getId(),
            											status.getUser().getId(),
            											status.getUser().getScreenName(),
            											status.getText(),
            											status.getGeoLocation().getLongitude(),
            											status.getGeoLocation().getLatitude(),
            											status.getCreatedAt(),
            											keyword);
            		try {
						db.addRowToTweetTable(tweetData);
					} catch (Exception e) {
						Common.writeLog("Database Error", e);
					}
            	}
            }

            @Override
            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
            }

            @Override
            public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
            }

            @Override
            public void onScrubGeo(long userId, long upToStatusId) {
            }

            @Override
            public void onStallWarning(StallWarning warning) {
            }

            @Override
            public void onException(Exception ex) {
            }
        };
        FilterQuery tweetFilter = new FilterQuery();
        String[] keywords = {"ball", "hockey", "athlete", "soccer", "sports", "playground", "nba", "olympic", "tennis", "gym", "race", 
        		"tech", "computer", "hardware", "software", "google", "facebook", "apple", "tesla", "hi-tech", "technology", "silicon", "iwatch", "iphone", "electronic",
        		"music", "concert", "jazz", "guitar", "rhythm", "rock", "dance","opera", "blues", "hip-hop", "itunes", "pandora", "singer", "grammy",
        		"cinema", "movie", "film", "theatre","show","screen", "action","trailer","scene", "oscar", "amc",
        		"eat", "pizza", "yummy", "food", "pasta", "noodle", "rice", "tea", "steak", "lunch", "dinner", "breakfast", "brunch"
        };
        tweetFilter.track(keywords);
        twitterStream.addListener(listener);
        twitterStream.filter(tweetFilter);
    }
}