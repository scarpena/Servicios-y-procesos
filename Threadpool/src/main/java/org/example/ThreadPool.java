package org.example;

public interface ThreadPool {
    void run(Runnable task);

    void terminate(boolean waitPendingTasks);


    static ThreadPool createThreadPool(int threadCount) throws InterruptedException {

        return new ThreadPool() {
            final ThreadPoolImpl pool = new ThreadPoolImpl(threadCount);
            volatile boolean terminatePool;

            @Override
            public void run(Runnable task) {
                if (task != null && !terminatePool) {
                    pool.run(task);
                }
            }

            @Override
            public void terminate(boolean waitPendingTasks) {
                while (pool.list.size() > 0) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                terminatePool = true;
                pool.terminate(true);
            }
        };
    }
}
