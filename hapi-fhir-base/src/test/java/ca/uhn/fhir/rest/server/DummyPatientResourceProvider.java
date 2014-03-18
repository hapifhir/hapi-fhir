package ca.uhn.fhir.rest.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.uhn.fhir.model.dstu.composite.CodingDt;
import ca.uhn.fhir.model.dstu.composite.HumanNameDt;
import ca.uhn.fhir.model.dstu.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.dstu.valueset.IdentifierUseEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.model.primitive.UriDt;
import ca.uhn.fhir.rest.annotation.Include;
import ca.uhn.fhir.rest.annotation.Optional;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.Required;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.param.CodingListParam;
import ca.uhn.fhir.rest.param.QualifiedDateParam;

/**
 * Created by dsotnikov on 2/25/2014.
 */
public class DummyPatientResourceProvider implements IResourceProvider {

	public Map<String, Patient> getIdToPatient() {
		Map<String, Patient> idToPatient = new HashMap<String, Patient>();
		{
			Patient patient = new Patient();
			patient.addIdentifier();
			patient.getIdentifier().get(0).setUse(IdentifierUseEnum.OFFICIAL);
			patient.getIdentifier().get(0).setSystem(new UriDt("urn:hapitest:mrns"));
			patient.getIdentifier().get(0).setValue("00001");
			patient.addName();
			patient.getName().get(0).addFamily("Test");
			patient.getName().get(0).addGiven("PatientOne");
			patient.getGender().setText("M");
			idToPatient.put("1", patient);
		}
		{
			Patient patient = new Patient();
			patient.getIdentifier().add(new IdentifierDt());
			patient.getIdentifier().get(0).setUse(IdentifierUseEnum.OFFICIAL);
			patient.getIdentifier().get(0).setSystem(new UriDt("urn:hapitest:mrns"));
			patient.getIdentifier().get(0).setValue("00002");
			patient.getName().add(new HumanNameDt());
			patient.getName().get(0).addFamily("Test");
			patient.getName().get(0).addGiven("PatientTwo");
			patient.getGender().setText("F");
			idToPatient.put("2", patient);
		}
		return idToPatient;
	}

	@Search()
	public Patient getPatient(@Required(name = Patient.SP_IDENTIFIER) IdentifierDt theIdentifier) {
		for (Patient next : getIdToPatient().values()) {
			for (IdentifierDt nextId : next.getIdentifier()) {
				if (nextId.matchesSystemAndValue(theIdentifier)) {
					return next;
				}
			}
		}
		return null;
	}
	
	@Search()
	public Patient getPatientWithDOB(@Required(name = "dob") QualifiedDateParam theDob) {
		Patient next = getIdToPatient().get("1");
		if (theDob.getComparator()!=null) {
			next.addIdentifier().setValue(theDob.getComparator().getCode());
		}else {
			next.addIdentifier().setValue("NONE");
		}
		next.addIdentifier().setValue(theDob.getValueAsString());
		return next;
	}	

	@Search()
	public List<Patient> getPatientWithOptionalName(@Required(name = "name1") StringDt theName1, @Optional(name = "name2") StringDt theName2) {
		List<Patient> retVal = new ArrayList<Patient>();
		Patient next = getIdToPatient().get("1");
		next.getName().get(0).getFamily().set(0, theName1);
		if (theName2 != null) {
			next.getName().get(0).getGiven().set(0, theName2);
		}
		retVal.add(next);

		return retVal;
	}

	@Search()
	public Patient getPatientWithIncludes(@Required(name = "withIncludes") StringDt theString, @Include List<String> theIncludes) {
		Patient next = getIdToPatient().get("1");
		
		for (String line : theIncludes) {
			next.addAddress().addLine(line);
		}
		
		return next;
	}

	
	@Search()
	public List<Patient> getPatientMultipleIdentifiers(@Required(name = "ids") CodingListParam theIdentifiers) {
		List<Patient> retVal = new ArrayList<Patient>();
		Patient next = getIdToPatient().get("1");
		
		for (CodingDt nextId : theIdentifiers.getCodings()) {
			next.getIdentifier().add(new IdentifierDt(nextId.getSystem().getValueAsString(), nextId.getCode().getValue()));
		}
		
		retVal.add(next);

		return retVal;
	}

	/**
	 * Retrieve the resource by its identifier
	 * 
	 * @param theId
	 *            The resource identity
	 * @return The resource
	 */
	@Read()
	public Patient getResourceById(@Read.IdParam IdDt theId) {
		return getIdToPatient().get(theId.getValue());
	}

	@Read()
	public Patient getResourceById(@Read.IdParam IdDt theId, @Read.VersionIdParam IdDt theVersionId) {
		Patient retVal = getIdToPatient().get(theId.getValue());
		retVal.getName().get(0).setText(theVersionId.getValue());
		return retVal;
	}

	@Search()
	public Collection<Patient> getResources() {
		return getIdToPatient().values();
	}

	@Override
	public Class<Patient> getResourceType() {
		return Patient.class;
	}

}
