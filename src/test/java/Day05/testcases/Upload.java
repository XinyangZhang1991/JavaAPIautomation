package Day05.testcases;


import Day05.common.BaseTest;
import Day05.pojo.CaseData;
import Day05.util.ExcelUtil;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.request;

public class Upload extends BaseTest {
    @DataProvider
    public Object[] getExcelData() {

        return ExcelUtil.readExcel("upload","src/test/resources/Lemondata.xlsx").toArray();
    }

    @Test(dataProvider = "getExcelData")
    //每一条caseData都是一条整个的测试用例数据
    public void test_upload(CaseData caseData) {
        System.out.println(caseData);
        request(caseData);
    }
}
