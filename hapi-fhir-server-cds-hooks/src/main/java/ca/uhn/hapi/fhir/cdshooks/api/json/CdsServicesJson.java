package ca.uhn.hapi.fhir.cdshooks.api.json;

import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a list of CDS Hooks Service descriptors
 *
 * @see <a href="https://cds-hooks.hl7.org/ballots/2020Sep/">Version 1.1 of the CDS Hooks Specification</a>
 */
public class CdsServicesJson implements IModelJson {
	@JsonProperty("services")
	private List<CdsServiceJson> myServices;

	public CdsServicesJson addService(CdsServiceJson theCdsServiceJson) {
		if (myServices == null) {
			myServices = new ArrayList<>();
		}
		myServices.add(theCdsServiceJson);
		return this;
	}

	public CdsServicesJson removeService(CdsServiceJson theCdsServiceJson){
		if (myServices == null) {
			myServices = new ArrayList<>();
		}
		myServices.remove(theCdsServiceJson);
		return this;
	}

	public List<CdsServiceJson> getServices() {
		return myServices;
	}
}
