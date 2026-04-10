/*-
 * #%L
 * Smile CDR - CDR
 * %%
 * Copyright (C) 2016 - 2026 Smile CDR, Inc.
 * %%
 * All rights reserved.
 * #L%
 */
package ca.uhn.fhir.jpa.interceptor;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.svc.IResourceIdentifierCacheSvc;
import ca.uhn.fhir.jpa.dao.BaseTransactionProcessor;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.BaseParam;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.util.ICachedSearchDetails;
import ca.uhn.fhir.util.BundleBuilder;
import ca.uhn.fhir.util.BundleUtil;
import ca.uhn.fhir.util.FhirTerser;
import ca.uhn.fhir.util.ResourceReferenceInfo;
import ca.uhn.fhir.util.UrlUtil;
import ca.uhn.fhir.util.bundle.BundleEntryParts;
import ca.uhn.fhir.util.bundle.ModifiableBundleEntryParts;
import com.google.common.annotations.VisibleForTesting;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.annotation.PostConstruct;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Strings;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Patient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.trim;

/**
 * This interceptor has several functions which help make
 * {@link ca.uhn.fhir.jpa.interceptor.PatientIdPartitionInterceptor Patient ID Partition Mode}
 * workable on MegaScale, where we want to minimize all-partition searches as much as
 * we possibly can.
 * <ul>
 * <li>
 * When a conditional operation is being performed with a Patient identifier URL (e.g.
 * <code>Patient?identifier=http://foo|123</code>) we use a special non-partitioned table
 * to pre-resolve identifiers.
 * </li>
 * <li>
 * When a FHIR transaction has Patient compartment resources with a reference to a conditionally
 * created/updated Patient resource (e.g. an <code>Encounter.subject</code> reference to a <code>urn:uuid:</code>
 * placeholder for a conditionally created Patient), or have a match URL reference to a Patient
 * (e.g. an <code>Encounter.subject</code> reference to <code>Patient?identifier=http://foo</code>)
 * these references are replaced with a reference to the actual Patient ID in order to allow
 * the transaction partitioner to group things correctly.
 * </li>
 * <li>
 * If a Patient is created with an identifier that is eligible for pre-resolution, we make sure that the
 * resource FHIR ID is stored in the pre-resolution table in case it gets used later for conditional URLs.
 * </li>
 * </ul>
 */
public class PatientIdentifierPreResolutionInterceptor {

	private final FhirContext myFhirContext;
	private final PartitionSettings myPartitionSettings;
	private final MatchUrlService myMatchUrlService;
	private final IResourceIdentifierCacheSvc myResourceIdentifierCacheSvc;
	private final FhirTerser myTerser;
	private Set<String> myPreResolveIdentifierPatientSystemsValues;
	private List<Pattern> myPreResolveIdentifierPatientSystemsPatterns;
	private RequestPartitionId myDefaultPartition;
	private RuntimeResourceDefinition myPatientResourceDefinition;

	private String myPreResolveIdentifierPatientSystems = EMPTY;

	/**
	 * Constructor
	 */
	public PatientIdentifierPreResolutionInterceptor(
			FhirContext theFhirContext,
			PartitionSettings thePartitionSettings,
			MatchUrlService theMatchUrlService,
			IResourceIdentifierCacheSvc theResourceIdentifierCacheSvc) {
		myFhirContext = theFhirContext;
		myPartitionSettings = thePartitionSettings;
		myMatchUrlService = theMatchUrlService;
		myResourceIdentifierCacheSvc = theResourceIdentifierCacheSvc;

		myTerser = theFhirContext.newTerser();

		myDefaultPartition = RequestPartitionId.defaultPartition(myPartitionSettings);
		myPatientResourceDefinition = myFhirContext.getResourceDefinition("Patient");
	}

	public void setPreResolveIdentifierPatientSystems(String thePreResolveIdentifierPatientSystems) {
		myPreResolveIdentifierPatientSystems = thePreResolveIdentifierPatientSystems;
		initializePreResolveIdentifierSystemValues();
	}

	private void initializePreResolveIdentifierSystemValues() {
		/*
		 * Parse out the values in the Pre-Resolve Patient Identifier Systems.
		 * These can be fixed values or regex patterns which we'll parse now.
		 */
		myPreResolveIdentifierPatientSystemsPatterns = new ArrayList<>();
		myPreResolveIdentifierPatientSystemsValues = new HashSet<>();

		String[] values = StringUtils.split(defaultString(myPreResolveIdentifierPatientSystems), " \n");
		for (String value : values) {
			value = trim(value);
			if (isNotBlank(value)) {
				if (value.startsWith("^") && value.endsWith("$")) {
					myPreResolveIdentifierPatientSystemsPatterns.add(Pattern.compile(value));
				} else {
					myPreResolveIdentifierPatientSystemsValues.add(value);
				}
			}
		}
	}

	@Hook(
			value = Pointcut.STORAGE_TRANSACTION_PROCESSING)
	public void hookTransaction(RequestDetails theRequestDetails, IBaseBundle theBundle) {
// FIXME
		//		if (myDocumentRepositoryEnabled) {
//			documentRepositoryPreMassageDocumentBundles(theRequestDetails, theBundle);
//		}
		preResolvePatientIdsInBundleEntryConditionalUrls(theRequestDetails, theBundle);
	}

	@Hook(Pointcut.STORAGE_PRESTORAGE_RESOURCE_CREATED)
	public void hookResourceCreated(RequestDetails theRequestDetails, IBaseResource theResource) {
		if ("Patient".equals(myFhirContext.getResourceType(theResource))) {
			ensurePatientOnlyHasOneIdentifierEligibleForPreAssignment(theResource);
			ensurePatientIdentifierIsSavedInPreResolutionTable(theRequestDetails, theResource);
		}
	}

	@Hook(Pointcut.STORAGE_PRESTORAGE_RESOURCE_UPDATED)
	public void hookResourceUpdated(IBaseResource theOldResource, IBaseResource theNewResource) {
		if ("Patient".equals(myFhirContext.getResourceType(theNewResource))) {
			ensurePatientIdentifierEligibleForPreAssignmentIsNotBeingModified(theOldResource, theNewResource);
		}
	}

	@Hook(Pointcut.STORAGE_PRESEARCH_PARTITION_SELECTED)
	public void hookSearchRegistered(
			RequestDetails theRequestDetails,
			ICachedSearchDetails theSearchDetails,
			SearchParameterMap theSearchParameterMap) {
		String resourceType = theSearchDetails.getResourceType();
		massageSearchMap(theRequestDetails, theSearchParameterMap, resourceType, Map.of());
	}

	/**
	 * This method looks at incoming searches and modifies them for efficient execution in
	 * Patient ID partition mode. This means:
	 * <ul>
	 * <li>
	 * Replacing <code>Patient?identifier=foo</code> with <code>Patient?_id=Patient/BAR</code>
	 * </li>
	 * <li>
	 * Replacing <code>Encounter?subject.identifier=foo</code> with <code>Encounter?subject=Patient/BAR</code>
	 * </li>
	 * </ul>
	 *
	 * @return Returns <code>true</code> if {@literal theSearchParameterMap} was modified
	 */
	private boolean massageSearchMap(
			RequestDetails theRequestDetails,
			SearchParameterMap theSearchParameterMap,
			String theResourceType,
			Map<String, String> theFullUrlToResourceIdSubstitution) {

		boolean changed;
		if ("Patient".equals(theResourceType)) {
			changed = massageSearchMapForPatientResource(theRequestDetails, theSearchParameterMap);
		} else {
			changed = massageSearchMapForNonPatientResource(
					theRequestDetails, theSearchParameterMap, theResourceType, theFullUrlToResourceIdSubstitution);
			// FIXME
//			if (myDocumentRepositoryEnabled && "Bundle".equals(theResourceType)) {
//				changed |=
//						massageSearchMapForDocumentRepositoryBundleResource(theRequestDetails, theSearchParameterMap);
//			}
		}

		return changed;
	}

	/**
	 * This method massages a FHIR Transaction Bundle containing one or more FHIR Document Bundles
	 * when operating in Document Repository mode:
	 * <ul>
	 *     <li>
	 *         If the Document Bundle is submitted with a reference to a Patient and that Patient
	 *         isn't present in the FHIR transaction Bundle as well, add a standalone Patient
	 *         as a conditional-create to the transaction Bundle.
	 *     </li>
	 *     <li>
	 *         Pre-resolve any Patient.identifier values in references, replacing them with the
	 *         appropriate resource ID.
	 *     </li>
	 * </ul>
	 */
	private void documentRepositoryPreMassageDocumentBundles(RequestDetails theRequestDetails, IBaseBundle theBundle) {
		if (!"transaction".equals(BundleUtil.getBundleType(myFhirContext, theBundle))) {
			return;
		}

		List<IBaseResource> bundleResources =
				myTerser.getValues(theBundle, "Bundle.entry.resource", IBaseResource.class);
		for (IBaseResource bundleResource : bundleResources) {
			if ("Bundle".equals(myFhirContext.getResourceType(bundleResource))) {
				IBaseBundle bundle = (IBaseBundle) bundleResource;
				documentRepositoryPreMassageDocumentBundle(theRequestDetails, bundle, theBundle, bundleResources);
			}
		}
	}

	/**
	 * @see #documentRepositoryPreMassageDocumentBundles(RequestDetails, IBaseBundle) for a description
	 */
	private void documentRepositoryPreMassageDocumentBundle(
			RequestDetails theRequestDetails,
			IBaseBundle theDocumentBundle,
			IBaseBundle theTransactionBundle,
			List<IBaseResource> theTransactionBundleResources) {

		if (!"document".equals(BundleUtil.getBundleType(myFhirContext, theDocumentBundle))) {
			return;
		}

		List<BundleEntryParts> documentEntries = BundleUtil.toListOfEntries(myFhirContext, theDocumentBundle);
		if (documentEntries.isEmpty()) {
			throw new PreconditionFailedException("Document bundle must contain at least one entry");
		}

		IBaseResource composition = documentEntries.get(0).getResource();
		if (!"Composition".equals(myFhirContext.getResourceType(composition))) {
			throw new PreconditionFailedException("First resource in a Document bundle must be a Composition");
		}

		IBaseReference subject =
				myTerser.getSingleValueOrNull(composition, "Composition.subject", IBaseReference.class);
		IIdType subjectId = subject.getReferenceElement();

		if (subjectId == null || subjectId.isEmpty()) {
			throw new PreconditionFailedException("Document Bundle Composition.subject must be populated");
		}

		if (!("Patient".equals(subjectId.getResourceType()) && subjectId.hasIdPart())
				&& !subject.getReferenceElement().isUuid()) {
			throw new PreconditionFailedException(
					"Document Composition.subject must be a Patient reference or a placeholder UUID, found: "
							+ subject.getReferenceElement());
		}

		IBaseResource documentPatientResource = findResourceInEntries(documentEntries, subject);

		if (!subjectId.isUuid()) {
			documentRepositoryHandlePatientById(
					theTransactionBundle, theTransactionBundleResources, subjectId, documentPatientResource);
		} else {
			documentRepositoryHandlePatientByPlaceholder(
					theRequestDetails,
					theTransactionBundle,
					theTransactionBundleResources,
					documentPatientResource,
					subjectId,
					documentEntries);
		}
	}

	/**
	 * Given a document Bundle where the subject is a relative reference,
	 * add a conditional-create-by-id on a placeholder Patient in the
	 * containing FHIR transaction Bundle to guarantee that
	 * we have something to index against.
	 */
	private void documentRepositoryHandlePatientById(
			IBaseBundle theTransactionBundle,
			List<IBaseResource> theTransactionBundleResources,
			IIdType subjectId,
			IBaseResource documentPatientResource) {
		Predicate<IBaseResource> matcher = r -> r.getIdElement().getIdPart().equals(subjectId.getIdPart());
		IBaseResource foundExisting = findPatientInTransaction(theTransactionBundleResources, matcher);
		if (foundExisting == null) {
			IBaseResource bundlePatientResource = myTerser.clone(documentPatientResource);
			BundleBuilder bundleBuilder = new BundleBuilder(myFhirContext, theTransactionBundle);
			bundlePatientResource.setUserData(JpaConstants.RESOURCE_ID_SERVER_ASSIGNED_VALUE, subjectId.getIdPart());
			bundleBuilder
					.addTransactionCreateEntry(bundlePatientResource)
					.conditional("Patient?_id=" + subjectId.getValue());
		}
	}

	/**
	 * Given a document Bundle where the subject is a placeholder UUID reference
	 * to a Patient resource within the same document Bundle, we first pre-resolve
	 * the Patient by identifier. Then we add a
	 * conditional-create-by-id on a placeholder Patient in the containing
	 * FHIR Trsansaction Bundle so we have something to index against. Finally,
	 * we replace any references within the Document Bundle to the placeholder UUID
	 * with the actual resource ID assigned to the Patient.
	 */
	private void documentRepositoryHandlePatientByPlaceholder(
			RequestDetails theRequestDetails,
			IBaseBundle theTransactionBundle,
			List<IBaseResource> theTransactionBundleResources,
			IBaseResource documentPatientResource,
			@Nonnull IIdType subjectId,
			List<BundleEntryParts> theDocumentEntries) {
		String patientFhirId;
		Optional<SystemAndValue> managedIdentifier = getManagedIdentifierAndThrowIfMultiple(documentPatientResource);
		if (managedIdentifier.isEmpty()) {
			throw new PreconditionFailedException(
					"Document Composition.subject must be a Patient reference with an ID or an identifier matching the configured Pre-Assigned Patient Identifier Systems");
		}

		Predicate<IBaseResource> matcher = r -> {
			Optional<SystemAndValue> bundleResourceManagedIdentifier =
					getManagedIdentifierAndThrowIfMultiple(documentPatientResource);
			return bundleResourceManagedIdentifier
					.map(theSystemAndValue -> theSystemAndValue.equals(managedIdentifier.get()))
					.orElse(false);
		};

		patientFhirId = myResourceIdentifierCacheSvc.getFhirIdAssociatedWithUniquePatientIdentifier(
				theRequestDetails,
				myDefaultPartition,
				managedIdentifier.get().system(),
				managedIdentifier.get().value(),
				() -> UUID.randomUUID().toString());

		IBaseResource foundExisting = findPatientInTransaction(theTransactionBundleResources, matcher);
		if (foundExisting == null) {
			IBaseResource bundlePatientResource = myTerser.clone(documentPatientResource);
			BundleBuilder bundleBuilder = new BundleBuilder(myFhirContext, theTransactionBundle);
			bundlePatientResource.setUserData(JpaConstants.RESOURCE_ID_SERVER_ASSIGNED_VALUE, patientFhirId);
			bundlePatientResource.setId(patientFhirId);
			bundleBuilder
					.addTransactionCreateEntry(bundlePatientResource, subjectId.getValue())
					.conditional("Patient?_id=Patient/" + patientFhirId);
		}

		/*
		 * If the Patient in the document bundle had a placeholder ID, replace any
		 * references within the Bundle with the actual resource ID assigned to the
		 * Patient.
		 */
		String originalPatientId = subjectId.getValue();
		for (BundleEntryParts entry : theDocumentEntries) {
			IBaseResource resource = entry.getResource();
			if (resource != null) {
				if (Objects.equals(resource.getIdElement().getValue(), originalPatientId)) {
					resource.setId("Patient/" + patientFhirId);
				}

				List<ResourceReferenceInfo> references = myTerser.getAllResourceReferences(resource);
				for (ResourceReferenceInfo reference : references) {
					if (reference
							.getResourceReference()
							.getReferenceElement()
							.getValue()
							.equals(originalPatientId)) {
						reference.getResourceReference().setReference("Patient/" + patientFhirId);
					}
				}
			}
		}
	}

	@Nullable
	private IBaseResource findPatientInTransaction(
			List<IBaseResource> theTransactionBundleResources, Predicate<IBaseResource> matcher) {
		for (IBaseResource bundleResource : theTransactionBundleResources) {
			if ("Patient".equals(myFhirContext.getResourceType(bundleResource))) {
				boolean foundExisting = matcher.test(bundleResource);
				if (foundExisting) {
					return bundleResource;
				}
			}
		}
		return null;
	}

	/**
	 * Replace Encounter?subject.identifier=AAA|BBB with Encounter?subject=Patient/123
	 * for any resource types that are in the patient compartment (such as Encounter)
	 */
	private boolean massageSearchMapForNonPatientResource(
			RequestDetails theRequestDetails,
			SearchParameterMap theSearchParameterMap,
			String theResourceType,
			Map<String, String> theFullUrlToResourceIdSubstitution) {
		boolean changed = false;
		List<RuntimeSearchParam> patientCompartmentSpNames =
				myFhirContext.getResourceDefinition(theResourceType).getSearchParamsForCompartmentName("Patient");
		for (RuntimeSearchParam sp : patientCompartmentSpNames) {
			String spName = sp.getName();
			List<List<IQueryParameterType>> compartmentAndParams = theSearchParameterMap.get(spName);
			if (compartmentAndParams == null) {
				continue;
			}

			for (List<IQueryParameterType> compartmentOrParams : compartmentAndParams) {
				for (IQueryParameterType compartmentParam : compartmentOrParams) {
					ReferenceParam nextParamRef = (ReferenceParam) compartmentParam;
					if (nextParamRef.hasChain() && nextParamRef.getChain().equals("identifier")) {
						/*
						 * If the parameter is a chained reference to a Patient identifier,
						 * replace it with a resolved Patient ID. This handles cases
						 * such as
						 *    Observation?subject.identifier=http://foo|123
						 *
						 */
						TokenParam nextParamToken = new TokenParam();
						nextParamToken.setValueAsQueryToken(myFhirContext, "identifier", null, nextParamRef.getValue());
						String system = nextParamToken.getSystem();
						String value = nextParamToken.getValue();
						if (isNotBlank(system) && isNotBlank(value)) {
							if (isPreResolvePatientIdentifierSystem(system)) {
								Optional<String> fhirIdOpt =
										myResourceIdentifierCacheSvc.getFhirIdAssociatedWithUniquePatientIdentifier(
												theRequestDetails, myDefaultPartition, system, value);
								if (fhirIdOpt.isPresent()) {
									nextParamRef.setChain(null);
									nextParamRef.setValue("Patient/" + fhirIdOpt.get());
									changed = true;
								}
							}
						}
					} else {
						/*
						 * If the parameter is a placeholder URI for a Patient, replace it with the
						 * resolved Patient ID. This handles cases such as a conditional operation
						 * with a URL such as
						 *    Observation?subject=urn:uuid:xxxxxxxxx
						 */
						String paramValue = nextParamRef.getValue();
						String substitution = theFullUrlToResourceIdSubstitution.get(paramValue);
						if (substitution != null) {
							nextParamRef.setValue(substitution);
							changed = true;
						}
					}
				}
			}
		}
		return changed;
	}

	/**
	 * In Document Repository mode, replace any queries for
	 * <code>composition.subject.identifier=http://foo|123</code>
	 * with a query for
	 * <code>composition.subject=Patient/123</code>
	 */
	private boolean massageSearchMapForDocumentRepositoryBundleResource(
			RequestDetails theRequestDetails, SearchParameterMap theSearchParameterMap) {
		boolean changed = false;
		List<List<IQueryParameterType>> compositionAndList = theSearchParameterMap.get("composition");
		if (compositionAndList != null) {
			for (List<IQueryParameterType> compositionOrList : compositionAndList) {
				for (IQueryParameterType compositionOrParam : compositionOrList) {
					ReferenceParam reference = (ReferenceParam) compositionOrParam;
					if ("subject.identifier".equals(reference.getChain())) {
						TokenParam nextParamToken = new TokenParam();
						nextParamToken.setValueAsQueryToken(myFhirContext, "identifier", null, reference.getValue());
						String system = nextParamToken.getSystem();
						String value = nextParamToken.getValue();
						if (isNotBlank(system) && isNotBlank(value)) {
							if (isPreResolvePatientIdentifierSystem(system)) {
								Optional<String> fhirIdOpt =
										myResourceIdentifierCacheSvc.getFhirIdAssociatedWithUniquePatientIdentifier(
												theRequestDetails, myDefaultPartition, system, value);
								if (fhirIdOpt.isPresent()) {
									reference.setValueAsQueryToken(
											myFhirContext, null, ".subject", "Patient/" + fhirIdOpt.get());
									changed = true;
								}
							}
						}
					}
				}
			}
		}
		return changed;
	}

	/**
	 * Replace Patient?identifier=AAA|BBB with Patient/123.
	 * If the identifier system is pre-resolvable but no patient exists with that identifier,
	 * the {@link CdrPatientIdPartitionInterceptor#PATIENT_IDENTIFIER_PRE_RESOLVE_NO_MATCH} flag is
	 * added to the request details.
	 */
	private boolean massageSearchMapForPatientResource(
			RequestDetails theRequestDetails, SearchParameterMap theSearchParameterMap) {
		List<List<IQueryParameterType>> identifierAndParams = theSearchParameterMap.get(Patient.SP_IDENTIFIER);
		if (identifierAndParams == null) {
			return false;
		}

		boolean changed = false;
		for (int andIdx = 0; andIdx < identifierAndParams.size(); andIdx++) {

			List<IQueryParameterType> identifierOrList = identifierAndParams.get(andIdx);
			List<TokenParam> idOrList = new ArrayList<>();
			boolean foundHits = false;
			boolean foundMisses = false;
			for (int orIdx = 0; orIdx < identifierOrList.size(); orIdx++) {
				IQueryParameterType identifierOrParam = identifierOrList.get(orIdx);
				TokenParam nextParamToken = (TokenParam) identifierOrParam;
				String system = nextParamToken.getSystem();
				String value = nextParamToken.getValue();
				boolean hit = false;
				if (isNotBlank(system) && isNotBlank(value)) {
					if (isPreResolvePatientIdentifierSystem(system)) {
						hit = true;
						Optional<String> fhirIdOpt =
								myResourceIdentifierCacheSvc.getFhirIdAssociatedWithUniquePatientIdentifier(
										theRequestDetails, myDefaultPartition, system, value);
						if (fhirIdOpt.isPresent()) {
							idOrList.add(new TokenParam("Patient/" + fhirIdOpt.get()));
						}
					}
				}

				if (hit) {
					foundHits = true;
				} else {
					foundMisses = true;
					break;
				}
			}

			if (foundHits && !foundMisses) {
				identifierAndParams.remove(andIdx);
				andIdx--;
				if (idOrList.isEmpty()) {
					/*
					 * If none of the identifiers were pre-resolvable, use a dummy ID that will
					 * not resolve to any real ID, so that we return an empty bundle. The ID "_"
					 * is not a valid FHIR ID, so it can never match a real resource.
					 */
					theSearchParameterMap.add(IAnyResource.SP_RES_ID, new TokenOrListParam(null, "Patient/_"));
				} else {
					theSearchParameterMap.add(IAnyResource.SP_RES_ID, new TokenOrListParam(idOrList));
				}
				changed = true;
			}
		}

		return changed;
	}

	/**
	 * If a resource is being created and has an identifier that is eligible for pre-resolution,
	 * make sure that identifier is in the pre-resolution table. This is needed for cases where
	 * a Patient is created manually with such an identifier, but then gets referenced in a
	 * conditional URL later.
	 */
	private void ensurePatientIdentifierIsSavedInPreResolutionTable(
			RequestDetails theRequestDetails, IBaseResource theResource) {
		Optional<SystemAndValue> oldManagedIdentifier = getManagedIdentifierAndThrowIfMultiple(theResource);
		if (oldManagedIdentifier.isPresent()) {

			String expectedId = theResource.getIdElement().getIdPart();
			Validate.notBlank(expectedId, "Resource ID has not yet been assigned");

			String system = oldManagedIdentifier.get().system();
			String value = oldManagedIdentifier.get().value();
			String actualId = myResourceIdentifierCacheSvc.getFhirIdAssociatedWithUniquePatientIdentifier(
					theRequestDetails, myDefaultPartition, system, value, () -> expectedId);

			if (!expectedId.equals(actualId)) {
				String msg = "Can not create resource with duplicate identifier: " + UrlUtil.sanitizeUrlPart(system)
						+ "|" + UrlUtil.sanitizeUrlPart(value) + " (identifier belongs to resource with ID: Patient/"
						+ actualId + ")";
				throw new PreconditionFailedException(msg);
			}
		}
	}

	/**
	 * For any resources in the TX bundle that are conditionally created/updated Patient
	 * resources, we use the {@link IResourceIdentifierCacheSvc} to pre-resolve the
	 * Patient identifier and convert the operation into a normal update by ID (i.e.
	 * a PUT with the pre-resolved patient ID). This operation will assign a new
	 * UUID ID if needed.
	 */
	private void preResolvePatientIdsInBundleEntryConditionalUrls(
			RequestDetails theRequestDetails, IBaseBundle theBundle) {
		Map<String, String> fullUrlToResourceIdSubstitution = new HashMap<>();

		List<ModifiableBundleEntryParts> entries = BundleUtil.toListOfEntriesModifiable(myFhirContext, theBundle);
		massageBundleConditionalUrlsForPatient(theRequestDetails, entries, fullUrlToResourceIdSubstitution);
		massageBundleConditionalUrlsForNonPatient(theRequestDetails, entries, fullUrlToResourceIdSubstitution);
		massageBundleEntryReferences(theRequestDetails, entries, fullUrlToResourceIdSubstitution);

		// FIXME
//		if (myDocumentRepositoryEnabled) {
//			massageBundleConditionalUrlsForDocuments(theRequestDetails, entries, fullUrlToResourceIdSubstitution);
//		}
	}

	private void massageBundleConditionalUrlsForPatient(
			RequestDetails theRequestDetails,
			List<ModifiableBundleEntryParts> entries,
			Map<String, String> fullUrlToResourceIdSubstitution) {
		for (ModifiableBundleEntryParts entry : entries) {
			String conditionalUrl = entry.getConditionalUrl();
			if (isPatientConditionalUrl(conditionalUrl)) {
				RuntimeResourceDefinition patientDefinition = myFhirContext.getResourceDefinition("Patient");
				SearchParameterMap searchParameterMap =
						myMatchUrlService.translateMatchUrl(conditionalUrl, patientDefinition);
				List<List<IQueryParameterType>> identifierParamsAnd = searchParameterMap.get(Patient.SP_IDENTIFIER);
				boolean urlAcceptable = false;

				if (isSingleAndOrValue(identifierParamsAnd)) {

					// replacePatientIdentifierParamWithId creates a new conditional URL and would ignore
					// any other parameters that were present in the URL
					if (searchParameterMap.size() > 1) {
						throw new PreconditionFailedException(
								"Can not include multiple parameters in Patient conditional URL: "
										+ UrlUtil.sanitizeUrlPart(conditionalUrl));
					}

					TokenParam identifierParam =
							(TokenParam) identifierParamsAnd.get(0).get(0);
					if (identifierParam.getModifier() == null
							&& identifierParam.getMissing() == null
							&& !identifierParam.isMdmExpand()) {
						if (isNotBlank(identifierParam.getSystem()) && isNotBlank(identifierParam.getValue())) {
							if (isPreResolvePatientIdentifierSystem(identifierParam.getSystem())) {
								urlAcceptable = true;
								massagePatientBundleEntryInTransactionForPreResolvedIdentifier(
										theRequestDetails,
										entry,
										conditionalUrl,
										identifierParam,
										fullUrlToResourceIdSubstitution);
							} else {
								throw new PreconditionFailedException(
										"Identifier system " + UrlUtil.sanitizeUrlPart(identifierParam.getSystem())
												+ " has not been configured for Patient Resource ID-Identifier Mapping and can not be used in a conditional URL");
							}
						}
					}
				} else if (identifierParamsAnd != null) {
					throw new PreconditionFailedException(
							"Patient conditional URLs on this server must not contain multiple identifiers");
				} else {

					List<List<IQueryParameterType>> resIdParameter = searchParameterMap.get(IAnyResource.SP_RES_ID);
					if (isSingleAndOrValue(resIdParameter)) {
						urlAcceptable = true;
						BaseParam resIdParam = (BaseParam) resIdParameter.get(0).get(0);
						IdType parsedId = new IdType(resIdParam.getValueAsQueryToken());
						if (!parsedId.hasResourceType() || !parsedId.hasIdPart()) {
							throw new PreconditionFailedException(
									"Invalid _id value: " + UrlUtil.sanitizeUrlPart(resIdParam.getValueAsQueryToken()));
						}

						String entryFullUrl = entry.getFullUrl();
						if (Strings.CS.startsWith(entryFullUrl, "urn:uuid:")) {
							fullUrlToResourceIdSubstitution.put(entryFullUrl, resIdParam.getValueAsQueryToken());
						}
					}
				}

				if (!urlAcceptable) {
					throw new PreconditionFailedException(
							"If Patient resources within a FHIR transaction are using a conditional URL, that URL must contain an identifier (identifier) or ID (_id) parameter");
				}
			} else if (isNotBlank(entry.getFullUrl())) {

				if (entry.getMethod() == RequestTypeEnum.PUT) {
					if (!entry.getUrl().contains("?")) {
						IIdType id = entry.getResource().getIdElement();
						if (!id.hasResourceType()) {
							id = id.withResourceType(myFhirContext.getResourceType(entry.getResource()));
						}
						fullUrlToResourceIdSubstitution.put(
								entry.getFullUrl(),
								id.toUnqualifiedVersionless().getValue());
					}
				}
			}
		}
	}

	private void massageBundleConditionalUrlsForNonPatient(
			RequestDetails theRequestDetails,
			List<ModifiableBundleEntryParts> entries,
			Map<String, String> fullUrlToResourceIdSubstitution) {
		for (ModifiableBundleEntryParts entry : entries) {
			String conditionalUrl = entry.getConditionalUrl();
			if (isNotBlank(conditionalUrl)
					&& !isPatientConditionalUrl(conditionalUrl)
					&& conditionalUrl.contains("?")) {
				MatchUrlService.ResourceTypeAndSearchParameterMap parsedMatchUrl;
				try {
					parsedMatchUrl = myMatchUrlService.parseAndTranslateMatchUrl(conditionalUrl);
				} catch (IllegalArgumentException | DataFormatException e) {
					throw new InvalidRequestException("Failed to parse conditional URL: "
							+ UrlUtil.sanitizeUrlPart(conditionalUrl) + " - " + e.getMessage());
				}
				SearchParameterMap matchUrl = parsedMatchUrl.searchParameterMap();
				String resourceType = parsedMatchUrl.resourceDefinition().getName();
				if (massageSearchMap(theRequestDetails, matchUrl, resourceType, fullUrlToResourceIdSubstitution)) {
					String newConditionalUrl = matchUrl.toNormalizedQueryString();
					entry.setConditionalUrl(resourceType + newConditionalUrl);
				}
			}
		}
	}

	private void massageBundleEntryReferences(
			RequestDetails theRequestDetails,
			List<ModifiableBundleEntryParts> entries,
			Map<String, String> fullUrlToResourceIdSubstitution) {
		for (ModifiableBundleEntryParts entry : entries) {
			if (entry.getResource() != null) {
				List<ResourceReferenceInfo> references = myTerser.getAllResourceReferences(entry.getResource());
				for (ResourceReferenceInfo referenceInfo : references) {
					IBaseReference reference = referenceInfo.getResourceReference();

					// Is this a reference to a placeholder (urn:uuid:*) or another reference that we
					// have pre-resolved in this transaction already?
					String replacementReference = fullUrlToResourceIdSubstitution.get(
							reference.getReferenceElement().getValue());
					if (replacementReference != null) {
						reference.setReference(replacementReference);
						continue;
					}

					// Is this a Patient?identifier=XXX reference?
					replaceConditionalPatientReferenceWithHardPatientReference(
							theRequestDetails, reference, fullUrlToResourceIdSubstitution);
				}
			}
		}
	}

	private void massageBundleConditionalUrlsForDocuments(
			RequestDetails theRequestDetails,
			List<ModifiableBundleEntryParts> entries,
			Map<String, String> fullUrlToResourceIdSubstitution) {
		for (ModifiableBundleEntryParts entry : entries) {
			IBaseResource resource = entry.getResource();
			if (resource != null && "Bundle".equals(myFhirContext.getResourceType(resource))) {
				if (!"document".equals(BundleUtil.getBundleType(myFhirContext, (IBaseBundle) resource))) {
					continue;
				}

				IBaseResource composition =
						myTerser.getSingleValueOrNull(resource, "entry.resource", IBaseResource.class);
				if (composition == null || !"Composition".equals(myFhirContext.getResourceType(composition))) {
					continue;
				}

				IBaseReference reference = myTerser.getSingleValueOrNull(composition, "subject", IBaseReference.class);
				replaceConditionalPatientReferenceWithHardPatientReference(
						theRequestDetails, reference, fullUrlToResourceIdSubstitution);
			}
		}
	}

	private void replaceConditionalPatientReferenceWithHardPatientReference(
			RequestDetails theRequestDetails,
			IBaseReference reference,
			Map<String, String> fullUrlToResourceIdSubstitution) {
		String referenceText = reference.getReferenceElement().getValue();
		if (isPatientConditionalUrl(referenceText)) {
			SearchParameterMap parsedMatchUrl =
					myMatchUrlService.translateMatchUrl(referenceText, myPatientResourceDefinition);
			if (massageSearchMap(theRequestDetails, parsedMatchUrl, "Patient", Map.of())) {

				parsedMatchUrl.clean();

				List<List<IQueryParameterType>> targetResourceIdsAnd = parsedMatchUrl.get(IAnyResource.SP_RES_ID);
				if (isSingleAndOrValue(targetResourceIdsAnd) && parsedMatchUrl.size() == 1) {
					// If the original reference was something like
					//    Patient?identifier=http://foo|123
					// we replace it with something like
					//    Patient/123
					TokenParam targetResourceIdParam =
							(TokenParam) targetResourceIdsAnd.get(0).get(0);
					String targetResourceId = targetResourceIdParam.getValue();
					fullUrlToResourceIdSubstitution.put(referenceText, targetResourceId);
					reference.setReference(targetResourceId);
				} else {
					// If the original reference had multiple parameters, such as
					//    Patient?identifier=http://foo|123&active=true
					// we replace it with something like
					//    Patient?_id=Patient/0000-0000-PAT0&active=true
					String newReferenceText = "Patient" + parsedMatchUrl.toNormalizedQueryString();
					reference.setReference(newReferenceText);
				}
			}
			return;
		}

		String replacement = fullUrlToResourceIdSubstitution.get(referenceText);
		if (replacement != null) {
			reference.setReference(replacement);
		}
	}

	/**
	 * When processing a FHIR transaction Bundle, if an entry contains a Patient with a
	 * conditional create/update/patch/delete on a pre-assigned identifier string, replace
	 * this with a reference to the pre-assigned FHIR ID.
	 */
	private void massagePatientBundleEntryInTransactionForPreResolvedIdentifier(
			RequestDetails theRequestDetails,
			ModifiableBundleEntryParts entry,
			String conditionalUrl,
			TokenParam identifierParam,
			Map<String, String> fullUrlToResourceIdSubstitution) {
		// If we're doing a create/update, the actual resource being stored
		// should also have the identifier from the conditional URL
		if (entry.getMethod() == RequestTypeEnum.POST || entry.getMethod() == RequestTypeEnum.PUT) {
			verifyResourceHasIdentifierForPreResolution(entry.getResource(), conditionalUrl, identifierParam);
		}

		String fhirId;
		boolean isPatchOrDelete =
				entry.getMethod() == RequestTypeEnum.PATCH || entry.getMethod() == RequestTypeEnum.DELETE;
		if (isPatchOrDelete) {
			// For PATCH/DELETE, the patient must already exist — don't create a phantom mapping
			Optional<String> fhirIdOpt = myResourceIdentifierCacheSvc.getFhirIdAssociatedWithUniquePatientIdentifier(
					theRequestDetails, myDefaultPartition, identifierParam.getSystem(), identifierParam.getValue());
			if (fhirIdOpt.isEmpty()) {
				if (entry.getMethod() == RequestTypeEnum.PATCH) {
					throw new ResourceNotFoundException(
							"Patient with identifier " + UrlUtil.sanitizeUrlPart(identifierParam.getSystem()) + "|"
									+ UrlUtil.sanitizeUrlPart(identifierParam.getValue()) + " was not found");
				}
				// For DELETE, use a transient UUID (not persisted to cache).
				// The DAO handles "not found" per FHIR spec (200/204).
				fhirId = UUID.randomUUID().toString();
			} else {
				fhirId = fhirIdOpt.get();
			}
		} else {
			fhirId = myResourceIdentifierCacheSvc.getFhirIdAssociatedWithUniquePatientIdentifier(
					theRequestDetails,
					myDefaultPartition,
					identifierParam.getSystem(),
					identifierParam.getValue(),
					() -> UUID.randomUUID().toString());
		}
		String replacementId = "Patient/" + fhirId;

		if (isPatchOrDelete) {
			// For PATCH, the resource is a patch body (e.g. Parameters), not a Patient - don't set Patient ID on it
			// For DELETE, there is no resource body
			// Convert from conditional to direct by-ID operation
			entry.setRequestUrl(replacementId);

		} else {
			entry.getResource().setId(replacementId);

			if (entry.getMethod() == RequestTypeEnum.POST) {
				entry.getResource().setUserData(JpaConstants.RESOURCE_ID_SERVER_ASSIGNED_VALUE, fhirId);
				entry.setConditionalUrl("Patient?_id=" + replacementId);
				entry.setMethod(RequestTypeEnum.POST);
				entry.setRequestUrl("Patient");
			} else {
				entry.getResource().setUserData(JpaConstants.RESOURCE_ID_SERVER_ASSIGNED, Boolean.TRUE);
				entry.setConditionalUrl(null);
				entry.setMethod(RequestTypeEnum.PUT);
				entry.setRequestUrl(replacementId);
			}
		}

		if (Strings.CS.startsWith(entry.getFullUrl(), "urn:uuid:")) {
			fullUrlToResourceIdSubstitution.put(entry.getFullUrl(), replacementId);
		}
	}

	/**
	 * Ensure that a Patient being created doesn't have multiple <code>Patient.identifier</code>
	 * values with a <code>Patient.identifier.system</code> matching the
	 * {@link ca.cdr.pers.module.BaseAppCtxPersistence#MEGASCALE_PID_MODES_PRE_RESOLVE_PATIENT_IDENTIFIER_SYSTEMS}
	 * specification
	 */
	private void ensurePatientOnlyHasOneIdentifierEligibleForPreAssignment(IBaseResource theResource) {
		assert myFhirContext.getResourceType(theResource).equals("Patient");
		getManagedIdentifierAndThrowIfMultiple(theResource);
	}

	/**
	 * Ensure that a Patient being updated doesn't modify the <code>Patient.identifier</code>
	 * value with a <code>Patient.identifier.system</code> matching the
	 * {@link ca.cdr.pers.module.BaseAppCtxPersistence#MEGASCALE_PID_MODES_PRE_RESOLVE_PATIENT_IDENTIFIER_SYSTEMS}
	 * specification
	 */
	private void ensurePatientIdentifierEligibleForPreAssignmentIsNotBeingModified(
			IBaseResource theOldResource, IBaseResource theNewResource) {
		assert myFhirContext.getResourceType(theOldResource).equals("Patient");
		assert myFhirContext.getResourceType(theNewResource).equals("Patient");

		Optional<SystemAndValue> oldManagedIdentifier = getManagedIdentifierAndThrowIfMultiple(theOldResource);
		Optional<SystemAndValue> newManagedIdentifier = getManagedIdentifierAndThrowIfMultiple(theNewResource);
		if (!Objects.equals(oldManagedIdentifier, newManagedIdentifier)) {
			String systems = describeSystems(
					oldManagedIdentifier.map(SystemAndValue::system).orElse(null),
					newManagedIdentifier.map(SystemAndValue::system).orElse(null));
			throw new PreconditionFailedException("Can not modify Patient.identifier value \""
					+ oldManagedIdentifier.map(SystemAndValue::value).orElse("(none)") + "\" with system: " + systems);
		}
	}

	private Optional<SystemAndValue> getManagedIdentifierAndThrowIfMultiple(IBaseResource theResource) {
		List<IBase> identifiers = myTerser.getValues(theResource, "identifier");
		String foundIdentifierSystem = null;
		String foundIdentifierValue = null;

		for (IBase identifier : identifiers) {
			String system = myTerser.getSinglePrimitiveValueOrNull(identifier, "system");
			if (isNotBlank(system)) {
				if (isPreResolvePatientIdentifierSystem(system)) {
					String value = myTerser.getSinglePrimitiveValueOrNull(identifier, "value");
					if (foundIdentifierValue != null) {
						String systems = describeSystems(system, foundIdentifierSystem);
						throw new PreconditionFailedException(
								"Patient must not have multiple identifiers with system(s): " + systems);
					}
					foundIdentifierSystem = system;
					foundIdentifierValue = value;
				}
			}
		}

		if (foundIdentifierSystem == null) {
			return Optional.empty();
		}

		return Optional.of(new SystemAndValue(foundIdentifierSystem, foundIdentifierValue));
	}

	/**
	 * Given an identifier system and value parsed out of a conditional URL, confirms that the given resource
	 * actually contains the identifier and would therefore actually match it. Throws a
	 * {@link PreconditionFailedException} if not.
	 */
	private void verifyResourceHasIdentifierForPreResolution(
			IBaseResource theResource, String theMatchUrl, TokenParam theIdentifier) {
		List<IBase> identifiers = myTerser.getValues(theResource, "identifier");
		for (IBase identifier : identifiers) {
			String system = myTerser.getSinglePrimitiveValueOrNull(identifier, "system");
			if (!Objects.equals(theIdentifier.getSystem(), system)) {
				continue;
			}
			String value = myTerser.getSinglePrimitiveValueOrNull(identifier, "value");
			if (!Objects.equals(theIdentifier.getValue(), value)) {
				continue;
			}
			return;
		}

		String msg = myFhirContext
				.getLocalizer()
				.getMessage(BaseTransactionProcessor.class, "invalidConditionalUrlResourceDoesntMatch", theMatchUrl);
		throw new PreconditionFailedException(msg);
	}

	private boolean isPreResolvePatientIdentifierSystem(String theSystem) {
		if (myPreResolveIdentifierPatientSystemsValues.contains(theSystem)) {
			return true;
		}
		for (Pattern pattern : myPreResolveIdentifierPatientSystemsPatterns) {
			if (pattern.matcher(theSystem).matches()) {
				return true;
			}
		}
		return false;
	}

	private boolean isPatientConditionalUrl(String theConditionalUrl) {
		return theConditionalUrl != null
				&& (theConditionalUrl.startsWith("Patient?") || theConditionalUrl.startsWith("/Patient?"));
	}

	@VisibleForTesting
	void setPreResolveIdentifierPatientSystemsForUnitTest(@Nonnull String thePreResolveIdentifierPatientSystems) {
		Validate.notNull(thePreResolveIdentifierPatientSystems, "preResolveIdentifierPatientSystems must not be null");
		myPreResolveIdentifierPatientSystems = thePreResolveIdentifierPatientSystems;
		initializePreResolveIdentifierSystemValues();
	}

	@VisibleForTesting
	public void setDocumentRepositoryEnabledForUnitTest(boolean theDocumentRepositoryEnabled) {
		// FIXME
//		myDocumentRepositoryEnabled = theDocumentRepositoryEnabled;
	}

	@Nonnull
	private static IBaseResource findResourceInEntries(List<BundleEntryParts> documentEntries, IBaseReference subject) {
		IBaseResource documentPatientResource = documentEntries.stream()
				.filter(t -> Objects.equals(
						t.getFullUrl(), subject.getReferenceElement().getValue()))
				.map(BundleEntryParts::getResource)
				.findFirst()
				.orElse(null);
		if (documentPatientResource == null) {
			throw new PreconditionFailedException("Document Bundles must contain a Patient resource");
		}
		return documentPatientResource;
	}

	private static boolean isSingleAndOrValue(List<List<IQueryParameterType>> theParamsAnd) {
		return theParamsAnd != null
				&& theParamsAnd.size() == 1
				&& theParamsAnd.get(0) != null
				&& theParamsAnd.get(0).size() == 1;
	}

	/**
	 * Given a collection of identifier.system values, produces a single string suitable for logging
	 * (but not intended to be machine processable)
	 */
	@Nonnull
	private static String describeSystems(String... theSystems) {
		return Arrays.stream(theSystems)
				.filter(Objects::nonNull)
				.distinct()
				.sorted()
				.collect(Collectors.joining(", "));
	}

	/**
	 * An identifier system and value
	 */
	private record SystemAndValue(String system, String value) {}
}
