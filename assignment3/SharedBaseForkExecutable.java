package paradis.assignment3;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;
import java.util.function.Consumer;

public class SharedBaseForkExecutable extends RecursiveAction {
    private final List<Consumer<WebPage>> functions;
    private final List<ArrayBlockingQueue<WebPage>> queues;

    SharedBaseForkExecutable(List<ArrayBlockingQueue<WebPage>> queues, List<Consumer<WebPage>> functions) {
        this.queues = queues;
        this.functions = functions;
    }

    @Override
    protected void compute() {;
        ArrayList<ForkJoinTask<Void>> tasks = new ArrayList<>(functions.size());
        for (int i = 0; i < functions.size(); i++) {
            ChainTaskExecutable task = new ChainTaskExecutable(queues.get(i), queues.get(i + 1), functions.get(i));
            tasks.add(task);
        }

        invokeAll(tasks);
    }

}

