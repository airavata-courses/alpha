package team.alpha;

public abstract class Constants {
    private Constants() {
    }

    //error messages
    public static final String MSG_INVALID_CREDENTIALS = "Either username or password is invalid";
    public static final String MSG_FAILED_TO_FETCH_USER = "Error while fetching user data";
    public static final String MSG_FAILED_TO_CREATE_USER = "Error while creating user data";
    public static final String MSG_USER_ALREADY_EXISTS = "That username has been taken. Please try another one.";
    public static final String MSG_FAILED_TO_FETCH_SUBSCRIBER_LIST = "Error while fetching subscribers list";
}
