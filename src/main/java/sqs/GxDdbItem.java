package sqs;

public class GxDdbItem {
    public static final String ID_FIELD = "id";
    public static final String STATUS_FIELD = "mspstatus";
    public static final String DATA_FIELD = "mspdata";
    public static final String TIMESTAMP_FIELD = "msptimestamp";

    private String m_id="";
    private String m_status="";
    private String m_data="";
    private String m_timestamp="";

    public String getId() { return m_id; }
    public void setId(String id) { m_id = id; }

    public String getStatus() { return m_status; }
    public void setStatus(String status) { m_status = status; }

    public String getData() { return m_data;}
    public void setData(String data) { m_data = data; }

    public String getTimestamp() { return m_timestamp; }
    public void setTimestamp(String timestamp) { m_timestamp = timestamp; }
}
