package com.example.demo.io.rw.read;

import lombok.SneakyThrows;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * 使用Buffer 读txt | log
 */
public class ReadFilesTest {

    @SneakyThrows
    public static void main(String[] args) {
        List<String> strings = Files.readAllLines(Paths.get("/Users/yz/Downloads/files_txt.log"), StandardCharsets.UTF_8);
        strings.stream().parallel().forEach(System.err::println);

        String s = Files.readString(Paths.get("/Users/yz/Downloads/files_txt.log"));
        System.err.println(s);

    }


}
