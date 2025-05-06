package com.example.demo.queue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class DelayQueueTest {

    static BlockingQueue<MyTask> task = new DelayQueue<>();

    static class MyTask implements Delayed {

        String name;
        long runningTime;

        MyTask(String name,long rt) {
            this.name = name;
            this.runningTime= rt;
        }
        @Override
        public int compareTo(Delayed other) {
            long td = this.getDelay(TimeUnit.MILLISECONDS);
            long od = other.getDelay(TimeUnit.MILLISECONDS);
            return Long.compare(td,od);
        }

        @Override
        public long getDelay(TimeUnit unit) {
            return unit.convert(runningTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
        }

        @Override
        public String toString() {
            return name + "-" + runningTime;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        long now = System.currentTimeMillis();
        MyTask t1 = new MyTask("t1",now + 1000);
        MyTask t2 = new MyTask("t2",now + 2000);
        MyTask t3 = new MyTask("t3",now + 1500);
        MyTask t4 = new MyTask("t4",now + 3000);
        MyTask t5 = new MyTask("t5",now + 500);
        MyTask t6 = new MyTask("t6",now + 2500);
        MyTask t7 = new MyTask("t7",now + 60000);

        task.put(t1);
        task.put(t2);
        task.put(t3);
        task.put(t4);
        task.put(t5);
        task.put(t6);
        task.put(t7);

        for (int i = 0; i < 7; i++) {
            System.out.println(task.take());
        }
    }

}
