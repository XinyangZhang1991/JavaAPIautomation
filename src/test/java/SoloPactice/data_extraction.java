package SoloPactice;


import Day05.common.Environment;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Set;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;

public class data_extraction {

    public static void main(String[] args) {

      /*  given().
                header("Content-Type", "application/json; charset=UTF-8").
                body("{\"principal\":\"lemon70\",\"credentials\":\"12345678\",\"appType\":3,\"loginType\":0}");
        when().
                post("http://shop.lemonban.com:8107/login").
        then().
                log().all();*/

        String data_to_extract ="{\"token\":\"access_token\"}";
        String data_needs_tobe_replaced ="{\"Content-Type\":\"application/json; charset=UTF-8\",\"Authorization\": \"bearer{{token}}\"}";

        Response response_from_login_attempt = given()
                .header("Content-Type", "application/json")
                .body("{\"principal\":\"lemon70\",\"credentials\":\"12345678\",\"appType\":3,\"loginType\":0}")
                .when()
                .post("http://shop.lemonban.com:8107/login")
                .then()
                .log().all()
                .extract().response();
        System.out.println("Response Body: " + response_from_login_attempt.getBody().asString());

        //jsonPath() parses the JSON response body and returns an object that allows you to query or extract data from the response using JSONPath expressions.
        // This method is particularly useful when dealing with structured JSON data, allowing you to directly access specific elements or values.
        JsonPath jsonPath_Response =response_from_login_attempt.jsonPath();
        Object result = jsonPath_Response.get("access_token");
        System.out.println("access_token:"+result);
        HashMap<String, Object> extractMap = jsonToMap(data_to_extract);
        System.out.println("deserilisations data "+extractMap);
        //keySet() is a method provided by the Map interface (which HashMap implements).
        // This method returns a Set of all the keys present in the map.
        //The Set contains only the keys and does not include the values.
        // Each key in the set will be unique, as a Set automatically removes duplicates.
        Set<String> keys = extractMap.keySet();
        System.out.println(keys);
        for (String key : keys) {
            Object value_in_excel= extractMap.get(key);
            System.out.println(value_in_excel);
            //String Conversion (+ ""): By appending an empty string ("") to the value_in_excel object, you're explicitly converting it to a String type.
            // This is necessary because jsonPath_Response.get() requires a string argument to execute the query properly
            Object result_from_response=jsonPath_Response.get(value_in_excel+"");
            System.out.println(result_from_response);
            Environment.env.put(key,result_from_response);
            System.out.println("Environment HashMap: " + Environment.env);




        }

    }

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
