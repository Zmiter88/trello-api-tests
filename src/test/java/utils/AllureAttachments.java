package utils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.Allure;
import java.nio.charset.StandardCharsets;

public class AllureAttachments {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static void attachRequest(String name, Object body) {
        Allure.addAttachment(name, "application/json", toJson(body), ".json");
    }

    public static void attachResponse(String name, String body) {
        Allure.addAttachment(name, "application/json", body, ".json");
    }

    private static String toJson(Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            return "{ \"error\": \"cannot serialize object\" }";
        }
    }
}
