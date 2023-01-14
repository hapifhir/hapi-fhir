package ca.uhn.fhir.jpa.ips.generator;

import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.TokenParam;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IIdType;

public interface IIpsGeneratorSvc {

	/**
	 * Generates an IPS document and returns the complete document bundle
	 * for the given patient by ID
	 */
	IBaseBundle generateIps(RequestDetails theRequestDetails, IIdType thePatientId);

	/**
	 * Generates an IPS document and returns the complete document bundle
	 * for the given patient by identifier
	 */
	IBaseBundle generateIps(RequestDetails theRequestDetails, TokenParam thePatientIdentifier);
}
