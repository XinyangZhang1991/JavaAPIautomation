package com.lemon.day02;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;

public class First_API_Request {
    public static void main(String[] args) {
        //发起接口请求四大要素：请求方法、接口地址、请求头（可选）、请求数据（可选）
        //given() 是用来设置请求预设：请求头、请求数据
        //when() 代表要去执行的操作：get/post/put/delete,填写接口请求地址
        //then() 代表的是在请求结束之后要做的操作，eg：提取响应数据,打印响应结果
        //链式调用

//        given()
//                .header("Content-Type", "application/json; charset=UTF-8")
//                .body("{\"principal\":\"lemon70\",\"credentials\":\"12345678\",\"appType\":3,\"loginType\":0}")
//                .when()
//                .post("http://shop.lemonban.com:8107/login")
//                .then()
//                .log().all();  // Log the complete response.

        given().
                header("Content-Type", "application/json; charset=UTF-8").
                body("{\"principal\":\"lemon70\",\"credentials\":\"12345678\",\"appType\":3,\"loginType\":0}").

        when().
                post("http://shop.lemonban.com:8107/login").
        then().
                log().all();


//        given().
//                header("Content-Type","application/json; charset=UTF-8").
//                body("{\"principal\":\"lemon_auto\",\"credentials\":\"lemon123456\",\"appType\":3,\"loginType\":0}").
//        when().
//                post("http://mall.lemonban.com:8107/login").
//        then().
//                log().all();

    }
}


