package paradis.assignment3;

import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ForkJoinPool;

public class Program1 {

    final static int NUM_WEBPAGES = 40;
    private static WebPage[] webPages = new WebPage[NUM_WEBPAGES];

    private void exec() {
        initialize();
        long start = System.nanoTime();
        ArrayList<ArrayBlockingQueue<WebPage>> endpoints = new ArrayList<>(NUM_WEBPAGES);

        ForkJoinPool forkJoinPool = new ForkJoinPool();

        for (int i = 0; i < NUM_WEBPAGES; i++) {
            WebPage currentWebPage = webPages[i];
            ArrayBlockingQueue<WebPage> startQueue = new ArrayBlockingQueue<>(1);
            startQueue.add(currentWebPage);
            ArrayBlockingQueue<WebPage> fromDownload = new ArrayBlockingQueue<>(1);

            BaseForkExecutable<WebPage> downloader = new BaseForkExecutable<WebPage>(Program1::downloadWebPages, startQueue, fromDownload);


            ArrayBlockingQueue<WebPage> end = new ArrayBlockingQueue<>(1);

        }
    }

    private static void initialize() {
        for (int i = 0; i < NUM_WEBPAGES; i++) {
            webPages[i] = new WebPage(i, "http://www.site.se/page" + i + ".html");
        }
    }

    // [Do modify this sequential part of the program.]
    private static void downloadWebPages(Queue<WebPage> incoming, Queue<WebPage> outgoing) {
        for (int i = 0; i < NUM_WEBPAGES; i++) {
            webPages[i].download();
        }
    }

    // [Do modify this sequential part of the program.]
    private static void analyzeWebPages() {
        for (int i = 0; i < NUM_WEBPAGES; i++) {
            webPages[i].analyze();
        }
    }

    // [Do modify this sequential part of the program.]
    private static void categorizeWebPages() {
        for (int i = 0; i < NUM_WEBPAGES; i++) {
            webPages[i].categorize();
        }
    }

    // [You are welcome to modify this method, but it should NOT be parallelized.]
    private static void presentResult() {
        for (int i = 0; i < NUM_WEBPAGES; i++) {
            System.out.println(webPages[i]);
        }
    }

    public static void main(String[] args) {
        new Program1().exec();
    }
}
