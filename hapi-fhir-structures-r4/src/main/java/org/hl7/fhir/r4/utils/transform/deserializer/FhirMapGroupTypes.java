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

    public int getValue()
    {
        return this.ordinal();
    }

    public static FhirMapGroupTypes forValue(int value)
    {
        return values()[value];
    }
}
