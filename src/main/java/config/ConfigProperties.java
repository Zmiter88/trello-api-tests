package config;

public class ConfigProperties {

    public static final String BASE_URI = System.getProperty("baseUri");
    public static final String OWNER_API_KEY = System.getProperty("owner.apiKey");
    public static final String OWNER_API_TOKEN = System.getProperty("owner.apiToken");
    public static final String OWNER_MEMBER_ID = System.getProperty("owner.memberId");
    public static final String SECOND_USER_API_KEY = System.getProperty("secondUser.apiKey");
    public static final String SECOND_USER_API_TOKEN = System.getProperty("secondUser.apiToken");
    public static final String SECOND_USER_MEMBER_ID = System.getProperty("secondUser.memberId");
    public static final String INVALID_USER_API_KEY = System.getProperty("invalid.apiKey");
    public static final String INVALID_USER_API_TOKEN = System.getProperty("invalid.apiToken");
}
