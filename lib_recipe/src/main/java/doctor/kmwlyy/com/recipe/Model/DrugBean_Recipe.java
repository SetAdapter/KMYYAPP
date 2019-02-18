package doctor.kmwlyy.com.recipe.Model;

/**
 * Created by Administrator on 2016/8/19.
 */
public class DrugBean_Recipe {
    public String Dose; //中药的 剂量
    public String Quantity;//西药的 数量
    public String DrugRouteName;
    public String Frequency;
    public DrugDetail_Recipe Drug;

    public DrugBean_Recipe() {
        Dose = "";
        Quantity = "";
        DrugRouteName = "";
        Frequency = "";
        Drug = new DrugDetail_Recipe();
    }
}
