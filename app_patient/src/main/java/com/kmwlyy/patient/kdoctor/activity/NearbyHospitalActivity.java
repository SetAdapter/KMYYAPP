package com.kmwlyy.patient.kdoctor.activity;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.google.gson.Gson;
import com.kmwlyy.core.base.BaseActivity;
import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.util.ToastUtils;
import com.kmwlyy.patient.R;
import com.kmwlyy.patient.helper.utils.LibUtils;
import com.kmwlyy.patient.kdoctor.bean.NearbyHospitalBean;
import com.kmwlyy.patient.kdoctor.net.HosptialHttpEvent;
import com.kmwlyy.registry.page.ContainerActivity;
import com.kmwlyy.registry.page.SelectDepartmentFragment;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.winson.ui.widget.CommonAdapter;
import com.winson.ui.widget.ViewHolder;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @Description描述:
 * @Author作者: hx
 * @Date日期: 2017/7/20
 */

public class NearbyHospitalActivity extends BaseActivity {
    @BindView(R.id.toolbar_title)
    TextView tvTitleBarTitle;
    @BindView(R.id.listView)
    ListView listView;
    @BindView(R.id.ptrClassicFrameLayout)
    PtrClassicFrameLayout ptrClassicFrameLayout;
    private List<NearbyHospitalBean.ResultDataBean> hosptials;

    public static void jumpThiPage(Context mContext) {
        Intent intent = new Intent(mContext, NearbyHospitalActivity.class);
        mContext.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_list_refresh_load_more2;
    }

    @Override
    public void afterBindView() {
        tvTitleBarTitle.setText("附近医院");
        initView();
    }

    private void initView() {
        ptrClassicFrameLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                ptrClassicFrameLayout.setLoadMoreEnable(false);
                getListDatas();
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return false;
            }
        });

        ptrClassicFrameLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                getListDatas();
            }
        });
        ptrClassicFrameLayout.autoRefresh();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NearbyHospitalBean.ResultDataBean hospital = hosptials.get(position);
                Intent intent = new Intent(mContext, ContainerActivity.class);
                intent.putExtra(SelectDepartmentFragment.TAG_HOSPITAL, Integer.valueOf(hospital.getResultId()));
                Log.e("aaaa",hospital.getResultId());
                intent.putExtra(ContainerActivity.TAG_FRAGMENT, ContainerActivity.DEPARTMENT);
                startActivity(intent);
            }
        });
    }

    /**
     * 获取列表数据
     */
    private void getListDatas() {
        HosptialHttpEvent event = new HosptialHttpEvent();
        event.getNearByHosptial();
        event.setHttpListener(new HttpListener() {
            @Override
            public void onError(int code, String msg) {
                ToastUtils.show(mContext, msg,0);
                if (ptrClassicFrameLayout.isLoadingMore()) {
                    ptrClassicFrameLayout.loadMoreComplete(true);
                }
                if (ptrClassicFrameLayout.isRefreshing()) {
                    ptrClassicFrameLayout.refreshComplete();
                }
            }

            @Override
            public void onSuccess(Object o) {
                NearbyHospitalBean nearbyHospitalBean = new Gson().fromJson((String) o, NearbyHospitalBean.class);
                hosptials = nearbyHospitalBean.getResultData();
                listView.setAdapter(new NearByHospitalAdapter(mContext, hosptials));
            }
        });
        new HttpClient(mContext,event,"",null).start();


    }

    @OnClick(R.id.navigation_btn)
    public void onViewClicked() {
        finish();
    }

    private class NearByHospitalAdapter extends CommonAdapter {

        public NearByHospitalAdapter(Context mContext, List datas) {
            super(mContext, R.layout.registration_hospital,datas);
        }

        @Override
        public void convert(ViewHolder holder, Object obj, int position) {
            NearbyHospitalBean.ResultDataBean resultDataBean = (NearbyHospitalBean.ResultDataBean) obj;
            ImageView hospital_img = (ImageView) holder.findViewById(R.id.hospital_img);
            TextView hospital_name = (TextView) holder.findViewById(R.id.hospital_name);
            TextView hospital_address =(TextView) holder.findViewById(R.id.hospital_address);
            TextView tv_distance = (TextView)holder.findViewById(R.id.tv_distance);
            ImageLoader.getInstance().displayImage(resultDataBean.getPictureUrl(), hospital_img,
                    LibUtils.getSquareDisplayOptions(R.mipmap.icon_default_loadding));
            hospital_name.setText(resultDataBean.getResultTitle());
            hospital_address.setText(resultDataBean.getAddress());
            tv_distance.setText(resultDataBean.getGeoDistance());
        }
    }
}
