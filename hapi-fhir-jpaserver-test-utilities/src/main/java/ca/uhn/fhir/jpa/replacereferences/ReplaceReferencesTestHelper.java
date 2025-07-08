/*-
 * #%L
 * HAPI FHIR JPA Server Test Utilities
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.replacereferences;

import ca.uhn.fhir.batch2.jobs.replacereferences.ReplaceReferenceResultsJson;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoPatient;
import ca.uhn.fhir.jpa.api.dao.PatientEverythingParameters;
import ca.uhn.fhir.jpa.interceptor.ex.ProvenanceAgentTestInterceptor;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.model.api.IProvenanceAgent;
import ca.uhn.fhir.model.api.ProvenanceAgent;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.gclient.IOperationUntypedWithInput;
import ca.uhn.fhir.rest.gclient.IOperationUntypedWithInputAndPartialOutput;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import ca.uhn.fhir.util.JsonUtil;
import jakarta.annotation.Nullable;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.IntegerType;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Period;
import org.hl7.fhir.r4.model.Practitioner;
import org.hl7.fhir.r4.model.Provenance;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.Task;
import org.hl7.fhir.r4.model.Type;
import org.junit.jupiter.api.Assertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static ca.uhn.fhir.jpa.provider.ReplaceReferencesSvcImpl.RESOURCE_TYPES_SYSTEM;
import static ca.uhn.fhir.rest.api.Constants.HEADER_PREFER;
import static ca.uhn.fhir.rest.api.Constants.HEADER_PREFER_RESPOND_ASYNC;
import static ca.uhn.fhir.rest.server.provider.ProviderConstants.HAPI_BATCH_JOB_ID_SYSTEM;
import static ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_MERGE;
import static ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_REPLACE_REFERENCES;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ReplaceReferencesTestHelper {
	private static final Logger ourLog = LoggerFactory.getLogger(ReplaceReferencesTestHelper.class);

	private final IFhirResourceDaoPatient<Patient> myPatientDao;
	private final IFhirResourceDao<Task> myTaskDao;
	private final IFhirResourceDao<Provenance> myProvenanceDao;
	private final IFhirResourceDao<Practitioner> myPractitionerDao;

	private final FhirContext myFhirContext;
	private final SystemRequestDetails mySrd = new SystemRequestDetails();
	private final IParser myJsonParser;

	public ReplaceReferencesTestHelper(FhirContext theFhirContext, DaoRegistry theDaoRegistry) {
		myFhirContext = theFhirContext;
		myPatientDao = (IFhirResourceDaoPatient<Patient>) theDaoRegistry.getResourceDao(Patient.class);
		myTaskDao = theDaoRegistry.getResourceDao(Task.class);
		myProvenanceDao = theDaoRegistry.getResourceDao(Provenance.class);
		myPractitionerDao = theDaoRegistry.getResourceDao(Practitioner.class);
		myJsonParser = myFhirContext.newJsonParser();
	}

	public void setSourceAndTarget(PatientMergeInputParameters inParams, ReplaceReferencesLargeTestData theTestData) {
		inParams.sourcePatient = new Reference().setReferenceElement(theTestData.getSourcePatientId());
		inParams.targetPatient = new Reference().setReferenceElement(theTestData.getTargetPatientId());
	}

	public Patient readPatient(IIdType thePatientId) {
		return myPatientDao.read(thePatientId, mySrd);
	}

	public ProvenanceAgent createTestProvenanceAgent() {
		ProvenanceAgent provenanceAgent = new ProvenanceAgent();
		IIdType whoPractitionerId = createPractitioner();
		provenanceAgent.setWho(new Reference(whoPractitionerId.toUnqualifiedVersionless()));
		Identifier identifier =
				new Identifier().setSystem("http://example.com/practitioner").setValue("the-practitioner-identifier");
		provenanceAgent.setOnBehalfOf(new Reference().setIdentifier(identifier));
		return provenanceAgent;
	}

	public List<IBaseResource> searchProvenance(String theTargetId) {
		SearchParameterMap map = new SearchParameterMap();
		map.add("target", new ReferenceParam(theTargetId));
		IBundleProvider searchBundle = myProvenanceDao.search(map, mySrd);
		return searchBundle.getAllResources();
	}

	public void assertReplaceReferencesProvenance(
			String theExpectedSourceResourceVersion,
			String theExpectedTargetResourceVersion,
			ReplaceReferencesLargeTestData theTestData,
			@Nullable List<IProvenanceAgent> theExpectedProvenanceAgents) {
		assertReplaceReferencesProvenance(
				theTestData.getSourcePatientId().withVersion(theExpectedSourceResourceVersion),
				theTestData.getTargetPatientId().withVersion(theExpectedTargetResourceVersion),
				ReplaceReferencesLargeTestData.TOTAL_EXPECTED_PATCHES,
				theTestData.getExpectedProvenanceTargetsForPatchedResources(),
				theExpectedProvenanceAgents);
	}

	public void assertReplaceReferencesProvenance(
			IIdType theSourceResourceIdWithExpectedVersion,
			IIdType theTargetResourceIdWithExpectedVersion,
			int theExpectedNumberOfPatches,
			Set<String> theExpectedPatchedResourceTargetReferences,
			@Nullable List<IProvenanceAgent> theExpectedProvenanceAgents) {

		List<IBaseResource> provenances = searchProvenance(theTargetResourceIdWithExpectedVersion.getIdPart());
		assertThat(provenances).hasSize(1);
		Provenance provenance = (Provenance) provenances.get(0);

		// assert targets
		int expectedNumberOfProvenanceTargets = theExpectedNumberOfPatches + 2;
		assertThat(provenance.getTarget()).hasSize(expectedNumberOfProvenanceTargets);

		// the first target reference should be the target patient
		String targetPatientReferenceInProvenance =
				provenance.getTarget().get(0).getReference();
		assertThat(targetPatientReferenceInProvenance).isEqualTo(theTargetResourceIdWithExpectedVersion.toString());

		// the second target reference should be the source patient
		String sourcePatientReference = provenance.getTarget().get(1).getReference();
		assertThat(sourcePatientReference).isEqualTo(theSourceResourceIdWithExpectedVersion.toString());

		Set<String> allActualTargets = extractResourceIdsFromProvenanceTarget(provenance.getTarget());
		assertThat(allActualTargets).containsAll(theExpectedPatchedResourceTargetReferences);

		validateAgents(theExpectedProvenanceAgents, provenance);

		Instant now = Instant.now();
		Instant oneMinuteAgo = now.minus(1, ChronoUnit.MINUTES);
		assertThat(provenance.getRecorded()).isBetween(oneMinuteAgo, now);

		Period period = provenance.getOccurredPeriod();
		assertThat(period.getStart()).isBefore(period.getEnd());
		assertThat(period.getStart()).isBetween(oneMinuteAgo, now);
		assertThat(period.getEnd()).isEqualTo(provenance.getRecorded());

		// validate provenance.reason
		assertThat(provenance.getReason()).hasSize(1);
		Coding reasonCoding = provenance.getReason().get(0).getCodingFirstRep();
		assertThat(reasonCoding).isNotNull();
		assertThat(reasonCoding.getSystem()).isEqualTo("http://terminology.hl7.org/CodeSystem/v3-ActReason");
		assertThat(reasonCoding.getCode()).isEqualTo("PATADMIN");

		// validate provenance.activity
		Coding activityCoding = provenance.getActivity().getCodingFirstRep();
		assertThat(activityCoding).isNotNull();
		assertThat(activityCoding.getSystem()).isEqualTo("http://terminology.hl7.org/CodeSystem/iso-21089-lifecycle");
		assertThat(activityCoding.getCode()).isEqualTo("link");
	}

	public void assertMergeProvenance(
			boolean theDeleteSource,
			ReplaceReferencesLargeTestData theTestData,
			@Nullable List<IProvenanceAgent> theExpectedProvenanceAgent) {
		assertMergeProvenance(
				theDeleteSource,
				theTestData.getSourcePatientId().withVersion("2"),
				theTestData.getTargetPatientId().withVersion("2"),
				ReplaceReferencesLargeTestData.TOTAL_EXPECTED_PATCHES,
				theTestData.getExpectedProvenanceTargetsForPatchedResources(),
				theExpectedProvenanceAgent);
	}

	public void assertMergeProvenance(
			boolean theDeleteSource,
			IIdType theSourcePatientIdWithExpectedVersion,
			IIdType theTargetPatientIdWithExpectedVersion,
			int theExpectedPatches,
			Set<String> theExpectedProvenanceTargetsForPatchedResources,
			@Nullable List<IProvenanceAgent> theExpectedProvenanceAgents) {

		List<IBaseResource> provenances = searchProvenance(theTargetPatientIdWithExpectedVersion.getIdPart());
		assertThat(provenances).hasSize(1);
		Provenance provenance = (Provenance) provenances.get(0);

		// assert targets
		int expectedNumberOfProvenanceTargets = theExpectedPatches;
		// target patient and source patient
		expectedNumberOfProvenanceTargets += 2;
		assertThat(provenance.getTarget()).hasSize(expectedNumberOfProvenanceTargets);
		// the first target reference should be the target patient
		String targetPatientReferenceInProvenance =
				provenance.getTarget().get(0).getReference();
		assertThat(targetPatientReferenceInProvenance).isEqualTo(theTargetPatientIdWithExpectedVersion.toString());
		// the second target reference should be the source patient
		String sourcePatientReference = provenance.getTarget().get(1).getReference();
		assertThat(sourcePatientReference).isEqualTo(theSourcePatientIdWithExpectedVersion.toString());

		Set<String> allActualTargets = extractResourceIdsFromProvenanceTarget(provenance.getTarget());
		assertThat(allActualTargets).containsAll(theExpectedProvenanceTargetsForPatchedResources);

		validateAgents(theExpectedProvenanceAgents, provenance);

		Instant now = Instant.now();
		Instant oneMinuteAgo = now.minus(1, ChronoUnit.MINUTES);
		assertThat(provenance.getRecorded()).isBetween(oneMinuteAgo, now);

		Period period = provenance.getOccurredPeriod();
		assertThat(period.getStart()).isBefore(period.getEnd());
		assertThat(period.getStart()).isBetween(oneMinuteAgo, now);
		assertThat(period.getEnd()).isEqualTo(provenance.getRecorded());

		// validate provenance.reason
		assertThat(provenance.getReason()).hasSize(1);
		Coding reasonCoding = provenance.getReason().get(0).getCodingFirstRep();
		assertThat(reasonCoding).isNotNull();
		assertThat(reasonCoding.getSystem()).isEqualTo("http://terminology.hl7.org/CodeSystem/v3-ActReason");
		assertThat(reasonCoding.getCode()).isEqualTo("PATADMIN");

		// validate provenance.activity
		Coding activityCoding = provenance.getActivity().getCodingFirstRep();
		assertThat(activityCoding).isNotNull();
		assertThat(activityCoding.getSystem()).isEqualTo("http://terminology.hl7.org/CodeSystem/iso-21089-lifecycle");
		assertThat(activityCoding.getCode()).isEqualTo("merge");
	}

	private Set<IIdType> getEverythingResourceIds(IIdType thePatientId) {
		PatientEverythingParameters everythingParams = new PatientEverythingParameters();
		everythingParams.setCount(new IntegerType(100));

		IBundleProvider bundleProvider =
				myPatientDao.patientInstanceEverything(null, mySrd, everythingParams, thePatientId);

		assertNull(bundleProvider.getNextPageId());

		return bundleProvider.getAllResources().stream()
				.map(IBaseResource::getIdElement)
				.map(IIdType::toUnqualifiedVersionless)
				.collect(Collectors.toSet());
	}

	public String getJobIdFromTask(Task task) {
		assertThat(task.getIdentifier())
				.hasSize(1)
				.element(0)
				.extracting(Identifier::getSystem)
				.isEqualTo(HAPI_BATCH_JOB_ID_SYSTEM);

		return task.getIdentifierFirstRep().getValue();
	}

	public Parameters callReplaceReferences(
			IGenericClient theFhirClient, IIdType theSourceId, IIdType theTargetId, boolean theIsAsync) {
		return callReplaceReferences(theFhirClient, theSourceId.toString(), theTargetId.toString(), theIsAsync, null);
	}

	public Parameters callReplaceReferences(
			IGenericClient theFhirClient,
			IIdType theSourceId,
			IIdType theTargetId,
			boolean theIsAsync,
			Integer theResourceLimit) {
		return callReplaceReferences(
				theFhirClient, theSourceId.toString(), theTargetId.toString(), theIsAsync, theResourceLimit);
	}

	public Parameters callReplaceReferences(
			IGenericClient theFhirClient,
			String theSourceId,
			String theTargetId,
			boolean theIsAsync,
			Integer theResourceLimit) {
		IOperationUntypedWithInputAndPartialOutput<Parameters> request = theFhirClient
				.operation()
				.onServer()
				.named(OPERATION_REPLACE_REFERENCES)
				.withParameter(
						Parameters.class,
						ProviderConstants.OPERATION_REPLACE_REFERENCES_PARAM_SOURCE_REFERENCE_ID,
						new StringType(theSourceId))
				.andParameter(
						ProviderConstants.OPERATION_REPLACE_REFERENCES_PARAM_TARGET_REFERENCE_ID,
						new StringType(theTargetId));
		if (theResourceLimit != null) {
			request.andParameter(
					ProviderConstants.OPERATION_REPLACE_REFERENCES_RESOURCE_LIMIT, new IntegerType(theResourceLimit));
		}

		if (theIsAsync) {
			request.withAdditionalHeader(HEADER_PREFER, HEADER_PREFER_RESPOND_ASYNC);
		}

		return request.returnResourceType(Parameters.class).execute();
	}

	public void assertAllReferencesUpdated(ReplaceReferencesLargeTestData theTestData) {
		assertAllReferencesUpdated(false, false, theTestData);
	}

	public void assertAllReferencesUpdated(
			boolean theIsMerge, boolean theWithDelete, ReplaceReferencesLargeTestData theTestData) {

		assertReferencesToTargetAfterOperation(theIsMerge, theWithDelete, theTestData);
		assertReferencesToSourceAfterOperation(theIsMerge, theWithDelete, theTestData);
	}

	private void assertReferencesToTargetAfterOperation(
			boolean theIsMerge, boolean theWithDelete, ReplaceReferencesLargeTestData theTestData) {
		Set<IIdType> actualTargetEverythingIds = getEverythingResourceIds(theTestData.getTargetPatientId());

		ourLog.info("Target Everything Ids: {}", actualTargetEverythingIds);

		// target should now be referenced by all resources that initially referenced source
		assertThat(actualTargetEverythingIds).containsAll(theTestData.getResourceIdsInitiallyReferencingSource());
		// everything operation returns the resources that are referenced by target as well,
		// they should be present in the $everything output
		assertThat(actualTargetEverythingIds).containsAll(theTestData.getResourceIdsInitiallyReferencedByTarget());
		// the resources that initially referenced target should still reference it
		assertThat(actualTargetEverythingIds).containsAll(theTestData.getResourceIdsInitiallyReferencingTarget());

		int expectedTargetEvertghingCount = theTestData
						.getResourceIdsInitiallyReferencingSource()
						.size()
				+ theTestData.getResourceIdsInitiallyReferencedByTarget().size()
				+ theTestData.getResourceIdsInitiallyReferencingTarget().size()
				+ 2; // +1 for the target resource itself and +1 for the Provenance resource created after the operation

		if (theIsMerge && !theWithDelete) {
			// if this is a merge and the source is not deleted,
			// the source and target will reference each other via REPLACED_BY and REPLACES,
			// so the source patient ID should be in the target $everything
			assertThat(actualTargetEverythingIds).contains(theTestData.getSourcePatientId());
			expectedTargetEvertghingCount++;
		} else {
			assertThat(actualTargetEverythingIds).doesNotContain(theTestData.getSourcePatientId());
		}

		assertThat(actualTargetEverythingIds).hasSize(expectedTargetEvertghingCount);
	}

	private void assertReferencesToSourceAfterOperation(
			boolean theIsMerge, boolean theWithDelete, ReplaceReferencesLargeTestData theTestData) {
		if (theWithDelete) {
			// source resource would be deleted, no need to check references
			return;
		}
		Set<IIdType> actualSourceEverythingIds = getEverythingResourceIds(theTestData.getSourcePatientId());

		ourLog.info("Source Everything Ids: {}", actualSourceEverythingIds);

		// the resources that initially referenced source should now reference target, so source $everything
		// should not contain them anymore
		assertThat(actualSourceEverythingIds)
				.doesNotContainAnyElementsOf(theTestData.getResourceIdsInitiallyReferencingSource());
		// $everything returns the resources that are referenced by source as well, they should be unchanged
		assertThat(actualSourceEverythingIds).containsAll(theTestData.getResourceIdsInitiallyReferencedBySource());
		int expectedSourceEverythingCount =
				theTestData.getResourceIdsInitiallyReferencedBySource().size()
						+ 2; // +1 for the source resource itself and +1 for the Provenance resource created after the
		// operation
		if (theIsMerge) {
			// If this is a merge, the source patient will have reference to the target patient
			assertThat(actualSourceEverythingIds).contains(theTestData.getTargetPatientId());
			expectedSourceEverythingCount++;
		}
		assertThat(actualSourceEverythingIds).hasSize(expectedSourceEverythingCount);
	}

	public void assertReferencesHaveNotChanged(ReplaceReferencesLargeTestData theTestData) {
		assertTargetReferencesHaveNotChanged(theTestData);
		assertSourceReferencesHaveNotChanged(theTestData);
	}

	public void assertTargetReferencesHaveNotChanged(ReplaceReferencesLargeTestData theTestData) {
		Set<IIdType> actualTargetEverythingIds = getEverythingResourceIds(theTestData.getTargetPatientId());
		ourLog.info("Found IDs for target $everything : {}", actualTargetEverythingIds);

		assertThat(actualTargetEverythingIds).containsAll(theTestData.getResourceIdsInitiallyReferencingTarget());
		assertThat(actualTargetEverythingIds).containsAll(theTestData.getResourceIdsInitiallyReferencedByTarget());
		assertThat(actualTargetEverythingIds).contains(theTestData.getTargetPatientId());

		assertThat(actualTargetEverythingIds).doesNotContain(theTestData.getSourcePatientId());
		assertThat(actualTargetEverythingIds)
				.doesNotContainAnyElementsOf(theTestData.getResourceIdsInitiallyReferencingSource());
	}

	public void assertSourceReferencesHaveNotChanged(ReplaceReferencesLargeTestData theTestData) {
		Set<IIdType> actualIds = getEverythingResourceIds(theTestData.getSourcePatientId());
		ourLog.info("Found IDs for source $everything : {}", actualIds);

		assertThat(actualIds).containsAll(theTestData.getResourceIdsInitiallyReferencingSource());
		assertThat(actualIds).containsAll(theTestData.getResourceIdsInitiallyReferencedBySource());
		assertThat(actualIds).contains(theTestData.getSourcePatientId());

		assertThat(actualIds).doesNotContain(theTestData.getTargetPatientId());
		assertThat(actualIds).doesNotContainAnyElementsOf(theTestData.getResourceIdsInitiallyReferencingTarget());
	}

	public PatientMergeInputParameters buildMultipleTargetMatchParameters(
			boolean theWithDelete,
			boolean theWithInputResultPatient,
			boolean theWithPreview,
			ReplaceReferencesLargeTestData theTestData) {
		PatientMergeInputParameters inParams = new PatientMergeInputParameters();
		inParams.sourcePatient = new Reference().setReferenceElement(theTestData.getSourcePatientId());
		inParams.targetPatientIdentifier = theTestData.getIdentifierCommonToBothResources();
		inParams.deleteSource = theWithDelete;
		if (theWithInputResultPatient) {
			inParams.resultPatient = theTestData.createResultPatientInput(theWithDelete);
		}
		if (theWithPreview) {
			inParams.preview = true;
		}
		return inParams;
	}

	public PatientMergeInputParameters buildMultipleSourceMatchParameters(
			boolean theWithDelete,
			boolean theWithInputResultPatient,
			boolean theWithPreview,
			ReplaceReferencesLargeTestData theTestData) {
		PatientMergeInputParameters inParams = new PatientMergeInputParameters();
		inParams.sourcePatientIdentifier = theTestData.getIdentifierCommonToBothResources();
		inParams.targetPatient = new Reference().setReferenceElement(theTestData.getTargetPatientId());
		inParams.deleteSource = theWithDelete;
		if (theWithInputResultPatient) {
			inParams.resultPatient = theTestData.createResultPatientInput(theWithDelete);
		}
		if (theWithPreview) {
			inParams.preview = true;
		}
		return inParams;
	}

	public Parameters callMergeOperation(IGenericClient theClient, Parameters inParameters, boolean isAsync) {
		IOperationUntypedWithInput<Parameters> request =
				theClient.operation().onType("Patient").named(OPERATION_MERGE).withParameters(inParameters);

		if (isAsync) {
			request.withAdditionalHeader(HEADER_PREFER, HEADER_PREFER_RESPOND_ASYNC);
		}

		return request.returnResourceType(Parameters.class).execute();
	}

	public Parameters callUndoMergeOperation(IGenericClient theClient, Parameters inParameters) {
		IOperationUntypedWithInput<Parameters> request = theClient
				.operation()
				.onType("Patient")
				.named("$hapi.fhir.undo-merge")
				.withParameters(inParameters);

		return request.returnResourceType(Parameters.class).execute();
	}

	public static class PatientMergeInputParameters {
		public Type sourcePatient;
		public Type sourcePatientIdentifier;
		public Type targetPatient;
		public Type targetPatientIdentifier;
		public Patient resultPatient;
		public Boolean preview;
		public Boolean deleteSource;
		public Integer resourceLimit;

		private Parameters asCommonParameters() {
			Parameters inParams = new Parameters();
			if (sourcePatient != null) {
				inParams.addParameter().setName("source-patient").setValue(sourcePatient);
			}
			if (sourcePatientIdentifier != null) {
				inParams.addParameter().setName("source-patient-identifier").setValue(sourcePatientIdentifier);
			}
			if (targetPatient != null) {
				inParams.addParameter().setName("target-patient").setValue(targetPatient);
			}
			if (targetPatientIdentifier != null) {
				inParams.addParameter().setName("target-patient-identifier").setValue(targetPatientIdentifier);
			}
			return inParams;
		}

		public Parameters asParametersResource() {
			Parameters inParams = asCommonParameters();
			if (resultPatient != null) {
				inParams.addParameter().setName("result-patient").setResource(resultPatient);
			}
			if (preview != null) {
				inParams.addParameter().setName("preview").setValue(new BooleanType(preview));
			}
			if (deleteSource != null) {
				inParams.addParameter().setName("delete-source").setValue(new BooleanType(deleteSource));
			}
			if (resourceLimit != null) {
				inParams.addParameter().setName("batch-size").setValue(new IntegerType(resourceLimit));
			}
			return inParams;
		}

		public Parameters asUndoParametersResource() {
			return asCommonParameters();
		}
	}

	public static void validatePatchResultBundle(
			Bundle patchResultBundle, int theTotalExpectedPatches, List<String> theExpectedResourceTypes) {
		String resourceMatchString = "(" + String.join("|", theExpectedResourceTypes) + ")";
		Pattern expectedPatchIssuePattern =
				Pattern.compile("Successfully patched resource \"" + resourceMatchString + "/\\d+/_history/\\d+\".");
		assertThat(patchResultBundle.getEntry())
				.hasSize(theTotalExpectedPatches)
				.allSatisfy(entry -> assertThat(entry.getResponse().getOutcome())
						.isInstanceOf(OperationOutcome.class)
						.extracting(OperationOutcome.class::cast)
						.extracting(OperationOutcome::getIssue)
						.satisfies(issues -> assertThat(issues)
								.hasSize(1)
								.element(0)
								.extracting(OperationOutcome.OperationOutcomeIssueComponent::getDiagnostics)
								.satisfies(
										diagnostics -> assertThat(diagnostics).matches(expectedPatchIssuePattern))));
	}

	public Bundle validateCompletedTask(JobInstance theJobInstance, IIdType theTaskId) {
		validateJobReport(theJobInstance, theTaskId);

		Bundle patchResultBundle;
		Task taskWithOutput = myTaskDao.read(theTaskId, mySrd);
		assertThat(taskWithOutput.getStatus()).isEqualTo(Task.TaskStatus.COMPLETED);
		ourLog.info(
				"Complete Task: {}",
				myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(taskWithOutput));

		Task.TaskOutputComponent taskOutput = taskWithOutput.getOutputFirstRep();

		// Assert on the output type
		Coding taskType = taskOutput.getType().getCodingFirstRep();
		assertEquals(RESOURCE_TYPES_SYSTEM, taskType.getSystem());
		assertEquals("Bundle", taskType.getCode());

		List<Resource> containedResources = taskWithOutput.getContained();
		assertThat(containedResources).hasSize(1).element(0).isInstanceOf(Bundle.class);

		Bundle containedBundle = (Bundle) containedResources.get(0);

		Reference outputRef = (Reference) taskOutput.getValue();
		patchResultBundle = (Bundle) outputRef.getResource();
		//		ourLog.info("containedBundle: {}",
		// myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(containedBundle));
		//		ourLog.info("patchResultBundle: {}",
		// myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(patchResultBundle));
		assertTrue(containedBundle.equalsDeep(patchResultBundle));
		return patchResultBundle;
	}

	public IIdType createPractitioner() {
		Practitioner practitioner = new Practitioner();
		practitioner.setActive(true);
		return myPractitionerDao.create(practitioner, mySrd).getId();
	}

	private void validateJobReport(JobInstance theJobInstance, IIdType theTaskId) {
		String report = theJobInstance.getReport();
		ReplaceReferenceResultsJson replaceReferenceResultsJson =
				JsonUtil.deserialize(report, ReplaceReferenceResultsJson.class);
		IdDt resultTaskId = replaceReferenceResultsJson.getTaskId().asIdDt();
		assertEquals(theTaskId.getIdPart(), resultTaskId.getIdPart());
	}

	/**
	 * Asserts that the source patient with the specified ID has been updated properly or deleted
	 * after merging with the specified target patient ID.
	 */
	public void assertSourcePatientUpdatedOrDeletedAfterMerge(
			IIdType theSourcePatientId, IIdType theTargetPatientId, boolean theDeleteSource) {
		if (theDeleteSource) {
			assertThrows(ResourceGoneException.class, () -> readPatient(theSourcePatientId.toUnqualifiedVersionless()));
		} else {
			Patient source = readPatient(theSourcePatientId.toUnqualifiedVersionless());
			assertThat(source.getLink()).hasSize(1);
			Patient.PatientLinkComponent link = source.getLink().get(0);
			assertThat(link.getOther().getReferenceElement()).isEqualTo(theTargetPatientId.toUnqualifiedVersionless());
			assertThat(link.getType()).isEqualTo(Patient.LinkType.REPLACEDBY);
		}
	}

	/**
	 *  Asserts that the target patient with the given ID has been updated properly after
	 *  a merging  with the given source patient ID
	 */
	public void assertTargetPatientUpdatedAfterMerge(
			IIdType targetPatientId,
			IIdType sourcePatientId,
			boolean withDelete,
			List<Identifier> theExpectedIdentifiers) {
		Patient target = myPatientDao.read(targetPatientId.toUnqualifiedVersionless(), mySrd);
		if (!withDelete) {
			assertThat(target.getLink()).hasSize(1);
			Patient.PatientLinkComponent link = target.getLink().get(0);
			assertThat(link.getOther().getReferenceElement()).isEqualTo(sourcePatientId.toUnqualifiedVersionless());
			assertThat(link.getType()).isEqualTo(Patient.LinkType.REPLACES);
		}
		// assertExpected Identifiers found on the target
		assertIdentifiers(target.getIdentifier(), theExpectedIdentifiers);
	}

	public void assertIdentifiers(List<Identifier> theActualIdentifiers, List<Identifier> theExpectedIdentifiers) {
		assertThat(theActualIdentifiers).hasSize(theExpectedIdentifiers.size());
		for (int i = 0; i < theExpectedIdentifiers.size(); i++) {
			Identifier expectedIdentifier = theExpectedIdentifiers.get(i);
			Identifier actualIdentifier = theActualIdentifiers.get(i);
			assertThat(expectedIdentifier.equalsDeep(actualIdentifier)).isTrue();
		}
	}

	/**
	 * Registers a {@link ProvenanceAgentTestInterceptor} on the given server.
	 * The interceptor will return the given list of provenance agents when its hook is called
	 */
	public static void registerProvenanceAgentInterceptor(
			RestfulServer theServer, List<IProvenanceAgent> theProvenanceAgents) {
		ProvenanceAgentTestInterceptor agentInterceptor = new ProvenanceAgentTestInterceptor(theProvenanceAgents);
		theServer.getInterceptorService().registerInterceptor(agentInterceptor);
	}

	private static Set<String> extractResourceIdsFromProvenanceTarget(List<Reference> theTargets) {
		return theTargets.stream()
				.map(Reference::getReference)
				.map(IdDt::new)
				.map(IdDt::toString)
				.collect(Collectors.toSet());
	}

	private void assertEqualReferences(IBaseReference theRef1, IBaseReference theRef2) {
		// xor checks if one is null and the other is not
		if (theRef1 == null ^ theRef2 == null) {
			Assertions.fail(
					"One of the references is null but the other is not - ref1: " + theRef1 + ", ref2: " + theRef2);
		}
		if (theRef1 == theRef2) {
			// handles the case where both are null as well
			return;
		}
		assertThat(myJsonParser.encodeToString(theRef1)).isEqualTo(myJsonParser.encodeToString(theRef2));
	}

	private void validateAgent(IProvenanceAgent theExpectedAgent, Provenance.ProvenanceAgentComponent theActualAgent) {
		Reference theActualWho = theActualAgent.hasWho() ? theActualAgent.getWho() : null;
		Reference theActualOnBehalfOf = theActualAgent.hasOnBehalfOf() ? theActualAgent.getOnBehalfOf() : null;
		assertEqualReferences(theActualWho, theExpectedAgent.getWho());
		assertEqualReferences(theActualOnBehalfOf, theExpectedAgent.getOnBehalfOf());
	}

	private void validateAgents(@Nullable List<IProvenanceAgent> theExpectedAgents, Provenance theProvenance) {
		if (theExpectedAgents == null) {
			assertThat(theProvenance.hasAgent()).isFalse();
			return;
		}
		var actualAgents = theProvenance.getAgent();
		assertThat(actualAgents).hasSameSizeAs(theExpectedAgents);
		for (int i = 0; i < theExpectedAgents.size(); i++) {
			validateAgent(theExpectedAgents.get(i), actualAgents.get(i));
		}
	}
}
