package API_Automation_FrameWork.testcases;


import API_Automation_FrameWork.common.BaseTest;
import API_Automation_FrameWork.pojo.CaseData;
import API_Automation_FrameWork.util.ExcelUtil;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class MakeAnOrder extends BaseTest {

    @DataProvider
    public Object[] getExcelData() {

        return ExcelUtil.readExcel("Search","src/test/resources/Lemondata.xlsx").toArray();
    }

    @Test(dataProvider = "getExcelData")
    //每一条caseData都是一条整个的测试用例数据
    public void test_search(CaseData caseData) {
        System.out.println(caseData);
        request(caseData);
    }
}
