//
// Translated by CS2J (http://www.cs2j.com): 8/18/2017 3:07:36 PM
//

package org.hl7.fhir.r4.utils.transform.deserializer;


public enum FhirMapUseNames
{
    /**
     'source'
     */
    NotSet,

    /**
     'source'
     */
    Source,

    /**
     'target'
     */
    Target,

    /**
     'queried'
     */
    Queried,

    /**
     'produced'
     */
    Produced;

    public static final int SIZE = java.lang.Integer.SIZE;

    public int getValue()
    {
        return this.ordinal();
    }

    public static FhirMapUseNames forValue(int value)
    {
        return values()[value];
    }
}

