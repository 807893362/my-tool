package com.example.demo.okhttp;

import okhttp3.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Objects;

public class OkhttpGetTest {




    public static void main(String[] args) {

        int a = 2;
        System.err.println(8 >>2*(a++));
        System.err.println(8*(a++) >>2);
        System.err.println(24 >>2);
        System.err.println(1024 >>2*3);
        System.err.println(1024 >>6);

        System.err.println(3 * a++);
        System.err.println(3 * (a++));

    }

    // get无参
    public static void doGet1(String url) throws IOException {
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(url)
                .get()//默认就是GET请求，可以不写
                .build();
        Response response = okHttpClient.newCall(request).execute();
        String string = response.body().string();
        System.out.println(string);
    }

    // get有参
    public static void doGet2(String url, Map<String, Object> paramMap) throws IOException {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder requestbuilder = new Request.Builder()
                .get();//默认就是GET请求，可以不写

        StringBuilder urlbuilder = new StringBuilder(url);
        if (Objects.nonNull(paramMap)) {
            urlbuilder.append("?");
            paramMap.forEach((key, value) -> {
                try {
                    urlbuilder.append(URLEncoder.encode(key, "utf-8")).append("=").append(URLEncoder.encode((String) value, "utf-8")).append("&");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            });
            urlbuilder.deleteCharAt(urlbuilder.length() - 1);
        }

        Request request = requestbuilder.url(urlbuilder.toString()).build();
        Response response = okHttpClient.newCall(request).execute();
        String string = response.body().string();
        System.out.println(string);
    }

    // 带参数+请求头
    public static void doGet3(String url, Map<String, Object> paramMap,Map<String, String> heardMap) throws IOException {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder requestbuilder = new Request.Builder()
                .get();//默认就是GET请求，可以不写

        //增加参数
        StringBuilder urlbuilder = new StringBuilder(url);
        if (Objects.nonNull(paramMap)) {
            urlbuilder.append("?");
            paramMap.forEach((key, value) -> {
                try {
                    urlbuilder.append(URLEncoder.encode(key, "utf-8")).append("=").append(URLEncoder.encode((String) value, "utf-8")).append("&");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            });
            urlbuilder.deleteCharAt(urlbuilder.length() - 1);
        }
        //增加请求头
        Request.Builder heardBuilder = requestbuilder.url(urlbuilder.toString());
        for (Map.Entry<String, String> stringObjectEntry : heardMap.entrySet()) {
            heardBuilder.addHeader(stringObjectEntry.getKey(),stringObjectEntry.getValue());
        }

        Request request = heardBuilder.build();
        Response response = okHttpClient.newCall(request).execute();
        System.out.println(response.body().string());
        System.out.println(response.message());
        System.out.println(response.code());

    }

    // GET 通过监听 获取请求结果
    public static void doGet(String url,Map<String, Object> paramMap,Map<String, String> heardMap) {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder requestbuilder = new Request.Builder()
                .get();//默认就是GET请求，可以不写

        //增加参数
        StringBuilder urlbuilder = new StringBuilder(url);
        if (Objects.nonNull(paramMap)) {
            urlbuilder.append("?");
            paramMap.forEach((key, value) -> {
                try {
                    urlbuilder.append(URLEncoder.encode(key, "utf-8")).append("=").append(URLEncoder.encode((String) value, "utf-8")).append("&");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            });
            urlbuilder.deleteCharAt(urlbuilder.length() - 1);
        }
        //增加请求头
        Request.Builder heardBuilder = requestbuilder.url(urlbuilder.toString());
        for (Map.Entry<String, String> stringObjectEntry : heardMap.entrySet()) {
            heardBuilder.addHeader(stringObjectEntry.getKey(),stringObjectEntry.getValue());
        }

        Request request = heardBuilder.build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println( "onFailure: ");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                System.out.println(response.body().string());
                System.out.println(response.message());
                System.out.println(response.code());
            }
        });
    }






}
