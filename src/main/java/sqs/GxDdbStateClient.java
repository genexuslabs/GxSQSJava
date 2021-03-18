package sqs;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.AttributeAction;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.AttributeValueUpdate;
import com.amazonaws.services.dynamodbv2.model.GetItemRequest;
import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class GxDdbStateClient {
    private static final AmazonDynamoDB mDdb = AmazonDynamoDBClientBuilder.defaultClient();

    public String ErrDescription = "";
    public int ErrCode = 0;

    public boolean setStatus(String tableName, String key, String status) {
       return setField(tableName, GxDdbItem.STATUS_FIELD, key, status);
    }

    public boolean setData(String tableName, String key, String data) {
        return setField(tableName, GxDdbItem.DATA_FIELD, key, data);
    }

    private boolean setField(String tableName, String fieldName, String key, String value) {
        HashMap<String,AttributeValue> item_key = new HashMap<String,AttributeValue>();
        item_key.put(GxDdbItem.ID_FIELD, new AttributeValue(key));

        HashMap<String,AttributeValueUpdate> updated_values = new HashMap<String,AttributeValueUpdate>();
        updated_values.put(fieldName, new AttributeValueUpdate(new AttributeValue(value), AttributeAction.PUT));
        updated_values.put(GxDdbItem.TIMESTAMP_FIELD, new AttributeValueUpdate(new AttributeValue(getTimestamp()), AttributeAction.PUT));

        try {
            mDdb.updateItem(tableName, item_key, updated_values);
            return true;
        } catch (ResourceNotFoundException e) {
            ErrDescription = e.getMessage();
            ErrCode = 3;
        } catch (AmazonServiceException e) {
            ErrDescription = e.getMessage();
            ErrCode = 2;
        }
        return false;
    }

    private String getTimestamp() {
        return String.valueOf(new Date().getTime());
    }

    public boolean setItem(String tableName, GxDdbItem item) {
        HashMap<String,AttributeValue> item_values = new HashMap<String,AttributeValue>();
        item_values.put(GxDdbItem.ID_FIELD, new AttributeValue(item.getId()));
        item_values.put(GxDdbItem.STATUS_FIELD, new AttributeValue(item.getStatus()));
        item_values.put(GxDdbItem.DATA_FIELD, new AttributeValue(item.getData()));
        item_values.put(GxDdbItem.TIMESTAMP_FIELD, new AttributeValue(getTimestamp()));

        try {
            mDdb.putItem(tableName, item_values);
            return true;
        } catch (ResourceNotFoundException e) {
            ErrDescription = e.getMessage();
            ErrCode = 3;
        } catch (AmazonServiceException e) {
            ErrDescription = e.getMessage();
            ErrCode = 2;
        }
        return false;
    }

    public GxDdbItem getItem(String tableName, String key) {
        HashMap<String,AttributeValue> key_to_get = new HashMap<String,AttributeValue>();
        key_to_get.put(GxDdbItem.ID_FIELD, new AttributeValue(key));

        GetItemRequest request = new GetItemRequest()
        .withKey(key_to_get)
        .withTableName(tableName);

        GxDdbItem item = new GxDdbItem();
        try {
            Map<String,AttributeValue> returned_item = mDdb.getItem(request).getItem();
            if (returned_item != null) {
                if  (returned_item.containsKey(GxDdbItem.ID_FIELD))
                    item.setId(returned_item.get(GxDdbItem.ID_FIELD).toString());

                if  (returned_item.containsKey(GxDdbItem.STATUS_FIELD))
                    item.setStatus(returned_item.get(GxDdbItem.STATUS_FIELD).toString());

                if  (returned_item.containsKey(GxDdbItem.DATA_FIELD))
                    item.setData(returned_item.get(GxDdbItem.DATA_FIELD).toString());

                if  (returned_item.containsKey(GxDdbItem.TIMESTAMP_FIELD))
                    item.setTimestamp(returned_item.get(GxDdbItem.TIMESTAMP_FIELD).toString());
            } else {
                ErrCode = 3;
                ErrDescription = "Item Not Found";
            } 
        } catch (AmazonServiceException e) {
            ErrDescription = e.getErrorMessage();
            ErrCode = 4;
        }
        return item;
    }
}
