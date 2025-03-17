package testcases;


import common.BaseTest;
import common.Environment;
import pojo.CaseData;
import util.ExcelUtil;
import util.RandomDataUtil;
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
