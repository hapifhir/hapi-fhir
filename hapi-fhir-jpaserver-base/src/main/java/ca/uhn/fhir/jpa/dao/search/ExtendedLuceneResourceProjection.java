package ca.uhn.fhir.jpa.dao.search;

import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.parser.IParser;
import org.hl7.fhir.instance.model.api.IBaseResource;

/**
 * Query result when fetching full resources from Hibernate Search.
 */
public class ExtendedLuceneResourceProjection {
	final long myPid;
	final String myForcedId;
	final String myResourceString;

	public ExtendedLuceneResourceProjection(long thePid, String theForcedId, String theResourceString) {
		myPid = thePid;
		myForcedId = theForcedId;
		myResourceString = theResourceString;
	}

	public IBaseResource toResource(IParser theParser) {
		IBaseResource result = theParser.parseResource(myResourceString);

		IdDt id;
		if (myForcedId != null) {
			id = new IdDt(myForcedId);
		} else {
			id = new IdDt(myPid);
		}
		result.setId(id);

		return result;
	}
}
