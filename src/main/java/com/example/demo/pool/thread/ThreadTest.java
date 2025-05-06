package com.example.demo.pool.thread;

/**
 * 关于内存开销
 * - 现成数据结构中还包含所谓的线程上下文。上下文是一个内存块，其中包含了CPU的寄存器集合。
 * - Java线程的线程栈区别于堆，它是不受Java程序控制的，只受系统资源限制。默认一个线程的线程栈大小是1M，别小看这1M的空间，如果每个用户请求都新建线程的话，1024个用户光线程就占用了1个G的内存，如果系统比较大的话，一下子系统资源就不够用了，最后程序就崩溃了。
 * 上下文切换
 * - 主要是各种寄存器的保存、恢复
 * - CPU 给每个任务都服务一定的时间，然后把当前任务的状态保存下来，在加载下一任务的状态后
 * ，继续服务下一任务，任务的状态保存及再加载, 这段过程就叫做上下文切换。时间片轮转的方式使多个任务在同一颗 CPU 上执行变成了可能。
 */
public class ThreadTest {

    public static void main(String[] args) {

        Thread thread = Thread.currentThread();
        String name = thread.getName();

        // 表示可以中断线程，实际上只是给线程设置一个中断标志，但是线程依旧会执行。
        thread.interrupt();
        // Thread类的静态方法。检查当前线程的中断标志，返回一个boolean并清除中断状态
        // ，其连续两次调用的返回结果不一样，因为第二次调用的时候线程的中断状态已经被清除，会返回一个false。
        boolean interrupted1 = thread.interrupted();
        // 测试线程是否被中断，不会清除中断状态。
        boolean interrupted = thread.isInterrupted();


    }


}
