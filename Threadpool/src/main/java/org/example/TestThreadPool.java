package org.example;


public class TestThreadPool {
    public static void main(String[] args) throws InterruptedException {

        var pool = ThreadPool.createThreadPool(2);

        pool.run(() -> {
            for (int i = 0; i < 50; i++) {
                System.out.println(Thread.currentThread().getName() + " Task number: " + i + " pool1");
                try {
                    Thread.sleep(400);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        pool.run(() -> {
            for (int i = 0; i < 50; i++) {
                System.out.println(Thread.currentThread().getName() + " Task number: " + i + " pool2");
                try {
                    Thread.sleep(400);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        pool.run(() -> {
            for (int i = 0; i < 50; i++) {
                System.out.println(Thread.currentThread().getName() + " Task number: " + i + " pool3");
                try {
                    Thread.sleep(400);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            }
        });


        pool.terminate(true);
    }
}