package at.tugraz.sw.hoi.messenger.otr;

import net.java.otr4j.OtrKeyManagerStore;

import org.bouncycastle.util.encoders.Base64;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class HoiOtrKeyManagerStore implements OtrKeyManagerStore {

  private SharedPreferences prefs;

  public HoiOtrKeyManagerStore(SharedPreferences prefs) {
    this.prefs = prefs;
  }

  @Override
  public boolean getPropertyBoolean(String field, boolean def) {
    return prefs.getBoolean(field, def);
  }

  @Override
  public byte[] getPropertyBytes(String key) {
    String prop = prefs.getString(key, "");
    return Base64.decode(prop);
  }

  @Override
  public void removeProperty(String key) {
    Editor edit = prefs.edit();
    edit.remove(key);
    edit.commit();

  }

  @Override
  public void setProperty(String key, byte[] arg1) {
    Editor edit = prefs.edit();
    edit.putString(key, new String(Base64.encode(arg1)));
    edit.commit();

  }

  @Override
  public void setProperty(String key, boolean value) {
    Editor edit = prefs.edit();
    edit.putBoolean(key, value);
    edit.commit();

  }
}
