package doctor.kmwlyy.com.recipe.Model;

/**
 * Created by Administrator on 2016/8/11.
 */
public class DiagnosisBean {
    public DiagDetailBean Detail;
    public String Description;

    public DiagnosisBean() {
        Detail = new DiagDetailBean();
        Description = "";
    }
}
