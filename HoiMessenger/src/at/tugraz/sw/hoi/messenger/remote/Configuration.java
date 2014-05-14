package at.tugraz.sw.hoi.messenger.remote;

public interface Configuration {

  // Base URL of the Demo Server (such as http://my_host:8080/gcm-demo)

  public static final String SERVER_URL = "http://sw14xp01.appspot.com/";
  public static final String SERVER_URL_2 = "http://2-dot-sw14xp01.appspot.com/";

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

  public static final String PROFILE_ID = "profile_id";
  public static final String ACTION_REGISTER = "at.tugraz.sw14_xp_01.REGISTER";
  public static final int STATUS_SUCCESS = 1;
  public static final int STATUS_FAILED = 0;
  public static final String EXTRA_STATUS = "status";
  public static final String CHAT_EMAIL_ID = "chat_email_id";
  public static final String CHAT_SENDER_ID = "sender_id_pref";

  public static final String PROPERTY_REG_ID = "registration_id";
  public static final String PROPERTY_APP_VERSION = "appVersion";

}
