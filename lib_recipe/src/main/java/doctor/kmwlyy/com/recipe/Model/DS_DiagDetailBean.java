package doctor.kmwlyy.com.recipe.Model;

/**
 * Created by Administrator on 2016/8/19.
 */
public class DS_DiagDetailBean {
    public String DiagnoseID;
    public String DiseaseCode;
    public String DiseaseName;
    public String DiagnoseType;
    public String Description;
    public String IsPrimary;

    public DS_DiagDetailBean() {
        DiagnoseID = "";
        DiseaseCode = "";
        DiseaseName = "";
        DiagnoseType = "";
        Description = "";
        IsPrimary = "";
    }
}

/**
 *         "DiagnoseID": "af91ce2e58844bcc811a6297944787d7",
 *         "DiseaseCode": "P61.201",
 *         "DiseaseName": "早产儿贫血",
 *         "DiagnoseType": 0,
 *         "Description": "c1",
 *         "IsPrimary": false
 *
 */
