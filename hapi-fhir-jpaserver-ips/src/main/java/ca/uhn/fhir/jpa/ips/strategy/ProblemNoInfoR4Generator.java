package ca.uhn.fhir.jpa.ips.strategy;

import ca.uhn.fhir.jpa.ips.api.INoInfoGenerator;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Condition;
import org.hl7.fhir.r4.model.Reference;

public class ProblemNoInfoR4Generator implements INoInfoGenerator {
	@Override
	public IBaseResource generate(IIdType theSubjectId) {
		Condition condition = new Condition();
		condition
			.setCode(new CodeableConcept()
				.addCoding(new Coding()
					.setCode("no-problem-info")
					.setSystem("http://hl7.org/fhir/uv/ips/CodeSystem/absent-unknown-uv-ips")
					.setDisplay("No information about problems")))
			.setSubject(new Reference(theSubjectId))
			.setClinicalStatus(new CodeableConcept()
				.addCoding(new Coding()
					.setCode("active")
					.setSystem("http://terminology.hl7.org/CodeSystem/condition-clinical")));
		return condition;
	}
}
