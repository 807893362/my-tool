package com.example.demo.shell.process;

import com.example.demo.shell.ffmpeg.FFmpegUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Java开发工作中可能会遇到调用操作系统命令的场景，比如查看下文件夹，执行下sh/exe文件等等，那么我们就要用到Process了
 * - ProcessBuilder.start() 和 Runtime.exec 方法创建一个本机进程，并返回 Process 子类的一个实例
 * - Process 不会被GC回收，需要调用它的destroy方法去结束该子进程。
 * - 方法摘要
 * -- abstract void destroy()
 *    杀掉子进程。
 * -- abstract int exitValue()
 *    返回子进程的出口值。
 * -- abstract InputStream getErrorStream()
 *    获取子进程的错误流。
 * -- abstract InputStream getInputStream()
 *    获取子进程的输入流。
 * -- abstract OutputStream getOutputStream()
 *    获取子进程的输出流。
 * -- abstract int waitFor()
 *    导致当前线程等待，如有必要，一直要等到由该 Process 对象表示的进程已经终止。
 * -- directory(File directory)
 *    方法设置此进程构建器的工作目录。随后由该对象的 start() 方法启动的子进程将使用它作为它们的工作目录。
 *    参数可以为 null - 这意味着使用当前 Java 进程的工作目录，通常是由系统属性 user.dir 命名的目录，作为子进程的工作目录。
 */
public class ProcessTest {

    private static final Logger log = LoggerFactory.getLogger(FFmpegUtils.class);

    public static void main(String[] args) throws IOException, InterruptedException {
        // 构建命令
        List<String> commands = new ArrayList<>();
        commands.add("ffplay");
        commands.add(" http://tcflv.upliveapp.com/tencent/14868_0_843876639645368320007c2f6d7e_871212248052059340809bc39265_single.flv")						;

        // 构建进程
        Process process = new ProcessBuilder()
                .command(commands)
                .directory(null)
                .start()
                ;

        // 读取进程标准输出
        new Thread(() -> {
            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line = null;
                while ((line = bufferedReader.readLine()) != null) {
                    log.info("info =>{}", line);
                }
            } catch (IOException e) {
            }
        }).start();
        // 读取进程异常输出
        new Thread(() -> {
            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                String line = null;
                while ((line = bufferedReader.readLine()) != null) {
                    log.info("err =>{}", line);
                }
            } catch (IOException e) {
            }
        }).start();

        // 阻塞直到任务结束
        if (process.waitFor() != 0) {
            throw new RuntimeException("视频切片异常");
        }

        System.err.println("stop.");
        process.destroy();
    }


}
