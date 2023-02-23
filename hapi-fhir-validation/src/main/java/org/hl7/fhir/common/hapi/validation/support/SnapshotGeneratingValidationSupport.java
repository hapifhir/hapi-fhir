package org.hl7.fhir.common.hapi.validation.support;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.ValidationSupportContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import ca.uhn.hapi.converters.canonical.VersionCanonicalizer;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.common.hapi.validation.validator.ProfileKnowledgeWorkerR5;
import org.hl7.fhir.common.hapi.validation.validator.VersionSpecificWorkerContextWrapper;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.conformance.profile.ProfileKnowledgeProvider;
import org.hl7.fhir.r5.conformance.profile.ProfileUtilities;
import org.hl7.fhir.r5.context.IWorkerContext;
import org.hl7.fhir.utilities.validation.ValidationMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.hl7.fhir.common.hapi.validation.support.CommonCodeSystemsTerminologyService.getFhirVersionEnum;

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
	private final VersionCanonicalizer myVersionCanonicalizer;

	/**
	 * Constructor
	 */
	public SnapshotGeneratingValidationSupport(FhirContext theCtx) {
		Validate.notNull(theCtx);
		myCtx = theCtx;
		myVersionCanonicalizer = new VersionCanonicalizer(theCtx);
	}

	@SuppressWarnings("EnhancedSwitchMigration")
	@Override
	public IBaseResource generateSnapshot(ValidationSupportContext theValidationSupportContext, IBaseResource theInput, String theUrl, String theWebUrl, String theProfileName) {

		String inputUrl = null;
		try {
			FhirVersionEnum version = theInput.getStructureFhirVersionEnum();

			org.hl7.fhir.r5.model.StructureDefinition inputCanonical = myVersionCanonicalizer.structureDefinitionToCanonical(theInput);

			inputUrl = inputCanonical.getUrl();
			if (theValidationSupportContext.getCurrentlyGeneratingSnapshots().contains(inputUrl)) {
				ourLog.warn("Detected circular dependency, already generating snapshot for: {}", inputUrl);
				return theInput;
			}
			theValidationSupportContext.getCurrentlyGeneratingSnapshots().add(inputUrl);

			String baseDefinition = inputCanonical.getBaseDefinition();
			if (isBlank(baseDefinition)) {
				throw new PreconditionFailedException(Msg.code(704) + "StructureDefinition[id=" + inputCanonical.getIdElement().getId() + ", url=" + inputCanonical.getUrl() + "] has no base");
			}

			IBaseResource base = theValidationSupportContext.getRootValidationSupport().fetchStructureDefinition(baseDefinition);
			if (base == null) {
				throw new PreconditionFailedException(Msg.code(705) + "Unknown base definition: " + baseDefinition);
			}

			org.hl7.fhir.r5.model.StructureDefinition baseCanonical = myVersionCanonicalizer.structureDefinitionToCanonical(base);

			if (baseCanonical.getSnapshot().getElement().isEmpty()) {
				// If the base definition also doesn't have a snapshot, generate that first
				theValidationSupportContext.getRootValidationSupport().generateSnapshot(theValidationSupportContext, base, null, null, null);
				baseCanonical = myVersionCanonicalizer.structureDefinitionToCanonical(base);
			}

			ArrayList<ValidationMessage> messages = new ArrayList<>();
			ProfileKnowledgeProvider profileKnowledgeProvider = new ProfileKnowledgeWorkerR5(myCtx);
			IWorkerContext context = new VersionSpecificWorkerContextWrapper(theValidationSupportContext, myVersionCanonicalizer);
			ProfileUtilities profileUtilities = new ProfileUtilities(context, messages, profileKnowledgeProvider);
			profileUtilities.generateSnapshot(baseCanonical, inputCanonical, theUrl, theWebUrl, theProfileName);

			switch (getFhirVersionEnum(theValidationSupportContext.getRootValidationSupport().getFhirContext(), theInput)) {
				case DSTU3:
					org.hl7.fhir.dstu3.model.StructureDefinition generatedDstu3 = (org.hl7.fhir.dstu3.model.StructureDefinition) myVersionCanonicalizer.structureDefinitionFromCanonical(inputCanonical);
					((org.hl7.fhir.dstu3.model.StructureDefinition) theInput).getSnapshot().getElement().clear();
					((org.hl7.fhir.dstu3.model.StructureDefinition) theInput).getSnapshot().getElement().addAll(generatedDstu3.getSnapshot().getElement());
					break;
				case R4:
					org.hl7.fhir.r4.model.StructureDefinition generatedR4 = (org.hl7.fhir.r4.model.StructureDefinition) myVersionCanonicalizer.structureDefinitionFromCanonical(inputCanonical);
					((org.hl7.fhir.r4.model.StructureDefinition) theInput).getSnapshot().getElement().clear();
					((org.hl7.fhir.r4.model.StructureDefinition) theInput).getSnapshot().getElement().addAll(generatedR4.getSnapshot().getElement());
					break;
				case R4B:
					org.hl7.fhir.r4b.model.StructureDefinition generatedR4b = (org.hl7.fhir.r4b.model.StructureDefinition) myVersionCanonicalizer.structureDefinitionFromCanonical(inputCanonical);
					((org.hl7.fhir.r4b.model.StructureDefinition) theInput).getSnapshot().getElement().clear();
					((org.hl7.fhir.r4b.model.StructureDefinition) theInput).getSnapshot().getElement().addAll(generatedR4b.getSnapshot().getElement());
					break;
				case R5:
					org.hl7.fhir.r5.model.StructureDefinition generatedR5 = (org.hl7.fhir.r5.model.StructureDefinition) myVersionCanonicalizer.structureDefinitionFromCanonical(inputCanonical);
					((org.hl7.fhir.r5.model.StructureDefinition) theInput).getSnapshot().getElement().clear();
					((org.hl7.fhir.r5.model.StructureDefinition) theInput).getSnapshot().getElement().addAll(generatedR5.getSnapshot().getElement());
					break;
				case DSTU2:
				case DSTU2_HL7ORG:
				case DSTU2_1:
				default:
					throw new IllegalStateException(Msg.code(706) + "Can not generate snapshot for version: " + version);
			}

			return theInput;

		} catch (BaseServerResponseException e) {
			throw e;
		} catch (Exception e) {
			throw new InternalErrorException(Msg.code(707) + "Failed to generate snapshot", e);
		} finally {
			if (inputUrl != null) {
				theValidationSupportContext.getCurrentlyGeneratingSnapshots().remove(inputUrl);
			}
		}
	}

	@Override
	public FhirContext getFhirContext() {
		return myCtx;
	}

}
