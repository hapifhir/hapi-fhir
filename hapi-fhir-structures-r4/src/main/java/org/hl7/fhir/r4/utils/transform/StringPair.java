package org.hl7.fhir.r4.utils.transform;

/**
 * String pair class
 */
public class StringPair {
  /**
   * Variable string
   */
  private String var;
  /**
   * Descriptor string
   */
  private String desc;

  /**
   * Constructor
   *
   * @param var  initial variable value
   * @param desc initial descriptor value
   */
  @SuppressWarnings("WeakerAccess")
  public StringPair(String var, String desc) {
    super();
    this.var = var;
    this.desc = desc;
  }

  /**
   * get accessor for var
   *
   * @return var string value
   */
  public String getVar() {
    return var;
  }

  /**
   * get accessor for desc
   *
   * @return desc string value
   */
  public String getDesc() {
    return desc;
  }
}
