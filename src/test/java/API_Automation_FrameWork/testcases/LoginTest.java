package API_Automation_FrameWork.testcases;
import API_Automation_FrameWork.common.BaseTest;
import API_Automation_FrameWork.util.ExcelUtil;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import API_Automation_FrameWork.pojo.CaseData;

public class LoginTest extends BaseTest {

    @DataProvider
    public Object[] getExcelData() {

        return ExcelUtil.readExcel("Login","src/test/resources/Lemondata.xlsx").toArray();
    }

    @Test(dataProvider = "getExcelData")
    //每一条caseData都是一条整个的测试用例数据
    public void test_login(CaseData caseData) {
        System.out.println(caseData);
        request(caseData);
    }
}
