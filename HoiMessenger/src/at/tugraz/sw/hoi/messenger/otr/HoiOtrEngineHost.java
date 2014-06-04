package at.tugraz.sw.hoi.messenger.otr;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

import net.java.otr4j.OtrEngineHost;
import net.java.otr4j.OtrPolicy;
import net.java.otr4j.session.SessionID;
import android.annotation.SuppressLint;
import android.util.Log;

public class HoiOtrEngineHost implements OtrEngineHost {

  private OtrPolicy policy;
  public String lastInjectedMessage;

  public HoiOtrEngineHost(OtrPolicy policy) {
    this.policy = policy;
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
    // don't care.
  }

  @SuppressLint("TrulyRandom")
  public KeyPair getKeyPair(SessionID paramSessionID) {
    KeyPairGenerator kg;
    try {
      kg = KeyPairGenerator.getInstance("DSA");

    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
      return null;
    }

    return kg.genKeyPair();
  }

}
