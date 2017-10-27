//
// Translated by CS2J (http://www.cs2j.com): 8/18/2017 3:07:36 PM
//

package org.hl7.fhir.r4.utils.transform.deserializer;


/**
* Stored Antls Search rule parsed data
*/
public class UrlSearch
{
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
  */
  public UrlSearch(String name, String value) throws Exception {
    this.Name = name;
    this.Value = value;
  }

  public static int compare(UrlSearch a, UrlSearch b) throws Exception {
    int retVal = a.Name.compareTo(b.Name);
    if (retVal != 0)
    return retVal;

    return a.Value.compareTo(b.Value);
  }

}
