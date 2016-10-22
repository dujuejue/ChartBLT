package com.example.administrator.chartblt;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
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
 * Created by dutingjue on 2016/10/21.
 */
public class Bargraph extends View {
    /*
    * 画笔
    * */
    private Paint barTopPaint,barBottomPaint, axisPaint, textPaint;
    /*
    * 背景色
    * */
    private static final int BG_color = Color.parseColor("#EEEEEE");
    /*
    * bar的宽度，屏幕数据，图标边框，数据值,间隔
    * */
    private int barWidth,spaceWidth;
    private Rect barRectBottom, barRectTop,rectDescription;
    private int screenW, screenH;
    private int leftMargin, rightMargin, topMargin, bottomMargin;
    private int maxValueInItem, maxHeight, sum=0;
    /*
    * 数据
    * */
    private List<Inputoutput> inputoutputList = new ArrayList<Inputoutput>();

    public Bargraph(Context context) {
        super(context);
    }

    public Bargraph(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(BG_color);
        if (inputoutputList.size() == 0) {
            return;
        }
        barWidth = (screenW - leftMargin - rightMargin) / (inputoutputList.size()*2+1);
        drawBar(canvas);
        drawAxis(canvas);
        drawDescription(canvas);
    }

    /*
    * bar绘制
    * */
    private void drawBar(Canvas canvas)  {
        int xStart = leftMargin;
        int yStar = screenH - bottomMargin;
        barBottomPaint.setColor(Color.parseColor("#00BFFF"));
        barTopPaint.setColor(Color.parseColor("#00CDCD"));
        for (int i = 0; i < inputoutputList.size(); i++) {
            //设置output桩柱图
            barRectBottom.left = xStart + i * barWidth*2+barWidth;
            barRectBottom.right = barRectBottom.left + barWidth;
            barRectBottom.bottom=yStar;
            double out=inputoutputList.get(i).getOutput();
            barRectBottom.top= (int) (yStar-(out/maxValueInItem*maxHeight));
            //设置input柱状图
            barRectTop.left = barRectBottom.left;
            barRectTop.right =  barRectBottom.right;
            barRectTop.bottom=barRectBottom.top;
            double in=inputoutputList.get(i).getInput();
            barRectTop.top= (int) (yStar-(in/maxValueInItem*maxHeight));
            //画柱状图
            canvas.drawRect(barRectBottom,barBottomPaint);
            canvas.drawRect(barRectTop,barTopPaint);
        }
    }
    /*
    * axis绘制
    * */
    private  void drawAxis(Canvas canvas){

        //坐标轴
        axisPaint.setStrokeWidth(3);
        axisPaint.setColor(Color.BLACK);
        //左Y轴以及箭头
        canvas.drawLine(leftMargin, screenH - bottomMargin , leftMargin, topMargin, axisPaint);
        canvas.drawLine(leftMargin - ScreenUtils.dp2px(getContext(), 5),
                topMargin + ScreenUtils.dp2px(getContext(), 5),
                leftMargin,
                topMargin, axisPaint);
        canvas.drawLine(leftMargin + ScreenUtils.dp2px(getContext(), 5),
                topMargin + ScreenUtils.dp2px(getContext(), 5),
                leftMargin,
                topMargin, axisPaint);
        //X轴以及箭头
        canvas.drawLine(leftMargin, screenH - bottomMargin, screenW - rightMargin, screenH - bottomMargin, axisPaint);
        canvas.drawLine(screenW - rightMargin, screenH - bottomMargin,
                screenW - rightMargin-ScreenUtils.dp2px(getContext(), 5),
                screenH - bottomMargin-ScreenUtils.dp2px(getContext(), 5),axisPaint);
        canvas.drawLine(screenW - rightMargin, screenH - bottomMargin,
                screenW - rightMargin-ScreenUtils.dp2px(getContext(), 5),
                screenH - bottomMargin+ScreenUtils.dp2px(getContext(), 5),axisPaint);
    }
    /*
    * description绘制
    * */
    private void drawDescription(Canvas canvas){
        int area=ScreenUtils.dp2px(getContext(),10);
        rectDescription.left=leftMargin;
        rectDescription.right=leftMargin+area;
        //设置投入的描述块属性
        rectDescription.top=screenH - bottomMargin+ScreenUtils.dp2px(getContext(),20);
        rectDescription.bottom=rectDescription.top+area;
        canvas.drawText("投入",rectDescription.right+ScreenUtils.dp2px(getContext(),2),
                rectDescription.bottom-ScreenUtils.dp2px(getContext(),2),textPaint);
        canvas.drawRect(rectDescription,barTopPaint);
        //设置产出的描述块属性
        rectDescription.top=rectDescription.bottom+ScreenUtils.dp2px(getContext(),5);
        rectDescription.bottom=rectDescription.top+area;
        canvas.drawText("产出",rectDescription.right+ScreenUtils.dp2px(getContext(),2),
                rectDescription.bottom-ScreenUtils.dp2px(getContext(),2),textPaint);
        canvas.drawRect(rectDescription,barBottomPaint);
    }
    /*
    * 初始化属性
    * */
    private void init() {
        barTopPaint=new Paint();
        barBottomPaint=new Paint();
        axisPaint = new Paint();
        textPaint = new Paint();
        barRectBottom = new Rect(0, 0, 0, 0);
        barRectTop = new Rect(0, 0, 0, 0);
        rectDescription=new Rect(0,0,0,0);
        screenH = ScreenUtils.getScreenH(getContext());
        screenW = ScreenUtils.getScreenW(getContext());
        spaceWidth=ScreenUtils.dp2px(getContext(),20);
        leftMargin = ScreenUtils.dp2px(getContext(), 20);
        rightMargin = ScreenUtils.dp2px(getContext(), 20);
        topMargin = ScreenUtils.dp2px(getContext(), 20);
        bottomMargin = ScreenUtils.dp2px(getContext(), 100);
        maxHeight=screenH-bottomMargin-topMargin;
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(ScreenUtils.dp2px(getContext(),10));
    }

    /*
    * 从activity设置数据
    * */
    public void setItemData(List<Inputoutput> inputoutputList) {
        this.inputoutputList = inputoutputList;
        int numMax = 0;
        for (Inputoutput b : inputoutputList) {
            sum += b.getInput();
            if (b.getInput() > numMax) {
                numMax = b.getInput();
            }
        }
        maxValueInItem=numMax;
        invalidate();
    }
}
