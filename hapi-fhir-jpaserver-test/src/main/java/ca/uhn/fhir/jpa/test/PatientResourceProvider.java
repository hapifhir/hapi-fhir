package ca.uhn.fhir.jpa.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.uhn.fhir.jpa.entity.BaseResourceProvider;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;

public class PatientResourceProvider extends BaseResourceProvider<Patient> {

	@Override
	public Class<? extends IResource> getResourceType() {
		return Patient.class;
	}
	
	@Search
	public List<Patient> searchByName(
			@Description(shortDefinition="Matches the patient's family (last) name")
			@RequiredParam(name=Patient.SP_NAME) StringDt theFamily,
			@Description(shortDefinition="Matches the patient's given (first) name")
			@OptionalParam(name=Patient.SP_GIVEN) StringDt theGiven) {
		
		Map<String, IQueryParameterType> params = new HashMap<>();
		params.put(Patient.SP_NAME, theFamily);
		params.put(Patient.SP_GIVEN, theGiven);
		
		return getDao().search(params);
	}
	
	@Search
	public List<Patient> searchByGiven(
			@Description(shortDefinition="Matches the patient's given (first) name")
			@RequiredParam(name=Patient.SP_GIVEN) StringDt theValue
			) {
		return getDao().search(Patient.SP_GIVEN, theValue);
	}

}
