package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class ThreadPoolImpl implements ThreadPool {

    final List<Runnable> list = new ArrayList<>();
    Runnable pendingTask;
    boolean running = true;
    Semaphore sem;

    public ThreadPoolImpl(int threadCount) throws InterruptedException {
        this.sem = new Semaphore(threadCount);
        if (threadCount < 0)
            throw new IllegalArgumentException("Error. the number of threads cannot be negative");

        for (int i = 0; i < threadCount; i++) {
            ThreadGroup threadGroup = new ThreadGroup("myThreadGroup");
            Worker worker = new Worker(threadGroup, "Worker " + i);
            worker.start();
            Thread.sleep(1000);
        }
    }

    @Override
    public void run(Runnable task) {
        list.add(task);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public Runnable take() throws InterruptedException {
        synchronized (list) {
            if (list.size() > 0) {
                pendingTask = list.get(0);
                list.remove(0);
            } else pendingTask = null;
        }
        return pendingTask;
    }

    @Override
    public void terminate(boolean waitPendingTasks) {
        while (list.size() > 0) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        running = false;
    }

    private class Worker extends Thread {

        public Worker(ThreadGroup threadGroup, String name) {
            super(threadGroup, name);
        }

        @Override
        public void run() {
            while (list.size() == 0 && running) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            while (running && !interrupted()) {
                try {
                    sem.acquire();
                    final Runnable task = take();
                    if (task != null) {
                        task.run();
                        System.out.println("End tasks " + Thread.currentThread().getName());
                    } else {
                        this.interrupt();
                    }
                    sem.release();
                } catch (InterruptedException e) {
                    this.interrupt();
                }
            }
        }
    }
}
