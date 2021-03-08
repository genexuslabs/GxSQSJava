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
As you can see in the sample you can send batches of messages to the Queue. Consider that this can fail depending on the Queue configuration on AWS.

In addition to that consider that AWS Queues are distributed, so may occur that after sending messages to the Queue you poll the Queue and no message is returned.

## Receiving messages from the Queue

```java
   GxQueue queue = GxQueue.create(url);
   
   ArrayList<GxMessageContent> messages = queue.getMessages( 10 );  // receive a maximum of 10 messages from the Queue
```

Consider that the nature of distributed Queues even you have several messages on the Queue calls to getMessages can return from 0 to 10 messages in this case.

There are several configurations in the AWS in order to change the behavior of the Queue. For example if you have a Long Pooling Queue probably you are going to receive more messages in each call. If you configure a Short Pooling Queue then you will have more empty sets returned by getMessages.

## Removing Messages from the Queue

When you call the getMessages it returns a collection of messages, each message has an unique receipt id. With this id you can remove a message from the Queue.

```java
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
```

## Some basic functionality of AWS Queues

When you retrieve messages from the queue, AWS SQS makes them invisible to other consumers.
If the consumer removes the messages then they disappear from the queue. If they are not deleted then the messages will appear again depending on the visibility timeout configured for the queue.

See https://docs.aws.amazon.com/AWSSimpleQueueService/latest/SQSDeveloperGuide/sqs-visibility-timeout.html for further details.
