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
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.auth.CompartmentSearchParameterModifications;
import ca.uhn.fhir.interceptor.model.ReadPartitionIdRequestDetails;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.partition.BaseRequestPartitionHelperSvc;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.extractor.ISearchParamExtractor;
import ca.uhn.fhir.jpa.util.ResourceCompartmentUtil;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.MethodNotAllowedException;
import ca.uhn.fhir.rest.server.interceptor.InterceptorOrders;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import ca.uhn.fhir.storage.PreviousVersionReader;
import ca.uhn.fhir.storage.interceptor.AutoCreatePlaceholderReferenceTargetRequest;
import ca.uhn.fhir.util.BundleUtil;
import ca.uhn.fhir.util.ExtensionUtil;
import ca.uhn.fhir.util.FhirTerser;
import ca.uhn.fhir.util.HapiExtensions;
import ca.uhn.fhir.util.ResourceReferenceInfo;
import ca.uhn.fhir.util.SearchParameterUtil;
import ca.uhn.fhir.util.bundle.BundleEntryParts;
import com.google.common.annotations.VisibleForTesting;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.apache.commons.lang3.Strings;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseExtension;
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
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static ca.uhn.fhir.interceptor.model.RequestPartitionId.getPartitionFromUserDataIfPresent;
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
	public static final String PLACEHOLDER_TO_REFERENCE_KEY =
			PatientIdPartitionInterceptor.class.getName() + "_placeholderToResource";
	private static final Logger ourLog = LoggerFactory.getLogger(PatientIdPartitionInterceptor.class);
	private static final String PATIENT_STR = "Patient";

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
			Set<RequestPartitionId> compartments =
					getPartitionViaPartiallyProcessedReference(theRequestDetails, policy, resource);
			if (compartments.size() == 1) {
				return compartments.iterator().next();
			} else if (compartments.isEmpty()) {
				Optional<RequestPartitionId> partitionId = policy.getPartitionIdForNoCompartment(myPartitionSettings);
				if (partitionId.isPresent()) {
					return partitionId.get();
				}
			} else {
				Optional<RequestPartitionId> partitionId =
						policy.getPartitionIdForNonUniqueCompartment(myPartitionSettings);
				if (partitionId.isPresent()) {
					return partitionId.get();
				}
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

	/**
	 * HACK: enable synthea bundles to sneak through with a server-assigned UUID.
	 * If we don't have a simple id for a compartment owner, maybe we're in a bundle during processing
	 * and a reference points to the Patient which has already been processed and assigned a partition.
	 */
	@SuppressWarnings("unchecked")
	@Nonnull
	private Set<RequestPartitionId> getPartitionViaPartiallyProcessedReference(
			RequestDetails theRequestDetails, ResourceCompartmentStoragePolicy thePolicy, IBaseResource theResource) {
		Map<String, IBaseResource> placeholderToReference = null;
		if (theRequestDetails != null) {
			placeholderToReference =
					(Map<String, IBaseResource>) theRequestDetails.getUserData().get(PLACEHOLDER_TO_REFERENCE_KEY);
		}
		if (placeholderToReference == null) {
			placeholderToReference = Map.of();
		}

		List<IBaseReference> references = myFhirContext
				.newTerser()
				.getCompartmentReferencesForResource(
						PATIENT_STR, theResource, new CompartmentSearchParameterModifications())
				.toList();

		Set<RequestPartitionId> retVal = new HashSet<>();
		for (IBaseReference reference : references) {
			String referenceString = reference.getReferenceElement().getValue();
			IBaseResource target = placeholderToReference.get(referenceString);
			if (target != null && Objects.equals(myFhirContext.getResourceType(target), PATIENT_STR)) {
				Optional<RequestPartitionId> partition = getPartitionFromUserDataIfPresent(target);
				if (partition.isPresent()) {
					retVal.add(partition.get());
				} else if (PATIENT_STR.equals(target.getIdElement().getResourceType())) {
					if (!target.getIdElement().isUuid() && target.getIdElement().hasIdPart()) {
						retVal.add(provideSingleCompartmentPartition(
								theRequestDetails, target.getIdElement().getIdPart()));
					}
				}
			}
		}

		return retVal;
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
	 * If we're about to process a FHIR transaction, we want to note the mappings between placeholder IDs
	 * and their resources and stuff them into a userdata map where we can access them later. We do this
	 * so that when we see a resource in the patient compartment (e.g. an Encounter) and it has a subject
	 * reference that's just a placeholder ID, we can look up the target of that and figure out which
	 * compartment that Encounter actually belongs to.
	 */
	@Hook(Pointcut.STORAGE_TRANSACTION_PROCESSING)
	public void transaction(RequestDetails theRequestDetails, IBaseBundle theBundle) {
		FhirTerser terser = myFhirContext.newTerser();

		/*
		 * If we have a Patient in the transaction bundle which is being POST-ed as a normal
		 * resource "create" (i.e., it will get a server-assigned ID), we'll proactively assign it an ID here.
		 *
		 * This is mostly a hack to get Synthea data working, but real clients could also be
		 * following the same pattern.
		 */
		List<IBase> rawEntries = new ArrayList<>(terser.getValues(theBundle, "entry", IBase.class));
		List<BundleEntryParts> parsedEntries = BundleUtil.toListOfEntries(myFhirContext, theBundle);
		Validate.isTrue(rawEntries.size() == parsedEntries.size(), "Parsed and raw entries don't match");

		Map<String, String> idSubstitutions = new HashMap<>();
		for (int i = 0; i < rawEntries.size(); i++) {
			BundleEntryParts nextEntry = parsedEntries.get(i);
			if (nextEntry.getResource() != null
					&& myFhirContext.getResourceType(nextEntry.getResource()).equals(PATIENT_STR)) {
				if (nextEntry.getMethod() == RequestTypeEnum.POST && isBlank(nextEntry.getConditionalUrl())) {
					if (nextEntry.getFullUrl() != null && nextEntry.getFullUrl().startsWith("urn:uuid:")) {
						String newId = UUID.randomUUID().toString();
						nextEntry.getResource().setId(newId);
						idSubstitutions.put(nextEntry.getFullUrl(), "Patient/" + newId);

						IBase entry = rawEntries.get(i);
						IBase request = terser.getValues(entry, "request").get(0);
						terser.setElement(request, "ifNoneExist", null);
						terser.setElement(request, "method", "PUT");
						terser.setElement(request, "url", "Patient/" + newId);
					}
				} else if (nextEntry.getMethod() == RequestTypeEnum.PUT
						&& isNotBlank(nextEntry.getFullUrl())
						&& isNotBlank(nextEntry.getUrl())
						&& isBlank(nextEntry.getConditionalUrl())
						&& !Strings.CS.equals(nextEntry.getFullUrl(), nextEntry.getUrl())) {
					idSubstitutions.put(nextEntry.getFullUrl(), nextEntry.getUrl());
				}
			}
		}

		if (!idSubstitutions.isEmpty()) {
			for (BundleEntryParts entry : parsedEntries) {
				IBaseResource resource = entry.getResource();
				if (resource != null) {
					List<ResourceReferenceInfo> references = terser.getAllResourceReferences(resource);
					for (ResourceReferenceInfo reference : references) {
						String referenceString = reference
								.getResourceReference()
								.getReferenceElement()
								.getValue();
						String substitution = idSubstitutions.get(referenceString);
						if (substitution != null) {
							reference.getResourceReference().setReference(substitution);
						}
					}
				}
			}
		}

		List<BundleEntryParts> entries = BundleUtil.toListOfEntries(myFhirContext, theBundle);
		Map<String, IBaseResource> placeholderToResource = new HashMap<>();
		for (BundleEntryParts nextEntry : entries) {
			String fullUrl = nextEntry.getFullUrl();
			if (fullUrl != null && fullUrl.startsWith("urn:uuid:")) {
				if (nextEntry.getResource() != null) {
					placeholderToResource.put(fullUrl, nextEntry.getResource());
				}
			}
		}

		if (theRequestDetails != null) {
			theRequestDetails.getUserData().put(PLACEHOLDER_TO_REFERENCE_KEY, placeholderToResource);
		}
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
