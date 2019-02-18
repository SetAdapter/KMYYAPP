package doctor.kmwlyy.com.recipe;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.util.DebugUtils;
import com.kmwlyy.core.util.ToastUtils;
import com.lidroid.xutils.ViewUtils;
import com.winson.ui.widget.listview.PageListView;
import com.winson.ui.widget.listview.PageListViewHelper;

import java.util.ArrayList;
import java.util.List;

import doctor.kmwlyy.com.recipe.Adapter.DrugListAdapter;
import doctor.kmwlyy.com.recipe.Event.Http_deleteRecipeList_Event;
import doctor.kmwlyy.com.recipe.Event.Http_editRecipeList_Event;
import doctor.kmwlyy.com.recipe.Event.Http_getRecipeList_Event;
import doctor.kmwlyy.com.recipe.Event.NetService;
import doctor.kmwlyy.com.recipe.Model.Constant;
import doctor.kmwlyy.com.recipe.Model.DrugListBean;
import doctor.kmwlyy.com.recipe.Model.RecipeLModifyBean;

/**
 * 处方集管理界面
 */
public class DrugListSelActivity extends BaseActivity implements PageListView.OnPageLoadListener, View.OnClickListener {
    public static final String TAG = "DrugListActivity";
    public Context mContext;
    TextView tv_tools_left;
    TextView iv_tools_right;

    private List<DrugListBean> list_druglist;
    private DrugListAdapter drugListAdapter;
    public static final String[] type = new String[]{Constant.CN_RECIPE, Constant.EN_RECIPE};

    private PageListView mRecipeListView;
    private PageListViewHelper<DrugListBean> mPageListViewHelper;

    @Override
    public void onRefreshData() {loadData(true);}

    @Override
    public void onLoadPageData() {loadData(false);}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drug_list_sel);
        ViewUtils.inject(this); //注入view和事件
        init();
    }

    private void init() {
        tv_tools_left = (TextView) findViewById(R.id.tv_left);
        iv_tools_right = (TextView) findViewById(R.id.tv_right);

        mContext = DrugListSelActivity.this;
        ((TextView)findViewById(R.id.tv_center)).setText(getResources().getString(R.string.string_druglist_title));
        tv_tools_left.setVisibility(View.VISIBLE);
        tv_tools_left.setOnClickListener(this);
        iv_tools_right.setOnClickListener(this);
        iv_tools_right.setVisibility(View.VISIBLE);
        iv_tools_right.setText(getResources().getString(R.string.string_druglist_man));

        //处方集列表
        list_druglist = new ArrayList<>();
        drugListAdapter = new DrugListAdapter(mContext,this,list_druglist,getIntent().getStringExtra("type"),getIntent().getStringExtra("OPDRegisterID"));

        mRecipeListView = (PageListView)findViewById(R.id.lv_druglist);
        mPageListViewHelper = new PageListViewHelper<>(mRecipeListView, drugListAdapter);
        mPageListViewHelper.getListView().setDivider(null);
        mPageListViewHelper.setOnPageLoadListener(this);


    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData(true);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.tv_left){//返回
            finish();
        }else if(v.getId() == R.id.tv_right){//跳转到管理处方界面
            manageRecipeList();
        }
    }

    /**
     * 请求列表数据
     */
    private void loadData(final boolean refresh) {
        mPageListViewHelper.setRefreshing(refresh);
        Http_getRecipeList_Event event = new Http_getRecipeList_Event(refresh ? "1" : mPageListViewHelper.getPageIndex()+"",new HttpListener<List<DrugListBean>>(
        ) {
            @Override
            public void onError(int code, String msg) {
                if (DebugUtils.debug) {
                    Log.i(TAG, "request error , code : " + code + " , msg : " + msg);
                }
                mPageListViewHelper.setRefreshing(false);
            }

            @Override
            public void onSuccess(List<DrugListBean> list) {
                if (DebugUtils.debug) {
                    Log.i(TAG, "request success");
                }
                mPageListViewHelper.setRefreshing(false);
                if (refresh) {
                    mPageListViewHelper.refreshData(list);
                } else {
                    mPageListViewHelper.addPageData(list);
                }
            }
        });
        HttpClient httpClient = NetService.createClient(mContext, event);
        httpClient.start();
    }

    /**
     * 跳转到处方管理界面
     */
    public void manageRecipeList() {
        startActivity(new Intent(mContext,DrugListActivity.class));
    }

    /**
     * 打开处方集详情界面
     * @param type 中药、西药处方
     * @param action add/modify
     */
    public void startActivity(String type, String action, RecipeLModifyBean bean){
        Intent intent = new Intent();
        intent.setClass(mContext, RecipeListActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("action", action);
        if(null != bean){
            Bundle bundle = new Bundle();
            bundle.putSerializable("bean", bean);
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }
}
