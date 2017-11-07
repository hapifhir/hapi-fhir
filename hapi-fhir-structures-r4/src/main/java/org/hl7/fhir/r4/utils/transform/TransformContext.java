package org.hl7.fhir.r4.utils.transform;

public class TransformContext {
  /**
   * Contains value for the application(?)
   */
  private Object appInfo;

  /**
   * Constructor with initial appInfo
   *
   * @param appInfo
   */
  public TransformContext(Object appInfo) {
    super();
    this.appInfo = appInfo;
  }

  /**
   * get accessor for appInfo
   *
   * @return appInfo value
   */
  public Object getAppInfo() {
    return appInfo;
  }

}
