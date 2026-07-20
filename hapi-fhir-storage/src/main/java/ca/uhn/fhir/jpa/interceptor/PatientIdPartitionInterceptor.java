/*-
 * #%L
 * HAPI FHIR Storage api
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.interceptor;

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementDefinition;
import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.i18n.HapiLocalizer;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.model.ReadPartitionIdRequestDetails;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.interceptor.model.TransactionResponseAssembledDetails;
import ca.uhn.fhir.interceptor.model.TransactionWriteAfterPrefetchDetails;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.dao.BaseStorageDao;
import ca.uhn.fhir.jpa.dao.ITransactionProcessorVersionAdapter;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.partition.BaseRequestPartitionHelperSvc;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.extractor.ISearchParamExtractor;
import ca.uhn.fhir.jpa.util.ResourceCompartmentUtil;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.api.StorageResponseCodeEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.MethodNotAllowedException;
import ca.uhn.fhir.rest.server.interceptor.InterceptorOrders;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import ca.uhn.fhir.storage.PreviousVersionReader;
import ca.uhn.fhir.storage.TransactionBundleNormalizer;
import ca.uhn.fhir.storage.interceptor.AutoCreatePlaceholderReferenceTargetRequest;
import ca.uhn.fhir.util.ExtensionUtil;
import ca.uhn.fhir.util.FhirTerser;
import ca.uhn.fhir.util.HapiExtensions;
import ca.uhn.fhir.util.ResourceReferenceInfo;
import ca.uhn.fhir.util.SearchParameterUtil;
import ca.uhn.fhir.util.UrlUtil;
import com.google.common.annotations.VisibleForTesting;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.apache.commons.lang3.Strings;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseExtension;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.IdType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static ca.uhn.fhir.jpa.interceptor.ResourceCompartmentStoragePolicy.alwaysUseDefaultPartition;
import static ca.uhn.fhir.jpa.interceptor.ResourceCompartmentStoragePolicy.mandatorySingleCompartment;
import static ca.uhn.fhir.jpa.interceptor.ResourceCompartmentStoragePolicy.nonUniqueCompartmentInDefault;
import static ca.uhn.fhir.jpa.util.ResourceCompartmentUtil.PATIENT_COMPARTMENT_SP_PATIENT;
import static ca.uhn.fhir.jpa.util.ResourceCompartmentUtil.PATIENT_COMPARTMENT_SP_SUBJECT;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * This interceptor allows JPA servers to be partitioned by Patient ID. It selects the compartment for read/create operations
 * based on the patient ID associated with the resource (and uses a default partition ID for any resources
 * not in the patient compartment).
 * This works better with {@link ca.uhn.fhir.jpa.api.config.JpaStorageSettings.IdStrategyEnum#UUID} and
 * {@link ca.uhn.fhir.jpa.model.config.PartitionSettings.CrossPartitionReferenceMode#ALLOWED_UNQUALIFIED}.
 */
@Interceptor
public class PatientIdPartitionInterceptor {
	public static final String PATIENT_COMPARTMENT_NONE = "NONE";
	private static final Logger ourLog = LoggerFactory.getLogger(PatientIdPartitionInterceptor.class);
	private static final String PATIENT_STR = "Patient";

	/**
	 * {@link TransactionDetails} user-data key under which {@link #resolvePatientReferencesAfterPreFetch} records the
	 * {@link RewrittenOutcome}s it applied (keyed by the resulting {@code Patient/id}), so
	 * {@link #restoreRewrittenPatientOutcomes} can restore the outcome the caller's original request implied.
	 */
	private static final String REWRITTEN_OUTCOMES_KEY =
			PatientIdPartitionInterceptor.class.getName() + "_rewrittenOutcomes";

	/**
	 * How a Patient entry's verb was rewritten, so the original outcome code can be restored once the rewritten verb
	 * has been processed.
	 */
	// Created by Claude Opus 4.7
	private enum RewriteIntent {
		UNCONDITIONAL_CREATE,
		CONDITIONAL_CREATE_NO_MATCH,
		CONDITIONAL_UPDATE_NO_MATCH
	}

	/**
	 * @param conditionalUrl the original conditional match URL, needed to render the outcome message; null for
	 *                       {@link RewriteIntent#UNCONDITIONAL_CREATE}.
	 */
	// Created by Claude Opus 4.7
	private record RewrittenOutcome(RewriteIntent intent, String conditionalUrl) {}

	@Autowired
	private FhirContext myFhirContext;

	@Autowired
	private ISearchParamExtractor mySearchParamExtractor;

	@Autowired
	private PartitionSettings myPartitionSettings;

	@Autowired
	private DaoRegistry myDaoRegistry;

	private Map<String, ResourceCompartmentStoragePolicy> myResourceTypeToCompartmentPolicy = Map.of();

	/**
	 * Constructor
	 */
	public PatientIdPartitionInterceptor(
			FhirContext theFhirContext,
			ISearchParamExtractor theSearchParamExtractor,
			PartitionSettings thePartitionSettings,
			DaoRegistry theDaoRegistry) {
		myFhirContext = theFhirContext;
		mySearchParamExtractor = theSearchParamExtractor;
		myPartitionSettings = thePartitionSettings;
		myDaoRegistry = theDaoRegistry;
	}

	/**
	 * Supplies a map of resource types to policies specifying how the interceptor will
	 * handle that resource type. Any resource types that are not found in the map will be
	 * assumed to have the following default behaviour:
	 * <ul>
	 * <li>
	 * Any resources not in the
	 * <a href="https://hl7.org/fhir/compartmentdefinition-patient.html">Patient Compartment</a>:
	 * {@link ResourceCompartmentStoragePolicy#alwaysUseDefaultPartition()}
	 * </li>
	 * <li>
	 * The <b>Group</b> and <b>List</b> resource: {@link ResourceCompartmentStoragePolicy#alwaysUseDefaultPartition()}
	 * </li>
	 * <li>
	 * The <b>Provenance</b> resource: {@link ResourceCompartmentStoragePolicy#nonUniqueCompartmentInDefault()}
	 * </li>
	 * <li>
	 * Any other resources in the
	 * <a href="https://hl7.org/fhir/compartmentdefinition-patient.html">Patient Compartment</a>:
	 * {@link ResourceCompartmentStoragePolicy#mandatorySingleCompartment()}
	 * </li>
	 * </ul>
	 * {@link ResourceCompartmentStoragePolicy#mandatorySingleCompartment()} behaviour.
	 * Other modes can be specified if you need to allow specific Patient Compartment resource
	 * types to be created and found even if they do not have a valid patient reference.
	 * <p>
	 * Resource types in the collection should be resources in the
	 * <a href="https://hl7.org/fhir/compartmentdefinition-patient.html">Patient Compartment</a>.
	 * </p>
	 *
	 * @throws NullPointerException     if the map is null or any entries in the map contain null values
	 * @throws IllegalArgumentException if any of the resource types in the map are not valid Patient Compartment resource types
	 * @since 8.8.0
	 */
	public void setResourceTypePolicies(
			@Nonnull Map<String, ResourceCompartmentStoragePolicy> thePatientCompartmentOptionalResourceTypes)
			throws NullPointerException, IllegalArgumentException {
		Validate.notNull(
				thePatientCompartmentOptionalResourceTypes,
				"thePatientCompartmentOptionalResourceTypes must not be null");
		for (Map.Entry<String, ResourceCompartmentStoragePolicy> entry :
				thePatientCompartmentOptionalResourceTypes.entrySet()) {
			String resourceType = entry.getKey();
			ResourceCompartmentStoragePolicy policy = entry.getValue();

			if (PATIENT_STR.equals(resourceType)) {
				throw new ConfigurationException(
						Msg.code(2864) + "Can not provide a resource type policy for resource type 'Patient'");
			}

			if (BaseRequestPartitionHelperSvc.NON_PARTITIONABLE_RESOURCE_NAMES.contains(resourceType)) {
				throw new ConfigurationException(Msg.code(2869)
						+ String.format(
								"Can not provide a resource type policy for non-partitionable resource type: %s",
								resourceType));
			}

			if (!myFhirContext.isKnownResourceType(resourceType)) {
				throw new ConfigurationException(Msg.code(2866)
						+ String.format(
								"Resource type '%s' is not a valid resource type, can not apply resource type policy",
								resourceType));
			}
			RuntimeResourceDefinition resourceDef = myFhirContext.getResourceDefinition(resourceType);

			ResourceCompartmentStoragePolicy strictnessMode = entry.getValue();
			if (strictnessMode == null) {
				throw new ConfigurationException(Msg.code(2867)
						+ String.format("Empty resource type policy for resource type: %s", resourceType));
			}

			if (policy.isOnlyAppliesToCompartmentResourceTypes()) {
				List<RuntimeSearchParam> compartmentSps =
						ResourceCompartmentUtil.getPatientCompartmentSearchParams(resourceDef);
				if (compartmentSps.isEmpty()) {
					throw new ConfigurationException(Msg.code(2868)
							+ String.format(
									"Resource type '%s' is not a Patient Compartment resource type, can not apply policy: %s",
									resourceType, policy.getName()));
				}
			}
		}

		myResourceTypeToCompartmentPolicy = Map.copyOf(thePatientCompartmentOptionalResourceTypes);
	}

	@Hook(Pointcut.STORAGE_PARTITION_IDENTIFY_CREATE)
	public RequestPartitionId identifyForCreate(IBaseResource theResource, RequestDetails theRequestDetails) {
		RuntimeResourceDefinition resourceDef = myFhirContext.getResourceDefinition(theResource);
		ResourceCompartmentStoragePolicy policy = getPolicyForResourceType(resourceDef);
		List<RuntimeSearchParam> compartmentSps =
				ResourceCompartmentUtil.getPatientCompartmentSearchParams(resourceDef);

		// Validate extension applicability before any policy-specific routing.
		// This rejects the extension on Patient and non-patient-compartment resources, and logs a
		// warning when it's present on a compartment resource under an ALWAYS_USE_* policy.
		validateExtensionApplicability(theResource, policy, compartmentSps);

		// Patient resources are always partitioned by their own ID — no policy override allowed.
		if (resourceDef.getName().equals(PATIENT_STR)) {
			IIdType idElement = theResource.getIdElement();
			if (idElement.getIdPart() == null || idElement.isUuid()) {
				throw new MethodNotAllowedException(
						Msg.code(1321)
								+ "Patient resource IDs must be client-assigned in patient compartment mode, or server id strategy must be UUID");
			}
			return provideSingleCompartmentPartition(theRequestDetails, idElement.getIdPart());
		}

		// ALWAYS_USE_* policies pin the partition for this resource type.
		Optional<RequestPartitionId> partitionFromPolicy = policy.getUsePartitionId(myPartitionSettings);
		if (partitionFromPolicy.isPresent()) {
			return partitionFromPolicy.get();
		}

		IBaseResource resource = theResource;
		if (theResource.isDeleted()) {
			// when a resource is deleted, the current version of the deleted resource is empty
			// and will definitely not have references to a patient.  in order to determine the
			// patient compartment of a deleted resource, we need to get its previous version.
			resource = getPreviousVersion(theResource);
		}

		Collection<String> oCompartmentIdentity = ResourceCompartmentUtil.getResourceCompartments(
				"Patient", resource, compartmentSps, mySearchParamExtractor);
		oCompartmentIdentity = deDuplicateCompartmentList(oCompartmentIdentity);

		// Check for patient-compartment extension override
		Optional<RequestPartitionId> partitionFromExtension =
				getPartitionFromCompartmentExtension(resource, theRequestDetails, policy, oCompartmentIdentity);
		if (partitionFromExtension.isPresent()) {
			return partitionFromExtension.get();
		}

		if (!oCompartmentIdentity.isEmpty()) {

			// One compartment
			if (oCompartmentIdentity.size() == 1) {
				return provideMultipleCompartmentPartition(theRequestDetails, oCompartmentIdentity);
			}

			// Multiple compartments
			Optional<RequestPartitionId> nonUniqueCompartmentPolicy =
					policy.getPartitionIdForNonUniqueCompartment(myPartitionSettings);
			if (nonUniqueCompartmentPolicy.isPresent()) {
				return nonUniqueCompartmentPolicy.get();
			}

			throw new InvalidRequestException(Msg.code(1324) + "Policy does not allow resource of type \""
					+ resourceDef.getName()
					+ "\" to be created in multiple Patient compartments: "
					+ oCompartmentIdentity.stream().map(t -> "Patient/" + t).collect(Collectors.joining(", ")));

		} else {
			Optional<RequestPartitionId> partitionId = policy.getPartitionIdForNoCompartment(myPartitionSettings);
			if (partitionId.isPresent()) {
				return partitionId.get();
			}

			return throwNonCompartmentMemberInstanceFailureResponse(resource);
		}
	}

	/**
	 * This hook ensures that auto-created placeholders in the patient compartment are given
	 * the same compartment membership as the source resource which resulted in the creation
	 * of the placeholder.
	 * <p>
	 * For example, suppose you have an ExplanationOfBenefit with references to a
	 * subject (ExplanationOfBenefit.patient = Patient/A) as well as a coverage
	 * reference (ExplanationOfBenefit.insurance = Coverage/B). In this case, the
	 * if the Coverage resource doesn't already exist it will be auto-created. Because
	 * Coverage is in the patient compartment, the auto-created placeholder will be
	 * given a reference to the same patient.
	 * </p>
	 *
	 * @since 8.10.0
	 */
	@Hook(
			value = Pointcut.STORAGE_PRE_AUTO_CREATE_PLACEHOLDER_REFERENCE,
			order = InterceptorOrders.AUTO_CREATE_PLACEHOLDER_MODIFY)
	void autoCreatePlaceholderReference(AutoCreatePlaceholderReferenceTargetRequest theRequest) {
		IBaseResource source = theRequest.getSourceResource();
		IBaseResource target = theRequest.getTargetResourceToCreate();
		assert source != null;
		assert target != null;

		RuntimeResourceDefinition targetDefinition = myFhirContext.getResourceDefinition(target.getClass());
		if (targetDefinition.getName().equals(PATIENT_STR)) {
			// Patient resources don't need any reference to the compartment owner added,
			// since they are the compartment owner.
			return;
		}

		// What patient compartment is the source resource in?
		List<IIdType> sourceCompartments =
				myFhirContext.newTerser().getCompartmentOwnersForResource(PATIENT_STR, source, Set.of());
		Optional<IIdType> sourceCompartmentOpt = sourceCompartments.stream()
				.filter(t -> t.getResourceType().equals(PATIENT_STR))
				.filter(t -> isNotBlank(t.getIdPart()))
				.findFirst();

		// Given all the possible SPs that define the patient compartment for the newly
		// created placeholder resource, find one whose path places a reference directly
		// off the root of the resource, and use it to set the compartment reference.
		List<RuntimeSearchParam> targetPatientCompartmentSPs =
				targetDefinition.getSearchParamsForCompartmentName(PATIENT_STR);
		if (sourceCompartmentOpt.isPresent() && !targetPatientCompartmentSPs.isEmpty()) {
			for (RuntimeSearchParam targetCompartmentSP : targetPatientCompartmentSPs) {
				String[] paths = SearchParameterUtil.splitSearchParameterExpressions(targetCompartmentSP.getPath());
				for (String path : paths) {
					int firstDotIdx = path.indexOf('.');
					if (firstDotIdx > 0) {
						String resourceName = path.substring(0, firstDotIdx);
						if (resourceName.equals(targetDefinition.getName())) {
							String restOfPath = path.substring(firstDotIdx + 1);
							BaseRuntimeChildDefinition child = targetDefinition.getChildByName(restOfPath);
							if (child != null) {

								BaseRuntimeElementDefinition<?> targetDef = child.getChildByName(restOfPath);
								if (targetDef != null && "Reference".equals(targetDef.getName())) {

									IBaseReference patientReference = (IBaseReference) myFhirContext
											.getElementDefinition("Reference")
											.newInstance();
									patientReference.setReference(
											sourceCompartmentOpt.get().getValueAsString());

									child.getMutator().addValue(target, patientReference);
									return;
								}
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Validates that the {@link HapiExtensions#EXT_PATIENT_COMPARTMENT} extension can be honored
	 * for the given resource. This extension allows explicitly specifying which Patient compartment
	 * a resource belongs to for partition selection. The partition is then determined based on that
	 * compartment assignment.
	 *
	 * <h3>Extension Values</h3>
	 * <ul>
	 *   <li>{@code "Patient/<id>"} — assigns the resource to the specified Patient's compartment.
	 *       The referenced Patient must be one of the resource's compartment members.</li>
	 *   <li>{@code "NONE"} — marks the resource as not belonging to any Patient compartment,
	 *       causing it to be stored in the non-compartmental (default) partition.</li>
	 * </ul>
	 *
	 * <h3>Resource Type Applicability (enforced here)</h3>
	 * <ul>
	 *   <li><b>Patient resource</b> — rejected. Patient is routed by its own ID, so the extension
	 *       can never be honored.</li>
	 *   <li><b>Non-patient-compartment resource type</b> (e.g., Organization) — rejected. The
	 *       extension only makes sense for resources that can belong to a Patient compartment.</li>
	 * </ul>
	 *
	 * <h3>Behavior by Policy</h3>
	 * <table>
	 *   <tr><th>Policy</th><th>{@code Patient/<id>}</th><th>{@code NONE}</th></tr>
	 *   <tr><td>MANDATORY_SINGLE_COMPARTMENT</td>
	 *       <td>Assigns to specified Patient's compartment</td>
	 *       <td>Rejected — this policy requires a Patient compartment</td></tr>
	 *   <tr><td>OPTIONAL_SINGLE_COMPARTMENT</td>
	 *       <td>Assigns to specified Patient's compartment</td>
	 *       <td>Treated as non-compartmental; stored in the default partition. Note: patient-ref
	 *           searches will not find resources in the default partition under this policy</td></tr>
	 *   <tr><td>NON_UNIQUE_COMPARTMENT_IN_DEFAULT</td>
	 *       <td>Assigns to specified Patient's compartment</td>
	 *       <td>Treated as non-compartmental; stored in the default partition</td></tr>
	 *   <tr><td>ALWAYS_USE_*</td>
	 *       <td colspan="2">Extension is ignored — policy always determines the partition.
	 *           A warning is logged. This is a deliberate design choice to keep {@code $merge} /
	 *           {@code $replace-references} working if a user changes the policy for Provenance
	 *           resources to a fixed partition.</td></tr>
	 * </table>
	 *
	 * <p>Value-level validation (string format, {@code NONE} vs policy compatibility,
	 * {@code Patient/<id>} membership check) is handled by
	 * {@link #getPartitionFromCompartmentExtension}.</p>
	 *
	 * @param theResource                the resource being created
	 * @param thePolicy                  the compartment storage policy for this resource type
	 * @param theCompartmentSearchParams the patient compartment search params for this resource
	 *                                   type (passed in to avoid recomputation)
	 * @throws InvalidRequestException if the extension is present on a Patient or a
	 *                                 non-patient-compartment resource type
	 */
	private void validateExtensionApplicability(
			IBaseResource theResource,
			ResourceCompartmentStoragePolicy thePolicy,
			List<RuntimeSearchParam> theCompartmentSearchParams) {
		// hasExtension safely returns false for resources that don't support extensions
		// (Bundle, Parameters, etc. — types that extend Resource directly instead of DomainResource)
		if (!ExtensionUtil.hasExtension(theResource, HapiExtensions.EXT_PATIENT_COMPARTMENT)) {
			return;
		}

		String resourceType = theResource.fhirType();

		// Reject on Patient — Patient is routed by its own ID, the extension can never be honored
		if (PATIENT_STR.equals(resourceType)) {
			throw new InvalidRequestException(Msg.code(2908)
					+ String.format(
							"Extension %s is not applicable to Patient resources",
							HapiExtensions.EXT_PATIENT_COMPARTMENT));
		}

		// Reject on non-patient-compartment resources — extension can never be honored.
		if (theCompartmentSearchParams.isEmpty()) {
			throw new InvalidRequestException(Msg.code(2909)
					+ String.format(
							"Extension %s is not applicable to resource type '%s': not a Patient compartment resource type",
							HapiExtensions.EXT_PATIENT_COMPARTMENT, resourceType));
		}

		// Compartment resource with an ALWAYS_USE_* policy: both the extension and the policy
		// want to control the partition. By design, we let the policy win and log a warning
		// rather than rejecting the request — this keeps $merge / $replace-references working
		// if an operator legitimately decides to pin Provenance to a fixed partition (e.g.,
		// ALWAYS_USE_DEFAULT_PARTITION to keep audit records in the default partition).
		if (thePolicy.getUsePartitionId(myPartitionSettings).isPresent()) {
			ourLog.warn(
					"Extension {} on resource type '{}' is a no-op: the active policy '{}' fixes the partition "
							+ "and takes precedence over the extension.",
					HapiExtensions.EXT_PATIENT_COMPARTMENT,
					resourceType,
					thePolicy.getName());
		}
	}

	/**
	 * Resolves the partition from the {@link HapiExtensions#EXT_PATIENT_COMPARTMENT} extension
	 * value if the extension is present on the resource. Handles value-level validation:
	 * validating the value as a string, handling the {@code NONE} sentinel (including rejecting
	 * it under policies that require a Patient compartment), parsing {@code Patient/<id>} form,
	 * and verifying the ID is one of the resource's patient compartments.
	 * <p>
	 * Resource-level applicability and the full behavior matrix by policy are documented on
	 * {@link #validateExtensionApplicability}.
	 * </p>
	 *
	 * @param theResource              the resource to check for the extension
	 * @param theRequestDetails        the request details
	 * @param thePolicy                the compartment storage policy for this resource type
	 * @param theCompartmentIdentities the patient compartment IDs extracted from the resource
	 * @return the override partition, or empty if the extension is absent
	 * @throws InvalidRequestException if the extension is present but the value is invalid
	 */
	private Optional<RequestPartitionId> getPartitionFromCompartmentExtension(
			IBaseResource theResource,
			RequestDetails theRequestDetails,
			ResourceCompartmentStoragePolicy thePolicy,
			Collection<String> theCompartmentIdentities) {
		// hasExtension safely returns false for resources that don't support extensions
		if (!ExtensionUtil.hasExtension(theResource, HapiExtensions.EXT_PATIENT_COMPARTMENT)) {
			return Optional.empty();
		}
		IBaseExtension<?, ?> extension =
				ExtensionUtil.getExtensionByUrl(theResource, HapiExtensions.EXT_PATIENT_COMPARTMENT);

		IBase value = extension.getValue();
		if (!(value instanceof IPrimitiveType<?> primitiveValue) || !(primitiveValue.getValue() instanceof String)) {
			throw new InvalidRequestException(Msg.code(2894)
					+ String.format("Extension %s must have a string value", HapiExtensions.EXT_PATIENT_COMPARTMENT));
		}

		String stringValue = (String) primitiveValue.getValue();

		if (PATIENT_COMPARTMENT_NONE.equals(stringValue)) {
			if (!thePolicy.hasPartitionIdForNoCompartment()) {
				throw new InvalidRequestException(Msg.code(2895)
						+ String.format(
								"Extension %s value \"%s\" is not allowed with %s policy",
								HapiExtensions.EXT_PATIENT_COMPARTMENT, PATIENT_COMPARTMENT_NONE, thePolicy.getName()));
			}
			return Optional.of(RequestPartitionId.defaultPartition(myPartitionSettings));
		}

		IdDt parsedId = new IdDt(stringValue);
		String idPart = parsedId.getIdPart();
		if (!PATIENT_STR.equals(parsedId.getResourceType()) || isBlank(idPart)) {
			throw new InvalidRequestException(Msg.code(2896)
					+ String.format(
							"Extension %s has an invalid value: \"%s\". Must be \"%s\" or \"Patient/<id>\"",
							HapiExtensions.EXT_PATIENT_COMPARTMENT, stringValue, PATIENT_COMPARTMENT_NONE));
		}

		if (!theCompartmentIdentities.contains(idPart)) {
			String compartments =
					theCompartmentIdentities.stream().map(t -> "Patient/" + t).collect(Collectors.joining(", "));
			throw new InvalidRequestException(Msg.code(2897)
					+ String.format(
							"Extension %s references Patient/%s which is not a compartment of this resource. Compartments are: %s",
							HapiExtensions.EXT_PATIENT_COMPARTMENT, idPart, compartments));
		}

		return Optional.of(provideSingleCompartmentPartition(theRequestDetails, idPart));
	}

	private IBaseResource getPreviousVersion(IBaseResource theResource) {
		IFhirResourceDao<IBaseResource> resourceDao = myDaoRegistry.getResourceDao(theResource);
		PreviousVersionReader<IBaseResource> reader = new PreviousVersionReader<>(resourceDao);

		Optional<IBaseResource> oPreviousVersion = reader.readPreviousVersion(theResource);

		return oPreviousVersion.orElse(theResource);
	}

	/**
	 * @see #setResourceTypePolicies(Map) for a description of the default policies
	 */
	@VisibleForTesting
	@Nonnull
	public ResourceCompartmentStoragePolicy getPolicyForResourceType(ReadPartitionIdRequestDetails theReadDetails) {
		String resourceType = theReadDetails.getResourceType();
		if (isBlank(resourceType)) {
			return ResourceCompartmentStoragePolicy.alwaysUseDefaultPartition();
		}
		return getPolicyForResourceType(myFhirContext.getResourceDefinition(resourceType));
	}

	/**
	 * @see #setResourceTypePolicies(Map) for a description of the default policies
	 */
	@Nonnull
	private ResourceCompartmentStoragePolicy getPolicyForResourceType(RuntimeResourceDefinition resourceDef) {
		ResourceCompartmentStoragePolicy retVal = myResourceTypeToCompartmentPolicy.get(resourceDef.getName());
		if (retVal == null) {
			retVal = getDefaultPolicyForResourceType(resourceDef);
		}
		return retVal;
	}

	/**
	 * Returns the default {@link ResourceCompartmentStoragePolicy} for a given resource type
	 * when no explicit policy has been set via {@link #setResourceTypePolicies(Map)}.
	 *
	 * <p>Subclasses may override this method to change the default policy for specific resource types.
	 *
	 * @see #setResourceTypePolicies(Map) for a description of the default policies
	 */
	@Nonnull
	protected ResourceCompartmentStoragePolicy getDefaultPolicyForResourceType(RuntimeResourceDefinition resourceDef) {
		return switch (resourceDef.getName()) {
			case "Group", "List" -> alwaysUseDefaultPartition();
			case "Patient" -> mandatorySingleCompartment();
			case "Provenance" -> nonUniqueCompartmentInDefault();
			default -> {
				List<RuntimeSearchParam> compartmentSps =
						ResourceCompartmentUtil.getPatientCompartmentSearchParams(resourceDef);
				if (compartmentSps.isEmpty()) {
					yield alwaysUseDefaultPartition();
				} else {
					yield mandatorySingleCompartment();
				}
			}
		};
	}

	@Hook(Pointcut.STORAGE_PARTITION_IDENTIFY_READ)
	public RequestPartitionId identifyForRead(
			@Nonnull ReadPartitionIdRequestDetails theReadDetails, RequestDetails theRequestDetails) {

		if (!isEmpty(theReadDetails.getResourceType())) {
			ResourceCompartmentStoragePolicy policy = getPolicyForResourceType(theReadDetails);
			Optional<RequestPartitionId> partitionId = policy.getUsePartitionId(myPartitionSettings);
			if (partitionId.isPresent()) {
				return partitionId.get();
			}
		}

		Validate.notNull(
				theReadDetails.getRestOperationType(),
				"REST operation type must be set on RequestDetails before invoking partition identification.");

		//noinspection EnumSwitchStatementWhichMissesCases
		switch (theReadDetails.getRestOperationType()) {
			case DELETE:
			case PATCH:
			case READ:
			case VREAD:
			case HISTORY_INSTANCE:
			case SEARCH_TYPE:
				if (theReadDetails.getSearchParams() != null) {
					SearchParameterMap params = theReadDetails.getSearchParams();
					// When searching Patient resources directly, a pre-resolved partition hint may have been
					// passed down from ResourceLinkPredicateBuilder (via ReadPartitionIdRequestDetails) to
					// handle chained searches (e.g. subject=Patient/abc&subject.gender=female). We merge that
					// hint with the partition derived from any _id values present in the search, so both
					// the direct patient ref and any chained params target the correct partition.
					if (PATIENT_STR.equals(theReadDetails.getResourceType())) {
						RequestPartitionId readRequestPartitionId = theReadDetails.getRequestPartitionId();

						List<String> idParts = getResourceIdsForSearchParam(params, "_id");
						RequestPartitionId multiCompartmentRequestPartitionId =
								provideMultipleCompartmentPartition(theRequestDetails, idParts);

						if (nonNull(readRequestPartitionId) && readRequestPartitionId.hasPartitionIds()) {
							if (multiCompartmentRequestPartitionId.hasPartitionIds()) {
								readRequestPartitionId =
										readRequestPartitionId.mergeIds(multiCompartmentRequestPartitionId);
							}
						} else {
							readRequestPartitionId = multiCompartmentRequestPartitionId;
						}

						return readRequestPartitionId;
					} else if (isNotBlank(theReadDetails.getResourceType())) {
						RuntimeResourceDefinition resourceDef =
								myFhirContext.getResourceDefinition(theReadDetails.getResourceType());
						List<RuntimeSearchParam> compartmentSps =
								ResourceCompartmentUtil.getPatientCompartmentSearchParams(resourceDef, true);
						for (RuntimeSearchParam nextCompartmentSp : compartmentSps) {
							String paramName = nextCompartmentSp.getName();
							List<String> idParts = getResourceIdsForSearchParam(params, paramName);
							if (!idParts.isEmpty()) {
								ResourceCompartmentStoragePolicy policy = getPolicyForResourceType(theReadDetails);
								RequestPartitionId partitionId =
										provideMultipleCompartmentPartition(theRequestDetails, idParts);
								// If the policy defines a non-unique compartment partition (e.g.
								// NON_UNIQUE_COMPARTMENT_IN_DEFAULT uses the default partition), include
								// it in the search. A resource could live in the patient's partition
								// (single-patient reference) or the non-unique compartment partition
								// (multi-patient reference), so we need to search both.
								Optional<RequestPartitionId> nonUniqueCompartmentPartition =
										policy.getPartitionIdForNonUniqueCompartment(myPartitionSettings);
								if (nonUniqueCompartmentPartition.isPresent()) {
									partitionId =
											nonUniqueCompartmentPartition.get().mergeIds(partitionId);
								}
								return partitionId;
							}
						}
					}
				} else if (theReadDetails.getReadResourceId() != null) {
					if (PATIENT_STR.equals(theReadDetails.getResourceType())) {
						return provideSingleCompartmentPartition(
								theRequestDetails,
								theReadDetails.getReadResourceId().getIdPart());
					}
				}
				break;
			case EXTENDED_OPERATION_SERVER:
				String extendedOp = theReadDetails.getExtendedOperationName();
				if (ProviderConstants.OPERATION_EXPORT.equals(extendedOp)
						|| ProviderConstants.OPERATION_EXPORT_POLL_STATUS.equals(extendedOp)) {
					return provideNonPatientSpecificCompartment();
				}
				break;
			default:
				// nothing
		}

		if (isBlank(theReadDetails.getResourceType())) {
			return provideNonCompartmentMemberTypeResponse(theRequestDetails);
		}

		// If we couldn't identify a patient ID by the URL, let's try using the
		// conditional target if we have one
		if (theReadDetails.getConditionalTargetOrNull() != null) {
			return identifyForCreate(theReadDetails.getConditionalTargetOrNull(), theRequestDetails);
		}

		return provideNonPatientSpecificCompartment();
	}

	/**
	 * Requests transaction bundle normalization for the current transaction: the normalizer pre-shapes the bundle
	 * (placeholder fullUrls, identifier-bound in-bundle references, synthetic conditional creates for inline match
	 * URL references) so {@link #resolvePatientReferencesAfterPreFetch} can resolve every Patient entry to a
	 * concrete id for compartment routing. Normalization only runs for transactions where a registered interceptor
	 * requests it this way.
	 */
	// Created by Claude Fable 5
	@Hook(Pointcut.STORAGE_TRANSACTION_PROCESSING)
	public void requestTransactionBundleNormalization(@Nonnull TransactionDetails theTransactionDetails) {
		theTransactionDetails.putUserData(TransactionBundleNormalizer.NORMALIZATION_REQUESTED_KEY, Boolean.TRUE);
	}

	/**
	 * Once {@code preFetch} has resolved every conditional URL and reference target, resolve each Patient entry to a
	 * concrete ID and substitute all in-bundle references to it. This makes compartment placement independent of the
	 * order entries are processed in: a matched conditional Patient reuses the existing resource's ID; an
	 * unconditional Patient is assigned a UUID and its create is rewritten as a direct update so the ID sticks; an
	 * unmatched conditional Patient is assigned a UUID on the body but stays a conditional update, so in-bundle
	 * duplicates of the same match URL consolidate.
	 */
	// Created by Claude Opus 4.7
	@Hook(Pointcut.STORAGE_TRANSACTION_WRITE_AFTER_PREFETCH)
	public void resolvePatientReferencesAfterPreFetch(
			@Nonnull TransactionWriteAfterPrefetchDetails thePrefetchDetails,
			@Nonnull TransactionDetails theTransactionDetails) {
		List<IBase> entries = thePrefetchDetails.getEntries();

		// References given as inline Patient match URLs (present when the normalizer is not active) resolve here
		rewriteInlinePatientMatchUrlReferences(entries, theTransactionDetails);

		Map<String, String> idSubstitutions = resolvePatientEntryIds(thePrefetchDetails, theTransactionDetails);
		substituteReferences(thePrefetchDetails.getVersionAdapter(), entries, idSubstitutions);
	}

	/**
	 * Resolves each urn-fullUrl Patient entry to a concrete ID (reusing a matched conditional's existing ID, or
	 * minting one when the server assigns UUIDs) and returns the fullUrl/URL -> concrete-reference substitutions
	 * to apply to the rest of the bundle.
	 */
	// Created by Claude Fable 5
	private Map<String, String> resolvePatientEntryIds(
			TransactionWriteAfterPrefetchDetails thePrefetchDetails, TransactionDetails theTransactionDetails) {
		ITransactionProcessorVersionAdapter<IBaseBundle, IBase> versionAdapter = thePrefetchDetails.getVersionAdapter();

		// A placeholder Patient (urn:uuid id, no client id) can only be assigned a server id up front when the server
		// assigns UUIDs; otherwise its id isn't known until insert, too late for compartment routing. In that case we
		// leave the entry untouched.
		boolean serverAssignsUuids = thePrefetchDetails.getStorageSettings().getResourceServerIdStrategy()
				== JpaStorageSettings.IdStrategyEnum.UUID;

		Map<String, String> idSubstitutions = new HashMap<>();
		Map<String, String> matchUrlToMintedReference = new HashMap<>();
		Map<String, RewrittenOutcome> rewrittenOutcomes =
				theTransactionDetails.getOrCreateUserData(REWRITTEN_OUTCOMES_KEY, HashMap::new);

		for (IBase entry : thePrefetchDetails.getEntries()) {
			String fullUrl = versionAdapter.getFullUrl(entry);
			if (fullUrl == null || !fullUrl.startsWith("urn:uuid:")) {
				setIdOnIdlessMatchedConditionalPatientUpdate(versionAdapter, theTransactionDetails, entry);
				continue;
			}
			IBaseResource resource = versionAdapter.getResource(entry);
			if (resource == null || !PATIENT_STR.equals(myFhirContext.getResourceType(resource))) {
				continue;
			}

			String method = versionAdapter.getEntryRequestVerb(myFhirContext, entry);
			String url = versionAdapter.getEntryRequestUrl(entry);
			String matchUrl = null;
			if ("POST".equals(method)) {
				matchUrl = versionAdapter.getEntryRequestIfNoneExist(entry);
			} else if ("PUT".equals(method) && url != null && url.contains("?")) {
				matchUrl = url;
			}

			if (isBlank(matchUrl)) {
				if ("POST".equals(method)) {
					if (serverAssignsUuids) {
						String newReference = assignNewIdAndRewriteToPut(
								versionAdapter, theTransactionDetails, entry, resource, fullUrl, idSubstitutions);
						rewrittenOutcomes.put(
								newReference, new RewrittenOutcome(RewriteIntent.UNCONDITIONAL_CREATE, null));
					}
				} else if ("PUT".equals(method) && isNotBlank(url) && !Strings.CS.equals(fullUrl, url)) {
					idSubstitutions.put(fullUrl, url);
				}
			} else {
				PreFetchResolution resolution = getPreFetchResolution(matchUrl, theTransactionDetails);
				if (resolution.matched() && resolution.matchedId() == null) {
					// Matched, but without a reverse-mapped id (non-token match URL): the id is unknowable this
					// early, and minting a new id for a matched URL would be wrong — leave the entry untouched.
				} else if (resolution.matched()) {
					String matchedReference =
							resolution.matchedId().toUnqualifiedVersionless().getValue();
					idSubstitutions.put(fullUrl, matchedReference);
					// Stamp the matched id on the body so identifyForCreate can route the entry. The entry stays
					// conditional: the framework validates the body id against the match and reports the
					// conditional-match outcome natively. A conditional POST already NOPs to the match correctly,
					// so it is left alone.
					if ("PUT".equals(method)) {
						resource.setId(matchedReference);
					}
				} else if (serverAssignsUuids) {
					// Keep the entry conditional (PUT Patient?<matchUrl>) rather than rewriting it to a direct
					// update: the framework then consolidates in-bundle duplicates of the same match URL, and the
					// create path writes the HFJ_RES_SEARCH_URL row that makes a concurrent transaction creating
					// the same conditional URL collide instead of duplicating the patient. Duplicates share one
					// minted id so references to any of them resolve to the single created patient.
					String conditionalUrl = matchUrl.contains("?") ? matchUrl : PATIENT_STR + "?" + matchUrl;
					String newReference =
							matchUrlToMintedReference.computeIfAbsent(conditionalUrl, k -> mintPatientReference());
					idSubstitutions.put(fullUrl, newReference);
					rewriteAsPut(versionAdapter, entry, resource, newReference, conditionalUrl);
					RewriteIntent intent = "POST".equals(method)
							? RewriteIntent.CONDITIONAL_CREATE_NO_MATCH
							: RewriteIntent.CONDITIONAL_UPDATE_NO_MATCH;
					rewrittenOutcomes.putIfAbsent(newReference, new RewrittenOutcome(intent, matchUrl));
				}
			}
		}
		return idSubstitutions;
	}

	/** Replaces every in-bundle reference that matches a substitution key with its concrete reference. */
	// Created by Claude Fable 5
	private void substituteReferences(
			ITransactionProcessorVersionAdapter<IBaseBundle, IBase> theVersionAdapter,
			List<IBase> theEntries,
			Map<String, String> theIdSubstitutions) {
		if (theIdSubstitutions.isEmpty()) {
			return;
		}
		FhirTerser terser = myFhirContext.newTerser();
		for (IBase entry : theEntries) {
			IBaseResource resource = theVersionAdapter.getResource(entry);
			if (resource == null) {
				continue;
			}
			for (ResourceReferenceInfo reference : terser.getAllResourceReferences(resource)) {
				String referenceString =
						reference.getResourceReference().getReferenceElement().getValue();
				String substitution = theIdSubstitutions.get(referenceString);
				if (substitution != null) {
					reference.getResourceReference().setReference(substitution);
				}
			}
		}
	}

	/**
	 * Once the transaction has been processed, restore the operation outcome each rewritten Patient entry would have
	 * had if {@link #resolvePatientReferencesAfterPreFetch} hadn't rewritten its verb to make compartment placement
	 * order-independent.
	 */
	// Created by Claude Opus 4.7
	@Hook(Pointcut.STORAGE_TRANSACTION_RESPONSE_ASSEMBLED)
	public void restoreRewrittenPatientOutcomes(
			@Nonnull TransactionResponseAssembledDetails theResponseDetails,
			@Nonnull TransactionDetails theTransactionDetails) {
		ITransactionProcessorVersionAdapter<IBaseBundle, IBase> versionAdapter = theResponseDetails.getVersionAdapter();
		Map<String, RewrittenOutcome> rewrittenOutcomes = theTransactionDetails.getUserData(REWRITTEN_OUTCOMES_KEY);
		if (rewrittenOutcomes == null || rewrittenOutcomes.isEmpty()) {
			return;
		}

		List<IBase> entries = versionAdapter.getEntries(theResponseDetails.getResponseBundle());
		for (IBase entry : entries) {
			String location = versionAdapter.getResponseLocation(entry);
			if (isBlank(location)) {
				continue;
			}
			IdDt locationId = new IdDt(location);
			String reference = locationId.toUnqualifiedVersionless().getValue();
			RewrittenOutcome rewrite = rewrittenOutcomes.get(reference);
			if (rewrite == null) {
				continue;
			}

			StorageResponseCodeEnum code = restoredOutcomeCode(rewrite);
			// The message names the versioned id, as native outcomes do
			String versionedId = locationId.toUnqualified().getValue();
			String message = restoredOutcomeMessage(code, versionedId, rewrite.conditionalUrl());

			// An entry without a live outcome is left without one: this hook restores what the DAO would have
			// reported for the original verb, and a DAO that reports nothing would have reported nothing.
			IBaseOperationOutcome liveOutcome = versionAdapter.getResponseOutcome(entry);
			if (liveOutcome != null) {
				restorePrimaryIssue(liveOutcome, code, message);
			}
		}
	}

	/**
	 * Rewrites the live outcome's primary issue in place — diagnostics and StorageResponseCode coding — keeping
	 * everything else the DAO attached, such as auto-created-placeholder issues.
	 */
	// Created by Claude Fable 5
	private void restorePrimaryIssue(
			IBaseOperationOutcome theOutcome, StorageResponseCodeEnum theCode, String theMessage) {
		FhirTerser terser = myFhirContext.newTerser();
		List<IBase> issues = terser.getValues(theOutcome, "issue");
		if (issues.isEmpty()) {
			return;
		}
		IBase issue = issues.get(0);
		setPrimitive(terser, issue, "diagnostics", theMessage);
		List<IBase> codings = terser.getValues(issue, "details.coding");
		if (!codings.isEmpty()) {
			setPrimitive(terser, codings.get(0), "code", theCode.getCode());
			setPrimitive(terser, codings.get(0), "display", theCode.getDisplay());
		}
	}

	// Created by Claude Fable 5
	private static void setPrimitive(FhirTerser theTerser, IBase theTarget, String thePath, String theValue) {
		((IPrimitiveType<?>) theTerser.getValues(theTarget, thePath, true).get(0)).setValueAsString(theValue);
	}

	// Created by Claude Opus 4.7
	private StorageResponseCodeEnum restoredOutcomeCode(RewrittenOutcome theRewrite) {
		return switch (theRewrite.intent()) {
			case UNCONDITIONAL_CREATE -> StorageResponseCodeEnum.SUCCESSFUL_CREATE;
			case CONDITIONAL_CREATE_NO_MATCH -> StorageResponseCodeEnum.SUCCESSFUL_CREATE_NO_CONDITIONAL_MATCH;
			case CONDITIONAL_UPDATE_NO_MATCH -> StorageResponseCodeEnum.SUCCESSFUL_UPDATE_NO_CONDITIONAL_MATCH;
		};
	}

	// Created by Claude Opus 4.7
	private String restoredOutcomeMessage(StorageResponseCodeEnum theCode, String theId, String theConditionalUrl) {
		HapiLocalizer localizer = myFhirContext.getLocalizer();
		return switch (theCode) {
			case SUCCESSFUL_CREATE -> localizer.getMessageSanitized(BaseStorageDao.class, "successfulCreate", theId);
			case SUCCESSFUL_CREATE_NO_CONDITIONAL_MATCH -> localizer.getMessageSanitized(
					BaseStorageDao.class,
					"successfulCreateConditionalNoMatch",
					theId,
					UrlUtil.sanitizeUrlPart(theConditionalUrl));
				// Unlike the native message, fill the {1} match-URL slot the DAO leaves unformatted
			case SUCCESSFUL_UPDATE_NO_CONDITIONAL_MATCH -> localizer.getMessageSanitized(
					BaseStorageDao.class,
					"successfulUpdateConditionalNoMatch",
					theId,
					UrlUtil.sanitizeUrlPart(theConditionalUrl));
			default -> theCode.getDisplay();
		};
	}

	// Created by Claude Opus 4.7
	private String assignNewIdAndRewriteToPut(
			ITransactionProcessorVersionAdapter<IBaseBundle, IBase> theVersionAdapter,
			TransactionDetails theTransactionDetails,
			IBase theEntry,
			IBaseResource theResource,
			String theFullUrl,
			Map<String, String> theIdSubstitutions) {
		String newReference = mintPatientReference();
		// A freshly minted id cannot exist yet. Record that the same way preFetch records unresolvable
		// ids, so the update path skips its existence lookup.
		theTransactionDetails.addResolvedResourceId(new IdDt(newReference), null);
		theIdSubstitutions.put(theFullUrl, newReference);
		rewriteAsPut(theVersionAdapter, theEntry, theResource, newReference, newReference);
		return newReference;
	}

	/**
	 * Rewrites the entry as a PUT of the given id: a direct update when the request URL is the id itself, or a
	 * conditional update when it is a match URL.
	 */
	// Created by Claude Opus 4.7
	private void rewriteAsPut(
			ITransactionProcessorVersionAdapter<IBaseBundle, IBase> theVersionAdapter,
			IBase theEntry,
			IBaseResource theResource,
			String theReference,
			String theRequestUrl) {
		theResource.setId(theReference);
		theVersionAdapter.setRequestVerb(theEntry, "PUT");
		theVersionAdapter.setRequestUrl(theEntry, theRequestUrl);
		theVersionAdapter.setRequestIfNoneExist(theEntry, null);
	}

	/** Mints a concrete reference for a Patient the server will create under the UUID id strategy. */
	// Created by Claude Fable 5
	private static String mintPatientReference() {
		return PATIENT_STR + "/" + UUID.randomUUID();
	}

	/**
	 * Without a normalizer-assigned placeholder fullUrl, an id-less conditional Patient update that pre-fetch matched
	 * to an existing Patient still needs the matched id stamped on the body. The entry stays conditional, so the DAO
	 * reports the conditional-match outcome natively. An unmatched id-less conditional update is left untouched.
	 */
	// Created by Claude Fable 5
	private void setIdOnIdlessMatchedConditionalPatientUpdate(
			ITransactionProcessorVersionAdapter<IBaseBundle, IBase> theVersionAdapter,
			TransactionDetails theTransactionDetails,
			IBase theEntry) {
		String method = theVersionAdapter.getEntryRequestVerb(myFhirContext, theEntry);
		String url = theVersionAdapter.getEntryRequestUrl(theEntry);
		if (!"PUT".equals(method) || url == null || !url.contains("?")) {
			return;
		}
		IBaseResource resource = theVersionAdapter.getResource(theEntry);
		if (resource == null
				|| !PATIENT_STR.equals(myFhirContext.getResourceType(resource))
				|| resource.getIdElement().getIdPart() != null) {
			return;
		}
		IIdType matchedId = getPreFetchResolution(url, theTransactionDetails).matchedId();
		if (matchedId != null) {
			resource.setId(matchedId.toUnqualifiedVersionless().getValue());
		}
	}

	/**
	 * Rewrites each Patient-compartment reference given as an inline match URL (e.g.
	 * {@code Observation.subject = "Patient?identifier=http://acme.org/mrn|PT00062"}) to a literal
	 * {@code Patient/<id>}, so per-entry partition determination ({@link #identifyForCreate}) can route the resource to
	 * the correct Patient compartment.
	 * <p>
	 * The rewrite reuses what the pre-fetch already resolved into
	 * {@link TransactionDetails#getResolvedMatchUrls()} rather than issuing its own search, and touches only the
	 * compartment-defining references. Anything the pre-fetch did not resolve is left untouched; if a partition still
	 * cannot be determined for an entry, per-entry determination rejects the transaction.
	 * <p>
	 * This only ever has work to do when all-partition search is supported: on infrastructure that cannot search
	 * across all partitions, an entry whose inline match URL the pre-fetch has not resolved is rejected before
	 * pre-fetch runs (by per-transaction partition determination), so it never reaches this rewrite.
	 */
	private void rewriteInlinePatientMatchUrlReferences(
			List<IBase> theEntries, TransactionDetails theTransactionDetails) {
		FhirTerser terser = myFhirContext.newTerser();
		for (IBase entry : theEntries) {
			IBaseResource resource = terser.getSingleValueOrNull(entry, "resource", IBaseResource.class);
			if (resource == null) {
				continue;
			}
			RuntimeResourceDefinition resourceDef = myFhirContext.getResourceDefinition(resource);
			List<RuntimeSearchParam> compartmentSps =
					ResourceCompartmentUtil.getPatientCompartmentSearchParams(resourceDef);
			if (compartmentSps.isEmpty()) {
				continue;
			}
			ResourceCompartmentUtil.getResourceCompartmentReferences(resource, compartmentSps, mySearchParamExtractor)
					.forEach(reference -> {
						String referenceValue = reference.getReferenceElement().getValue();
						if (isPatientInlineMatchUrl(referenceValue)) {
							rewriteInlineMatchUrlReference(reference, referenceValue, theTransactionDetails);
						}
					});
		}
	}

	/**
	 * A Patient inline match URL is a reference whose value targets the Patient resource type with a search
	 * query, i.e. starts with {@code "Patient?"} or {@code "/Patient?"}.
	 */
	private static boolean isPatientInlineMatchUrl(@Nullable String theReferenceValue) {
		return theReferenceValue != null
				&& (theReferenceValue.startsWith(PATIENT_STR + "?")
						|| theReferenceValue.startsWith("/" + PATIENT_STR + "?"));
	}

	/**
	 * Rewrites a single Patient inline match URL reference to {@code Patient/<id>} using the resource ID the
	 * pre-fetch already resolved for it into {@link TransactionDetails#getResolvedMatchUrls()}. No search is
	 * performed here — we act only on what the pre-fetch put in the transaction details. If the match URL was not
	 * resolved to a concrete Patient there, the reference is left untouched and per-entry partition determination
	 * handles it.
	 */
	private void rewriteInlineMatchUrlReference(
			IBaseReference theReference, String theReferenceValue, TransactionDetails theTransactionDetails) {
		// Act only on a match URL the pre-fetch resolved to a concrete Patient; otherwise leave the reference as is.
		IIdType resolvedId =
				getPreFetchResolution(theReferenceValue, theTransactionDetails).matchedId();
		if (resolvedId != null) {
			theReference.setReference(resolvedId.toUnqualifiedVersionless().getValue());
		}
	}

	/**
	 * What the pre-fetch resolved for a match URL: {@code matched} is true when the pre-fetch found an existing
	 * resource. {@code matchedId} carries its FHIR id, or null when the match has no reverse-mapped id (non-token
	 * match URL) — such an entry is matched yet its id is unknowable this early, so callers that mint ids for
	 * unmatched URLs must not treat it as unmatched.
	 */
	// Created by Claude Fable 5
	private record PreFetchResolution(boolean matched, @Nullable IIdType matchedId) {}

	/**
	 * Consults what the pre-fetch resolved for the given match URL, via
	 * {@link TransactionDetails#getResolvedMatchUrls()} and the reverse-id map. No search is performed — we act
	 * only on what the pre-fetch put in the transaction details.
	 */
	// Created by Claude Fable 5
	private PreFetchResolution getPreFetchResolution(String theMatchUrl, TransactionDetails theTransactionDetails) {
		IResourcePersistentId<?> resolved =
				theTransactionDetails.getResolvedMatchUrls().get(theMatchUrl);
		if (resolved == null || resolved == TransactionDetails.NOT_FOUND) {
			return new PreFetchResolution(false, null);
		}
		return new PreFetchResolution(true, theTransactionDetails.getReverseResolvedId(resolved));
	}

	@SuppressWarnings("SameParameterValue")
	private List<String> getResourceIdsForSearchParam(SearchParameterMap theParams, String theParamName) {
		List<List<IQueryParameterType>> paramAndListForParamName = theParams.get(theParamName);
		if (paramAndListForParamName == null) {
			return Collections.emptyList();
		}

		Set<String> needingAtLeastOneChainedSpToResolveSet = new LinkedHashSet<>();
		List<String> idParts = new ArrayList<>();

		paramAndListForParamName.stream().flatMap(Collection::stream).forEach(aParam -> {
			if (aParam instanceof ReferenceParam referenceParam && nonNull(referenceParam.getChain())) {
				if (PATIENT_COMPARTMENT_SP_PATIENT.equals(theParamName)
						|| PATIENT_COMPARTMENT_SP_SUBJECT.equals(theParamName)) {
					// 'patient' and 'subject' SP have a 0..1 cardinality and will always refer to a Patient
					// resource. Chained SP on subject or patient can't be resolved on their own but if combined
					// with another SP resolving directly to a patient (?subject=Patient/abc), it is safe to
					// assume that the chained SP points to the same object.
					// Essentially, if we have one SP that resolves directly, we don't need to resolve other SP
					// pointing to the same resource.
					//
					// we keep track of the chained SP names needing direct resolution to support SP interchangeability:
					// ?subject=Patient/abc&subject.active=true == ?subject.active=true&subject=Patient/abc
					needingAtLeastOneChainedSpToResolveSet.add(referenceParam.getChain());
					return; // exits the forEach lambda, not the method
				}
				throw new MethodNotAllowedException(Msg.code(1323) + "The parameter " + theParamName + "."
						+ referenceParam.getChain() + " is not supported in patient compartment mode");
			}

			String qualifier = aParam.getQueryParameterQualifier();
			if (isNotBlank(qualifier) && !Constants.PARAMQUALIFIER_MDM.equals(qualifier)) {
				throw new MethodNotAllowedException(Msg.code(1322) + "The parameter " + theParamName + qualifier
						+ " is not supported in patient compartment mode");
			}
			String valueAsQueryToken = aParam.getValueAsQueryToken();
			if (Strings.CS.startsWith(valueAsQueryToken, "Patient/")
					|| Strings.CS.contains(valueAsQueryToken, "/Patient/")) {
				IdType id = new IdType(valueAsQueryToken);
				if (id.getResourceType().equals(PATIENT_STR)) {
					idParts.add(id.getIdPart());
				}
			} else if (valueAsQueryToken.indexOf('/') == -1) {
				IdType id = new IdType(valueAsQueryToken);
				if (id.isIdPartValid()) {
					idParts.add(valueAsQueryToken);
				}
			}
		});

		// Every value for this parameter has now been scanned. What we do with the result depends on
		// whether the infrastructure can fan a search out across all partitions.
		if (myPartitionSettings.isAllPartitionSearchSupported()) {
			// We can fan out across all partitions, so never reject here: return whatever we resolved
			// (an empty list just widens to an all-partition search).
			return idParts;
		}

		// The infrastructure cannot fan out, so a search must resolve to a known partition. Three cases:
		//   1. idParts is non-empty: we pinned the partition(s) directly from a concrete Patient id.
		//      Return the ids.
		//   2. idParts is empty and no chained subject/patient param was seen: this parameter expressed no
		//      patient scoping, so there is nothing to resolve. Return the empty list.
		//   3. idParts is empty but a chained subject/patient param was seen (e.g. subject.identifier=...):
		//      the only patient anchor is one we could not resolve to a concrete id, so we cannot honor
		//      the single-partition guarantee. Reject the search with HAPI-2928.
		// Only case 3 errors; cases 1 and 2 fall through and return normally.
		if (idParts.isEmpty() && !needingAtLeastOneChainedSpToResolveSet.isEmpty()) {
			throw new MethodNotAllowedException(Msg.code(2928)
					+ buildErrorMsgForChainedParameters(theParamName, needingAtLeastOneChainedSpToResolveSet));
		}

		return idParts;
	}

	private String buildErrorMsgForChainedParameters(String theParamName, @Nonnull Set<String> theChainedParameterSet) {
		return new StringBuilder("Could not resolve chained parameter(s) ")
				.append(theChainedParameterSet)
				.append(" on parameter ")
				.append(theParamName)
				.append(". Consider adding a direct Patient reference to your search (?")
				.append(theParamName)
				.append("=Patient/abc&")
				.append(theParamName)
				.append(".")
				.append(theChainedParameterSet.iterator().next())
				.append("=...)")
				.toString();
	}

	/**
	 * Return a partition or throw an error for FHIR operations that can not be used with this interceptor
	 */
	protected RequestPartitionId provideNonPatientSpecificCompartment() {
		return RequestPartitionId.allPartitions();
	}

	/**
	 * Generate the partition for a given patient resource ID. This method may be overridden in subclasses, but it
	 * may be easier to override {@link #providePartitionIdForPatientId(RequestDetails, String)} instead.
	 */
	@Nonnull
	protected RequestPartitionId provideSingleCompartmentPartition(
			RequestDetails theRequestDetails, String thePatientIdPart) {
		int partitionId = providePartitionIdForPatientId(theRequestDetails, thePatientIdPart);
		return RequestPartitionId.fromPartitionIdAndName(partitionId, thePatientIdPart);
	}

	@SuppressWarnings("unused")
	@Nullable
	protected RequestPartitionId provideMultipleCompartmentPartition(
			RequestDetails theRequestDetails, Collection<String> thePatientIdParts) {
		thePatientIdParts = deDuplicateCompartmentList(thePatientIdParts);
		RequestPartitionId partitionId = null;
		for (String compartmentId : thePatientIdParts) {
			RequestPartitionId compartmentPartition =
					provideSingleCompartmentPartition(theRequestDetails, compartmentId);
			if (partitionId != null) {
				partitionId = partitionId.mergeIds(compartmentPartition);
			} else {
				partitionId = compartmentPartition;
			}
		}

		if (partitionId == null) {
			partitionId = RequestPartitionId.allPartitions();
		}

		return partitionId;
	}

	/**
	 * Translates an ID (e.g. "ABC") into a compartment ID number.
	 * <p>
	 * The default implementation of this method returns:
	 * <code>Math.abs(thePatientIdPart.hashCode()) % 15000</code>.
	 * <p>
	 * This logic can be replaced with other logic of your choosing.
	 *
	 * @see #defaultPartitionAlgorithm(String)
	 */
	@SuppressWarnings("unused")
	protected int providePartitionIdForPatientId(RequestDetails theRequestDetails, String thePatientIdPart) {
		return defaultPartitionAlgorithm(thePatientIdPart);
	}

	/**
	 * Return a compartment ID (or throw an exception) when an attempt is made to search for a resource that is
	 * in the patient compartment, but without any search parameter identifying which compartment to search.
	 * <p>
	 * E.g. this method will be called for the search <code>Observation?code=foo</code> since the patient
	 * is not identified in the URL.
	 */
	@Nonnull
	protected RequestPartitionId throwNonCompartmentMemberInstanceFailureResponse(IBaseResource theResource) {
		throw new MethodNotAllowedException(Msg.code(1326) + "Resource of type "
				+ myFhirContext.getResourceType(theResource) + " has no values placing it in the Patient compartment");
	}

	/**
	 * Return a compartment ID (or throw an exception) when storing/reading resource types that
	 * are not in the patient compartment (e.g. ValueSet).
	 */
	@SuppressWarnings("unused")
	@Nonnull
	protected RequestPartitionId provideNonCompartmentMemberTypeResponse(RequestDetails theRequestDetails) {
		return myPartitionSettings.getDefaultRequestPartitionId();
	}

	/**
	 * De-duplicate if we have more than 1 (some resource types can have the patient in
	 * multiple fields, so it's possible to get the same patient more than once here)
	 */
	@Nonnull
	private static Collection<String> deDuplicateCompartmentList(Collection<String> oCompartmentIdentity) {
		if (oCompartmentIdentity.size() > 1) {
			oCompartmentIdentity = new HashSet<>(oCompartmentIdentity);
		}
		return oCompartmentIdentity;
	}

	/**
	 * This method supplies the default algorithm used for partitioning, if {@link #providePartitionIdForPatientId(RequestDetails, String)}
	 * has not been overridden.
	 */
	public static int defaultPartitionAlgorithm(String theResourceIdPart) {
		return Math.abs(theResourceIdPart.hashCode() % 15000);
	}
}
