package Day05.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CaseData {
    private int id;
    private String title;
    private String priority;
    private String method;
    private String url;
    private String headers;
    private String params;
    private String expected;
    private String extractedResponse;
    private String sql;
}
