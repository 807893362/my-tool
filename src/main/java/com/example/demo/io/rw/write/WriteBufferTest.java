package com.example.demo.io.rw.write;

import lombok.Cleanup;
import lombok.SneakyThrows;

import java.io.BufferedWriter;
import java.io.FileWriter;

/**
 * 使用Buffer 写txt | log
 */
public class WriteBufferTest {

    @SneakyThrows
    public static void main(String[] args) {
        FileWriter fileWriter = new FileWriter("/Users/yz/Downloads/buffer_txt.log");
        @Cleanup BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        bufferedWriter.write("Hello, this is an example.");
        bufferedWriter.close();
    }


}
