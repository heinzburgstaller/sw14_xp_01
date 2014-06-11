package at.tugraz.sw.hoi.messenger.otr;

import java.security.KeyPair;

import net.java.otr4j.OtrEngineHost;
import net.java.otr4j.OtrKeyManager;
import net.java.otr4j.OtrPolicy;
import net.java.otr4j.session.SessionID;
import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.util.Log;

public class HoiOtrEngineHost implements OtrEngineHost {

  private OtrPolicy policy;
  public String lastInjectedMessage;
  private OtrKeyManager keyManager;

  public HoiOtrEngineHost(OtrPolicy policy, SharedPreferences prefs) {
    this.policy = policy;
    this.keyManager = new HoiOtrKeyManager(prefs);
  }

  public OtrPolicy getSessionPolicy(SessionID ctx) {
    return this.policy;
  }

  public void injectMessage(SessionID sessionID, String msg) {
    this.lastInjectedMessage = msg;
    String msgDisplay = (msg.length() > 10) ? msg.substring(0, 10) + "..." : msg;
    Log.d("OTR", "IM injects message: " + msgDisplay);
  }

  public void showError(SessionID sessionID, String error) {
    Log.d("OTR", "IM shows error to user: " + error);
  }

  public void showWarning(SessionID sessionID, String warning) {
    Log.d("OTR", "IM shows warning to user: " + warning);
  }

  public void sessionStatusChanged(SessionID sessionID) {
    // keyManager.savePublicKey(sessionID,);
  }

  @SuppressLint("TrulyRandom")
  public KeyPair getKeyPair(SessionID paramSessionID) {

    KeyPair kp = keyManager.loadLocalKeyPair(paramSessionID);

    if (kp == null) {
      keyManager.generateLocalKeyPair(paramSessionID);
      kp = keyManager.loadLocalKeyPair(paramSessionID);
    }

    return kp;

  }
}
