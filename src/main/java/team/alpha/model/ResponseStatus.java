package team.alpha.model;

public class ResponseStatus {

    public static final int OK = 200;
    public static final int USER_CREATED = 201;
    public static final int USERNAME_CONFLICT = 409;
    public static final int USER_UNAUTHORIZED = 401;
    public static final int SERVER_ERROR = 500;

    private ResponseStatus() {
        //cant be instantiated
    }
}
