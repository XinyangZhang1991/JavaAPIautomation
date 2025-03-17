package API_Automation_FrameWork.testcases;


import API_Automation_FrameWork.common.BaseTest;
import API_Automation_FrameWork.pojo.CaseData;
import API_Automation_FrameWork.util.ExcelUtil;
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
