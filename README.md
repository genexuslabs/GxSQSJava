# GxSQSJava

This module implements an External Object that allows access to AWS SQS (Simple Queue Services).

The implementation allows sending and receiving messages from queues defined in AWS of any type, standards or FIFO (For more detail https://docs.aws.amazon.com/AWSSimpleQueueService/latest/SQSDeveloperGuide/welcome.html)

## Access to the Message queue

An AWS queue is accessible via URL that has to be given via the AWS Console.

The recommended mechanism to access the queue is to configure access to AWS resources using AWS Cli in the environment where the sending and receiving of messages from the queue will be used.

With the following command:

aws configure

There you will ask for the Access Key and Secret Key in addition to the default region.

See https://docs.aws.amazon.com/config/latest/developerguide/security-iam.html for more details.

## Sending messages to the queue

```java
   GxQueue queue = GxQueue.create(url);
   
   // Sending 10 messages
    ArrayList<GxMessageContent> contents = new ArrayList<>();
    for (int i = 0 ; i < 10; i++) {
        GxMessageContent m = new GxMessageContent();
        m.setId(String.valueOf(i));
        m.setContents("Message " + m.getId());
        contents.add(m);
    }
   queue.sendMessages(contents, "groupTest");
   return queue;
```

