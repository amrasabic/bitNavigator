package utillities;

import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.resource.factory.MessageFactory;
import com.twilio.sdk.resource.instance.Message;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import play.Logger;

import java.util.ArrayList;
import java.util.List;
 
public class SMS {
 
  // Find your Account Sid and Token at twilio.com/user/account
  public static final String ACCOUNT_SID = "ACdab715965c26ce08f3283d2a34f624c5";
  public static final String AUTH_TOKEN = "efeca99b3ddfd7f11bb91aa59c554a09";
 
  public static void sendSMS(String phoneNumber, String message) {
    TwilioRestClient client = new TwilioRestClient(ACCOUNT_SID, AUTH_TOKEN);
    // Build a filter for the MessageList
    List<NameValuePair> params = new ArrayList<NameValuePair>();
    params.add(new BasicNameValuePair("Body", message));
    params.add(new BasicNameValuePair("To", phoneNumber));
    params.add(new BasicNameValuePair("From", "+12056321116"));

    MessageFactory messageFactory = client.getAccount().getMessageFactory();
    try {
        Message msg = messageFactory.create(params);
    } catch (TwilioRestException e) {
        Logger.error(e.getMessage());
    }

  }
}