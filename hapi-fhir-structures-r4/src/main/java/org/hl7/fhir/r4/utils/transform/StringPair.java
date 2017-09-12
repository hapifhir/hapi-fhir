package org.hl7.fhir.r4.utils.transform;

public class StringPair {
  private String var;
  private String desc;

  public StringPair(String var, String desc) {
    super();
    this.var = var;
    this.desc = desc;
  }

  public String getVar() {
    return var;
  }

  public String getDesc() {
    return desc;
  }
}
