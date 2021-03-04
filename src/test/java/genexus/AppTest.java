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

    private String m_url;
    private String getUrl(){
        if (m_url == null) {
            m_url = System.getenv("QUEUE_URL");
            if (m_url == null || m_url.isEmpty()) {
                //fifo m_url = "https://sqs.us-east-1.amazonaws.com/034683868020/TestQueue.fifo";
                m_url = "https://sqs.us-east-1.amazonaws.com/034683868020/StandardQueue";
            }
        }
        return m_url;
    }

    @Test
    public void sendMessages()
    {
        GxQueue q = sendMsgs(getUrl());
       // assertTrue(message, condition);
        assertEquals(true, q.getMessages(10).size() > 0);
    
    }

    private GxQueue sendMsgs(String url) {
        GxQueue queue = GxQueue.create(url);
        ArrayList<GxMessageContent> contents = new ArrayList<>();
        for (int i = 0 ; i < 10; i++) {
            GxMessageContent m = new GxMessageContent();
            m.setId(String.valueOf(i));
            m.setContents("Message " + m.getId());
            contents.add(m);
        }
        queue.sendMessages(contents, "groupTest");
        return queue;
    }

    private boolean removeMsgs(GxQueue q) {

        boolean messagesInQueue = false;
        while (true)
        {
            for (GxQueueMessage m : q.getMessages(10)) {
                q.removeMessage(m.getReceiptId());
                messagesInQueue = true;
             }
             if (!messagesInQueue)
                return true;
             messagesInQueue = false;
         }
    }
   
    @Test
    public void consumeMessages()
    {
        // send some messages
        GxQueue q = sendMsgs(getUrl());
        // see if we have them on the queue
        assertEquals(true, q.getMessages(10).size() > 0);
        
    }

    @Test
    public void removeMessages()
    {
        // Send 10 messages to ensure messages in the queue
        GxQueue q = sendMsgs(getUrl());

        // remove all them
        int actualRead = q.getMessages(10).size();
        if (actualRead > 0)
            removeMsgs(q);
        assertEquals(0, q.getMessages(10).size());
    }
}
