package ca.uhn.hapi.fhir.cdshooks.svc.cr;

import ca.uhn.fhir.model.api.IModelJson;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceResponseJson;
import org.hl7.fhir.dstu3.model.Parameters;

public class CdsCrResolverStu3 {
	public Parameters encodeParams(IModelJson theJson) {
		var retVal = new Parameters();
		return retVal;
	}

	public CdsServiceResponseJson encodeResponse(Object theResponse) {
		var retVal = new CdsServiceResponseJson();
		return retVal;
	}
}
