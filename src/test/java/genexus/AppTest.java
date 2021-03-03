package genexus;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import sqs.GxMessageContent;
import sqs.GxQueue;
import sqs.GxQueueMessage;

/**
 * Unit test for simple App.
 */
public class AppTest 
{

    @Test
    public void sendMessages()
    {
        GxQueue queue = new GxQueue();
        queue.setURL("https://sqs.us-east-1.amazonaws.com/034683868020/TestQueue.fifo");
        List<GxMessageContent> contents = new ArrayList<>();
        for (int i = 0 ; i < 10; i++) {
            GxMessageContent m = new GxMessageContent();
            m.setId(String.valueOf(i));
            m.setContents("Message " + m.getId());
            contents.add(m);
        }
        queue.sendMessages(contents);
        assertEquals(10, queue.getMessages().size());
    
    }
   
    @Test
    public void consumeMessages()
    {
        GxQueue queue = new GxQueue();
        queue.setURL("https://sqs.us-east-1.amazonaws.com/034683868020/TestQueue.fifo");
        for (GxQueueMessage m : queue.getMessages()) {
            String content = m.getContents();
        }
        assertEquals(10, queue.getMessages().size());
        removeMessages();
    }

    @Test
    public void removeMessages()
    {
        GxQueue queue = new GxQueue();
        queue.setURL("https://sqs.us-east-1.amazonaws.com/034683868020/TestQueue.fifo");
        for (GxQueueMessage m : queue.getMessages()) {
            queue.removeMessage(m.getReceiptId());
        }
        assertEquals(0, queue.getMessages().size());
    }
}
