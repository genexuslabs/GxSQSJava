package sqs;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageBatchRequest;
import com.amazonaws.services.sqs.model.SendMessageBatchRequestEntry;


import util.MyLogger;


public class GxQueue {
    
    private static final Logger logger = Logger.getLogger(MyLogger.class.getName());
    private String mURL;
    private final AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();

    private void setURL(String url) {
        mURL = url;
    }

    public static GxQueue create(String url) {
        GxQueue q = new GxQueue();
        q.setURL(url);
        return q;
    }

    public List<GxQueueMessage> getMessages(int maxCount) {
        ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(mURL).withAttributeNames("All");
        receiveMessageRequest.setMaxNumberOfMessages(maxCount);
        List<Message> messages = sqs.receiveMessage(receiveMessageRequest).getMessages();
        ArrayList<GxQueueMessage> out = new ArrayList<>();
        for (Message m : messages) {
            out.add(new GxQueueMessage(m));
        }
        return out;
    }

    public void removeMessage(String receiptId) {
        sqs.deleteMessage(mURL, receiptId);
    }

    public boolean sendMessages(List<GxMessageContent> messageContents) {
       
        List<SendMessageBatchRequestEntry> entries = new ArrayList<>();
        for (GxMessageContent m : messageContents) {
            boolean add = entries.add(new SendMessageBatchRequestEntry(m.getId(), m.getContents())
            .withMessageGroupId("groupTest")
            .withMessageDeduplicationId(m.getId()));
            if (!add) {
                return false;
            }
        }
        try {
            SendMessageBatchRequest request = new SendMessageBatchRequest()
            .withQueueUrl(mURL)
            .withEntries(entries);
            
            sqs.sendMessageBatch(request);
            return true;
        } catch (Exception e) {
            logger.severe(e.getMessage());
            return false;
        }
    }
}


