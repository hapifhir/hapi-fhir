package ca.uhn.fhir.rest.server;

public interface IRestfulServerDefaults {

    /**
     * Should the server "pretty print" responses by default (requesting clients can always override this default by supplying an <code>Accept</code> header in the request, or a <code>_pretty</code>
     * parameter in the request URL.
     * <p>
     * The default is <code>false</code>
     * </p>
     * 
     * @return Returns the default pretty print setting
     */    
    public boolean isDefaultPrettyPrint();

}
