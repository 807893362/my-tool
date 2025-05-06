package com.example.demo.io.rw.write;

import lombok.Cleanup;
import lombok.SneakyThrows;

import java.io.File;
import java.io.FileWriter;

/**
 * 写txt | log
 */
public class WriteFileTest {

    @SneakyThrows
    public static void main(String[] args) {
        File file = new File("/Users/yz/Downloads/txt.log");
        @Cleanup FileWriter writer = new FileWriter(file);
        writer.write("Hello World! \n");
        writer.write("Hello World! \r");
        writer.write("Hello World! \r\n");
    }


}
