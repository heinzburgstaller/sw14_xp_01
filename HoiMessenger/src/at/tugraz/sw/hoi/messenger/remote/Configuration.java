package at.tugraz.sw.hoi.messenger.remote;

public interface Configuration {

  // Base URL of the Demo Server (such as http://my_host:8080/gcm-demo)

  public static final String SERVER_URL = "http://sw14xp01.appspot.com/";

  // Google API project id registered to use GCM.
  public static final String SENDER_ID = "773501229543";

  public static final String API_KEY = "AIzaSyBPT80qCXojVMx3nrQvORWDds8k-EYvxvY";
  public static final String EMAIL = "email";
  public static final String REG_ID = "regId";
  public static final String FROM = "senderEmail";
  public static final String TO = "receiverEmail";
  public static final String MSG = "message";

  public static final String SUCCESS = "Success!";
  public static final String FAILURE = "Failure!";
}
