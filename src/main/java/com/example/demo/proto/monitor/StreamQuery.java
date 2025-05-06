package com.example.demo.proto.monitor;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class StreamQuery {

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
    public static void streamRealAll(){
        long l = System.currentTimeMillis();
        String path = "/stream/real/all";

        /*MonitorStreamRealAll.Request.Builder builder = MonitorStreamRealAll.Request.newBuilder();
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
        streamRealAll();
    }




}
