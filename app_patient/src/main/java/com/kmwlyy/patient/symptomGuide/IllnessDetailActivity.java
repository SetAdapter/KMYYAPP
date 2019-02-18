package com.kmwlyy.patient.symptomGuide;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.kmwlyy.core.base.BaseActivity;
import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.util.CommonUtils;
import com.kmwlyy.core.util.ToastUtils;
import com.kmwlyy.patient.R;
import com.kmwlyy.patient.kdoctor.net.BaseConstants;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;

import static com.tencent.qalsdk.service.QalService.context;

/**
 * @Description描述:
 * @Author作者: hx
 * @Date日期: 2017/7/31
 */

public class IllnessDetailActivity extends BaseActivity {
    private final static int DISEASE_DETAIL = 1;//获取疾病详情
    @BindView(R.id.top_bar)
    View top_bar;
    @BindView(R.id.illness_name)
    CheckBox illness_name;//疾病名
    @BindView(R.id.illness_detail)
    TextView illness_detail;//疾病详情
    @BindView(R.id.related_symptom)
    CheckBox related_symptom;//相关病状单选按钮
    @BindView(R.id.related_symptom_detail)
    TextView related_symptom_detail;// 相关症状详情
    @BindView(R.id.check_way)
    CheckBox check_way;//检查方式
    @BindView(R.id.check_way_details)
    TextView check_way_details;//检查方式详情
    @BindView(R.id.treat_way)
    CheckBox treat_way;//治疗方式
    @BindView(R.id.treat_way_details)
    TextView treat_way_details;//诊断详情
    @BindView(R.id.prevent_way)
    CheckBox prevent_way;//预防护理
    @BindView(R.id.prevent_way_details)
    TextView prevent_way_details;//预防护理详情
    @BindView(R.id.progressBar)
    RelativeLayout progressBar;

    @BindView(R.id.scorollView)
    ScrollView scorollView;//内容的scorollView

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    private SymptomDetail symptomDetail;//疾病详情对象


    private String id;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_illness_detail;
    }

    @Override
    protected void afterBindView() {
        setOnClickEvent();
        toolbarTitle.setText("症状百科");
        progressBar.setVisibility(View.VISIBLE);
        id = getIntent().getStringExtra("id");
        getNetData(id);
    }

    /**
     * 设置点击事件
     */
    private void setOnClickEvent() {
        illness_name.setOnClickListener(this);
        illness_detail.setOnClickListener(this);
        related_symptom_detail.setOnClickListener(this);
        related_symptom.setOnClickListener(this);
        check_way.setOnClickListener(this);
        check_way_details.setOnClickListener(this);
        treat_way.setOnClickListener(this);
        treat_way_details.setOnClickListener(this);
        prevent_way.setOnClickListener(this);
        prevent_way_details.setOnClickListener(this);
    }

    /**
     * 获取网络数据
     */
    private void getNetData(String id) {
        SymptomHttpEvent symptomHttpEvent = new SymptomHttpEvent(id);
        symptomHttpEvent.setHttpListener(new HttpListener<String>() {
            @Override
            public void onError(int code, String msg) {
                requestError(code, msg);
            }

            @Override
            public void onSuccess(String o) {
                requestSuccess(o);
            }
        });
        HttpClient httpClient = new HttpClient(mContext, symptomHttpEvent,BaseConstants.SERVER_URL,null);
        httpClient.start();
    }

    @OnClick(R.id.navigation_btn)
    public void onViewClicked() {
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.illness_detail:
            case R.id.illness_name://病状详情的展开地
                showPop(symptomDetail.Data.SYMPTOM_NAME, symptomDetail == null ? "" : symptomDetail.Data.BRIEFINTRO_CONTENT, this, top_bar);
                break;
            case R.id.related_symptom_detail:
            case R.id.related_symptom://症状原因
                showPop("症狀原因", symptomDetail == null ? "" : symptomDetail.Data.CAUSE_DETAIL, this, top_bar);
                break;
            case R.id.check_way_details:
            case R.id.check_way://检查
                showPop("检查", symptomDetail == null ? "" : symptomDetail.Data.RELATED_INSPECTIONS_NLIST, this, top_bar);
                break;
            case R.id.treat_way_details:
            case R.id.treat_way://诊断
                showPop("诊断", symptomDetail == null ? "" : symptomDetail.Data.IDENTIFICATION_DETAIL, this, top_bar);
                break;
            case R.id.prevent_way_details:
            case R.id.prevent_way://预防
                showPop("预防", symptomDetail == null ? "" : symptomDetail.Data.PREVENTION_DETAIL, this, top_bar);
                break;
        }
    }

    public static void showPop(String title, String content, final Activity context, View top_bar) {
        LayoutInflater inflater = context.getLayoutInflater();
        View contentView = inflater.inflate(R.layout.fragment_disease_detail_detail, null);
        TextView titleTv = (TextView) contentView.findViewById(R.id.disease_title);
        TextView contentTv = (TextView) contentView.findViewById(R.id.tv_symptom_content);
        final ImageView arrowIv = (ImageView) contentView.findViewById(R.id.arrow_iv);
        arrowIv.setImageResource(R.mipmap.disease_baike_arrow_up);
        /** 设置旋转动画 */
        final RotateAnimation animation = new RotateAnimation(-180f, 0f, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setInterpolator(new LinearInterpolator());
        animation.setDuration(1000);//设置动画持续时间
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                arrowIv.setImageResource(R.mipmap.disease_baike_arrow_up);
//                arrowIv.setVisibility(View.GONE);
//                arrowIv1.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
//        arrowIv.startAnimation(animation);

        titleTv.setText(title);
        contentTv.setText(Html.fromHtml(content));
        Rect rect = new Rect();
        context.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        int statusBar = rect.top;// 顶部栏高度
        int titleBarHeight = CommonUtils.dip2px(context, 45);
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);

        int screenWidth = wm.getDefaultDisplay().getWidth();
        int screenHeight = wm.getDefaultDisplay().getHeight();
        int width = screenWidth - CommonUtils.dip2px(context, 40);
        int height = screenHeight - statusBar - titleBarHeight - CommonUtils.dip2px(context, 10);
        final PopupWindow popupWindow = new PopupWindow(contentView,
                width, height, true);

        popupWindow.setTouchable(true);

        popupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                Log.i("mengdd", "onTouch : ");

                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });

        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug
        popupWindow.setBackgroundDrawable(context.getResources().getDrawable(R.color.transparent));
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f, context);
            }
        });
        // 设置好参数之后再show
        popupWindow.setAnimationStyle(R.style.popwin_anim_right_style);
        popupWindow.showAsDropDown(top_bar, CommonUtils.dip2px(context, 40), 0);
        backgroundAlpha(0.4f, context);
        arrowIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arrowIv.setImageResource(R.mipmap.disease_arrow_animation);
                arrowIv.startAnimation(animation);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        popupWindow.dismiss();
                    }
                }, 1000);

            }
        });
    }

    public static void backgroundAlpha(float f, Activity context) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = f;
        context.getWindow().setAttributes(lp);
    }


    /**
     * 设置页面信息
     */
    private void initPageMsg() {
        if (symptomDetail == null || null == symptomDetail.Data) {
            return;
        }

        if (TextUtils.isEmpty(symptomDetail.Data.SYMPTOM_NAME)) {
            illness_name.setText(" ");
        } else {
            illness_name.setText(symptomDetail.Data.SYMPTOM_NAME);
        }

        if (TextUtils.isEmpty(symptomDetail.Data.BRIEFINTRO_CONTENT)) {
            illness_detail.setText(" ");
        } else {
            illness_detail.setText(symptomDetail.Data.BRIEFINTRO_CONTENT);
        }
        illness_detail.post(new Runnable() {
            @Override
            public void run() {
                if (illness_detail.getLineCount() > 3) {
                    illness_detail.setMaxLines(3);
                }
            }
        });

        if (TextUtils.isEmpty(symptomDetail.Data.CAUSE_DETAIL)) {
            related_symptom_detail.setText(" ");
        } else {
            related_symptom_detail.setText(symptomDetail.Data.CAUSE_DETAIL);
        }
        related_symptom_detail.post(new Runnable() {
            @Override
            public void run() {
                if (related_symptom_detail.getLineCount() > 3) {
                    related_symptom_detail.setMaxLines(3);
                }
            }
        });
        check_way.setText("检查");
        //检查
        if (TextUtils.isEmpty(symptomDetail.Data.RELATED_INSPECTIONS_NLIST)) {
            check_way_details.setText(" ");
        } else {
            check_way_details.setText(symptomDetail.Data.RELATED_INSPECTIONS_NLIST);
        }
        check_way_details.post(new Runnable() {
            @Override
            public void run() {
                if (check_way_details.getLineCount() > 3) {
                    check_way_details.setMaxLines(3);
                }
            }
        });

        //诊断
        treat_way.setText("诊断");
        if (TextUtils.isEmpty(symptomDetail.Data.IDENTIFICATION_DETAIL)) {
            treat_way_details.setText(" ");
        } else {
            treat_way_details.setText(symptomDetail.Data.IDENTIFICATION_DETAIL);
        }
        treat_way_details.post(new Runnable() {
            @Override
            public void run() {
                if (treat_way_details.getLineCount() > 3) {
                    treat_way_details.setMaxLines(3);
                }
            }
        });

        //预防
        prevent_way.setText("预防");
        if (TextUtils.isEmpty(symptomDetail.Data.PREVENTION_DETAIL)) {
            prevent_way_details.setText(" ");
        } else {
            prevent_way_details.setText(symptomDetail.Data.PREVENTION_DETAIL);
        }
        prevent_way_details.post(new Runnable() {
            @Override
            public void run() {
                if (prevent_way_details.getLineCount() > 3) {
                    prevent_way_details.setMaxLines(3);
                }
            }
        });
    }

    /**
     * 初始化可展开按钮的状态
     */
    private void intiCheckState() {
        illness_name.setChecked(false);
        related_symptom.setChecked(false);
        check_way.setChecked(false);
        treat_way.setChecked(false);
        prevent_way.setChecked(false);

        illness_detail.setMaxLines(3);
        check_way_details.setMaxLines(3);
        treat_way_details.setMaxLines(3);
        prevent_way_details.setMaxLines(3);
    }

    /**
     * 网络请求成功
     *
     * @param data
     */
    public void requestSuccess(String data) {
        progressBar.setVisibility(View.GONE);
        try {
            JSONObject jsonObject = new JSONObject(data);
            int ResultCode = jsonObject.optInt("ResultCode");
            if (ResultCode != 0) {
                return;
            }
            if (jsonObject.optJSONObject("Data") != null) {
                symptomDetail = new Gson().fromJson(data, SymptomDetail.class);
                scorollView.scrollTo(0, 0);//置顶
                intiCheckState();
                initPageMsg();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void requestError(int code, String msg) {
        progressBar.setVisibility(View.GONE);
        ToastUtils.show(context, "网络异常", 0);
    }
}
