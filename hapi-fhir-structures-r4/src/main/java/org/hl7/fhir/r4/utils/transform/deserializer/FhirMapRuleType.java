//
// Translated by CS2J (http://www.cs2j.com): 8/18/2017 3:07:36 PM
//

package org.hl7.fhir.r4.utils.transform.deserializer;


/**
* Names that can be used in mapping 'uses ... as' clause.
*/
public class FhirMapRuleType   
{
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
         
        if ((a.Occurances == null) && (b.Occurances == null))
            return 0;
         
        if (a.Occurances == null)
            return -1;
         
        if (b.Occurances == null)
            return 1;
         
        retVal = a.Occurances.length - b.Occurances.length;
        if (retVal != 0)
            return retVal;
         
        for (int i = 0;i < a.Occurances.length;i++)
        {
            retVal = a.Occurances[i] - b.Occurances[i];
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
    public Integer[] Occurances;
}


