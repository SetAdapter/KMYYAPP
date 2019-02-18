package com.kmwlyy.patient.kdoctor;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kmwlyy.core.util.CommonUtils;
import com.kmwlyy.patient.R;


public class HHEmptyView extends RelativeLayout implements View.OnClickListener {

    /**
     * 默认提示TextView
     */
    private String mWarnText;
    /**
     * 默认加载中TextView
     */
    private String mLoadingText;

    /**
     * 默认提示文本
     */
    private TextView mWarnView;
    /**
     * 默认重新加载btn
     */
    private Button mLoadDataBtn;
    /**
     * 需要绑定的View
     */
    private View mBindView;

    /**
     *
     */
    private ImageView mImageView;

    private View mCustomLoadingView;

    private OnBtnClickListener onBtnClickListener;
    private AttributeSet attrs;
    private Context context;

    public HHEmptyView(Context context) {
        super(context);
    }

    public HHEmptyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public HHEmptyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        this.attrs = attrs;
        this.context = context;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.HHEmptyView, 0, 0);
        mWarnText = typedArray.getString(R.styleable.HHEmptyView_hh_empty_warn_txt);
        String buttonText = typedArray.getString(R.styleable.HHEmptyView_hh_empty_button_txt);
        mLoadingText = typedArray.getString(R.styleable.HHEmptyView_hh_empty_loading_txt);
        typedArray.recycle();

        if (TextUtils.isEmpty(mWarnText)) {
            mWarnText = "暂无相关数据";
        }

        if (TextUtils.isEmpty(buttonText)) {
            buttonText = "重新加载";
        }

        if (TextUtils.isEmpty(mLoadingText)) {
            mLoadingText = "加载中...";
        }

        LinearLayout linearLayout = new LinearLayout(context);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        linearLayout.setLayoutParams(layoutParams);
        linearLayout.setOrientation(LinearLayout.VERTICAL);


        mImageView = new ImageView(getContext());
        LinearLayout.LayoutParams mImageViewLp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mImageViewLp.gravity = Gravity.CENTER;
        linearLayout.addView(mImageView, mImageViewLp);

        mWarnView = new TextView(getContext());
        mWarnView.setTextSize(15);
        mWarnView.setTextColor(Color.parseColor("#666666"));
        LinearLayout.LayoutParams mWarnLp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mWarnLp.setMargins(0, CommonUtils.dip2px(context, 20), 0, 0);
        mWarnLp.gravity = Gravity.CENTER;
        linearLayout.addView(mWarnView, mWarnLp);

        mLoadDataBtn = new Button(getContext());
        mLoadDataBtn.setText(buttonText);
        mLoadDataBtn.setTextSize(15);
        mLoadDataBtn.setTextColor(Color.parseColor("#666666"));
        mLoadDataBtn.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.shape_common_button));
        LinearLayout.LayoutParams mLoadingDataLp = new LinearLayout.LayoutParams(CommonUtils.dip2px(context, 100), CommonUtils.dip2px(context, 45));
        mLoadingDataLp.setMargins(0, CommonUtils.dip2px(context, 20), 0, 0);
        mLoadingDataLp.gravity = Gravity.CENTER;
        linearLayout.addView(mLoadDataBtn, mLoadingDataLp);

        mLoadDataBtn.setOnClickListener(this);
        View view = LayoutInflater.from(context).inflate(R.layout.empty_loading_view, null);
        setCustomLoadingView(view);
        addView(linearLayout);
    }

    /**
     * 加载中
     */
    public void loading() {
        if (mBindView != null) {
            mBindView.setVisibility(GONE);
        }
        setVisibility(VISIBLE);
        mImageView.setVisibility(INVISIBLE);
        mLoadDataBtn.setVisibility(INVISIBLE);
        mWarnView.setVisibility(INVISIBLE);

        mCustomLoadingView.setVisibility(VISIBLE);
    }

    /**
     * 加载成功
     */
    public void success() {
        if (mBindView != null) {
            mBindView.setVisibility(VISIBLE);
        }
        setVisibility(GONE);
    }

    /**
     * 加载成功，但没有数据
     */
    public void nullData() {
        nullData(null, null);
    }

    /**
     * 加载成功，但没有数据
     *
     * @param hint 提示语
     */
    public void nullData(String hint) {
        nullData(null, hint);
    }

    /**
     * 加载成功，但没有数据
     *
     * @param drawable 为null,显示默认图
     * @param hint     提示语,为null时
     */
    public void nullData(Drawable drawable, String hint) {
        if (mBindView != null) {
            mBindView.setVisibility(VISIBLE);
        }
        setVisibility(VISIBLE);
        mCustomLoadingView.setVisibility(GONE);
        mWarnView.setVisibility(VISIBLE);
        mImageView.setVisibility(VISIBLE);
        mLoadDataBtn.setVisibility(INVISIBLE);

        if (drawable == null) {
            mImageView.setImageDrawable(getContext().getResources().getDrawable(R.mipmap.bg_empty_data));
        } else {
            mImageView.setImageDrawable(drawable);
        }
        mWarnView.setText(hint == null ? mWarnText : hint);
    }

    /**
     * 加载成功，但没有数据
     *
     * @param drawable 为null,显示默认图
     * @param hint     提示语,为null时
     */
    public void nullData(Drawable drawable, SpannableStringBuilder hint, String str) {
        if (mBindView != null) {
            mBindView.setVisibility(VISIBLE);
        }
        setVisibility(VISIBLE);
        mCustomLoadingView.setVisibility(GONE);
        mWarnView.setVisibility(VISIBLE);
        mImageView.setVisibility(VISIBLE);

        if (drawable == null) {
            mImageView.setImageDrawable(getContext().getResources().getDrawable(R.mipmap.bg_empty_data));
        } else {
            mImageView.setImageDrawable(drawable);
        }
        mWarnView.setText(hint == null ? mWarnText : hint);
    }

    /**
     * 加载失败
     */
    public void empty() {
        empty("网络好像不太给力，请检查网络再试试");
    }

    /**
     * 加载失败
     *
     * @param msg 加载失败提示语
     */
    public void empty(String msg) {

        if (!TextUtils.isEmpty(msg)) {
            mWarnText = msg;
        }

        if (mBindView != null) {
            mBindView.setVisibility(GONE);
        }
        setVisibility(VISIBLE);
        mImageView.setVisibility(VISIBLE);
        mLoadDataBtn.setVisibility(VISIBLE);
        mWarnView.setVisibility(VISIBLE);
        mWarnView.setText(mWarnText);
        mCustomLoadingView.setVisibility(GONE);
        mImageView.setImageDrawable(getContext().getResources().getDrawable(R.mipmap.bg_net_error));
    }

    /**
     * 加载失败
     *
     * @param msg 加载失败提示语
     */
    public void empty(int resId, String msg) {

        if (!TextUtils.isEmpty(msg)) {
            mWarnText = msg;
        }

        if (mBindView != null) {
            mBindView.setVisibility(GONE);
        }
        setVisibility(VISIBLE);
        mImageView.setVisibility(VISIBLE);
        mLoadDataBtn.setVisibility(VISIBLE);
        mWarnView.setVisibility(VISIBLE);
        mWarnView.setText(mWarnText);
        mCustomLoadingView.setVisibility(GONE);
        mImageView.setImageResource(resId);
    }

    /**
     * 设置绑定view
     *
     * @param view
     */
    public void bindView(View view) {
        this.mBindView = view;
    }

    public void setOnBtnClickListener(OnBtnClickListener onBtnClickListener) {
        this.onBtnClickListener = onBtnClickListener;
    }

    @Override
    public void onClick(View v) {
        if (onBtnClickListener != null) {
            onBtnClickListener.onBtnClick();
        } else {
//            throw new IllegalArgumentException("must be set click");
        }
    }

    /**
     * 设置自定义加载view
     *
     * @param view
     */
    public void setCustomLoadingView(View view) {
        if (view != null) {
            mCustomLoadingView = view;
            mWarnView.setVisibility(GONE);

            LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.CENTER_IN_PARENT);
            params.addRule(RelativeLayout.ABOVE, R.id.id_hh_empty_btn_view);
            ImageView imageView = (ImageView) view.findViewById(R.id.iv_anim_bg);

            imageView.setImageResource(R.drawable.loading_anim_union);
            AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getDrawable();
            animationDrawable.start();
            addView(view, params);
            invalidate();
        }
    }

    public interface OnBtnClickListener {
        /**
         * 重新加载回调接口
         */
        void onBtnClick();
    }

    public void setBtnInvisiable(boolean b) {
        if (b) {
            mLoadDataBtn.setVisibility(INVISIBLE);
        } else {
            mLoadDataBtn.setVisibility(VISIBLE);
        }
    }
}
