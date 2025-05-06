package com.example.demo.io.rw.read;

import lombok.Cleanup;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * 使用Buffer 读txt | log
 */
public class ReadBufferTest {

    @SneakyThrows
    public static void main(String[] args) {
        FileReader fileWriter = new FileReader("/Users/yz/Downloads/buffer_txt.log");
        @Cleanup BufferedReader bufferedReader = new BufferedReader(fileWriter);
        bufferedReader.lines().parallel().forEach(System.err::println);

        BufferedReader bufferedReader1 = Files.newBufferedReader(Paths.get("/Users/yz/Downloads/buffer_txt.log"));
        bufferedReader1.lines().parallel().forEach(System.err::println);
    }



}
