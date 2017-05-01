package se.edinjakupovic.mobilescraper.WebScraping;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import se.edinjakupovic.mobilescraper.DTOs.UrlSummaryDTO;

/**
 * ThreadSearch.java - Called from the ParseURl class. Gets one link per tread
 * and returns the webscraped content after summarizing it.
 * @see se.edinjakupovic.mobilescraper.ViewActivities.ResultPage.ParseUrl
 * @see ThreadScrapeResult
 *
 * @author Edin Jakupovic
 * @version 1.0
 * */

public class ThreadSearch {

    /**
     * Creates a thread for each searchresult from google. Up
     * to 10 threads. An ExecutorService collects the ThreadScrapeResult
     * from each thread. The result is sorted by relevance and displayed to ui
     *
     * @param result Contains the string returned from the QuerySearch @see QuerySearch
     *
     *
     */
    public static ArrayList<UrlSummaryDTO> threadSearch(ArrayList result){
        String Temp = result.toString();
        String set[] = Temp.split("\\s+");
        int numOfTreads = set.length/2;
        ArrayList<ThreadScrapeResult> threadResult = new ArrayList<>();
        ArrayList<UrlSummaryDTO> threadSummeries = new ArrayList<>();

        List<Callable<ThreadScrapeResult>> callableTasks = new ArrayList<>();
        for(int i=0, j=0; j<set.length-1; i++,j+=2){
            callableTasks.add(new UrlRun(set[j],Double.parseDouble(set[j+1])));
        }

        ExecutorService executor = Executors.newFixedThreadPool(numOfTreads);
        try {
            List<Future<ThreadScrapeResult>> futures = executor.invokeAll(callableTasks);
            for (Future futurex:futures) {
                threadResult.add((ThreadScrapeResult) futurex.get());
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        for (ThreadScrapeResult x: threadResult) {
            if(x.getText().length() > 100){
                threadSummeries.add(new UrlSummaryDTO(x.getText(),x.getUrl(),x.getRelevance()));

            }
        }
        return threadSummeries;
    }
}
