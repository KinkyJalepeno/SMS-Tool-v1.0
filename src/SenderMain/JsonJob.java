package SenderMain;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class JsonJob {

    private String serverCurrentStatus;
    private String emailStatus;
    private String email2smsStatus;
    private String mySqlStatus;

    private String reply;
    private String number;
    private String cardAddress;
    private String portNumber;
    //private String activeSim;


    private Object obj;
    private JSONObject jsonObject;


    public JsonJob(String response) {

        obj = JSONValue.parse(response);
        jsonObject = (JSONObject) obj;
    }

    public void parseStatus() {

        serverCurrentStatus = (String) jsonObject.get("server_currently_status");
        emailStatus = (String) jsonObject.get("email_server_status");
        email2smsStatus = (String) jsonObject.get("email2sms_status");
        mySqlStatus = (String) jsonObject.get("my_sql_status");

    }

    public String getServerCurrentStatus(){

        return serverCurrentStatus;
    }

    public String getEmailStatus(){

        return emailStatus;
    }

    public String getEmail2smsStatus(){

        return email2smsStatus;
    }

    public String getMySqlStatus(){

        return mySqlStatus;
    }


    public void parseResponse() {

        number = (String) jsonObject.get("number");
        cardAddress = (String) jsonObject.get("card_add");
        portNumber = (String) jsonObject.get("port_num");
        reply = (String) jsonObject.get("reply");
    }

    public String getNumber(){
        return number;
    }

    public String getReply(){
        return reply;
    }

    public String getCardAddress(){
        return cardAddress;
    }

    public String getPortNumber(){
        return portNumber;
    }

//    public String getActiveSim(){
//        return activeSim;
//    }



}
