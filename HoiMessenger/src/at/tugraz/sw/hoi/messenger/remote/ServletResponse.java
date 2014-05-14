package at.tugraz.sw.hoi.messenger.remote;

public class ServletResponse {

  public enum Status {
    SUCCESS, FAILURE
  }

  private Status status;
  private String message;

  public ServletResponse(Status status, String message) {
    super();
    this.status = status;
    this.message = message;
  }

  public Status getStatus() {
    return status;
  }

  public String getMessage() {
    return message;
  }

}
