package org.hl7.fhir.common.hapi.validation.support;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.ValidationSupportContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import ca.uhn.fhir.util.Logs;
import ca.uhn.hapi.converters.canonical.VersionCanonicalizer;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.common.hapi.validation.validator.ProfileKnowledgeWorkerR5;
import org.hl7.fhir.common.hapi.validation.validator.VersionSpecificWorkerContextWrapper;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.conformance.profile.ProfileKnowledgeProvider;
import org.hl7.fhir.r5.conformance.profile.ProfileUtilities;
import org.hl7.fhir.r5.context.IWorkerContext;
import org.hl7.fhir.r5.fhirpath.FHIRPathEngine;
import org.hl7.fhir.r5.model.StructureDefinition;
import org.hl7.fhir.utilities.validation.ValidationMessage;
import org.slf4j.Logger;

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
	private static final Logger ourLog = Logs.getTerminologyTroubleshootingLog();
	private final FhirContext myCtx;
	private final VersionCanonicalizer myVersionCanonicalizer;
	private final IWorkerContext myWorkerContext;
	private final FHIRPathEngine myFHIRPathEngine;

	/**
	 * Constructor
	 */
	public SnapshotGeneratingValidationSupport(FhirContext theFhirContext) {
		this(theFhirContext, null, null);
	}

	public SnapshotGeneratingValidationSupport(
			FhirContext theFhirContext, IWorkerContext theWorkerContext, FHIRPathEngine theFHIRPathEngine) {
		Validate.notNull(theFhirContext);
		myCtx = theFhirContext;
		myVersionCanonicalizer = new VersionCanonicalizer(theFhirContext);
		myWorkerContext = theWorkerContext;
		myFHIRPathEngine = theFHIRPathEngine;
	}

	@SuppressWarnings("EnhancedSwitchMigration")
	@Override
	public IBaseResource generateSnapshot(
			ValidationSupportContext theValidationSupportContext,
			IBaseResource theInput,
			String theUrl,
			String theWebUrl,
			String theProfileName) {

		String inputUrl = null;
		try {
			IBaseResource inputClone = myCtx.newTerser().clone(theInput);
			org.hl7.fhir.r5.model.StructureDefinition inputCanonical =
					myVersionCanonicalizer.structureDefinitionToCanonical(inputClone);

			inputUrl = inputCanonical.getUrl();

			// FIXME: remove?
//			if (theValidationSupportContext.getCurrentlyGeneratingSnapshots().contains(inputUrl)) {
//				ourLog.warn("Detected circular dependency, already generating snapshot for: {}", inputUrl);
//				return theInput;
//			}

			String baseDefinition = inputCanonical.getBaseDefinition();
			if (isBlank(baseDefinition)) {
				throw new PreconditionFailedException(Msg.code(704) + "StructureDefinition[id="
						+ inputCanonical.getIdElement().getId() + ", url=" + inputCanonical.getUrl() + "] has no base");
			}

			IWorkerContext workerContext = myWorkerContext;
			// FIXME: should we change so this cant be null?
			if (workerContext == null) {
				workerContext =
					new VersionSpecificWorkerContextWrapper(theValidationSupportContext.getRootValidationSupport());
			}

			StructureDefinition base = workerContext.fetchResource(StructureDefinition.class, baseDefinition);
			if (base == null) {
				throw new PreconditionFailedException(Msg.code(705) + "Unknown base definition: " + baseDefinition);
			}

			ArrayList<ValidationMessage> messages = new ArrayList<>();
			ProfileKnowledgeProvider profileKnowledgeProvider = new ProfileKnowledgeWorkerR5(myCtx);
			ProfileUtilities profileUtilities = new ProfileUtilities(workerContext, messages, profileKnowledgeProvider);

			// FIXME: remove?
//			theValidationSupportContext.getCurrentlyGeneratingSnapshots().add(inputUrl);

			ourLog.info("Generating snapshot for StructureDefinition: {}", inputCanonical.getUrl());
			profileUtilities.generateSnapshot(base, inputCanonical, theUrl, theWebUrl, theProfileName);

			return myVersionCanonicalizer.structureDefinitionFromCanonical(inputCanonical);

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

	@Override
	public String getName() {
		return getFhirContext().getVersion().getVersion() + " Snapshot Generating Validation Support";
	}
}
