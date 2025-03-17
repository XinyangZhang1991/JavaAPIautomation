package SoloPactice;


import common.Environment;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.restassured.RestAssured.given;

public class data_replacement {

    public static void main(String[] args) {

        String data_to_extract = "{\"token\":\"access_token\"}";
        String Whole_data_needs_tobe_replaced = "{\"Content-Type\":\"application/json; charset=UTF-8\",\"Authorization\": \"bearer{{token}}\"}";

        Response response_from_login_attempt = given()
                .header("Content-Type", "application/json")
                .body("{\"principal\":\"lemon70\",\"credentials\":\"12345678\",\"appType\":3,\"loginType\":0}")
                .when()
                .post("http://shop.lemonban.com:8107/login")
                .then()
                .log().all()
                .extract().response();
        System.out.println("Response Body: " + response_from_login_attempt.getBody().asString());
// --------------------------Data extraction example ----------------------------------------------
        //jsonPath() parses the JSON response body and returns an object that allows you to query or extract data from the response using JSONPath expressions.
        // This method is particularly useful when dealing with structured JSON data, allowing you to directly access specific elements or values.
        JsonPath jsonPath_Response = response_from_login_attempt.jsonPath();
        Object result = jsonPath_Response.get("access_token");
        System.out.println("access_token:" + result);
        HashMap<String, Object> extractMap = jsonToMap(data_to_extract);
        System.out.println("deserilisations data " + extractMap);
        //keySet() is a method provided by the Map interface (which HashMap implements).
        // This method returns a Set of all the keys present in the map.
        //The Set contains only the keys and does not include the values.
        // Each key in the set will be unique, as a Set automatically removes duplicates.
        Set<String> keys = extractMap.keySet();
        System.out.println(keys);
        for (String key : keys) {
            Object value_in_excel = extractMap.get(key);
            System.out.println(value_in_excel);
            //String Conversion (+ ""): By appending an empty string ("") to the value_in_excel object, you're explicitly converting it to a String type.
            // This is necessary because jsonPath_Response.get() requires a string argument to execute the query properly
            Object result_from_response = jsonPath_Response.get(value_in_excel + "");
            System.out.println(result_from_response);
            Environment.env.put(key, result_from_response);
            System.out.println("Environment HashMap: " + Environment.env);
        }

// --------------------------------Data replacement example -----------------------------------
        Pattern pattern = Pattern.compile("\\{\\{(.*?)\\}\\}");
        Matcher matcher = pattern.matcher(Whole_data_needs_tobe_replaced);
        while (matcher.find()) {
            String substring = matcher.group();
            System.out.println("正则找到得总数据："+matcher.group());
            String variable_name_needs_to_replaced_from_env = matcher.group(1);
            System.out.println("正则找到得（）里面得数据："+matcher.group(1));
            Object new_value_from_env = Environment.env.get(variable_name_needs_to_replaced_from_env);
            System.out.println("环境变量拿到得比配数据："+matcher.group(1));
            //value + "" 通过拼接转化为字符串
            Whole_data_needs_tobe_replaced = Whole_data_needs_tobe_replaced.replace(substring, new_value_from_env + "");
            System.out.println("替换完的数据为："+Whole_data_needs_tobe_replaced);

        }

    }
//above are all Main method
    public static HashMap<String, Object> jsonToMap(String str) {

        ObjectMapper objectMapper = new ObjectMapper();
        HashMap<String, Object> map = null;
        try {
            map = objectMapper.readValue(str, HashMap.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return map;
    }
}
