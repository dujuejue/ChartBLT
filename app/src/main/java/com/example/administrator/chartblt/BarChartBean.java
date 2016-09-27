package com.example.administrator.chartblt;

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
public class BarChartBean {
    private String barName;
    private float yNum;
    private int barColor;

    public BarChartBean(String barName,float yNum,int barColor){
        this.barName=barName;
        this.yNum=yNum;
        this.barColor=barColor;
    }
    public String getBarName() {
        return barName;
    }

    public float getyNum() {
        return yNum;
    }

    public int getBarColor() {

        return barColor;
    }

    public void setBarName(String barName) {
        this.barName = barName;
    }

    public void setyNum(float yNum) {
        this.yNum = yNum;
    }

    public void setBarColor(int barColor) {
        this.barColor = barColor;
    }
}
