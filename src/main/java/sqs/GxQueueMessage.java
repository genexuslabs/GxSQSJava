package sqs;

import com.amazonaws.services.sqs.model.Message;

public class GxQueueMessage
{
    private Message mAWSMessage;

    public GxQueueMessage(Message m) {
        mAWSMessage = m;
	}

    public String getContents() {
        return mAWSMessage.getBody();
    }

    public String getId() {
        return mAWSMessage.getMessageId();
    }

    public String getReceiptId() {
        return mAWSMessage.getReceiptHandle();
    }
}
