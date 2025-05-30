package com.example.demo.okhttp;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.example.demo.shell.ffmpeg.FFmpegUtils;
import okhttp3.*;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class OkHttpUtil {

    private static final Logger log = LoggerFactory.getLogger(FFmpegUtils.class);

    public static final String MEDIA_TYPE_JSON = "application/json; charset=utf-8";

    private OkHttpUtil() {
    }

    /**
     * 获取默认的OkHttpClient
     * @return
     */
    public static OkHttpClient getOkHttpClient() {
        return getOkHttpClient(60, 60, 60);
    }

    public static OkHttpClient getOkHttpClient(int connectTimeout, int readTimeOut, int writeTimeOut) {
        OkHttpClient.Builder builder = new okhttp3.OkHttpClient().newBuilder();
        builder.connectTimeout(connectTimeout, TimeUnit.SECONDS);
        builder.readTimeout(readTimeOut, TimeUnit.SECONDS);
        builder.writeTimeout(writeTimeOut, TimeUnit.SECONDS);
        return builder.build();
    }

    /**
     * get请求
     * @param okHttpClient
     * @param url
     * @param headers header参数
     * @return
     */
    public static String get(OkHttpClient okHttpClient, String url, Headers headers) {
        log.info("okHttpClient get url:{}.", url);
        Request request = new Request.Builder().url(url).headers(headers).get().build();

        String responseData = request(okHttpClient, url, request);
        log.info("okHttpClient get url:{},request responseData====> {}", url, responseData);
        return responseData;
    }

    public static String get(OkHttpClient okHttpClient, String url) {
        Headers headers = new Headers.Builder().build();
        return get( okHttpClient, url, headers);
    }

    /**
     * GET请求。使用默认的 okHttpClient 和 headers
     * @param url
     * @return
     */
    public static String get(String url) {
        OkHttpClient okHttpClient = getOkHttpClient();
        Headers headers = new Headers.Builder().build();
        return get( okHttpClient, url, headers);
    }

    /**
     * post请求，获取响应结果
     *
     * @param okHttpClient
     * @param url
     * @param bodyJson
     * @param headers
     * @return
     */
    public static String post(OkHttpClient okHttpClient, String url, JSONObject bodyJson, Headers headers) {
        log.info("okHttpClient post url:{}, body====> {}", url, bodyJson);
        MediaType mediaTypeJson = MediaType.parse(MEDIA_TYPE_JSON);
        RequestBody requestBody = RequestBody.create(mediaTypeJson, JSON.toJSONString(bodyJson));
        Request request = new Request.Builder().url(url).headers(headers).post(requestBody).build();

        String responseData = request(okHttpClient, url, request);
        log.info("okHttpClient post url:{},post responseData====> {}", url, responseData);
        return responseData;
    }

    public static String post(OkHttpClient okHttpClient, String url, JSONObject bodyJson) {
        Headers headers = new Headers.Builder().build();
        return post( okHttpClient,  url,  bodyJson, headers);
    }

    /**
     * post请求。使用默认的 okHttpClient 和 headers
     * @param url
     * @param bodyJson
     * @return
     */
    public static String post( String url, JSONObject bodyJson) {
        //使用默认的 okHttpClient
        OkHttpClient okHttpClient = getOkHttpClient();
        Headers headers = new Headers.Builder().build();
        //如果需要自定义 okHttpClient或headers传参，可以调用以下方法
        return post( okHttpClient,  url,  bodyJson, headers);
    }

    /**
     * 获取响应结果
     *
     * @param okHttpClient
     * @param url
     * @param request
     * @return
     */
    public static String request(OkHttpClient okHttpClient, String url, Request request) {
        String responseData = "";
        try (Response response = okHttpClient.newCall(request).execute()) {
            if (response != null && response.body() != null) {
                return response.body().string();
            }
        } catch (Exception e) {
            log.error("okHttpClient getResponse error.url:{}", url, e);
        }

        return responseData;
    }

    /**
     * 上传文件
     *
     * @param okHttpClient  okHttp客户端
     * @param url 上传文件的url
     * @param fileKey       文件对应的key
     * @param formDataJson  form-data参数
     * @param headers
     * @param file
     * @return
     */
    public static String uploadFile(OkHttpClient okHttpClient, String url,
                                    String fileKey, File file, JSONObject formDataJson, Headers headers) {
        log.info("uploadFile url:{}, uploadFile formDataJson====> {}", url, formDataJson);
        // 支持传文件的同时，传参数。
        MultipartBody requestBody = getMultipartBody(fileKey, file,  formDataJson);

        // 构建request请求体
        Request request = new Request.Builder().url(url).headers(headers).post(requestBody).build();

        String responseData = request(okHttpClient, url, request);

        // 会在本地产生临时文件，用完后需要删除
        if (file.exists()) {
            file.delete();
        }
        return responseData;

    }

    /**
     * 上传文件
     * @param url
     * @param fileKey form-data文件对应的key
     * @param multipartFile 文件上传对应的 multipartFile
     * @param formDataJson form-data参数
     * @return
     */
    public static String uploadFile(String url,
                                    String fileKey, MultipartFile multipartFile, JSONObject formDataJson) {
        //使用默认的okHttpClient
        OkHttpClient okHttpClient = getOkHttpClient();
        Headers headers = new Headers.Builder().build();
        return uploadFile(okHttpClient, url, fileKey, getFile(multipartFile), formDataJson, headers);
    }

    public static String uploadFile(OkHttpClient okHttpClient, String url,
                                    String fileKey, File file, JSONObject formDataJson) {
        Headers headers = new Headers.Builder().build();
        return uploadFile(okHttpClient, url,  fileKey, file, formDataJson, headers);
    }

    /**
     * 上传文件
     * 使用默认的okHttpClient
     *
     * @param url
     * @param fileKey form-data文件对应的key
     * @param file 文件
     * @param formDataJson form-data参数
     * @return
     */
    public static String uploadFile(String url,
                                    String fileKey, File file, JSONObject formDataJson) {
        //使用默认的okHttpClient
        OkHttpClient okHttpClient = getOkHttpClient();
        Headers headers = new Headers.Builder().build();
        return uploadFile(okHttpClient, url, fileKey, file, formDataJson, headers);
    }

    /**
     * 上传文件用。构建form-data 参数
     *
     * @param fileKey       文件对应的key
     * @param file          文件
     * @param formDataJson  form-data参数
     * @return
     */
    public static MultipartBody getMultipartBody(String fileKey, File file, JSONObject formDataJson) {
        RequestBody fileBody = RequestBody.create(MultipartBody.FORM, file);

        MultipartBody.Builder bodyBuilder = new MultipartBody.Builder();
        // 设置传参为form-data格式
        bodyBuilder.setType(MultipartBody.FORM);
        bodyBuilder.addFormDataPart(fileKey, file.getName(), fileBody);
        // 添加 form-data参数
        for (Map.Entry<String, Object> entry : formDataJson.entrySet()) {
            //参数通过 bodyBuilder.addFormDataPart(key, value) 添加
            bodyBuilder.addFormDataPart(entry.getKey(), Objects.toString(entry.getValue(),""));
        }
        return bodyBuilder.build();
    }

    /**
     * 获取文件
     * @param multipartFile
     * @return
     */
    public static File getFile(MultipartFile multipartFile) {
        File file = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        try {
            FileUtils.copyInputStreamToFile(multipartFile.getInputStream(), file);
        } catch (IOException e) {
            log.error("copyInputStreamToFile error.", e);
        }
        return file;
    }

}



