package com.example.demo.shell.exe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * process调用exe
 */
public class ExeTest {

    public static void main(String[] args) throws IOException, InterruptedException {

        // 构建命令
        List<String> commands = new ArrayList<>();
        commands.add("C:\\Debug\\TestRedis.exe");

        ProcessBuilder pb = new ProcessBuilder(commands);
        pb.redirectErrorStream(true);
        Process process = pb.start();
        //可能导致进程阻塞，甚至死锁
        int ret = process.waitFor();
        System.out.println("return value:"+ret);
        System.out.println(process.exitValue());
        byte[] bytes = new byte[process.getInputStream().available()];
        process.getInputStream().read(bytes);
        System.out.println(new String(bytes));
    }

}
