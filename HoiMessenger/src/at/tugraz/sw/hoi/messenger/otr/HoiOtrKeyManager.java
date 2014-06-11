package at.tugraz.sw.hoi.messenger.otr;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import net.java.otr4j.OtrKeyManager;
import net.java.otr4j.OtrKeyManagerListener;
import net.java.otr4j.OtrKeyManagerStore;
import net.java.otr4j.crypto.OtrCryptoEngineImpl;
import net.java.otr4j.crypto.OtrCryptoException;
import net.java.otr4j.session.SessionID;

import org.bouncycastle.util.encoders.Base64;

import android.content.SharedPreferences;

public class HoiOtrKeyManager implements OtrKeyManager {

  private OtrKeyManagerStore store;

  public HoiOtrKeyManager(OtrKeyManagerStore store) {
    this.store = store;
  }

  public HoiOtrKeyManager(SharedPreferences prefs) throws IOException {
    this.store = new HoiOtrKeyManagerStore(prefs);
  }

  private List<OtrKeyManagerListener> listeners = new Vector<OtrKeyManagerListener>();

  public void addListener(OtrKeyManagerListener l) {
    synchronized (listeners) {
      if (!listeners.contains(l))
        listeners.add(l);
    }
  }

  public void removeListener(OtrKeyManagerListener l) {
    synchronized (listeners) {
      listeners.remove(l);
    }
  }

  public void generateLocalKeyPair(SessionID sessionID) {
    if (sessionID == null)
      return;

    String accountID = sessionID.getAccountID();
    KeyPair keyPair;
    try {
      keyPair = KeyPairGenerator.getInstance("DSA").genKeyPair();
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
      return;
    }

    // Store Public Key.
    PublicKey pubKey = keyPair.getPublic();
    X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(pubKey.getEncoded());

    this.store.setProperty(accountID + ".publicKey", x509EncodedKeySpec.getEncoded());

    // Store Private Key.
    PrivateKey privKey = keyPair.getPrivate();
    PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(privKey.getEncoded());

    this.store.setProperty(accountID + ".privateKey", pkcs8EncodedKeySpec.getEncoded());
  }

  public String getLocalFingerprint(SessionID sessionID) {
    KeyPair keyPair = loadLocalKeyPair(sessionID);

    if (keyPair == null)
      return null;

    PublicKey pubKey = keyPair.getPublic();

    try {
      return new OtrCryptoEngineImpl().getFingerprint(pubKey);
    } catch (OtrCryptoException e) {
      e.printStackTrace();
      return null;
    }
  }

  public String getRemoteFingerprint(SessionID sessionID) {
    PublicKey remotePublicKey = loadRemotePublicKey(sessionID);
    if (remotePublicKey == null)
      return null;
    try {
      return new OtrCryptoEngineImpl().getFingerprint(remotePublicKey);
    } catch (OtrCryptoException e) {
      e.printStackTrace();
      return null;
    }
  }

  public boolean isVerified(SessionID sessionID) {
    if (sessionID == null)
      return false;

    return this.store.getPropertyBoolean(sessionID.getUserID() + ".publicKey.verified", false);
  }

  public KeyPair loadLocalKeyPair(SessionID sessionID) {
    if (sessionID == null)
      return null;

    String accountID = sessionID.getAccountID();
    // Load Private Key.
    byte[] b64PrivKey = this.store.getPropertyBytes(accountID + ".privateKey");
    if (b64PrivKey == null)
      return null;

    PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(b64PrivKey);

    // Load Public Key.
    byte[] b64PubKey = this.store.getPropertyBytes(accountID + ".publicKey");
    if (b64PubKey == null)
      return null;

    X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(b64PubKey);

    PublicKey publicKey;
    PrivateKey privateKey;

    // Generate KeyPair.
    KeyFactory keyFactory;
    try {
      keyFactory = KeyFactory.getInstance("DSA");
      publicKey = keyFactory.generatePublic(publicKeySpec);
      privateKey = keyFactory.generatePrivate(privateKeySpec);
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
      return null;
    } catch (InvalidKeySpecException e) {
      e.printStackTrace();
      return null;
    }

    return new KeyPair(publicKey, privateKey);
  }

  public PublicKey loadRemotePublicKey(SessionID sessionID) {
    if (sessionID == null)
      return null;

    String userID = sessionID.getUserID();

    byte[] b64PubKey = this.store.getPropertyBytes(userID + ".publicKey");
    if (b64PubKey == null)
      return null;

    X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(b64PubKey);

    // Generate KeyPair.
    KeyFactory keyFactory;
    try {
      keyFactory = KeyFactory.getInstance("DSA");
      return keyFactory.generatePublic(publicKeySpec);
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
      return null;
    } catch (InvalidKeySpecException e) {
      e.printStackTrace();
      return null;
    }
  }

  public void savePublicKey(SessionID sessionID, PublicKey pubKey) {
    if (sessionID == null)
      return;

    X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(pubKey.getEncoded());

    String userID = sessionID.getUserID();
    this.store.setProperty(userID + ".publicKey", x509EncodedKeySpec.getEncoded());

    this.store.removeProperty(userID + ".publicKey.verified");
  }

  public void unverify(SessionID sessionID) {
    if (sessionID == null)
      return;

    if (!isVerified(sessionID))
      return;

    this.store.removeProperty(sessionID.getUserID() + ".publicKey.verified");

    for (OtrKeyManagerListener l : listeners)
      l.verificationStatusChanged(sessionID);

  }

  public void verify(SessionID sessionID) {
    if (sessionID == null)
      return;

    if (this.isVerified(sessionID))
      return;

    this.store.setProperty(sessionID.getUserID() + ".publicKey.verified", true);

    for (OtrKeyManagerListener l : listeners)
      l.verificationStatusChanged(sessionID);
  }

}
