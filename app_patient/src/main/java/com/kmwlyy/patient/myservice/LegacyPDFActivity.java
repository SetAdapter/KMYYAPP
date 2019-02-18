package com.kmwlyy.patient.myservice;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnErrorListener;
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
import com.kmwlyy.patient.helper.net.event.HttpUserOPDRegisters;
import com.kmwlyy.patient.helper.utils.EventApi;
import com.kmwlyy.patient.helper.utils.PUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import es.voghdev.pdfviewpager.library.remote.DownloadFile;
import es.voghdev.pdfviewpager.library.remote.DownloadFileUrlConnectionImpl;

/**
 * Sample Activity for API Levels under 21
 */
public class LegacyPDFActivity extends BaseActivity implements DownloadFile.Listener,OnErrorListener {

    public static final String TAG = "LegacyPDFActivity";

    @BindView(R.id.tv_center)
    TextView tv_center;
    @BindView(R.id.tv_right)
    TextView tv_right;
    @BindView(R.id.pdfView)
    PDFView pdfView;


    DownloadFile downloadFile;

    String id, url;
    String mOPDRegisterID;

    private ArrayList<ConsultRecipeBean> recipeFiles = new ArrayList<>();

    private HttpClient mHttpClient;

    private boolean loadRecipeFinish = false;

    private boolean loadPDFSuccess = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_legacy_pdf);
        butterknife.ButterKnife.bind(this);

        try {
            Bundle bundle = getIntent().getExtras();
            mOPDRegisterID = bundle.getString("OPDRegisterID");
            url = bundle.getString("url");
            id = bundle.getString("id") + ".pdf";
        } catch (Exception e) {
        }

        tv_center.setText(R.string.diagnose_detail);
        tv_right.setText(R.string.buy_medicines);
        tv_right.setVisibility(View.VISIBLE);
        downloadPDF();
        getRecipesInfo();
    }



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

    private void downloadPDF(){
        // 请求头部
        String noneStr = CommonUtils.createRandomString();
        String appToken = BaseApplication.instance.getAppToken();
        String userToken = BaseApplication.instance.getUserData() == null ? null : BaseApplication.instance.getUserData().mUserToken;
        String sign = CommonUtils.createSignString(noneStr, appToken, userToken);
        String[] headers = new String[]{sign, noneStr, appToken, userToken};
        downloadFile = new DownloadFileUrlConnectionImpl(this, new Handler(), this);
        downloadFile.download(headers,url,
                new File(getExternalFilesDir("pdf"), id).getAbsolutePath());

    }

    @Override
    public void onSuccess(String url, String destinationPath) {
        if (loadPDFSuccess) {
            File file = new File(destinationPath);
            pdfView.fromFile(file)
//                    .pages(0, 2, 1, 3, 3, 3) // all pages are displayed by default
                    .enableSwipe(true)
                    .swipeHorizontal(true)
                    .enableDoubletap(true)
                    .defaultPage(0)
//                .onDraw(onDrawListener)
//                .onLoad(onLoadCompleteListener)
//                .onPageChange(onPageChangeListener)
//                .onPageScroll(onPageScrollListener)
                    .onError(this)
                    .enableAnnotationRendering(false)
                    .password(null)
                    .scrollHandle(null)
                    .load();
        }
    }

    //PDF下载失败
    @Override
    public void onFailure(Exception e) {
        loadPDFSuccess = false;
    }

    @Override
    public void onProgressUpdate(int progress, int total) {
    }

    //PDF打开失败
    @Override
    public void onError(Throwable t) {
    }

    @Override
    protected void onDestroy() {
        NetService.closeClient(mHttpClient);
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void recipeFileRefresh(EventApi.RefreshPDFRecipeFiles event) {
        loadRecipeFinish = false;
        getRecipesInfo();
    }
}