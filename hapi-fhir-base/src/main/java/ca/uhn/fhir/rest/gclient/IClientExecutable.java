package ca.uhn.fhir.rest.gclient;


public interface IClientExecutable<T extends IClientExecutable<?,?>, Y> {

	Y execute();

	T encodedJson();

	T encodedXml();

	/**
	 * If set to true, the client will log the request and response to the SLF4J logger. This can be useful for
	 * debugging, but is generally not desirable in a production situation.
	 */
	T andLogRequestAndResponse(boolean theLogRequestAndResponse);

	T prettyPrint();

}
