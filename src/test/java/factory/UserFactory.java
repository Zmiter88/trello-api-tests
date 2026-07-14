package factory;

import config.ConfigProperties;
import model.Role;
import model.User;

public class UserFactory {

    public static User owner() {
        return User.builder()
                .name("owner")
                .apiKey(ConfigProperties.OWNER_API_KEY)
                .apiToken(ConfigProperties.OWNER_API_TOKEN)
                .role(Role.OWNER)
                .build();
    }

    public static User member() {
        return User.builder()
                .name("secondUser")
                .apiKey(ConfigProperties.SECOND_USER_API_KEY)
                .apiToken(ConfigProperties.SECOND_USER_API_TOKEN)
                .role(Role.MEMBER)
                .build();
    }

    public static User invalidApiKey() {
        return User.builder()
                .name("invalidApiKeyUser")
                .apiKey(ConfigProperties.INVALID_USER_API_KEY)
                .apiToken(ConfigProperties.OWNER_API_TOKEN)
                .role(Role.INVALID)
                .build();
    }

    public static User invalidApiToken() {
        return User.builder()
                .name("invalidTokenUser")
                .apiKey(ConfigProperties.OWNER_API_KEY)
                .apiToken(ConfigProperties.INVALID_USER_API_TOKEN)
                .role(Role.INVALID)
                .build();
    }

    public static User missingApiKey() {
        return User.builder()
                .name("missingApiKey")
                .apiKey(null)
                .apiToken(ConfigProperties.OWNER_API_TOKEN)
                .role(Role.INVALID)
                .build();
    }

    public static User missingApiToken() {
        return User.builder()
                .name("missingToken")
                .apiKey(ConfigProperties.OWNER_API_KEY)
                .apiToken(null)
                .role(Role.INVALID)
                .build();
    }
}
