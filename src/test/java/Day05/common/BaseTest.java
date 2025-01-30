package Day05.common;


import Day05.pojo.CaseData;
import Day05.util.JDBCUtil;
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
        String postsql = caseData.getPostsql();
        String db_assertion = caseData.getDb_assertion();
        //请求头中{{XXX}}替换
        headers = replaceParam(headers);
        //请求参数中{{XXX}}替换
        params = replaceParam(params);
        //请求地址中{{XXX}}替换
        url = replaceParam(url);
        postsql = replaceParam(postsql);
        db_assertion=replaceParam(db_assertion);
        //
        logger.info("=================请求信息Request_information=================");
        logger.info("请求方法Request_Method:" + method);
        logger.info("请求地址Request_URL:" + url);
        logger.info("请求头:Request_Headers" + headers);
        logger.info("请求参数:Request_Parameters" + params);
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
        logger.info("=================响应信息Response Info =================");
        logger.info("响应状态码Status Code:" + res.getStatusCode());
        logger.info("响应时间Response Time:" + res.getTime() + "ms");
        logger.info("响应头Response Header:" + res.getHeaders().asList());
        logger.info("响应体Response Body:" + res.getBody().asString());
        //Step 3 做断言
        assertResponse(res,caseData.getExpected());
        //响应提取
        extractResponse(res,caseData.getExtractedResponse());
        extractSQL (postsql);
        assertSQL(db_assertion);
        return res;
    }


    public void assertResponse(Response res,String expected){
        //先判空，如果是excel表中的expected是空的， 那么整个断言方法就不需要再执行了
        if(expected != null) {
            //expected --> json格式的字符串  --> 转换为Map
            HashMap<String, Object> expectedMap = jsonToMap(expected);
            //遍历map
            Set<String> keys = expectedMap.keySet(); //先拿到Excel表里面所有的Key值
            logger.info("=================断言信息 Response Assertion Info=================");
            for (String key : keys) {
                //1、响应状态码-key为statuscode 2、响应体字符串断言-key为bodystr 3、JSON响应体字段断言
                if (key.equals("statuscode")) {
                    //拿到实际的响应状态码
                    int actualValue = res.getStatusCode();
                    Object expectedValue = expectedMap.get(key);
                    logger.info("http status code assertion:  实际值actual value: 【" + actualValue + "】expected value : 期望值【" + expectedValue + "】");
                    Assert.assertEquals(actualValue, expectedValue);
                } else if (key.equals("bodystring")) {
                    //获取实际的接口响应体字符串文本数据
                    String actualValue = res.body().asString();
                    Object expectedValue = expectedMap.get(key);
                    logger.info("http response body string assertion: 实际值【" + actualValue + "】期望值【" + expectedValue + "】");
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

    /**
     * 数据库断言封装
     * @param assertSql
     */
    public void assertSQL(String assertSql){
        if(assertSql != null){
            HashMap<String,Object> assertSqlMap = jsonToMap(assertSql);
            Set<String> keys = assertSqlMap.keySet();
            logger.info("=================数据库断言DB assertion=================");
            for(String key : keys){
                //key为要执行的SQL语句
                //value期望值
                Object value = assertSqlMap.get(key);
                Object result = JDBCUtil.querySingleData(key);
                logger.info("执行的SQL语句SQLQuery is :"+key);
                logger.info("期望值 expected value :"+value+"，实际值 actual value:"+result);
                //类型不一致的解决思路：让两者类型统一：中间角色-String
                Assert.assertEquals(result+"",value+"");
            }
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
                //
                Object result =null;
                if (value.equals("bodystr")){
                    result=res.body().asString();
                }
                else {
                    result = res.jsonPath().get(value + "");
                }
                //将变量名以及对应的字段值存储到环境变量区域中
                logger.info("=================提取响应Response Extraction=================");
                logger.info("变量名Variable_Key:"+key+"，变量值Variable_Value:"+result);
                Environment.env.put(key, result);
            }
        }
    }

    public void extractSQL(String sqlInfo){
        //{"code":"SELECT mobile_code FROM tz_sms_log WHERE user_phone = '13323234521'"}
        if(sqlInfo != null){
            HashMap<String,Object> sqlMap = jsonToMap(sqlInfo);
            Set<String> keys = sqlMap.keySet();
            logger.info("===========执行后置 PostSQL execution==========");
            for (String key: keys){
                String sql = (String) sqlMap.get(key);
                //执行sql - Java代码
                Object result = JDBCUtil.querySingleData(sql);
                //保存到环境变量中
                Environment.env.put(key,result);
                logger.info("SQLQuery is :"+sql);
                logger.info("Value received from SQL:"+result);
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
                logger.info("===========Replacement Info==========");
                logger.info(subStr+ "is being replaced to :"+value);
                logger.info("Data after replacement is  :"+str);

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
