package ca.uhn.fhir.jpa.ips.strategy;

import ca.uhn.fhir.jpa.ips.api.INoInfoGenerator;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.AllergyIntolerance;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Reference;

public class AllergyIntoleranceNoInfoR4Generator implements INoInfoGenerator {
	@Override
	public IBaseResource generate(IIdType theSubjectId) {
		AllergyIntolerance allergy = new AllergyIntolerance();
		allergy.setCode(new CodeableConcept()
				.addCoding(new Coding()
					.setCode("no-allergy-info")
					.setSystem("http://hl7.org/fhir/uv/ips/CodeSystem/absent-unknown-uv-ips")
					.setDisplay("No information about allergies")))
			.setPatient(new Reference(theSubjectId))
			.setClinicalStatus(new CodeableConcept()
				.addCoding(new Coding()
					.setCode("active")
					.setSystem("http://terminology.hl7.org/CodeSystem/allergyintolerance-clinical")));
		return allergy;
	}
}
