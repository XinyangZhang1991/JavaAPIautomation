package API_Automation_FrameWork.util;

import static io.restassured.RestAssured.given;


public class EncryptTest {

    public static void main(String[] args) {
        given().
                header("Content-Type","application/json").
                header("X-Lemonban-Media-Type","lemonban.v3").
                body("{\"mobile_phone\": \"13323231111\",\"pwd\": \"12345678\"}").
                when().
                post("http://api.lemonban.com:8788/futureloan/member/login").
                then().
                log().body().extract().response();
/*//需要从第一个接口中拿到 memberid, token,
        int memberId = res_login.jsonPath().get("data.id");
        String token = res_login.jsonPath().get("data.token_info.token");
        String token_value = "Bearer "+res_login.jsonPath().get("data.token_info.token");*/
    }
}
