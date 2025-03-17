package API_Automation_FrameWork.testcases;


import API_Automation_FrameWork.common.BaseTest;
import API_Automation_FrameWork.common.Environment;
import API_Automation_FrameWork.pojo.CaseData;
import API_Automation_FrameWork.util.ExcelUtil;
import API_Automation_FrameWork.util.RandomDataUtil;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.request;

public class RegisterTest extends BaseTest {

    @DataProvider
    public Object[] getExcelData() {

        return ExcelUtil.readExcel("Register","src/test/resources/Lemondata.xlsx").toArray();
    }

    @BeforeClass
    public void setup(){
            String phone =RandomDataUtil.getUnregisterPhone();
            String username = RandomDataUtil.getUnregisterUsername();
            Environment.env.put("phone",phone);
            Environment.env.put("username",username);
        }

    @Test(dataProvider = "getExcelData")
    //每一条caseData都是一条整个的测试用例数据
    public void test_register(CaseData caseData) {
        System.out.println(caseData);
        request(caseData);
    }
}
