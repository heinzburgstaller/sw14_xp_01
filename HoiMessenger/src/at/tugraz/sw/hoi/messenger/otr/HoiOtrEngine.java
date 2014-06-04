package at.tugraz.sw.hoi.messenger.otr;

import net.java.otr4j.OtrEngineImpl;

public class HoiOtrEngine extends OtrEngineImpl {

  private HoiOtrEngineHost listener;

  public HoiOtrEngine(HoiOtrEngineHost listener) {
    super(listener);
    this.listener = listener;
  }

  public HoiOtrEngineHost getListener() {
    return listener;
  }

}
