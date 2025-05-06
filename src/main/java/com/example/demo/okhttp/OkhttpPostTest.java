package com.example.demo.okhttp;

import okhttp3.*;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

/**
 * - 过时方法 RequestBody.create(mediaType, requestBody)
 * -- 解决参考：getRequest1
 */
public class OkhttpPostTest {


    public static void main(String[] args) throws IOException {
        OkHttpClient okHttpClient = new OkHttpClient();

        // file post
        Request request = getRequest1(new File("/Users/yz/Downloads/rx.png"));
        Response response = okHttpClient.newCall(request).execute();
        System.err.println( response.code() + " === " + response.body().string());

        // json post
        request = getRequest1("{}");
        response = okHttpClient.newCall(request).execute();
        System.err.println( response.code() + " === " + response.body().string());

    }

    // Request json
    public static Request getRequest1(String json) throws IOException {
        //String转RequestBody String、ByteArray、ByteString都可以用toRequestBody()
        MediaType mediaType = MediaType.Companion.parse("application/json;charset=utf-8");
        RequestBody stringBody = RequestBody.Companion.create(json, mediaType);
        Request request = new Request
                .Builder()
                .url("http://127.0.0.1:7080/json")
                .post(stringBody)
                .build();
        return request;
    }

    // Request file
    public static Request getRequest1(File file) {
        //File转RequestBody
        MediaType mediaType = MediaType.Companion.parse("text/x-markdown; charset=utf-8");
        RequestBody fileBody = RequestBody.Companion.create(file, mediaType);
        MultipartBody multipartBody = new MultipartBody.Builder()
                .addFormDataPart("file", file.getName(), fileBody)
                .build();
        Request request = new Request
                .Builder()
                .url("http://127.0.0.1:7080/upload")
                .post(multipartBody)
                .build();
        return request;
    }

    // post json
    public static void doPost(String url, String json, Map<String, String> heardMap) throws IOException {
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        String requestBody = json;
        Request.Builder requestbuilder = new Request.Builder()
                .url(url)
                .post(RequestBody.create(mediaType, requestBody));
        //增加请求头
        for (Map.Entry<String, String> stringObjectEntry : heardMap.entrySet()) {
            requestbuilder.addHeader(stringObjectEntry.getKey(), stringObjectEntry.getValue());
        }

        Request request = requestbuilder.build();
        OkHttpClient okHttpClient = new OkHttpClient();
        Response response = okHttpClient.newCall(request).execute();
        System.out.println(response.body().string());
        System.out.println(response.message());
        System.out.println(response.code());
    }

    // post parms
    public static void doPost1(String url, Map<String, Object> paramMap, Map<String, String> heardMap) throws IOException {
        FormBody.Builder formBody = new FormBody.Builder();
        if (Objects.nonNull(paramMap)) {
            paramMap.forEach((x, y) -> formBody.add(x, (String) y));
        }
        RequestBody requestBody = formBody.build();
        Request.Builder requestbuilder = new Request.Builder()
                .url(url)
                .post(requestBody);
        //增加请求头
        for (Map.Entry<String, String> stringObjectEntry : heardMap.entrySet()) {
            requestbuilder.addHeader(stringObjectEntry.getKey(), stringObjectEntry.getValue());
        }

        Request request = requestbuilder.build();
        OkHttpClient okHttpClient = new OkHttpClient();
        Response response = okHttpClient.newCall(request).execute();
        System.out.println(response.body().string());
        System.out.println(response.message());
        System.out.println(response.code());
    }

    // post 表单
    public static void doPost2(String url, File file) throws IOException {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("id", "111")
                .addFormDataPart("content", "{\"do_layout\":1}")
                .addFormDataPart("file", file.getName(), RequestBody.create(MediaType.parse("text/plain"), file))
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("x-tilake-app-key", "")
                .addHeader("x-tilake-ca-timestamp", "")
                .addHeader("x-tilake-ca-signature", "")
                .addHeader("Content-Type", body.contentType().toString())
                .addHeader("Accept", "*/*")
                .build();

        try {
            Response response = client.newCall(request).execute();
            System.out.println(response.body().string());
            System.out.println(response.message());
            System.out.println(response.code());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
