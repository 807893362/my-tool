package com.example.demo.io.rw.write;

import lombok.SneakyThrows;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * 写txt | log
 * - 同stream
 */
public class WriteFilesTest {

    @SneakyThrows
    public static void main(String[] args) {
        String text = "Hello, this is an example.";
        Files.write(Paths.get("/Users/yz/Downloads/files_txt.log"), text.getBytes(StandardCharsets.UTF_8));
    }


}
