package org.hl7.fhir.common.hapi.validation.support;

import ca.uhn.fhir.context.BaseRuntimeElementDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeCompositeDatatypeDefinition;
import ca.uhn.fhir.context.RuntimePrimitiveDatatypeDefinition;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.common.hapi.validation.validator.ProfileKnowledgeWorkerR5;
import org.hl7.fhir.common.hapi.validation.validator.VersionSpecificWorkerContextWrapper;
import org.hl7.fhir.common.hapi.validation.validator.VersionTypeConverterDstu3;
import org.hl7.fhir.common.hapi.validation.validator.VersionTypeConverterR4;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.context.IWorkerContext;
import org.hl7.fhir.utilities.validation.ValidationMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

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
	private static final Logger ourLog = LoggerFactory.getLogger(SnapshotGeneratingValidationSupport.class);
	private final FhirContext myCtx;
	private Set<String> myExpanding = new HashSet<>();


	/**
	 * Constructor
	 */
	public SnapshotGeneratingValidationSupport(FhirContext theCtx) {
		Validate.notNull(theCtx);
		myCtx = theCtx;
	}

	@Override
	public IBaseResource generateSnapshot(IValidationSupport theValidationSupport, IBaseResource theInput, String theUrl, String theWebUrl, String theProfileName) {

		String inputUrl = null;
		try {
			assert theInput.getStructureFhirVersionEnum() == myCtx.getVersion().getVersion();

			VersionSpecificWorkerContextWrapper.IVersionTypeConverter converter = null;
			switch (theInput.getStructureFhirVersionEnum()) {
				case DSTU3:
					converter = new VersionTypeConverterDstu3();
					break;
				case R4:
					converter = new VersionTypeConverterR4();
					break;
				case R5:
					converter = VersionSpecificWorkerContextWrapper.IDENTITY_VERSION_TYPE_CONVERTER;
					break;
				case DSTU2:
				case DSTU2_HL7ORG:
				case DSTU2_1:
				default:
					throw new IllegalStateException("Can not generate snapshot for version: " + theInput.getStructureFhirVersionEnum());
			}


			org.hl7.fhir.r5.model.StructureDefinition inputCanonical = (org.hl7.fhir.r5.model.StructureDefinition) converter.toCanonical(theInput);

			inputUrl = inputCanonical.getUrl();
			if (myExpanding.contains(inputUrl)) {
				ourLog.warn("Detected circular dependency, already generating snapshot for: {}", inputUrl);
				return theInput;
			}
			myExpanding.add(inputUrl);

			IBaseResource base = theValidationSupport.fetchStructureDefinition(inputCanonical.getBaseDefinition());
			if (base == null) {
				throw new PreconditionFailedException("Unknown base definition: " + inputCanonical.getBaseDefinition());
			}

			org.hl7.fhir.r5.model.StructureDefinition baseCanonical = (org.hl7.fhir.r5.model.StructureDefinition) converter.toCanonical(base);

			ArrayList<ValidationMessage> messages = new ArrayList<>();
			org.hl7.fhir.r5.conformance.ProfileUtilities.ProfileKnowledgeProvider profileKnowledgeProvider = new ProfileKnowledgeWorkerR5(myCtx);
			IWorkerContext context = new VersionSpecificWorkerContextWrapper(theValidationSupport, converter);
			new org.hl7.fhir.r5.conformance.ProfileUtilities(context, messages, profileKnowledgeProvider).generateSnapshot(baseCanonical, inputCanonical, theUrl, theWebUrl, theProfileName);

			switch (theInput.getStructureFhirVersionEnum()) {
				case DSTU3:
					org.hl7.fhir.dstu3.model.StructureDefinition generatedDstu3 = (org.hl7.fhir.dstu3.model.StructureDefinition) converter.fromCanonical(inputCanonical);
					((org.hl7.fhir.dstu3.model.StructureDefinition) theInput).getSnapshot().getElement().clear();
					((org.hl7.fhir.dstu3.model.StructureDefinition) theInput).getSnapshot().getElement().addAll(generatedDstu3.getSnapshot().getElement());
					break;
				case R4:
					org.hl7.fhir.r4.model.StructureDefinition generatedR4 = (org.hl7.fhir.r4.model.StructureDefinition) converter.fromCanonical(inputCanonical);
					((org.hl7.fhir.r4.model.StructureDefinition) theInput).getSnapshot().getElement().clear();
					((org.hl7.fhir.r4.model.StructureDefinition) theInput).getSnapshot().getElement().addAll(generatedR4.getSnapshot().getElement());
					break;
				case R5:
					org.hl7.fhir.r5.model.StructureDefinition generatedR5 = (org.hl7.fhir.r5.model.StructureDefinition) converter.fromCanonical(inputCanonical);
					((org.hl7.fhir.r5.model.StructureDefinition) theInput).getSnapshot().getElement().clear();
					((org.hl7.fhir.r5.model.StructureDefinition) theInput).getSnapshot().getElement().addAll(generatedR5.getSnapshot().getElement());
					break;
				case DSTU2:
				case DSTU2_HL7ORG:
				case DSTU2_1:
				default:
					throw new IllegalStateException("Can not generate snapshot for version: " + theInput.getStructureFhirVersionEnum());
			}

			return theInput;

		} catch (Exception e) {
			throw new InternalErrorException("Failed to generate snapshot", e);
		} finally {
			if (inputUrl != null) {
				myExpanding.remove(inputUrl);
			}
		}
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
