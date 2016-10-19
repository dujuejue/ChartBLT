package com.example.administrator.chartblt;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * 　　　　　　　　┏┓　　　┏┓
 * 　　　　　　　┏┛┻━━━┛┻┓
 * 　　　　　　　┃　　　　　　　┃
 * 　　　　　　　┃　　　━　　　┃
 * 　　　　　　 ████━████     ┃
 * 　　　　　　　┃　　　　　　　┃
 * 　　　　　　　┃　　　┻　　　┃
 * 　　　　　　　┃　　　　　　　┃
 * 　　　　　　　┗━┓　　　┏━┛
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　 　 ┗━━━┓
 * 　　　　　　　　　┃ 神兽保佑　　 ┣┓
 * 　　　　　　　　　┃ 代码无BUG   ┏┛
 * 　　　　　　　　　┗┓┓┏━┳┓┏┛
 * 　　　　　　　　　　┃┫┫　┃┫┫
 * 　　　　　　　　　　┗┻┛　┗┻┛
 * Created by dutingjue on 2016/9/21.
 */
public class CombineChart extends View {
    private List<BarChartBean> barChartBeanList = new ArrayList<BarChartBean>();
    private Paint barPaint, axisPaint, textPaint, pointPaint, linePaint, itemPaint, lrPaint;
    private int screenW, screenH;
    private int leftMargin, rightMargin, topMargin, bottomMargin;
    private int barWidth,preBarWidth, xStart, yStart;
    private static final int BG_color = Color.parseColor("#EEEEEE");
    private int maxValueInItem, maxHeight;
    private Rect barRect, leftBar, rightBar;
    private int sum;
    private List<Integer> left = new ArrayList<Integer>();
    private List<Integer> right = new ArrayList<Integer>();
    private List<Integer> rightY = new ArrayList<Integer>();
    private Path path;
    private int yRightItem;

    private GestureDetector mGestureListener;
    private float lastPointX;
    private float leftMoving = 0.0f;
    private float movingThisTime = 0.0f;
    private int maxRight, minRight;


    private float zoomScale = 1.0f,preZoomScale=1.0f;
    private float preDistance = 0, distance = 0;
    private int mode = 0;
    private int mid = 0,midI=0;


    public CombineChart(Context context) {
        super(context);

    }

    public CombineChart(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        setData();
        init();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        left.clear();
        right.clear();
        rightY.clear();
        canvas.drawColor(BG_color);
        if (barChartBeanList.size()==0){
            return;
        }

        //设置barWidth,maxRight和minRight
        barWidth = (int) ((screenW - rightMargin - leftMargin) / barChartBeanList.size() * zoomScale);
        minRight = screenW - rightMargin;
        if (zoomScale <= 1) {
            maxRight = minRight;
        } else {
            maxRight = (int) (barWidth * barChartBeanList.size() + leftMargin);
        }
        checkZoomScale();
        checkTheLeftMoving();

        //设置测试数据

        //setData();
        //画柱状图和折线图


        path.reset();
        path.incReserve(barChartBeanList.size());
        drawBar(canvas);
        linePaint.setStrokeWidth(2);
        linePaint.setColor(Color.parseColor("#FFA500"));
        linePaint.setAntiAlias(true);
        linePaint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(path, linePaint);

        for (int i = 0; i < barChartBeanList.size(); i++) {
            canvas.drawCircle(right.get(i), rightY.get(i), 10, pointPaint);
        }


        //四周空白
        leftBar.bottom = screenH;
        leftBar.right = leftMargin;
        lrPaint.setColor(BG_color);
        canvas.drawRect(leftBar, lrPaint);
        rightBar.left = screenW - rightMargin;
        rightBar.right = screenW;
        rightBar.bottom = screenH;
        canvas.drawRect(rightBar, lrPaint);

        //坐标轴
        axisPaint.setStrokeWidth(3);
        axisPaint.setColor(Color.BLACK);
        //左Y轴以及箭头
        canvas.drawLine(leftMargin, screenH - bottomMargin * 4, leftMargin, topMargin, axisPaint);
        canvas.drawLine(leftMargin - ScreenUtils.dp2px(getContext(), 5),
                topMargin + ScreenUtils.dp2px(getContext(), 5),
                leftMargin,
                topMargin, axisPaint);
        canvas.drawLine(leftMargin + ScreenUtils.dp2px(getContext(), 5),
                topMargin + ScreenUtils.dp2px(getContext(), 5),
                leftMargin,
                topMargin, axisPaint);
        //X轴
        canvas.drawLine(leftMargin, screenH - bottomMargin * 4, screenW - rightMargin, screenH - bottomMargin * 4, axisPaint);
        //画右Y轴
        canvas.drawLine(screenW - rightMargin, screenH - bottomMargin * 4, screenW - rightMargin, topMargin, axisPaint);
        canvas.drawLine(screenW - rightMargin - ScreenUtils.dp2px(getContext(), 5),
                topMargin + ScreenUtils.dp2px(getContext(), 5),
                screenW - rightMargin,
                topMargin, axisPaint);
        canvas.drawLine(screenW - rightMargin + ScreenUtils.dp2px(getContext(), 5),
                topMargin + ScreenUtils.dp2px(getContext(), 5),
                screenW - rightMargin,
                topMargin, axisPaint);


        //画坐标刻度值
        drawYText(canvas);


    }

    private void init() {

        barPaint = new Paint();
        itemPaint = new Paint();
        axisPaint = new Paint();
        textPaint = new Paint();
        lrPaint = new Paint();
        textPaint.setAntiAlias(true);
        pointPaint = new Paint();
        pointPaint.setColor(Color.parseColor("#FF6347"));
        pointPaint.setStyle(Paint.Style.FILL);
        linePaint = new Paint();
        barRect = new Rect(0, 0, 0, 0);
        leftBar = new Rect(0, 0, 0, 0);
        rightBar = new Rect(0, 0, 0, 0);
        path = new Path();
        screenH = ScreenUtils.getScreenH(getContext());
        screenW = ScreenUtils.getScreenW(getContext());
        leftMargin = ScreenUtils.dp2px(getContext(), 20);
        rightMargin = ScreenUtils.dp2px(getContext(), 20);
        topMargin = ScreenUtils.dp2px(getContext(), 20);
        bottomMargin = ScreenUtils.dp2px(getContext(), 30);
        maxHeight = (screenH - bottomMargin * 4) - topMargin;
    }

    private void setData() {
        if (barChartBeanList.size() == 0) {
            maxValueInItem = 120;
            barChartBeanList.add(new BarChartBean("111", 120, Color.parseColor("#6FC5F4")));
            barChartBeanList.add(new BarChartBean("222", 100, Color.parseColor("#78DA9F")));
            barChartBeanList.add(new BarChartBean("333", 90, Color.parseColor("#FCAE84")));
            barChartBeanList.add(new BarChartBean("444", 70, Color.parseColor("#FF69B4")));
            barChartBeanList.add(new BarChartBean("555", 40, Color.parseColor("#CD853F")));
            barChartBeanList.add(new BarChartBean("666", 40, Color.parseColor("#FFF0F5")));
            barChartBeanList.add(new BarChartBean("777", 40, Color.parseColor("#FFE4C4")));
            barChartBeanList.add(new BarChartBean("888", 40, Color.parseColor("#FF6347")));
            barChartBeanList.add(new BarChartBean("999", 50, Color.RED));
            barChartBeanList.add(new BarChartBean("111", 70, Color.BLUE));
            for (BarChartBean b : barChartBeanList) {
                sum += b.getyNum();
            }

        }
    }

    private void drawBar(Canvas canvas) {
        xStart = leftMargin;
        yStart = screenH - bottomMargin * 4;
        float sumCurrent = 0;
        leftMoving=leftMoving+midI*barWidth*(zoomScale-preZoomScale);
        checkZoomScale();
        for (int i = 0; i < barChartBeanList.size(); i++) {
            barRect.left = (int) (xStart + i * barWidth - leftMoving );
            barRect.bottom = screenH - bottomMargin * 4;
            barRect.right = barRect.left + barWidth;
            barRect.top = (int) (barRect.bottom - (barChartBeanList.get(i).getyNum() / maxValueInItem) * maxHeight / 2 * zoomScale + 0.5f);
            left.add(barRect.left);
            right.add(barRect.right);
            barPaint.setColor(barChartBeanList.get(i).getBarColor());
            canvas.drawRect(barRect, barPaint);
            //画X轴text
            String name = barChartBeanList.get(i).getBarName();
            textPaint.setTextSize(ScreenUtils.dp2px(getContext(), 10));
            canvas.drawText(name,
                    barRect.left + (barWidth - textPaint.measureText(name)) / 2,
                    barRect.bottom + ScreenUtils.dp2px(getContext(), 10), textPaint);

            sumCurrent += barChartBeanList.get(i).getyNum();

            int yCycle = (int) (sumCurrent / sum * maxHeight);

            if (i == 0) {
                path.moveTo(leftMargin - leftMoving, screenH - bottomMargin * 4);
                path.lineTo(right.get(i), screenH - bottomMargin * 4 - yCycle);
                rightY.add(screenH - bottomMargin * 4 - yCycle);
            } else {
                path.lineTo(right.get(i), screenH - bottomMargin * 4 - yCycle);
                rightY.add(screenH - bottomMargin * 4 - yCycle);
            }

        }

    }


    private void drawYText(Canvas canvas) {
        //画Y右轴
        yRightItem = (int) (maxHeight / 10 + 0.5f);
        itemPaint.setColor(Color.BLACK);
        itemPaint.setAntiAlias(true);
        itemPaint.setTextSize(ScreenUtils.dp2px(getContext(), 8));
        itemPaint.setStrokeWidth(2);
        Paint.FontMetricsInt fontMetricsInt = itemPaint.getFontMetricsInt();
        for (int i = 1; i < 10; i++) {
            canvas.drawLine(screenW - rightMargin, screenH - bottomMargin * 4 - yRightItem * i
                    , screenW - rightMargin - ScreenUtils.dp2px(getContext(), 5), screenH - bottomMargin * 4 - yRightItem * i,
                    axisPaint);
            canvas.drawText(10 * i + "%",
                    screenW - rightMargin + ScreenUtils.dp2px(getContext(), 2),
                    screenH - bottomMargin * 4 - yRightItem * i
                            - (fontMetricsInt.bottom - fontMetricsInt.top) / 2 - fontMetricsInt.top,
                    itemPaint);
        }
        canvas.drawText("100%", screenW - rightMargin + ScreenUtils.dp2px(getContext(), 2),
                screenH - bottomMargin * 4 - maxHeight
                        - (fontMetricsInt.bottom - fontMetricsInt.top) / 2 - fontMetricsInt.top,
                itemPaint);


    }

    //手势识别响应事件
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                lastPointX = event.getRawX();
                break;
            case MotionEvent.ACTION_MOVE:
                if (mode == 2) {
                    distance = getDistance(event);
                    preZoomScale=zoomScale;
                    zoomScale = zoomScale * (distance / preDistance);
                    checkZoomScale();
                    midI=checkWhere(mid);
                    preDistance = distance;
                } else {
                    float moveX = event.getRawX();
                    movingThisTime = lastPointX - moveX;
                    leftMoving += movingThisTime;
                    checkTheLeftMoving();
                    lastPointX = moveX;
                }
                invalidate();
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                preDistance = getDistance(event);
                if (preDistance > 20f) {
                    mode = 2;
                    mid = (int) getMid(event);
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
                mode = 0;
                break;

        }
        return true;
    }

    /**
     * 检查向左滑动的距离 确保没有画出屏幕
     */
    private void checkTheLeftMoving() {
        if (leftMoving < 0) {
            leftMoving = 0;
        }
        if (maxRight == minRight) {
            leftMoving = 0;
        } else if (leftMoving > (maxRight - minRight) && maxRight > minRight) {
            leftMoving = maxRight - minRight;
        }
    }

    /*获取两指之间的距离*/
    private float getDistance(MotionEvent event) {
        float x = event.getX(1) - event.getX(0);
        float y = event.getY(1) - event.getY(0);
        float distance = (float) Math.sqrt(x * x + y * y);//两点间的距离
        return distance;
    }

    /*获取两指中心点X坐标*/
    private float getMid(MotionEvent event) {
        float x = event.getX(1) - (event.getX(1) - event.getX(0)) / 2 - leftMargin;
        return x;
    }

    /*限制缩放比例*/
    private void checkZoomScale() {
        if (zoomScale > 2) {
            zoomScale = 2.0f;
        } else if (zoomScale < 1) {
            zoomScale = 1.0f;
        }
    }

    /*查询缩放位置*/
    private int checkWhere(int mid) {
        for (int i = 0; i < right.size(); i++) {
            if (mid>=left.get(i)&&mid<=right.get(i)){
                return i;
            }
        }
        return -1;
    }

    /*从activity设置数据*/
    public void setItemData(List<BarChartBean> barChartBeanList){
        this.barChartBeanList=barChartBeanList;
        float numMax=0;
        for (BarChartBean b : barChartBeanList) {
            sum += b.getyNum();
            if (b.getyNum()>numMax){
                numMax=b.getyNum();
            }
        }
        maxValueInItem = (int) numMax;
        invalidate();
    }

}
