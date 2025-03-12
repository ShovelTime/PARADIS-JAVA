package paradis.assignment3;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class MyExecutor implements ExecutorService {
    private final ReentrantReadWriteLock taskQueueLock = new ReentrantReadWriteLock();
    private final int threadCount;
    private final ArrayList<ExecutorThread> threadPool;
    private final ArrayList<Runnable> waitingTasks = new ArrayList<>();
    private volatile boolean shutdown = false;

    MyExecutor(int threadCount) {
        this.threadCount = threadCount;
        this.threadPool = new ArrayList<>(threadCount);
    }
    MyExecutor() {
        this(Runtime.getRuntime().availableProcessors());
    }

    @Override
    public void shutdown() {
        if(shutdown) return;
        shutdown = true;
        taskQueueLock.readLock().lock();
    }

    @Override
    public List<Runnable> shutdownNow() {
        if(!shutdown) shutdown();
        taskQueueLock.readLock().lock();
        return Collections.unmodifiableList(this.waitingTasks);
    }

    @Override
    public boolean isShutdown() {
        return shutdown;
    }

    @Override
    public boolean isTerminated() {
        return false;
    }

    @Override
    public boolean awaitTermination(long l, TimeUnit timeUnit) throws InterruptedException {
        return false;
    }

    @Override
    public <T> Future<T> submit(Callable<T> callable) {
        return null;
    }

    @Override
    public <T> Future<T> submit(Runnable runnable, T t) {
        return null;
    }

    @Override
    public Future<?> submit(Runnable runnable) {
        return null;
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> collection) throws InterruptedException {
        return List.of();
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> collection, long l, TimeUnit timeUnit) throws InterruptedException {
        return List.of();
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> collection) throws InterruptedException, ExecutionException {
        return null;
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> collection, long l, TimeUnit timeUnit) throws InterruptedException, ExecutionException, TimeoutException {
        return null;
    }

    @Override
    public void execute(Runnable runnable) {

    }
}

class ExecutorThread extends Thread {
    private final MyExecutor executor;
    private final LinkedBlockingQueue<Runnable> taskQueue;

    ExecutorThread(MyExecutor executor, LinkedBlockingQueue<Runnable> taskQueue) {
        this.executor = executor;
        this.taskQueue = taskQueue;
    }
}