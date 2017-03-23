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

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import static com.example.huhanghao.flowleaves.R.drawable.leaf;

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
    long startTime = 0;      //叶子滑动开始时间
    int addTime;
    //存放叶子lsit
    private List<Leaf> leafList;
    //叶子的宽和高
    private int mLeafWidth, mLeafHeight;
    //叶子滑动一周的时间5秒
    private final static long cycleTime = 5000;
    //叶子数量
    private final static int leafNumber = 5;

    public LeafView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // 设置黄色背景画笔
        bgPaint = new Paint();
        bgPaint.setColor(mResources.getColor(R.color.bg_color, null));
        // 获取背景图片和叶子图片
        bgBitmap = ((BitmapDrawable) mResources.getDrawable(R.drawable.leaf_kuang, null)).getBitmap();
        leafBitmap = ((BitmapDrawable) mResources.getDrawable(leaf, null)).getBitmap();
        mLeafHeight = leafBitmap.getHeight();
        mLeafWidth = leafBitmap.getWidth();

        //获取所有叶子的信息，放入list
        leafList = getLeaves(leafNumber);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = getMeasuredWidth();
        height = getMeasuredHeight();
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

        // 画叶子
        int size = leafList.size();
        for (int i = 0; i < size; i++) {
            Leaf leaf = leafList.get(i);
            // 获取叶子坐标
            getRotate(leaf);
            // 获取叶子旋转角度
            getLocation(leaf);

            // 添加叶子,因为叶子在后期要做旋转和位移变化，所以需要将他画在画布上
            canvas.save();
            Matrix matrix = new Matrix();
            // 设置滑动
            matrix.postTranslate(leaf.x, leaf.y);
            // 设置旋转
            matrix.postRotate(leaf.rotateAngle, leaf.x + mLeafWidth / 2, leaf.y + mLeafHeight / 2);
            // 添加叶子到画布
            canvas.drawBitmap(leafBitmap, matrix, new Paint());
            canvas.restore();

        }


        // 重复调用ondraw
        postInvalidate();
    }

    /**
     * 获取matrix矩阵的X位置
     *
     * @return
     */
    private float getMatrixX() {
        float betweenTime = startTime - System.currentTimeMillis();
        // 周期结束加一个cycleTime
        if (betweenTime < 0) {
            startTime = System.currentTimeMillis() + cycleTime;
            betweenTime = cycleTime;
        }

        // 通过时间差计算出叶子的坐标
        float fraction = (float) betweenTime / cycleTime;
        float x = (int) (width * fraction);
        return x;
    }

    /**
     * 获取叶子的Y的位置
     *
     * @return
     */
    private float getMatrixY() {
        float w = (float) ((float) 2 * Math.PI / width);
        int y = (int) ((height / 4) * Math.sin(w * getMatrixX())) + (height - mLeafHeight) / 2;
        return y;
    }

    private void getRotate(Leaf leaf) {
        float scale = ((leaf.startTime - System.currentTimeMillis()) % cycleTime) / (float) (cycleTime);
        int rotate = (int) (scale * 360);
        leaf.rotateAngle = rotate;
    }

    private void getLocation(Leaf leaf) {
        float betweenTime = leaf.startTime - System.currentTimeMillis();
        // 周期结束加一个cycleTime
        if (betweenTime < 0) {
            leaf.startTime = System.currentTimeMillis() + cycleTime;
            betweenTime = cycleTime;
        }

        // 通过时间差计算出叶子的坐标
        float fraction = (float) betweenTime / cycleTime;
        float x = (int) (width * fraction);
        leaf.x = x;

        float w = (float) ((float) 2 * Math.PI / width);
        int y = (int) ((height / 4) * Math.sin(w * getMatrixX())) + (height - mLeafHeight) / 2;
        leaf.y = y;

    }


    private Leaf getLeaf() {
        Random random = new Random();
        Leaf leaf = new Leaf();
        // 随机初始化叶子初始化角度
        leaf.rotateAngle = random.nextInt();

        // 随机初始化叶子启动时间
        addTime += random.nextInt((int) (cycleTime));
        leaf.startTime = System.currentTimeMillis() + startTime + addTime;
        return leaf;
    }

    private List<Leaf> getLeaves(int leafSize) {
        List<Leaf> list = new LinkedList<>();
        for (int i = 0; i < leafSize; i++) {
            list.add(getLeaf());
        }
        return list;
    }

}
