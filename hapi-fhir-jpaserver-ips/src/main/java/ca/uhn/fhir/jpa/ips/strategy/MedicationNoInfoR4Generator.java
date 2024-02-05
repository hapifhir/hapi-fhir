package ca.uhn.fhir.jpa.ips.strategy;

import ca.uhn.fhir.jpa.ips.api.INoInfoGenerator;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.MedicationStatement;
import org.hl7.fhir.r4.model.Reference;

public class MedicationNoInfoR4Generator implements INoInfoGenerator {
	@Override
	public IBaseResource generate(IIdType theSubjectId) {
		MedicationStatement medication = new MedicationStatement();
		// setMedicationCodeableConcept is not available
		medication
			.setMedication(new CodeableConcept()
				.addCoding(new Coding()
					.setCode("no-medication-info")
					.setSystem("http://hl7.org/fhir/uv/ips/CodeSystem/absent-unknown-uv-ips")
					.setDisplay("No information about medications")))
			.setSubject(new Reference(theSubjectId))
			.setStatus(MedicationStatement.MedicationStatementStatus.UNKNOWN);
		// .setEffective(new
		// Period().addExtension().setUrl("http://hl7.org/fhir/StructureDefinition/data-absent-reason").setValue((new Coding().setCode("not-applicable"))))
		return medication;
	}
}
