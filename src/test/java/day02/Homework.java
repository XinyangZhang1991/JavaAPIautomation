package day02;


import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class Homework {

    public static void main(String[] args) {
        //搜索商品
        given().
        when().
                get("http://shop.lemonban.com:8107/search/searchProdPage?"+
                        "prodName=芒果&categoryId=&sort=0&orderBy=0&current=1&isAllProdType=true&st=0&size=12").
        then().
                log().all();

        //商品详情页面
        given().

        when().
                get("http://shop.lemonban.com:8107/prod/prodInfo?prodId=4345").
        then().
                log().all();
    }
}
