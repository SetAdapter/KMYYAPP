package doctor.kmwlyy.com.recipe;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public abstract class BaseActivity extends AppCompatActivity {
    public ProgressDialog mLoadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //隐藏键盘
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    /**
     * 显示 Dialog
     */
    public void showLoadDialog(int id) {
        mLoadingDialog = new ProgressDialog(this);
        mLoadingDialog.setMessage(getResources().getString(id));
        mLoadingDialog.setCancelable(false);
        mLoadingDialog.show();
    }

    /**
     * 取消 Dialog
     */
    public void dismissLoadDialog() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
    }

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
//
//            // 获得当前得到焦点的View，一般情况下就是EditText（特殊情况就是轨迹求或者实体案件会移动焦点）
//            View v = getCurrentFocus();
//
//            if (isShouldHideInput(v, ev)) {
//                hideSoftInput(v.getWindowToken());
//            }
//        }
//        return super.dispatchTouchEvent(ev);
//    }
//
//    /**
//     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时没必要隐藏
//     *
//     * @param v
//     * @param event
//     * @return
//     */
//    private boolean isShouldHideInput(View v, MotionEvent event) {
//        if (v != null && (v instanceof EditText)) {
//            int[] l = { 0, 0 };
//            v.getLocationInWindow(l);
//            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
//                    + v.getWidth();
//            if (event.getX() > left && event.getX() < right
//                    && event.getY() > top && event.getY() < bottom) {
//                // 点击EditText的事件，忽略它。
//                return false;
//            } else {
//                return true;
//            }
//        }
//        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditView上，和用户用轨迹球选择其他的焦点
//        return false;
//    }
//
//    /**
//     * 多种隐藏软件盘方法的其中一种
//     *
//     * @param token
//     */
//    private void hideSoftInput(IBinder token) {
//        if (token != null) {
//            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//            im.hideSoftInputFromWindow(token,
//                    InputMethodManager.HIDE_NOT_ALWAYS);
//        }
//    }

    public abstract void onClick(View v);
}
