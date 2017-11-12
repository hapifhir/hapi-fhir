package ca.uhn.fhir.jpa.cqf.ruler.builders;

import org.hl7.fhir.dstu3.model.*;
import org.hl7.fhir.exceptions.FHIRException;

import java.util.ArrayList;
import java.util.List;

public class CarePlanActivityDetailBuilder extends BaseBuilder<CarePlan.CarePlanActivityDetailComponent> {

    public CarePlanActivityDetailBuilder() {
        super(new CarePlan.CarePlanActivityDetailComponent());
    }

    public CarePlanActivityDetailBuilder buildCategory(CodeableConcept category) {
        complexProperty.setCategory(category);
        return this;
    }

    public CarePlanActivityDetailBuilder buildDefinition(Reference reference) {
        complexProperty.setDefinition(reference);
        return this;
    }

    public CarePlanActivityDetailBuilder buildCode(CodeableConcept code) {
        complexProperty.setCode(code);
        return this;
    }

    public CarePlanActivityDetailBuilder buildReasonCode(List<CodeableConcept> concepts) {
        complexProperty.setReasonCode(concepts);
        return this;
    }

    public CarePlanActivityDetailBuilder buildReasonCode(CodeableConcept concept) {
        if (!complexProperty.hasReasonCode()) {
            complexProperty.setReasonCode(new ArrayList<>());
        }

        complexProperty.addReasonCode(concept);
        return this;
    }

    public CarePlanActivityDetailBuilder buildReasonReference(List<Reference> references) {
        complexProperty.setReasonReference(references);
        return this;
    }

    public CarePlanActivityDetailBuilder buildReasonReference(Reference reference) {
        if (!complexProperty.hasReasonReference()) {
            complexProperty.setReasonReference(new ArrayList<>());
        }

        complexProperty.addReasonReference(reference);
        return this;
    }

    public CarePlanActivityDetailBuilder buildGoal(List<Reference> goals) {
        complexProperty.setGoal(goals);
        return this;
    }

    public CarePlanActivityDetailBuilder buildGoal(Reference goal) {
        if (!complexProperty.hasGoal()) {
            complexProperty.setGoal(new ArrayList<>());
        }

        complexProperty.addGoal(goal);
        return this;
    }

    // required
    public CarePlanActivityDetailBuilder buildStatus(CarePlan.CarePlanActivityStatus status) {
        complexProperty.setStatus(status);
        return this;
    }

    // String overload
    public CarePlanActivityDetailBuilder buildStatus(String status) throws FHIRException {
        complexProperty.setStatus(CarePlan.CarePlanActivityStatus.fromCode(status));
        return this;
    }

    public CarePlanActivityDetailBuilder buildStatusReason(String reason) {
        complexProperty.setStatusReason(reason);
        return this;
    }

    public CarePlanActivityDetailBuilder buildProhibited(boolean prohibited) {
        complexProperty.setProhibited(prohibited);
        return this;
    }

    // Type is one of the following: Timing, Period, or String
    public CarePlanActivityDetailBuilder buildScheduled(Type type) {
        complexProperty.setScheduled(type);
        return this;
    }

    public CarePlanActivityDetailBuilder buildLocation(Reference location) {
        complexProperty.setLocation(location);
        return this;
    }

    public CarePlanActivityDetailBuilder buildPerformer(List<Reference> performers) {
        complexProperty.setPerformer(performers);
        return this;
    }

    public CarePlanActivityDetailBuilder buildPerformer(Reference performer) {
        if (!complexProperty.hasPerformer()) {
            complexProperty.setPerformer(new ArrayList<>());
        }

        complexProperty.addPerformer(performer);
        return this;
    }

    // Type is one of the following: CodeableConcept or Reference
    public CarePlanActivityDetailBuilder buildProduct(Type type) {
        complexProperty.setProduct(type);
        return this;
    }

    public CarePlanActivityDetailBuilder buildDailyAmount(SimpleQuantity amount) {
        complexProperty.setDailyAmount(amount);
        return this;
    }

    public CarePlanActivityDetailBuilder buildQuantity(SimpleQuantity quantity) {
        complexProperty.setQuantity(quantity);
        return this;
    }

    public CarePlanActivityDetailBuilder buildDescription(String description) {
        complexProperty.setDescription(description);
        return this;
    }
}
