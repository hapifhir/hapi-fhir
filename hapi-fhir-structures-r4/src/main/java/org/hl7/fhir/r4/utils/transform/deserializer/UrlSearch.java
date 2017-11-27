package org.hl7.fhir.r4.utils.transform.deserializer;


/**
 * Stored ANTLR Search rule parsed data
 *
 * @author Travis Lukach
 * (c) Applicadia LLC.
 */
public class UrlSearch {
  /**
   * Name Authority.
   */
  public String Name = "";
  /**
   * value.Blank if not set
   */
  public String Value = "";

  /**
   * Default constructor
   */
  UrlSearch() throws Exception {
  }

  /**
   * Default constructor
   *
   * @param name  name desired to be given when UrlSearch is initialized
   * @param value Value desired to be given when UrlSearch is initialized
   * @throws Exception on bad value //TODO: Do we even the throws method in this?
   */
  @SuppressWarnings("unused")
  public UrlSearch(String name, String value) throws Exception {
    this.Name = name;
    this.Value = value;
  }

  /**
   * Compares UrlSearch objects using a String compare method
   *
   * @param a the first of compared values
   * @param b the other compared value
   * @return the int value of the comparison
   * @throws Exception on bad UrlSearch values
   */
  public static int compare(UrlSearch a, UrlSearch b) throws Exception {
    int retVal = a.Name.compareTo(b.Name);
    if (retVal != 0) {
      return retVal;
    }
    return a.Value.compareTo(b.Value);
  }

}
