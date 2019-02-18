package com.kmwlyy.doctor.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.util.CommonUtils;
import com.kmwlyy.core.util.DebugUtils;
import com.kmwlyy.doctor.NetService;
import com.kmwlyy.doctor.R;
import com.kmwlyy.doctor.adapter.DrugStoreRecipeAdapter;
import com.kmwlyy.doctor.model.DrugStoreRecipeBean;
import com.kmwlyy.doctor.model.httpEvent.Http_getDrugStorelRecipe_Event;
import com.winson.ui.widget.listview.PageListView;
import com.winson.ui.widget.listview.PageListViewHelper;

import java.util.ArrayList;
import java.util.List;

import doctor.kmwlyy.com.recipe.StoreRecipeActivity;

public class DrugStoreRecipeActivity extends BaseActivity implements PageListView.OnPageLoadListener{
    public static final String TAG = "DrugStoreRecipeActivity";
    private List<DrugStoreRecipeBean> list_recipe;
    private DrugStoreRecipeAdapter recipeAdapter;
    private Context mContext = DrugStoreRecipeActivity.this;

    private PageListView mRecipeListView;
    private PageListViewHelper<DrugStoreRecipeBean> mPageListViewHelper;
    @Override
    public void onRefreshData() {loadData(true);}

    @Override
    public void onLoadPageData() {loadData(false);}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drug_store_recipe);
        init();
    }

    public void init(){
        ((TextView)findViewById(R.id.tv_center)).setText(getResources().getString(R.string.string_drugstore));
        mLeftBtn = (TextView) findViewById(R.id.tv_left);
        mLeftBtn.setVisibility(View.VISIBLE);

        list_recipe = new ArrayList<>();
        recipeAdapter = new DrugStoreRecipeAdapter(mContext,list_recipe);
        //药店处方列表
        mRecipeListView = (PageListView)findViewById(R.id.lv_recipe);
        mPageListViewHelper = new PageListViewHelper<>(mRecipeListView, recipeAdapter);
        mPageListViewHelper.getListView().setDivider(null);
        mPageListViewHelper.setOnPageLoadListener(this);
        mPageListViewHelper.getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //已完成
                if(recipeAdapter.getList().get(position).DrugstoreRecipeStatus.equals("4")){
                    Intent intent;
                    if (Build.VERSION.SDK_INT >= 21) {
                        intent = new Intent(mContext, PDFActivity.class);
                    }else{
                        intent = new Intent(mContext, LegacyPDFActivity.class);
                    }
                    Bundle bundle = new Bundle();
                    bundle.putString("url",  recipeAdapter.getList().get(position).RecipeFileUrl);
                    bundle.putString("id", CommonUtils.toMD5(HttpClient.IMAGE_URL + "/DoctorRecipeFile/" + recipeAdapter.getList().get(position - 1).DrugstoreRecipeID));
                    intent.putExtras(bundle);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(mContext,StoreRecipeActivity.class);
                    intent.putExtra("detail",recipeAdapter.getList().get(position).DrugstoreRecipeID);
                    intent.putExtra("store",recipeAdapter.getList().get(position).Drugstore.DrugstoreName);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData(true);
    }

    /**
     * 请求列表数据
     */
    private void loadData(final boolean refresh) {
        mPageListViewHelper.setRefreshing(refresh);
        Http_getDrugStorelRecipe_Event event = new Http_getDrugStorelRecipe_Event(refresh ? "1" : mPageListViewHelper.getPageIndex()+"",new HttpListener<List<DrugStoreRecipeBean>>(
        ) {
            @Override
            public void onError(int code, String msg) {
                if (DebugUtils.debug) {
                    Log.i(TAG, "request error , code : " + code + " , msg : " + msg);
                }
                mPageListViewHelper.setRefreshing(false);
            }

            @Override
            public void onSuccess(List<DrugStoreRecipeBean> list) {
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

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_left://返回
                finish();
                break;
        }
    }
}
