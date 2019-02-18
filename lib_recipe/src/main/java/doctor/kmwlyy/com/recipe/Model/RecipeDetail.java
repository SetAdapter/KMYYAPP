package doctor.kmwlyy.com.recipe.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/22.
 */
public class RecipeDetail {
    public String RecipeName;
    public String Usage;
    public String TCMQuantity;  //剂数
    public String RecipeType;
    public List<DiagnosisBean> DiagnoseList;
    public List<DrugBean_Recipe> Details;

    public RecipeDetail(String recipeName, String recipeType) {
        RecipeName = recipeName;
        Usage = "";
        this.TCMQuantity = "1";
        RecipeType = recipeType;
        DiagnoseList = new ArrayList<>();
        Details = new ArrayList<>();
    }
}
