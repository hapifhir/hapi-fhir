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
import org.hl7.fhir.r4.model.CarePlan;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.IntegerType;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Organization;
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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static ca.uhn.fhir.jpa.provider.ReplaceReferencesSvcImpl.RESOURCE_TYPES_SYSTEM;
import static ca.uhn.fhir.rest.api.Constants.HEADER_PREFER;
import static ca.uhn.fhir.rest.api.Constants.HEADER_PREFER_RESPOND_ASYNC;
import static ca.uhn.fhir.rest.server.provider.ProviderConstants.HAPI_BATCH_JOB_ID_SYSTEM;
import static ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_REPLACE_REFERENCES;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ReplaceReferencesTestHelper {
	private static final Logger ourLog = LoggerFactory.getLogger(ReplaceReferencesTestHelper.class);

	static final Identifier pat1IdentifierA =
			new Identifier().setSystem("SYS1A").setValue("VAL1A");
	static final Identifier pat1IdentifierB =
			new Identifier().setSystem("SYS1B").setValue("VAL1B");
	static final Identifier pat2IdentifierA =
			new Identifier().setSystem("SYS2A").setValue("VAL2A");
	static final Identifier pat2IdentifierB =
			new Identifier().setSystem("SYS2B").setValue("VAL2B");
	static final Identifier patBothIdentifierC =
			new Identifier().setSystem("SYSC").setValue("VALC");
	public static final int TOTAL_EXPECTED_PATCHES = 23;
	public static final int SMALL_BATCH_SIZE = 5;
	public static final int EXPECTED_SMALL_BATCHES = (TOTAL_EXPECTED_PATCHES + SMALL_BATCH_SIZE - 1) / SMALL_BATCH_SIZE;
	private final IFhirResourceDaoPatient<Patient> myPatientDao;
	private final IFhirResourceDao<Task> myTaskDao;
	private final IFhirResourceDao<Organization> myOrganizationDao;
	private final IFhirResourceDao<Encounter> myEncounterDao;
	private final IFhirResourceDao<CarePlan> myCarePlanDao;
	private final IFhirResourceDao<Observation> myObservationDao;
	private final IFhirResourceDao<Provenance> myProvenanceDao;
	private final IFhirResourceDao<Practitioner> myPractitionerDao;

	private IIdType myOrgId;
	private IIdType mySourcePatientId;
	private IIdType mySourceCarePlanId;
	private IIdType mySourceEncId1;
	private IIdType mySourceEncId2;
	private ArrayList<IIdType> mySourceObsIds;
	private IIdType myTargetPatientId;
	private IIdType myTargetEnc1;

	private final FhirContext myFhirContext;
	private final SystemRequestDetails mySrd = new SystemRequestDetails();
	private final IParser myJsonParser;

	public ReplaceReferencesTestHelper(FhirContext theFhirContext, DaoRegistry theDaoRegistry) {
		myFhirContext = theFhirContext;
		myPatientDao = (IFhirResourceDaoPatient<Patient>) theDaoRegistry.getResourceDao(Patient.class);
		myTaskDao = theDaoRegistry.getResourceDao(Task.class);
		myOrganizationDao = theDaoRegistry.getResourceDao(Organization.class);
		myEncounterDao = theDaoRegistry.getResourceDao(Encounter.class);
		myCarePlanDao = theDaoRegistry.getResourceDao(CarePlan.class);
		myObservationDao = theDaoRegistry.getResourceDao(Observation.class);
		myProvenanceDao = theDaoRegistry.getResourceDao(Provenance.class);
		myPractitionerDao = theDaoRegistry.getResourceDao(Practitioner.class);
		myJsonParser = myFhirContext.newJsonParser();
	}

	public void beforeEach() {

		Organization org = new Organization();
		org.setName("an org");
		myOrgId = myOrganizationDao.create(org, mySrd).getId().toUnqualifiedVersionless();
		ourLog.info("OrgId: {}", myOrgId);

		Patient patient1 = new Patient();
		patient1.getManagingOrganization().setReferenceElement(myOrgId);
		patient1.addIdentifier(pat1IdentifierA);
		patient1.addIdentifier(pat1IdentifierB);
		patient1.addIdentifier(patBothIdentifierC);
		mySourcePatientId = myPatientDao.create(patient1, mySrd).getId().toUnqualifiedVersionless();

		Patient patient2 = new Patient();
		patient2.addIdentifier(pat2IdentifierA);
		patient2.addIdentifier(pat2IdentifierB);
		patient2.addIdentifier(patBothIdentifierC);
		patient2.getManagingOrganization().setReferenceElement(myOrgId);
		myTargetPatientId = myPatientDao.create(patient2, mySrd).getId().toUnqualifiedVersionless();

		Encounter enc1 = new Encounter();
		enc1.setStatus(Encounter.EncounterStatus.CANCELLED);
		enc1.getSubject().setReferenceElement(mySourcePatientId);
		enc1.getServiceProvider().setReferenceElement(myOrgId);
		mySourceEncId1 = myEncounterDao.create(enc1, mySrd).getId().toUnqualifiedVersionless();

		Encounter enc2 = new Encounter();
		enc2.setStatus(Encounter.EncounterStatus.ARRIVED);
		enc2.getSubject().setReferenceElement(mySourcePatientId);
		enc2.getServiceProvider().setReferenceElement(myOrgId);
		mySourceEncId2 = myEncounterDao.create(enc2, mySrd).getId().toUnqualifiedVersionless();

		CarePlan carePlan = new CarePlan();
		carePlan.setStatus(CarePlan.CarePlanStatus.ACTIVE);
		carePlan.getSubject().setReferenceElement(mySourcePatientId);
		mySourceCarePlanId = myCarePlanDao.create(carePlan, mySrd).getId().toUnqualifiedVersionless();

		Encounter targetEnc1 = new Encounter();
		targetEnc1.setStatus(Encounter.EncounterStatus.ARRIVED);
		targetEnc1.getSubject().setReferenceElement(myTargetPatientId);
		targetEnc1.getServiceProvider().setReferenceElement(myOrgId);
		this.myTargetEnc1 = myEncounterDao.create(targetEnc1, mySrd).getId().toUnqualifiedVersionless();

		mySourceObsIds = new ArrayList<>();
		for (int i = 0; i < 20; i++) {
			Observation obs = new Observation();
			obs.getSubject().setReferenceElement(mySourcePatientId);
			obs.setStatus(Observation.ObservationStatus.FINAL);
			IIdType obsId = myObservationDao.create(obs, mySrd).getId().toUnqualifiedVersionless();
			mySourceObsIds.add(obsId);
		}
	}

	public void setSourceAndTarget(PatientMergeInputParameters inParams) {
		inParams.sourcePatient = new Reference().setReferenceElement(mySourcePatientId);
		inParams.targetPatient = new Reference().setReferenceElement(myTargetPatientId);
	}

	public Patient createResultPatient(boolean theDeleteSource) {
		Patient resultPatient = new Patient();
		resultPatient.setIdElement((IdType) myTargetPatientId);
		resultPatient.addIdentifier(pat1IdentifierA);
		if (!theDeleteSource) {
			// add the link only if we are not deleting the source
			Patient.PatientLinkComponent link = resultPatient.addLink();
			link.setOther(new Reference(mySourcePatientId));
			link.setType(Patient.LinkType.REPLACES);
		}
		return resultPatient;
	}

	public Patient readSourcePatient() {
		return readPatient(mySourcePatientId);
	}

	public Patient readTargetPatient() {
		return readPatient(myTargetPatientId);
	}

	public Patient readPatient(IIdType thePatientId) {
		return myPatientDao.read(thePatientId, mySrd);
	}

	public IIdType getTargetPatientId() {
		return myTargetPatientId;
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
			String theExpectedSourcePatientVersion,
			String theExpectedTargetPatientVersion,
			@Nullable List<IProvenanceAgent> theExpectedProvenanceAgents) {
		List<IBaseResource> provenances =
				searchProvenance(myTargetPatientId.toVersionless().getIdPart());
		assertThat(provenances).hasSize(1);
		Provenance provenance = (Provenance) provenances.get(0);

		// assert targets
		int expectedNumberOfProvenanceTargets = TOTAL_EXPECTED_PATCHES + 2;
		assertThat(provenance.getTarget()).hasSize(expectedNumberOfProvenanceTargets);
		// the first target reference should be the target patient
		String targetPatientReferenceInProvenance =
				provenance.getTarget().get(0).getReference();
		assertThat(targetPatientReferenceInProvenance)
				.isEqualTo(myTargetPatientId
						.withVersion(theExpectedTargetPatientVersion)
						.toString());
		// the second target reference should be the source patient
		String sourcePatientReference = provenance.getTarget().get(1).getReference();
		assertThat(sourcePatientReference)
				.isEqualTo(mySourcePatientId
						.withVersion(theExpectedSourcePatientVersion)
						.toString());

		Set<String> allActualTargets = extractResourceIdsFromProvenanceTarget(provenance.getTarget());
		assertThat(allActualTargets).containsAll(getExpectedProvenanceTargetsForPatchedResources());

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
			boolean theDeleteSource, @Nullable List<IProvenanceAgent> theExpectedProvenanceAgent) {
		assertMergeProvenance(
				theDeleteSource,
				mySourcePatientId.withVersion("2"),
				myTargetPatientId.withVersion("2"),
				TOTAL_EXPECTED_PATCHES,
				getExpectedProvenanceTargetsForPatchedResources(),
				theExpectedProvenanceAgent);
	}

	public void assertMergeProvenance(
			boolean theDeleteSource,
			IIdType theSourcePatientIdWithExpectedVersion,
			IIdType theTargetPatientIdWithExpectedVersion,
			int theExpectedPatches,
			Set<String> theExpectedProvenanceTargetsForPatchedResources,
			@Nullable List<IProvenanceAgent> theExpectedProvenanceAgents) {

		List<IBaseResource> provenances = searchProvenance(
				theTargetPatientIdWithExpectedVersion.toVersionless().getIdPart());
		assertThat(provenances).hasSize(1);
		Provenance provenance = (Provenance) provenances.get(0);

		// assert targets
		int expectedNumberOfProvenanceTargets = theExpectedPatches;
		// target patient and source patient if not deleted
		expectedNumberOfProvenanceTargets += theDeleteSource ? 1 : 2;
		assertThat(provenance.getTarget()).hasSize(expectedNumberOfProvenanceTargets);
		// the first target reference should be the target patient
		String targetPatientReferenceInProvenance =
				provenance.getTarget().get(0).getReference();
		assertThat(targetPatientReferenceInProvenance).isEqualTo(theTargetPatientIdWithExpectedVersion.toString());
		if (!theDeleteSource) {
			// the second target reference should be the source patient, if it wasn't deleted
			String sourcePatientReference = provenance.getTarget().get(1).getReference();
			assertThat(sourcePatientReference).isEqualTo(theSourcePatientIdWithExpectedVersion.toString());
		}

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

	private Set<String> getExpectedProvenanceTargetsForPatchedResources() {
		Set<String> allExpectedTargets = new HashSet<>();

		allExpectedTargets.add(mySourceEncId1.withVersion("2").toString());
		allExpectedTargets.add(mySourceEncId2.withVersion("2").toString());
		allExpectedTargets.add(mySourceCarePlanId.withVersion("2").toString());
		allExpectedTargets.addAll(mySourceObsIds.stream()
				.map(obsId -> obsId.withVersion("2").toString())
				.toList());
		return allExpectedTargets;
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

	public Parameters callReplaceReferences(IGenericClient theFhirClient, boolean theIsAsync) {
		return callReplaceReferencesWithResourceLimit(theFhirClient, theIsAsync, null);
	}

	public Parameters callReplaceReferences(
			IGenericClient theFhirClient, String theSourceId, String theTargetId, boolean theIsAsync) {
		return callReplaceReferencesWithResourceLimit(theFhirClient, theSourceId, theTargetId, theIsAsync, null);
	}

	public Parameters callReplaceReferencesWithResourceLimit(
			IGenericClient theFhirClient, boolean theIsAsync, Integer theResourceLimit) {
		return callReplaceReferencesWithResourceLimit(
				theFhirClient,
				mySourcePatientId.getValue(),
				myTargetPatientId.getValue(),
				theIsAsync,
				theResourceLimit);
	}

	public Parameters callReplaceReferencesWithResourceLimit(
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

	public void assertAllReferencesUpdated() {
		assertAllReferencesUpdated(false);
	}

	public void assertAllReferencesUpdated(boolean theWithDelete) {

		Set<IIdType> actual = getEverythingResourceIds(myTargetPatientId);

		ourLog.info("Found IDs: {}", actual);

		if (theWithDelete) {
			assertThat(actual).doesNotContain(mySourcePatientId);
		}
		assertThat(actual).contains(mySourceEncId1);
		assertThat(actual).contains(mySourceEncId2);
		assertThat(actual).contains(myOrgId);
		assertThat(actual).contains(mySourceCarePlanId);
		assertThat(actual).containsAll(mySourceObsIds);
		assertThat(actual).contains(myTargetPatientId);
		assertThat(actual).contains(myTargetEnc1);
	}

	public void assertNothingChanged() {
		assertReferencesToTargetPatientAreAsInitiallyCreated();
		assertReferencesToSourcePatientAreAsInitiallyCreated();
	}

	public void assertReferencesToTargetPatientAreAsInitiallyCreated() {
		Set<IIdType> actualIds = getEverythingResourceIds(myTargetPatientId);
		ourLog.info("Found IDs for target $everything : {}", actualIds);

		// Validate that all expected resources are present and reference the target patient
		assertThat(actualIds).contains(myTargetPatientId);
		assertThat(actualIds).contains(myTargetEnc1);
		assertThat(actualIds).contains(myOrgId);

		assertThat(actualIds).doesNotContain(mySourcePatientId);
		assertThat(actualIds).doesNotContain(mySourceEncId1);
		assertThat(actualIds).doesNotContain(mySourceEncId2);
		assertThat(actualIds).doesNotContain(mySourceCarePlanId);
		assertThat(actualIds).doesNotContainAnyElementsOf(mySourceObsIds);
	}

	public void assertReferencesToSourcePatientAreAsInitiallyCreated() {
		Set<IIdType> actualIds = getEverythingResourceIds(mySourcePatientId);
		ourLog.info("Found IDs for source $everything : {}", actualIds);

		// Validate that all expected resources are present and reference the source patient
		assertThat(actualIds).contains(mySourcePatientId);
		assertThat(actualIds).contains(mySourceEncId1);
		assertThat(actualIds).contains(mySourceEncId2);
		assertThat(actualIds).contains(myOrgId);
		assertThat(actualIds).contains(mySourceCarePlanId);
		assertThat(actualIds).containsAll(mySourceObsIds);

		assertThat(actualIds).doesNotContain(myTargetPatientId);
		assertThat(actualIds).doesNotContain(myTargetEnc1);
	}

	public PatientMergeInputParameters buildMultipleTargetMatchParameters(
			boolean theWithDelete, boolean theWithInputResultPatient, boolean theWithPreview) {
		PatientMergeInputParameters inParams = new PatientMergeInputParameters();
		inParams.sourcePatient = new Reference().setReferenceElement(mySourcePatientId);
		inParams.targetPatientIdentifier = patBothIdentifierC;
		inParams.deleteSource = theWithDelete;
		if (theWithInputResultPatient) {
			inParams.resultPatient = createResultPatient(theWithDelete);
		}
		if (theWithPreview) {
			inParams.preview = true;
		}
		return inParams;
	}

	public PatientMergeInputParameters buildMultipleSourceMatchParameters(
			boolean theWithDelete, boolean theWithInputResultPatient, boolean theWithPreview) {
		PatientMergeInputParameters inParams = new PatientMergeInputParameters();
		inParams.sourcePatientIdentifier = patBothIdentifierC;
		inParams.targetPatient = new Reference().setReferenceElement(mySourcePatientId);
		inParams.deleteSource = theWithDelete;
		if (theWithInputResultPatient) {
			inParams.resultPatient = createResultPatient(theWithDelete);
		}
		if (theWithPreview) {
			inParams.preview = true;
		}
		return inParams;
	}

	public IIdType getSourcePatientId() {
		return mySourcePatientId;
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

		public Parameters asParametersResource() {
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

	public List<Identifier> getExpectedIdentifiersForTargetAfterMerge(boolean theWithInputResultPatient) {

		List<Identifier> expectedIdentifiersOnTargetAfterMerge;
		if (theWithInputResultPatient) {
			expectedIdentifiersOnTargetAfterMerge =
					List.of(new Identifier().setSystem("SYS1A").setValue("VAL1A"));
		} else {
			// the identifiers copied over from source should be marked as old
			expectedIdentifiersOnTargetAfterMerge = List.of(
					new Identifier().setSystem("SYS2A").setValue("VAL2A"),
					new Identifier().setSystem("SYS2B").setValue("VAL2B"),
					new Identifier().setSystem("SYSC").setValue("VALC"),
					new Identifier().setSystem("SYS1A").setValue("VAL1A").copy().setUse(Identifier.IdentifierUse.OLD),
					new Identifier().setSystem("SYS1B").setValue("VAL1B").copy().setUse(Identifier.IdentifierUse.OLD));
		}
		return expectedIdentifiersOnTargetAfterMerge;
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
	 * Asserts that the test source patient which has been created in beforeEach() has been updated properly or deleted
	 * after a merge operation with the test target patient, which was also created in beforeEach().
	 */
	public void assertSourcePatientUpdatedOrDeletedAfterMerge(boolean theDeleteSource) {
		assertSourcePatientUpdatedOrDeletedAfterMerge(getSourcePatientId(), getTargetPatientId(), theDeleteSource);
	}

	/**
	 * Asserts that the test target patient which has been created in beforeEach() has been updated properly
	 * after a merge operation with the test source patient, which was also created in beforeEach().
	 */
	public void assertTargetPatientUpdatedAfterMerge(boolean withDelete, List<Identifier> theExpectedIdentifiers) {
		assertTargetPatientUpdatedAfterMerge(
				getTargetPatientId(), getSourcePatientId(), withDelete, theExpectedIdentifiers);
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
