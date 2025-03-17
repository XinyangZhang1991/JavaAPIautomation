package util;


import pojo.CaseData;
import com.alibaba.excel.EasyExcel;

import java.util.List;

public class ExcelUtil {
    public static List<CaseData> readExcel(String sheetName, String path){
        List<CaseData> Data = EasyExcel.read(path)
                .head(CaseData.class)
                .sheet(sheetName).doReadSync();
        return Data;
    }
}
