package ca.uhn.fhir.jpa.model.search;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Quantity;

import javax.validation.constraints.NotNull;

public class ObservationComponentSearchIndexData {

	private final CodeableConcept myCode;
	private Quantity myQuantity;
	private CodeableConcept myConcept;


	public ObservationComponentSearchIndexData(@NotNull Observation.ObservationComponentComponent theComponent) {
		myCode = theComponent.getCode();
		if (theComponent.getValueQuantity() != null) {
			myQuantity = theComponent.getValueQuantity();
		} else if (theComponent.getValueCodeableConcept() != null) {
			myConcept = theComponent.getValueCodeableConcept();
		} else {
//			fixme jm: new code
			throw new InternalErrorException(Msg.code(0) +
				"Ony components with Quantity or CodeableConcept values are accepted");
		}
	}


	public CodeableConcept getCode() {
		return myCode;
	}

	public Quantity getQuantity() {
		return myQuantity;
	}

	public CodeableConcept getConcept() {
		return myConcept;
	}
}
