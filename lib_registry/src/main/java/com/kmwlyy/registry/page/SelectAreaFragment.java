package com.kmwlyy.registry.page;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.kmwlyy.core.base.BaseFragment;
import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.util.CommonUtils;
import com.kmwlyy.core.util.SPUtils;
import com.kmwlyy.core.util.ToastUtils;
import com.kmwlyy.registry.R;
import com.kmwlyy.registry.R2;
import com.kmwlyy.registry.bean.Contants;
import com.kmwlyy.registry.net.NetBean;
import com.kmwlyy.registry.net.NetEvent;
import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;
import com.winson.ui.widget.AlterDialogView;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016-8-15.
 */
public class SelectAreaFragment extends BaseFragment implements TencentLocationListener {

    @BindView(R2.id.refresh_left)
    SwipeRefreshLayout mRefreshLeft;

    @BindView(R2.id.refresh_right)
    SwipeRefreshLayout mRefreshRight;

    @BindView(R2.id.recycler_left)
    ListView mLeftRecyclerView;

    @BindView(R2.id.recycler_right)
    RecyclerView mRightRecyclerView;

    @BindView(R2.id.tv_localCity)
    TextView tv_localCity;

    private List<NetBean.Area> mLeftList = new ArrayList<>();
    private List<NetBean.Area> mRightList;
    private Map<Integer, List<NetBean.Area>> mAreaMap;

    protected TencentLocationManager locationManager;
    protected String cityName = "";//处理后的城市名
    protected String locationCity = "";//定位到的完整城市名
    protected String province = "";//处理后的省份名

    protected NetBean.Area locationArea = new NetBean.Area();//定位到的城市信息

    protected NetBean.Area hotCityParents = new NetBean.Area();//热门城市
    protected List<NetBean.Area> hotCityList = new ArrayList<>();//热门城市

    private final int hotCityAreaId = -5124;//热门城市的id

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_select_city;
    }

    @Override
    protected void afterBindView() {

        hotCityParents.setAREA_Name(getString(R.string.hotCity));
        hotCityParents.setAREA_ID(hotCityAreaId);

        mAreaMap = new HashMap<>();
        Contants.initImageLoader(mContext);
        mRefreshLeft.post(new Runnable() {
            @Override
            public void run() {
                getAreaList(0);
            }
        });

        mCenterText.setText(getResources().getString(R.string.r_select_city));
        mLeftRecyclerView.setAdapter(mLeftAdapter);
        mRightRecyclerView.setAdapter(mRightAdapter);
        mRightRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRefreshLeft.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAreaList(0);
            }
        });
        mRefreshRight.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mLeftList != null && mLeftList.size() != 0) {
                    NetBean.Area item = mLeftList.get(mCheckIndex);
                    mAreaMap.remove(item.getAREA_ID());
                    getAreaList(item.getAREA_ID());
                } else {
                    mRefreshRight.setRefreshing(false);
                }
            }
        });

        if(CommonUtils.checkReadStoragePermissions(getActivity(),this)){
            getLocation();
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults!=null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
        {
            getLocation();
        }
        else{
            tv_localCity.setText(R.string.get_location_fail);
        }
    }

    /*******************************************************/
    /*获取热门城市*/
    private void getHotAreaList(){
        NetEvent.GetHotCity event = new NetEvent.GetHotCity();
        event.setHttpListener(new HttpListener<String>() {
            @Override
            public void onError(int code, String msg) {
                ToastUtils.showShort(getActivity(), msg);
                mRefreshLeft.setRefreshing(false);
                mRefreshRight.setRefreshing(false);
            }

            @Override
            public void onSuccess(String result) {
                mRefreshLeft.setRefreshing(false);
                mRefreshRight.setRefreshing(false);
                try {
                    List<NetBean.Area> areas = new ArrayList<NetBean.Area>();
                    JSONArray jsonArray = new JSONArray(result);
                    for (int i = 0 ;i < jsonArray.length(); i++){
                        NetBean.Area area = new NetBean.Area();
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        area.setAREA_ID(jsonObject.getInt("CITY_ID"));
                        area.setAREA_Name(jsonObject.getString("CITY_NAME"));
                        areas.add(area);
                    }
                    hotCityList.clear();
                    hotCityList.addAll(areas);
                    mAreaMap.put(hotCityAreaId, hotCityList);
                    notifyRightListChanged();
                }catch (Exception e){}

            }
        });
        new HttpClient(getActivity(), event).start();
    }


    private void getAreaList(final int areaid) {//获取区域列表
        if (areaid == 0) {
            mRefreshLeft.setRefreshing(true);
        }
        else if (areaid == hotCityAreaId){
            //获取热门城市
            mRefreshRight.setRefreshing(true);
            getHotAreaList();
            return;
        }
        else {
            mRefreshRight.setRefreshing(true);
        }
        NetEvent.GetArea event = new NetEvent.GetArea(areaid + "");
        event.setHttpListener(new HttpListener<List<NetBean.Area>>() {
            @Override
            public void onError(int code, String msg) {
                ToastUtils.showShort(getActivity(), msg);
                mRefreshLeft.setRefreshing(false);
                mRefreshRight.setRefreshing(false);
            }

            @Override
            public void onSuccess(List<NetBean.Area> areas) {
                mRefreshLeft.setRefreshing(false);
                mRefreshRight.setRefreshing(false);
                if (areas != null) {
                    if (areaid == 0) {
                        mLeftList.clear();
                        mLeftList.add(hotCityParents);//热门城市
                        mLeftList.addAll(areas);
                        mLeftAdapter.notifyDataSetChanged();
                    } else {
                        mAreaMap.put(areaid, areas);
                        notifyRightListChanged();
                    }
                }
            }
        });
        new HttpClient(getActivity(), event).start();
    }

    /* 尝试获取定位到的城市关联ID */
    private void getLocationCityInfo() {
        NetEvent.GetOneArea event = new NetEvent.GetOneArea(province, cityName);
        event.setHttpListener(new HttpListener<NetBean.Area>() {
            @Override
            public void onError(int code, String msg) {
                tv_localCity.setText(R.string.get_location_fail);
            }

            @Override
            public void onSuccess(NetBean.Area area) {
                tv_localCity.setText(locationCity);
                locationArea.setAREA_ID(area.getAREA_ID());
                locationArea.setAREA_Name(area.getAREA_Name());
                locationArea.setAREA_LEVEL(area.getAREA_LEVEL());
                locationArea.setCITY_ID(area.getCITY_ID());
                locationArea.setPARENT_ID(area.getPARENT_ID());

                String historyAreaid = SPUtils.get(mContext, SPUtils.REG_AREAID, 5) + "";
                if (!String.valueOf(locationArea.getAREA_ID()).equalsIgnoreCase(historyAreaid)) {
                    autoAskChangeCity();
                }
            }
        });
        new HttpClient(getActivity(), event).start();
    }

    private void notifyRightListChanged() {
        if (mLeftList != null && mLeftList.size() != 0) {
            NetBean.Area item = mLeftList.get(mCheckIndex);
            if (mAreaMap.containsKey(item.getAREA_ID())) {
                mRightList = mAreaMap.get(item.getAREA_ID());
            } else {
                mRightList = null;
                getAreaList(item.getAREA_ID());
            }
            mRightAdapter.notifyDataSetChanged();
        }
    }

    /* 开始定位 */
    private void getLocation() {
        TencentLocationRequest request = TencentLocationRequest.create();
        request.setAllowCache(true);
        request.setRequestLevel(TencentLocationRequest.REQUEST_LEVEL_ADMIN_AREA);
        locationManager = TencentLocationManager.getInstance(getActivity());
        locationManager.requestLocationUpdates(request, this);
    }

    /* 定位结果的回调 */
    @Override
    public void onLocationChanged(TencentLocation tencentLocation, int error, String s) {
        if (TencentLocation.ERROR_OK == error) {
            // 定位成功
            locationCity = tencentLocation.getCity();
            cityName = cutCityName(locationCity);
            province = cutProvinceName(tencentLocation.getProvince());
            getLocationCityInfo();
        }
        else {
            tv_localCity.setText(R.string.get_location_fail);
        }
        locationManager.removeUpdates(this);
    }

    @Override
    public void onStatusUpdate(String s, int i, String s1) {

    }


    //询问是否切换城市
    private void autoAskChangeCity(){

        AlterDialogView.Builder builder = new AlterDialogView.Builder(getActivity());
        builder.setTitle(R.string.string_dialog_title);
        builder.setMessage(String.format(getResources().getString(R.string.change_location), locationCity));
        builder.setNegativeButton(R.string.r_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton(R.string.r_confirm,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0,
                                        int arg1) {
                        clickLocalCity();
                        arg0.dismiss();
                    }
                });
        builder.create().show();
    }

    @OnClick(R2.id.tv_localCity)
    public void clickLocalCity(){
        if (locationArea.getAREA_ID() == 0 && TextUtils.isEmpty(locationArea.getAREA_Name())){
            return;
        }

        SPUtils.put(mContext, SPUtils.REG_AREANAME, locationArea.getAREA_Name());
        SPUtils.put(mContext, SPUtils.REG_AREAID, locationArea.getAREA_ID());
        EventBus.getDefault().post(locationArea);
        getActivity().finish();
    }

    /* 裁剪掉市的后缀 */
    private String cutCityName(String cityName) {
        if (TextUtils.isEmpty(cityName)) {
            return "";
        }


        if (cityName.lastIndexOf("市") == cityName.length() - 1) {
            cityName = cityName.substring(0, cityName.length() - 1);
        }

        return cityName;
    }

    /* 裁剪掉省的后缀 */
    private String cutProvinceName(String provinceName) {
        if (TextUtils.isEmpty(provinceName)) {
            return "";
        }

        if (provinceName.indexOf("省") > 0) {
            provinceName = provinceName.replace("省", "");
        }
        if (provinceName.indexOf("自治区") > 0) {
            provinceName = provinceName.replace("自治区", "");
        }
        if (provinceName.indexOf("回族") > 0) {
            provinceName = provinceName.replace("回族", "");
        }
        if (provinceName.indexOf("维吾尔") > 0) {
            provinceName = provinceName.replace("维吾尔", "");
        }
        if (provinceName.indexOf("壮族") > 0) {
            provinceName = provinceName.replace("壮族", "");
        }
        if (provinceName.indexOf("特别行政区") > 0) {
            provinceName = provinceName.replace("特别行政区", "");
        }

        return provinceName;
    }

    /*******************************************************/
    private static int mCheckIndex;

    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R2.id.check_area)
        public RadioButton mCheckArea;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    BaseAdapter mLeftAdapter = new BaseAdapter() {
        @Override
        public int getCount() {
            return mLeftList == null ? 0 : mLeftList.size();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public Object getItem(int position) {
            return mLeftList.get(position);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            MyViewHolder mHolder = null;
            if (convertView == null) {
                convertView = View.inflate(getActivity(), R.layout.item_area, null);
                mHolder = new MyViewHolder(convertView);
                convertView.setTag(mHolder);
            } else {
                mHolder = (MyViewHolder) convertView.getTag();
            }
            NetBean.Area item = mLeftList.get(position);
            mHolder.mCheckArea.setText(item.getAREA_Name());
            mHolder.mCheckArea.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        mCheckIndex = position;
                        notifyDataSetChanged();
                        notifyRightListChanged();
                    }
                }
            });
            mHolder.mCheckArea.setChecked(position == mCheckIndex);
            return convertView;
        }
    };

    /*******************************************************/

    RecyclerView.Adapter mRightAdapter = new RecyclerView.Adapter() {

        @Override
        public int getItemCount() {
            return mRightList == null ? 0 : mRightList.size();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = View.inflate(getActivity(), R.layout.item_area, null);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            final NetBean.Area item = mRightList.get(position);
            MyViewHolder mHolder = (MyViewHolder) holder;
            mHolder.mCheckArea.setText(item.getAREA_Name());
            mHolder.mCheckArea.setBackgroundResource(R.drawable.bg_item_list);
            mHolder.mCheckArea.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventBus.getDefault().post(item);
                    doBack();
                }
            });
        }
    };
}
