package doctor.kmwlyy.com.recipe;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.kmwlyy.core.base.BaseApplication;
import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.util.DebugUtils;
import com.kmwlyy.core.util.ToastUtils;
import com.networkbench.agent.impl.NBSAppAgent;

import java.util.ArrayList;
import java.util.List;

import doctor.kmwlyy.com.recipe.Event.Http_getRecipe_Event;
import doctor.kmwlyy.com.recipe.Event.Http_saveRecipe_Event;
import doctor.kmwlyy.com.recipe.Event.Http_updateBJCA_Event;
import doctor.kmwlyy.com.recipe.Event.NetService;
import doctor.kmwlyy.com.recipe.Fragment.CNDrugFragment;
import doctor.kmwlyy.com.recipe.Fragment.ENDrugFragment;
import doctor.kmwlyy.com.recipe.Fragment.RecipeFragment;
import doctor.kmwlyy.com.recipe.Fragment.ZhuSuFragment;
import doctor.kmwlyy.com.recipe.Model.PatientDiagnoseBean;
import doctor.kmwlyy.com.recipe.Model.RecipeBean;
import doctor.kmwlyy.com.recipe.Model.RecipeDetail;

/**
 * 处方总界面
 */
public class RecipeActivity extends BaseActivity implements View.OnClickListener {
    public static final String TAG = "RecipeActivity";
    private List<Fragment> fragmentList;
    private FragmentManager manager;
    private Adapter adapter;

    //主诉，中药处方，西药处方 Fragment
    private ZhuSuFragment zhuSuFragment;
    private RecipeFragment recipeFragment;

    private RadioGroup radiogroup;
    private ViewPager viewpager;

    private Context mContext;

    private RecipeBean mRecipe;

    String OPDRegisterID = ""; //预约ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        mContext = RecipeActivity.this;
        //获取处方信息
        OPDRegisterID = getIntent().getStringExtra("id");
        //初使化
        init();
    }

    private void init() {
        ((TextView) findViewById(R.id.tv_center)).setText(getResources().getString(R.string.app_string_recipe));
        findViewById(R.id.tv_left).setOnClickListener(this);
        findViewById(R.id.tv_right).setOnClickListener(this);
        findViewById(R.id.tv_right).setVisibility(View.VISIBLE);
        ((TextView)findViewById(R.id.tv_right)).setText(getResources().getString(R.string.app_string_save));
        radiogroup = (RadioGroup) findViewById(R.id.radiogroup);
        viewpager = (ViewPager) findViewById(R.id.viewpager);

        fragmentList = new ArrayList<Fragment>();
        zhuSuFragment = new ZhuSuFragment();
        recipeFragment = new RecipeFragment();

        Bundle bundle = new Bundle();
        bundle.putString("id", OPDRegisterID);
        recipeFragment.setArguments(bundle);

        fragmentList.add(zhuSuFragment);
        fragmentList.add(recipeFragment);

        manager = getSupportFragmentManager();
        adapter = new Adapter(manager);
        viewpager.setAdapter(adapter);
        viewpager.setOffscreenPageLimit(2);

        viewpager.setOnPageChangeListener(new MyOnPageChangeListener());
        radiogroup.check(R.id.btn_sympton);
        radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                if (checkedId == R.id.btn_sympton) {
                    viewpager.setCurrentItem(0);
                }  else if (checkedId == R.id.btn_recipe) {
                    viewpager.setCurrentItem(1);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //隐藏键盘
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getRecipe(OPDRegisterID);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.tv_left){//返回
            finish();
        }
        else if(v.getId() == R.id.tv_right){//保存
            save();
        }
    }

    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageSelected(int arg0) {

            switch (arg0) {
                case 0:
                    radiogroup.check(R.id.btn_sympton);
                    break;
                case 1:
                    radiogroup.check(R.id.btn_recipe);
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }
    }

    private class Adapter extends FragmentPagerAdapter {

        public Adapter(android.support.v4.app.FragmentManager fm) {
            super(fm);
            manager = fm;
            // TODO Auto-generated constructor stub
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return fragmentList.size();
        }

        @Override
        public Fragment getItem(int position) {
            // TODO Auto-generated method stub

            return fragmentList.get(position);

        }
    }
    /**
     * 先获取处方的模版
     */
    public void getRecipe(String OPDRegisterID) {
        showLoadDialog(R.string.string_wait);
        Http_getRecipe_Event event = new Http_getRecipe_Event( OPDRegisterID , new HttpListener<RecipeBean>(

        ) {
            @Override
            public void onError(int code, String msg) {
                if (DebugUtils.debug) {
                    Log.i(TAG, "getRecipe error , code : " + code + " , msg : " + msg);
                }
                dismissLoadDialog();
                ToastUtils.showShort(mContext,msg);
                finish();
            }

            @Override
            public void onSuccess(RecipeBean recipe) {
                if (DebugUtils.debug) {
                    Log.i(TAG, "getRecipe success");
                }
                dismissLoadDialog();
                if(recipe != null && recipe.getOPDRegisterID().length() > 0){
                    mRecipe = recipe;
                    recipeFragment.setData(mRecipe.getRecipeList());
                    zhuSuFragment.setData(mRecipe.getMedicalRecord());
                }else{
                    ToastUtils.showShort(mContext,"request error");
                    finish();
                }

            }
        });
        HttpClient httpClient = NetService.createClient(mContext, event);
        httpClient.start();
    }

    /**
     * 发请求 - 保存处方
     */
    private void saveRecipe(PatientDiagnoseBean bean) {
        NBSAppAgent.onEvent("诊室-保存主诉，诊断建议等");
        showLoadDialog(R.string.string_wait_save);
        Http_saveRecipe_Event event = new Http_saveRecipe_Event( bean , new HttpListener<String>(

        ) {
            @Override
            public void onError(int code, String msg) {
                if (DebugUtils.debug) {
                    Log.i(TAG, "saveRecipe error , code : " + code + " , msg : " + msg);
                }
                dismissLoadDialog();
                ToastUtils.showShort(mContext,msg);
            }

            @Override
            public void onSuccess(String str) {
                if (DebugUtils.debug) {
                    Log.i(TAG, "saveRecipe success");
                }
                dismissLoadDialog();
                ToastUtils.showShort(mContext,getResources().getString(R.string.string_response_save));
            }
        });
        HttpClient httpClient = NetService.createClient(mContext, event);
        httpClient.start();
    }

    /**
     * 保存
     */
    public void save(){
        if(zhuSuFragment.checkParms()){
            sendRecipe();
        }else {
            ToastUtils.showShort(mContext,getResources().getString(R.string.string_recipe_remind1));
        }
    }

    /**
     * 收集界面参数 发请求
     */
    public void sendRecipe(){
        //组装数据：
        PatientDiagnoseBean patientDiagnoseBean = new PatientDiagnoseBean();

        patientDiagnoseBean.setOPDRegisterID(mRecipe.getOPDRegisterID());

        patientDiagnoseBean.setMedicalRecord(zhuSuFragment.getData());

        //发送
        saveRecipe(patientDiagnoseBean);
    }
}
