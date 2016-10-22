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
    static final String SERVICE_URL = "http://192.168.30.41/Webservice1.asmx";
    //调用柏拉图方法
    static final String methodNamePlato = "FailData";
    //调用的柏拉图soapaction
    static final String SOAPACTION="http://mestest.org/FailData";
    //查询投入产出方法
    static final String methodNameInOut="In_Out";
    //调用的投入产出soapaction
    static final String SOAPACTIONInOut="http://mestest.org/In_Out";
    /*
    * 获取柏拉图数据
    * */
    public static int getPLATOData(List<String>name,List<String>count,String startTime,String endTime){

        //创建HttpTransportSE
        HttpTransportSE ht=new HttpTransportSE(SERVICE_URL);
        ht.debug=true;
        //使用SOAP1.2协议创建Envelop对象
        SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER12);
        //设置与.Net提供的Web Service保持较好的兼容性
        envelope.dotNet=true;
        //实例化SoapObject对象
        SoapObject soapObject=new SoapObject(SERVICE_NS,methodNamePlato);
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

    /*
    * 获取投入产出数据
    * */
    public static int getInOutData(List<Integer> in,List<Integer>out,String startTime,String endTime){

        //创建HttpTransportSE
        HttpTransportSE ht=new HttpTransportSE(SERVICE_URL);
        ht.debug=true;
        //使用SOAP1.2协议创建Envelop对象
        SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER12);
        //设置与.Net提供的Web Service保持较好的兼容性
        envelope.dotNet=true;
        //实例化SoapObject对象
        SoapObject soapObject=new SoapObject(SERVICE_NS,methodNameInOut);
        soapObject.addProperty("StartTime",startTime);
        soapObject.addProperty("EndTime",endTime);
        envelope.bodyOut=soapObject;

        try {
            //调用Web Service

            ht.call(SOAPACTIONInOut,envelope);
            if (envelope.getResponse()!=null){
                //获取服务器响应的soap消息
                SoapObject result= (SoapObject) envelope.bodyIn;
                for (int i=0;i<result.getPropertyCount();i++){
                    Log.i("web",result.getProperty(i).toString());
                    String data=result.getProperty(i).toString();
                    if (data!=null&&!data.equals("anyType{}")){
                        if (data.equals("-1"))return dataFalse;
                        String[]nameCount=data.split(";");
                        int inData= Integer.parseInt(nameCount[0].split(",")[1]);
                        in.add(inData);
                        int outData= Integer.parseInt(nameCount[1].split(",")[1]);
                        out.add(outData);
                    }else return dataNull;
                }
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
