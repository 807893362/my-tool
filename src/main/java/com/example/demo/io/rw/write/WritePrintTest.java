package com.example.demo.io.rw.write;

import lombok.Cleanup;
import lombok.SneakyThrows;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

/**
 * 写txt | log
 */
public class WritePrintTest {

    @SneakyThrows
    public static void main(String[] args) {
        File file = new File("/Users/yz/Downloads/print_txt.log");
        @Cleanup PrintWriter writer = new PrintWriter(file);
        writer.write("Hello World! \n");
        writer.write("Hello World! \r");
        writer.write("Hello World! \r\n");
    }


}
