package at.tugraz.sw.hoi.messenger.otr;

import java.util.HashMap;
import java.util.Map;

import net.java.otr4j.OtrPolicy;
import net.java.otr4j.OtrPolicyImpl;
import net.java.otr4j.session.SessionID;

public class HoiOtrUtil {

  public static final String PROTOCOL = "HOI";

  private static HoiOtrUtil instance = null;

  private Map<String, SessionID> sessions = new HashMap<String, SessionID>();
  private Map<SessionID, HoiOtrEngine> engines = new HashMap<SessionID, HoiOtrEngine>();

  public SessionID getSessionId(String fromEmail, String toEmail) {
    SessionID sessionID = sessions.get(toEmail);
    if (sessionID != null) {
      return sessionID;
    }

    sessionID = new SessionID(fromEmail, toEmail, PROTOCOL);
    sessions.put(toEmail, sessionID);
    return sessionID;
  }

  public HoiOtrEngine getEngine(SessionID sessionID) {
    HoiOtrEngine engine = engines.get(sessionID);
    if (engine != null) {
      return engine;
    }

    HoiOtrEngineHost host = new HoiOtrEngineHost(new OtrPolicyImpl(OtrPolicy.ALLOW_V2 | OtrPolicy.ERROR_START_AKE));
    engine = new HoiOtrEngine(host);
    engines.put(sessionID, engine);
    return engine;
  }

  protected HoiOtrUtil() {

  }

  public static HoiOtrUtil getInstance() {
    if (instance == null) {
      instance = new HoiOtrUtil();
    }
    return instance;
  }
}
