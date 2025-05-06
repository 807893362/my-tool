package com.example.demo.io.rw.read;

import lombok.Cleanup;
import lombok.SneakyThrows;

import java.io.File;
import java.util.Scanner;

/**
 * 使用Buffer 读txt | log
 */
public class ReadScannerTest {

    @SneakyThrows
    public static void main(String[] args) {
        File file = new File("/Users/yz/Downloads/print_txt.log");
        @Cleanup Scanner scanner = new Scanner(file);
        while (scanner.hasNext()) {
            System.err.println(scanner.next());
        }
    }


}
