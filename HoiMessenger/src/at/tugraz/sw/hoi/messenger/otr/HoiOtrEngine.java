package at.tugraz.sw.hoi.messenger.otr;

import net.java.otr4j.OtrEngineHost;
import net.java.otr4j.OtrEngineImpl;

public class HoiOtrEngine extends OtrEngineImpl {

  private OtrEngineHost listener;

  public HoiOtrEngine(OtrEngineHost listener) {
    super(listener);
    this.listener = listener;
  }

  public OtrEngineHost getListener() {
    return listener;
  }

}
