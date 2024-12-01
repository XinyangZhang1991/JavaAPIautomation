package Day04;


import com.alibaba.excel.EasyExcel;

import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;


import static io.restassured.RestAssured.given;

public class LoginTest {
    ////数据提供者 - 会由代码读取Excel中的数据返回
    @DataProvider
    public Object[] getExcelData() {
        List<CaseData> Data = EasyExcel.read("src/test/resources/Lemondata.xlsx")
                .head(CaseData.class)
                .sheet("Login").doReadSync();
        return Data.toArray();
        //System.out.println(Data);
    }

    @Test(dataProvider = "getExcelData")
    //每一条caseData都是一条整个的测试用例数据
    public void test_login(CaseData caseData) {
        System.out.println(caseData);
        request(caseData);
    }
    //需求：封装设计统一的接口请求方法，用来兼容所有的接口请求，不管你是注册、登录、搜索商品、添加购物车...
    public Response request(CaseData caseData) {
        //four main request sections
        String url = caseData.getUrl();
        String method = caseData.getMethod();
        String headers = caseData.getHeaders();
        String params = caseData.getParams();

        //RequestSpecification --
        //Step 1 : setting the request header
        RequestSpecification req = given();
        if (headers != null) {
            req.headers(jsonToMap(headers));
        }

        //Step 2: setting the request method
        Response res = null;
        if (method.equalsIgnoreCase("get")) {
            //执行get请求
            res = req.get(url + params).then().log().all().extract().response();
        } else if (method.equalsIgnoreCase("post")) {
            //执行post请求
            //TODO 考虑到文件上传的请求
            res = req.body(params).post(url).then().log().all().extract().response();
        } else if (method.equalsIgnoreCase("put")) {
            //执行put请求
            res = req.body(params).put(url).then().log().all().extract().response();
        } else if (method.equalsIgnoreCase("delete")) {
            //执行delete请求
            res = req.delete(url + params).then().log().all().extract().response();
        }
        return res;
    }

    public HashMap<String, Object> jsonToMap(String str) {

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





//        Response response1 =
//                given().
//                        header("Content-Type","application/json; charset=UTF-8").
//                        body(caseData.getParams()).
//                when().
//                        post("http://shop.lemonban.com:8107/login").
//                then().
//                        log().body().extract().response();