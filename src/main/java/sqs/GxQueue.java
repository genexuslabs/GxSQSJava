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
import com.amazonaws.services.sqs.model.SendMessageBatchResult;

import util.MyLogger;


public class GxQueue {
    
    private static final Logger logger = Logger.getLogger(MyLogger.class.getName());
    private String mURL;
    private final AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();
    private boolean mIsFifo;

    private void setURL(String url) {
        mURL = url;
        mIsFifo = mURL.contains(".fifo");
    }

    public static GxQueue create(String url) {
        GxQueue q = new GxQueue();
        q.setURL(url);
        return q;
    }

    public ArrayList<GxQueueMessage> getMessages(int maxCount) {
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

    private SendMessageBatchRequestEntry createMessage(GxMessageContent m, String groupId) {
        SendMessageBatchRequestEntry entry = new SendMessageBatchRequestEntry(m.getId(), m.getContents());
        if (mIsFifo) {
            return entry.withMessageGroupId(groupId)
            .withMessageDeduplicationId(m.getId());
        }
        return entry;
    }

    public boolean sendMessages(ArrayList<GxMessageContent> messageContents, String groupId) {
        List<SendMessageBatchRequestEntry> entries = new ArrayList<>();
        for (GxMessageContent m : messageContents) {
            boolean add = entries.add(createMessage(m, groupId));
            if (!add) {
                return false;
            }
        }
        try {
            SendMessageBatchRequest request = new SendMessageBatchRequest()
            .withQueueUrl(mURL)
            .withEntries(entries);
            SendMessageBatchResult res = sqs.sendMessageBatch(request);
            return (!res.getFailed().isEmpty());
        } catch (Exception e) {
            logger.severe(e.getMessage());
            return false;
        }
    }
}


