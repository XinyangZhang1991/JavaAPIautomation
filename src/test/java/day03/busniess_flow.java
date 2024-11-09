package day03;


import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class busniess_flow {
    public static void main(String[] args) {
        //1、登录
        //登录接口
        Response response1 =
                given().
                        header("Content-Type","application/json; charset=UTF-8").
                        body("{\"principal\":\"lemon70\",\"credentials\":\"12345678\",\"appType\":3,\"loginType\":0}").
                        when().
                        post("http://shop.lemonban.com:8107/login").
                        then().
                        log().body().extract().response();
        String token = "bearer"+response1.jsonPath().get("access_token");

        //2、搜索商品
        Response response2 =
                given().
                        when().
                        get("http://shop.lemonban.com:8107/search/searchProdPage?prodName=阿玛尼粉底液").
                        then().
                        log().body().extract().response();
        int prodId = response2.jsonPath().get("records[0].prodId");

        //3、进入到商品的详情页
        Response response3 =
                given().
                        when().
                        get("http://shop.lemonban.com:8107/prod/prodInfo?prodId="+prodId).
                        then().
                        log().body().extract().response();
        int skuId = response3.jsonPath().get("skuList[0].skuId");

        //添加购物车
        Response response4 =
                given().
                        header("Authorization",token).
                        header("Content-Type","application/json; charset=UTF-8").
                        body("{\"basketId\":0,\"count\":1,\"prodId\":\""+prodId+"\",\"shopId\":1,\"skuId\":"+skuId+"}").
                        when().
                        post("http://shop.lemonban.com:8107/p/shopCart/changeItem").
                        then().
                        log().body().extract().response();
        //如果响应体数据是文本类型（字符串）,不需要用到GPath表达式
        String result = response4.body().asString();

        System.out.println("整个业务流程最终的结果是："+result);
    }
}
