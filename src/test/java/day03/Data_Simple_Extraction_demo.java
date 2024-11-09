package day03;


import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class Data_Simple_Extraction_demo {
    public static void main(String[] args) {
        Response response=
        given().when().get("http://shop.lemonban.com:8107/search/searchProdPage?"+
                        "prodName=芒果&categoryId=&sort=0&orderBy=0&current=1&isAllProdType=true&st=0&size=12").
                then().log().all().extract().response();
        Object result = response.jsonPath().get("records[0].price");
        System.out.println(result);
    }
}
