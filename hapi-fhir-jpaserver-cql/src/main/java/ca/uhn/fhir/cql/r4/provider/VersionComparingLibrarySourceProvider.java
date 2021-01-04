package ca.uhn.fhir.cql.r4.provider;

import org.cqframework.cql.cql2elm.LibrarySourceProvider;

// Expected to be obsolete by mid-2021
@Deprecated
public abstract class VersionComparingLibrarySourceProvider implements LibrarySourceProvider {
    public static int compareVersions(String version1, String version2)
    {
        // Treat null as MAX VERSION
        if (version1 == null && version2 == null) {
            return 0;
        }

        if (version1 != null && version2 == null) {
            return -1;
        }

        if (version1 == null && version2 != null) {
            return 1;
        }

        String[] string1Vals = version1.split("\\.");
        String[] string2Vals = version2.split("\\.");
    
        int length = Math.max(string1Vals.length, string2Vals.length);
    
        for (int i = 0; i < length; i++)
        {
            Integer v1 = (i < string1Vals.length)?Integer.parseInt(string1Vals[i]):0;
            Integer v2 = (i < string2Vals.length)?Integer.parseInt(string2Vals[i]):0;
    
            //Making sure Version1 bigger than version2
            if (v1 > v2)
            {
                return 1;
            }
            //Making sure Version1 smaller than version2
            else if(v1 < v2)
            {
                return -1;
            }
        }
    
        //Both are equal
        return 0;
    }
}
