package SoloPactice;


import Day05.pojo.CaseData;
import Day05.util.ExcelUtil;
import com.alibaba.excel.EasyExcel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.org.apache.xerces.internal.dom.PSVIAttrNSImpl;

import java.util.HashMap;
import java.util.List;

public class ExcelReading {


    public static List<CaseData> readExcel(String sheetName, String path) {
            List<CaseData> Data = EasyExcel.read(path)
                    .head(CaseData.class)
                    .sheet(sheetName).doReadSync();
            return Data;
    }


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
        List<CaseData> caseDatalist = ExcelUtil.readExcel("Register","src/test/resources/Lemondata.xlsx");
        for (CaseData caseData : caseDatalist) {
            System.out.println(caseData);
            String headers = caseData.getHeaders();
            System.out.println(headers);
        }


    }
}
