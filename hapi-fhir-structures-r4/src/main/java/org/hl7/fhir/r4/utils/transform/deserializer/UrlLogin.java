//
// Translated by CS2J (http://www.cs2j.com): 8/18/2017 3:07:36 PM
//

package org.hl7.fhir.r4.utils.transform.deserializer;


/**
* Stored Antls Url rule parsed data
*/
public class UrlLogin
{
  /**
  * Url Login name.Blank if no login
  */
  public String Name = "";
  /**
  * Url Login password.Blank if no
  */
  String Password = "";
  /**
  * Compare this url to another one.
  *
  *  @return
  */
  public static int compare(UrlLogin a, UrlLogin b) throws Exception {
    if ((a == null) && (b == null))
    return 0;

    if (a == null)
    return -1;

    if (b == null)
    return 1;

    int retVal;
    retVal = a.Name.compareTo(b.Name);
    if (retVal != 0)
    return retVal;

    retVal = a.Password.compareTo(b.Password);
    if (retVal != 0)
    return retVal;

    return 0;
  }

}
