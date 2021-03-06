package fish.payara.conference.app.config;

/**
 * Application constants.
 */
public final class Constants {

    public final static String AUTHORIZATION_HEADER = "Authorization";

    public final static String BEARER_PREFIX = "Bearer ";

    //Regex for acceptable logins
    public static final String LOGIN_REGEX = "^[_'.@A-Za-z0-9-]*$";

    //Regex for acceptable emails
    public static final String EMAIL_REGEX = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?";

    public static final String EMAIL_REGEX_MESSAGE = "{invalid.email}";

    public static final String INCORRECT_PASSWORD_MESSAGE = "Incorrect password";

    private Constants() {
    }
}
