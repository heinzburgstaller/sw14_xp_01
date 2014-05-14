package at.tugraz.sw.hoi.messenger.remote;

import at.tugraz.sw.hoi.messenger.remote.ServletResponse.Status;

public class ServletUtil {

  /*
   * Parameters: in at.tugraz.sw.hoi.messenger.remote
   */

  public static ServletResponse register(String email, String regId) {
    return new ServletResponse(Status.FAILURE, "");
  }

}
