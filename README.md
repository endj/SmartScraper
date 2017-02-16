TODO LIST:

Post to php where searchterm and websites are inserted.

Get device information and limit ammount of threads. Faster?

       return getNumCoresOldPhones();
        return Runtime.getRuntime().availableProcessors()


Sen för att skapa threads


int numberOfCores = get.cores;

Thread[] threads = new Thread[numberOfCores];

String[] text = result.split("\\¤¤+");  // Link results

int ratio = text.length/threads.length // Get how many links per core

 // Check if links > cores
 
 First case: no links -> Error message // no result found, go back to search page
 Second case: less links then core -> Create as many threads as links
 third case: more links then cores -> Push links in order to threads until all links are pushed, need some object to hold them
 
 
for(int i=0; i < numberOfCores; i++){
    threads[i].; 
}
