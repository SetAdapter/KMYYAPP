package doctor.kmwlyy.com.recipe.Utils;

import android.content.Context;

import doctor.kmwlyy.com.recipe.R;

/**
 * Created by Administrator on 2016/8/12.
 */
public class MyUtils {
    /**
     * 根据类型返回性别
     */
    public static String getGendar(Context mContext,String gendar){
        String str = "";
        switch(gendar){
            case "0":
                str = mContext.getResources().getString(R.string.string_male);
                break;
            case "1":
                str = mContext.getResources().getString(R.string.string_female);
                break;
            case "2":
                str = mContext.getResources().getString(R.string.string_unknow);
                break;
        }
        return str;
    }
    /**
     * 根据类型返回处方类型
     */
    public static String getRecipeType(Context mContext,int type){
        String str = "";
        switch(type){
            case 1:
                str = mContext.getResources().getString(R.string.string_recipe_cn);
                break;
            case 2:
                str = mContext.getResources().getString(R.string.string_recipe_en);
                break;
        }
        return str;
    }
}
