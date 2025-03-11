package paradis.assignment3;

import java.util.concurrent.RecursiveTask;

public class BaseForkExecutable extends RecursiveTask<WebPage> {
    private final WebPage targetPage;

    BaseForkExecutable(WebPage targetPage) {
        this.targetPage = targetPage;
    }

    @Override
    protected WebPage compute() {

        return null;
    }

}
