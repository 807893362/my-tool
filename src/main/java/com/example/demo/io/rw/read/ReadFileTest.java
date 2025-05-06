package com.example.demo.io.rw.read;

import lombok.Cleanup;
import lombok.SneakyThrows;

import java.io.File;
import java.io.FileReader;

/**
 * 读txt | log
 */
public class ReadFileTest {

    @SneakyThrows
    public static void main(String[] args) {
        File file = new File("/Users/yz/Downloads/txt.log");
        @Cleanup FileReader reader = new FileReader(file);
        int line;
        while ((line = reader.read()) != -1){
            System.out.print((char)line);
        }

    }



}
