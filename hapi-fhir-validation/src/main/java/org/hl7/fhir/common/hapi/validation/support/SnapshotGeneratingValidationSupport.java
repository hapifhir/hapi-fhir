package org.hl7.fhir.common.hapi.validation.support;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.ValidationSupportContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import ca.uhn.fhir.util.Logs;
import ca.uhn.hapi.converters.canonical.VersionCanonicalizer;
import com.google.common.annotations.VisibleForTesting;
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
	@VisibleForTesting
	public static final String GENERATING_SNAPSHOT_LOG_MSG = "Generating snapshot for StructureDefinition: {}";

	public static final String CURRENTLY_GENERATING_USERDATA_KEY =
			SnapshotGeneratingValidationSupport.class.getName() + "_CURRENTLY_GENERATING";
	private static final Logger ourLog = Logs.getTerminologyTroubleshootingLog();
	private final FhirContext myCtx;
	private final VersionCanonicalizer myVersionCanonicalizer;
	private final IWorkerContext myWorkerContext;

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
	}

	@Override
	public IBaseResource generateSnapshot(
			ValidationSupportContext theValidationSupportContext,
			IBaseResource theInput,
			String theUrl,
			String theWebUrl,
			String theProfileName) {

		synchronized (theInput) {
			if (theInput.getUserData(CURRENTLY_GENERATING_USERDATA_KEY) != null) {
				String url = myCtx.newTerser().getSinglePrimitiveValueOrNull(theInput, "url");
				ourLog.info("Detected circular dependency, already generating snapshot for: {}", url);
				return theInput;
			}

			try {
				theInput.setUserData(CURRENTLY_GENERATING_USERDATA_KEY, CURRENTLY_GENERATING_USERDATA_KEY);

				IBaseResource inputClone = myCtx.newTerser().clone(theInput);
				org.hl7.fhir.r5.model.StructureDefinition inputCanonical =
						myVersionCanonicalizer.structureDefinitionToCanonical(inputClone);

				String baseDefinition = inputCanonical.getBaseDefinition();
				if (isBlank(baseDefinition)) {
					throw new PreconditionFailedException(Msg.code(704) + "StructureDefinition[id="
							+ inputCanonical.getIdElement().getId() + ", url=" + inputCanonical.getUrl()
							+ "] has no base");
				}

				IWorkerContext workerContext = myWorkerContext;
				if (workerContext == null) {
					workerContext = new VersionSpecificWorkerContextWrapper(
							theValidationSupportContext.getRootValidationSupport());
				}

				StructureDefinition base = workerContext.fetchResource(StructureDefinition.class, baseDefinition);
				if (base == null) {
					throw new PreconditionFailedException(Msg.code(705) + "Unknown base definition: " + baseDefinition);
				}

				ArrayList<ValidationMessage> messages = new ArrayList<>();
				ProfileKnowledgeProvider profileKnowledgeProvider = new ProfileKnowledgeWorkerR5(myCtx);
				ProfileUtilities profileUtilities =
						new ProfileUtilities(workerContext, messages, profileKnowledgeProvider);

				ourLog.info(GENERATING_SNAPSHOT_LOG_MSG, inputCanonical.getUrl());
				profileUtilities.generateSnapshot(base, inputCanonical, theUrl, theWebUrl, theProfileName);

				return myVersionCanonicalizer.structureDefinitionFromCanonical(inputCanonical);

			} catch (BaseServerResponseException e) {
				throw e;
			} catch (Exception e) {
				throw new InternalErrorException(Msg.code(707) + "Failed to generate snapshot", e);
			} finally {
				theInput.setUserData(CURRENTLY_GENERATING_USERDATA_KEY, null);
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
