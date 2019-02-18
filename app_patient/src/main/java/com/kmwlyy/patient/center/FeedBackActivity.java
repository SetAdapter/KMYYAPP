package com.kmwlyy.patient.center;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.net.event.FeedbackEvent;
import com.kmwlyy.core.util.DebugUtils;
import com.kmwlyy.core.util.ToastUtils;
import com.kmwlyy.patient.R;
import com.kmwlyy.patient.helper.base.BaseActivity;
import com.kmwlyy.patient.helper.net.NetService;
import com.kmwlyy.patient.helper.net.bean.UploadImageResp;
import com.kmwlyy.patient.helper.net.event.HttpIM;
import com.kmwlyy.patient.onlinediagnose.UploadImageAdapter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import me.nereo.multi_image_selector.MultiImageSelector;

/**
 * @Description描述:
 * @Author作者: zhaoqile
 * @Date日期: 2016/12/15
 */
public class FeedBackActivity extends BaseActivity {

  public static final String TAG = FeedBackActivity.class.getSimpleName();
  public static final String SUBJECT = "康美医疗";

  @BindView(R.id.tv_center)
  TextView tv_center;


  @BindView(R.id.feedback_content)
  EditText feedback_content;
  @BindView(R.id.grid_upload_pictures)
  GridView grid_upload_pictures;
  @BindView(R.id.submit)
  TextView submit;
  @BindView(R.id.tv_length)
  TextView tv_length;


  private static final int REQUEST_IMAGE = 2;


  private ArrayList<String> mSelectPath = new ArrayList<>();
  /**
   * 需要上传的图片路径  控制默认图片在最后面需要用LinkedList
   */
  private LinkedList<String> dataList = new LinkedList<String>();
  /**
   * 图片上传Adapter
   */
  private UploadImageAdapter adapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_feed_back);
    butterknife.ButterKnife.bind(this);
    initView();
  }

  private void initView() {
    tv_center.setText(R.string.suggest_feedback);

    dataList.addLast(null);// 初始化第一个添加按钮数据
    adapter = new UploadImageAdapter(this, dataList, 9);
    grid_upload_pictures.setAdapter(adapter);
    grid_upload_pictures.setOnItemClickListener(mItemClick);

    submit.setEnabled(false);
    submit.setBackgroundResource(R.drawable.unable_btn);

    feedback_content.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {

      }

      @Override
      public void afterTextChanged(Editable s) {
        if (s.length() >= 10) {
          submit.setEnabled(true);
          submit.setBackgroundResource(R.drawable.app_btn);
        } else {
          submit.setEnabled(false);
          submit.setBackgroundResource(R.drawable.unable_btn);
        }
        int i = s.length();
        tv_length.setText(i + "/300");
      }
    });
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == REQUEST_IMAGE) {
      if (resultCode == RESULT_OK) {
        mSelectPath = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);
        if (mSelectPath.size() == 0) {
          return;
        }
        dataList.removeAll(dataList);
        dataList.addLast(null);
        for (String p : mSelectPath) {
          if (dataList.size() == 10) {
            continue;
          }
          dataList.add(dataList.size() - 1, p);
        }
        adapter.update(dataList); // 刷新图片

      }
    }
  }

  /**
   * 上传图片GridView Item单击监听
   */
  private AdapterView.OnItemClickListener mItemClick = new AdapterView.OnItemClickListener() {

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
        long id) {
      if (mSelectPath != null || mSelectPath.size() != 0){
        mSelectPath.clear();
        int i = adapter.getCount();
        for(int j=0;j< i;j++){
          String path =  (String)adapter.getItem(j);
          if (!TextUtils.isEmpty(path)) {
            mSelectPath.add(path);
          }
        }
      }
      MultiImageSelector.create(FeedBackActivity.this)
          .showCamera(true)
          .count(9)
          .multi()
          .origin(mSelectPath)
          .start(FeedBackActivity.this, REQUEST_IMAGE);

    }
  };

  @OnClick(R.id.submit)
  public void submit() {
    showLoadDialog(R.string.submit_wait);
    if (!dataList.isEmpty() && dataList.get(0) != null) {
      uploadImage();
    } else {
      sendFeedback(null);
    }
  }

  private void uploadImage() {
    final ArrayList<FeedbackEvent.ImageFile> imageFileList = new ArrayList<>();
    final int length = getLength();
    final Point point = new Point();
    for (final String path : dataList) {
      if (TextUtils.isEmpty(path)){
        continue;
      }
      HttpIM.UploadImage uploadImage = new HttpIM.UploadImage(path,
          new HttpListener<UploadImageResp>() {
            @Override
            public void onError(int code, String msg) {
//                    LogUtils.d(TAG, "upload image error : " + path);
              point.x++;
              if (point.x >= length) {
                // complete
                sendFeedback(imageFileList);
              }

            }

            @Override
            public void onSuccess(UploadImageResp data) {
              point.x++;

              FeedbackEvent.ImageFile imageFile = new FeedbackEvent.ImageFile();
              imageFile.url = data.mFileName;
              imageFile.fileName = data.md5;
              imageFileList.add(imageFile);

              if (point.x >= length) {
                // complete
                sendFeedback(imageFileList);
              }
            }
          });

      HttpClient uploadImageClient = NetService.createClient(this, uploadImage);
//                uploadImageClient.start();
      uploadImageClient.imageStart();
    }

  }

  private void sendFeedback(List<FeedbackEvent.ImageFile> imageFiles) {
    String feedBackContent = feedback_content.getText().toString();
    FeedbackEvent.Feedback sendFeedback = new FeedbackEvent.Feedback(feedBackContent, imageFiles,SUBJECT,
        new HttpListener() {
          @Override
          public void onError(int code, String msg) {
            dismissLoadDialog();
            if (DebugUtils.debug) {
              Log.d(TAG, DebugUtils.errorFormat("sendFeedback", code, msg));
            }
            ToastUtils.showLong(FeedBackActivity.this, R.string.feedback_fail);

          }

          @Override
          public void onSuccess(Object o) {
            dismissLoadDialog();
            if (DebugUtils.debug) {
              Log.d(TAG, DebugUtils.successFormat("sendFeedback", ""));
            }
            ToastUtils.showLong(FeedBackActivity.this, R.string.feedback_success);
            FeedBackActivity.this.finish();

          }
        });

    HttpClient client = NetService.createClient(this, sendFeedback);
    client.start();
  }
  private int getLength(){
    if (dataList.getLast() == null){
      return  dataList.size()-1;
    }else{
      return dataList.size();
    }
  }
}
