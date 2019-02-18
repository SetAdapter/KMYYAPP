package com.kmwlyy.imchat.message;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.kmwlyy.imchat.R;
import com.kmwlyy.imchat.TimApplication;
import com.kmwlyy.imchat.adapter.ChatAdapter;
import com.kmwlyy.imchat.page.WebViewActivity;
import com.kmwlyy.imchat.utils.EmoticonUtil;
import com.tencent.TIMFaceElem;
import com.tencent.TIMMessage;
import com.tencent.TIMTextElem;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * 文本消息数据
 */
public class TextMessage extends Message {

    public TextMessage(TIMMessage message){
        this.message = message;
    }

    public TextMessage(String s){
        message = new TIMMessage();
        TIMTextElem elem = new TIMTextElem();
        elem.setText(s);
        message.addElement(elem);
    }

    public void sort(Editable s, ImageSpan[] spans) {
        ImageSpan temp;
        for (int i = spans.length - 1; i > 0; --i) {
            for (int j = 0; j < i; ++j) {
                int before = s.getSpanStart(spans[j]);
                int behind = s.getSpanStart(spans[j + 1]);

                if (behind < before) {
                    temp = spans[j];
                    spans[j] = spans[j + 1];
                    spans[j + 1] = temp;
                }
            }
        }
    }

    public TextMessage(Editable s) {
        message = new TIMMessage();
        ImageSpan[] spans = s.getSpans(0, s.length(), ImageSpan.class);//发现在某些机子上这步之后spans的顺序会被打乱。导致问题。因此进行冒泡排序重新排。
        sort(s, spans);
        int currentIndex = 0;
//        for (int i =0;i<spans.length;i++){
//            ImageSpan span = spans[i];
//
        for (ImageSpan span : spans) {
            int startIndex = s.getSpanStart(span);
            int endIndex = s.getSpanEnd(span);
            if (currentIndex < startIndex){
                TIMTextElem textElem = new TIMTextElem();
                textElem.setText(s.subSequence(currentIndex, startIndex).toString());
                message.addElement(textElem);
            }
            TIMFaceElem faceElem = new TIMFaceElem();
            int index = Integer.parseInt(s.subSequence(startIndex, endIndex).toString());
            faceElem.setIndex(index);
            if (index < EmoticonUtil.emoticonData.length){
                faceElem.setData(EmoticonUtil.emoticonData[index].getBytes(Charset.forName("UTF-8")));
            }
            message.addElement(faceElem);
            currentIndex = endIndex;
        }
        if (currentIndex < s.length()){
            TIMTextElem textElem = new TIMTextElem();
            textElem.setText(s.subSequence(currentIndex, s.length()).toString());
            message.addElement(textElem);
        }

    }



    /**
     * 在聊天界面显示消息
     *
     * @param viewHolder 界面样式
     * @param context 显示消息的上下文
     */
    @Override
    public void showMessage(ChatAdapter.ViewHolder viewHolder, final Context context) {
        clearView(viewHolder);
        boolean hasText = false;
        TextView tv = new TextView(TimApplication.getContext());
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        tv.setTextColor(TimApplication.getContext().getResources().getColor(isSelf() ? R.color.white : R.color.black));
        tv.setAutoLinkMask(Linkify.WEB_URLS);
        final SpannableStringBuilder stringBuilder = new SpannableStringBuilder();
        for (int i = 0; i<message.getElementCount(); ++i){
            switch (message.getElement(i).getType()){
                case Face:
                    TIMFaceElem faceElem = (TIMFaceElem) message.getElement(i);
                    int startIndex = stringBuilder.length();
                    try{
                        AssetManager am = context.getAssets();
                        InputStream is = am.open(String.format("emoticon/%d.gif", faceElem.getIndex()));
                        if (is == null) continue;
                        Bitmap bitmap = BitmapFactory.decodeStream(is);
                        Matrix matrix = new Matrix();
                        int width = bitmap.getWidth();
                        int height = bitmap.getHeight();
                        matrix.postScale(2, 2);
                        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                                width, height, matrix, true);
                        ImageSpan span = new ImageSpan(context, resizedBitmap, ImageSpan.ALIGN_BASELINE);
                        stringBuilder.append(String.valueOf(faceElem.getIndex()));
                        stringBuilder.setSpan(span, startIndex, startIndex + getNumLength(faceElem.getIndex()), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        is.close();
                    }catch (IOException e){

                    }
                    break;
                case Text:
                    TIMTextElem textElem = (TIMTextElem) message.getElement(i);
                    stringBuilder.append(textElem.getText());
                    hasText = true;
                    break;
            }

        }
        if (!hasText){
            stringBuilder.insert(0," ");
        }
        tv.setText(stringBuilder);
        CharSequence text = tv.getText();
        if (text instanceof Spannable) {
            int end = text.length();
            Spannable sp = (Spannable) text;
            URLSpan urls[] = sp.getSpans(0, end, URLSpan.class);
            SpannableStringBuilder style = new SpannableStringBuilder(text);
            style.clearSpans();
            for (URLSpan urlSpan : urls) {
                MyURLSpan myURLSpan = new MyURLSpan(urlSpan.getURL(),context);
                style.setSpan(myURLSpan, sp.getSpanStart(urlSpan),
                        sp.getSpanEnd(urlSpan),
                        Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

            }
            tv.setText(style);
        }
        getBubbleView(viewHolder,false).addView(tv);
        showStatus(viewHolder);
    }

    /**
     * 获取消息摘要
     */
    @Override
    public String getSummary() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i<message.getElementCount(); ++i){
            switch (message.getElement(i).getType()){
                case Face:
                    TIMFaceElem faceElem = (TIMFaceElem) message.getElement(i);
                    byte[] data = faceElem.getData();
                    if (data != null){
                        result.append(new String(data, Charset.forName("UTF-8")));
                    }
                    break;
                case Text:
                    TIMTextElem textElem = (TIMTextElem) message.getElement(i);
                    result.append(textElem.getText());
                    break;
            }

        }
        return result.toString();
    }

    /**
     * 保存消息或消息文件
     */
    @Override
    public void save() {

    }

    private int getNumLength(int n){
        return String.valueOf(n).length();
    }

    private class MyURLSpan extends ClickableSpan {

        private String url;
        private Context context;

        public MyURLSpan(String url,Context context) {
            this.url = url;
            this.context = context;
        }

        @Override
        public void onClick(View arg0) {
            Intent intent = new Intent(context,WebViewActivity.class);
            intent.putExtra(WebViewActivity.URL,url);
            ((Activity) context).startActivity(intent);
        }

    }

}
