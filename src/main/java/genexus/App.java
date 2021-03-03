package genexus;

import sqs.GxQueue;
import sqs.GxQueueMessage;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );

        GxQueue queue = new GxQueue();
        queue.setURL("https://sqs.us-east-1.amazonaws.com/034683868020/TestQueue.fifo");
        for (GxQueueMessage m : queue.getMessages()) {
            System.out.println(m.getContents());
        }
    }
}
