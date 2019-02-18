package com.kmwlyy.patient.myservice;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kmwlyy.core.base.BaseApplication;
import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.util.CommonUtils;
import com.kmwlyy.core.util.DebugUtils;
import com.kmwlyy.core.util.ToastUtils;
import com.kmwlyy.patient.R;
import com.kmwlyy.patient.helper.base.BaseActivity;
import com.kmwlyy.patient.helper.net.NetService;
import com.kmwlyy.patient.helper.net.bean.ConsultRecipeBean;
import com.kmwlyy.patient.helper.net.bean.ConsultRecipeListBean;
import com.kmwlyy.patient.helper.net.bean.UserOPDRegisters;
import com.kmwlyy.patient.helper.net.bean.VVConsultDetailBean;
import com.kmwlyy.patient.helper.net.event.HttpUserOPDRegisters;
import com.kmwlyy.patient.helper.utils.EventApi;
import com.kmwlyy.patient.helper.utils.PUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import es.voghdev.pdfviewpager.library.RemotePDFViewPager;
import es.voghdev.pdfviewpager.library.adapter.PDFPagerAdapter;
import es.voghdev.pdfviewpager.library.remote.DownloadFile;

/**
 * @Description描述: 查看处方的PDF
 * @Author作者: zhaoqile
 * @Date日期: 2016/12/26
 */
public class PDFActivity extends BaseActivity implements DownloadFile.Listener {
    public static final String TAG = "PDFActivity";

    @BindView(R.id.tv_center)
    TextView tv_center;
    @BindView(R.id.tv_right)
    TextView tv_right;

    PDFPagerAdapter adapter;
    RemotePDFViewPager remotePDFViewPager;
    String id, url;
    FrameLayout body;
    String mOPDRegisterID;

    private ArrayList<ConsultRecipeBean> recipeFiles = new ArrayList<>();

    private HttpClient mHttpClient;

    private boolean loadRecipeFinish = false;

    private boolean loadPDFSuccess = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);
        butterknife.ButterKnife.bind(this);

        try {
            Bundle bundle = getIntent().getExtras();
            mOPDRegisterID = bundle.getString("OPDRegisterID");
            url = bundle.getString("url");
            id = bundle.getString("id");
//            recipeFiles.addAll((ArrayList<ConsultRecipeBean>) bundle.getSerializable("recipeFiles"));

//        url = "http://10.2.29.123:8001" + "/DoctorRecipeFile/" + "0fd9805abaea4e28b9077447c4946383";
//        id = "0fd9805abaea4e28b9077447c4946383";
        } catch (Exception e) {

        }

        Log.i(TAG, url);

        initView();
        getRecipesInfo();
    }

    public void initView() {

        tv_center.setText(R.string.diagnose_detail);
        tv_right.setText(R.string.buy_medicines);
        tv_right.setVisibility(View.VISIBLE);
//        ((TextView)findViewById(R.id.tv_title_center)).setText(getResources().getString(R.string.string_drugrecipe_detail));
//        mLeftBtn = (TextView) findViewById(R.id.tv_tools_left);
//        mLeftBtn.setVisibility(View.VISIBLE);
//        mLeftBtn.setOnClickListener(this);
        body = (FrameLayout) findViewById(R.id.body);

        // 请求头部
        String noneStr = CommonUtils.createRandomString();
        String appToken = BaseApplication.instance.getAppToken();
        String userToken = BaseApplication.instance.getUserData() == null ? null : BaseApplication.instance.getUserData().mUserToken;
        String sign = CommonUtils.createSignString(noneStr, appToken, userToken);
        String[] headers = new String[]{sign, noneStr, appToken, userToken};

        remotePDFViewPager = new RemotePDFViewPager(this, url, id, this, headers);
        remotePDFViewPager.setId(R.id.pdfViewPager);
    }

    private void getRecipesInfo() {
        HttpUserOPDRegisters.GetRecipes GetRecipes = new HttpUserOPDRegisters.GetRecipes(mOPDRegisterID, new HttpListener<ConsultRecipeListBean>() {
            @Override
            public void onError(int code, String msg) {
                if (DebugUtils.debug) {
                    Log.d(TAG, DebugUtils.errorFormat("GetRecipes", code, msg));
                }
                loadRecipeFinish = true;
            }

            @Override
            public void onSuccess(ConsultRecipeListBean data) {
                Log.d(TAG, DebugUtils.successFormat("GetRecipes", PUtils.toJson(data)));
                loadRecipeFinish = true;
                recipeFiles.clear();
                recipeFiles.addAll(data.ConsultRecipeList);
            }
        });

        mHttpClient = NetService.createClient(this, GetRecipes);
        mHttpClient.start();
    }


    @Override
    public void onSuccess(String url, String destinationPath) {
        // That's the positive case. PDF Download went fine
        if (loadPDFSuccess) {
            adapter = new PDFPagerAdapter(this, id);
            remotePDFViewPager.setAdapter(adapter);
            updateLayout();
        }
    }

    public void updateLayout() {
        body.removeAllViewsInLayout();
        body.addView(remotePDFViewPager,
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onFailure(Exception e) {
        // This will be called if download fails
        Log.i(TAG, "download failed");
        loadPDFSuccess = false;
    }

    @Override
    public void onProgressUpdate(int progress, int total) {
        // You will get download progress here
        // Always on UI Thread so feel free to update your views here
        Log.i(TAG, progress + "/" + total);
    }

    @Override
    protected void onDestroy() {
        NetService.closeClient(mHttpClient);
        super.onDestroy();
        if (adapter != null) {
            adapter.close();
        }
    }

    //购买药品
    @OnClick(R.id.tv_right)
    public void clickBuy() {
        if (loadRecipeFinish) {
            Intent intentSD = new Intent(this, SelectDiagnoseActivity.class);
            Bundle bundleSD = new Bundle();
            bundleSD.putString("OPDRegisterID", mOPDRegisterID);
            bundleSD.putSerializable("recipeFiles", recipeFiles);
            intentSD.putExtras(bundleSD);
            startActivity(intentSD);
        } else {
            ToastUtils.showLong(this, R.string.reload_recipeFile);
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void recipeFileRefresh(EventApi.RefreshPDFRecipeFiles event) {
        loadRecipeFinish = false;
        getRecipesInfo();
    }
}
