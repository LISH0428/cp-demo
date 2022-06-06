package com.example.cpdemo.util;


import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;

public class HttpConnect {

    public static String getStringResponse(String url)  {
        //1.生成HttpClient对象并设置参数
        HttpClient httpClient = new HttpClient();

        //设置Http连接超时为5秒
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
        //2.生成GetMethod对象并设置参数
        GetMethod getMethod = new GetMethod(url);
//        getMethod.getParams().setParameter("http.protocol.cookie-policy", CookiePolicy.BROWSER_COMPATIBILITY);
          //getMethod.addRequestHeader("cookie", "buvid3=7BFE2D9F-1390-428D-BE4C-4FF9AE13F943148792infoc; rpdid=|(J~J)RY)Y|l0J'uYkYuRJY||; _uuid=EAD9F763-7991-7B03-4C1C-11974BA7046D28089infoc; buvid_fp_plain=7BFE2D9F-1390-428D-BE4C-4FF9AE13F943148792infoc; LIVE_BUVID=AUTO3016289524396836; fingerprint_s=fa4b56d1b9a8d4466762caa71de3f284; video_page_version=v_old_home; i-wanna-go-back=-1; b_ut=5; SESSDATA=0e601da0%2C1657266240%2C02895%2A11; bili_jct=1b06c5fc07a86a748d9f3b1347733ad1; DedeUserID=14401305; DedeUserID__ckMd5=9d84efcecd7918b7; sid=4hj9bsi9; buvid4=510A2867-0575-96CB-632E-A28B274711ED00957-022012016-f4+T5hcUEwRzowtTYg9BKA%3D%3D; buvid_fp=a8fd93191bf2443f9d64b7e5500c6565; blackside_state=1; CURRENT_BLACKGAP=0; fingerprint=469a2f16da627df46b2d79840844a977; fingerprint3=80cf7f4d9fdeaa7788678c592639c90d; bp_t_offset_14401305=642243120041295881; nostalgia_conf=-1; hit-dyn-v2=1; b_lsid=C93E510210_1800403D8E0; bp_video_offset_14401305=646376283029110800; innersign=1; CURRENT_FNVAL=4048; PVID=6; CURRENT_QUALITY=116");

        getMethod.addRequestHeader("referer","https://new.qq.com/");
        //getMethod.addRequestHeader("referer","https://military.china.com/");
        getMethod.addRequestHeader("sec-ch-ua","\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"100\", \"Microsoft Edge\";v=\"100\"");
        getMethod.addRequestHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/100.0.4896.60 Safari/537.36 Edg/100.0.1185.29");

        //设置get请求超时为5秒
        getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 5000);
        //设置请求重试处理，用的是默认的重试处理：请求三次
        getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
        String response = "";
        //3.执行HTTP GET 请求
        try {
            int statusCode = httpClient.executeMethod(getMethod);
            //4.判断访问的状态码
            if (statusCode != HttpStatus.SC_OK) {
                System.err.println("请求出错：" + getMethod.getStatusLine());
            }
            //5.处理HTTP响应内容
            //HTTP响应头部信息，这里简单打印
            //Header[] headers = getMethod.getResponseHeaders();
            //for(Header h : headers) {
            //    System.out.println(h.getName() + "---------------" + h.getValue());
            //}
            //读取HTTP响应内容，这里简单打印网页内容
            //读取为字节数组
            byte[] responseBody = getMethod.getResponseBody();
            response = new String(responseBody, StandardCharsets.UTF_8);
            //System.out.println("-----------response:" + response);
            //读取为InputStream，在网页内容数据量大时候推荐使用
            //InputStream response = getMethod.getResponseBodyAsStream();
        } catch (HttpException e) {
            //发生致命的异常，可能是协议不对或者返回的内容有问题
            System.out.println("请检查输入的URL!");
            e.printStackTrace();
        } catch (IOException e) {
            //发生网络异常
            System.out.println("发生网络异常!");
        } finally {
            //6.释放连接
            getMethod.releaseConnection();
        }
        return response;


    }
    public static String downloadByStream(String url) throws FileNotFoundException {
        //String path="E:\\bili\\"+cid;
        Long random=System.currentTimeMillis()%10000L;
        String path="E:\\dod\\"+random.toString();
        File flv=new File(path);
        OutputStream os=new FileOutputStream(flv);

        //1.生成HttpClient对象并设置参数
        HttpClient httpClient = new HttpClient();
        //设置Http连接超时为5秒
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
        //2.生成GetMethod对象并设置参数
        GetMethod getMethod = new GetMethod(url);
        getMethod.addRequestHeader("referer","https://new.qq.com/");
        getMethod.addRequestHeader("sec-ch-ua","\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"100\", \"Microsoft Edge\";v=\"100\"");
        getMethod.addRequestHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/100.0.4896.60 Safari/537.36 Edg/100.0.1185.29");

        //设置get请求超时为5秒
        getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 5000);
        //设置请求重试处理，用的是默认的重试处理：请求三次
        getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
        //3.执行HTTP GET 请求

        try {
            int statusCode = httpClient.executeMethod(getMethod);
            //4.判断访问的状态码
            if (statusCode != HttpStatus.SC_OK) {
                System.err.println("请求出错：" + getMethod.getStatusLine());
            }
            //5.处理HTTP响应内容
            //HTTP响应头部信息，这里简单打印
            Header[] headers = getMethod.getResponseHeaders();
            long fileLength=1L;
            for(Header h : headers) {
               // System.out.println(h.getName() + "---------------" + h.getValue());
                if("Content-Length".equals(h.getName())){
                    fileLength= Long.parseLong(h.getValue());
                    System.out.println("文件大小: "+ fileLength/1024/1024+"Mb");

                }
            }
            //读取HTTP响应内容
            //读取为InputStream，在网页内容数据量大时候推荐使用
            InputStream response = getMethod.getResponseBodyAsStream();
            BufferedInputStream is = new BufferedInputStream( response );
            byte[] buffer = new byte[8192];//1024*8
            int i;
            int curr=0;
            int flag=0;
            while ((i = is.read(buffer)) != -1) {
                curr+=i;
                flag+=1;
                if(flag%40==0){
                    DecimalFormat df = new DecimalFormat("00.#");
                    String rate=df.format((double) curr/fileLength*100);
                    System.out.println("下载进度： "+ rate+"%");
                }

                os.write(buffer,0,i);
            }
            System.out.println("文件下载完成");
            os.close();
            is.close();
        } catch (HttpException e) {
            //发生致命的异常，可能是协议不对或者返回的内容有问题
            System.out.println("请检查输入的URL!");
            e.printStackTrace();
        } catch (IOException e) {
            //发生网络异常
            System.out.println("发生网络异常!");
            return random.toString()+"发生网络异常!重新点击下载";
        } finally {
            //6.释放连接
            getMethod.releaseConnection();
        }


        return path;
    }
}
