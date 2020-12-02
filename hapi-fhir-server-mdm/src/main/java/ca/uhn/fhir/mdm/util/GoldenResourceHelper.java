package ca.uhn.fhir.mdm.util;

/*-
 * #%L
 * HAPI FHIR - Enterprise Master Patient Index
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementCompositeDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.fhirpath.IFhirPath;
import ca.uhn.fhir.mdm.api.IMdmSettings;
import ca.uhn.fhir.mdm.log.Logs;
import ca.uhn.fhir.mdm.model.CanonicalEID;
import ca.uhn.fhir.mdm.model.MdmTransactionContext;
import ca.uhn.fhir.util.FhirTerser;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.BooleanType;
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

@Service
public class GoldenResourceHelper {

	private static final Logger ourLog = Logs.getMdmTroubleshootingLog();

	private static final String FIELD_NAME_IDENTIFIER = "identifier";

	@Autowired
	private IMdmSettings myMdmSettings;
	@Autowired
	private EIDHelper myEIDHelper;


	private final FhirContext myFhirContext;

	@Autowired
	public GoldenResourceHelper(FhirContext theFhirContext) {
		myFhirContext = theFhirContext;
	}

	/**
	 * Creates a copy of the specified resource. This method will carry over resource EID if it exists. If it does not exist,
	 * a randomly generated UUID EID will be created.
	 *
	 * @param <T>                 Supported MDM resource type (e.g. Patient, Practitioner)
	 * @param theIncomingResource The resource that will be used as the starting point for the MDM linking.
	 */
	public <T extends IAnyResource> T createGoldenResourceFromMdmTarget(T theIncomingResource) {
		validateContextSupported();

		// get a ref to the actual ID Field
		RuntimeResourceDefinition resourceDefinition = myFhirContext.getResourceDefinition(theIncomingResource);
		IBaseResource newGoldenResource = resourceDefinition.newInstance();

		// hapi has 2 metamodels: for children and types
		BaseRuntimeChildDefinition goldenResourceIdentifier = resourceDefinition.getChildByName(FIELD_NAME_IDENTIFIER);

		cloneAllExternalEidsIntoNewGoldenResource(goldenResourceIdentifier, theIncomingResource, newGoldenResource);

		addHapiEidIfNoExternalEidIsPresent(newGoldenResource, goldenResourceIdentifier, theIncomingResource);

		MdmUtil.setMdmManaged(newGoldenResource);
		MdmUtil.setGoldenResource(newGoldenResource);

		return (T) newGoldenResource;
	}

	/**
	 * If there are no external EIDs on the incoming resource, create a new HAPI EID on the new Golden Resource.
	 */
	//TODO GGG ask james if there is any way we can convert this canonical EID into a generic STU-agnostic IBase.
	private <T extends IAnyResource> void addHapiEidIfNoExternalEidIsPresent(
		IBaseResource theNewGoldenResource, BaseRuntimeChildDefinition theGoldenResourceIdentifier, IAnyResource theTargetResource) {

		List<CanonicalEID> eidsToApply = myEIDHelper.getExternalEid(theNewGoldenResource);
		if (!eidsToApply.isEmpty()) {
			return;
		}

		CanonicalEID hapiEid = myEIDHelper.createHapiEid();
		theGoldenResourceIdentifier.getMutator().addValue(theNewGoldenResource, toId(hapiEid));

		// set identifier on the target resource
		cloneEidIntoResource(theTargetResource, hapiEid);
	}

	private void cloneEidIntoResource(IBaseResource theResourceToCloneInto, CanonicalEID theEid) {
		// get a ref to the actual ID Field
		RuntimeResourceDefinition resourceDefinition = myFhirContext.getResourceDefinition(theResourceToCloneInto);
		// hapi has 2 metamodels: for children and types
		BaseRuntimeChildDefinition resourceIdentifier = resourceDefinition.getChildByName(FIELD_NAME_IDENTIFIER);
		cloneEidIntoResource(resourceIdentifier, toId(theEid), theResourceToCloneInto);
	}

	/**
	 * Given an Child Definition of `identifier`, a R4/DSTU3 EID Identifier, and a new resource, clone the EID into that resources' identifier list.
	 */
	private void cloneEidIntoResource(BaseRuntimeChildDefinition theIdentifierDefinition, IBase theEid, IBase theResourceToCloneEidInto) {
		// FHIR choice types - fields within fhir where we have a choice of ids
		BaseRuntimeElementCompositeDefinition<?> childIdentifier = (BaseRuntimeElementCompositeDefinition<?>) theIdentifierDefinition.getChildByName(FIELD_NAME_IDENTIFIER);
		IBase resourceNewIdentifier = childIdentifier.newInstance();

		FhirTerser terser = myFhirContext.newTerser();
		terser.cloneInto(theEid, resourceNewIdentifier, true);
		theIdentifierDefinition.getMutator().addValue(theResourceToCloneEidInto, resourceNewIdentifier);
	}

	/**
	 * Clones specified composite field (collection). Composite field values must confirm to the collections
	 * contract.
	 *
	 * @param theFrom Resource to clone the specified filed from
	 * @param theTo   Resource to clone the specified filed to
	 * @param field   Field name to be copied
	 */
	private void cloneCompositeField(IBaseResource theFrom, IBaseResource theTo, String field) {
		FhirTerser terser = myFhirContext.newTerser();

		RuntimeResourceDefinition definition = myFhirContext.getResourceDefinition(theFrom);
		BaseRuntimeChildDefinition childDefinition = definition.getChildByName(field);

		IFhirPath fhirPath = myFhirContext.newFhirPath();
		List<IBase> theFromFieldValues = childDefinition.getAccessor().getValues(theFrom);
		List<IBase> theToFieldValues = childDefinition.getAccessor().getValues(theTo);

		for (IBase theFromFieldValue : theFromFieldValues) {
			if (contains(theFromFieldValue, theToFieldValues)) {
				continue;
			}

			BaseRuntimeElementCompositeDefinition<?> compositeDefinition = (BaseRuntimeElementCompositeDefinition<?>) childDefinition.getChildByName(field);
			IBase newFieldValue = compositeDefinition.newInstance();
			terser.cloneInto(theFromFieldValue, newFieldValue, true);

			theToFieldValues.add(newFieldValue);
		}
	}

	private boolean contains(IBase theItem, List<IBase> theItems) {
		PrimitiveTypeComparingPredicate predicate = new PrimitiveTypeComparingPredicate();
		return theItems.stream().filter(i -> {
			return predicate.test(i, theItem);
		}).findFirst().isPresent();
	}

	private void cloneAllExternalEidsIntoNewGoldenResource(BaseRuntimeChildDefinition theGoldenResourceIdentifier,
																			 IBase theGoldenResource, IBase theNewGoldenResource) {
		// FHIR choice types - fields within fhir where we have a choice of ids
		IFhirPath fhirPath = myFhirContext.newFhirPath();
		List<IBase> goldenResourceIdentifiers = theGoldenResourceIdentifier.getAccessor().getValues(theGoldenResource);

		for (IBase base : goldenResourceIdentifiers) {
			Optional<IPrimitiveType> system = fhirPath.evaluateFirst(base, "system", IPrimitiveType.class);
			if (system.isPresent()) {
				String mdmSystem = myMdmSettings.getMdmRules().getEnterpriseEIDSystem();
				String baseSystem = system.get().getValueAsString();
				if (Objects.equals(baseSystem, mdmSystem)) {
					cloneEidIntoResource(theGoldenResourceIdentifier, base, theNewGoldenResource);
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
		throw new UnsupportedOperationException("Version not supported: " + myFhirContext.getVersion().getVersion());
	}

	/**
	 * Updates EID on Golden Resource, based on the incoming target resource. If the incoming resource has an external EID, it is applied
	 * to the Golden Resource, unless that golden resource already has an external EID which does not match, in which case throw {@link IllegalArgumentException}
	 * <p>
	 * If running in multiple EID mode, then incoming EIDs are simply added to the Golden Resource without checking for matches.
	 *
	 * @param theGoldenResource The golden resource to update the external EID on.
	 * @param theTargetResource The target we will retrieve the external EID from.
	 * @return the modified {@link IBaseResource} representing the Golden Resource.
	 */
	public IAnyResource updateGoldenResourceExternalEidFromTargetResource(IAnyResource theGoldenResource, IAnyResource
		theTargetResource, MdmTransactionContext theMdmTransactionContext) {
		//This handles overwriting an automatically assigned EID if a patient that links is coming in with an official EID.
		List<CanonicalEID> incomingTargetEid = myEIDHelper.getExternalEid(theTargetResource);
		List<CanonicalEID> goldenResourceOfficialEid = myEIDHelper.getExternalEid(theGoldenResource);

		if (incomingTargetEid.isEmpty()) {
			return theGoldenResource;
		}

		if (goldenResourceOfficialEid.isEmpty() || !myMdmSettings.isPreventMultipleEids()) {
			log(theMdmTransactionContext, "Incoming resource:" + theTargetResource.getIdElement().toUnqualifiedVersionless() + " + with EID " + incomingTargetEid.stream().map(CanonicalEID::toString).collect(Collectors.joining(","))
				+ " is applying this EIDs to its related Target Resource, as this Target Resource does not yet have an external EID");
			addCanonicalEidsToGoldenResourceIfAbsent(theGoldenResource, incomingTargetEid);
		} else if (!goldenResourceOfficialEid.isEmpty() && myEIDHelper.eidMatchExists(goldenResourceOfficialEid, incomingTargetEid)) {
			log(theMdmTransactionContext, "incoming resource:" + theTargetResource.getIdElement().toVersionless() + " with EIDs " + incomingTargetEid.stream().map(CanonicalEID::toString).collect(Collectors.joining(",")) + " does not need to overwrite Golden Resource, as this EID is already present");
		} else {
			throw new IllegalArgumentException(
				String.format("Target EIDs %s would create a duplicate golden resource, as EIDs %s already exist!",
					incomingTargetEid.toString(), goldenResourceOfficialEid.toString()));
		}
		return theGoldenResource;
	}

	public IBaseResource overwriteExternalEids(IBaseResource theGoldenResource, List<CanonicalEID> theNewEid) {
		clearExternalEids(theGoldenResource);
		addCanonicalEidsToGoldenResourceIfAbsent(theGoldenResource, theNewEid);
		return theGoldenResource;
	}

	private void clearExternalEidsFromTheGoldenResource(BaseRuntimeChildDefinition theGoldenResourceIdentifier, IBase theGoldenResource) {
		IFhirPath fhirPath = myFhirContext.newFhirPath();
		List<IBase> goldenResourceIdentifiers = theGoldenResourceIdentifier.getAccessor().getValues(theGoldenResource);
		List<IBase> clonedIdentifiers = new ArrayList<>();
		FhirTerser terser = myFhirContext.newTerser();

		for (IBase base : goldenResourceIdentifiers) {
			Optional<IPrimitiveType> system = fhirPath.evaluateFirst(base, "system", IPrimitiveType.class);
			if (system.isPresent()) {
				String mdmSystem = myMdmSettings.getMdmRules().getEnterpriseEIDSystem();
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
	private void addCanonicalEidsToGoldenResourceIfAbsent(IBaseResource theGoldenResource, List<CanonicalEID> theIncomingTargetExternalEids) {
		List<CanonicalEID> goldenResourceExternalEids = myEIDHelper.getExternalEid(theGoldenResource);

		for (CanonicalEID incomingExternalEid : theIncomingTargetExternalEids) {
			if (goldenResourceExternalEids.contains(incomingExternalEid)) {
				continue;
			} else {
				cloneEidIntoResource(theGoldenResource, incomingExternalEid);
			}
		}
	}

	private <T extends IBase> T toId(CanonicalEID eid) {
		switch (myFhirContext.getVersion().getVersion()) {
			case R4:
				return (T) eid.toR4();
			case DSTU3:
				return (T) eid.toDSTU3();
		}
		throw new IllegalStateException("Unsupported FHIR version " + myFhirContext.getVersion().getVersion());
	}


	private <T extends IBase> T toBooleanType(boolean theFlag) {
		switch (myFhirContext.getVersion().getVersion()) {
			case R4:
				return (T) new BooleanType(theFlag);
			case DSTU3:
				return (T) new org.hl7.fhir.dstu3.model.BooleanType(theFlag);
		}
		throw new IllegalStateException("Unsupported FHIR version " + myFhirContext.getVersion().getVersion());
	}

	private <T extends IBase> boolean fromBooleanType(T theFlag) {
		switch (myFhirContext.getVersion().getVersion()) {
			case R4:
				return ((BooleanType) theFlag).booleanValue();
			case DSTU3:
				return ((org.hl7.fhir.dstu3.model.BooleanType) theFlag).booleanValue();
		}
		throw new IllegalStateException("Unsupported FHIR version " + myFhirContext.getVersion().getVersion());
	}

	public void mergeFields(IBaseResource theFromGoldenResource, IBaseResource theToGoldenResource) {
		//	TODO NG - Revisit when merge rules are defined
		cloneCompositeField(theFromGoldenResource, theToGoldenResource, FIELD_NAME_IDENTIFIER);

//		switch (myFhirContext.getVersion().getVersion()) {
//			case R4:
//				mergeR4PersonFields(theFromGoldenResource, theToGoldenResource);
//				break;
//			case DSTU3:
//				mergeDstu3PersonFields(theFromGoldenResource, theToGoldenResource);
//				break;
//			default:
//				throw new UnsupportedOperationException("Version not supported: " + myFhirContext.getVersion().getVersion());
//		}
	}

	/**
	 * An incoming resource is a potential duplicate if it matches a target that has a golden resource with an official
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

	public void handleExternalEidAddition(IAnyResource theGoldenResource, IAnyResource theTargetResource, MdmTransactionContext
		theMdmTransactionContext) {
		List<CanonicalEID> eidFromResource = myEIDHelper.getExternalEid(theTargetResource);
		if (!eidFromResource.isEmpty()) {
			updateGoldenResourceExternalEidFromTargetResource(theGoldenResource, theTargetResource, theMdmTransactionContext);
		}
	}

	public void deactivateResource(IAnyResource theResource) {
		MdmUtil.setGoldenResourceRedirected(theResource);
	}

	public boolean isDeactivated(IBaseResource theGoldenResource) {
		return MdmUtil.isGoldenRecordRedirected(theGoldenResource);
	}
}
