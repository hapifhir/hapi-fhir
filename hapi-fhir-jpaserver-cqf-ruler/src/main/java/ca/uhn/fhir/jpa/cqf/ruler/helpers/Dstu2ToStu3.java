package ca.uhn.fhir.jpa.cqf.ruler.helpers;

import ca.uhn.fhir.model.dstu2.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu2.composite.CodingDt;
import ca.uhn.fhir.model.dstu2.composite.SimpleQuantityDt;
import ca.uhn.fhir.model.dstu2.resource.MedicationOrder;
import ca.uhn.fhir.model.primitive.BooleanDt;
import org.hl7.fhir.dstu3.model.*;
import org.hl7.fhir.exceptions.FHIRException;

import java.util.ArrayList;
import java.util.List;

public class Dstu2ToStu3 {

    // MedicationRequest
    public static MedicationRequest resolveMedicationRequest(MedicationOrder order) throws FHIRException {
        /*
        *   Required fields:
        *   MedicationOrder -> MedicationRequest
        *   medication -> medication
        *   dosageInstruction (Backbone) -> Dosage (Element)
        */
        return new MedicationRequest()
                .setStatus(MedicationRequest.MedicationRequestStatus.fromCode(order.getStatus()))
                .setMedication(convertToCodeableConcept((CodeableConceptDt) order.getMedication()))
                .setDosageInstruction(convertToDosage(order.getDosageInstruction()));
    }

    private static CodeableConcept convertToCodeableConcept(CodeableConceptDt conceptDt) {
        CodeableConcept concept = new CodeableConcept().setText(conceptDt.getText() == null ? "" : conceptDt.getText());
        concept.setId(conceptDt.getElementSpecificId());
        List<Coding> codes = new ArrayList<>();
        for (CodingDt code : conceptDt.getCoding()) {
            codes.add(new Coding()
                    .setCode(code.getCode())
                    .setSystem(code.getSystem())
                    .setDisplay(code.getDisplay())
                    .setVersion(code.getVersion())
            );
        }
        return concept.setCoding(codes);
    }

    private static List<Dosage> convertToDosage(List<MedicationOrder.DosageInstruction> instructions) throws FHIRException {
        List<Dosage> dosages = new ArrayList<>();

        for (MedicationOrder.DosageInstruction dosageInstruction : instructions) {
            Dosage dosage = new Dosage();
            dosage.setText(dosageInstruction.getText());
            dosage.setAsNeeded(dosageInstruction.getAsNeeded() == null ? new BooleanType(true) : new BooleanType(((BooleanDt) dosageInstruction.getAsNeeded()).getValue()));


            Integer frequency = dosageInstruction.getTiming().getRepeat().getFrequency();
            Integer frequencyMax = dosageInstruction.getTiming().getRepeat().getFrequencyMax();

            Timing.TimingRepeatComponent repeat = new Timing.TimingRepeatComponent();
            if (frequency != null) {
                repeat.setFrequency(frequency);
            }
            if (frequencyMax != null) {
                repeat.setFrequencyMax(frequencyMax);
            }
            repeat.setPeriod(dosageInstruction.getTiming().getRepeat().getPeriod())
                    .setPeriodUnit(Timing.UnitsOfTime.fromCode(dosageInstruction.getTiming().getRepeat().getPeriodUnits()));

            Timing timing = new Timing();
            timing.setRepeat(repeat);
            dosage.setTiming(timing);

            SimpleQuantityDt quantityDt = (SimpleQuantityDt) dosageInstruction.getDose();
            dosage.setDose(new SimpleQuantity()
                    .setValue(quantityDt.getValue())
                    .setUnit(quantityDt.getUnit())
                    .setCode(quantityDt.getCode())
                    .setSystem(quantityDt.getSystem())
            );

            dosages.add(dosage);
        }

        return dosages;
    }
}
