package ca.uhn.fhir.mdm.util;

/*-
 * #%L
 * HAPI FHIR - Master Data Management
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementCompositeDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.fhirpath.IFhirPath;
import ca.uhn.fhir.mdm.api.IMdmSettings;
import ca.uhn.fhir.mdm.api.IMdmSurvivorshipService;
import ca.uhn.fhir.mdm.log.Logs;
import ca.uhn.fhir.mdm.model.CanonicalEID;
import ca.uhn.fhir.mdm.model.MdmTransactionContext;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.util.FhirTerser;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static ca.uhn.fhir.context.FhirVersionEnum.DSTU3;
import static ca.uhn.fhir.context.FhirVersionEnum.R4;

@Service
public class GoldenResourceHelper {

	private static final Logger ourLog = Logs.getMdmTroubleshootingLog();

	static final String FIELD_NAME_IDENTIFIER = "identifier";

	@Autowired
	private IMdmSettings myMdmSettings;
	@Autowired
	private EIDHelper myEIDHelper;
	@Autowired
	private IMdmSurvivorshipService myMdmSurvivorshipService;

	private final FhirContext myFhirContext;

	@Autowired
	public GoldenResourceHelper(FhirContext theFhirContext) {
		myFhirContext = theFhirContext;
	}

	/**
	 * Creates a copy of the specified resource. This method will carry over resource EID if it exists. If it does not exist,
	 * a randomly generated UUID EID will be created.
	 *
	 * @param <T>                      Supported MDM resource type (e.g. Patient, Practitioner)
	 * @param theIncomingResource      The resource that will be used as the starting point for the MDM linking.
	 * @param theMdmTransactionContext
	 */
	@Nonnull
	public <T extends IAnyResource> T createGoldenResourceFromMdmSourceResource(T theIncomingResource, MdmTransactionContext theMdmTransactionContext) {
		validateContextSupported();

		// get a ref to the actual ID Field
		RuntimeResourceDefinition resourceDefinition = myFhirContext.getResourceDefinition(theIncomingResource);
		IBaseResource newGoldenResource = resourceDefinition.newInstance();

		myMdmSurvivorshipService.applySurvivorshipRulesToGoldenResource(theIncomingResource, newGoldenResource, theMdmTransactionContext);

		// hapi has 2 metamodels: for children and types
		BaseRuntimeChildDefinition goldenResourceIdentifier = resourceDefinition.getChildByName(FIELD_NAME_IDENTIFIER);

		cloneAllExternalEidsIntoNewGoldenResource(goldenResourceIdentifier, theIncomingResource, newGoldenResource);

		addHapiEidIfNoExternalEidIsPresent(newGoldenResource, goldenResourceIdentifier, theIncomingResource);

		MdmResourceUtil.setMdmManaged(newGoldenResource);
		MdmResourceUtil.setGoldenResource(newGoldenResource);

		// add the partition id to the new resource
		newGoldenResource.setUserData(Constants.RESOURCE_PARTITION_ID, theIncomingResource.getUserData(Constants.RESOURCE_PARTITION_ID));

		return (T) newGoldenResource;
	}

	/**
	 * If there are no external EIDs on the incoming resource, create a new HAPI EID on the new Golden Resource.
	 */
	//TODO GGG ask james if there is any way we can convert this canonical EID into a generic STU-agnostic IBase.
	private <T extends IAnyResource> void addHapiEidIfNoExternalEidIsPresent(
		IBaseResource theNewGoldenResource, BaseRuntimeChildDefinition theGoldenResourceIdentifier, IAnyResource theSourceResource) {

		List<CanonicalEID> eidsToApply = myEIDHelper.getExternalEid(theNewGoldenResource);
		if (!eidsToApply.isEmpty()) {
			return;
		}

		CanonicalEID hapiEid = myEIDHelper.createHapiEid();
		theGoldenResourceIdentifier.getMutator().addValue(theNewGoldenResource, IdentifierUtil.toId(myFhirContext, hapiEid));

		// set identifier on the source resource
		cloneEidIntoResource(myFhirContext, theSourceResource, hapiEid);
	}

	private void cloneAllExternalEidsIntoNewGoldenResource(BaseRuntimeChildDefinition theGoldenResourceIdentifier,
																			 IAnyResource theGoldenResource, IBase theNewGoldenResource) {
		// FHIR choice types - fields within fhir where we have a choice of ids
		IFhirPath fhirPath = myFhirContext.newFhirPath();
		List<IBase> goldenResourceIdentifiers = theGoldenResourceIdentifier.getAccessor().getValues(theGoldenResource);

		for (IBase base : goldenResourceIdentifiers) {
			Optional<IPrimitiveType> system = fhirPath.evaluateFirst(base, "system", IPrimitiveType.class);
			if (system.isPresent()) {
				String resourceType = myFhirContext.getResourceType(theGoldenResource);
				String mdmSystem = myMdmSettings.getMdmRules().getEnterpriseEIDSystemForResourceType(resourceType);
				String baseSystem = system.get().getValueAsString();
				if (Objects.equals(baseSystem, mdmSystem)) {
					ca.uhn.fhir.util.TerserUtil.cloneEidIntoResource(myFhirContext, theGoldenResourceIdentifier, base, theNewGoldenResource);
					ourLog.debug("System {} differs from system in the MDM rules {}", baseSystem, mdmSystem);
				}
			} else {
				ourLog.debug("System is missing, skipping");
			}
		}
	}

	private void validateContextSupported() {
		FhirVersionEnum fhirVersion = myFhirContext.getVersion().getVersion();
		if (fhirVersion == R4 || fhirVersion == DSTU3) {
			return;
		}
		throw new UnsupportedOperationException(Msg.code(1489) + "Version not supported: " + myFhirContext.getVersion().getVersion());
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
	public IAnyResource updateGoldenResourceExternalEidFromSourceResource(IAnyResource theGoldenResource, IAnyResource
		theSourceResource, MdmTransactionContext theMdmTransactionContext) {
		//This handles overwriting an automatically assigned EID if a patient that links is coming in with an official EID.
		List<CanonicalEID> incomingSourceEid = myEIDHelper.getExternalEid(theSourceResource);
		List<CanonicalEID> goldenResourceOfficialEid = myEIDHelper.getExternalEid(theGoldenResource);

		if (incomingSourceEid.isEmpty()) {
			return theGoldenResource;
		}

		if (goldenResourceOfficialEid.isEmpty() || !myMdmSettings.isPreventMultipleEids()) {
			log(theMdmTransactionContext, "Incoming resource:" + theSourceResource.getIdElement().toUnqualifiedVersionless() + " + with EID " + incomingSourceEid.stream().map(CanonicalEID::toString).collect(Collectors.joining(","))
				+ " is applying this EIDs to its related Source Resource, as this Source Resource does not yet have an external EID");
			addCanonicalEidsToGoldenResourceIfAbsent(theGoldenResource, incomingSourceEid);
		} else if (!goldenResourceOfficialEid.isEmpty() && myEIDHelper.eidMatchExists(goldenResourceOfficialEid, incomingSourceEid)) {
			log(theMdmTransactionContext, "incoming resource:" + theSourceResource.getIdElement().toVersionless() + " with EIDs " + incomingSourceEid.stream().map(CanonicalEID::toString).collect(Collectors.joining(",")) + " does not need to overwrite Golden Resource, as this EID is already present");
		} else {
			throw new IllegalArgumentException(Msg.code(1490) + String.format("Source EIDs %s would create a duplicate golden resource, as EIDs %s already exist!",
					incomingSourceEid.toString(), goldenResourceOfficialEid.toString()));
		}
		return theGoldenResource;
	}

	public IBaseResource overwriteExternalEids(IBaseResource theGoldenResource, List<CanonicalEID> theNewEid) {
		clearExternalEids(theGoldenResource);
		addCanonicalEidsToGoldenResourceIfAbsent(theGoldenResource, theNewEid);
		return theGoldenResource;
	}

	private void clearExternalEidsFromTheGoldenResource(BaseRuntimeChildDefinition theGoldenResourceIdentifier, IBaseResource theGoldenResource) {
		IFhirPath fhirPath = myFhirContext.newFhirPath();
		List<IBase> goldenResourceIdentifiers = theGoldenResourceIdentifier.getAccessor().getValues(theGoldenResource);
		List<IBase> clonedIdentifiers = new ArrayList<>();
		FhirTerser terser = myFhirContext.newTerser();

		for (IBase base : goldenResourceIdentifiers) {
			Optional<IPrimitiveType> system = fhirPath.evaluateFirst(base, "system", IPrimitiveType.class);
			if (system.isPresent()) {
				String resourceType = myFhirContext.getResourceType(theGoldenResource);
				String mdmSystem = myMdmSettings.getMdmRules().getEnterpriseEIDSystemForResourceType(resourceType);
				String baseSystem = system.get().getValueAsString();
				if (Objects.equals(baseSystem, mdmSystem)) {
					ourLog.debug("Found EID confirming to MDM rules {}. It should not be copied, skipping", baseSystem);
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
	 */
	private void addCanonicalEidsToGoldenResourceIfAbsent(IBaseResource theGoldenResource, List<CanonicalEID> theIncomingSourceExternalEids) {
		List<CanonicalEID> goldenResourceExternalEids = myEIDHelper.getExternalEid(theGoldenResource);

		for (CanonicalEID incomingExternalEid : theIncomingSourceExternalEids) {
			if (goldenResourceExternalEids.contains(incomingExternalEid)) {
				continue;
			}
			cloneEidIntoResource(myFhirContext, theGoldenResource, incomingExternalEid);
		}
	}

	public boolean hasIdentifier(IBaseResource theResource) {
		return ca.uhn.fhir.util.TerserUtil.hasValues(myFhirContext, theResource, FIELD_NAME_IDENTIFIER);
	}

	public void mergeIndentifierFields(IBaseResource theFromGoldenResource, IBaseResource theToGoldenResource, MdmTransactionContext theMdmTransactionContext) {
		ca.uhn.fhir.util.TerserUtil.cloneCompositeField(myFhirContext, theFromGoldenResource, theToGoldenResource,
			FIELD_NAME_IDENTIFIER);
	}

	public void mergeNonIdentiferFields(IBaseResource theFromGoldenResource, IBaseResource theToGoldenResource, MdmTransactionContext theMdmTransactionContext) {
		myMdmSurvivorshipService.applySurvivorshipRulesToGoldenResource(theFromGoldenResource, theToGoldenResource, theMdmTransactionContext);
	}

	/**
	 * An incoming resource is a potential duplicate if it matches a source that has a golden resource with an official
	 * EID, but the incoming resource also has an EID that does not match.
	 */
	public boolean isPotentialDuplicate(IAnyResource theExistingGoldenResource, IAnyResource theComparingGoldenResource) {
		List<CanonicalEID> externalEidsGoldenResource = myEIDHelper.getExternalEid(theExistingGoldenResource);
		List<CanonicalEID> externalEidsResource = myEIDHelper.getExternalEid(theComparingGoldenResource);
		return !externalEidsGoldenResource.isEmpty() && !externalEidsResource.isEmpty() && !myEIDHelper.eidMatchExists(externalEidsResource, externalEidsGoldenResource);
	}

	private void log(MdmTransactionContext theMdmTransactionContext, String theMessage) {
		theMdmTransactionContext.addTransactionLogMessage(theMessage);
		ourLog.debug(theMessage);
	}

	public void handleExternalEidAddition(IAnyResource theGoldenResource, IAnyResource theSourceResource, MdmTransactionContext
		theMdmTransactionContext) {
		List<CanonicalEID> eidFromResource = myEIDHelper.getExternalEid(theSourceResource);
		if (!eidFromResource.isEmpty()) {
			updateGoldenResourceExternalEidFromSourceResource(theGoldenResource, theSourceResource, theMdmTransactionContext);
		}
	}

	/**
	 * Clones the specified canonical EID into the identifier field on the resource
	 *
	 * @param theFhirContext         Context to pull resource definitions from
	 * @param theResourceToCloneInto Resource to set the EID on
	 * @param theEid                 EID to be set
	 */
	public void cloneEidIntoResource(FhirContext theFhirContext, IBaseResource theResourceToCloneInto, CanonicalEID theEid) {
		// get a ref to the actual ID Field
		RuntimeResourceDefinition resourceDefinition = theFhirContext.getResourceDefinition(theResourceToCloneInto);
		// hapi has 2 metamodels: for children and types
		BaseRuntimeChildDefinition resourceIdentifier = resourceDefinition.getChildByName(FIELD_NAME_IDENTIFIER);
		ca.uhn.fhir.util.TerserUtil.cloneEidIntoResource(theFhirContext, resourceIdentifier,
			IdentifierUtil.toId(theFhirContext, theEid), theResourceToCloneInto);
	}
}
