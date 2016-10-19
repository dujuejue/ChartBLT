package com.example.administrator.chartblt;

import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.net.Authenticator;
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
 * Created by dutingjue on 2016/10/10.
 */
public class WebUtils {
    //该请求没有数据
    static final int dataFalse=0;
    //请求格式错误
    static final int dataNull=1;
    //请求成功
    static final int dataTrue=2;
    //网络问题
    static final int netFalse=3;
    //WebService命名空间
    static final String SERVICE_NS = "http://mestest.org/";
    //WebService提供服务的URL
    static final String SERVICE_URL = "http://192.168.30.44/Webservice1.asmx";
    //调用方法
    static final String methodName = "HelloWorld2";
    //调用的soapaction
    static final String SOAPACTION="http://mestest.org/HelloWorld2";

    public static int getPLATOData(List<String>name,List<String>count,String startTime,String endTime){

        //创建HttpTransportSE
        HttpTransportSE ht=new HttpTransportSE(SERVICE_URL);
        ht.debug=true;
        //使用SOAP1.2协议创建Envelop对象
        SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER12);
        //设置与.Net提供的Web Service保持较好的兼容性
        envelope.dotNet=true;
        //实例化SoapObject对象
        SoapObject soapObject=new SoapObject(SERVICE_NS,methodName);
        soapObject.addProperty("StartTime",startTime);
        soapObject.addProperty("EndTime",endTime);
        envelope.bodyOut=soapObject;

        try {
            //调用Web Service

            ht.call(SOAPACTION,envelope);
            if (envelope.getResponse()!=null){
                //获取服务器响应的soap消息
                SoapObject result= (SoapObject) envelope.bodyIn;
                Log.i("web",result.getProperty(0).toString());
                String data=result.getProperty(0).toString();
                if (data!=null&&!data.equals("anyType{}")){
                    if (data.equals("-1"))return dataFalse;
                    String[]nameCount=data.split(";");
                    for (String a:nameCount){
                        name.add(a.split(",")[1]);
                        count.add(a.split(",")[0]);
                    }
                }else return dataNull;
                //解析服务器响应的soap消息
                return dataTrue;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        return  netFalse;
    }
}
