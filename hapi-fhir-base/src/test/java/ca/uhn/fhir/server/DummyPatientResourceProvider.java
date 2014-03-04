package ca.uhn.fhir.server;

import java.util.HashMap;
import java.util.Map;

import ca.uhn.fhir.model.dstu.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu.composite.HumanNameDt;
import ca.uhn.fhir.model.dstu.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.dstu.valueset.IdentifierUseEnum;
import ca.uhn.fhir.model.primitive.UriDt;
import ca.uhn.fhir.server.IResourceProvider;
import ca.uhn.fhir.server.operations.GET;
import ca.uhn.fhir.server.parameters.Required;

/**
 * Created by dsotnikov on 2/25/2014.
 */
public class DummyPatientResourceProvider implements IResourceProvider<Patient> {

	private Map<Long, Patient> myIdToPatient = new HashMap<>();
	
	public DummyPatientResourceProvider() {
		{
			Patient patient = new Patient();
			patient.getIdentifier().add(new IdentifierDt());
			patient.getIdentifier().get(0).setUse(IdentifierUseEnum.OFFICIAL);
			patient.getIdentifier().get(0).setSystem(new UriDt("urn:hapitest:mrns"));
			patient.getIdentifier().get(0).setValue("00001");
			patient.getName().add(new HumanNameDt());
			patient.getName().get(0).addFamily("Test");
			patient.getName().get(0).addGiven("PatientOne");
			patient.setGender(new CodeableConceptDt());
			patient.getGender().setText("M");
			myIdToPatient.put(1L, patient);
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
			patient.setGender(new CodeableConceptDt());
			patient.getGender().setText("F");
			myIdToPatient.put(2L, patient);
		}
	}

	@GET
	public Patient getPatient(@Required(name = "identifier") IdentifierDt theIdentifier) {
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

	@Override
	public Patient getResourceById(long theId) {
		return myIdToPatient.get(theId);
	}
}
