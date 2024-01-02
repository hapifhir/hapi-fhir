/*-
 * #%L
 * HAPI FHIR - Master Data Management
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package ca.uhn.fhir.mdm.util;

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementCompositeDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.fhirpath.IFhirPath;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.mdm.api.IMdmSettings;
import ca.uhn.fhir.mdm.api.IMdmSurvivorshipService;
import ca.uhn.fhir.mdm.log.Logs;
import ca.uhn.fhir.mdm.model.CanonicalEID;
import ca.uhn.fhir.mdm.model.MdmTransactionContext;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.util.FhirTerser;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static ca.uhn.fhir.context.FhirVersionEnum.DSTU3;
import static ca.uhn.fhir.context.FhirVersionEnum.R4;
import static ca.uhn.fhir.context.FhirVersionEnum.R5;

@Service
public class GoldenResourceHelper {

	private static final Logger ourLog = Logs.getMdmTroubleshootingLog();

	static final String FIELD_NAME_IDENTIFIER = "identifier";

	private final IMdmSettings myMdmSettings;

	private final EIDHelper myEIDHelper;

	private final MdmPartitionHelper myMdmPartitionHelper;

	private final FhirContext myFhirContext;

	@Autowired
	public GoldenResourceHelper(
			FhirContext theFhirContext,
			IMdmSettings theMdmSettings,
			EIDHelper theEIDHelper,
			MdmPartitionHelper theMdmPartitionHelper) {
		myFhirContext = theFhirContext;
		myMdmSettings = theMdmSettings;
		myEIDHelper = theEIDHelper;
		myMdmPartitionHelper = theMdmPartitionHelper;
	}

	/**
	 * Creates a copy of the specified resource. This method will carry over resource EID if it exists. If it does not exist,
	 * a randomly generated UUID EID will be created.
	 *
	 * @param <T>                      Supported MDM resource type (e.g. Patient, Practitioner)
	 * @param theIncomingResource 	  The resource to build the golden resource off of.
	 *                                 Could be the source resource or another golden resource.
	 *                                 If a golden resource, do not provide an IMdmSurvivorshipService
	 * @param theMdmTransactionContext The mdm transaction context
	 * @param theMdmSurvivorshipService IMdmSurvivorshipSvc. Provide only if survivorshipskills are desired
	 *                                  to be applied. Provide null otherwise.
	 */
	@Nonnull
	public <T extends IAnyResource> T createGoldenResourceFromMdmSourceResource(
			T theIncomingResource,
			MdmTransactionContext theMdmTransactionContext,
			IMdmSurvivorshipService theMdmSurvivorshipService) {
		validateContextSupported();

		// get a ref to the actual ID Field
		RuntimeResourceDefinition resourceDefinition = myFhirContext.getResourceDefinition(theIncomingResource);
		IBaseResource newGoldenResource = resourceDefinition.newInstance();

		if (theMdmSurvivorshipService != null) {
			theMdmSurvivorshipService.applySurvivorshipRulesToGoldenResource(
					theIncomingResource, newGoldenResource, theMdmTransactionContext);
		}

		// hapi has 2 metamodels: for children and types
		BaseRuntimeChildDefinition goldenResourceIdentifier = resourceDefinition.getChildByName(FIELD_NAME_IDENTIFIER);

		cloneMDMEidsIntoNewGoldenResource(goldenResourceIdentifier, theIncomingResource, newGoldenResource);

		addHapiEidIfNoExternalEidIsPresent(newGoldenResource, goldenResourceIdentifier, theIncomingResource);

		MdmResourceUtil.setMdmManaged(newGoldenResource);
		MdmResourceUtil.setGoldenResource(newGoldenResource);

		// TODO - on updating links, if resolving a link, this should go away?
		// blocked resource's golden resource will be marked special
		// they are not part of MDM matching algorithm (will not link to other resources)
		// but other resources can link to them
		if (theMdmTransactionContext.getIsBlocked()) {
			MdmResourceUtil.setGoldenResourceAsBlockedResourceGoldenResource(newGoldenResource);
		}

		// add the partition id to the new resource
		newGoldenResource.setUserData(
				Constants.RESOURCE_PARTITION_ID,
				myMdmPartitionHelper.getRequestPartitionIdForNewGoldenResources(theIncomingResource));

		return (T) newGoldenResource;
	}

	/**
	 * If there are no external EIDs on the incoming resource, create a new HAPI EID on the new Golden Resource.
	 */
	// TODO GGG ask james if there is any way we can convert this canonical EID into a generic STU-agnostic IBase.
	private <T extends IAnyResource> void addHapiEidIfNoExternalEidIsPresent(
			IBaseResource theNewGoldenResource,
			BaseRuntimeChildDefinition theGoldenResourceIdentifier,
			IAnyResource theSourceResource) {

		List<CanonicalEID> eidsToApply = myEIDHelper.getExternalEid(theNewGoldenResource);
		if (!eidsToApply.isEmpty()) {
			return;
		}

		CanonicalEID hapiEid = myEIDHelper.createHapiEid();
		theGoldenResourceIdentifier
				.getMutator()
				.addValue(theNewGoldenResource, IdentifierUtil.toId(myFhirContext, hapiEid));

		// set identifier on the source resource
		cloneEidIntoResource(myFhirContext, theSourceResource, hapiEid);
	}

	private void cloneMDMEidsIntoNewGoldenResource(
			BaseRuntimeChildDefinition theGoldenResourceIdentifier,
			IAnyResource theIncomingResource,
			IBaseResource theNewGoldenResource) {
		String incomingResourceType = myFhirContext.getResourceType(theIncomingResource);
		String mdmEIDSystem = myMdmSettings.getMdmRules().getEnterpriseEIDSystemForResourceType(incomingResourceType);

		if (mdmEIDSystem == null) {
			return;
		}

		// FHIR choice types - fields within fhir where we have a choice of ids
		IFhirPath fhirPath = myFhirContext.newFhirPath();
		List<IBase> incomingResourceIdentifiers =
				theGoldenResourceIdentifier.getAccessor().getValues(theIncomingResource);

		for (IBase incomingResourceIdentifier : incomingResourceIdentifiers) {
			Optional<IPrimitiveType> incomingIdentifierSystem =
					fhirPath.evaluateFirst(incomingResourceIdentifier, "system", IPrimitiveType.class);
			if (incomingIdentifierSystem.isPresent()) {
				String incomingIdentifierSystemString =
						incomingIdentifierSystem.get().getValueAsString();
				if (Objects.equals(incomingIdentifierSystemString, mdmEIDSystem)) {
					ourLog.debug(
							"Incoming resource EID System {} matches EID system in the MDM rules.  Copying to Golden Resource.",
							incomingIdentifierSystemString);
					ca.uhn.fhir.util.TerserUtil.cloneIdentifierIntoResource(
							myFhirContext,
							theGoldenResourceIdentifier,
							incomingResourceIdentifier,
							theNewGoldenResource);
				} else {
					ourLog.debug(
							"Incoming resource EID System {} differs from EID system in the MDM rules {}.  Not copying to Golden Resource.",
							incomingIdentifierSystemString,
							mdmEIDSystem);
				}
			} else {
				ourLog.debug("No EID System in incoming resource.");
			}
		}
	}

	private void validateContextSupported() {
		FhirVersionEnum fhirVersion = myFhirContext.getVersion().getVersion();
		if (fhirVersion == R4 || fhirVersion == DSTU3 || fhirVersion == R5) {
			return;
		}
		throw new UnsupportedOperationException(Msg.code(1489) + "Version not supported: "
				+ myFhirContext.getVersion().getVersion());
	}

	/**
	 * Updates EID on Golden Resource, based on the incoming source resource. If the incoming resource has an external EID, it is applied
	 * to the Golden Resource, unless that golden resource already has an external EID which does not match, in which case throw {@link IllegalArgumentException}
	 * <p>
	 * If running in multiple EID mode, then incoming EIDs are simply added to the Golden Resource without checking for matches.
	 *
	 * @param theGoldenResource The golden resource to update the external EID on.
	 * @param theSourceResource The source we will retrieve the external EID from.
	 * @return the modified {@link IBaseResource} representing the Golden Resource.
	 */
	public IAnyResource updateGoldenResourceExternalEidFromSourceResource(
			IAnyResource theGoldenResource,
			IAnyResource theSourceResource,
			MdmTransactionContext theMdmTransactionContext) {
		// This handles overwriting an automatically assigned EID if a patient that links is coming in with an official
		// EID.
		List<CanonicalEID> incomingSourceEid = myEIDHelper.getExternalEid(theSourceResource);
		List<CanonicalEID> goldenResourceOfficialEid = myEIDHelper.getExternalEid(theGoldenResource);

		if (incomingSourceEid.isEmpty()) {
			return theGoldenResource;
		}

		if (goldenResourceOfficialEid.isEmpty() || !myMdmSettings.isPreventMultipleEids()) {
			if (addCanonicalEidsToGoldenResourceIfAbsent(theGoldenResource, incomingSourceEid)) {
				log(
						theMdmTransactionContext,
						"Incoming resource:" + theSourceResource.getIdElement().toUnqualifiedVersionless()
								+ " + with EID "
								+ incomingSourceEid.stream()
										.map(CanonicalEID::toString)
										.collect(Collectors.joining(","))
								+ " is applying this EID to its related Golden Resource, as this Golden Resource does not yet have an external EID");
			}
		} else if (!goldenResourceOfficialEid.isEmpty()
				&& myEIDHelper.eidMatchExists(goldenResourceOfficialEid, incomingSourceEid)) {
			log(
					theMdmTransactionContext,
					"Incoming resource:" + theSourceResource.getIdElement().toVersionless() + " with EIDs "
							+ incomingSourceEid.stream()
									.map(CanonicalEID::toString)
									.collect(Collectors.joining(","))
							+ " does not need to overwrite the EID in the Golden Resource, as this EID is already present in the Golden Resource");
		} else {
			throw new IllegalArgumentException(Msg.code(1490)
					+ String.format(
							"Incoming resource EID %s would create a duplicate Golden Resource, as Golden Resource EID %s already exists!",
							incomingSourceEid.toString(), goldenResourceOfficialEid.toString()));
		}
		return theGoldenResource;
	}

	public IBaseResource overwriteExternalEids(IBaseResource theGoldenResource, List<CanonicalEID> theNewEid) {
		clearExternalEids(theGoldenResource);
		addCanonicalEidsToGoldenResourceIfAbsent(theGoldenResource, theNewEid);
		return theGoldenResource;
	}

	private void clearExternalEidsFromTheGoldenResource(
			BaseRuntimeChildDefinition theGoldenResourceIdentifier, IBaseResource theGoldenResource) {
		IFhirPath fhirPath = myFhirContext.newFhirPath();
		List<IBase> goldenResourceIdentifiers =
				theGoldenResourceIdentifier.getAccessor().getValues(theGoldenResource);
		List<IBase> clonedIdentifiers = new ArrayList<>();
		FhirTerser terser = myFhirContext.newTerser();

		for (IBase base : goldenResourceIdentifiers) {
			Optional<IPrimitiveType> system = fhirPath.evaluateFirst(base, "system", IPrimitiveType.class);
			if (system.isPresent()) {
				String resourceType = myFhirContext.getResourceType(theGoldenResource);
				String mdmSystem = myMdmSettings.getMdmRules().getEnterpriseEIDSystemForResourceType(resourceType);
				String baseSystem = system.get().getValueAsString();
				if (Objects.equals(baseSystem, mdmSystem)) {
					ourLog.debug(
							"Found EID confirming to MDM rules {}. It does not need to be copied, skipping",
							baseSystem);
					continue;
				}
			}

			BaseRuntimeElementCompositeDefinition<?> childIdentifier = (BaseRuntimeElementCompositeDefinition<?>)
					theGoldenResourceIdentifier.getChildByName(FIELD_NAME_IDENTIFIER);
			IBase goldenResourceNewIdentifier = childIdentifier.newInstance();
			terser.cloneInto(base, goldenResourceNewIdentifier, true);

			clonedIdentifiers.add(goldenResourceNewIdentifier);
		}

		goldenResourceIdentifiers.clear();
		goldenResourceIdentifiers.addAll(clonedIdentifiers);
	}

	private void clearExternalEids(IBaseResource theGoldenResource) {
		// validate the system - if it's set to EID system - then clear it - type and STU version
		validateContextSupported();

		// get a ref to the actual ID Field
		RuntimeResourceDefinition resourceDefinition = myFhirContext.getResourceDefinition(theGoldenResource);
		BaseRuntimeChildDefinition goldenResourceIdentifier = resourceDefinition.getChildByName(FIELD_NAME_IDENTIFIER);
		clearExternalEidsFromTheGoldenResource(goldenResourceIdentifier, theGoldenResource);
	}

	/**
	 * Given a list of incoming External EIDs, and a Golden Resource, apply all the EIDs to this resource, which did not already exist on it.
	 * @return true if an EID was added
	 */
	private boolean addCanonicalEidsToGoldenResourceIfAbsent(
			IBaseResource theGoldenResource, List<CanonicalEID> theIncomingSourceExternalEids) {
		List<CanonicalEID> goldenResourceExternalEids = myEIDHelper.getExternalEid(theGoldenResource);
		boolean addedEid = false;
		for (CanonicalEID incomingExternalEid : theIncomingSourceExternalEids) {
			if (goldenResourceExternalEids.contains(incomingExternalEid)) {
				continue;
			}
			cloneEidIntoResource(myFhirContext, theGoldenResource, incomingExternalEid);
			addedEid = true;
		}
		return addedEid;
	}

	public boolean hasIdentifier(IBaseResource theResource) {
		return ca.uhn.fhir.util.TerserUtil.hasValues(myFhirContext, theResource, FIELD_NAME_IDENTIFIER);
	}

	public void mergeIndentifierFields(
			IBaseResource theFromGoldenResource,
			IBaseResource theToGoldenResource,
			MdmTransactionContext theMdmTransactionContext) {
		ca.uhn.fhir.util.TerserUtil.cloneCompositeField(
				myFhirContext, theFromGoldenResource, theToGoldenResource, FIELD_NAME_IDENTIFIER);
	}

	/**
	 * An incoming resource is a potential duplicate if it matches a source that has a golden resource with an official
	 * EID, but the incoming resource also has an EID that does not match.
	 */
	public boolean isPotentialDuplicate(
			IAnyResource theExistingGoldenResource, IAnyResource theComparingGoldenResource) {
		List<CanonicalEID> externalEidsGoldenResource = myEIDHelper.getExternalEid(theExistingGoldenResource);
		List<CanonicalEID> externalEidsResource = myEIDHelper.getExternalEid(theComparingGoldenResource);
		return !externalEidsGoldenResource.isEmpty()
				&& !externalEidsResource.isEmpty()
				&& !myEIDHelper.eidMatchExists(externalEidsResource, externalEidsGoldenResource);
	}

	private void log(MdmTransactionContext theMdmTransactionContext, String theMessage) {
		theMdmTransactionContext.addTransactionLogMessage(theMessage);
		ourLog.debug(theMessage);
	}

	public void handleExternalEidAddition(
			IAnyResource theGoldenResource,
			IAnyResource theSourceResource,
			MdmTransactionContext theMdmTransactionContext) {
		List<CanonicalEID> eidFromResource = myEIDHelper.getExternalEid(theSourceResource);
		if (!eidFromResource.isEmpty()) {
			updateGoldenResourceExternalEidFromSourceResource(
					theGoldenResource, theSourceResource, theMdmTransactionContext);
		}
	}

	/**
	 * Clones the specified canonical EID into the identifier field on the resource
	 *
	 * @param theFhirContext         Context to pull resource definitions from
	 * @param theResourceToCloneInto Resource to set the EID on
	 * @param theEid                 EID to be set
	 */
	public void cloneEidIntoResource(
			FhirContext theFhirContext, IBaseResource theResourceToCloneInto, CanonicalEID theEid) {
		// get a ref to the actual ID Field
		RuntimeResourceDefinition resourceDefinition = theFhirContext.getResourceDefinition(theResourceToCloneInto);
		// hapi has 2 metamodels: for children and types
		BaseRuntimeChildDefinition resourceIdentifier = resourceDefinition.getChildByName(FIELD_NAME_IDENTIFIER);
		ca.uhn.fhir.util.TerserUtil.cloneIdentifierIntoResource(
				theFhirContext,
				resourceIdentifier,
				IdentifierUtil.toId(theFhirContext, theEid),
				theResourceToCloneInto);
	}
}
