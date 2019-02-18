package com.kmwlyy.patient.kdoctor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

import com.kmwlyy.core.util.CommonUtils;
import com.kmwlyy.patient.R;
import com.kmwlyy.patient.kdoctor.activity.AiChatActivity2;


/**
 * @Description描述: 有参照这批博客https://www.oschina.net/code/snippet_54100_6262
 * @Author作者: liuyixin
 * @Date日期: 2016/12/21
 */
public class CartoonView extends View {
    private static final String TAG = "CartoonView";
    private int screenWidth;//屏幕宽高
    private int screenHeight;
    private Context context;

    private static int WIDTH = 165;//卡通图片的宽高165*250
    private static int HEIGHT = 250;//
    private Rect rect;
    private int deltaX, deltaY, dX, dY, uX, uY;//点击位置和图形边界的偏移量
    private Paint paint;//画笔
    private boolean isMoved = false;
    private int den = 0;
    private int marbottom = 180;
    private int martop = 0;
    private Bitmap mBitmap;

    public CartoonView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {
        DisplayMetrics dm = getResources().getDisplayMetrics();

        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
        den = dm.densityDpi;
        paint = new Paint();
        mBitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_cartoon);
        WIDTH = mBitmap.getScaledWidth(den);
        HEIGHT = mBitmap.getScaledHeight(den);
        Rect rect = new Rect();
        ((Activity)context).getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        int statusBar =  rect.top;
        martop = CommonUtils.dip2px(context, 210) - statusBar - HEIGHT;
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isMoved) {
                    context.startActivity(new Intent(context, AiChatActivity2.class));
                }
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (null == rect) {
//            rect = new Rect(screenWidth - WIDTH - 30, screenHeight - HEIGHT - marbottom, screenWidth - 30, screenHeight - marbottom);
//            rect = new Rect(screenWidth - WIDTH, screenHeight - HEIGHT - marbottom, screenWidth, screenHeight - marbottom);
            rect = new Rect(screenWidth - WIDTH, martop, screenWidth, martop + HEIGHT);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mBitmap, rect.left, rect.top, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!rect.contains(x, y)) {
                    return false;//没有在矩形上点击，不处理触摸消息
                }
                dX = (int) event.getX();
                dY = (int) event.getY();
                deltaX = x - rect.left;
                deltaY = y - rect.top;
                break;
            case MotionEvent.ACTION_MOVE:
                Rect old = new Rect(rect);
                //更新矩形的位置
                if ((x - deltaX) >= (screenWidth - WIDTH)) {//ConvertUtils.toDp(context, x) >= 315
                    rect.right = screenWidth;
                } else {
                    rect.right = (rect.left + WIDTH);
                }
                if ((y - deltaY) >= (screenHeight - marbottom - HEIGHT)) {//这是到底部的情况  ConvertUtils.toDp(context, y) >= 500
                    rect.top = screenHeight - HEIGHT - marbottom;
                    rect.bottom = (screenHeight - marbottom);
                } else {
                    rect.top = (y - deltaY) > 0 ? (y - deltaY) : 0;//拖动到顶部边界的时候
                    rect.bottom = rect.top + HEIGHT;
                }
                old.union(rect);//要刷新的区域，求新矩形区域与旧矩形区域的并集
                invalidate(old);//出于效率考虑，设定脏区域，只进行局部刷新，不是刷新整个view
                break;
            case MotionEvent.ACTION_UP:
                uX = (int) event.getX();
                uY = (int) event.getY();
                if (Math.abs(uY - dY) > 10) {
                    isMoved = true;
                } else {
                    isMoved = false;
                }
                break;
        }
        return super.onTouchEvent(event);//处理了触摸消息，消息不再传递
    }

}
