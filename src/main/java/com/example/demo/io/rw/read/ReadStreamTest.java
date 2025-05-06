package com.example.demo.io.rw.read;

import lombok.Cleanup;
import lombok.SneakyThrows;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;

/**
 * 使用Buffer 读txt | log
 */
public class ReadStreamTest {

    @SneakyThrows
    public static void main(String[] args) {
        File file = new File("/Users/yz/Downloads/print_txt.log");
        @Cleanup FileInputStream stream = new FileInputStream(file);
        int line;
        while (( line = stream.read()) != -1) {
            System.err.print((char)line);
        }

        @Cleanup BufferedInputStream stream1 = new BufferedInputStream(new FileInputStream(file));
        while (( line = stream1.read()) != -1) {
            System.err.print((char)line);
        }

        @Cleanup DataInputStream stream2 = new DataInputStream(new FileInputStream(file));
        while (( line = stream2.read()) != -1) {
            System.err.print((char)line);
        }

    }


}
