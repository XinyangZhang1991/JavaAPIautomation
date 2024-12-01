package com.lemon.day03;


import io.restassured.response.Response;

import java.io.File;
import java.util.Locale;

import static io.restassured.RestAssured.given;

public class Data_Extraction_upload_file_stream {
    public static void main(String[] args) {
// a pipeline to upload to the a picture
        //step 1: login first
        Response response_login=
        given().
                header("Content-Type", "application/json; charset=UTF-8").
                body("{\"principal\":\"lemon70\",\"credentials\":\"12345678\",\"appType\":3,\"loginType\":0}").

                when().
                post("http://shop.lemonban.com:8107/login").
                then().
                log().all().extract().response();
//step 2: 实现个人头像上传并且保存头像设置
        Response response_upload=
        given().
                header("Content-Type","multipart/form-data").
                header("Authorization","bearera20f6211-9feb-446a-bbdb-01c87b1ddb9a").
                multiPart(new File("src/test/resources/lemon_photo.png")).
        when().
                post("http://shop.lemonban.com:8107/p/file/upload").
        then().
                log().all().extract().response();

        String url= response_upload.jsonPath().get("resourcesUrl");
        String path = response_upload.jsonPath().get("filepath");

//Step 3:

    }
}
