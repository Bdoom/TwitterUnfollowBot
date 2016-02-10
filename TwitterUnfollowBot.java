import twitter4j.IDs;
import twitter4j.Relationship;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterUnfollowBot {

	private Twitter twitter;

	private void initConfiguration() {
		
		ConfigurationBuilder builder = new ConfigurationBuilder();
		builder.setOAuthConsumerKey("MbD0FQ548RpxEIoMcfJ7zrttd");
		builder.setOAuthConsumerSecret("b7NZ95t3MwGlwjJlXxkrLqw7KtGbzJ8MN7dUVqEWYKmsuk8TZG");
		builder.setOAuthAccessToken("4075713333-xNjbd90p3r1w0ywH39Idfc7sxLWy8jOkuJsOVeF");
		builder.setOAuthAccessTokenSecret("ms1pEZtt9UIyRgTxoUg3kWjpdcsiULw3UKi79K04yRBW6");
		Configuration configuration = builder.build();
		TwitterFactory factory = new TwitterFactory(configuration);
		twitter = factory.getInstance();
		removeNotFollowed();

	}
	
	private void removeNotFollowed() {
		long[] longIds = null;
		int unfollowedPeople = 0;
		
		try {
			IDs ids = twitter.getFriendsIDs(-1);
			longIds = ids.getIDs();
		} catch (TwitterException e) {
			e.printStackTrace();
		}
		
		System.out.println("Friendships: " + longIds.length);
		
		for (long l : longIds) {
			try {
				Relationship r = twitter.showFriendship(twitter.getId(), l);
				if (!r.isTargetFollowingSource()) {
					twitter.destroyFriendship(l);
					unfollowedPeople++;

					System.out.println("Tweeters unfollowed " + unfollowedPeople);
				}
				if (r.isTargetFollowingSource() && !r.isSourceFollowingTarget()) {
					twitter.createFriendship(l);
				}
			} catch (IllegalStateException | TwitterException e) {
				e.printStackTrace();
			}			
		}
	}

	

	public static void main(String[] args) {
		new TwitterUnfollowBot().initConfiguration();
	}
}