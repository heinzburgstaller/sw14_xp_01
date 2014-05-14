package at.tugraz.sw.hoi.messenger.remote;

public class Parameter {

  private String name;
  private String value;

  public Parameter(String name, String value) {
    super();
    this.name = name;
    this.value = value;
  }

  public String getName() {
    return name;
  }

  public String getValue() {
    return value;
  }

}
