package paradis.assignment3;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

public class MyExecutor implements ExecutorService {
    private final ReentrantReadWriteLock taskQueueLock = new ReentrantReadWriteLock();
    private final int threadCount;
    private final ArrayList<ExecutorThread> threadPool;
    private final ArrayList<FutureTask<?>> waitingTasks = new ArrayList<>();
    private volatile boolean shutdown = false;
    private volatile boolean terminated = false;



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
        for (ExecutorThread thread : threadPool) {
            if(!thread.isTerminated()) return false;
        }
        return true;
    }

    @Override
    public boolean awaitTermination(long l, TimeUnit timeUnit) throws InterruptedException {
        return false;
    }

    @Override
    public <T> Future<T> submit(Callable<T> callable) {
        FutureTask<T> futureTask = new FutureTask<>(callable);
        this.waitingTasks.add(futureTask);
        return futureTask;
    }

    @Override
    public <T> Future<T> submit(Runnable runnable, T t) {
        FutureTask<T> futureTask = new FutureTask<>(runnable, t);
        this.waitingTasks.add(futureTask);
        return futureTask;
    }

    @Override
    public Future<?> submit(Runnable runnable) {
        FutureTask<?> futureTask = new FutureTask<>(runnable, null);
        this.waitingTasks.add(futureTask);
        return futureTask;
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> collection) throws InterruptedException {
        List<FutureTask<T>> tasks = collection.stream().map(FutureTask::new).toList();
        List<Future<T>> futures = tasks.stream().map(task -> (RunnableFuture<T>) task).collect(Collectors.toList());
        this.waitingTasks.addAll(tasks);
        for(Future<T> future : futures) {
            try {
                future.get();
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
        }

        return futures;
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> collection, long l, TimeUnit timeUnit) throws InterruptedException {
        List<FutureTask<T>> tasks = collection.stream().map(FutureTask::new).toList();
        this.waitingTasks.addAll(tasks);

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
        FutureTask<Void> futureTask = new FutureTask<>(runnable, null);
        this.waitingTasks.add(futureTask);

    }
}

class ExecutorThread extends Thread {
    private final MyExecutor executor;
    private final LinkedBlockingQueue<FutureTask<?>> taskQueue;
    private volatile boolean terminated = false;

    ExecutorThread(MyExecutor executor, LinkedBlockingQueue<FutureTask<?>> taskQueue) {
        this.executor = executor;
        this.taskQueue = taskQueue;
    }

    public boolean isTerminated() {
        return terminated;
    }

    public void run() {
        try {
            while(!executor.isShutdown())
            {
                FutureTask<?> task = taskQueue.take();
                task.run();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            this.terminated = true;
        }
    }
}