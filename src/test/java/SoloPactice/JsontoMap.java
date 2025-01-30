package SoloPactice;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;

public class JsontoMap {

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

    public static void main(String[] args) {
        String data ="CaseData(id=1, title=send SMS, priority=null, method=put, url=http://shop.lemonban.com:8107/user/sendRegisterSms, headers=Content-Type: application/json; charset=UTF-8, params={\"mobile\":\"{{phone}}\"}, expected={\"statuscode\":200}, extractedResponse=null, postsql={\"code\":\"SELECT mobile_code FROM tz_sms_log WHERE user_phone = '{{phone}}' ORDER BY rec_date DESC LIMIT 1;\"}, db_assertion=null)";
    }
}
