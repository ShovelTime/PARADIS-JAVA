package paradis.assignment3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Consumer;

public class BaseForkExecutable extends RecursiveTask<WebPage> {
    private final WebPage targetPage;
    private final List<Consumer<WebPage>> functions;

    BaseForkExecutable(WebPage targetPage, List<Consumer<WebPage>> functions) {
        this.targetPage = targetPage;
        this.functions = functions;
    }

    @Override
    protected WebPage compute() {
        ArrayList<ArrayBlockingQueue<WebPage>> queues = new ArrayList<>(functions.size() + 1);
        ArrayList<ForkJoinTask<Void>> tasks = new ArrayList<>(functions.size());
        for(int i = 0; i < functions.size() + 1; i++) {
            queues.add(new ArrayBlockingQueue<>(1));
        }
        for (int i = 0; i < functions.size(); i++) {
            ChainTaskExecutable task = new ChainTaskExecutable(queues.get(i), queues.get(i + 1), functions.get(i));
            tasks.add(task);
        }

        try {
            queues.getFirst().put(targetPage);
            invokeAll(tasks);
            return queues.getLast().take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
