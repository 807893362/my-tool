package com.example.demo.io.rw.write;

import lombok.Cleanup;
import lombok.SneakyThrows;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;

/**
 * 写txt | log
 */
public class WriteStreamTest {

    @SneakyThrows
    public static void main(String[] args) {
        @Cleanup FileOutputStream outputStream = new FileOutputStream("/Users/yz/Downloads/stream_txt.log");
        String text = "Hello, this is an example.";
        byte[] data = text.getBytes();
        outputStream.write(data);
        outputStream.close();
    }


}
