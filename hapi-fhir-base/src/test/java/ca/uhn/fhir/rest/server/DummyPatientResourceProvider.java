package ca.uhn.fhir.rest.server;

import java.util.HashMap;
import java.util.Map;

import ca.uhn.fhir.model.dstu.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu.composite.HumanNameDt;
import ca.uhn.fhir.model.dstu.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.dstu.valueset.IdentifierUseEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.UriDt;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.operations.Search;
import ca.uhn.fhir.rest.server.parameters.Required;

/**
 * Created by dsotnikov on 2/25/2014.
 */
public class DummyPatientResourceProvider implements IResourceProvider {

	private Map<String, Patient> myIdToPatient = new HashMap<String, Patient>();
	
	public DummyPatientResourceProvider() {
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
			myIdToPatient.put("1", patient);
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
			myIdToPatient.put("2", patient);
		}
	}

	@Search()
	public Patient getPatient(@Required(name = Patient.SP_IDENTIFIER) IdentifierDt theIdentifier) {
		for (Patient next : myIdToPatient.values()) {
			for (IdentifierDt nextId : next.getIdentifier()) {
				if (nextId.matchesSystemAndValue(theIdentifier)) {
					return next;
				}
			}
		}
		return null;
	}

	@Override
	public Class<Patient> getResourceType() {
		return Patient.class;
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
		return myIdToPatient.get(theId.getValue());
	}
}
