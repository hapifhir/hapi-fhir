package org.hl7.fhir.common.hapi.validation.validator;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.context.BaseRuntimeElementDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeCompositeDatatypeDefinition;
import ca.uhn.fhir.context.RuntimePrimitiveDatatypeDefinition;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.r5.model.ElementDefinition;
import org.hl7.fhir.r5.model.StructureDefinition;

public class ProfileKnowledgeWorkerR5 implements org.hl7.fhir.r5.conformance.ProfileUtilities.ProfileKnowledgeProvider {
    private final FhirContext myCtx;

    public ProfileKnowledgeWorkerR5(FhirContext theCtx) {
        myCtx = theCtx;
    }

    @Override
    public boolean isDatatype(String typeSimple) {
        BaseRuntimeElementDefinition<?> def = myCtx.getElementDefinition(typeSimple);
        Validate.notNull(typeSimple);
        return (def instanceof RuntimePrimitiveDatatypeDefinition) || (def instanceof RuntimeCompositeDatatypeDefinition);
    }

    @Override
    public boolean isResource(String typeSimple) {
        BaseRuntimeElementDefinition<?> def = myCtx.getElementDefinition(typeSimple);
        Validate.notNull(typeSimple);
        return def instanceof RuntimeResourceDefinition;
    }

    @Override
    public boolean hasLinkFor(String typeSimple) {
        return false;
    }

    @Override
    public String getLinkFor(String corePath, String typeSimple) {
        return null;
    }

    @Override
    public BindingResolution resolveBinding(StructureDefinition theStructureDefinition, ElementDefinition.ElementDefinitionBindingComponent theElementDefinitionBindingComponent, String theS) throws FHIRException {
        return null;
    }

    @Override
    public BindingResolution resolveBinding(StructureDefinition theStructureDefinition, String theS, String theS1) throws FHIRException {
        return null;
    }

    @Override
    public String getLinkForProfile(StructureDefinition theStructureDefinition, String theS) {
        return null;
    }

    @Override
    public boolean prependLinks() {
        return false;
    }

    @Override
    public String getLinkForUrl(String corePath, String url) {
        throw new UnsupportedOperationException(Msg.code(693));
    }

}
