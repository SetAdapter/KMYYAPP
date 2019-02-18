package com.kmwlyy.patient.myservice;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.util.CommonUtils;
import com.kmwlyy.core.util.DebugUtils;
import com.kmwlyy.core.util.ToastUtils;
import com.kmwlyy.patient.R;
import com.kmwlyy.patient.helper.base.BaseActivity;
import com.kmwlyy.patient.helper.net.NetService;
import com.kmwlyy.patient.helper.net.bean.ConsultRecipeBean;
import com.kmwlyy.patient.helper.net.bean.CreateOrderBean;
import com.kmwlyy.patient.helper.net.event.HttpUserRecipeOrders;
import com.kmwlyy.patient.helper.utils.EventApi;
import com.kmwlyy.patient.helper.utils.PUtils;
import com.kmwlyy.patient.pay.PayActivity;

import java.math.BigDecimal;
import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @Description描述: 选择要购买的处方
 * @Author作者: zhaoqile
 * @Date日期: 2016/12/28
 */
public class SelectDiagnoseActivity extends BaseActivity{
    private static final String TAG = SelectDiagnoseActivity.class.getSimpleName();


    @BindView(R.id.listView)
    ListView listView;

    HttpClient createOrderClient;

    private String mOPDRegisterID;
    private float totalFee = 0.0f;

    private ArrayList<ConsultRecipeBean> recipeFiles = new ArrayList<>();//处方
    private ArrayList<ConsultRecipeBean> selectRecipeFiles = new ArrayList<>();//处方

    public String recipeFileNames = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_diagnose);
        butterknife.ButterKnife.bind(this);

        try{
            Bundle bundle = getIntent().getExtras();
            mOPDRegisterID = bundle.getString("OPDRegisterID");
            recipeFiles.clear();
            recipeFiles.addAll((ArrayList<ConsultRecipeBean>)bundle.getSerializable("recipeFiles"));
        }
        catch (Exception e){}

        //没有处方就退出
        if (recipeFiles==null || recipeFiles.size() <= 0) {
            ToastUtils.showLong(this,R.string.no_diagnose_can_buy);
            finish();
            return;
        }

        for (int i = recipeFiles.size() - 1; i >= 0; i-- ) {
            //移除非中药的处方
            if (recipeFiles.get(i).mRecipeType != 1){
                recipeFiles.remove(i);
            }
        }

        //没有处方就退出
        if (recipeFiles==null || recipeFiles.size() <= 0) {
            ToastUtils.showLong(this,R.string.no_diagnose_can_buy);
            finish();
        }



        MyAdapter myAdapter = new MyAdapter(this);
        listView.setAdapter(myAdapter);

        //初始化activity宽高
        initActivityWH(listView);

    }

    //初始化activity宽高
    private void initActivityWH(ListView listView){
        try{
            //宽度
            int width = (int)(CommonUtils.getScreenWidth(this) * 0.7);
            //高度
            int height = (int)(CommonUtils.getScreenHeight(this) * 0.7);
            //Y轴位移
            int y =  0 - (int)(CommonUtils.getScreenHeight(this) * 0.1);

            WindowManager m = getWindowManager();
//            Display d = m.getDefaultDisplay();  //为获取屏幕宽、高
            WindowManager.LayoutParams p = getWindow().getAttributes();  //获取对话框当前的参数值

            int allChildHeight = PUtils.dip2px(this,88) + getTotalHeightOfListView(listView) + 2;//所有控件的高度


            if (allChildHeight > height) {
                p.height = height;//根据
            }else{
                p.height = allChildHeight;
            }



            p.width = width;
            p.y = y;
//            p.alpha = 1.0f;      //设置本身透明度
//            p.dimAmount = 0.0f;      //设置黑暗度



            getWindow().setAttributes(p);
        }
        catch (Exception e){}
    }


    /**
     * 获取ListView的高度
     * @return ListView的高度
     */
    public int getTotalHeightOfListView(ListView list) {
        //ListView的适配器
        ListAdapter mAdapter = list.getAdapter();
        if (mAdapter == null) {
            return 0;
        }
        int totalHeight = 0;
        //循环适配器中的每一项
        for (int i = 0; i < mAdapter.getCount(); i++) {
            //得到每项的界面view
            View mView = mAdapter.getView(i, null, list);
            //得到一个view的大小
            mView.measure(
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

            //总共ListView的高度
            totalHeight += mView.getMeasuredHeight();
        }
        return totalHeight;
    }


    @Override
    public void onDestroy() {
        if (createOrderClient!=null) {
            NetService.closeClient(createOrderClient);
        }
        super.onDestroy();
    }


    //点击取消
    @OnClick(R.id.tv_cancel)
    public void clickCancel() {
        finish();
    }


    //点击购买
    @OnClick(R.id.tv_buy)
    public void clickBuy() {
        if (selectRecipeFiles.size() <= 0  || TextUtils.isEmpty(mOPDRegisterID)) {
            ToastUtils.showLong(SelectDiagnoseActivity.this,R.string.no_select_diagnose);
        }
        else{
            createOrder();
        }

    }

    //联网请求创建订单
    private void createOrder(){
        showLoadDialog(R.string.create_order);
        final ArrayList<String> selectRecipeNos = new ArrayList<>();
        totalFee = 0.00f;
        for(ConsultRecipeBean bean : selectRecipeFiles) {
            selectRecipeNos.add(bean.mRecipeNo);
            totalFee += bean.mAmount;
            recipeFileNames += bean.mRecipeName;
            recipeFileNames += "/r/n";
        }

        if (recipeFileNames.length() > 4){
            recipeFileNames = recipeFileNames.substring(0,recipeFileNames.length()-4);
        }


        HttpUserRecipeOrders.CreateOrder createOrder = new HttpUserRecipeOrders.CreateOrder(
                mOPDRegisterID,
                selectRecipeNos,
                new HttpListener<CreateOrderBean>() {
                    @Override
                    public void onError(int code, String msg) {
                        if (DebugUtils.debug) {
                            Log.d(TAG, DebugUtils.errorFormat("createOrder",code,msg));
                        }
                        dismissLoadDialog();
                        ToastUtils.showLong(SelectDiagnoseActivity.this,R.string.create_order_fail);
                    }

                    @Override
                    public void onSuccess(CreateOrderBean bean) {
                        if (DebugUtils.debug) {
                            Log.d(TAG, DebugUtils.successFormat("createOrder", DebugUtils.toJson(bean)));
                        }
                        dismissLoadDialog();
                        try {
                            Intent pay = new Intent();
                            pay.putExtra("orderNo", bean.mOrderNo);
                            BigDecimal b = new BigDecimal(Float.toString(totalFee));
                            pay.putExtra("totalFee", b.doubleValue());
                            pay.putExtra("info", recipeFileNames);
                            pay.putExtra("num", selectRecipeNos.size());
                            pay.putExtra("time", selectRecipeFiles.get(0).mRecipeDate);
                            pay.setClass(SelectDiagnoseActivity.this, PrescriptionPayActivity.class);
                            startActivity(pay);
                            SelectDiagnoseActivity.this.finish();
                        }catch (Exception e){}

                        EventBus.getDefault().post(new EventApi.RefreshPDFRecipeFiles());
                    }
                }
        );

        createOrderClient = NetService.createClient(this, createOrder);
        createOrderClient.start();
    }



    public class MyAdapter extends BaseAdapter{

        private LayoutInflater inflater;


        public MyAdapter(Context context) {
            this.inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return recipeFiles.size();
        }

        @Override
        public ConsultRecipeBean getItem(int position) {
            return recipeFiles.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView != null) {
                holder = (ViewHolder) convertView.getTag();
            } else {
                convertView = inflater.inflate(R.layout.item_select_diagnose, parent, false);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            }
            final ConsultRecipeBean recipeFile = getItem(position);



            holder.tv_id.setText(recipeFile.mRecipeFileID);
            holder.tv_name.setText(recipeFile.mRecipeName);
            holder.tv_price.setText(CommonUtils.convertTowDecimalStr(recipeFile.mAmount));


            /*  判断处方购买状态 */
            if (!TextUtils.isEmpty(recipeFile.mOrderNo)){
                holder.iv_select.setImageDrawable(getResources().getDrawable(R.drawable.select_unable));
                holder.tv_state.setText(R.string.purchased);
                holder.tv_state.setTextColor(getResources().getColor(R.color.primary_color));
            }
            else if (recipeFile.isExpried){
                //已过期
                holder.iv_select.setImageDrawable(getResources().getDrawable(R.drawable.select_unable));
                holder.tv_state.setText(R.string.expired);
                holder.tv_state.setTextColor(getResources().getColor(R.color.primary_color));
            }
            else{
                for (ConsultRecipeBean temp : selectRecipeFiles){
                    if (temp.mRecipeFileID.equals(recipeFile.mRecipeFileID)){
                        holder.iv_select.setSelected(true);
                        holder.iv_select.setImageDrawable(getResources().getDrawable(R.drawable.select));
                    }else{
                        holder.iv_select.setSelected(false);
                        holder.iv_select.setImageDrawable(getResources().getDrawable(R.drawable.unselect));
                    }
                }

                holder.tv_state.setText(R.string.unpaid);
                holder.tv_state.setTextColor(getResources().getColor(R.color.app_yellow));
            }

            holder.ll_root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (TextUtils.isEmpty(recipeFile.mOrderNo) && !recipeFile.isExpried) {
                        if (holder.iv_select.isSelected()) {
                            holder.iv_select.setImageDrawable(
                                SelectDiagnoseActivity.this.getResources()
                                    .getDrawable(R.drawable.unselect));
                            holder.iv_select.setSelected(false);
                            selectRecipeFiles.remove(recipeFile);
                        } else {
                            holder.iv_select.setImageDrawable(
                                SelectDiagnoseActivity.this.getResources()
                                    .getDrawable(R.drawable.selected));
                            holder.iv_select.setSelected(true);
                            selectRecipeFiles.add(recipeFile);
                        }
                    }
                }
            });

            return convertView;
        }
    }

    public class ViewHolder{
        @BindView(R.id.ll_root)
        public LinearLayout ll_root;
        @BindView(R.id.iv_select)
        public ImageView iv_select;
        @BindView(R.id.tv_type)
        public TextView tv_type;
        @BindView(R.id.tv_id)
        public TextView tv_id;
        @BindView(R.id.tv_name)
        public TextView tv_name;
        @BindView(R.id.tv_state)
        public TextView tv_state;
        @BindView(R.id.tv_price)
        public TextView tv_price;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
