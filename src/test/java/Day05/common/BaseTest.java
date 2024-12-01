package Day05.common;


import Day05.pojo.CaseData;
import Day05.util.Log4jTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.log4j.Logger;
import org.testng.Assert;

import java.io.File;
import java.util.HashMap;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.restassured.RestAssured.given;

public class BaseTest {
    //得到日志对象
    Logger logger = Logger.getLogger(BaseTest.class);

    //需求：封装设计统一的接口请求方法，用来兼容所有的接口请求，不管你是注册、登录、搜索商品、添加购物车...
    public Response request(CaseData caseData) {
        //four main request sections
        String url = caseData.getUrl();
        String method = caseData.getMethod();
        String headers = caseData.getHeaders();
        String params = caseData.getParams();
        //请求头中{{XXX}}替换
        headers = replaceParam(headers);
        //请求参数中{{XXX}}替换
        params = replaceParam(params);
        //
        logger.info("=================请求信息=================");
        logger.info("请求方法:" + method);
        logger.info("请求地址:" + url);
        logger.info("请求头:" + headers);
        logger.info("请求参数:" + params);
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
            if(headers.contains("multipart/form-data")){
                res = req.multiPart(new File(params)).post(url).then().extract().response();
            }else {
                res = req.body(params).post(url).then().log().all().extract().response();
            }
        } else if (method.equalsIgnoreCase("put")) {
            //执行put请求
            res = req.body(params).put(url).then().log().all().extract().response();
        } else if (method.equalsIgnoreCase("delete")) {
            //执行delete请求
            res = req.delete(url + params).then().log().all().extract().response();
        }
        logger.info("=================响应信息=================");
        logger.info("响应状态码:" + res.getStatusCode());
        logger.info("响应时间:" + res.getTime() + "ms");
        logger.info("响应头:" + res.getHeaders().asList());
        logger.info("响应体:" + res.getBody().asString());
        //Step 3 做断言
        assertResponse(res,caseData.getExpected());
        //响应提取
        extractResponse(res,caseData.getExtractedResponse());
        return res;
    }

    public void assertResponse(Response res,String expected){
        //先判空，如果是excel表中的expected是空的， 那么整个断言方法就不需要再执行了
        if(expected != null) {
            //expected --> json格式的字符串  --> 转换为Map
            HashMap<String, Object> expectedMap = jsonToMap(expected);
            //遍历map
            Set<String> keys = expectedMap.keySet(); //先拿到Excel表里面所有的Key值
            logger.info("=================断言信息=================");
            for (String key : keys) {
                //1、响应状态码-key为statuscode 2、响应体字符串断言-key为bodystr 3、JSON响应体字段断言
                if (key.equals("statuscode")) {
                    //拿到实际的响应状态码
                    int actualValue = res.getStatusCode();
                    Object expectedValue = expectedMap.get(key);
                    logger.info("http响应状态码断言，实际值【" + actualValue + "】期望值【" + expectedValue + "】");
                    Assert.assertEquals(actualValue, expectedValue);
                } else if (key.equals("bodystring")) {
                    //获取实际的接口响应体字符串文本数据
                    String actualValue = res.body().asString();
                    Object expectedValue = expectedMap.get(key);
                    logger.info("响应体文本断言，实际值【" + actualValue + "】期望值【" + expectedValue + "】");
                    Assert.assertEquals(actualValue, expectedValue);
                } else {
                    //此时是响应体字段断言，对应的key是Gpath提取字段表达式
                    Object actualValue = res.jsonPath().get(key);
                    Object expectedValue = expectedMap.get(key);
                    Assert.assertEquals(actualValue, expectedValue);
                    logger.info("响应字段断言，提取响应字段【" + key + "】实际值【" + actualValue + "】期望值【" + expectedValue + "】");
                }
            }
        }else {
            logger.info("没有断言数据，请在Excel中补上这列数据");
        }
    }

    //提取的方法
    public void extractResponse(Response res, String extractInfo){
        if(extractInfo != null) {
            //extractInfo : {"token":"access_token"}
            HashMap<String, Object> extractMap = jsonToMap(extractInfo);
            Set<String> keys = extractMap.keySet();
            for (String key : keys) {
                //key:变量名  value:提取表达式
                Object value = extractMap.get(key);
                //拿到对应字段值, 字段值已经在eXCel表中设计好了， 是能够用JSONPATH取到响应体中所需字段位置的表达式
                Object result = res.jsonPath().get(value + "");
                //将变量名以及对应的字段值存储到环境变量区域中
                logger.info("=================提取响应=================");
                logger.info("变量名:"+key+"，变量值:"+result);
                Environment.env.put(key, result);
            }
        }
    }

    /**
     * 用来替换请求参数中{{XXX}}标记的部分
     */
    public String replaceParam(String str){
        if(str != null) {
            //1、需要通过正则表达式来识别标记{{XXX}}
            Pattern pattern = Pattern.compile("\\{\\{(.*?)\\}\\}");
            Matcher matcher = pattern.matcher(str);
            while (matcher.find()) {
                //2、找到环境变量区域中对应的变量名，将其值取出来
                //获取{{XXX}}
                String subStr = matcher.group();
                //获取XXX
                String variableName = matcher.group(1);
                Object value = Environment.env.get(variableName);
                //3、将字串subStr替换为上述的值
                //value + "" 通过拼接转化为字符串
                str = str.replace(subStr, value + "");
            }
            return str;
        }
        return null;
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
