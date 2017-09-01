//
// Translated by CS2J (http://www.cs2j.com): 8/18/2017 3:07:35 PM
//

package org.hl7.fhir.r4.utils.transform.deserializer;


public enum FhirMapGroupTypes
{
    /**
     Unset value
     */
    NotSet,

    /**
     Group type types
     */
    Types,

    /**
     Group type type types
     */
    TypeTypes;

    public static final int SIZE = java.lang.Integer.SIZE;

    public String getValue()
    {
        switch (this){
          case Types:
            return "TYPES";
          case TypeTypes:
            return "TYPE+TYPES";
          default:
            return "?";
        }
    }

    public static FhirMapGroupTypes forValue(int value)
    {
        return values()[value];
    }
}
