package Day05.pojo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CaseData {
    @ExcelProperty ("id")  //excel 表格里面表头的名称
    private int id;
    @ExcelProperty ("title")
    private String title;
    @ExcelProperty ("priority")
    private String priority;
    @ExcelProperty ("method")
    private String method;
    @ExcelProperty ("url")
    private String url;
    @ExcelProperty ("header")
    private String headers;
    @ExcelProperty ("params")
    private String params;
    @ExcelProperty ("expected result")
    private String expected;
    @ExcelProperty ("data to extract")
    private String extractedResponse;
    @ExcelProperty ("postsql")
    private String postsql;
    @ExcelProperty ("db_assertion")
    private String db_assertion;
}
