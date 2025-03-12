package paradis.assignment3;

import paradis.assignment3.WebPage;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.RecursiveAction;
import java.util.function.Consumer;

public class ChainTaskExecutable extends RecursiveAction {
    private final ArrayBlockingQueue<WebPage> from, to;
    private final Consumer<WebPage> function;
    ChainTaskExecutable(ArrayBlockingQueue<WebPage> from, ArrayBlockingQueue<WebPage> to, Consumer<WebPage> function) {
        this.from = from;
        this.to = to;
        this.function = function;
    }

    @Override
    protected void compute() {
        try {
            WebPage fromPage = from.take();
            function.accept(fromPage);
            to.put(fromPage);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}