package Day05.testcases;
import Day05.common.BaseTest;
import Day05.util.ExcelUtil;
import com.alibaba.excel.EasyExcel;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import Day05.pojo.CaseData;
import java.util.List;

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
