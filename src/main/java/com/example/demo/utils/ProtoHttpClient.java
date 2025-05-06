
package com.example.demo.utils;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class ProtoHttpClient {

    private static Logger log = LoggerFactory.getLogger(ProtoHttpClient.class);

    private static int CONNECT_TIMEOUT = 1000;
    private static int CONNECT_TIMEOUT_MIN = 100;

    public static boolean handlePostRequest(String reqUrl, byte[] proto, int connectTimeout, int retryCount) {
        boolean report = false;
        try {
            URL url = new URL(reqUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestProperty("Content-Type", "application/x-protobuf");
            connection.setRequestProperty("Accept", "application/x-protobuf");
            // 未设置超时时间，默认1s超时
            connectTimeout = connectTimeout > 0 ? connectTimeout : CONNECT_TIMEOUT;
            // 设置的超时时间小于CONNECT_TIMEOUT_MIN.设置为CONNECT_TIMEOUT_MIN
            connectTimeout = connectTimeout < CONNECT_TIMEOUT_MIN ? CONNECT_TIMEOUT_MIN : connectTimeout;
            connection.setConnectTimeout(connectTimeout);
            BufferedOutputStream out = new BufferedOutputStream(connection.getOutputStream());
            if (null != proto) {
                out.write(proto);
            }
            out.flush();
            out.close();
            report = connection.getResponseCode() == 200;
        } catch (IOException e) {
            log.error("prometheus handlePostRequest error.", e);
            if (retryCount > 0) {
                report = handlePostRequest(reqUrl, proto, connectTimeout, retryCount - 1);
            }
        }
        return report;
    }


    public static void request(String requestUrl, byte[] requestBytes) throws IOException {
        System.out.println("request url = " + requestUrl);
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(requestUrl);
        InputStream inputStream = new ByteArrayInputStream(requestBytes);
        InputStreamEntity inputStreamEntity = new InputStreamEntity(inputStream, requestBytes.length);

        //这两行很重要的，是告诉springmvc客户端请求和响应的类型，指定application/x-protobuf类型,spring会用ProtobufHttpMessageConverter类来解析请求和响应的实体
        httpPost.addHeader("Content-Type", "application/x-protobuf");
        httpPost.addHeader("Accept", "application/x-protobuf");
        httpPost.setEntity(inputStreamEntity);

        try {
            HttpResponse response2 = httpClient.execute(httpPost);
            if(null != response2 && response2.getStatusLine().getStatusCode() == 200){

            }
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
    }

}
