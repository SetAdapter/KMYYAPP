package doctor.kmwlyy.com.recipe.Model;

import android.content.Context;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import doctor.kmwlyy.com.recipe.R;

/**
 * Created by Administrator on 2016/8/20.
 */
public class Constant {
    public final static String CurrentPage = "1";
    public final static String PageSize = "10";
    public final static String ICDType_CN = "1";
    public final static String ICDType_EN = "2";
    public final static String DrugType_CN = "1";
    public final static String DrugType_EN = "2";
    public final static String CN_RECIPE = "中药处方";
    public final static String EN_RECIPE = "西药处方";
    public final static String COMMON_RECIPE = "常用处方";
    public final static String RECIPE_ADD = "ADD";
    public final static String RECIPE_MODIFY = "MODIFY";
    public final static String RECIPE_SELECT = "SELECT";
    public static String[] getFreq(Context context){
        String[] Freqs = new String[]{context.getResources().getString(R.string.string_freq1),context.getResources().getString(R.string.string_freq2),context.getResources().getString(R.string.string_freq3),context.getResources().getString(R.string.string_freq4),
                context.getResources().getString(R.string.string_freq5),context.getResources().getString(R.string.string_freq6),context.getResources().getString(R.string.string_freq7),context.getResources().getString(R.string.string_freq8),context.getResources().getString(R.string.string_freq9),context.getResources().getString(R.string.string_freq10),context.getResources().getString(R.string.string_freq11),
                context.getResources().getString(R.string.string_freq12),context.getResources().getString(R.string.string_freq13),context.getResources().getString(R.string.string_freq14),context.getResources().getString(R.string.string_freq15),context.getResources().getString(R.string.string_freq16),
                context.getResources().getString(R.string.string_freq17),context.getResources().getString(R.string.string_freq18),context.getResources().getString(R.string.string_freq19),context.getResources().getString(R.string.string_freq20),context.getResources().getString(R.string.string_freq21),
                context.getResources().getString(R.string.string_freq22),context.getResources().getString(R.string.string_freq23),context.getResources().getString(R.string.string_freq24),context.getResources().getString(R.string.string_freq25),context.getResources().getString(R.string.string_freq26)};
        return Freqs;
    }
    public static String[] getRouteName(Context context){
        String[] RouteName = new String[]{context.getResources().getString(R.string.string_route1),context.getResources().getString(R.string.string_route2),context.getResources().getString(R.string.string_route3),context.getResources().getString(R.string.string_route4),context.getResources().getString(R.string.string_route5),
                context.getResources().getString(R.string.string_route6),context.getResources().getString(R.string.string_route7),context.getResources().getString(R.string.string_route8),context.getResources().getString(R.string.string_route9),context.getResources().getString(R.string.string_route10),context.getResources().getString(R.string.string_route11),context.getResources().getString(R.string.string_route12),context.getResources().getString(R.string.string_route13),context.getResources().getString(R.string.string_route14),
                context.getResources().getString(R.string.string_route15),context.getResources().getString(R.string.string_route16),context.getResources().getString(R.string.string_route17),context.getResources().getString(R.string.string_route18),context.getResources().getString(R.string.string_route19),context.getResources().getString(R.string.string_route20)};
        return RouteName;
    }
    public static String[] getEnUnit(Context context){
        String[] EnUnit = new String[]{
                context.getResources().getString(R.string.string_enunit1),
                context.getResources().getString(R.string.string_enunit2),
                context.getResources().getString(R.string.string_enunit3),
                context.getResources().getString(R.string.string_enunit4),
                context.getResources().getString(R.string.string_enunit5),
                context.getResources().getString(R.string.string_enunit6),
                context.getResources().getString(R.string.string_enunit7),
                context.getResources().getString(R.string.string_enunit8),
                context.getResources().getString(R.string.string_enunit9),
                context.getResources().getString(R.string.string_enunit10),
                context.getResources().getString(R.string.string_enunit11),
                context.getResources().getString(R.string.string_enunit12),
                context.getResources().getString(R.string.string_enunit13),
                context.getResources().getString(R.string.string_enunit14),
                context.getResources().getString(R.string.string_enunit15),
                context.getResources().getString(R.string.string_enunit16),
                context.getResources().getString(R.string.string_enunit17),
                context.getResources().getString(R.string.string_enunit18),
                context.getResources().getString(R.string.string_enunit19),
                context.getResources().getString(R.string.string_enunit20),
                context.getResources().getString(R.string.string_enunit21),
                context.getResources().getString(R.string.string_enunit22),
                context.getResources().getString(R.string.string_enunit23),
                context.getResources().getString(R.string.string_enunit24),
                context.getResources().getString(R.string.string_enunit25),
                context.getResources().getString(R.string.string_enunit26),
                context.getResources().getString(R.string.string_enunit27),
                context.getResources().getString(R.string.string_enunit28),
                context.getResources().getString(R.string.string_enunit29),
                context.getResources().getString(R.string.string_enunit30),
                context.getResources().getString(R.string.string_enunit31),
                context.getResources().getString(R.string.string_enunit32),
                context.getResources().getString(R.string.string_enunit33),
                context.getResources().getString(R.string.string_enunit34),
                context.getResources().getString(R.string.string_enunit35),
                context.getResources().getString(R.string.string_enunit36),
                context.getResources().getString(R.string.string_enunit37)
        };
        return EnUnit;
    }
    public static String[] getCnUnit(Context context){
        String[] CnUnit = new String[]{context.getResources().getString(R.string.string_cnunit1),
                context.getResources().getString(R.string.string_cnunit2),
                context.getResources().getString(R.string.string_cnunit3),
                context.getResources().getString(R.string.string_cnunit4),
                context.getResources().getString(R.string.string_cnunit5),
                context.getResources().getString(R.string.string_cnunit6),
                context.getResources().getString(R.string.string_cnunit7),
                context.getResources().getString(R.string.string_cnunit8),
                context.getResources().getString(R.string.string_cnunit9),
                context.getResources().getString(R.string.string_cnunit10),
                context.getResources().getString(R.string.string_cnunit11),
                context.getResources().getString(R.string.string_cnunit12),
                context.getResources().getString(R.string.string_cnunit13),
                context.getResources().getString(R.string.string_cnunit14),
                context.getResources().getString(R.string.string_cnunit15),
                context.getResources().getString(R.string.string_cnunit16),
                context.getResources().getString(R.string.string_cnunit17)
        };
        return CnUnit;
    }
    public static String[] getZhiFa(Context context){
        String[] CnUnit = new String[]{context.getResources().getString(R.string.string_zhifa1),
        context.getResources().getString(R.string.string_zhifa2)
        };
        return CnUnit;
    }
    public static String[] getUsage(Context context){
        String[] CnUnit = new String[]{
                context.getResources().getString(R.string.string_usage1),
                context.getResources().getString(R.string.string_usage2),
                context.getResources().getString(R.string.string_usage3)
        };
        return CnUnit;
    }

    public static List<DrugDetail> Druglist  = new ArrayList<>();

    public static DrugDetail getDrugBean(int position) {
        return Druglist.get(position);
    }

    public static void setDrugList(List<DrugDetail> newlist) {
        Druglist.clear();
        Druglist.addAll(newlist);
    }


    public static List<DiagDetailBean> DiagDetailList = new ArrayList<>();

    public static void setDiagDetailList(List<DiagDetailBean> list) {
        DiagDetailList.clear();
        DiagDetailList.addAll(list);
    }

    public static DiagDetailBean getDiagBean(int position) {
        return DiagDetailList.get(position);
    }

    public static int getRouteNamePostion(Context context,String str){
        String[] RouteName = getRouteName(context);
        for (int i=0;i<RouteName.length;i++){
            if(str.equals(RouteName[i])){
                return i;
            }
        }
        return getRouteName(context).length -1;
    }

    public static int getFreqPostion(Context context,String str){
        String[] Freqs = getFreq(context);
        for (int i=0;i<Freqs.length;i++){
            if(str.equals(Freqs[i])){
                return i;
            }
        }
        return getFreq(context).length -1;
    }

    public static int getCnUnitPostion(Context context,String str){
        String[] unit = getCnUnit(context);
        for (int i=0;i<unit.length;i++){
            if(str.equals(unit[i])){
                return i;
            }
        }
        return getCnUnit(context).length -1;
    }

    public static int getEnunitPostion(Context context,String str){
        String[] unit = getEnUnit(context);
        for (int i=0;i<unit.length;i++){
            if(str.equals(unit[i])){
                return i;
            }
        }
        return getEnUnit(context).length -1;
    }

    public static int getUsagePostion(Context context,String str){
        String[] usages = getUsage(context);
        for (int i=0;i<usages.length;i++){
            if(str.equals(usages[i])){
                return i;
            }
        }
        return 0;
    }

    public static int getZhifaPostion(Context context,String str){
        String[] usages = getUsage(context);
        for (int i=0;i<usages.length;i++){
            if(str.equals(usages[i])){
                return i;
            }
        }
        return 0;
    }

    /**
     * 计算价格
     * num = 剂量
     * unitPrice = 单位价格
     * unit = 剂数
     */
    public static String getFee(String num,String unitPrice){
        if(num.length() == 0 || unitPrice.length() == 0){
            return "";
        }
        return Double.parseDouble(num) * Double.parseDouble(unitPrice) + "";
    }

    /**
     * 数量 * 价格 = 列表里一项药品的子价格
     * @param quantity
     * @param price
     * @return
     */
    public static String getSubTotal(Context context,String quantity,String price){
        String str = context.getResources().getString(R.string.string_price);
        if(quantity.length() == 0 || price.length() == 0){
            return str;
        }
        DecimalFormat df = new DecimalFormat("######0.00");
        return str + df.format(Double.parseDouble(quantity) * Double.parseDouble(price)) + context.getResources().getString(R.string.string_yuan);
    }
}
