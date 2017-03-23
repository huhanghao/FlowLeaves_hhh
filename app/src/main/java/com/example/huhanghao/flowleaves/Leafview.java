package com.example.huhanghao.flowleaves;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by huhanghao on 2017/3/23.
 */

public class LeafView extends View {

    private Resources mResources = getResources();
    private final Paint bgPaint;
    private int height;
    private int width;
    private RectF bgRect;
    private final Bitmap bgBitmap;
    private Rect bgDestRect;
    private final Bitmap leafBitmap;
    private final int mLeafHeight;
    long cycleTime = 5000;   //叶子滑动一周的时间5秒
    long startTime = 0;      //叶子滑动开始时间

    public LeafView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // 设置黄色背景画笔
        bgPaint = new Paint();
        bgPaint.setColor(mResources.getColor(R.color.bg_color, null));
        // 获取背景图片和叶子图片
        bgBitmap = ((BitmapDrawable) mResources.getDrawable(R.drawable.leaf_kuang, null)).getBitmap();
        leafBitmap = ((BitmapDrawable) mResources.getDrawable(R.drawable.leaf, null)).getBitmap();
        mLeafHeight = leafBitmap.getWidth();

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
        bgDestRect = new Rect(0, 0, width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        bgRect = new RectF(0, 0, width, height);
        // 添加黄色背景
        canvas.drawRect(bgRect, bgPaint);
        // 添加背景图片
        canvas.drawBitmap(bgBitmap, null, bgDestRect, null);
        // 添加叶子,因为叶子在后期要做旋转和位移变化，所以需要将他花在画布上
        Matrix matrix = new Matrix();
        matrix.postTranslate(getMatriX(), (height - mLeafHeight) / 2);
        canvas.drawBitmap(leafBitmap, matrix, new Paint());

        // 重复调用ondraw
        postInvalidate();
    }

    private float getMatriX(){
        float betweenTime = startTime - System.currentTimeMillis();
        // 周期结束加一个cycleTime
        if(betweenTime < 0){
            startTime = System.currentTimeMillis() + cycleTime;
            betweenTime = cycleTime;
        }

        // 通过时间差计算出叶子的坐标
        float fraction = (float) betweenTime / cycleTime;
        float x = (int)(width * fraction);
        return x;
    }
}
