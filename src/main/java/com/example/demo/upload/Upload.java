package com.example.demo.upload;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

public class Upload {

    public static void main(String[] args) {
        upload(1L, "zh", "/Users/yz/Downloads/wav/pcm.pcm");
    }


    public static void upload(long uid, String language, String localFile){
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        try {
            httpClient = HttpClients.createDefault();

            // 把一个普通参数和文件上传给下面这个地址 是一个servlet
            HttpPost httpPost = new HttpPost("http://127.0.0.1:7091/speech/upload");

            // 把文件转换成流对象FileBody
            FileBody bin = new FileBody(new File(localFile));

            HttpEntity reqEntity = MultipartEntityBuilder.create()
                    .addPart("speechFile", bin)
                    .addPart("language", new StringBody(language, ContentType.create(
                            "text/plain", Consts.UTF_8)))
                    .addPart("uid", new StringBody(uid+"", ContentType.create(
                            "text/plain", Consts.UTF_8)))

                    .build();

            httpPost.setHeader("api-key", "sjdfhjhdjhqwh==");
            httpPost.setEntity(reqEntity);

            // 发起请求 并返回请求的响应
            response = httpClient.execute(httpPost);

            System.out.println("The response value of token:" + response.getFirstHeader("token"));

            // 获取响应对象
            HttpEntity resEntity = response.getEntity();
            if (resEntity != null) {
                // 打印响应长度
                System.out.println("Response content length: " + resEntity.getContentLength());
                // 打印响应内容
                System.out.println(EntityUtils.toString(resEntity, Charset.forName("UTF-8")));
            }

            // 销毁
            EntityUtils.consume(resEntity);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if(response != null){
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                if(httpClient != null){
                    httpClient.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
