package com.kmwlyy.imchat.message;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kmwlyy.core.util.LogUtils;
import com.kmwlyy.imchat.R;
import com.kmwlyy.imchat.adapter.ChatAdapter;
import com.tencent.TIMCustomElem;
import com.tencent.TIMMessage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * 自定义消息
 */
public class CustomMessage extends Message {


    private String TAG = getClass().getSimpleName();

    private final int TYPE_TYPING = 14;
    public static final String EXT_SURVEYQUESTION = "Survey.Question";

    private Type type;
    private String mDesc;
    private String data;
    private String ext;
    private JSONArray mAnswerArray;
    private MyKBSClickListener mKBSClickListener;
    private String mExtStr;

    public CustomMessage(TIMMessage message){
        this.message = message;
        TIMCustomElem elem = (TIMCustomElem) message.getElement(0);
        mDesc = elem.getDesc();
        parseExt(elem.getExt());
        LogUtils.i("CustomMessage","CustomMessage desc: "+elem.getDesc() + "  ext: " + new String(elem.getExt()));
        ext = new String(elem.getExt());
        parse(elem.getData());

    }

    public CustomMessage(Type type){
        message = new TIMMessage();
        String data = "";
        JSONObject dataJson = new JSONObject();
        try{
            switch (type){
                case TYPING:
                    dataJson.put("userAction",TYPE_TYPING);
                    dataJson.put("actionParam","EIMAMSG_InputStatus_Ing");
                    data = dataJson.toString();
            }
        }catch (JSONException e){
            Log.e(TAG, "generate json error");
        }
        TIMCustomElem elem = new TIMCustomElem();
        elem.setData(data.getBytes());
        message.addElement(elem);
    }


    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    private void parseExt(byte[] Ext) {
        type = Type.INVALID;
        try {
            mExtStr = new String(Ext, "UTF-8");
        } catch (Exception e) {
            LogUtils.e(TAG, "parse json error");
        }
    }
    private void parse(byte[] data){
        type = Type.INVALID;
        try{
            String str = new String(data, "UTF-8");
            JSONObject jsonObj = new JSONObject(str);
            mAnswerArray = jsonObj.optJSONArray("Answer");
            int action = jsonObj.optInt("userAction");
            switch (action){
                case TYPE_TYPING:
                    type = Type.TYPING;
                    this.data = jsonObj.getString("actionParam");
                    if (this.data.equals("EIMAMSG_InputStatus_End")){
                        type = Type.INVALID;
                    }
                    break;
            }
            //状态 0=未就诊,1=候诊中,2=就诊中,3=已就诊,4=呼叫中,5=离开中,6=患者已离开
            action = jsonObj.optInt("State");
            switch (action){
                case 0:
                    type = Type.NONE;
                    break;
                case 1:
                    type = Type.WAITING;
                    break;
                case 2:
                    type = Type.DOING;
                    break;
                case 3:
                    type = Type.DONE;
                    break;
                case 4:
                    type = Type.CALLING;
                    break;
                case 5:
                    type = Type.OFFLINE;
                    break;
                case 6:
                    type = Type.PATIENT_LEAVE;
                    break;
            }

        }catch (IOException | JSONException e){
            Log.e(TAG, "parse json error");
        }
    }

    /**
     * 显示消息
     *
     * @param viewHolder 界面样式
     * @param context    显示消息的上下文
     */
    @Override
    public void showMessage(ChatAdapter.ViewHolder viewHolder, Context context) {
        if (CustomMessage.EXT_SURVEYQUESTION.equals(mExtStr)) {//康博士消息
            LogUtils.i("CustomMessage","康博士消息_CustomMessage："+getMyAnswerArray());

            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            LinearLayout linearLayout = new LinearLayout(context);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            TextView mDescTv= new TextView(context);
            mDescTv.setLayoutParams(params);
            mDescTv.setTextColor(context.getResources().getColor(R.color.black));
            mDescTv.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
            mDescTv.setText(mDesc);
            linearLayout.addView(mDescTv);
            params.setMargins(0,20,0,0);
            for (int i = 0; i < getMyAnswerArray().length(); i++) {
                final TextView mTv= new TextView(context);
                mTv.setLayoutParams(params);
                mTv.setLineSpacing(1.2f, 1.2f);//设置行间距
                mTv.setTextColor(context.getResources().getColor(R.color.main_blue));
                mTv.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
                Drawable drawable = context.getResources().getDrawable(R.drawable.left_kbs_bg);
                //drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); //设置边界
                mTv.setBackgroundDrawable(drawable);
                mTv.setPadding(36,15,36,15);
                mTv.setText(mAnswerArray.optString(i));

                mTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mKBSClickListener!=null){
                            mKBSClickListener.kbsClickListener(mTv.getText().toString());
                        }
                    }
                });

                linearLayout.addView(mTv,i+1,params);
            }

            clearView(viewHolder);
            getBubbleView(viewHolder,false).addView(linearLayout);
            showStatus(viewHolder);
        }
    }

    /**
     * 获取消息摘要
     */
    @Override
    public String getSummary() {
        return null;
    }

    /**
     * 保存消息或消息文件
     */
    @Override
    public void save() {

    }

    public enum Type{
        NONE,
        WAITING,
        DOING,
        DONE,
        CALLING,
        OFFLINE,
        TYPING,
        PATIENT_LEAVE,
        INVALID,
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public static class reLoadData {

    }

    //用于康博士消息回调按钮点击事件到Activity
    public interface MyKBSClickListener{
        void kbsClickListener(String msg);
    }
    public void setMyKBSClickListener(MyKBSClickListener mKBSClickListener) {
        this.mKBSClickListener = mKBSClickListener;
    }

    public JSONArray getMyAnswerArray() {
        return mAnswerArray;
    }

}
