package doctor.kmwlyy.com.recipe.Model;

/**
 * Created by Administrator on 2016/8/19.
 */
public class DrugBean {
    public String RecipeFormulaDetailID;
    public String Dose;
    public String Quantity;
    public String DrugRouteName;
    public String Frequency;
    public DrugDetail Drug;
    public String SubTotal; //小计

    public DrugBean() {
        RecipeFormulaDetailID = "";
        Dose = "";
        Quantity = "";
        DrugRouteName = "";
        Frequency = "";
        Drug = new DrugDetail();
        SubTotal = "";
    }

    /**
     * 转下类型
     * @return
     */
    public DrugBean_Recipe transform(){
        DrugBean_Recipe drug= new DrugBean_Recipe();
        drug.Dose = Dose;
        drug.Quantity = Quantity;
        drug.DrugRouteName = DrugRouteName;
        drug.Frequency = Frequency;
        drug.Drug.DrugID = Drug.DrugID;
        drug.Drug.DoseUnit = Drug.DoseUnit;

        return  drug;
    }
}
