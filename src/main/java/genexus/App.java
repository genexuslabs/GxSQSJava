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
        GxQueue queue = GxQueue.create("https://sqs.us-east-1.amazonaws.com/034683868020/TestQueue.fifo");
        for (GxQueueMessage m : queue.getMessages(10)) {
            System.out.println(m.getContents());
        }
    }
}
