package doctor.kmwlyy.com.recipe.Model;

/**
 * Created by Administrator on 2016/8/11.
 */
public class DS_DiagnosisBean {
    public DiagDetailBean Detail;
    public String Description;
    public String IsPrimary;

    public DS_DiagnosisBean() {
        Detail = new DiagDetailBean();
        Description = "";
        IsPrimary = "";
    }
}
