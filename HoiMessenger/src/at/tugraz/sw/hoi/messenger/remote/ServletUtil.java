package at.tugraz.sw.hoi.messenger.remote;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.util.Log;
import at.tugraz.sw.hoi.messenger.remote.ServletResponse.Status;

/**
 * Parameters: in at.tugraz.sw.hoi.messenger.remote
 * 
 * @author sw
 * 
 */
public class ServletUtil {

  public static final String TAG = "ServletUtil";

  public static final String REGISTER = "register";
  public static final String UNREGISTER = "register";
  public static final String CHAT = "chat";

  public static ServletResponse register(String email, String regId) {
    return post(REGISTER, new Parameter(Configuration.EMAIL, email), new Parameter(Configuration.REG_ID, regId));
  }

  public static ServletResponse unregister(String email) {

    return post(UNREGISTER, new Parameter(Configuration.EMAIL, email));
  }

  public static ServletResponse chat(String msg, String from, String to) {
    ServletResponse response = post(CHAT, new Parameter(Configuration.MSG, msg),
        new Parameter(Configuration.FROM, from), new Parameter(Configuration.TO, to));
    return response;
  }

  private static ServletResponse post(String servlet, Parameter... parameters) {
    URL url;
    HttpURLConnection connection = null;

    try {
      url = new URL(Configuration.SERVER_URL + servlet);
      byte[] bytes = convertParametersToBody(parameters);
      connection = (HttpURLConnection) url.openConnection();
      connection.setDoOutput(true);
      connection.setUseCaches(false);
      connection.setFixedLengthStreamingMode(bytes.length);
      connection.setRequestMethod("POST");
      connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");

      OutputStream out = connection.getOutputStream();
      out.write(bytes);
      out.close();

      String resp = getStringFromInputStream(connection.getInputStream());
      Status status = Status.FAILURE;

      if (resp.startsWith(Configuration.SUCCESS)) {
        status = Status.SUCCESS;
      } else {
        Log.d(TAG, "Post - Error from Server");
      }

      return new ServletResponse(status, resp);
    } catch (MalformedURLException e) {
      return new ServletResponse(Status.FAILURE, e.getMessage());
    } catch (IOException e) {
      return new ServletResponse(Status.FAILURE, e.getMessage());
    } finally {
      if (connection != null) {
        connection.disconnect();
      }
    }
  }

  private static byte[] convertParametersToBody(Parameter... parameters) {
    StringBuilder bodyBuilder = new StringBuilder();

    for (Parameter p : parameters) {
      bodyBuilder.append(p.getName()).append('=').append(p.getValue());
      bodyBuilder.append('&');
    }

    if (parameters.length > 0) {
      bodyBuilder.deleteCharAt(bodyBuilder.length() - 1);
    }

    String body = bodyBuilder.toString();
    // Log.v(TAG, "Posting '" + body + "' to " + url);
    return body.getBytes();
  }

  // convert InputStream to String
  private static String getStringFromInputStream(InputStream is) {

    BufferedReader br = null;
    StringBuilder sb = new StringBuilder();

    String line;
    try {

      br = new BufferedReader(new InputStreamReader(is));
      while ((line = br.readLine()) != null) {
        sb.append(line);
      }

    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (br != null) {
        try {
          br.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }

    return sb.toString();

  }

}
