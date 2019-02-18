package com.kmwlyy.patient.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.util.CommonUtils;
import com.kmwlyy.core.util.DebugUtils;
import com.kmwlyy.core.util.ImageUtils;
import com.kmwlyy.patient.R;
import com.kmwlyy.patient.helper.base.BaseActivity;
import com.kmwlyy.patient.helper.net.NetService;
import com.kmwlyy.patient.helper.net.bean.HealthInformationBean;
import com.kmwlyy.patient.helper.net.event.HealthInformationEvent;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.winson.ui.widget.CommonAdapter;
import com.winson.ui.widget.ViewHolder;
import com.winson.ui.widget.listview.PageListView;
import com.winson.ui.widget.listview.PageListViewHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @Description描述: 健康资讯列表页面
 * @Author作者: zhaoqile
 * @Date日期: 2016/12/12
 */
public class HealthInfoListActivity extends BaseActivity implements PageListView.OnPageLoadListener{
    private static final String TAG = HealthInfoListActivity.class.getSimpleName();

    @BindView(R.id.lv_healthInfo)
    PageListView lv_healthInfo;
    @BindView(R.id.tv_center)
    TextView tv_center;

    private HttpClient getHealthInfoClient;
    private PageListViewHelper<HealthInformationBean> listViewHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_healthinfo_list);
        butterknife.ButterKnife.bind(this);

        initToolBar();
        initListView();
    }

    private void initToolBar(){
        tv_center.setText(R.string.health_information);
    }

    private void initListView(){
        listViewHelper =  new PageListViewHelper<>(lv_healthInfo, new HealthInfoAdapter(HealthInfoListActivity.this, null));
        int diagnoseListPadding = getResources().getDimensionPixelSize(R.dimen.list_padding);
//        listViewHelper.getListView().setDividerHeight(0);
        listViewHelper.getListView().setPadding(0, 0, 0, diagnoseListPadding);
        listViewHelper.getListView().setClipToPadding(false);
        listViewHelper.setOnPageLoadListener(this);
        listViewHelper.getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String url = listViewHelper.getAdapter().getItem(position).url;
                Intent intent = new Intent(HealthInfoListActivity.this,HealthInfoDetailActivity.class);
                intent.putExtra(HealthInfoDetailActivity.KEY_HEALTH_INFO_URL,url);
                HealthInfoListActivity.this.startActivity(intent);
            }
        });

        getHealthInformation(true);
    }

    @Override
    public void onRefreshData() {
        getHealthInformation(true);
    }

    @Override
    public void onLoadPageData() {
        getHealthInformation(false);
    }



    private void getHealthInformation(final boolean refresh){

        HealthInformationEvent.GetList getHealthInformation = new HealthInformationEvent.GetList(
                refresh ? 1 : listViewHelper.getPageIndex(),
                listViewHelper.getPageSize(),
                new HttpListener<ArrayList<HealthInformationBean>>() {
                    @Override
                    public void onError(int code, String msg) {
                        if (DebugUtils.debug) {
                            Log.d(TAG, DebugUtils.errorFormat("getHealthInformationList", code, msg));
                        }
                        listViewHelper.setRefreshing(false);
                    }

                    @Override
                    public void onSuccess(ArrayList<HealthInformationBean> healthInformation) {
                        if (DebugUtils.debug) {
                            Log.d(TAG, DebugUtils.successFormat("getHealthInformationList", DebugUtils.toJson(healthInformation)));
                        }
                        listViewHelper.setRefreshing(false);
                        if (refresh) {
                            listViewHelper.refreshData(healthInformation);
                        } else {
                            listViewHelper.addPageData(healthInformation);
                        }
                    }
                }
        );

        getHealthInfoClient = NetService.createClient(this, getHealthInformation);
        getHealthInfoClient.start();
    }




    class HealthInfoAdapter extends CommonAdapter<HealthInformationBean> {

        public HealthInfoAdapter(Context context, List<HealthInformationBean> datas) {
            super(context, R.layout.item_health_info, datas);
        }


        @Override
        public void convert(ViewHolder viewHolder, HealthInformationBean data, int position) {

             TextView tv_article_title = (TextView) viewHolder.findViewById(R.id.tv_article_title);
             TextView tv_article_date = (TextView) viewHolder.findViewById(R.id.tv_article_date);
             TextView tv_read_quantity = (TextView) viewHolder.findViewById(R.id.tv_read_quantity);
            ImageView iv_article = (ImageView) viewHolder.findViewById(R.id.iv_article);

            tv_article_title.setText(data.title);
            if (data.formatDate == null)
            {
                data.formatDate = CommonUtils.convertTime(HealthInfoListActivity.this,data.lastModifiedTime);
            }
            tv_article_date.setText(data.formatDate);
            tv_read_quantity.setText(data.readingQuantity+"阅读");

            if (TextUtils.isEmpty(data.imageUrl))
            {
                iv_article.setImageDrawable(getResources().getDrawable(R.drawable.buy_image_word));
            }
            else{
                ImageLoader.getInstance().displayImage(data.imageUrl,iv_article, ImageUtils.getCacheOptions());
            }

        }
    }

}
