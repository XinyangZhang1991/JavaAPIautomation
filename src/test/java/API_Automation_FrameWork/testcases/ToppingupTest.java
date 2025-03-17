package API_Automation_FrameWork.testcases;


import API_Automation_FrameWork.common.BaseTest;
import API_Automation_FrameWork.common.Environment;
import API_Automation_FrameWork.pojo.CaseData;
import API_Automation_FrameWork.util.ExcelUtil;
import com.lemon.encryption.RSAManager;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class ToppingupTest extends BaseTest {
    //数据提供者 - 会由代码读取Excel中的数据返回
    @DataProvider
    public Object[] getDatas() {
        return ExcelUtil.readExcel("RSAencryption","src/test/resources/Lemondata.xlsx").toArray();
    }

    @BeforeMethod
    public void setup() throws Exception {
        //第一次登录接口请求前环境变量中是没有token及memberId的
        if(Environment.env.get("token") != null && Environment.env.get("memberId") != null){
            //充值接口请求前执行
            //生成timestamp及sign参数
            long timestamp = System.currentTimeMillis()/1000;
            String token = (String)Environment.env.get("token");
            String sign = token.substring(0,50) + timestamp;
            sign = RSAManager.encryptWithBase64(sign);
            //存储到环境变量中
            Environment.env.put("sign",sign);
            Environment.env.put("timestamp",timestamp);
        }

    }

    @Test(dataProvider = "getDatas")
    public void test_toppoing_up(CaseData caseData) {
        request(caseData);
    }

}
