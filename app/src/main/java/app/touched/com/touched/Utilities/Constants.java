package app.touched.com.touched.Utilities;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Anshul on 2/24/2018.
 */

public class Constants {

    public static final String USERS_NODE="users";
    public static final String USERS_Details_NODE="user_details";
    public static final String IS_LOGIN_NODE="is_login";
    public static final String LAST_ONLINE_TIME_NODE="last_online_time";
    public static final String USER_LAST_ONLINE_TIME_NODE="last_online_time";
    public static final String MSG_COUNT_NODE="msg_count";
    public static final String EXPLORE_FRAGMENT="exploreFragment";
    public static final String LEADERBOARD_FRAGMENT="leaderboardFragment";
    public static final String MESSAGE_FRAGMENT="messageFragment";
    public static final String FACEBOOK_URL="https://graph.facebook.com/";
    public static final String SPLASH_SCREEN_NODE="splasScreen";
    public static final String IS_OTHER="isOtherUser";
    public static final String MALE="male";
    public static final String FEMALE="female";
    public static final String LOCATION_NAME="location/name";
    public static final String FRIENDS_TAG="friend_details";
    public static final String MSG_NODE="messages";
    public static final String IS_ONLINE="true";



    public static final int MY_MESSAGE = 0, OTHER_MESSAGE = 1, MY_IMAGE_MSG = 2, OTHER_IMAGE_MSG = 3;

}
