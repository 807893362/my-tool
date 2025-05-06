package com.example.demo.okhttp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传大小限制 2.0.x之后:
 *  spring.servlet.multipart.max-file-size=50MB
 *  spring.servlet.multipart.max-request-size=50MB
 */
@RestController
public class TestController {

    // http://127.0.0.1:7080/upload
    @PostMapping("/upload")
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file){
        String result = "SUCCESS";
        try {
            String name = file.getName();
            System.err.println(name);
            byte[] bytes = file.getBytes();
            System.err.println(bytes.length);
            return ResponseEntity.ok(result);
        }catch (Exception e){
            return ResponseEntity.badRequest().body("FAIL");
        }
    }

    // http://127.0.0.1:7080/json
    @PostMapping("/json")
    public ResponseEntity<String> json(@RequestBody String json){
        String result = "SUCCESS";
        try {
            System.err.println(json);
            return ResponseEntity.ok(result);
        }catch (Exception e){
            return ResponseEntity.badRequest().body("FAIL");
        }
    }


}
