//
//
//

package org.hl7.fhir.r4.utils.transform.deserializer;


import java.util.List;

/**
* Names that can be used in mapping 'uses ... as' clause.
*/
public class FhirMapRuleType   
{
  public FhirMapRuleType(String typeName, List<Integer> occurances){
    this.TypeName = typeName;
    this.Occurrences = occurances;
  }
  public FhirMapRuleType() {
  }
  /**
  * Compare two instances to each other.
  *
  *  @return
  */
  public static int compare(FhirMapRuleType a, FhirMapRuleType b) throws Exception {
    if ((a == null) && (b == null))
    return 0;

    if (a == null)
    return -1;

    if (b == null)
    return 1;

    int retVal = a.TypeName.compareTo(b.TypeName);
    if (retVal != 0)
    return retVal;

    if ((a.Occurrences == null) && (b.Occurrences == null))
    return 0;

    if (a.Occurrences == null)
    return -1;

    if (b.Occurrences == null)
    return 1;

    retVal = a.Occurrences.size() - b.Occurrences.size();
    if (retVal != 0)
    return retVal;

    for (int i = 0; i < a.Occurrences.size(); i++)
    {
      retVal = a.Occurrences.get(i) - b.Occurrences.get(i);
      if (retVal != 0)
      return retVal;

    }
    return 0;
  }

  /**
  * Type name
  */
  public String TypeName;
  /**
  * Cardinality (minimum, maximum) Zero length if unset.
  */
  public List<Integer> Occurrences;
}
