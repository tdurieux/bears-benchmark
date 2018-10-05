package csse.auth;

public class SecurityConstants {

    public static final String SECRET = "SecretKeyToGenJWTs";
    public static final long EXPIRATION_TIME = 1_800_000; // 10 days->8640_000_000 *30 mins->1_800_000
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_UP_URL = "/users/signup";
//    public static final String LOGIN_URL = "/users/login";
//    public static final String USER_LIST_URL = "/users/list";
//    public static final String GET_USER_URL = "/users/{username}";
//    public static final String SEARCH_URL= "/users/search/{EID}";
//    public static final String DEACTIVATE_URL = "/users/deactivate}"; 
//    public static final String EDIT_USER_URL = "/users/update/{username}";
//    public static final String RESET_PASSWORD_URL ="/users/resetpassword/{username}";
//    public static final String FORGOT_URL="/users/forgotpassword/{username}";
}