package com.example.demo.proto.monitor;

import com.example.demo.utils.BytesUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class StreamReport {

    // curl 'https://a.upliveapp.com/service/profile/get'
    // -H 'Host: a.upliveapp.com'
    // -H 'Accept: application/x-protobuf'
    // -H 'feature: 0'
    // -H 'UserAgent: iOS/appstore version/8.2.5 os/13.6.1 model/iPhoneXR build/825177 feature/0
    // smd/2021033113180293887ca4306e96a06df70abbb7a85dfd01152a1b3dc968f0 cc/PH bundleid/inhouse.uplive.production
    // adj/d68720f1ee593102a76d0c3ddc8532fd appkey/uplive idfa/BA344DD0-1582-4BF7-965B-B050385F1867
    // idfv/B5616201-B2D7-42F3-8FDC-8F7E972D2701 timezone/GMT+08:00'
    // -H 'Accept-Encoding: gzip, deflate, br'  -H 'Accept-Language: zh-CN'  -H 'Content-Type: application/x-protobuf'
    // -H 'Charset: UTF-8'
    // -H 'Content-Length: 8'
    // -H 'AB: -1'
    // -H 'User-Agent: UpLive/825177 CFNetwork/1128.0.1 Darwin/19.6.0'
    // -H 'Connection: keep-alive'
    // -H 'tid: AA0C3F7E-C16A-4D8C-A545-47E436296212'
    // -H 'userToken: oWHO2NTGFi91a2bb2ZNJ1o'
    // -H 'Cookie: 96ba8adac6882fb0_gr_cs1=522693017; 96ba8adac6882fb0_gr_last_sent_cs1=522693017; gr_user_id=7b314ff8-9e93-4f33-87ef-092013a44817'
    private static final String baseUrl;

    //
    private static String userToken = "rsnX2NwCfsfbab42dZNZ6p";

    static {
        // curl -d '{"uid":1,"roomId":1,"userRoomHisId":1,"page":1}' -H 'Content-Type: application/json'https://monitor-stream.pengpengla.com/stream/api/event/list/get
        baseUrl = "http://127.0.0.1:12673";
    }

   /* public static ResultResponse.Result request(String requestUrl, byte[] requestBytes) throws IOException {
        String fullRequestUrl = baseUrl + requestUrl;
        System.out.println("request url = " + fullRequestUrl);
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(fullRequestUrl);
        InputStream inputStream = new ByteArrayInputStream(requestBytes);
        InputStreamEntity inputStreamEntity = new InputStreamEntity(inputStream, requestBytes.length);

        //这两行很重要的，是告诉springmvc客户端请求和响应的类型，指定application/x-protobuf类型,spring会用ProtobufHttpMessageConverter类来解析请求和响应的实体
        httpPost.addHeader("Content-Type", "application/x-protobuf");
        httpPost.addHeader("Accept", "application/x-protobuf");
        httpPost.addHeader("Accept-Language", "zh-CN");
        httpPost.addHeader("userToken", userToken);
        httpPost.addHeader("UserAgent", "iOS/appstore version/8.2.5 os/13.6.1 model/iPhoneXR build/825177 feature/0  smd/2021033113180293887ca4306e96a06df70abbb7a85dfd01152a1b3dc968f0");

        httpPost.setEntity(inputStreamEntity);

        try {
            HttpResponse response2 = httpClient.execute(httpPost);
            System.out.println(response2.getStatusLine());
            HttpEntity entity2 = response2.getEntity();
            ByteArrayOutputStream buf = new ByteArrayOutputStream();
            entity2.writeTo(buf);
            ResultResponse.Result result = ResultResponse.Result.parseFrom(buf.toByteArray());
            System.out.println("request result code = " + result.getCodeValue());
            if (result.hasData()) {
                System.out.println("result data = " + result.getData());
            }
            return result;
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
    }*/

    // stream/event/report
    public static void streamEventReport(){
        long l = System.currentTimeMillis();
        String path = "/stream/event/report";

      /*  List< MonitorStreamEventOther.OtherStreamData> singleDatas = new ArrayList<>();
        MonitorStreamEventOther.OtherStreamData.Builder singleData = MonitorStreamEventOther.OtherStreamData.newBuilder();
        singleData.setLiveModel(LiveModelOuterClass.LiveModel.LIVE);
        singleData.setPointTime(System.currentTimeMillis());
        singleData.setActionType(MonitorStreamActionTypeOuterClass.MonitorStreamActionType.APP_DESK);
        singleDatas.add(singleData.build());

        List<MonitorStreamEventOther.SingleOtherStreamData> singles = new ArrayList<>();
        MonitorStreamEventOther.SingleOtherStreamData.Builder single = MonitorStreamEventOther.SingleOtherStreamData.newBuilder();
        single.addAllOtherStreamDatas(singleDatas);
        single.setUserRoomHisId(1L);
        single.setRoomId(2L);
        single.setAnchorUid(3L);
        singles.add(single.build());

        MonitorStreamEventOther.Request.Builder byteData = MonitorStreamEventOther.Request.newBuilder();
        byteData.addAllOtherStreamDatas(singles);

        MonitorStreamEventReport.ReportData.Builder data = MonitorStreamEventReport.ReportData.newBuilder();
        data.setType(4);
        byte[] bytes = byteData.build().toByteArray();
        for (int i = 0; i < bytes.length; i++) {
            System.out.print(bytes[i]+",");
        }

        byte[] bytesNotEnpty = BytesUtils.addBytes(new byte[]{0}, bytes);
        System.err.println("-----------------");
        for (int i = 0; i < bytesNotEnpty.length; i++) {
            System.out.print(bytesNotEnpty[i]+",");
        }
        System.err.println("");
        ByteString byteString = ByteString.copyFrom(bytesNotEnpty);
        System.err.println(byteString);
        data.setByteDatas(byteString);

        List<MonitorStreamEventReport.ReportData> datas = new ArrayList<>();
        datas.add(data.build());
        MonitorStreamEventReport.Request.Builder builder = MonitorStreamEventReport.Request.newBuilder();
        builder.setIsSdkLogin(true);
        builder.setReportTime(System.currentTimeMillis());
        builder.addAllReportDatas(datas);
        builder.setUid(1L);

        ResultResponse.Result result = null;
        try {
            result = request(path, builder.build().toByteArray());
            if (result.getCode().equals(ResultResponse.Code.SC_SUCCESS)) {
                System.err.println("OK.");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/

    }

    public static void main(String[] args) {

        streamEventReport();

    }




}
