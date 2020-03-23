package org.hl7.fhir.common.hapi.validation.support;

import ca.uhn.fhir.context.BaseRuntimeElementDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeCompositeDatatypeDefinition;
import ca.uhn.fhir.context.RuntimePrimitiveDatatypeDefinition;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.common.hapi.validation.validator.ProfileKnowledgeWorkerR5;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.utilities.validation.ValidationMessage;

import java.util.ArrayList;

/**
 * Simple validation support module that handles profile snapshot generation.
 * <p>
 * This module currently supports the following FHIR versions:
 * <ul>
 *    <li>DSTU3</li>
 *    <li>R4</li>
 *    <li>R5</li>
 * </ul>
 */
public class SnapshotGeneratingValidationSupport implements IValidationSupport {
	private final FhirContext myCtx;

	/**
	 * Constructor
	 */
	public SnapshotGeneratingValidationSupport(FhirContext theCtx) {
		Validate.notNull(theCtx);
		myCtx = theCtx;
	}


	@Override
	public IBaseResource generateSnapshot(IValidationSupport theValidationSupport, IBaseResource theInput, String theUrl, String theWebUrl, String theProfileName) {

		assert theInput.getStructureFhirVersionEnum() == myCtx.getVersion().getVersion();
		switch (theInput.getStructureFhirVersionEnum()) {
			case DSTU3: {
				org.hl7.fhir.dstu3.model.StructureDefinition input = (org.hl7.fhir.dstu3.model.StructureDefinition) theInput;
				org.hl7.fhir.dstu3.context.IWorkerContext context = new org.hl7.fhir.dstu3.hapi.ctx.HapiWorkerContext(myCtx, theValidationSupport);
				org.hl7.fhir.dstu3.conformance.ProfileUtilities.ProfileKnowledgeProvider profileKnowledgeProvider = new MyProfileKnowledgeWorkerDstu3();
				ArrayList<ValidationMessage> messages = new ArrayList<>();
				org.hl7.fhir.dstu3.model.StructureDefinition base = (org.hl7.fhir.dstu3.model.StructureDefinition) theValidationSupport.fetchStructureDefinition(input.getBaseDefinition());
				if (base == null) {
					throw new PreconditionFailedException("Unknown base definition: " + input.getBaseDefinition());
				}
				new org.hl7.fhir.dstu3.conformance.ProfileUtilities(context, messages, profileKnowledgeProvider).generateSnapshot(base, input, theUrl, theProfileName);
				break;
			}
			case R4: {
				org.hl7.fhir.r4.model.StructureDefinition input = (org.hl7.fhir.r4.model.StructureDefinition) theInput;
				org.hl7.fhir.r4.context.IWorkerContext context = new org.hl7.fhir.r4.hapi.ctx.HapiWorkerContext(myCtx, theValidationSupport);
				org.hl7.fhir.r4.conformance.ProfileUtilities.ProfileKnowledgeProvider profileKnowledgeProvider = new MyProfileKnowledgeWorkerR4();
				ArrayList<ValidationMessage> messages = new ArrayList<>();
				org.hl7.fhir.r4.model.StructureDefinition base = (org.hl7.fhir.r4.model.StructureDefinition) theValidationSupport.fetchStructureDefinition(input.getBaseDefinition());
				if (base == null) {
					throw new PreconditionFailedException("Unknown base definition: " + input.getBaseDefinition());
				}
				new org.hl7.fhir.r4.conformance.ProfileUtilities(context, messages, profileKnowledgeProvider).generateSnapshot(base, input, theUrl, theWebUrl, theProfileName);
				break;
			}
			case R5: {
				org.hl7.fhir.r5.model.StructureDefinition input = (org.hl7.fhir.r5.model.StructureDefinition) theInput;
				org.hl7.fhir.r5.context.IWorkerContext context = new org.hl7.fhir.r5.hapi.ctx.HapiWorkerContext(myCtx, theValidationSupport);
				org.hl7.fhir.r5.conformance.ProfileUtilities.ProfileKnowledgeProvider profileKnowledgeProvider = new ProfileKnowledgeWorkerR5(myCtx);
				ArrayList<ValidationMessage> messages = new ArrayList<>();
				org.hl7.fhir.r5.model.StructureDefinition base = (org.hl7.fhir.r5.model.StructureDefinition) theValidationSupport.fetchStructureDefinition(input.getBaseDefinition());
				if (base == null) {
					throw new PreconditionFailedException("Unknown base definition: " + input.getBaseDefinition());
				}
				new org.hl7.fhir.r5.conformance.ProfileUtilities(context, messages, profileKnowledgeProvider).generateSnapshot(base, input, theUrl, theWebUrl, theProfileName);
				break;
			}

			// NOTE: Add to the class javadoc if you add to this

			case DSTU2:
			case DSTU2_HL7ORG:
			case DSTU2_1:
			default:
				throw new IllegalStateException("Can not generate snapshot for version: " + theInput.getStructureFhirVersionEnum());
		}

		return theInput;
	}

	@Override
	public FhirContext getFhirContext() {
		return myCtx;
	}


	private class MyProfileKnowledgeWorkerR4 implements org.hl7.fhir.r4.conformance.ProfileUtilities.ProfileKnowledgeProvider {
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
		public BindingResolution resolveBinding(org.hl7.fhir.r4.model.StructureDefinition def, org.hl7.fhir.r4.model.ElementDefinition.ElementDefinitionBindingComponent binding, String path) throws FHIRException {
			return null;
		}

		@Override
		public BindingResolution resolveBinding(org.hl7.fhir.r4.model.StructureDefinition def, String url, String path) throws FHIRException {
			return null;
		}

		@Override
		public String getLinkForProfile(org.hl7.fhir.r4.model.StructureDefinition profile, String url) {
			return null;
		}

		@Override
		public boolean prependLinks() {
			return false;
		}

		@Override
		public String getLinkForUrl(String corePath, String url) {
			throw new UnsupportedOperationException();
		}

	}

	private class MyProfileKnowledgeWorkerDstu3 implements org.hl7.fhir.dstu3.conformance.ProfileUtilities.ProfileKnowledgeProvider {
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
		public BindingResolution resolveBinding(org.hl7.fhir.dstu3.model.StructureDefinition theStructureDefinition, org.hl7.fhir.dstu3.model.ElementDefinition.ElementDefinitionBindingComponent theElementDefinitionBindingComponent, String theS) {
			return null;
		}

		@Override
		public String getLinkForProfile(org.hl7.fhir.dstu3.model.StructureDefinition theStructureDefinition, String theS) {
			return null;
		}

		@Override
		public boolean prependLinks() {
			return false;
		}

	}

}
