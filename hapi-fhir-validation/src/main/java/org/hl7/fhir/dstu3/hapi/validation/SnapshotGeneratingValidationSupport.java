package org.hl7.fhir.dstu3.hapi.validation;

import ca.uhn.fhir.context.*;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.dstu3.conformance.ProfileUtilities;
import org.hl7.fhir.dstu3.context.IWorkerContext;
import org.hl7.fhir.dstu3.hapi.ctx.HapiWorkerContext;
import org.hl7.fhir.dstu3.hapi.ctx.IValidationSupport;
import org.hl7.fhir.dstu3.model.CodeSystem;
import org.hl7.fhir.dstu3.model.ElementDefinition;
import org.hl7.fhir.dstu3.model.StructureDefinition;
import org.hl7.fhir.dstu3.model.ValueSet;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.utilities.validation.ValidationMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple validation support module that handles profile snapshot generation. This is
 * separate from other funcrtions since it needs a link to a validation support
 * module itself, and it is useful to be able to pass a chain in.
 */
public class SnapshotGeneratingValidationSupport implements IValidationSupport {
	private final FhirContext myCtx;
	private final IValidationSupport myValidationSupport;

	public SnapshotGeneratingValidationSupport(FhirContext theCtx, IValidationSupport theValidationSupport) {
		Validate.notNull(theCtx);
		Validate.notNull(theValidationSupport);
		myCtx = theCtx;
		myValidationSupport = theValidationSupport;
	}

	@Override
	public ValueSet.ValueSetExpansionComponent expandValueSet(FhirContext theContext, ValueSet.ConceptSetComponent theInclude) {
		return null;
	}

	@Override
	public List<IBaseResource> fetchAllConformanceResources(FhirContext theContext) {
		return null;
	}

	@Override
	public List<StructureDefinition> fetchAllStructureDefinitions(FhirContext theContext) {
		return null;
	}

	@Override
	public CodeSystem fetchCodeSystem(FhirContext theContext, String uri) {
		return null;
	}

	@Override
	public ValueSet fetchValueSet(FhirContext theContext, String uri) {
		return null;
	}

	@Override
	public <T extends IBaseResource> T fetchResource(FhirContext theContext, Class<T> theClass, String theUri) {
		return null;
	}

	@Override
	public StructureDefinition fetchStructureDefinition(FhirContext theCtx, String theUrl) {
		return null;
	}

	@Override
	public boolean isCodeSystemSupported(FhirContext theContext, String theSystem) {
		return false;
	}

	@Override
	public StructureDefinition generateSnapshot(StructureDefinition theInput, String theUrl, String theProfileName) {
		IWorkerContext context = new HapiWorkerContext(myCtx, myValidationSupport);
		ProfileUtilities.ProfileKnowledgeProvider profileKnowledgeProvider = new MyProfileKnowledgeWorker();
		ArrayList<ValidationMessage> messages = new ArrayList<>();

		StructureDefinition base = myValidationSupport.fetchStructureDefinition(myCtx, theInput.getBaseDefinition());
		if (base == null) {
			throw new PreconditionFailedException("Unknown base definition: " + theInput.getBaseDefinition());
		}

		new ProfileUtilities(context, messages, profileKnowledgeProvider).generateSnapshot(base, theInput, theUrl, theProfileName);

		return theInput;
	}

	@Override
	public CodeValidationResult validateCode(FhirContext theContext, String theCodeSystem, String theCode, String theDisplay) {
		return null;
	}

	@Override
	public LookupCodeResult lookupCode(FhirContext theContext, String theSystem, String theCode) {
		return null;
	}

	private class MyProfileKnowledgeWorker implements ProfileUtilities.ProfileKnowledgeProvider {
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
		public BindingResolution resolveBinding(StructureDefinition def, ElementDefinition.ElementDefinitionBindingComponent binding, String path) throws FHIRException {
			return null;
		}

		@Override
		public String getLinkForProfile(StructureDefinition profile, String url) {
			return null;
		}

		@Override
		public boolean prependLinks() {
			return false;
		}
	}

}
