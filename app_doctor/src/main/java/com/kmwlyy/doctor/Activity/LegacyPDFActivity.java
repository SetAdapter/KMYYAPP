package com.kmwlyy.doctor.Activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnErrorListener;
import com.kmwlyy.core.base.BaseApplication;
import com.kmwlyy.core.util.CommonUtils;
import com.kmwlyy.doctor.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.io.File;

import es.voghdev.pdfviewpager.library.remote.DownloadFile;
import es.voghdev.pdfviewpager.library.remote.DownloadFileUrlConnectionImpl;

public class LegacyPDFActivity extends BaseActivity implements DownloadFile.Listener, OnErrorListener {

    public static final String TAG = "LegacyPDFActivity";

    @ViewInject(R.id.tv_center)
    TextView tv_title_center;
    @ViewInject(R.id.tv_left)
    TextView btn_left;

    @ViewInject(R.id.pdfView)
    PDFView pdfView;


    DownloadFile downloadFile;

    String id, url;
    String mOPDRegisterID;
    private boolean loadPDFSuccess = true;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_legacy_pdf);
        ViewUtils.inject(this);

        try {
            Bundle bundle = getIntent().getExtras();
            mOPDRegisterID = bundle.getString("OPDRegisterID");
            url = bundle.getString("url");
            id = bundle.getString("id") + ".pdf";
        } catch (Exception e) {
        }

        tv_title_center.setText(R.string.diagnose_detail);


        btn_left.setVisibility(View.VISIBLE);
        btn_left.setText(getResources().getString(R.string.string_back));
        btn_left.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        downloadPDF();
    }


    private void downloadPDF() {
        // 请求头部
        String noneStr = CommonUtils.createRandomString();
        String appToken = BaseApplication.instance.getAppToken();
        String userToken = BaseApplication.instance.getUserData() == null ? null : BaseApplication.instance.getUserData().mUserToken;
        String sign = CommonUtils.createSignString(noneStr, appToken, userToken);
        String[] headers = new String[]{sign, noneStr, appToken, userToken};
        downloadFile = new DownloadFileUrlConnectionImpl(this, new Handler(), this);
        downloadFile.download(headers, url, new File(getExternalFilesDir("pdf"), id).getAbsolutePath());

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


}
